package pjv.chess.pieces;

import pjv.chess.board.Board;
import pjv.chess.board.Move;
import pjv.chess.board.Tile;
import pjv.chess.board.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class King extends ChessPiece{
    private final static int[] MOVE_OFFSETS = {-9, -8, -7, -1, 1, 7, 8, 9}; //offsets from current location for "king move"

    public King(int piecePosition, boolean alliance) {
        super(piecePosition, alliance, true);
    }

    public King(int piecePosition, boolean alliance, boolean isFirstMove){
        super(piecePosition, alliance, isFirstMove);
    }

    @Override
    public List<Move> calculateAllLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<>();

        for (int currentOffset : MOVE_OFFSETS) {

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
    public String toString(){ return "K"; }

    @Override
    public boolean isKing(){
        return true;
    }

    @Override
    public ChessPiece movePiece(Move move) {

        return new King(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance(), false);
    }

    public void addCastleMoves(Collection<Move> castleMoves){

    }

    private boolean isOverEdgeMove(int piecePosition, int offset){

        int columnNumber = Utils.getColumnNumber(piecePosition);

        switch(columnNumber){
            case 1:
                if(offset == -9 || offset == -1 || offset == 7 ){
                    return true;
                }
                break;
            case 8:
                if(offset == -7  || offset == 1 || offset == 9){
                    return true;
                }
        }
        return false;
    }

}
