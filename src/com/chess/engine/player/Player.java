package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;

import java.util.Collection;

public abstract class Player {
    protected final Board board;
    protected final King pKing;
    protected final Collection<Move> legalMoves;
    Player(final Board board,
           final Collection<Move>legalMoves,
           final Collection<Move>oppMoves){
        this.board=board;
        this.pKing= establishKing();
        this.legalMoves=legalMoves;
    }

    private King establishKing() {
         for(final Piece piece: getActivePieces()){
             if(piece.getPieceType().isKing()){
                 return (King) piece;
             }
         }
    }
    public abstract Collection<Piece> getActivePieces();
}
