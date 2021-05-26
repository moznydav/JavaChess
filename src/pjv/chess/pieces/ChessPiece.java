package pjv.chess.pieces;

import pjv.chess.board.Board;
import pjv.chess.board.Move;

import java.util.Collection;
import java.util.List;

/**
 * Abstract class that is father to all pieces classes (Rook, Bishop..)
 *
 * @author David Mozny
 */
public abstract class ChessPiece {

    int piecePosition;
    boolean alliance; //true = white, false = black
    boolean isFirstMove = true; //because of castling and pawn jump

    ChessPiece(int piecePosition, boolean alliance, boolean isFirstMove){
        this.piecePosition = piecePosition;
        this.alliance = alliance;
        this.isFirstMove = isFirstMove;
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

    /**
     * All getters for ChessPiece class
     * @return
     */
    public int getPiecePosition() {
        return this.piecePosition;
    }

    public boolean getPieceAlliance(){
        return this.alliance;
    }

    public boolean isFirstMove(){
        return this.isFirstMove;
    }

    /**
     * Flips first move parameter of ChessPiece
     */
    public void switchFirstMove(){ this.isFirstMove = false; }

    /**
     * Calculates legal move of piece, most important method in ChessPiece class
     * @param board
     * @return list of legal moves
     */
    public abstract List<Move> calculateAllLegalMoves(Board board);

    /**
     * Checks if given piece is King
     * @return true/false
     */
    public abstract boolean isKing();

    /**
     * Second most important method in ChessPiece class that handles deleting chessPiece and creating new one between moves
     * @param move
     * @return new ChessPiece
     */
    public abstract ChessPiece movePiece(Move move);

}
