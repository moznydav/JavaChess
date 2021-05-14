package pjv.chess.board;

public enum Utils {;

    public static int TILE_COUNT = 64;
    public static int ROW_LENGTH = 8;
    public static int COLUMN_HEIGHT = 8;

    public static int getColumnNumber(int piecePosition){
        return piecePosition % 8;
    }
    public static int getRowNumber(int piecePosition){
        return piecePosition / 8;
    }

    public static boolean isValidCoordinate(int coordinate){
       return (coordinate >=0 && coordinate < 64);
    }
}
