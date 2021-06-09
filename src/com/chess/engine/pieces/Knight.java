package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class Knight extends Piece {
    private final int[] POSS_MOVE_COORDS = {-17, -15, -10, 6, 6, 10, 15, 17};        //these vals added to move as possible knight move coords

    public Knight(final Alliance pieceAlliance, final int piecePos) {
        super(PieceType.KNIGHT,pieceAlliance, piecePos);
    }


    @Override
    public Knight movePiece(Move move) {
        return new Knight(move.getMovePiece().getPieceAlliance(), move.getDestCoor());
    }


    @Override
    public List<Move> legalMoves(final Board board) {         //returns list of all valid moves
        int moveDestCoor;                               //int representing destination of move on board
        final List<Move> legalMoves = new ArrayList<>();  //temp list for valid moves

        for (final int move : POSS_MOVE_COORDS) {         //for each lop going through possible moves, adding to coor of knight obj

            moveDestCoor = this.piecePos + move;
            if(firstColumnExclusion(this.piecePos,moveDestCoor)||secondColumnExclusion(this.piecePos,moveDestCoor)||
            seventhColumnExclusion(this.piecePos,moveDestCoor)||eigthColumnExclusion(this.piecePos,moveDestCoor)){
                continue;
            }
            if (BoardUtils.isValidTileCoor(moveDestCoor)) {
                final Tile moveDestTile = board.getTile(moveDestCoor);

                if (!moveDestTile.isTileOccupied()) {
                    legalMoves.add(new MajorMove(board,this,moveDestCoor));

                } else {
                    final Piece pieceAtDest = moveDestTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDest.getPieceAlliance();
                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new OffensiveMove(board,this,moveDestCoor,pieceAtDest));
                    }
                }
            }

        }
        return ImmutableList.copyOf(legalMoves);
    }
    @Override
    public String toString(){
        return PieceType.KNIGHT.toString();
    }
    private static boolean firstColumnExclusion(final int cPos, final int candidatePos){        //these moves dont work and must be excluded
        return (BoardUtils.FIRST_COLUMN[cPos])&& ((candidatePos==-17) || (candidatePos == -10) ||
        candidatePos==6 || candidatePos==15);
    }
    private static boolean secondColumnExclusion(final int cPos, final int candidatePos){
        return BoardUtils.SECOND_COLUMN[cPos]&&((candidatePos == -10)|| candidatePos ==6);
    }
    private static boolean seventhColumnExclusion(final int cPos, final int candidatePos){
        return BoardUtils.SEVENTH_COLUMN[cPos] && ((candidatePos == -6)|| candidatePos== 10);
    }
    private static boolean eigthColumnExclusion(final int cPos, final int candidatePos){
        return BoardUtils.EIGTH_COLUMN[cPos] && ((candidatePos == -15) || candidatePos== -6 || candidatePos == 10
                || candidatePos == 17);
    }

}
