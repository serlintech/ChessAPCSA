package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
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
            int candidateDest = this.piecePos + (this.getPieceAlliance().getDirection() * currentCandidate);

            if(!BoardUtils.isValidTileCoor(candidateDest)){
                continue;
            }
            if(currentCandidate == 8 && !board.getTile(candidateDest).isTileOccupied()){
                //have to make pawn move type
                legalMoves.add(new Move.MajorMove(board, this, candidateDest));
            }
        }
        return legalMoves;
    }

}
