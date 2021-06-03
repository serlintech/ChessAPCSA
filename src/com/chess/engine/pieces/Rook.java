package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Rook extends Piece{
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-8, -1, 1, 8};

    public Rook(Alliance pieceAlliance,int piecePosition){
        super(PieceType.ROOK,pieceAlliance, piecePosition);
    }

    @Override
    public Collection<Move> legalMoves(Board board){
        final List<Move> legalMoves = new ArrayList<>();
        for(final int candidateCoordinateOffset: CANDIDATE_MOVE_VECTOR_COORDINATES){

            int moveDestCoor = this.piecePos;

            while(BoardUtils.isValidTileCoor(moveDestCoor)){
                if(isFirstColumnExclusion(moveDestCoor, candidateCoordinateOffset) || isEightColumnExclusion(moveDestCoor,candidateCoordinateOffset)) {
                    break;
                }
                moveDestCoor += candidateCoordinateOffset;
                if (BoardUtils.isValidTileCoor(moveDestCoor)){
                    final Tile moveDestTile = board.getTile(moveDestCoor);
                    if (!moveDestTile.isTileOccupied()) {
                        legalMoves.add(new Move.MajorMove(board, this, moveDestCoor));
                    } else {
                        final Piece pieceAtDest = moveDestTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDest.getPieceAlliance();
                        if (this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new Move.OffensiveMove(board,this,moveDestCoor, pieceAtDest));
                        }
                        break;
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }
    @Override
    public String toString(){
        return PieceType.ROOK.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1);
    }

    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGTH_COLUMN[currentPosition] && (candidateOffset == 1);
    }
}
