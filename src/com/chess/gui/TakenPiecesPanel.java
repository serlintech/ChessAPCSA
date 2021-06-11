package com.chess.gui;

import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;
import com.google.common.primitives.Ints;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static com.chess.gui.Table.*;

public class TakenPiecesPanel extends JPanel {
    private final JPanel topPanel;
    private final JPanel bottomPanel;
    private static final Dimension TAKEN_PIECES_DIM = new Dimension(40,80);
    private static final Color PANEL_COLOR = Color.decode("0xFDFE6");
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
    public TakenPiecesPanel(){
        super(new BorderLayout());
        setBackground(Color.decode("#7597d1"));
        setBorder(PANEL_BORDER);
        this.topPanel = new JPanel(new GridLayout(8,2));
        this.bottomPanel= new JPanel(new GridLayout(8,2));
        this.topPanel.setBackground(PANEL_COLOR);
        this.bottomPanel.setBackground(PANEL_COLOR);
        add(this.topPanel, BorderLayout.NORTH);
        add(this.bottomPanel, BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECES_DIM);
    }
    public void redo(final MoveLog moveLog){
        bottomPanel.removeAll();
        topPanel.removeAll();

        final List<Piece> whiteTakenPieces = new ArrayList<>();
        final List<Piece> blackTakenPieces= new ArrayList<>();
        for(final Move move : moveLog.getMoves()){
            if(move.isAttackMove()){
                final Piece takenPiece = move.getIncidentPiece();
                if(takenPiece.getPieceAlliance().isWhite()){
                    whiteTakenPieces.add(takenPiece);
;                }else if(takenPiece.getPieceAlliance().isBlack()){
                    blackTakenPieces.add(takenPiece);
                }else{
                    throw new RuntimeException("Should not reach here... piece neither black nor white.");
                }
            }

        }
        Collections.sort(whiteTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceVal(),o2.getPieceVal());
            }
        });
        Collections.sort(blackTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceVal(),o2.getPieceVal());
            }
        });
        for(final Piece takenPiece : whiteTakenPieces){
            try{
                final BufferedImage image = ImageIO.read(new File("chessPieces/"+
                        takenPiece.getPieceAlliance().toString().substring(0,1)+
                        " "+takenPiece.toString()));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel();
                this.bottomPanel.add(imageLabel);
            }catch (final IOException e){
                e.printStackTrace();;

            }
        }
        for(final Piece takenPiece : blackTakenPieces){
            try{
                final BufferedImage image = ImageIO.read(new File("chessPieces/"+
                        takenPiece.getPieceAlliance().toString().substring(0,1)+
                        ""+takenPiece.toString()));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel();
                this.bottomPanel.add(imageLabel);
            }catch (final IOException e){
                e.printStackTrace();;

            }
        }
        validate();
    }


}
