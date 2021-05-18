package com.chess.engine.board;

public class BoardUtils {
    public static final boolean[] FIRST_COLUMN = null;
    public static final boolean[] SECOND_COLUMN = null;
    public static final boolean[] SEVENTH_COLUMN = null;
    public static final boolean[] EIGTH_COLUMN = null;
    private BoardUtils(){
        throw new RuntimeException("BoardUtil cannot be instantiated.");    //no BoardUtil objs
    }


    public static boolean isValidTileCoor(int moveDestCoor) {         // returns true if a tile coordinate is valid
        return 0 <= moveDestCoor && moveDestCoor < 64;                 //  ie, it is a coordinate in range 0-63

    }
}
