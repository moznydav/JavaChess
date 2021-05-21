package pjv.chess.board;

import jdk.jshell.execution.Util;
import pjv.chess.gui.PlayerPanel;
import pjv.chess.pieces.*;
import pjv.chess.players.Player;

import javax.management.ImmutableDescriptor;
import java.util.*;

public class Board {
    private static int DEFAULT_TIME = 300;

    private List<Tile> chessBoard;
    Collection<ChessPiece> whitePieces;
    Collection<ChessPiece> blackPieces;

    private Player whitePlayer;
    private Player blackPlayer;
    private Player currentPlayer;

    public int whiteTime;
    public int blackTime;

    public PlayerPanel whitePlayerPanel;
    public PlayerPanel blackPlayerPanel;

    private Pawn enPassantPawn;

    private Board(Builder builder) {
        this.chessBoard = createChessBoard(builder);
        this.whitePieces = countActivePieces(this.chessBoard,true);
        this.blackPieces = countActivePieces(this.chessBoard, false);
        this.enPassantPawn = builder.enPassantPawn;

        Collection<Move> whiteLegalMoves = calculateLegalMoves(this.whitePieces);
        Collection<Move> blackLegalMoves = calculateLegalMoves(this.blackPieces);

        this.whitePlayerPanel = whitePlayerPanel;
        this.blackPlayerPanel = blackPlayerPanel;

        //boolean isWhitesTurn = true;
        this.whiteTime = builder.whiteTime;
        this.blackTime = builder.blackTime;

        this.whitePlayerPanel = builder.whitePlayerPanel;
        this.blackPlayerPanel = builder.blackPlayerPanel;

        this.whitePlayer = new Player(this, true, whiteLegalMoves, blackLegalMoves, whiteTime, whitePlayerPanel);
        this.blackPlayer = new Player(this, false, whiteLegalMoves, blackLegalMoves, blackTime, blackPlayerPanel);
        this.currentPlayer = builder.thisTurn ? this.whitePlayer : this.blackPlayer;
        //this.currentPlayer.startClock();

    }

    private List<ChessPiece> countActivePieces(List<Tile> chessBoard, boolean alliance){
        List<ChessPiece> activePieces = new ArrayList<>();

        for(Tile tile : chessBoard){
            if(!tile.isEmpty()){
                ChessPiece piece = tile.getPiece();
                if(piece.getPieceAlliance() == alliance){
                    activePieces.add(piece);
                }
            }
        }
    return activePieces;
    }

    public Collection<ChessPiece> getWhitePieces() {
        return this.whitePieces;
    }
    public Collection<ChessPiece> getBlackPieces(){
        return this.blackPieces;
    }

    public Player getWhitePlayer(){
        return this.whitePlayer;
    }

    public Player getBlackPlayer(){
        return this.blackPlayer;
    }

    public Player getCurrentPlayer(){ return this.currentPlayer; }

    public Pawn getEnPassantPawn(){ return this.enPassantPawn; }

    public Collection<Move> getAllLegalMoves(){

        Collection<Move> allLegalMoves = this.whitePlayer.getMyMoves();

        allLegalMoves.addAll(this.blackPlayer.getMyMoves());

        return allLegalMoves;
    }


    private Collection<Move> calculateLegalMoves(Collection<ChessPiece> chessPieces) {
        Collection<Move> legalMoves = new ArrayList<>();

        for(final ChessPiece chessPiece : chessPieces){
            legalMoves.addAll(chessPiece.calculateAllLegalMoves(this));
        }
        return legalMoves;
    }

    private static List<Tile> createChessBoard(Builder builder) {
        Tile[] tiles = new Tile[Utils.TILE_COUNT];
        for(int i = 0; i < Utils.TILE_COUNT; i++){
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }
        return Arrays.asList(tiles);

    }

    public static Board createStandardBoard(){
        Builder builder = new Builder();

        //black pieces
        builder.setPiece(new Rook(0, false));
        builder.setPiece(new Knight(1, false));
        builder.setPiece(new Bishop(2, false));
        builder.setPiece(new Queen(3, false));
        builder.setPiece(new King(4, false));
        builder.setPiece(new Bishop(5, false));
        builder.setPiece(new Knight(6, false));
        builder.setPiece(new Rook(7, false));
        builder.setPiece(new Pawn(8, false));
        builder.setPiece(new Pawn(9, false));
        builder.setPiece(new Pawn(10, false));
        builder.setPiece(new Pawn(11, false));
        builder.setPiece(new Pawn(12, false));
        builder.setPiece(new Pawn(13, false));
        builder.setPiece(new Pawn(14, false));
        builder.setPiece(new Pawn(15, false));
        //*/

        //white pieces
        builder.setPiece(new Rook(56,true));
        builder.setPiece(new Knight(57,true));
        builder.setPiece(new Bishop(58,true));
        builder.setPiece(new Queen(59,true));
        builder.setPiece(new King(60,true));
        builder.setPiece(new Bishop(61,true));
        builder.setPiece(new Knight(62, true));
        builder.setPiece(new Rook(63,true));
        builder.setPiece(new Pawn(48,true));
        builder.setPiece(new Pawn(49,true));
        builder.setPiece(new Pawn(50,true));
        builder.setPiece(new Pawn(51,true));
        builder.setPiece(new Pawn(52,true));
        builder.setPiece(new Pawn(53,true));
        builder.setPiece(new Pawn(54,true));
        builder.setPiece(new Pawn(55,true));
        //*/

        //white has the first move
        builder.setNextTurn(true);
        builder.keepWhiteTime(DEFAULT_TIME);
        builder.keepBlackTime(DEFAULT_TIME);
        builder.keepWhitePlayerPanel(new PlayerPanel(true));
        builder.keepBlackPanel(new PlayerPanel(false));

        return builder.build();
    }

    public Tile getTile(int tileCoord){

        return chessBoard.get(tileCoord);
    }

    public int getWhiteTime(){ return this.whiteTime; }

    public int getBlackTime(){ return this.blackTime; }


    @Override
    public String toString(){ //used for debugging
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < Utils.TILE_COUNT; i++){
            String tileText = this.chessBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if((i+1) % Utils.ROW_LENGTH == 0){
                builder.append("\n");
            }
        }
        return builder.toString();
    }
    public void printAllLegalMovesOfPiece(Tile tile){ //used for debugging
        for(Move move : tile.getPiece().calculateAllLegalMoves(this)){
            System.out.println(tile.getPiece() + "'s legal move is on tile " + move.getDestinationCoordinate());
        }
    }

    public static class Builder{

        Map<Integer, ChessPiece> boardConfig;
        boolean thisTurn;
        Pawn enPassantPawn;
        private int whiteTime;
        private int blackTime;
        private PlayerPanel whitePlayerPanel;
        private PlayerPanel blackPlayerPanel;


        public Builder(){
            this.boardConfig = new HashMap<>();
        }

        public Builder setPiece(ChessPiece piece){
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        public Player chooseActivePlayer(Player whitePlayer, Player blackPlayer, boolean isWhitesTurn){
            return isWhitesTurn ? whitePlayer : blackPlayer;
        }

        public Builder setNextTurn(boolean nextTurn){ //true - white
            this.thisTurn = nextTurn;
            return this;
        }

        public Builder keepWhitePlayerPanel(PlayerPanel whitePlayerPanel){
            this.whitePlayerPanel = whitePlayerPanel;
            return this;
        }

        public Builder keepBlackPanel(PlayerPanel blackPlayerPanel){
            this.blackPlayerPanel = blackPlayerPanel;
            return this;
        }

        public Builder keepWhiteTime(int time){
            this.whiteTime = time;
            return this;
        }

        public Builder keepBlackTime(int time){
            this.blackTime = time;
            return this;
        }

        public Board build(){
            return new Board(this);
        }

        public void setEnPassantPawn(Pawn movedPawn) {
            this.enPassantPawn = movedPawn;
        }
    }
}
