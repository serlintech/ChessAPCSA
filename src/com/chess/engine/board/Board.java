package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.pieces.Bishop;
import com.chess.engine.pieces.*;
import com.chess.engine.player.Player;
import com.chess.engine.player.bPlayer;
import com.chess.engine.player.wPlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;


public class Board {
    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;
    private final wPlayer whitePlayer;
    private final bPlayer blackPlayer;
    private final Player currentPlayer;

    public Player getWhitePlayer(){
        return this.whitePlayer;
    }
    public Player getBlackPlayer(){
        return this.blackPlayer;
    }

    private Board(Builder builder) {
        this.gameBoard= createGameBoard(builder);
        this.whitePieces = calcActivePieces(this.gameBoard, Alliance.WHITE);
        this.blackPieces = calcActivePieces(this.gameBoard, Alliance.BLACK);

        final Collection<Move> whiteLegalMoves = calcLegalMoves(this.whitePieces);
        final Collection<Move> blackLegalMoves = calcLegalMoves(this.blackPieces);
        this.whitePlayer=new wPlayer(this, whiteLegalMoves, blackLegalMoves);
        this.blackPlayer= new bPlayer(this, whiteLegalMoves, blackLegalMoves);
        this.currentPlayer=builder.nextMoveGen.choosePlayer(this.whitePlayer, this.blackPlayer);
    }
    @Override
    public String toString(){
        final StringBuilder builder = new StringBuilder();
        for(int i =0; i< BoardUtils.TILES; i++){
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s",tileText));
            if((i+1)%BoardUtils.TILES_PER_ROW==0){
                builder.append("\n");
            }
        }
        return builder.toString();
    }


    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    private Collection<Move> calcLegalMoves(Collection<Piece>pieces) {
        final List<Move> legalMoves =new ArrayList<>();

        for(final Piece piece : pieces){
            legalMoves.addAll(piece.legalMoves(this));
        }
        return ImmutableList.copyOf(legalMoves);
    }


    private Collection<Piece> calcActivePieces(List<Tile> gameBoard, Alliance alliance) {

        final List<Piece> activePieces = new ArrayList<>();
        for(final Tile tile: gameBoard){
            if(tile.isTileOccupied()){
                final Piece piece = tile.getPiece();
                if(piece.getPieceAlliance()== alliance){
                    activePieces.add(piece);
                }
            }
        }
        return activePieces;
    }
    public Collection<Piece> getBlackPieces(){
        return this.blackPieces;

    }
    public Collection<Piece> getWhitePieces(){
        return this.whitePieces;

    }



    private static List<Tile> createGameBoard(final Builder builder){
        final Tile[] tiles = new Tile[BoardUtils.TILES];
        for(int i=0; i<BoardUtils.TILES;i++){
            tiles[i]=Tile.createTile(i, builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }
    public static Board createInitialBoard(){           //Creates initial chess board
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Rook(Alliance.BLACK,0));
        builder.setPiece(new Knight(Alliance.BLACK, 1));
        builder.setPiece(new Bishop(Alliance.BLACK, 2));
        builder.setPiece(new Queen(Alliance.BLACK, 3));
        builder.setPiece(new King(Alliance.BLACK, 4));
        builder.setPiece(new Bishop(Alliance.BLACK, 5));
        builder.setPiece(new Knight(Alliance.BLACK, 6));
        builder.setPiece(new Rook(Alliance.BLACK, 7));
        builder.setPiece(new Pawn(Alliance.BLACK, 8));
        builder.setPiece(new Pawn(Alliance.BLACK, 9));
        builder.setPiece(new Pawn(Alliance.BLACK, 10));
        builder.setPiece(new Pawn(Alliance.BLACK, 11));
        builder.setPiece(new Pawn(Alliance.BLACK, 12));
        builder.setPiece(new Pawn(Alliance.BLACK, 13));
        builder.setPiece(new Pawn(Alliance.BLACK, 14));
        builder.setPiece(new Pawn(Alliance.BLACK, 15));
        // White Layout
        builder.setPiece(new Pawn(Alliance.WHITE, 48));
        builder.setPiece(new Pawn(Alliance.WHITE, 49));
        builder.setPiece(new Pawn(Alliance.WHITE, 50));
        builder.setPiece(new Pawn(Alliance.WHITE, 51));
        builder.setPiece(new Pawn(Alliance.WHITE, 52));
        builder.setPiece(new Pawn(Alliance.WHITE, 53));
        builder.setPiece(new Pawn(Alliance.WHITE, 54));
        builder.setPiece(new Pawn(Alliance.WHITE, 55));
        builder.setPiece(new Rook(Alliance.WHITE, 56));
        builder.setPiece(new Knight(Alliance.WHITE, 57));
        builder.setPiece(new Bishop(Alliance.WHITE, 58));
        builder.setPiece(new Queen(Alliance.WHITE, 59));
        builder.setPiece(new King(Alliance.WHITE, 60));
        builder.setPiece(new Bishop(Alliance.WHITE, 61));
        builder.setPiece(new Knight(Alliance.WHITE, 62));
        builder.setPiece(new Rook(Alliance.WHITE, 63));
        //white to move
        builder.setMoveGen(Alliance.WHITE);
        //build the board
        return builder.build();
    }
    public Tile getTile(final int tileCoor){

        return gameBoard.get(tileCoor);
    }

    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(),
                this.getBlackPlayer().getLegalMoves()));
    }


    public static class Builder{


        Map<Integer, Piece> boardConfig;
        Alliance nextMoveGen;
        Pawn enPassantPawn;

        public Builder(){
            this.boardConfig= new HashMap<>();
        }
        public Builder setPiece(final Piece piece){
            this.boardConfig.put(piece.getPiecePos(),piece);
            return this;
        }
        public Builder setMoveGen(final Alliance nextMoveGen){
            this.nextMoveGen = nextMoveGen;
            return this;
        }

        public Board build(){
            return new Board(this);

        }

        public void setEnPassantPawn(Pawn movedPawn) {
            this.enPassantPawn = enPassantPawn;
        }
    }
}
