package pjv.chess;

import pjv.chess.board.Board;

public class ChessGame {

    public static void main(String[] args){
        Board board = Board.createStandardBoard();

        System.out.println(board);
    }
}
