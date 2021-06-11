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
    private final int cachedHashCode;
    Piece(final PieceType pieceType, final Alliance pieceAlliance,final int piecePos, final boolean isFirstMove){
        this.pieceType=pieceType;
        this.pieceAlliance=pieceAlliance;
        this.piecePos=piecePos;
        this.isFirstMove=isFirstMove;
        this.cachedHashCode = computeHashCode();
    }
    private int computeHashCode(){
        int result = pieceType.hashCode();
        result = 31* result + pieceAlliance.hashCode();
        result = 31 * result + piecePos;
        result = 31 * result + (isFirstMove ? 1 :0);
        return result;
    }

    public abstract Piece movePiece(Move move);
    public abstract Collection<Move> legalMoves(final Board board);
    public Alliance getPieceAlliance(){
        return pieceAlliance;

    }
    @Override
    public boolean equals(final Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Piece)){
            return false;
        }
        final Piece otherPiece = (Piece) other; //we know it is a piece by this point
        return piecePos == otherPiece.getPiecePos() && pieceType == otherPiece.getPieceType() &&
                pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove();
    }
    @Override
    public int hashCode(){
        return this.cachedHashCode;

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

    public int getPieceVal(){
        return this.pieceType.getPieceVal();
    }

    public enum PieceType{
        PAWN(100,"P"){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT(300,"N") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP(300,"B") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK(500,"R") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN(900,"Q") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING(1000,"K") {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };

        private String pieceName;
        private int pieceVal;
        PieceType(final int pieceVal, final String pieceName){
            this.pieceName=pieceName;
            this.pieceVal = pieceVal;
        }
        @Override
        public String toString(){
            return this.pieceName;
        }
        public abstract boolean isKing();

        public abstract boolean isRook();

        public int getPieceVal(){
            return this.pieceVal;
        }
    }
}
