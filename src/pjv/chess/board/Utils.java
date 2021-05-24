package pjv.chess.board;

import java.util.Collection;
import java.util.Random;

public enum Utils {;

    public static int TILE_COUNT = 64;
    public static int ROW_LENGTH = 8;
    public static int COLUMN_HEIGHT = 8;
    public static int WHITE_KING_POSITION = 60;
    public static int BLACK_KING_POSITION = 4;
    public static int CLOSER_ROOK_DISTANCE = 3;
    public static int FURTHER_ROOK_DISTANCE = 4;


    public static int DEFAULT_TIME = 300;
    public static int DEFAULT_INCREMENT = 10;


    public static int getColumnNumber(int piecePosition){
        return piecePosition % 8 + 1;
    }
    public static int getRowNumber(int piecePosition){
        return piecePosition / 8 + 1;
    }

    public static boolean isValidCoordinate(int coordinate){
       return (coordinate >=0 && coordinate < 64);
    }

    public static String getAlgebraicNotation(int piecePosition){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Character.toString((char) 96 + getColumnNumber(piecePosition)));
        stringBuilder.append(Character.toString((char) 57 - getRowNumber(piecePosition)));

        return stringBuilder.toString();
    }

    public static int getCoordinatesFromAlgebraicNotation(String algebraicNotation){
        int columnNumber = algebraicNotation.charAt(0) - 96;;
        int rowNumber = 57 - algebraicNotation.charAt(1);

        return (rowNumber - 1) * 8 + columnNumber - 1;
    }


    public static String getAlgebraicColumn(int piecePosition){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Character.toString((char) 96 + getColumnNumber(piecePosition)));
        return stringBuilder.toString();
    }

    public static String getAlgebraicRow(int piecePosition){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Character.toString((char) 57 - getRowNumber(piecePosition)));
        return stringBuilder.toString();
    }

    public static Move getRandomMove(Board board){
        Collection<Move> moves = board.getCurrentPlayer().getMyMoves();
        Random random = new Random();
        int i = random.nextInt(moves.size());
        return (Move) moves.toArray()[i];

    }
}
