package pjv.chess.board;

public enum Utils {;

    public static int TILE_COUNT = 64;
    public static int ROW_LENGTH = 8;
    public static int COLUMN_HEIGHT = 8;
    public static int WHITE_KING_POSITION = 60;
    public static int BLACK_KING_POSITION = 4;
    public static int CLOSER_ROOK_DISTANCE = 3;
    public static int FURTHER_ROOK_DISTANCE = 4;

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
        stringBuilder.append(String.valueOf(96 + getColumnNumber(piecePosition)));
        stringBuilder.append(String.valueOf(57 - getRowNumber(piecePosition)));

        return stringBuilder.toString();
    }

    public static int getCoordinatesFromAlgebraicNotation(String algebraicNotation){
        int columnNumber = algebraicNotation.charAt(0) - 96;;
        int rowNumber = 57 - algebraicNotation.charAt(1);

        return (columnNumber - 1) * 8 + rowNumber - 1;
    }
}
