
package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;

public final class StandardBoardEval implements BoardEval {

    private static final int CHECK_BONUS = 50; // five tenths the value of a pawn
    private static final int CHECK_MATE_BONUS = 10000; //one hundred times the value of a pawn
    private static final int DEPTH_BONUS = 100; //equal to the value of a pawn (pawn worth 100 points)
    private static final int CASTLE_BONUS = 60; //six tenths the value of a pawn (100 points is a pawn)

    @Override
    public int evaluate(final Board board, final int depth) {
        return scorePlayer(board, board.getWhitePlayer(), depth) -
                scorePlayer(board, board.getBlackPlayer(), depth);
    }



    private int scorePlayer(final Board board,
                            final Player player,
                            final int depth) {
        return pieceValue(player) +
                mobility(player) +
                check(player) +
                checkmate(player, depth) +
                castled(player);
        // + checkmate, check, castled, mobility.....
    }

    private static int castled(Player player) {
        return player.isCastled() ? CASTLE_BONUS : 0;
    }

    private static int checkmate(Player player, int depth) {
        return player.getOpponent().isInCheckMate() ? CHECK_MATE_BONUS * depthBonus(depth) : 0;
    }

    private static int depthBonus(int depth) {
        return depth == 0 ? 1 : DEPTH_BONUS * depth;
    }

    private static int check(final Player player) {
        return player.getOpponent().isInCheck() ? CHECK_BONUS : 0;
    }

    private static int mobility(final Player player) {
        return player.getLegalMoves().size();
    }

    private static int pieceValue(final Player player){
        int pieceValueScore = 0;
        for(final Piece piece : player.getActivePieces()) {
            pieceValueScore += piece.getPieceVal();
        }
        return pieceValueScore;
    }

}

