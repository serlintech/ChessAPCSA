package com.chess.engine;

import com.chess.engine.board.Board;

public class JCClient {
    //Java Chess Client
    public static void main(String[] args){
        Board board1 = Board.createInitialBoard();
        System.out.println(board1);
    }
}
