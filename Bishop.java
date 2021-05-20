package com.chess.engine.pieces;
import com.chess.engine.Alliance;


import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Bishop extends Piece{
	private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9, -7, 7, 9};
	
	Bishop(int piecePos, Alliance pieceAll){
		super(piecePos, pieceAll);
	}
	
	@Override 
	public Collection<Move> calculateLegalMoves(Board board){
		final List<Move> legalMoves = new ArrayList<>();
		for(final int candidateCoordinateOffset: CANDIDATE_MOVE_VECTOR_COORDINATES){
			
			int candidateDestinationCoordinate = this.piecePos;
			
			while(BoardUtils.isValidTileCoor(candidateDestinationCoordinate)){
				 if(isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) || isEightColumnExclusion(candidateDestinationCoordinate,candidateCoordinateOffset)) {
					 break;
				 }
				candidateDestinationCoordinate += candidateCoordinateOffset;
				if (BoardUtils.isValidTileCoor(candidateDestinationCoordinate)){
					 final Tile moveDestTile = board.getTile(moveDestCoor);
		                if (!moveDestTile.isTileOccupied()) {
		                    legalMoves.add(new Move());
		                } else {
		                    final Piece pieceAtDest = moveDestTile.getPiece();
		                    final Alliance pieceAlliance = pieceAtDest.getPieceAlliance();
		                    if (this.pieceAlliance != pieceAlliance) {
		                        legalMoves.add(new Move());
		                        }
		                    break;
		                    }		                
		                }
				}
			}
		return ImmutableList.copyOf(legalMoves);
	}
	
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN(currentPosition) && (candidateOffset == -9 || candidateOffset == 7);
	}
	
	private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.EIGTH_COLUMN(currentPosition) && (candidateOffset == -7 || candidateOffset == 9);
	}
}
