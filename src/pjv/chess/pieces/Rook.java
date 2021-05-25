package pjv.chess.pieces;

import pjv.chess.board.Board;
import pjv.chess.board.Move;
import pjv.chess.board.Tile;
import pjv.chess.board.Utils;

import java.util.ArrayList;
import java.util.List;

public class Rook extends ChessPiece{
    private final static int[] MOVE_OFFSETS = {-8, -1, 1, 8}; //offsets from current location for "rook move"

    public Rook(int piecePosition, boolean alliance) {
        super(piecePosition, alliance, true);
    }

    public Rook(int piecePosition, boolean alliance, boolean isFirstMove){
        super(piecePosition, alliance, isFirstMove);
    }

    @Override
    public List<Move> calculateAllLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<>();

        for(int currentOffset: MOVE_OFFSETS){
            int destinationCoordinate = this.piecePosition;

            while(Utils.isValidCoordinate(destinationCoordinate+currentOffset) && !isOverEdgeMove(this.piecePosition, currentOffset)){
                destinationCoordinate += currentOffset;
                Tile destinationTile = board.getTile(destinationCoordinate);

                if(destinationTile.isEmpty()) {
                    legalMoves.add(new Move.DefaultMove(board, this, destinationCoordinate));
                } else {
                    ChessPiece pieceAtDestination = destinationTile.getPiece();
                    boolean alliance = pieceAtDestination.alliance;

                    if (alliance != this.alliance) { //
                        legalMoves.add(new Move.AttackMove(board, this, destinationCoordinate, pieceAtDestination));
                    }
                    break;
                }
            }


        }
        return legalMoves;
    }

    @Override
    public String toString(){ return "R"; }


    @Override
    public boolean isKing(){
        return false;
    }

    @Override
    public ChessPiece movePiece(Move move) {

        return new Rook(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance(), false);
    }

    private boolean isOverEdgeMove(int piecePosition, int offset){

        int columnNumber = Utils.getColumnNumber(piecePosition);

        switch(columnNumber){
            case 1:
                if(offset == -1){
                    return true;
                }
                break;
            case 8:
                if(offset == 1){
                    return true;
                }
        }
        return false;
    }
}

