package pjv.chess.tests;

import org.junit.Assert;
import org.junit.Test;
import pjv.chess.board.Board;
import pjv.chess.board.BoardTransition;
import pjv.chess.board.Move;
import pjv.chess.board.Utils;
import pjv.chess.pieces.*;

import java.util.Collection;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

public class ChessPieceTest {
    @Test
    public void testSimpleEnPassant() {
        final Board.Builder builder = new Board.Builder();
        // Black Layout
        builder.setPiece(new King(4, false, false));
        builder.setPiece(new Pawn(11, false));
        // White Layout
        builder.setPiece(new King(60, true, false));
        builder.setPiece(new Pawn(52, true));

        // Set the current player
        builder.setNextTurn(true);
        final Board board = builder.build();
        final Move m1 = Move.MoveMaker.createMove(board, Utils.getCoordinatesFromAlgebraicNotation(
                "e2"), Utils.getCoordinatesFromAlgebraicNotation("e4"));
        final BoardTransition t1 = board.getCurrentPlayer().makeMove(m1);
        Assert.assertTrue(t1.getMoveStatus().isDone());
        final Move m2 = Move.MoveMaker.createMove(t1.getNewBoard(), Utils.getCoordinatesFromAlgebraicNotation("e8"), Utils.getCoordinatesFromAlgebraicNotation("d8"));
        final BoardTransition t2 = t1.getNewBoard().getCurrentPlayer().makeMove(m2);
        Assert.assertTrue(t2.getMoveStatus().isDone());
        final Move m3 = Move.MoveMaker.createMove(t2.getNewBoard(), Utils.getCoordinatesFromAlgebraicNotation("e4"), Utils.getCoordinatesFromAlgebraicNotation("e5"));
        final BoardTransition t3 = t2.getNewBoard().getCurrentPlayer().makeMove(m3);
        Assert.assertTrue(t3.getMoveStatus().isDone());
        final Move m4 = Move.MoveMaker.createMove(t3.getNewBoard(), Utils.getCoordinatesFromAlgebraicNotation("d7"), Utils.getCoordinatesFromAlgebraicNotation("d5"));
        final BoardTransition t4 = t3.getNewBoard().getCurrentPlayer().makeMove(m4);
        Assert.assertTrue(t4.getMoveStatus().isDone());
        final Move m5 = Move.MoveMaker.createMove(t4.getNewBoard(), Utils.getCoordinatesFromAlgebraicNotation("e5"), Utils.getCoordinatesFromAlgebraicNotation("d6"));
        final BoardTransition t5 = t4.getNewBoard().getCurrentPlayer().makeMove(m5);
        Assert.assertTrue(t5.getMoveStatus().isDone());
    }

    @Test
    public void testPawnPromotion() {
        final Board.Builder builder = new Board.Builder();
        // Black Layout
        builder.setPiece(new Rook(3, false));
        builder.setPiece(new King(22, false, false));
        // White Layout
        builder.setPiece(new Pawn(15, true));
        builder.setPiece(new King(52, true, false));
        // Set the current player
        builder.setNextTurn(true);
        final Board board = builder.build();
        final Move m1 = Move.MoveMaker.createMove(board, Utils.getCoordinatesFromAlgebraicNotation(
                "h7"), Utils.getCoordinatesFromAlgebraicNotation("h8"));
        final BoardTransition t1 = board.getCurrentPlayer().makeMove(m1);
        Assert.assertTrue(t1.getMoveStatus().isDone());
        final Move m2 = Move.MoveMaker.createMove(t1.getNewBoard(), Utils.getCoordinatesFromAlgebraicNotation("d8"), Utils.getCoordinatesFromAlgebraicNotation("h8"));
        final BoardTransition t2 = t1.getNewBoard().getCurrentPlayer().makeMove(m2);
        Assert.assertTrue(t2.getMoveStatus().isDone());
    }

    @Test
    public void testKnightInCorners() {
        final Board.Builder boardBuilder = new Board.Builder();
        boardBuilder.setPiece(new King(4, false, false));
        boardBuilder.setPiece(new Knight(0, false));
        boardBuilder.setPiece(new Knight(56, true));
        boardBuilder.setPiece(new King(60, true, false));
        boardBuilder.setNextTurn(true);
        final Board board = boardBuilder.build();
        final Collection<Move> whiteLegals = board.getWhitePlayer().getMyMoves();
        final Collection<Move> blackLegals = board.getBlackPlayer().getMyMoves();
        assertEquals(whiteLegals.size(), 7);
        assertEquals(blackLegals.size(), 7);
        final Move wm1 = Move.MoveMaker
                .createMove(board, Utils.getCoordinatesFromAlgebraicNotation("a1"), Utils.getCoordinatesFromAlgebraicNotation("b3"));
        final Move wm2 = Move.MoveMaker
                .createMove(board, Utils.getCoordinatesFromAlgebraicNotation("a1"), Utils.getCoordinatesFromAlgebraicNotation("c2"));
        assertTrue(whiteLegals.contains(wm1));
        assertTrue(whiteLegals.contains(wm2));
        final Move bm1 = Move.MoveMaker
                .createMove(board, Utils.getCoordinatesFromAlgebraicNotation("a8"), Utils.getCoordinatesFromAlgebraicNotation("b6"));
        final Move bm2 = Move.MoveMaker
                .createMove(board, Utils.getCoordinatesFromAlgebraicNotation("a8"), Utils.getCoordinatesFromAlgebraicNotation("c7"));
        assertTrue(blackLegals.contains(bm1));
        assertTrue(blackLegals.contains(bm2));

    }

    @Test
    public void testQueenOnEmptyBoard() {
        final Board.Builder builder = new Board.Builder();
        // Black Layout
        builder.setPiece(new King(4, false, false));
        // White Layout
        builder.setPiece(new Queen(36, true));
        builder.setPiece(new King(60, true, false));
        // Set the current player
        builder.setNextTurn(true);
        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.getWhitePlayer().getMyMoves();
        final Collection<Move> blackLegals = board.getBlackPlayer().getMyMoves();
        assertEquals(whiteLegals.size(), 31);
        assertEquals(blackLegals.size(), 5);
        assertTrue(whiteLegals.contains(Move.MoveMaker
                .createMove(board, Utils.getCoordinatesFromAlgebraicNotation("e4"), Utils.getCoordinatesFromAlgebraicNotation("e8"))));
        assertTrue(whiteLegals.contains(Move.MoveMaker
                .createMove(board, Utils.getCoordinatesFromAlgebraicNotation("e4"), Utils.getCoordinatesFromAlgebraicNotation("e7"))));
        assertTrue(whiteLegals.contains(Move.MoveMaker
                .createMove(board, Utils.getCoordinatesFromAlgebraicNotation("e4"), Utils.getCoordinatesFromAlgebraicNotation("e6"))));
        assertTrue(whiteLegals.contains(Move.MoveMaker
                .createMove(board, Utils.getCoordinatesFromAlgebraicNotation("e4"), Utils.getCoordinatesFromAlgebraicNotation("e5"))));
        assertTrue(whiteLegals.contains(Move.MoveMaker
                .createMove(board, Utils.getCoordinatesFromAlgebraicNotation("e4"), Utils.getCoordinatesFromAlgebraicNotation("e3"))));
        assertTrue(whiteLegals.contains(Move.MoveMaker
                .createMove(board, Utils.getCoordinatesFromAlgebraicNotation("e4"), Utils.getCoordinatesFromAlgebraicNotation("e2"))));
        assertTrue(whiteLegals.contains(Move.MoveMaker
                .createMove(board, Utils.getCoordinatesFromAlgebraicNotation("e4"), Utils.getCoordinatesFromAlgebraicNotation("a4"))));
        assertTrue(whiteLegals.contains(Move.MoveMaker
                .createMove(board, Utils.getCoordinatesFromAlgebraicNotation("e4"), Utils.getCoordinatesFromAlgebraicNotation("b4"))));
        assertTrue(whiteLegals.contains(Move.MoveMaker
                .createMove(board, Utils.getCoordinatesFromAlgebraicNotation("e4"), Utils.getCoordinatesFromAlgebraicNotation("c4"))));
        assertTrue(whiteLegals.contains(Move.MoveMaker
                .createMove(board, Utils.getCoordinatesFromAlgebraicNotation("e4"), Utils.getCoordinatesFromAlgebraicNotation("d4"))));
        assertTrue(whiteLegals.contains(Move.MoveMaker
                .createMove(board, Utils.getCoordinatesFromAlgebraicNotation("e4"), Utils.getCoordinatesFromAlgebraicNotation("f4"))));
        assertTrue(whiteLegals.contains(Move.MoveMaker
                .createMove(board, Utils.getCoordinatesFromAlgebraicNotation("e4"), Utils.getCoordinatesFromAlgebraicNotation("g4"))));
        assertTrue(whiteLegals.contains(Move.MoveMaker
                .createMove(board, Utils.getCoordinatesFromAlgebraicNotation("e4"), Utils.getCoordinatesFromAlgebraicNotation("h4"))));
    }


}