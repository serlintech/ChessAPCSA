package com.chess.engine.player;

public enum MoveStatus {
    DONE {
        boolean isDone() {
            return true;
        }
    },
    ILLEGAL_MOVE{
        @Override
        boolean isDone() {
            return false;
        }
    },
    ERROR_IN_CHECK{
        @Override
        boolean isDone() {
            return false;
        }
    };



    abstract boolean isDone();
}
