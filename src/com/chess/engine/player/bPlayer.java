package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;

import java.util.Collection;

public class bPlayer extends Player{
    public bPlayer(final Board board,
                   final Collection<Move> whiteLegalMoves,
                   final Collection<Move> blackLegalMoves) {
        super(board,blackLegalMoves,whiteLegalMoves);

    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.getWhitePlayer();
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }
}
