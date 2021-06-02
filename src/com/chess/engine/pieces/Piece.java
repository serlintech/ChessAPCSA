package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.*;

public abstract class Piece {
    protected final PieceType pieceType;
    protected final int piecePos;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    Piece(final PieceType pieceType, final Alliance pieceAlliance,final int piecePos){
        this.pieceType=pieceType;
        this.pieceAlliance=pieceAlliance;
        this.piecePos=piecePos;
        this.isFirstMove=false;
    }
    public abstract Collection<Move> legalMoves(final Board board);
    public Alliance getPieceAlliance(){
        return pieceAlliance;

    }
    public PieceType getPieceType(){
        return this.pieceType;
    }
    public boolean isFirstMove(){
        return this.isFirstMove;
    }
    public int getPiecePos(){
        return piecePos;
    }
    public enum PieceType{
        PAWN("P"){
            @Override
            public boolean isKing() {
                return false;
            }
        },
        KNIGHT("N") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        BISHOP("B") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        QUEEN("Q") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }
        };

        private String pieceName;
        PieceType(final String pieceName){
            this.pieceName=pieceName;
        }
        @Override
        public String toString(){
            return this.pieceName;
        }
        public abstract boolean isKing();
    }
}
