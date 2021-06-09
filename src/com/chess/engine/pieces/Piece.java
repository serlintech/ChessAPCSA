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
    Piece(final PieceType pieceType, final Alliance pieceAlliance,final int piecePos){
        this.pieceType=pieceType;
        this.pieceAlliance=pieceAlliance;
        this.piecePos=piecePos;
        this.isFirstMove=false;
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
