package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.*;

public abstract class Piece {
    protected final int piecePos;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    Piece(final Alliance pieceAlliance,final int piecePos){
        this.pieceAlliance=pieceAlliance;
        this.piecePos=piecePos;
        this.isFirstMove=false;
    }
    public abstract Collection<Move> legalMoves(final Board board);
    public Alliance getPieceAlliance(){
        return pieceAlliance;

    }
    public boolean isFirstMove(){
        return this.isFirstMove;
    }
    public int getPiecePos(){
        return piecePos;
    }
    public enum PieceType{
        PAWN("P"),
        KNIGHT("N"),
        BISHOP("B"),
        ROOK("R"),
        QUEEN("Q"),
        KING("K");

        private String pieceName;
        PieceType(final String pieceName){
            this.pieceName=pieceName;
        }
        @Override
        public String toString(){
            return this.pieceName;
        }
    }
}
