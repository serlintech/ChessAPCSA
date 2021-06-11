package com.chess.engine.board;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import javax.print.attribute.standard.MediaSize;
import java.nio.channels.NotYetBoundException;

import static com.chess.engine.board.Board.*;

public abstract class Move {
    protected final Board board;
    protected final Piece movePiece;
    protected final int destCoor;
    protected final boolean isFirstMove;
    public static final Move NULL_MOVE = new NullMove();

    private Move(final Board board, final Piece movePiece, final int destCoor){
        this.board=board;
        this.movePiece=movePiece;
        this.destCoor=destCoor;
        this.isFirstMove=movePiece.isFirstMove();
    }
    private Move(final Board board,
                 final int destCoor){
        this.board=board;
        this.destCoor=destCoor;
        this.movePiece=null;
        this.isFirstMove=false;
    }
    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;

        result = prime * result + this.destCoor;
        result = prime * result + this.movePiece.hashCode();
        result=prime*result+this.movePiece.getPiecePos();
        result = result + (isFirstMove ? 1 : 0);
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
        return getCurrentCoor()==otherMove.getCurrentCoor() &&
                getDestCoor() == otherMove.getDestCoor() &&
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
        final Board.Builder builder = new Builder();

        this.board.getCurrentPlayer().getActivePieces().stream().filter(piece -> !this.movePiece.equals(piece)).forEach(builder::setPiece);
        this.board.getCurrentPlayer().getOpponent().getActivePieces().forEach(builder::setPiece);
        builder.setPiece(this.movePiece.movePiece(this));
        builder.setMoveGen(this.board.getCurrentPlayer().getOpponent().getAlliance());
        builder.setMoveTransition(this);
        return builder.build();
    }

    public static final class MajorMove extends Move{

        public MajorMove(final Board board, final Piece movePiece, final int destCoor) {
            super(board, movePiece, destCoor);
        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof MajorMove && super.equals(other);
        }
        @Override
        public String toString(){
            return movePiece.getPieceType().toString() +BoardUtils.getPosAtCoor(this.destCoor);
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
            return this == other || other instanceof MajorMove && super.equals(other);
        }
        @Override
        public String toString(){
            return movePiece.getPieceType().toString() + BoardUtils.getPosAtCoor(this.destCoor);
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
        public boolean equals(final Object other){
            return this==other || other instanceof PawnJumpMove && super.equals(other);
        }
        @Override
        public Board execute(){
            final Builder builder = new Builder();
            this.board.getCurrentPlayer().getActivePieces().stream().filter(piece -> !this.movePiece.equals(piece)).forEach(builder::setPiece);
            this.board.getCurrentPlayer().getOpponent().getActivePieces().forEach(builder::setPiece);
            final Pawn movedPawn = (Pawn)this.movePiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveGen(this.board.getCurrentPlayer().getOpponent().getAlliance());
            builder.setMoveTransition(this);
            return builder.build();
        }
        @Override
        public String toString(){
            return BoardUtils.getPosAtCoor(this.destCoor);
        }

    }

    static abstract class CastleMove extends Move{
        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDest;

        public CastleMove(final Board board,
                          final Piece movePiece,
                          final int destCoor,
                          final Rook castleRook,
                          final int castleRookStart,
                          final int castleRookDest) {
            super(board, movePiece, destCoor);
            this.castleRook = castleRook;
            this.castleRookStart=castleRookStart;
            this.castleRookDest = castleRookDest;
        }
        public Rook getCastleRook(){
            return this.castleRook;
        }
        @Override
        public boolean isCastleMove(){
            return true;
        }

        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Piece piece : this.board.getCurrentPlayer().getActivePieces()){
                if(!this.movePiece.equals(piece) && !this.castleRook.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()){

                builder.setPiece(this.movePiece);
            }
            builder.setPiece(this.movePiece.movePiece(this));
            //todo look into first move
            builder.setPiece(new Rook(this.castleRook.getPieceAlliance(), this.castleRookDest));
            builder.setMoveGen(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }



    }
    public static final class KingSideCastleMove extends CastleMove{

        public KingSideCastleMove(final Board board,
                                  final Piece movePiece,
                                  final int destCoor,
                                  final Rook castleRook,
                                  final int castleRookStart,
                                  final int castleRookDest) {
            super(board, movePiece, destCoor,castleRook,castleRookStart,castleRookDest);
        }
        @Override
        public String toString(){
            return "0-0";
        }

    }
    public static final class QueenSideCastleMove extends CastleMove{

        public QueenSideCastleMove(final Board board,
                                   final Piece movePiece,
                                   final int destCoor,
                                   final Rook castleRook,
                                   final int castleRookStart,
                                   final int castleRookDest) {
            super(board, movePiece, destCoor,castleRook,castleRookStart,castleRookDest);
        }
        @Override
        public String toString(){
            return "0-0-0";
        }

    }
    public static final class NullMove extends Move{

        public NullMove() {
            super( null, -1);
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
