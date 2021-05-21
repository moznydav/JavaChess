package pjv.chess.board;

public class FENUtils {

    private FENUtils(){
        throw new RuntimeException("Not instantiable");
    }

    public static Board createGameFromFEN(String fenString){
        return null;
    }
    public static String saveGameToFEN(Board board){
        return calculateBoardText() + " " +
                calculateCurrentPlayerText() + " " +
                calculateCasteText() + " " + calculateEnPassantSquare(board) +
                " " + "0, 1"; //TODO halfmoveClock & fullMoveNumber
    }
}
