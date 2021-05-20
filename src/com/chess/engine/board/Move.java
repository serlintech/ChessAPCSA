package com.chess.engine.board;
import com.chess.engine.pieces.Piece;

public abstract class Move {
    final Board board;
    final Piece movePiece;
    final int destCoor;
    Move(final Board board, final Piece movePiece, final int destCoor){
        this.board=board;
        this.movePiece=movePiece;
        this.destCoor=destCoor;
    }


    public static final class MajorMove extends Move{

        public MajorMove(final Board board, final Piece movePiece, final int destCoor) {
            super(board, movePiece, destCoor);
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
    }
}
