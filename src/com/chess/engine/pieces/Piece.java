package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.*;

public abstract class Piece {
    protected final int piecePos;
    protected final Alliance pieceAlliance;

    Piece(final int piecePos, final Alliance pieceAlliance){
        this.pieceAlliance=pieceAlliance;
        this.piecePos=piecePos;
    }
    public abstract Collection<Move> legalMoves(final Board board);
    public Alliance getPieceAlliance(){
        return pieceAlliance;
    }
}
