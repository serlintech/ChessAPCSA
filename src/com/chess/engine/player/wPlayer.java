package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class wPlayer extends Player{
    public wPlayer(final Board board,
                   final Collection<Move> whiteLegalMoves,
                   final Collection<Move> blackLegalMoves) {
        super(board, whiteLegalMoves, blackLegalMoves);
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.getBlackPlayer();
    }

    @Override
    public Collection<Move> calcKingCastles(final Collection<Move> playerLegals,
                                            final Collection<Move> oppLegals) {
        //white castles
        final List<Move> kingCastles = new ArrayList<>();
        if(this.getPlayerKing().isFirstMove() && !this.isInCheck()){
            if(!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()){
                final Tile rookTile = this.board.getTile(63);

                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                    if(Player.calcAttacksOnTile(61,oppLegals).isEmpty() &&
                       Player.calcAttacksOnTile(62, oppLegals).isEmpty() &&
                       rookTile.getPiece().getPieceType().isRook()){
                        //TODO ADD CASTLE MOVES
                        kingCastles.add(new KingSideCastleMove(this.board,
                                                                    this.getPlayerKing(),
                                                           62,
                                                                    (Rook)rookTile.getPiece(),
                                                                    rookTile.getTileCoor(),
                                                                    61));

                    }

                }
            }
            if(!this.board.getTile(59).isTileOccupied() &&
               !this.board.getTile(59).isTileOccupied() &&
               !this.board.getTile(57).isTileOccupied()){
                final Tile rookTile = this.board.getTile(56);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() &&
                    Player.calcAttacksOnTile(58, oppLegals).isEmpty() &&
                    Player.calcAttacksOnTile(59, oppLegals).isEmpty() &&
                    rookTile.getPiece().getPieceType().isRook()){
                    //TODO add castle move
                    kingCastles.add(new QueenSideCastleMove(this.board,
                                                            this.pKing,
                                                            58,
                                                            (Rook)rookTile.getPiece(),
                                                            rookTile.getTileCoor(),
                                                            59));
                }
            }
        }


        return ImmutableList.copyOf(kingCastles);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }
}

