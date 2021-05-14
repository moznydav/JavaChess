package pjv.chess.pieces;

import pjv.chess.board.Board;
import pjv.chess.board.Move;
import pjv.chess.board.Tile;
import pjv.chess.board.Utils;

import java.util.ArrayList;
import java.util.List;

public class Knight extends ChessPiece{

    private final static int[] MOVE_OFFSETS = {-17, -15, -10, -6, 6, 10, -15, -17}; //offsets from current location for "knight move"

    public Knight(int piecePosition, boolean alliance) {
        super(piecePosition, alliance);
    }

    @Override
    public List<Move> calculateAllLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<>();

        for(int currentOffset: MOVE_OFFSETS){

            int destinationCoordinate = this.piecePosition + currentOffset;

            if(Utils.isValidCoordinate(destinationCoordinate) && !isOverEdgeMove(this.piecePosition, currentOffset)) {
                Tile destinationTile = board.getTile(destinationCoordinate);

                if(destinationTile.isEmpty()) {
                    legalMoves.add(new Move.DefaultMove(board, this, destinationCoordinate));
                } else {
                    ChessPiece pieceAtDestination = destinationTile.getPiece();
                    boolean alliance = pieceAtDestination.alliance;

                    if(alliance != this.alliance){ //
                        legalMoves.add(new Move.AttackMove(board, this, destinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }
        return legalMoves;
    }
    @Override
    public String toString(){ return "N"; }


    private boolean isOverEdgeMove(int piecePosition, int offset){

        int columnNumber = Utils.getColumnNumber(piecePosition);
        int rowNumber = Utils.getRowNumber(piecePosition);

        switch(columnNumber){
            case 1:
                if(offset == -17 || offset == -10 || offset == 6  || offset == 15){
                    return true;
                }
                break;
            case 2:
                if(offset == -10 || offset == 6){
                    return true;
                }
                break;
            case 7:
                if(offset == -6 || offset == 10){
                    return true;
                }
                break;
            case 8:
                if(offset == -15 || offset == -6 || offset == 10 || offset == 17){
                    return true;
                }
                break;
        }
        switch (rowNumber){
            case 1:
                if(offset == -17 || offset == -15 || offset == -10  || offset == -6){
                    return true;
                }
                break;
            case 2:
                if(offset == -17 || offset == -15){
                    return true;
                }
                break;
            case 7:
                if(offset == 15 || offset == 17){
                    return true;
                }
                break;
            case 8:
                if(offset == 6 || offset == 10 || offset == 15 || offset == 17){
                    return true;
                }
                break;
        }

        return false;
    }

}
