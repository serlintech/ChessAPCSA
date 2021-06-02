package com.chess.engine.board;

public class BoardUtils {
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGTH_COLUMN = initColumn(7);
    public static final boolean[] SECOND_ROW = initRow(8);
    public static final boolean[] SEVENTH_ROW = initRow(48);
    public static final int TILES = 64;
    public static final int TILES_PER_ROW = 8;
    private BoardUtils(){
        throw new RuntimeException("BoardUtil cannot be instantiated.");    //no BoardUtil objs
    }

    public static boolean[] initRow(int rowNum){
        final boolean[] row = new boolean[TILES];
        do{
            row[rowNum] = true;
            rowNum++;
        }while(rowNum % TILES_PER_ROW != 0);
        return row;
    }
    public static boolean isValidTileCoor(int moveDestCoor) {         // returns true if a tile coordinate is valid
        return 0 <= moveDestCoor && moveDestCoor < 64;                 //  ie, it is a coordinate in range 0-63

    }
    private static boolean[] initColumn(int i) {
        final boolean[] column= new boolean[TILES];
        do{
            column[i]=true;
            i+= TILES_PER_ROW;

        }while(i<TILES);
        return column;
    }
}
