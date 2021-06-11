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

import static com.chess.engine.board.Move.*;

public class King extends Piece{
    private final static int[] CANDIDATES = {-9,-8,-7,-1,1,7,8,9};

    public King( final Alliance pieceAlliance, final int piecePos){

        super( PieceType.KING,pieceAlliance,piecePos,true);
    }
    public King( final Alliance pieceAlliance, final int piecePos, final boolean isFirstMove){

        super( PieceType.KING,pieceAlliance,piecePos,isFirstMove);
    }


    @Override
    public King movePiece(Move move) {
        return new King(move.getMovePiece().getPieceAlliance(), move.getDestCoor());
    }
    @Override
    public Collection<Move> legalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentCandidate : CANDIDATES){

            final int moveDestCoor = this.piecePos + currentCandidate;
            if(isFirstColumnExclusion(this.piecePos,currentCandidate) || isEigthColumnExclusion(this.piecePos,currentCandidate)){
                continue;
            }
            if(BoardUtils.isValidTileCoor(moveDestCoor)){
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
        return PieceType.KING.toString();
    }
    private static boolean isFirstColumnExclusion(final int cPos, final int candidatePos){        //these moves dont work and must be excluded
        return (BoardUtils.FIRST_COLUMN[cPos])&& ((candidatePos==-9) || (candidatePos == -1) ||
                candidatePos==7 );
    }
    private static boolean isEigthColumnExclusion(final int cPos, final int candidatePos){
        return BoardUtils.EIGHTH_COLUMN[cPos]&&((candidatePos == -7)|| candidatePos ==1 || candidatePos==9);
    }




}


