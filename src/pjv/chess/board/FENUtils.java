package pjv.chess.board;

import jdk.jshell.execution.Util;
import pjv.chess.gui.PlayerPanel;
import pjv.chess.pieces.*;

public class FENUtils {

    public static String SAVE_PATH = "saves/fen/";

    private FENUtils(){
        throw new RuntimeException("Not instantiable");
    }

    public static Board createGameFromFEN(String fenString){
        Board.Builder builder = new Board.Builder();

        String[] tokenizedFEN = tokenizeFEN(fenString);
        String[] tokenizedBoard = tokenizedFEN[0].split("/");

        builder.setNextTurn(tokenizedFEN[1] == "w" ? false : true);
        builder.keepWhiteTime(Utils.DEFAULT_TIME - Utils.DEFAULT_INCREMENT);
        builder.keepBlackTime(Utils.DEFAULT_TIME);
        builder.keepWhitePlayerPanel(new PlayerPanel(true));
        builder.keepBlackPanel(new PlayerPanel(false));

        boolean whiteKingSideCapable = false;
        boolean whiteQueenSideCapable = false;
        boolean blackKingSideCapable = false;
        boolean blackQueenSideCapable = false;

        int enPassantPawnCoordinate = -1;

        for(char character : tokenizedFEN[2].toCharArray()){
            switch (character){
                case 'K':
                    whiteKingSideCapable = true;
                    break;
                case 'Q':
                    whiteQueenSideCapable = true;
                    break;
                case 'k':
                    blackKingSideCapable = true;
                    break;
                case 'q':
                    blackQueenSideCapable = true;
                    break;
            }
        }
        if(tokenizedFEN[3] != "-" && tokenizedFEN.length == 2){
            enPassantPawnCoordinate = Utils.getCoordinatesFromAlgebraicNotation(tokenizedFEN[3]);
        }

        for(int i = 0; i < Utils.ROW_LENGTH; i++){
            int charPosition = 0;
            for(int j = 0; j < Utils.COLUMN_HEIGHT; j++){
                int coordinate = i * 8 + j;
                switch (tokenizedBoard[i].charAt(charPosition)) {
                    case 'p':
                        if(enPassantPawnCoordinate != -1 && enPassantPawnCoordinate == coordinate){
                            builder.setEnPassantPawn(new Pawn(coordinate, false));
                        } else {
                            builder.setPiece(new Pawn(coordinate, false));
                        }
                        break;
                    case 'P':
                        if(enPassantPawnCoordinate != -1 && enPassantPawnCoordinate == coordinate){
                            builder.setEnPassantPawn(new Pawn(coordinate, true));
                        } else {
                            builder.setPiece(new Pawn(coordinate, true));
                        }
                        break;
                    case 'n':
                        builder.setPiece(new Knight(coordinate, false));
                        break;
                    case 'N':
                        builder.setPiece(new Knight(coordinate, true));
                        break;
                    case 'b':
                        builder.setPiece(new Bishop(coordinate, false));
                        break;
                    case 'B':
                        builder.setPiece(new Bishop(coordinate, true));
                        break;
                    case 'r':
                        if((coordinate == 0 && !blackQueenSideCapable) ||
                                (coordinate == 7 && !blackKingSideCapable)){
                            builder.setPiece(new Rook(coordinate, false, false));
                        } else {
                            builder.setPiece(new Rook(coordinate, false, true));
                        }
                        break;
                    case 'R':
                        if((coordinate == 56 && !whiteQueenSideCapable) ||
                                (coordinate == 63 && !whiteKingSideCapable)){
                            builder.setPiece(new Rook(coordinate, true, false));
                        } else {
                            builder.setPiece(new Rook(coordinate, true, true));
                        }
                        break;
                    case 'q':
                        builder.setPiece(new Queen(coordinate, false));
                        break;
                    case 'Q':
                        builder.setPiece(new Queen(coordinate, true));
                        break;
                    case 'k':
                        builder.setPiece(new King(coordinate, false));
                        break;
                    case 'K':
                        builder.setPiece(new King(coordinate, true));
                        break;
                    default:
                        j += tokenizedBoard[i].charAt(charPosition) - 48;
                }
                charPosition++;
            }
        }
        return builder.build();
    }

    private static String[] tokenizeFEN(String fenString){
        String[] tokenizedFEN = fenString.split("\\s");
        return tokenizedFEN;
    }

    public static String saveGameToFEN(Board board){
        return calculateBoardText(board) + " " +
                calculateCurrentPlayerText(board) + " " +
                calculateCastleText(board) + " " + calculateEnPassantSquare(board) +
                " " + "0, 1"; //TODO halfmoveClock & fullMoveNumber
    }

    private static String calculateBoardText(Board board){
        StringBuilder stringBuilder = new StringBuilder();
        Tile tile;
        int emptyTiles;

        for(int i = 0; i < Utils.COLUMN_HEIGHT; i++){
            emptyTiles = 0;
            for(int j = 0; j < Utils.ROW_LENGTH; j++){
               tile = board.getTile((i * 8) + j);
               if(!tile.isEmpty()){
                    if(emptyTiles != 0){
                        stringBuilder.append(emptyTiles);
                    }
                    stringBuilder.append(tile.toString());
                    emptyTiles = 0;
               } else {
                   emptyTiles++;
                   if(j == 7){
                       stringBuilder.append(emptyTiles);
                   }
               }
            }
            if(i != 7 ){
                stringBuilder.append("/");
            }
        }
        return stringBuilder.toString();
    }

    private static String calculateCurrentPlayerText(Board board){
        return board.getCurrentPlayer().getAlliance() ? "w" : "b";
    }

    private static String calculateCastleText(Board board){
        StringBuilder stringBuilder = new StringBuilder();

        if(board.getWhitePlayer().isKingSideCastleCapable()){
            stringBuilder.append("K");
        }
        if(board.getWhitePlayer().isQueenSideCastleCapable()){
            stringBuilder.append("Q");
        }
        if(board.getBlackPlayer().isKingSideCastleCapable()){
            stringBuilder.append("k");
        }
        if(board.getBlackPlayer().isQueenSideCastleCapable()){
            stringBuilder.append("q");
        }
        String castleText = stringBuilder.toString();
        return castleText.isEmpty() ? "-" : castleText;
    }

    private static String calculateEnPassantSquare(Board board){
        Pawn enPassantPawn = board.getEnPassantPawn();

        if(enPassantPawn != null){
            return Utils.getAlgebraicNotation(enPassantPawn.getPiecePosition());
        }
        return "-";
    }
}
