package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class Pawn extends Piece{
    private final static int[] moveCandidates= {8,16,7 ,9};



    public Pawn(Alliance pieceAlliance, int piecePos) {

        super(PieceType.PAWN, pieceAlliance, piecePos,true);
    }
    public Pawn(Alliance pieceAlliance, int piecePos, final boolean isFirstMove) {

        super(PieceType.PAWN, pieceAlliance, piecePos,isFirstMove);
    }
    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getMovePiece().getPieceAlliance(), move.getDestCoor());
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
                legalMoves.add(new MajorMove(board, this, candidateDest));
            }else if(currentCandidate == 16 && this.isFirstMove() &&
                    ((BoardUtils.SECOND_ROW[this.piecePos] && this.getPieceAlliance().isBlack()) ||
                    (BoardUtils.SEVENTH_ROW[this.piecePos ]&& this.pieceAlliance.isWhite()))) {

                    final int behindCandidate = this.piecePos + (this.pieceAlliance.getDirection()*8);
                    if(!board.getTile(behindCandidate).isTileOccupied() &&
                            !board.getTile(candidateDest).isTileOccupied()){
                        legalMoves.add(new PawnJumpMove(board, this, candidateDest));
                    }

            }else if(currentCandidate==7 &&
                !(BoardUtils.EIGHTH_COLUMN[this.piecePos] && this.getPieceAlliance().isWhite()) ||
                    (BoardUtils.FIRST_COLUMN[this.piecePos] && this.pieceAlliance.isBlack()) ){
                if(board.getTile(candidateDest).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDest).getPiece();
                    if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                        legalMoves.add(new PawnOffensiveMove(board, this, candidateDest,pieceOnCandidate));
                    }
                }
            }else if(currentCandidate == 9 &&
                    !(BoardUtils.FIRST_COLUMN[this.piecePos] && this.getPieceAlliance().isWhite()) ||
                    (BoardUtils.EIGHTH_COLUMN[this.piecePos] && this.pieceAlliance.isBlack()) ){
                if(board.getTile(candidateDest).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDest).getPiece();
                    if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                        legalMoves.add(new PawnOffensiveMove(board, this, candidateDest,pieceOnCandidate));
                    }
                }
            }
        }


        return ImmutableList.copyOf(legalMoves);
    }
    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }

}
