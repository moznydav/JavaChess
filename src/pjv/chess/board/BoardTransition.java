package pjv.chess.board;

import pjv.chess.board.Board;
import pjv.chess.board.Move;

public class BoardTransition {

    private Board oldBoard;
    private Board newBoard;
    private Move move;
    private Move.MoveStatus moveStatus;

    public BoardTransition(Board oldBoard, Board newBoard, Move move, Move.MoveStatus moveStatus){
        this.oldBoard = oldBoard;
        this.newBoard = newBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public Move.MoveStatus getMoveStatus(){
        return this.moveStatus;
    }

    public Board getNewBoard(){
        return this.newBoard;
    }

    public Board getOldBoard(){
        return this.oldBoard;
    }
}
