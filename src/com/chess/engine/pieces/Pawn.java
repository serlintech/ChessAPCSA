package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece{
    private final static int[] moveCandidates= {8};



    Pawn(int piecePos, Alliance pieceAlliance) {
        super(piecePos, pieceAlliance);
    }

    @Override
    public Collection<Move> legalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>();
        for(final int currentCandidate : moveCandidates){
            int candidateDest = this.piecePos + currentCandidate;
        }
    }

}
