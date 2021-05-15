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
}
