package pjv.chess;

import pjv.chess.board.Board;
import pjv.chess.gui.Table;

public class ChessGame {

    public static void main(String[] args){
        Board board = Board.createStandardBoard();

        System.out.println(board);

        Table table = new Table();
    }
}
