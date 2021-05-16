package pjv.chess.players;

import pjv.chess.board.*;
import pjv.chess.pieces.ChessPiece;
import pjv.chess.pieces.King;
import pjv.chess.pieces.Rook;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class Player {
    Board board;
    King playerKing;
    boolean alliance;
    Collection<Move> myMoves;
    Collection<Move> opponentsMoves;

    ChessClock chessClock;


    public Player(Board board, boolean alliance, Collection<Move> whiteLegalMoves, Collection<Move> blackLegalMoves) {

        this.board = board;
        this.playerKing = setupKing();
        this.alliance = alliance;
        this.myMoves = alliance ? whiteLegalMoves : blackLegalMoves;
        this.opponentsMoves = !alliance ? whiteLegalMoves : blackLegalMoves;

        this.chessClock = new ChessClock();


        if(myMoves.addAll(calculateCastleMoves(myMoves, opponentsMoves, alliance))){
            //System.out.println("Added even more successfully");
        };
        //board.printAllLegalMovesOfPiece(board.getTile(Utils.WHITE_KING_POSITION));
        //board.printAllLegalMovesOfPiece(board.getTile(Utils.BLACK_KING_POSITION));
    }

    private King setupKing() {
        for(ChessPiece piece : getMyPieces()){
            if(piece.isKing()){
                return (King) piece;
            }
        }
        throw new RuntimeException("Invalid board, player has no King");
    }

    public Collection<ChessPiece> getMyPieces() {
        return this.alliance ? this.board.getWhitePieces() : this.board.getBlackPieces();
    }

    public Collection<ChessPiece> getOpponentsPieces(){
        return !this.alliance ? this.board.getWhitePieces() : this.board.getBlackPieces();
    }

    public Player getOpponent(){
        return this.alliance ? this.board.getBlackPlayer() : this.board.getWhitePlayer();
    }

    public King getPlayerKing() { return this.playerKing; }

    public Collection<Move> getMyMoves(){ return this.myMoves; }

    public boolean getAlliance(){ return this.alliance; }

    public ChessClock getChessClock(){ return this.chessClock; }

    Collection<Move> calculateCastleMoves(Collection<Move> playerLegalMoves, Collection<Move> opponentLegalMoves, boolean alliance){ //white = true;
        Collection<Move> kingCastleMoves = new ArrayList<>();
        int kingsPosition = alliance ? Utils.WHITE_KING_POSITION : Utils.BLACK_KING_POSITION;
        Tile closerRookTile = this.board.getTile(kingsPosition + Utils.CLOSER_ROOK_DISTANCE);
        Tile furtherRookTile = this.board.getTile(kingsPosition - Utils.FURTHER_ROOK_DISTANCE);

        if(this.playerKing.isFirstMove() && !this.isInCheck()){
            if(!closerRookTile.isEmpty() && closerRookTile.getPiece().isFirstMove()){
                if(isLegalForCastling(this.board.getTile(kingsPosition + Utils.CLOSER_ROOK_DISTANCE-2) , opponentLegalMoves) &&
                        isLegalForCastling(this.board.getTile(kingsPosition + Utils.CLOSER_ROOK_DISTANCE-1) , opponentLegalMoves)){
                    //System.out.println("I got here - short castle");
                    if(kingCastleMoves.add(new Move.ShortCastleMove(this.board, this.playerKing, kingsPosition +Utils.CLOSER_ROOK_DISTANCE-1,
                            (Rook) closerRookTile.getPiece(), closerRookTile.getTileCoordinates(), closerRookTile.getTileCoordinates()-Utils.CLOSER_ROOK_DISTANCE+1))){
                        //System.out.println("Added successfully");
                    };
                }

            } if (!furtherRookTile.isEmpty() && furtherRookTile.getPiece().isFirstMove()){
                if(isLegalForCastling(this.board.getTile(kingsPosition - Utils.FURTHER_ROOK_DISTANCE+2) , opponentLegalMoves) &&
                        isLegalForCastling(this.board.getTile(kingsPosition - Utils.FURTHER_ROOK_DISTANCE+1) , opponentLegalMoves)){
                    //System.out.println("I got here - long castle");
                    if(kingCastleMoves.add(new Move.LongCastleMove(this.board, this.playerKing, kingsPosition -Utils.FURTHER_ROOK_DISTANCE+2,
                        (Rook) closerRookTile.getPiece(), closerRookTile.getTileCoordinates(), closerRookTile.getTileCoordinates()+Utils.FURTHER_ROOK_DISTANCE-1))){
                        //System.out.println("Added successfully");
                    };
                }
            }
        }
        return kingCastleMoves;
    }

    public boolean isLegalMove(Move move){
        return this.myMoves.contains(move);
    }

    public boolean isLegalForCastling(Tile checkedTile, Collection<Move> opponentLegalMoves){
        if(checkedTile.isEmpty() && Player.calculateAttacksOnTile(checkedTile.getTileCoordinates(), opponentLegalMoves).isEmpty()){
            return true;
        } else {
            return false;
        }
    }

    public boolean isInCheck(){
        return !calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentsMoves).isEmpty();
    }
    public boolean isInCheckMate(){
        if(isInCheck() && !hasEscapeSquares()){
            return true;
        }
        return false;
    }

    public boolean hasEscapeSquares(){
        for(Move move: this.playerKing.calculateAllLegalMoves(this.board)){
            if(!calculateAttacksOnTile(move.getDestinationCoordinate(), this.opponentsMoves).isEmpty()){
                return true;
            }
        }
        return false;
    }

    public boolean isInStaleMate(){
        return false;
    }
    public boolean hasCastled(){
        return false;
    }

    public BoardTransition makeMove(Move move){

        if(!isLegalMove(move)){
                return new BoardTransition(this.board, move, Move.MoveStatus.ILLEGAL_MOVE);
            }

            Board newBoard = move.moveExecution();

            Collection<Move> kingsAttacks = Player.calculateAttacksOnTile(newBoard.getCurrentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                    newBoard.getCurrentPlayer().getMyMoves());

            if(!kingsAttacks.isEmpty()){ //king is in check
                return new BoardTransition(this.board, move, Move.MoveStatus.DOES_SELF_CHECK);
            }
            return new BoardTransition(newBoard, move, Move.MoveStatus.DONE);
    }


    private static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves){
        Collection<Move> attackMoves = new ArrayList<>();
        for(Move move : moves){
            if(piecePosition == move.getDestinationCoordinate()){
                attackMoves.add(move);
            }
        }
        return attackMoves;
    }

}
