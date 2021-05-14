package pjv.chess.board;

import jdk.jshell.execution.Util;
import pjv.chess.pieces.*;

import javax.management.ImmutableDescriptor;
import java.util.*;

public class Board {

    private List<Tile> chessBoard;
    Collection<ChessPiece> whitePieces;
    Collection<ChessPiece> blackPieces;

    private Board(Builder builder) {
        this.chessBoard = createChessBoard(builder);
        this.whitePieces = countActivePieces(this.chessBoard,true);
        this.blackPieces = countActivePieces(this.chessBoard, false);

        List<Move> whiteLegalMoves = calculateLegalMoves(this.whitePieces);
        List<Move> blackLegalMoves = calculateLegalMoves(this.blackPieces);
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

    private List<Move> calculateLegalMoves(Collection<ChessPiece> chessPieces) {
        List<Move> legalMoves = new ArrayList<>();

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

        builder.setPiece(new Rook(48,true));
        builder.setPiece(new Knight(49,true));
        builder.setPiece(new Bishop(50,true));
        builder.setPiece(new Queen(51,true));
        builder.setPiece(new King(52,true));
        builder.setPiece(new Bishop(53,true));
        builder.setPiece(new Knight(54, true));
        builder.setPiece(new Rook(55,true));
        builder.setPiece(new Pawn(56,true));
        builder.setPiece(new Pawn(57,true));
        builder.setPiece(new Pawn(58,true));
        builder.setPiece(new Pawn(59,true));
        builder.setPiece(new Pawn(60,true));
        builder.setPiece(new Pawn(61,true));
        builder.setPiece(new Pawn(62,true));
        builder.setPiece(new Pawn(63,true));
        //*/

        //white has the first move
        builder.activeTurn(true);

        return builder.build();
    }

    public Tile getTile(int tileCoord){

        return chessBoard.get(tileCoord);
    }

    @Override
    public String toString(){
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

    public static class Builder{

        Map<Integer, ChessPiece> boardConfig;
        boolean whitesTurn = true;

        public Builder(){
            this.boardConfig = new HashMap<>();
        }

        public Builder setPiece(ChessPiece piece){
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        public Builder activeTurn(boolean currentTurn) {
            this.whitesTurn = currentTurn;
            return this;
        }

        public Board build(){
            return new Board(this);
        }
    }
}
