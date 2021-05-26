package pjv.chess.board;

import pjv.chess.board.Board;
import pjv.chess.board.Move;

/**
 * This class is used for good transition between boards
 * This class is also used for checking if move is valid via MoveStatus
 *
 * @author David Mozny
 */
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
}
