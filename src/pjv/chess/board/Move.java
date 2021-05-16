package pjv.chess.board;

import pjv.chess.pieces.ChessPiece;
import pjv.chess.pieces.Pawn;
import pjv.chess.pieces.Rook;

public abstract class Move {

    Board board;
    ChessPiece movedPiece;
    int pieceDestination;

    public static Move NULL_MOVE = new NullMove();

    Move(Board board, ChessPiece movedPiece, int pieceDestination){
        this.board = board;
        this.movedPiece = movedPiece;
        this.pieceDestination = pieceDestination;
    }

    public Board moveExecution(){
        Board.Builder builder = new Board.Builder();

        for(ChessPiece piece : this.board.getCurrentPlayer().getMyPieces()){
            if(!this.movedPiece.equals(piece)){
                builder.setPiece(piece);
            }
        }

        for(ChessPiece piece : this.board.getCurrentPlayer().getOpponent().getMyPieces()){
            builder.setPiece(piece);
        }
        builder.setPiece(this.movedPiece.movePiece(this));
        this.movedPiece.switchFirstMove();
        flipChessClock();
        builder.setNextTurn(this.board.getCurrentPlayer().getOpponent().getAlliance());
        return builder.build();
    }

    public static class DefaultMove extends Move{

        public DefaultMove(Board board, ChessPiece movedPiece, int pieceDestination) {
            super(board, movedPiece, pieceDestination);
        }

    }

    public static class NullMove extends Move{

        NullMove() {
            super(null, null,-1);
        }

        @Override
        public Board moveExecution(){
            throw new RuntimeException("Null move is not executable");
        }
    }

    public static class AttackMove extends Move{

        ChessPiece attackedPiece;

        public AttackMove(Board board, ChessPiece movedPiece, int pieceDestination, ChessPiece attackedPiece) {
            super(board, movedPiece, pieceDestination);
            this.attackedPiece = attackedPiece;
        }
        @Override
        public Board moveExecution(){
            Board.Builder builder = new Board.Builder();

            for(ChessPiece piece : this.board.getCurrentPlayer().getMyPieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }

            for(ChessPiece piece : this.board.getCurrentPlayer().getOpponent().getMyPieces()){
                if(!piece.equals(this.attackedPiece)){
                    builder.setPiece(piece);
                }
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            this.movedPiece.switchFirstMove();
            flipChessClock();
            builder.setNextTurn(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }

    }

    public static class PawnMove extends Move{

        PawnMove(Board board, ChessPiece movedPiece, int pieceDestination) {
            super(board, movedPiece, pieceDestination);
        }
    }

    public static class PawnPromotion extends PawnMove{

        Move pawnMove;
        Pawn promotedPawn;

        public PawnPromotion(Move pawnMove) {
            super(pawnMove.getBoard() , pawnMove.getMovedPiece(), pawnMove.getDestinationCoordinate());
            this.pawnMove = pawnMove;
            this.promotedPawn = (Pawn) pawnMove.getMovedPiece();
        }

        @Override
        public Board moveExecution(){

            Board newBoard = this.pawnMove.moveExecution();
            Board.Builder builder = new Board.Builder();
            for(ChessPiece piece : newBoard.getCurrentPlayer().getMyPieces()){
                if(!this.promotedPawn.equals((piece))){
                    builder.setPiece(piece);
                }
            }
            for(final ChessPiece piece : newBoard.getCurrentPlayer().getOpponentsPieces()){
                builder.setPiece(piece);
            }
            builder.setPiece(this.promotedPawn.getPromotedPiece().movePiece(this));
            flipChessClock();
            builder.setNextTurn(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    public static class PawnJump extends PawnMove{

        public PawnJump(Board board, ChessPiece movedPiece, int pieceDestination) {
            super(board, movedPiece, pieceDestination);
        }
        @Override
        public Board moveExecution(){
            Board.Builder builder = new Board.Builder();

            for(ChessPiece piece : this.board.getCurrentPlayer().getMyPieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }

            for(ChessPiece piece : this.board.getCurrentPlayer().getOpponent().getMyPieces()){
                builder.setPiece(piece);
            }
            Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            this.movedPiece.switchFirstMove();
            flipChessClock();
            builder.setNextTurn(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    public static class PawnEnPassant extends AttackMove{

        public PawnEnPassant(Board board, ChessPiece movedPiece, int pieceDestination, ChessPiece attackedPiece) {
            super(board, movedPiece, pieceDestination, attackedPiece);
        }
    }

   static abstract class CastleMove extends Move {
        Rook castleRook;
        int castleRookPosition;
        int casteRookDestination;

        public CastleMove(Board board, ChessPiece movedPiece, int pieceDestination,
                          Rook castleRook, int castleRookPosition, int casteRookDestination) {
            super(board, movedPiece, pieceDestination);
            this.castleRook = castleRook;
            this.castleRookPosition = castleRookPosition;
            this.casteRookDestination = casteRookDestination;
        }

        public Rook getCastleRook(){
            return this.castleRook;
        }
        @Override
        public boolean isCastlingMove(){
            return true;
        }

        @Override
        public Board moveExecution(){
            Board.Builder builder = new Board.Builder();
            for(ChessPiece piece : this.board.getCurrentPlayer().getMyPieces()){
                if(!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(ChessPiece piece : this.board.getCurrentPlayer().getOpponent().getMyPieces()){
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRook.getPiecePosition(), this.castleRook.getPieceAlliance()));
            this.movedPiece.switchFirstMove();
            flipChessClock();
            builder.setNextTurn(!builder.thisTurn);
            return builder.build();


        }
    }

    public static class ShortCastleMove extends CastleMove {

        public ShortCastleMove(Board board, ChessPiece movedPiece, int pieceDestination,
                               Rook castleRook, int castleRookPosition, int casteRookDestination) {
            super(board, movedPiece, pieceDestination, castleRook, castleRookPosition, casteRookDestination);
        }
    }

    public static class LongCastleMove extends CastleMove {

        public LongCastleMove(Board board, ChessPiece movedPiece, int pieceDestination,
                              Rook castleRook, int castleRookPosition, int casteRookDestination) {
            super(board, movedPiece, pieceDestination,  castleRook, castleRookPosition, casteRookDestination);
        }
    }


    public int getCurrentCoordinate(){
        return this.getMovedPiece().getPiecePosition();
    }

    public int getDestinationCoordinate(){
        return this.pieceDestination;
    }

    public ChessPiece getMovedPiece(){ return this.movedPiece; }

    public Board getBoard(){ return this.board; }

    public boolean isAttack(){ return false; }

    public boolean isCastlingMove(){ return false; }

    public void flipChessClock(){
        System.out.println("This happened");
        board.getCurrentPlayer().getChessClock().pause();
        board.getCurrentPlayer().getOpponent().getChessClock().resume();
    }


    public enum MoveStatus {

        DONE {
            @Override
            public boolean isDone() {
                return true;
            }

            @Override
            public boolean isIllegal() {
                return false;
            }
        },
        ILLEGAL_MOVE {
            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public boolean isIllegal() {
                return true;
            }
        },
        DOES_SELF_CHECK {
            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public boolean isIllegal() {
                return false;
            }
        };

        public abstract boolean isDone();

        public abstract boolean isIllegal();
    }

    public static class moveMaker{
        public static Move createMove(Board board, int pieceCoordinate, int destinationCoordinate) {
            for (Move move : board.getAllLegalMoves()){
                if (move.getCurrentCoordinate()== pieceCoordinate &&
                        move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}
