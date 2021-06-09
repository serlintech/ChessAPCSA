package com.chess.engine.board;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;

import javax.print.attribute.standard.MediaSize;
import java.nio.channels.NotYetBoundException;

import static com.chess.engine.board.Board.*;

public abstract class Move {
    final Board board;
    final Piece movePiece;
    final int destCoor;
    public static final Move NULL_MOVE = new NullMove();
    Move(final Board board, final Piece movePiece, final int destCoor){
        this.board=board;
        this.movePiece=movePiece;
        this.destCoor=destCoor;
    }
    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;

        result = prime * result + this.destCoor;
        result = prime * result + this.movePiece.hashCode();
        return result;
    }
    @Override
    public boolean equals(final Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Move)){
            return false;
        }
        final Move otherMove = (Move) other;
        return getDestCoor() == otherMove.getDestCoor() &&
                getMovePiece().equals(otherMove.getMovePiece());
    }





    public int getCurrentCoor(){
        return this.getMovePiece().getPiecePos();
    }

    public int getDestCoor(){
        return this.destCoor;
    }
    public Piece getMovePiece(){
        return this.movePiece;
    }

    public boolean isAttackMove(){
        return false;
    }
    public boolean isCastleMove(){
        return false;
    }
    public Piece getIncidentPiece(){
        return null;
    }
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
        builder.setPiece(this.movePiece.movePiece(this));
        //sets move maker for outgoing board to black
        builder.setMoveGen(this.board.getCurrentPlayer().getOpponent().getAlliance());

        return builder.build();
    }

    public static final class MajorMove extends Move{

        public MajorMove(final Board board, final Piece movePiece, final int destCoor) {
            super(board, movePiece, destCoor);
        }

    }
    public static class OffensiveMove extends Move{
        final Piece incidentPiece;
        public OffensiveMove(final Board board,
                             final Piece movePiece,
                             final int destCoor,
                             final Piece incidentPiece) {
            super(board, movePiece, destCoor);
            this.incidentPiece=incidentPiece;
        }
        @Override
        public int hashCode(){
            return this.incidentPiece.hashCode() + super.hashCode();
        }
        @Override
        public boolean equals(final Object other){
            if(this == other){
                return true;

            }
            if(!(other instanceof OffensiveMove)){
                return false;
            }
            final OffensiveMove OtherMove = (OffensiveMove) other;
            return super.equals(OtherMove) && getIncidentPiece().equals(OtherMove.getIncidentPiece());
        }

        @Override
        public Board execute() {
            return null;
        }
        @Override
        public boolean isAttackMove(){
            return true;
        }
        @Override
        public Piece getIncidentPiece(){
            return this.incidentPiece;
        }

    }
    public static final class PawnMove extends Move{

        public PawnMove(final Board board, final Piece movePiece, final int destCoor) {
            super(board, movePiece, destCoor);
        }

    }
    public static final class PawnOffensiveMove extends OffensiveMove{

        public PawnOffensiveMove(final Board board,
                                 final Piece movePiece,
                                 final int destCoor,
                                 final Piece incidentPiece) {
            super(board, movePiece, destCoor, incidentPiece);
        }

    }
    public static final class PawnEnPassantMove extends OffensiveMove{

        public PawnEnPassantMove(final Board board,
                                 final Piece movePiece,
                                 final int destCoor,
                                 final Piece incidentPiece) {
            super(board, movePiece, destCoor, incidentPiece);
        }

    }public static final class PawnJumpMove extends Move{

        public PawnJumpMove(final Board board, final Piece movePiece, final int destCoor) {
            super(board, movePiece, destCoor);
        }
        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Piece piece : this.board.getCurrentPlayer().getActivePieces()){
                if(!this.movePiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()){

                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn)this.movePiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveGen(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }

    }

    static abstract class CastleMove extends Move{

        public CastleMove(final Board board, final Piece movePiece, final int destCoor) {
            super(board, movePiece, destCoor);
        }

    }
    public static final class KingSideCastleMove extends CastleMove{

        public KingSideCastleMove(final Board board, final Piece movePiece, final int destCoor) {
            super(board, movePiece, destCoor);
        }

    }
    public static final class QueenSideCastleMove extends CastleMove{

        public QueenSideCastleMove(final Board board, final Piece movePiece, final int destCoor) {
            super(board, movePiece, destCoor);
        }

    }
    public static final class NullMove extends Move{

        public NullMove() {
            super(null, null, -1);
        }

        @Override
        public Board execute(){
            throw new RuntimeException("Cannot execute a null move.");
        }

    }
    public static class MoveFactory{
        private MoveFactory(){
            throw new RuntimeException("Factory class... not instantiable.");
        }
        public static Move createMove(final Board board,
                                      final int currentCoor,
                                      final int destCoor){
            for(final Move move : board.getAllLegalMoves()){
                if(move.getCurrentCoor() == currentCoor && move.getDestCoor()==destCoor){
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}
