package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {
    protected final Board board;
    protected final King pKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;
    Player(final Board board,
           final Collection<Move>legalMoves,
           final Collection<Move>oppMoves){
        this.board=board;
        this.pKing= establishKing();
        this.legalMoves=legalMoves;
        this.isInCheck = !Player.calcAttacksOnTile(this.pKing.getPiecePos(), oppMoves).isEmpty();
    }
    public static Collection<Move> calcAttacksOnTile(int piecePos, Collection<Move> oppMoves){
        final List<Move> attackMoves = new ArrayList<>();
        for(final Move move: oppMoves){
            if(piecePos == move.getDestCoor()){
                attackMoves.add(move);
            }
        }return ImmutableList.copyOf(attackMoves);
    }
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
    public boolean isLegalMove(final Move move){
        return this.legalMoves.contains(move);
    }
    public King getPlayerKing(){
        return this.pKing;
    }
    public Collection<Move> getLegalMoves(){
        return legalMoves;
    }

    public boolean isInCheck(){
        return this.isInCheck;
    }
    public boolean isCastled(){
        return false;
    }
    public boolean isInCheckMate(){
        return this.isInCheck && !hasEscapeMoves();
    }
    public boolean isInStaleMate(){
        return !this.isInCheck && !hasEscapeMoves();
    }
    protected boolean hasEscapeMoves(){//returns true if a move in legal move can go through w/o being in check
        for(final Move move: this.legalMoves){
            final MoveTransition transition = makeMove(move);
            if(transition.getMoveStatus().isDone()){
                return true;
            }
        }return false;
    }

    public MoveTransition makeMove(final Move move){
        if(!isLegalMove(move)){
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        final Board transBoard = move.execute();
        final Collection<Move> kingAttacks = Player.calcAttacksOnTile(transBoard.getCurrentPlayer().getOpponent().getPlayerKing().getPiecePos(),
                transBoard.getCurrentPlayer().getLegalMoves());
        if(!kingAttacks.isEmpty()){
            return new MoveTransition(this.board,move,MoveStatus.ERROR_IN_CHECK);
        }
        return new MoveTransition(transBoard, move, MoveStatus.DONE);
    }

    private King establishKing() {
         for(final Piece piece: getActivePieces()){
             if(piece.getPieceType().isKing()){
                 return (King) piece;
             }
         }////if no king, invalid board setup, throws error
        throw new RuntimeException("Should not reach here, Not a valid board state.");
    }
    public abstract Collection<Piece> getActivePieces();
}
