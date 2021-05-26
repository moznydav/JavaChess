package pjv.chess.board;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PGNUtils {

    public static String SAVE_PATH = "saves/pgn/";

    private static String DEFAULT_EVENT = "[Event \"A match\"]\n";
    private static String DEFAULT_SITE = "[Site \"Prague, Czech Republic CZ\"]\n";
    private static String DEFAULT_ROUND = "[Round \"9\"]\n";
    private static String DEFAULT_WHITE = "[White \"Some dude\"]\n";
    private static String DEFAULT_BLACK = "[Black \"Some other dude\"]\n";

    private PGNUtils(){ throw new RuntimeException("Not instiable"); }


    /**
     * Creates board from PGN code
     * @param pgnString
     * @return new Board
     */
    public static Board createGameFromPGN(String pgnString){
        List<Board> allBoards = createExploreGameFromPGN(pgnString);
        return allBoards.get(allBoards.size() - 1);
    }

    /**
     * Saves move log to pgn string
     *
     * @param moveLog
     * @param whiteGain
     * @return pgn string
     */
    public static String saveGameToPGN(List<String> moveLog, int whiteGain){ //white gain = 0 - white lost, 1 - draw, 2  - win
        StringBuilder stringBuilder = new StringBuilder();
        String result;
        int moveCounter = 0;

        stringBuilder.append(DEFAULT_EVENT);
        stringBuilder.append(DEFAULT_SITE);
        stringBuilder.append("[Date \"" + LocalDate.now() + "\"]\n");
        stringBuilder.append(DEFAULT_ROUND);
        stringBuilder.append(DEFAULT_WHITE);
        stringBuilder.append(DEFAULT_BLACK);
        stringBuilder.append("[Result \"");

        switch (whiteGain){
            case 0:
                result = "0-1";
                break;
            case 1:
                result = "1/2-1/2";
                break;
            case 2:
                result = "1-0";
                break;
            default:
                result = "*";
        }
        stringBuilder.append(result + "\"]\n\n");


        for(String move : moveLog) {
            if(moveCounter % 2 == 0){
                stringBuilder.append((moveCounter / 2 + 1) + ". ");
            }
            stringBuilder.append(move + " ");
            moveCounter++;
        }
        if(result != "*"){
            stringBuilder.append(result);
        }

        return stringBuilder.toString();
    }

    /**
     * Creates list of board for player to explore
     * @param pgnString
     * @return list of boards
     */
    public static List<Board> createExploreGameFromPGN(String pgnString){
        List<Board> allBoards = new ArrayList<>();
        allBoards.add(Board.createStandardBoard());


        String[] pgnMoves = tokenizePGN(pgnString);


        int itemCount = 0;
        int halfMoveCount = 0;
        boolean isCurrentPlayerWhite = true;


        for(String moveString : pgnMoves){
            itemCount++;
            if(itemCount % 3 != 1){
                if(!moveString.equals("0-1") && !moveString.equals("1/2-1/2") && !moveString.equals("1-0")){
                    //System.out.println(getMoveFromPGN(moveString, isCurrentPlayerWhite, allBoards.get(halfMoveCount)));
                    System.out.println(moveString);


                    BoardTransition transition = allBoards.get(halfMoveCount).getCurrentPlayer().makeMove(getMoveFromPGN(moveString,
                            isCurrentPlayerWhite, allBoards.get(halfMoveCount)));

                    allBoards.add(transition.getNewBoard());

                    halfMoveCount++;
                    isCurrentPlayerWhite = !isCurrentPlayerWhite;
                }

            }
        }

        return allBoards;
    }

    /**
     * Main method for getting move that was given by pgn string
     *
     * @param pgnString
     * @param isCurrentPlayerWhite
     * @param currentBoard
     * @return move
     */
    public static Move getMoveFromPGN(String pgnString, boolean isCurrentPlayerWhite, Board currentBoard){
        int moveLength = pgnString.length();
        if(pgnString.charAt(moveLength-1) == '+'){ //'+' doesn't help me
            moveLength--;
        }
        //Collection<Move> myMoves = null;
        Collection<Move> myMoves = isCurrentPlayerWhite ? currentBoard.getWhitePlayer().getMyMoves() : currentBoard.getBlackPlayer().getMyMoves();

        switch(moveLength){

            case 2: //is PawnMove
                for(Move move : myMoves){
                    if(hasSameDestination(pgnString, move, 0) && move.isPawnMove()){
                        return move;
                    }
                }
                break;
            case 3: //is DefaultMove/ShortCastle
                switch (pgnString.charAt(0)){
                    case 'N':
                        for(Move move : myMoves) {
                            if (hasSameDestination(pgnString, move, 1) &&
                                    move.getMovedPiece().toString() == "N") {
                                return move;
                            }
                        }
                        break;
                    case 'B':
                        for(Move move : myMoves) {
                            if (hasSameDestination(pgnString, move, 1) &&
                                    move.getMovedPiece().toString() == "B") {
                                return move;
                            }
                        }
                        break;
                    case 'R':
                        for(Move move : myMoves) {
                            if (hasSameDestination(pgnString, move, 1) &&
                                    move.getMovedPiece().toString() == "R") {
                                return move;
                            }
                        }
                        break;
                    case 'Q':
                        for(Move move : myMoves) {
                            if (hasSameDestination(pgnString, move, 1) &&
                                    move.getMovedPiece().toString() == "Q") {
                                return move;
                            }
                        }
                        break;
                    case 'K':
                        for(Move move : myMoves) {
                            if (hasSameDestination(pgnString, move, 1) &&
                                    move.getMovedPiece().toString() == "K") {
                                return move;
                            }
                        }
                        break;
                    case 'O':
                        for(Move move : myMoves) {
                            if (move.isShortCastle()) {
                                return move;
                            }
                        }
                        break;

                }
                break;
            case 4: //AttackMove/Disambiguating DefaultMove/PawnAttackMove/Pawn Promotion
                if(Character.isUpperCase(pgnString.charAt(0))){ //AttackMove/Disambiguating DefaultMove
                    switch (pgnString.charAt(0)){
                        case 'N':
                            for(Move move : myMoves){
                                if (hasSameDestination(pgnString, move, 2) &&
                                        move.getMovedPiece().toString() == "N") {

                                    if (attackOrDisambiguatous(pgnString, move)) {
                                        return move;
                                    }
                                }
                            }

                            break;
                        case 'B':
                            for(Move move : myMoves) {
                                if (hasSameDestination(pgnString, move, 2) &&
                                        move.getMovedPiece().toString() == "B") {
                                    if(attackOrDisambiguatous(pgnString, move)) {
                                        return move;
                                    }
                                }
                            }
                            break;
                        case 'R':
                            for(Move move : myMoves) {
                                if (hasSameDestination(pgnString, move, 2) &&
                                        move.getMovedPiece().toString() == "R") {
                                    if(attackOrDisambiguatous(pgnString, move)) {
                                        return move;
                                    }
                                }
                            }
                            break;
                        case 'Q':
                            for(Move move : myMoves) {
                                if (hasSameDestination(pgnString, move, 2) &&
                                        move.getMovedPiece().toString() == "Q") {
                                    if(attackOrDisambiguatous(pgnString, move)) {
                                        return move;
                                    }
                                }
                            }
                            break;
                        case 'K':
                            for(Move move : myMoves) {
                                if (hasSameDestination(pgnString, move, 2) &&
                                        move.getMovedPiece().toString() == "K") {
                                    return move;
                                }
                            }
                            break;
                        case 'O':
                            for(Move move : myMoves) {
                                if (move.isShortCastle()) {
                                    return move;
                                }
                            }
                            break;

                        default:
                            throw new IllegalStateException("Unexpected value: " + pgnString.charAt(0));
                    }
                }
                else{//PawnAttackMove/PawnPromotion
                        for(Move move : myMoves){
                            if(isInCorrectColumn(pgnString, move, 0) && hasSameDestination(pgnString, move, 2) && pgnString.charAt(1) == 'x'){
                                return move;
                            } else if(hasSameDestination(pgnString, move, 0)){
                                return new Move.PawnPromotion(move, pgnString.charAt(3));
                            }
                        }
                }
                break;
            case 5: //is LongCastle/more Disambiguating moves
                for(Move move : myMoves){
                    if(pgnString.charAt(0) == 'O' && move.isLongCastle()){
                        return move;
                    }
                    if((hasSameDestination(pgnString, move, 3)) &&
                            ((singleDisambiguatousAttack(pgnString, move)) || (doubleDisambiguatous(pgnString, move, 1)))) {
                        return move;
                    }
                }

                break;
            case 6: //Disambiguating AttackMove
                for(Move move : myMoves){
                    if(doubleDisambiguatous(pgnString, move, 2)) {
                        return move;
                    }
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + moveLength);
        }
        return null;
    }

    private static boolean hasSameDestination(String pgnString, Move move, int start){
        String pgnCoordinates = pgnString.substring(start, start+2);


        if(move.getDestinationCoordinate() == Utils.getCoordinatesFromAlgebraicNotation(pgnCoordinates)){
            return true;
        }
        return false;
    }

    private static boolean isInCorrectRow(String pgnString, Move move, int start){
        if((move.getMovedPiece().getPiecePosition() / 8) == pgnString.charAt(start) - 49){
            return true;
        }
        return false;
    }

    private static boolean isInCorrectColumn(String pgnString, Move move, int start){
        if((move.getMovedPiece().getPiecePosition() % 8) == pgnString.charAt(start) - 97){
            return true;
        }
        return false;
    }

    private static boolean doubleDisambiguatous(String pgnString, Move move, int start){
        if(isInCorrectColumn(pgnString, move, start) && isInCorrectRow(pgnString, move, start+1)){
            return true;
        }
        return false;
    }

    private static boolean singleDisambiguatousAttack(String pgnString, Move move){
        if((pgnString.charAt(1) == 'x') &&
                ((Character.isDigit(pgnString.charAt(1))
                        && ((move.getMovedPiece().getPiecePosition() / 8) == pgnString.charAt(1) - 49)) ||
                ((move.getMovedPiece().getPiecePosition() % 8) == 104 - pgnString.charAt(1)))){
            return true;
        }
        return false;
    }

    private static boolean attackOrDisambiguatous(String pgnString, Move move) {
        if ((pgnString.charAt(1) == 'x') ||
                (Character.isDigit(pgnString.charAt(1))
                        && ((move.getMovedPiece().getPiecePosition() / 8) == 56 - pgnString.charAt(1) )) ||
                ((move.getMovedPiece().getPiecePosition() % 8) == pgnString.charAt(1) - 97) ) {
            return true;
        }
        return false;
    }

    private static String[] tokenizePGN(String pgnString){
        String[] tokenizedPGN = pgnString.split("]"); //info tokens + moves(last)

        String movesString = tokenizedPGN[tokenizedPGN.length-1].replace("\n"," ").replace("\r","");

        String[] pgnMoves = movesString.split("\\s");

        return Arrays.copyOfRange(pgnMoves, 2, pgnMoves.length);
    }

    /**
     * Checks move for disambiguating state
     *
     * @param pgnMove
     * @param currentBoard
     * @param previousMove
     * @return checked move pgn code
     */
    public static String checkedPGNMove(String pgnMove, Board currentBoard, Move previousMove){
        StringBuilder stringBuilder = new StringBuilder();
        for(Move move : currentBoard.getCurrentPlayer().getOpponent().getMyMoves()){
            if(hasSameDestination(pgnMove, move, pgnMove.length()-2)){
                stringBuilder.append(pgnMove.charAt(0));
                if(Utils.getColumnNumber(move.getMovedPiece().getPiecePosition()) == Utils.getColumnNumber(previousMove.getMovedPiece().getPiecePosition())){
                    stringBuilder.append(Utils.getAlgebraicRow(previousMove.getMovedPiece().getPiecePosition()));
                } else {
                    stringBuilder.append(Utils.getAlgebraicColumn(previousMove.getMovedPiece().getPiecePosition()));
                }
                for(int i = 1; i < pgnMove.length(); i++){
                    stringBuilder.append(pgnMove.charAt(i));
                }
                return stringBuilder.toString();
            }
        }
        return pgnMove;
    }

}
