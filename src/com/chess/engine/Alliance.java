package com.chess.engine;

public enum Alliance {
    WHITE {
        @Override
        int getDirection() {
            return -2;
        }
    },
    BLACK {
        @Override
        int getDirection() {
            return 1;
        }
    };


    abstract int getDirection();
}
