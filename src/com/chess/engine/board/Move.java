package com.chess.engine.board;
import com.chess.engine.pieces.Piece;

import static com.chess.engine.board.Board.*;

public abstract class Move {
    final Board board;
    final Piece movePiece;
    final int destCoor;
    Move(final Board board, final Piece movePiece, final int destCoor){
        this.board=board;
        this.movePiece=movePiece;
        this.destCoor=destCoor;
    }

    public int getDestCoor(){
        return this.destCoor;
    }

    public abstract Board execute();

    public static final class MajorMove extends Move{

        public MajorMove(final Board board, final Piece movePiece, final int destCoor) {
            super(board, movePiece, destCoor);
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();

            for(final Piece piece : this.board.getCurrentPlayer().getActivePieces()){
                //TODO hash code + equals for pieces
                if(!this.movePiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            //plan is to move the moved piece w/ this call
            builder.setMoveGen(null);
            //sets move maker for outgoing board to black
            builder.setMoveGen(this.board.getCurrentPlayer().getOpponent().getAlliance());

            return builder.build();
        }
    }
    public static final class OffensiveMove extends Move{
        final Piece incidentPiece;
        public OffensiveMove(final Board board,
                             final Piece movePiece,
                             final int destCoor,
                             final Piece incidentPiece) {
            super(board, movePiece, destCoor);
            this.incidentPiece=incidentPiece;
        }

        @Override
        public Board execute() {
            return null;
        }
    }
}
