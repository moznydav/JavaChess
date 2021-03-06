package pjv.chess.pieces;

import pjv.chess.board.Board;
import pjv.chess.board.Move;
import pjv.chess.board.Tile;
import pjv.chess.board.Utils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends ChessPiece{

    private final static int[] MOVE_OFFSETS = {7, 8, 9, 16}; //positive for black, negative for white

    int HALF_OF_ROWS = 4;
    int HALFPOINT = 3;
    int WHITE_COEFFICIENT = -1;
    int BLACK_COEFFICIENT = 1;

    public Pawn(int piecePosition, boolean alliance) {
        super(piecePosition, alliance, true);
    }

    @Override
    public List<Move> calculateAllLegalMoves(Board board) {
        int offsetCoefficient = this.alliance ? WHITE_COEFFICIENT : BLACK_COEFFICIENT; //white moves in negative direction and is "true"
        List<Move> legalMoves = new ArrayList<>();

        for (int currentOffset : MOVE_OFFSETS) {
            int destinationCoordinate = this.piecePosition + offsetCoefficient * currentOffset;

            if (Utils.isValidCoordinate(destinationCoordinate)) {
                Tile destinationTile = board.getTile(destinationCoordinate);
                ChessPiece pieceAtDestination;
                switch (currentOffset) {
                    case 8:
                        if (destinationTile.isEmpty()) {
                            if(isPromotionSquare(destinationCoordinate, this)){
                                legalMoves.add(new Move.PawnPromotion(new Move.PawnMove(board, this, destinationCoordinate)));
                            } else {
                                legalMoves.add(new Move.PawnMove(board, this, destinationCoordinate));
                            }
                        }
                        break;
                    case 16:
                        if (destinationTile.isEmpty() &&
                                board.getTile(destinationCoordinate).isEmpty() &&
                                ((Utils.getRowNumber(this.piecePosition) == 2 && !this.alliance) || //is black and is in 2nd row
                                        (Utils.getRowNumber(this.piecePosition) == 7 && this.alliance))) { //is white and is in 7th row
                            legalMoves.add(new Move.PawnJump(board, this, destinationCoordinate));
                        }
                        break;
                    case 7:
                        pieceAtDestination = destinationTile.getPiece();
                        if(!destinationTile.isEmpty() && pieceAtDestination.alliance != this.alliance){
                            if((this.alliance && Utils.getColumnNumber(this.piecePosition) != 8) || /*is white*/
                                    (!this.alliance && Utils.getColumnNumber(this.piecePosition) != 1) /*is black*/){
                                if(isPromotionSquare(destinationCoordinate, this)){
                                    legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board, this, destinationCoordinate, pieceAtDestination)));
                                } else {
                                    legalMoves.add(new Move.PawnAttackMove(board, this, destinationCoordinate, pieceAtDestination));
                                }
                            }
                        } else if(board.getEnPassantPawn() != null){
                            if (board.getEnPassantPawn().getPiecePosition() == destinationCoordinate - (8 * offsetCoefficient) &&
                                    destinationTile.isEmpty() && board.getEnPassantPawn().getPieceAlliance() != this.alliance){
                                legalMoves.add(new Move.PawnEnPassant(board, this, destinationCoordinate, board.getEnPassantPawn()));
                            }
                        }
                        break;

                    case 9:
                        pieceAtDestination = destinationTile.getPiece();
                        if(!destinationTile.isEmpty() && pieceAtDestination.alliance != this.alliance){
                            if((this.alliance && Utils.getColumnNumber(this.piecePosition) != 1) || /*is white*/
                                    (!this.alliance && Utils.getColumnNumber(this.piecePosition) != 8) /*is black*/){
                                if(isPromotionSquare(destinationCoordinate, this)){
                                    legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board, this, destinationCoordinate, pieceAtDestination)));
                                } else {
                                    legalMoves.add(new Move.PawnAttackMove(board, this, destinationCoordinate, pieceAtDestination));
                                }
                            }
                        } else if(board.getEnPassantPawn() != null){
                            if (board.getEnPassantPawn().getPiecePosition() == destinationCoordinate - (8 * offsetCoefficient) &&
                                    destinationTile.isEmpty() && board.getEnPassantPawn().getPieceAlliance() != this.alliance){
                                legalMoves.add(new Move.PawnEnPassant(board, this, destinationCoordinate, board.getEnPassantPawn()));
                            }
                        }
                        break;
                }
            }
        }
        return legalMoves;
    }
    @Override
    public String toString(){ return "P"; }

    @Override
    public boolean isKing(){
        return false;
    }

    @Override
    public ChessPiece movePiece(Move move) {

        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    private boolean isPromotionSquare(int tileCoordinate, ChessPiece piece){
        if((Utils.getRowNumber(tileCoordinate) == 1 && piece.alliance) ||
                (Utils.getRowNumber(tileCoordinate) == 8 && !piece.alliance)){
            return true;
        } else { return false; }
    }

    public ChessPiece getPromotedPiece(){
        //TODO optional autopromote
        String[] options = {"Queen", "Knight", "Rook", "Bishop"};

        int input = JOptionPane.showOptionDialog(null, "Select piece to promote into", "Select piece",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch(input){
            case 0:
                return new Queen(this.piecePosition, this.getPieceAlliance());
            case 1:
                return new Knight(this.piecePosition, this.getPieceAlliance());
            case 2:
                return new Rook(this.piecePosition, this.getPieceAlliance());
            case 3:
                return new Bishop(this.piecePosition, this.getPieceAlliance());
        }
        return new Queen(this.piecePosition, this.getPieceAlliance());
    }

}







