package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    private final int[] POSS_MOVE_COORDS = {-17, -15, -10, 6, 6, 10, 15, 17};        //these vals added to move as possible knight move coords

    Knight(final int piecePos, final Alliance pieceAlliance) {
        super(piecePos, pieceAlliance);
    }

    @Override
    public List<Move> legalMoves(Board board) {         //returns list of all valid moves
        int moveDestCoor;
        final List<Move> legalMoves = new ArrayList<>();  //temp list for valid moves

        for (final int move : POSS_MOVE_COORDS) {         //for each lop going through possible moves, adding to coor of knight obj
            moveDestCoor = this.piecePos + move;

            if (BoardUtils.isValidTileCoor(moveDestCoor)) {
                final Tile moveDestTile = board.getTile(moveDestCoor);

                if (!moveDestTile.isTileOccupied()) {
                    legalMoves.add(new Move());

                } else {
                    final Piece pieceAtDest = moveDestTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDest.getPieceAlliance();
                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new Move());
                    }
                }
            }

        }
        return ImmutableList.copyOf(legalMoves);
    }
    private static boolean firstColumnExclusin(final int cPos, final int candidatePos){
        BoardUtils.FIRST_COLUMN(cPosition) && ((candidatePos==-17) || (candidatePos == -10) ||
        candidatePos==6 || candidatePos==15);
    }


}
