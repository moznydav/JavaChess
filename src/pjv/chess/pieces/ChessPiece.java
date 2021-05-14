package pjv.chess.pieces;

import pjv.chess.board.Board;
import pjv.chess.board.Move;

import java.util.List;

public abstract class ChessPiece {

    int piecePosition;
    boolean alliance; //true = white, false = black
    boolean isFirstMove = true;


    ChessPiece(int piecePosition, boolean alliance){
        this.piecePosition = piecePosition;
        this.alliance = alliance;
    }

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public boolean getPieceAlliance(){
        return this.alliance;
    }

    public boolean isFirstMove(){
        return this.isFirstMove;
    }

    public abstract List<Move> calculateAllLegalMoves(Board board);
}
