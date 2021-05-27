package pjv.chess.tests;

import org.junit.Assert;
import org.junit.Test;
import pjv.chess.board.*;
import pjv.chess.pieces.Bishop;
import pjv.chess.pieces.King;
import pjv.chess.pieces.Rook;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    @Test
    public void testDiscoveredCheck() {
        final Board.Builder builder = new Board.Builder();
        // Black Layout
        builder.setPiece(new King(4, false, false));
        builder.setPiece(new Rook(24, false));
        // White Layout
        builder.setPiece(new Bishop(44, true));
        builder.setPiece(new Rook(52, true));
        builder.setPiece(new King(58, true, false));
        // Set the getCurrent player
        builder.setNextTurn(true);
        final Board board = builder.build();
        final BoardTransition t1 = board.getCurrentPlayer()
                .makeMove(Move.MoveMaker.createMove(board, Utils.getCoordinatesFromAlgebraicNotation("e3"),
                        Utils.getCoordinatesFromAlgebraicNotation("b6")));
        assertTrue(t1.getMoveStatus().isDone());
        assertTrue(t1.getNewBoard().getCurrentPlayer().isInCheck());
        final BoardTransition t2 = t1.getNewBoard()
                .getCurrentPlayer()
                .makeMove(Move.MoveMaker.createMove(t1.getNewBoard(), Utils.getCoordinatesFromAlgebraicNotation("a5"),
                        Utils.getCoordinatesFromAlgebraicNotation("b5")));
        assertFalse(t2.getMoveStatus().isDone());
        final BoardTransition t3 = t1.getNewBoard()
                .getCurrentPlayer()
                .makeMove(Move.MoveMaker.createMove(t1.getNewBoard(), Utils.getCoordinatesFromAlgebraicNotation("a5"),
                        Utils.getCoordinatesFromAlgebraicNotation("e5")));
        assertTrue(t3.getMoveStatus().isDone());
    }

    @Test
    public void testIllegalMove() {
        final Board board = Board.createStandardBoard();
        final Move m1 = Move.MoveMaker.createMove(board, Utils.getCoordinatesFromAlgebraicNotation("e2"),
                Utils.getCoordinatesFromAlgebraicNotation("e6"));
        final BoardTransition t1 = board.getCurrentPlayer()
                .makeMove(m1);
        assertFalse(t1.getMoveStatus().isDone());
    }
    @Test
    public void testWhiteKingSideCastle() {
        final Board board = Board.createStandardBoard();
        final BoardTransition t1 = board.getCurrentPlayer()
                .makeMove(Move.MoveMaker.createMove(board, Utils.getCoordinatesFromAlgebraicNotation("e2"),
                        Utils.getCoordinatesFromAlgebraicNotation("e4")));
        assertTrue(t1.getMoveStatus().isDone());
        final BoardTransition t2 = t1.getNewBoard()
                .getCurrentPlayer()
                .makeMove(Move.MoveMaker.createMove(t1.getNewBoard(), Utils.getCoordinatesFromAlgebraicNotation("e7"),
                        Utils.getCoordinatesFromAlgebraicNotation("e5")));
        assertTrue(t2.getMoveStatus().isDone());
        final BoardTransition t3 = t2.getNewBoard()
                .getCurrentPlayer()
                .makeMove(Move.MoveMaker.createMove(t2.getNewBoard(), Utils.getCoordinatesFromAlgebraicNotation("g1"),
                        Utils.getCoordinatesFromAlgebraicNotation("f3")));
        assertTrue(t3.getMoveStatus().isDone());
        final BoardTransition t4 = t3.getNewBoard()
                .getCurrentPlayer()
                .makeMove(Move.MoveMaker.createMove(t3.getNewBoard(), Utils.getCoordinatesFromAlgebraicNotation("d7"),
                        Utils.getCoordinatesFromAlgebraicNotation("d6")));
        assertTrue(t4.getMoveStatus().isDone());
        final BoardTransition t5 = t4.getNewBoard()
                .getCurrentPlayer()
                .makeMove(Move.MoveMaker.createMove(t4.getNewBoard(), Utils.getCoordinatesFromAlgebraicNotation("f1"),
                        Utils.getCoordinatesFromAlgebraicNotation("e2")));
        assertTrue(t5.getMoveStatus().isDone());
        final BoardTransition t6 = t5.getNewBoard()
                .getCurrentPlayer()
                .makeMove(Move.MoveMaker.createMove(t5.getNewBoard(), Utils.getCoordinatesFromAlgebraicNotation("d6"),
                        Utils.getCoordinatesFromAlgebraicNotation("d5")));
        assertTrue(t6.getMoveStatus().isDone());
        final Move wm1 = Move.MoveMaker
                .createMove(t6.getNewBoard(), Utils.getCoordinatesFromAlgebraicNotation(
                        "e1"), Utils.getCoordinatesFromAlgebraicNotation("g1"));
        assertTrue(t6.getNewBoard().getCurrentPlayer().getMyMoves().contains(wm1));
        final BoardTransition t7 = t6.getNewBoard().getCurrentPlayer().makeMove(wm1);
        assertTrue(t7.getMoveStatus().isDone());
        Assert.assertFalse(t7.getNewBoard().getWhitePlayer().isKingSideCastleCapable());
        Assert.assertFalse(t7.getNewBoard().getWhitePlayer().isQueenSideCastleCapable());
    }
    @Test
    public void testBlackQueenSideCastle() {
        final Board board = Board.createStandardBoard();
        final BoardTransition t1 = board.getCurrentPlayer()
                .makeMove(Move.MoveMaker.createMove(board, Utils.getCoordinatesFromAlgebraicNotation("e2"),
                        Utils.getCoordinatesFromAlgebraicNotation("e4")));
        assertTrue(t1.getMoveStatus().isDone());
        final BoardTransition t2 = t1.getNewBoard()
                .getCurrentPlayer()
                .makeMove(Move.MoveMaker.createMove(t1.getNewBoard(), Utils.getCoordinatesFromAlgebraicNotation("e7"),
                        Utils.getCoordinatesFromAlgebraicNotation("e5")));
        assertTrue(t2.getMoveStatus().isDone());
        final BoardTransition t3 = t2.getNewBoard()
                .getCurrentPlayer()
                .makeMove(Move.MoveMaker.createMove(t2.getNewBoard(), Utils.getCoordinatesFromAlgebraicNotation("d2"),
                        Utils.getCoordinatesFromAlgebraicNotation("d3")));
        assertTrue(t3.getMoveStatus().isDone());
        final BoardTransition t4 = t3.getNewBoard()
                .getCurrentPlayer()
                .makeMove(Move.MoveMaker.createMove(t3.getNewBoard(), Utils.getCoordinatesFromAlgebraicNotation("d8"),
                        Utils.getCoordinatesFromAlgebraicNotation("e7")));
        assertTrue(t4.getMoveStatus().isDone());
        final BoardTransition t5 = t4.getNewBoard()
                .getCurrentPlayer()
                .makeMove(Move.MoveMaker.createMove(t4.getNewBoard(), Utils.getCoordinatesFromAlgebraicNotation("b1"),
                        Utils.getCoordinatesFromAlgebraicNotation("c3")));
        assertTrue(t5.getMoveStatus().isDone());
        final BoardTransition t6 = t5.getNewBoard()
                .getCurrentPlayer()
                .makeMove(Move.MoveMaker.createMove(t5.getNewBoard(), Utils.getCoordinatesFromAlgebraicNotation("b8"),
                        Utils.getCoordinatesFromAlgebraicNotation("c6")));
        assertTrue(t6.getMoveStatus().isDone());
        final BoardTransition t7 = t6.getNewBoard()
                .getCurrentPlayer()
                .makeMove(Move.MoveMaker.createMove(t6.getNewBoard(), Utils.getCoordinatesFromAlgebraicNotation("c1"),
                        Utils.getCoordinatesFromAlgebraicNotation("d2")));
        assertTrue(t7.getMoveStatus().isDone());
        final BoardTransition t8 = t7.getNewBoard()
                .getCurrentPlayer()
                .makeMove(Move.MoveMaker.createMove(t7.getNewBoard(), Utils.getCoordinatesFromAlgebraicNotation("d7"),
                        Utils.getCoordinatesFromAlgebraicNotation("d6")));
        assertTrue(t8.getMoveStatus().isDone());
        final BoardTransition t9 = t8.getNewBoard()
                .getCurrentPlayer()
                .makeMove(Move.MoveMaker.createMove(t8.getNewBoard(), Utils.getCoordinatesFromAlgebraicNotation("f1"),
                        Utils.getCoordinatesFromAlgebraicNotation("e2")));
        assertTrue(t9.getMoveStatus().isDone());
        final BoardTransition t10 = t9.getNewBoard()
                .getCurrentPlayer()
                .makeMove(Move.MoveMaker.createMove(t9.getNewBoard(), Utils.getCoordinatesFromAlgebraicNotation("c8"),
                        Utils.getCoordinatesFromAlgebraicNotation("d7")));
        assertTrue(t10.getMoveStatus().isDone());
        final BoardTransition t11 = t10.getNewBoard()
                .getCurrentPlayer()
                .makeMove(
                        Move.MoveMaker.createMove(t10.getNewBoard(), Utils.getCoordinatesFromAlgebraicNotation("g1"),
                                Utils.getCoordinatesFromAlgebraicNotation("f3")));
        assertTrue(t11.getMoveStatus().isDone());
        final Move wm1 = Move.MoveMaker
                .createMove(t11.getNewBoard(), Utils.getCoordinatesFromAlgebraicNotation(
                        "e8"), Utils.getCoordinatesFromAlgebraicNotation("c8"));
        assertTrue(t11.getNewBoard().getCurrentPlayer().getMyMoves().contains(wm1));
        final BoardTransition t12 = t11.getNewBoard().getCurrentPlayer().makeMove(wm1);
        assertTrue(t12.getMoveStatus().isDone());
        Assert.assertFalse(t12.getNewBoard().getBlackPlayer().isKingSideCastleCapable());
        Assert.assertFalse(t12.getNewBoard().getBlackPlayer().isQueenSideCastleCapable());
    }

    @Test
    public void testNoCastlingOuNewfCheck() {
        final Board board = FENUtils.createGameFromFEN("r3k2r/1pN1nppp/p3p3/3p4/8/8/PPPK1PPP/R6R b kq - 1 18");
        final Move illegalCastleMove = Move.MoveMaker
                .createMove(board, Utils.getCoordinatesFromAlgebraicNotation("e8"), Utils.getCoordinatesFromAlgebraicNotation("c8"));
        final BoardTransition t1 = board.getCurrentPlayer()
                .makeMove(illegalCastleMove);
        Assert.assertFalse(t1.getMoveStatus().isDone());
    }
    
}