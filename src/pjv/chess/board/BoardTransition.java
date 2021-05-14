package pjv.chess.board;

import pjv.chess.board.Board;
import pjv.chess.board.Move;

public class BoardTransition {

    private Board newBoard;
    private Move move;
    private Move.MoveStatus moveStatus; //is legal

    public BoardTransition(Board newBoard, Move move, Move.MoveStatus moveStatus){
        this.newBoard = newBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }
}
