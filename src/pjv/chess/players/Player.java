package pjv.chess.players;

import pjv.chess.board.*;
import pjv.chess.gui.PlayerPanel;
import pjv.chess.pieces.ChessPiece;
import pjv.chess.pieces.King;
import pjv.chess.pieces.Rook;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class that contains and handles all Player related things, such as player clock, pieces, moves and game state(check, mate, stalemate)
 *
 * @author David Mozny
 */
public class Player {
    Board board;
    King playerKing;
    boolean alliance;
    Collection<Move> myMoves;
    Collection<Move> opponentsMoves;
    boolean isInCheck;

    int timeLeft;
    ChessClock chessClock;
    PlayerPanel playerPanel;


    public Player(Board board, boolean alliance, Collection<Move> whiteLegalMoves, Collection<Move> blackLegalMoves, int timeLeft, PlayerPanel playerPanel) {

        this.board = board;
        this.alliance = alliance;
        this.myMoves = alliance ? whiteLegalMoves : blackLegalMoves;
        this.opponentsMoves = !alliance ? whiteLegalMoves : blackLegalMoves;

        this.playerKing = setupKing();
        this.isInCheck = !calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentsMoves).isEmpty();
        myMoves.addAll(calculateCastleMoves(opponentsMoves, this.alliance));

        this.playerPanel = playerPanel;
        this.timeLeft = timeLeft;
        this.chessClock = new ChessClock(timeLeft, this.alliance, this.playerPanel);

    }

    private King setupKing() {
        for(ChessPiece piece : getMyPieces()){
            if(piece.isKing()){
                return (King) piece;
            }
        }
        throw new RuntimeException("Invalid board, player has no King");
    }

    /**
     * All getters used by class Player
     * @return
     */
    public Collection<ChessPiece> getMyPieces() { return this.alliance ? this.board.getWhitePieces() : this.board.getBlackPieces(); }

    public Collection<ChessPiece> getOpponentsPieces(){ return !this.alliance ? this.board.getWhitePieces() : this.board.getBlackPieces(); }

    public Player getOpponent(){ return this.alliance ? this.board.getBlackPlayer() : this.board.getWhitePlayer(); }

    public King getPlayerKing() { return this.playerKing; }

    public Collection<Move> getMyMoves(){ return this.myMoves; }

    public boolean getAlliance(){ return this.alliance; }

    public ChessClock getChessClock(){ return this.chessClock; }

    public PlayerPanel getPlayerPanel(){ return this.playerPanel;}

    /**
     * All setters used by class Player
     * @param playerPanel
     */

    public void setPlayerPanel(PlayerPanel playerPanel){ this.playerPanel = playerPanel; }

    /**
     * Calculates available castle moves of player
     * @param opponentLegalMoves
     * @param alliance
     * @return collection of castle moves
     */
    public Collection<Move> calculateCastleMoves(Collection<Move> opponentLegalMoves, boolean alliance){ //white = true;
        Collection<Move> kingCastleMoves = new ArrayList<>();
        int kingsPosition = alliance ? Utils.WHITE_KING_POSITION : Utils.BLACK_KING_POSITION;
        Tile closerRookTile = this.board.getTile(kingsPosition + Utils.CLOSER_ROOK_DISTANCE);
        Tile furtherRookTile = this.board.getTile(kingsPosition - Utils.FURTHER_ROOK_DISTANCE);

        if(this.playerKing.isFirstMove() && !this.isInCheck()){
            if(!closerRookTile.isEmpty() && closerRookTile.getPiece().isFirstMove()){
                if(isLegalForCastling(this.board.getTile(kingsPosition + Utils.CLOSER_ROOK_DISTANCE-2) , opponentLegalMoves) &&
                        isLegalForCastling(this.board.getTile(kingsPosition + Utils.CLOSER_ROOK_DISTANCE-1) , opponentLegalMoves)){
                    if(kingCastleMoves.add(new Move.ShortCastleMove(this.board, this.playerKing, kingsPosition +Utils.CLOSER_ROOK_DISTANCE-1,
                            (Rook) closerRookTile.getPiece(), closerRookTile.getTileCoordinates(), closerRookTile.getTileCoordinates()-Utils.CLOSER_ROOK_DISTANCE+1))){
                    };
                }

            } if (!furtherRookTile.isEmpty() && furtherRookTile.getPiece().isFirstMove()){
                if(isLegalForCastling(this.board.getTile(kingsPosition - Utils.FURTHER_ROOK_DISTANCE+3) , opponentLegalMoves) &&
                        isLegalForCastling(this.board.getTile(kingsPosition - Utils.FURTHER_ROOK_DISTANCE+2) , opponentLegalMoves) &&
                        isLegalForCastling(this.board.getTile(kingsPosition - Utils.FURTHER_ROOK_DISTANCE+1) , opponentLegalMoves)){
                    if(kingCastleMoves.add(new Move.LongCastleMove(this.board, this.playerKing, kingsPosition -Utils.FURTHER_ROOK_DISTANCE+2,
                        (Rook) furtherRookTile.getPiece(), furtherRookTile.getTileCoordinates(), furtherRookTile.getTileCoordinates()+Utils.FURTHER_ROOK_DISTANCE-1))){
                        //System.out.println("Added successfully");
                    };
                }
            }
        }
        return kingCastleMoves;
    }

    private boolean isLegalMove(Move move){
        return this.myMoves.contains(move);
    }

    private boolean isLegalForCastling(Tile checkedTile, Collection<Move> opponentLegalMoves){
        if(checkedTile.isEmpty() && Player.calculateAttacksOnTile(checkedTile.getTileCoordinates(), opponentLegalMoves).isEmpty()){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if player is in check
     * @return true/false
     */
    public boolean isInCheck(){
        return !calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentsMoves).isEmpty();
    }
    /**
     * Checks if player is in checkmate
     * @return true/false
     */
    public boolean isInCheckMate(){
        if(isInCheck() && !hasMoves()){
            return true;
        }
        return false;
    }
    /**
     * Checks if player is in stalemate
     * @return true/false
     */
    public boolean isInStaleMate(){
        return !this.isInCheck && !hasMoves();
    }

    private boolean hasMoves(){
        for(Move move: this.getMyMoves()){
            BoardTransition newBoard = makeMove(move);
            if(newBoard.getMoveStatus().isDone()){
                return true;
            }
        }
        return false;
    }

    /**
     * Starts player chess clock, also handles run method of chessClock every second
     */
    public void startClock(){
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(this.chessClock, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Stops player chess clock
     */
    public void stopClock(){
        this.chessClock.requestStop();
        this.playerPanel.update(this.chessClock.getTimeLeft());
    }

    /**
     * One of the most important player methods that handles execution of chosen move and returns BoardTransition which is the result of given move
     * @param move
     * @return new BoardTransition
     */
    public BoardTransition makeMove(Move move){

        if(!isLegalMove(move)){
                return new BoardTransition(this.board, this.board, move, Move.MoveStatus.ILLEGAL_MOVE);
            }
            Board newBoard = move.moveExecution();

            Collection<Move> kingsAttacks = calculateAttacksOnTile(newBoard.getCurrentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                    newBoard.getCurrentPlayer().getMyMoves());

            if(!kingsAttacks.isEmpty()){ //king is in check
                return new BoardTransition(this.board, this.board, move, Move.MoveStatus.DOES_SELF_CHECK);
            }
            return new BoardTransition(this.board, newBoard, move, Move.MoveStatus.DONE);
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

    /**
     * Checks if player is king side castle capable
     * @return true/false
     */
    public boolean isKingSideCastleCapable() {
        int kingSideRookCoordinate = this.alliance ? 63 : 7;
        if(this.playerKing.isFirstMove() && this.board.getTile(kingSideRookCoordinate).getPiece().isFirstMove()){
            return true;
        }
        return false;
    }
    /**
     * Checks if player is queen side castle capable
     * @return true/false
     */
    public boolean isQueenSideCastleCapable() {
        int QueenSideRookCoordinate = this.alliance ? 56 : 0;
        if(this.playerKing.isFirstMove() && this.board.getTile(QueenSideRookCoordinate).getPiece().isFirstMove()){
            return true;
        }
        return false;
    }
}
