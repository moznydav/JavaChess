package pjv.chess.tests;

import org.junit.Test;
import pjv.chess.board.Board;
import pjv.chess.board.BoardTransition;
import pjv.chess.board.Move;
import pjv.chess.board.Utils;
import pjv.chess.pieces.King;
import pjv.chess.pieces.Pawn;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    public void initialBoard() {

        final Board board = Board.createStandardBoard();
        assertEquals(board.getCurrentPlayer().getMyMoves().size(), 20);
        assertEquals(board.getCurrentPlayer().getOpponent().getMyMoves().size(), 20);
        assertFalse(board.getCurrentPlayer().isInCheck());
        assertFalse(board.getCurrentPlayer().isInCheckMate());
        assertTrue(board.getCurrentPlayer().isKingSideCastleCapable());
        assertTrue(board.getCurrentPlayer().isQueenSideCastleCapable());
        assertEquals(board.getCurrentPlayer(), board.getWhitePlayer());
        assertEquals(board.getCurrentPlayer().getOpponent(), board.getBlackPlayer());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheck());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheckMate());
        assertTrue(board.getCurrentPlayer().getOpponent().isKingSideCastleCapable());
        assertTrue(board.getCurrentPlayer().getOpponent().isQueenSideCastleCapable());
    }
    @Test
    public void testKingMove() {

        final Board.Builder builder = new Board.Builder();
        // Black Layout
        builder.setPiece(new King(4, false, false));
        builder.setPiece(new Pawn(12, false));
        // White Layout
        builder.setPiece(new Pawn(52, true));
        builder.setPiece(new King(60, true, false));
        builder.setNextTurn(true);
        // Set the current player
        Board board = builder.build();
        //System.out.println(board);

        assertEquals(board.getWhitePlayer().getMyMoves().size(), 6);
        assertEquals(board.getBlackPlayer().getMyMoves().size(), 6);
        assertFalse(board.getCurrentPlayer().isInCheck());
        assertFalse(board.getCurrentPlayer().isInCheckMate());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheck());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheckMate());
        assertEquals(board.getCurrentPlayer(), board.getWhitePlayer());
        assertEquals(board.getCurrentPlayer().getOpponent(), board.getBlackPlayer());

        final Move move = Move.MoveMaker.createMove(board, Utils.getCoordinatesFromAlgebraicNotation("e1"),
                Utils.getCoordinatesFromAlgebraicNotation("f1"));

        final BoardTransition boardTransition = board.getCurrentPlayer().makeMove(move);

        assertEquals(boardTransition.getNewBoard().getCurrentPlayer(), boardTransition.getNewBoard().getBlackPlayer());

        assertTrue(boardTransition.getMoveStatus().isDone());
        assertEquals(boardTransition.getNewBoard().getWhitePlayer().getPlayerKing().getPiecePosition(), 61);
        System.out.println(boardTransition.getNewBoard());
    }

    @Test
    public void testAlgebreicNotation() {
        assertEquals(Utils.getAlgebraicNotation(0), "a8");
        assertEquals(Utils.getAlgebraicNotation(1), "b8");
        assertEquals(Utils.getAlgebraicNotation(2), "c8");
        assertEquals(Utils.getAlgebraicNotation(3), "d8");
        assertEquals(Utils.getAlgebraicNotation(4), "e8");
        assertEquals(Utils.getAlgebraicNotation(5), "f8");
        assertEquals(Utils.getAlgebraicNotation(6), "g8");
        assertEquals(Utils.getAlgebraicNotation(7), "h8");
    }
}