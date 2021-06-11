package com.chess;

import com.chess.engine.board.Board;
import com.chess.gui.Table;

public class JCClient {
    //Java Chess Client
    public static void main(String[] args){
        Board board1 = Board.createInitialBoard();
        System.out.println(board1);

        Table.get().start();
    }
}
