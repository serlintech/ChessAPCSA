package com.chess.engine.pieces;
import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Queen extends Piece{
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9,-8, -7,-1,1, 7,8, 9};

    public Queen( final Alliance pieceAlliance,final int piecePos) {
        super(PieceType.QUEEN, pieceAlliance, piecePos,true);
    }
    public Queen( final Alliance pieceAlliance,final int piecePos, final boolean isFirstMove) {
        super(PieceType.QUEEN, pieceAlliance, piecePos,isFirstMove);
    }
    @Override
    public Queen movePiece(Move move) {
        return new Queen(move.getMovePiece().getPieceAlliance(), move.getDestCoor());
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
                        legalMoves.add(new MajorMove(board, this, moveDestCoor));
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
        return PieceType.QUEEN.toString();
    }
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset==-1 ||candidateOffset == -9 || candidateOffset == 7);
    }

    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset==1 || candidateOffset == 9);
    }

}
