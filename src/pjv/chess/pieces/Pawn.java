package pjv.chess.pieces;

import pjv.chess.board.Board;
import pjv.chess.board.Move;
import pjv.chess.board.Tile;
import pjv.chess.board.Utils;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends ChessPiece{

    private final static int[] MOVE_OFFSETS = {7, 8, 9, 16}; //positive for black, negative for white

    int HALF_OF_ROWS = 4;
    int HALFPOINT = 3;
    int WHITE_COEFFICIENT = -1;
    int BLACK_COEFFICIENT = 1;

    public Pawn(int piecePosition, boolean alliance) {
        super(piecePosition, alliance);
    }

    @Override
    public List<Move> calculateAllLegalMoves(Board board) {
        int offsetCoefficient = this.alliance ? WHITE_COEFFICIENT : BLACK_COEFFICIENT; //white moves in negative direction and is "true"
        List<Move> legalMoves = new ArrayList<>();

        for (int currentOffset : MOVE_OFFSETS) {
            int destinationCoordinate = this.piecePosition + offsetCoefficient * currentOffset;
            Tile destinationTile = board.getTile(destinationCoordinate);

            if (Utils.isValidCoordinate(destinationCoordinate)) {
                ChessPiece pieceAtDestination;
                switch (currentOffset) {
                    case 8:
                        if (destinationTile.isEmpty()) {
                            //TODO possible promotion
                            legalMoves.add(new Move.DefaultMove(board, this, destinationCoordinate));
                        }
                        break;
                    case 16:
                        if (destinationTile.isEmpty() &&
                                board.getTile(destinationCoordinate).isEmpty() &&
                                ((Utils.getRowNumber(this.piecePosition) == 2 && !this.alliance) || //is black and is in 2nd row
                                        (Utils.getRowNumber(this.piecePosition) == 7 && this.alliance))) { //is white and is in 7th row
                            legalMoves.add(new Move.DefaultMove(board, this, destinationCoordinate));
                        }
                        break;
                    case 7:
                        pieceAtDestination = destinationTile.getPiece();
                        if(!destinationTile.isEmpty() && pieceAtDestination.alliance != this.alliance){
                            if((this.alliance && Utils.getColumnNumber(this.piecePosition) != 8) || /*is white*/
                                    (!this.alliance && Utils.getColumnNumber(this.piecePosition) != 1) /*is black*/){
                                legalMoves.add(new Move.AttackMove(board, this, destinationCoordinate, pieceAtDestination));
                            }
                        }
                        break;

                    case 9:
                        pieceAtDestination = destinationTile.getPiece();
                        if(!destinationTile.isEmpty() && pieceAtDestination.alliance != this.alliance){
                            if((this.alliance && Utils.getColumnNumber(this.piecePosition) != 1) || /*is white*/
                                    (!this.alliance && Utils.getColumnNumber(this.piecePosition) != 8) /*is black*/){
                                legalMoves.add(new Move.AttackMove(board, this, destinationCoordinate, pieceAtDestination));
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

}







