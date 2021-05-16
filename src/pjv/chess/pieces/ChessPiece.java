package pjv.chess.pieces;

import pjv.chess.board.Board;
import pjv.chess.board.Move;

import java.util.Collection;
import java.util.List;

public abstract class ChessPiece {

    int piecePosition;
    boolean alliance; //true = white, false = black
    boolean isFirstMove = true; //because of castling and pawn jump

    ChessPiece(int piecePosition, boolean alliance){
        this.piecePosition = piecePosition;
        this.alliance = alliance;
    }

    @Override
    public boolean equals(Object other){ //are the chess pieces equal, not just object equality
        if(this == other){
            return true;
        } else if(!(other instanceof ChessPiece)){
            return false;
        }
        ChessPiece otherPiece = (ChessPiece) other;
        return piecePosition == otherPiece.getPiecePosition() &&
                alliance== otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove();
    }

    @Override
    public int hashCode(){
        int hash = 7;
        int prime = 31;
        hash = prime * hash + (alliance ? 1 : 0);
        hash = prime * hash + piecePosition;
        hash = prime * hash + (isFirstMove ? 1 : 0);
        return hash;
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

    public void switchFirstMove(){ this.isFirstMove = false; }

    public abstract List<Move> calculateAllLegalMoves(Board board);

    public abstract boolean isKing();

    public abstract ChessPiece movePiece(Move move);

}
