package com.chess.engine;

import com.chess.engine.player.Player;
import com.chess.engine.player.bPlayer;
import com.chess.engine.player.wPlayer;

public enum Alliance {
    WHITE {
        @Override
        public int getDirection() {

            return -1;
        }
        public boolean isWhite(){
            return true;
        }

        @Override
        public Player choosePlayer(wPlayer whitePlayer, bPlayer blackPlayer) {
            return whitePlayer;
        }

        public boolean isBlack(){
            return false;
        }
    },
    BLACK {
        @Override
        public int getDirection() {

            return 1;
        }
        public boolean isBlack(){
            return true;
        }
        public boolean isWhite(){
            return false;
        }

        @Override
        public Player choosePlayer(final wPlayer whitePlayer,final bPlayer blackPlayer) {
            return blackPlayer;
        }
    };


    public abstract int getDirection();

    public abstract boolean isBlack();
    public abstract boolean isWhite();

    public abstract Player choosePlayer(final wPlayer whitePlayer,final bPlayer blackPlayer);
}
