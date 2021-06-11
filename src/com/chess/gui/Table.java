package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.Player;
import com.chess.engine.player.ai.MiniMax;
import com.chess.engine.player.ai.MoveStrategy;
import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table extends Observable {
    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
    private final static Dimension BOARD_PANEL_DIM = new Dimension(400,350);
    private final static Dimension TILE_PANEL_DIM = new Dimension(10,10);
    private final GameHistoryPanel gameHistoryPanel;
    private final TakenPiecesPanel takenPiecesPanel;
    private BoardDirection boardDirection;
    private Tile sourceTile, destTile;
    private Piece humanMovedPiece;
    private final MoveLog moveLog;
    private Move compMove;

    private static String defaultPieceImgPath = "chessPieces/";
    private final JFrame game;
    private Board chessBoard;
    private final BoardPanel boardPanel;
    private boolean highlightLegalMoves;
    private Color lightTile = Color.decode("#FFFACD");
    private Color darkTile = Color.decode("#593E1A");
    private final GameSetup gameSetup;
    private static final Table INSTANCE_TABLE= new Table();

    private Table(){
        this.game= new JFrame("APCSA CHESS");
        this.game.setLayout(new BorderLayout());
        final JMenuBar tableMenu = createTableMenu();
        this.game.setJMenuBar(tableMenu);
        this.chessBoard = Board.createInitialBoard();
        this.game.setSize(OUTER_FRAME_DIMENSION);
        this.boardDirection = BoardDirection.NORMAL;
        this.highlightLegalMoves=false;
        this.boardPanel=new BoardPanel();
        this.addObserver(new TableAIObs());
        this.game.add(this.boardPanel, BorderLayout.CENTER);
        this.gameHistoryPanel = new GameHistoryPanel();
        this.takenPiecesPanel = new TakenPiecesPanel();
        this.game.add(this.takenPiecesPanel, BorderLayout.WEST);
        this.game.add(this.gameHistoryPanel, BorderLayout.EAST);
        this.game.setVisible(true);
        this.moveLog= new MoveLog();
        this.gameSetup=new GameSetup(this.game, true);

    }
    public static Table get(){
        return INSTANCE_TABLE;
    }
    private Board getBoard(){
        return chessBoard;
    }
    public void updateBoard(final Board board){
        this.chessBoard=board;
    }
    public void updateCompMove(final Move move){
        this.compMove = move;
    }
    private MoveLog getMoveLog(){
        return this.moveLog;
    }
    private GameHistoryPanel getGameHistory(){
        return this.gameHistoryPanel;
    }
    private TakenPiecesPanel getTakenPieces(){
        return this.takenPiecesPanel;
    }
    private BoardPanel getBoardPanel(){
        return this.boardPanel;
    }
    private void moveMadeUpdater(final PlayerType playerType){
        setChanged();
        notifyObservers(playerType);
    }


    private JMenuBar createTableMenu(){
        final JMenuBar tableMenu = new JMenuBar();
        tableMenu.add(createFileMenu());
        tableMenu.add(genPreferenceMenu());
        tableMenu.add(createOptionsGen());
        return tableMenu;
    }
    private JMenu createOptionsGen(){
        final JMenu optionsMenu = new JMenu("Options");
        final JMenuItem setupGameMenu = new JMenuItem("Setup Game", KeyEvent.VK_S);
        setupGameMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Table.get().getGameSetup().promptUser();
                Table.get().setupUpdate(Table.get().getGameSetup());
            }
        });
        optionsMenu.add(setupGameMenu);
        return optionsMenu;

    }
    public void start(){
        Table.get().getMoveLog().clear();
        Table.get().getGameHistory().redo(chessBoard,Table.get().getMoveLog());
        Table.get().getTakenPieces().redo(Table.get().getMoveLog());
        Table.get().getBoardPanel().drawBoard(Table.get().getBoard());
    }
    private GameSetup getGameSetup(){
        return this.gameSetup;
    }
    private void  setupUpdate(final GameSetup gameSetup){
        setChanged();
        notifyObservers(gameSetup);
    }
    public static class TableAIObs implements Observer{
        @Override
        public void update(final Observable o, final Object z){
            if(Table.get().getGameSetup().isAIPlayer(Table.get().getBoard().getCurrentPlayer())&&
            !Table.get().getBoard().getCurrentPlayer().isInCheckMate()&&
            !Table.get().getBoard().getCurrentPlayer().isInStaleMate()){
                //execute ai work
                final AIBrain thinkTank = new AIBrain();
                thinkTank.execute();
            }
            if(Table.get().getBoard().getCurrentPlayer().isInCheckMate()){
                System.out.println("Game Ove,maroney "+Table.get().getBoard().getCurrentPlayer()+" is In CheckMate!");
            }
            if(Table.get().getBoard().getCurrentPlayer().isInStaleMate()){
                System.out.println("Game over, "+Table.get().getBoard().getCurrentPlayer()+" is in StaleMate.");
            }
        }

        private static class AIBrain extends SwingWorker<Move, String>{
            private AIBrain(){

            }
            @Override
            protected Move doInBackground() throws Exception{
                final MoveStrategy miniMax = new MiniMax(4);
                final Move superiorMove = miniMax.execute(Table.get().getBoard());
                return superiorMove;
            }
            @Override
            public void done(){
                try {
                    final Move superiorMove = get();
                    Table.get().updateCompMove(superiorMove);
                    Table.get().updateBoard(Table.get().getBoard().getCurrentPlayer().makeMove(superiorMove).getEndBoard());
                    Table.get().getMoveLog().addMove(superiorMove);
                    Table.get().getGameHistory().redo(Table.get().getBoard(), Table.get().getMoveLog());
                    Table.get().getTakenPieces().redo(Table.get().getMoveLog());
                    Table.get().getBoardPanel().drawBoard(Table.get().getBoard());
                    Table.get().moveMadeUpdater(PlayerType.COMPUTER);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private JMenu createFileMenu(){
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Would open pgn");
            }
        });
        fileMenu.add(openPGN);

        final JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitItem);
        return fileMenu;
    }
    private class BoardPanel extends JPanel{
        final List<TilePanel> boardTiles;
        BoardPanel(){
            super(new GridLayout(8,8));
            this.boardTiles= new ArrayList<>();
            for(int i=0; i< BoardUtils.TILES;i++){
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIM);
            validate();
        }

        public void drawBoard(Board board) {
            removeAll();
            for(final TilePanel tilePanel : boardDirection.traverse(boardTiles)){
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }
    public enum BoardDirection{
        NORMAL{
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED{
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return Lists.reverse(boardTiles);
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles) ;

        abstract BoardDirection opposite();

    }
    private JMenu genPreferenceMenu(){
        final JMenu preferenceMenu = new JMenu("Preferences");
        final JMenuItem flipBoardItem = new JMenuItem("Flip Board");
        flipBoardItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);

            }
        });
        preferenceMenu.add(flipBoardItem);

        preferenceMenu.addSeparator();
        final JCheckBoxMenuItem highlightMoveCheck = new JCheckBoxMenuItem("Highlight legal moves", false);
        highlightMoveCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlightLegalMoves = highlightMoveCheck.isSelected();
            }
        });
        preferenceMenu.add(highlightMoveCheck);
        return preferenceMenu;
    }
    public static class MoveLog{
        private final List<Move> moves;
        MoveLog(){
            this.moves=new ArrayList<>();
        }
        public List<Move> getMoves(){
            return this.moves;
        }
        public void addMove(final Move move){
            this.moves.add(move);
        }
        public int size(){
            return this.moves.size();
        }
        public void clear(){
            this.moves.clear();
        }
        public Move removeMove(int index){
            return this.moves.remove(index);
        }
        public boolean removeMove(final Move move){
            return this.moves.remove(move);
        }

    }

    private class TilePanel extends JPanel{
        private final int tileID;
        TilePanel(final BoardPanel boardPanel,
                  final int tileID){
            super(new GridBagLayout());
            this.tileID=tileID;
            setPreferredSize(TILE_PANEL_DIM);
            assignTileColor();
            assignGUITilePiece(chessBoard);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if(isRightMouseButton(e)) {
                        sourceTile = null;
                        destTile = null;
                        humanMovedPiece = null;

                    }else if (isLeftMouseButton(e)) {
                        if (sourceTile == null) {
                            sourceTile = chessBoard.getTile(tileID);
                            humanMovedPiece = sourceTile.getPiece();
                            if (humanMovedPiece == null) {
                                sourceTile = null;
                            }
                        }else{
                            destTile = chessBoard.getTile(tileID);
                            final Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getTileCoor(), destTile.getTileCoor());
                            final MoveTransition transition = chessBoard.getCurrentPlayer().makeMove(move);
                            if(transition.getMoveStatus().isDone()){
                                chessBoard=transition.getEndBoard();
                                moveLog.addMove(move);
                            }
                            sourceTile = null;
                            //destTile = null;
                            humanMovedPiece=null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                gameHistoryPanel.redo(chessBoard,moveLog);
                                takenPiecesPanel.redo(moveLog);
                                if(gameSetup.isAIPlayer(chessBoard.getCurrentPlayer())){
                                    Table.get().moveMadeUpdater(PlayerType.HUMAN);
                                }
                                boardPanel.drawBoard(chessBoard);
                            }
                        });

                    }

                }

                @Override
                public void mousePressed(final MouseEvent e) {

                }

                @Override
                public void mouseReleased(final MouseEvent e) {

                }

                @Override
                public void mouseEntered(final MouseEvent e) {

                }

                @Override
                public void mouseExited(final MouseEvent e) {

                }
            });
            validate();
        }
        public void drawTile(final Board board){
            assignTileColor();
            assignGUITilePiece(board);
            highlightLegals(board);
            validate();
            repaint();
        }
        private void highlightLegals(final Board board){
            if(highlightLegalMoves){
                for(final Move move : pieceLegalMoves(board)){
                    if(move.getDestCoor()==this.tileID){
                        try{
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("chessPieces/dot.png")))));
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        private Collection<Move> pieceLegalMoves(final Board board){
            if(humanMovedPiece != null && humanMovedPiece.getPieceAlliance()==board.getCurrentPlayer().getAlliance()){
                return humanMovedPiece.legalMoves(board);
            }
            return Collections.emptyList();
        }

        private void assignGUITilePiece(final Board board){
            this.removeAll();
            if(board.getTile(this.tileID).isTileOccupied()){

                try {
                    System.out.println("Parsing... " + defaultPieceImgPath + board.getTile(this.tileID).getPiece().getPieceAlliance().toString().substring(0,1) +
                            board.getTile(this.tileID).getPiece().toString()+ ".png");
                    final BufferedImage image =
                            ImageIO.read(new File(defaultPieceImgPath + board.getTile(this.tileID).getPiece().getPieceAlliance().toString().substring(0,1) +
                                    board.getTile(this.tileID).getPiece().toString()+ ".png"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        private void assignTileColor() {
            if (BoardUtils.FIRST_ROW[this.tileID] ||
                    BoardUtils.THIRD_ROW[this.tileID] ||
                    BoardUtils.FIFTH_ROW[this.tileID] ||
                    BoardUtils.SEVENTH_ROW[this.tileID]){
                setBackground(this.tileID % 2 == 0 ? lightTile : darkTile);
            }else if(BoardUtils.SECOND_ROW[this.tileID] ||
                    BoardUtils.FOURTH_ROW[this.tileID]||
                    BoardUtils.SIXTH_ROW[this.tileID] ||
                    BoardUtils.EIGHTH_ROW[this.tileID]){
                setBackground(this.tileID % 2 != 0 ? lightTile : darkTile);
            }

        }

    }
    enum PlayerType{
        HUMAN,
        COMPUTER
    }

}
