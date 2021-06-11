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
    public Collection<Move> calcKingCastles(final Collection<Move> playerLegals,
                                            final Collection<Move> oppLegals) {
        final List<Move> kingCastles = new ArrayList<>();
        if(this.getPlayerKing().isFirstMove() && !this.isInCheck()){
            if(!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()){
                final Tile rookTile = this.board.getTile(7);

                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                    if(Player.calcAttacksOnTile(5,oppLegals).isEmpty() &&
                            Player.calcAttacksOnTile(6, oppLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()){
                        //TODO ADD CASTLE MOVES
                        kingCastles.add(new KingSideCastleMove(this.board, this.pKing,6,(Rook)rookTile.getPiece(),rookTile.getTileCoor(),5));

                    }

                }
            }
            if(!this.board.getTile(1).isTileOccupied() &&
                    !this.board.getTile(2).isTileOccupied() &&
                    !this.board.getTile(3).isTileOccupied()){
                final Tile rookTile = this.board.getTile(0);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() &&
                    Player.calcAttacksOnTile(2,oppLegals).isEmpty() &&
                            Player.calcAttacksOnTile(3, oppLegals).isEmpty()&&
                            rookTile.getPiece().getPieceType().isRook()){
                    //TODO add castle move
                    kingCastles.add(new QueenSideCastleMove(this.board,this.pKing,2,(Rook)rookTile.getPiece(), rookTile.getTileCoor(), 3));
                }
            }
        }


        return ImmutableList.copyOf(kingCastles);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }
}
