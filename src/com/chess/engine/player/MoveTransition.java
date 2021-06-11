package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public class MoveTransition {

    private final Move move;
    private final MoveStatus moveStatus;
    private final Board startBoard, endBoard;

    public MoveTransition(final Board startBoard,
                          final Board endBoard,
                          final Move move,
                          final MoveStatus moveStatus){
        this.startBoard=startBoard;
        this.endBoard=endBoard;
        this.move=move;
        this.moveStatus=moveStatus;

    }
    public MoveStatus getMoveStatus(){
        return this.moveStatus;
    }


    public Board getStartBoard(){
        return startBoard;
    }
    public Board getEndBoard(){
        return endBoard;
    }


}
