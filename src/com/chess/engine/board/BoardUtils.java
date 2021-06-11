package com.chess.engine.board;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BoardUtils {
    public static final boolean[] FIRST_COLUMN =initColumn(0);
    public static final boolean[] SECOND_COLUMN =initColumn(1);
    public static final boolean[] SEVENTH_COLUMN =initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7);

    public static final boolean[] FIRST_ROW = initRow(0);
    public static final boolean[] SECOND_ROW = initRow(8);
    public static final boolean[] THIRD_ROW = initRow(16);
    public static final boolean[] FOURTH_ROW = initRow(24);
    public static final boolean[] FIFTH_ROW = initRow(32);
    public static final boolean[] SIXTH_ROW = initRow(40);
    public static final boolean[] SEVENTH_ROW = initRow(48);
    public static final boolean[] EIGHTH_ROW = initRow(56);

    public static final int TILES = 64;
    public static final int TILES_PER_ROW = 8;

    public static final String [] ALGEBRAIC_NOTATION = initAlgNotation();

    private static String[] initAlgNotation() {
        return new String[]{
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"};
    }

    public static final Map<String, Integer> POS_TO_COOR = initPosToCoorMap();

    private static Map<String, Integer> initPosToCoorMap() {
        final Map<String, Integer> posToCoor = new HashMap<>();
        for(int i=0; i<TILES;i++) {
            posToCoor.put(ALGEBRAIC_NOTATION[i], i);
        }
        return ImmutableMap.copyOf(posToCoor);
    }


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

    public static String getPosAtCoor(int Coor) {
        return ALGEBRAIC_NOTATION[Coor];
    }
    public static int getCoorAtPos(String position) {
        return POS_TO_COOR.get(position);
    }
}
