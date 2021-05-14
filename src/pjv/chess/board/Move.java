package pjv.chess.board;

import pjv.chess.pieces.ChessPiece;

public class Move {

    Board board;
    ChessPiece movedPiece;
    int pieceDestination;

    Move(Board board, ChessPiece movedPiece, int pieceDestination){
        this.board = board;
        this.movedPiece = movedPiece;
        this.pieceDestination = pieceDestination;
    }


    public static class DefaultMove extends Move {

        public DefaultMove(Board board, ChessPiece movedPiece, int pieceDestination) {
            super(board, movedPiece, pieceDestination);
        }
    }

    public static class AttackMove extends Move{

        ChessPiece attackedPiece;

        public AttackMove(Board board, ChessPiece movedPiece, int pieceDestination, ChessPiece attackedPiece) {
            super(board, movedPiece, pieceDestination);
            this.attackedPiece = attackedPiece;
        }
    }

}
