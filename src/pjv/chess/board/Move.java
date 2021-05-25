package pjv.chess.board;

import jdk.jshell.execution.Util;
import pjv.chess.pieces.ChessPiece;
import pjv.chess.pieces.Pawn;
import pjv.chess.pieces.Queen;
import pjv.chess.pieces.Rook;

import javax.swing.*;

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

    public abstract Board moveExecution();

    public abstract boolean isDefaultMove();

    public abstract boolean isAttackMove();

    public abstract boolean isPawnMove();

    public abstract boolean isPawnJump();

    public abstract boolean isPawnAttackMove();

    public abstract boolean isPawnPromotion();

    public abstract boolean isEnPassant();

    public abstract boolean isShortCastle();

    public abstract boolean isLongCastle();


    public static class DefaultMove extends Move{

        public DefaultMove(Board board, ChessPiece movedPiece, int pieceDestination) {
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
            this.movedPiece.switchFirstMove();
            builder.setPiece(this.movedPiece.movePiece(this));
            endTurn(builder);
            return builder.build();
        }

        @Override
        public boolean isDefaultMove() {
            return true;
        }

        @Override
        public boolean isAttackMove() {
            return false;
        }

        @Override
        public boolean isPawnMove() {
            return false;
        }

        @Override
        public boolean isPawnJump() {
            return false;
        }

        @Override
        public boolean isPawnAttackMove() {
            return false;
        }

        @Override
        public boolean isPawnPromotion() {
            return false;
        }

        @Override
        public boolean isEnPassant() {
            return false;
        }

        @Override
        public boolean isShortCastle() {
            return false;
        }

        @Override
        public boolean isLongCastle() {
            return false;
        }

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(this.movedPiece.toString());
            stringBuilder.append(checkDisambiguating());
            stringBuilder.append(Utils.getAlgebraicNotation(getDestinationCoordinate()));

            if (this.board.getCurrentPlayer().getOpponent().isInCheck()) {
                if (this.board.getCurrentPlayer().getOpponent().isInCheckMate()) {
                    stringBuilder.append("#");
                } else {
                    stringBuilder.append("+");
                }
            } else {
                stringBuilder.append("");
            }
            return stringBuilder.toString();
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

        @Override
        public boolean isDefaultMove() {
            return false;
        }

        @Override
        public boolean isAttackMove() {
            return false;
        }

        @Override
        public boolean isPawnMove() {
            return false;
        }

        @Override
        public boolean isPawnJump() {
            return false;
        }

        @Override
        public boolean isPawnAttackMove() {
            return false;
        }

        @Override
        public boolean isPawnPromotion() {
            return false;
        }

        @Override
        public boolean isEnPassant() {
            return false;
        }

        @Override
        public boolean isShortCastle() {
            return false;
        }

        @Override
        public boolean isLongCastle() {
            return false;
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
            this.movedPiece.switchFirstMove();
            builder.setPiece(this.movedPiece.movePiece(this));
            endTurn(builder);
            return builder.build();
        }

        @Override
        public boolean isDefaultMove() {
            return false;
        }

        @Override
        public boolean isAttackMove() {
            return true;
        }

        @Override
        public boolean isPawnMove() {
            return false;
        }

        @Override
        public boolean isPawnJump() {
            return false;
        }

        @Override
        public boolean isPawnAttackMove() {
            return false;
        }

        @Override
        public boolean isPawnPromotion() {
            return false;
        }

        @Override
        public boolean isEnPassant() {
            return false;
        }

        @Override
        public boolean isShortCastle() {
            return false;
        }

        @Override
        public boolean isLongCastle() {
            return false;
        }

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(this.movedPiece.toString());
            stringBuilder.append(checkDisambiguating());
            stringBuilder.append("x");
            stringBuilder.append(Utils.getAlgebraicNotation(getDestinationCoordinate()));

            if (this.board.getCurrentPlayer().getOpponent().isInCheck()) {
                if (this.board.getCurrentPlayer().getOpponent().isInCheckMate()) {
                    stringBuilder.append("#");
                } else {
                    stringBuilder.append("+");
                }
            } else {
                stringBuilder.append("");
            }
            return stringBuilder.toString();
        }

    }

    public static class PawnMove extends DefaultMove{

        public PawnMove(Board board, ChessPiece movedPiece, int pieceDestination) {
            super(board, movedPiece, pieceDestination);
        }

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(Utils.getAlgebraicNotation(getDestinationCoordinate()));


            if (this.board.getCurrentPlayer().getOpponent().isInCheck()) {
                if (this.board.getCurrentPlayer().getOpponent().isInCheckMate()) {
                    stringBuilder.append("#");
                } else {
                    stringBuilder.append("+");
                }
            } else {
                stringBuilder.append("");
            }
            return stringBuilder.toString();
        }
        @Override
        public boolean isDefaultMove() {
            return false;
        }

        @Override
        public boolean isAttackMove() {
            return false;
        }

        @Override
        public boolean isPawnMove() {
            return true;
        }
        @Override
        public boolean isPawnJump() {
            return false;
        }

        @Override
        public boolean isPawnAttackMove() {
            return false;
        }

        @Override
        public boolean isPawnPromotion() {
            return false;
        }

        @Override
        public boolean isEnPassant() {
            return false;
        }

        @Override
        public boolean isShortCastle() {
            return false;
        }

        @Override
        public boolean isLongCastle() {
            return false;
        }
    }

    public static class PawnPromotion extends PawnMove{

        Move pawnMove;
        Pawn promotedPawn;
        ChessPiece promotedPiece;
        char promotedPieceSign;

        public PawnPromotion(Move pawnMove) {
            super(pawnMove.getBoard() , pawnMove.getMovedPiece(), pawnMove.getDestinationCoordinate());
            this.pawnMove = pawnMove;
            this.promotedPawn = (Pawn) pawnMove.getMovedPiece();
            this.promotedPieceSign = 'P';
        }
        public PawnPromotion(Move pawnMove, char promotedPieceSign){
            super(pawnMove.getBoard() , pawnMove.getMovedPiece(), pawnMove.getDestinationCoordinate());
            this.promotedPieceSign = promotedPieceSign;
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
            promotedPiece = this.promotedPawn.getPromotedPiece(promotedPieceSign);
            builder.setPiece(promotedPiece.movePiece(this));
            endTurn(builder);
            return builder.build();
        }

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(Utils.getAlgebraicNotation(getDestinationCoordinate()));
            stringBuilder.append("=");
            stringBuilder.append(promotedPiece.toString());

            if (this.board.getCurrentPlayer().getOpponent().isInCheck()) {
                if(this.board.getCurrentPlayer().getOpponent().isInCheckMate()){
                    stringBuilder.append("#");
                } else {
                    stringBuilder.append("+");
                }
            } else {
                stringBuilder.append("");
            }
            return stringBuilder.toString();
        }
        @Override
        public boolean isDefaultMove() {
            return false;
        }

        @Override
        public boolean isAttackMove() {
            return false;
        }

        @Override
        public boolean isPawnMove() {
            return false;
        }

        @Override
        public boolean isPawnJump() {
            return false;
        }

        @Override
        public boolean isPawnAttackMove() {
            return false;
        }

        @Override
        public boolean isPawnPromotion() {
            return true;
        }

        @Override
        public boolean isEnPassant() {
            return false;
        }

        @Override
        public boolean isShortCastle() {
            return false;
        }

        @Override
        public boolean isLongCastle() {
            return false;
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
            endTurn(builder);
            return builder.build();
        }
        @Override
        public boolean isDefaultMove() {
            return false;
        }

        @Override
        public boolean isAttackMove() {
            return false;
        }

        @Override
        public boolean isPawnMove() {
            return true;
        }

        @Override
        public boolean isPawnJump() {
            return true;
        }

        @Override
        public boolean isPawnAttackMove() {
            return false;
        }

        @Override
        public boolean isPawnPromotion() {
            return false;
        }

        @Override
        public boolean isEnPassant() {
            return false;
        }

        @Override
        public boolean isShortCastle() {
            return false;
        }

        @Override
        public boolean isLongCastle() {
            return false;
        }
    }

    public static class PawnAttackMove extends AttackMove{

        public PawnAttackMove(Board board, ChessPiece movedPiece, int pieceDestination, ChessPiece attackedPiece) {
            super(board, movedPiece, pieceDestination, attackedPiece);
        }

        public String toString(){
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(Utils.getAlgebraicColumn(this.movedPiece.getPiecePosition()));
            stringBuilder.append("x");
            stringBuilder.append(Utils.getAlgebraicNotation(getDestinationCoordinate()));

            if (this.board.getCurrentPlayer().getOpponent().isInCheck()) {
                if (this.board.getCurrentPlayer().getOpponent().isInCheckMate()) {
                    stringBuilder.append("#");
                } else {
                    stringBuilder.append("+");
                }
            } else {
                stringBuilder.append("");
            }
            return stringBuilder.toString();
        }
        @Override
        public boolean isDefaultMove() {
            return false;
        }

        @Override
        public boolean isAttackMove() {
            return false;
        }

        @Override
        public boolean isPawnMove() {
            return false;
        }

        @Override
        public boolean isPawnJump() {
            return false;
        }

        @Override
        public boolean isPawnAttackMove() {
            return true;
        }

        @Override
        public boolean isPawnPromotion() {
            return false;
        }

        @Override
        public boolean isEnPassant() {
            return false;
        }

        @Override
        public boolean isShortCastle() {
            return false;
        }

        @Override
        public boolean isLongCastle() {
            return false;
        }
    }

    public static class PawnEnPassant extends PawnAttackMove{

        public PawnEnPassant(Board board, ChessPiece movedPiece, int pieceDestination, ChessPiece attackedPiece) {
            super(board, movedPiece, pieceDestination, attackedPiece);
        }
        @Override
        public boolean isDefaultMove() {
            return false;
        }

        @Override
        public boolean isAttackMove() {
            return false;
        }

        @Override
        public boolean isPawnMove() {
            return false;
        }

        @Override
        public boolean isPawnJump() {
            return false;
        }

        @Override
        public boolean isPawnAttackMove() {
            return false;
        }

        @Override
        public boolean isPawnPromotion() {
            return false;
        }

        @Override
        public boolean isEnPassant() {
            return true;
        }

        @Override
        public boolean isShortCastle() {
            return false;
        }

        @Override
        public boolean isLongCastle() {
            return false;
        }
    }

   public static class CastleMove extends Move {
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
            this.movedPiece.switchFirstMove();
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.casteRookDestination, this.castleRook.getPieceAlliance()));
            endTurn(builder);
            return builder.build();
        }

       @Override
       public boolean isDefaultMove() {
           return false;
       }

       @Override
       public boolean isAttackMove() {
           return false;
       }

       @Override
       public boolean isPawnMove() {
           return false;
       }

       @Override
       public boolean isPawnJump() {
           return false;
       }

       @Override
       public boolean isPawnAttackMove() {
           return false;
       }

       @Override
       public boolean isPawnPromotion() {
           return false;
       }

       @Override
       public boolean isEnPassant() {
           return false;
       }

       @Override
       public boolean isShortCastle() {
           return false;
       }

       @Override
       public boolean isLongCastle() {
           return false;
       }
   }

    public static class ShortCastleMove extends CastleMove {
        Rook castleRook;
        int castleRookPosition;
        int casteRookDestination;

        public ShortCastleMove(Board board, ChessPiece movedPiece, int pieceDestination,
                               Rook castleRook, int castleRookPosition, int casteRookDestination) {
            super(board, movedPiece, pieceDestination, castleRook, castleRookPosition, casteRookDestination);
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
            this.movedPiece.switchFirstMove();
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.casteRookDestination, this.castleRook.getPieceAlliance()));
            endTurn(builder);
            return builder.build();
        }
        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("O-O");

            if (this.board.getCurrentPlayer().getOpponent().isInCheck()) {
                if (this.board.getCurrentPlayer().getOpponent().isInCheckMate()) {
                    stringBuilder.append("#");
                } else {
                    stringBuilder.append("+");
                }
            } else {
                stringBuilder.append("");
            }
            return stringBuilder.toString();
        }
        @Override
        public boolean isDefaultMove() {
            return false;
        }

        @Override
        public boolean isAttackMove() {
            return false;
        }

        @Override
        public boolean isPawnMove() {
            return false;
        }

        @Override
        public boolean isPawnJump() {
            return false;
        }

        @Override
        public boolean isPawnAttackMove() {
            return false;
        }

        @Override
        public boolean isPawnPromotion() {
            return false;
        }

        @Override
        public boolean isEnPassant() {
            return false;
        }

        @Override
        public boolean isShortCastle() {
            return true;
        }

        @Override
        public boolean isLongCastle() {
            return false;
        }
    }

    public static class LongCastleMove extends CastleMove {
        Rook castleRook;
        int castleRookPosition;
        int casteRookDestination;

        public LongCastleMove(Board board, ChessPiece movedPiece, int pieceDestination,
                              Rook castleRook, int castleRookPosition, int casteRookDestination) {
            super(board, movedPiece, pieceDestination,  castleRook, castleRookPosition, casteRookDestination);
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
            this.movedPiece.switchFirstMove();
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.casteRookDestination, this.castleRook.getPieceAlliance()));
            endTurn(builder);
            return builder.build();
        }
        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("O-O-O");

            if (this.board.getCurrentPlayer().getOpponent().isInCheck()) {
                if(this.board.getCurrentPlayer().getOpponent().isInCheckMate()){
                    stringBuilder.append("#");
                } else {
                    stringBuilder.append("+");
                }
            } else {
                stringBuilder.append("");
            }
            return stringBuilder.toString();
        }
        @Override
        public boolean isDefaultMove() {
            return false;
        }

        @Override
        public boolean isAttackMove() {
            return false;
        }

        @Override
        public boolean isPawnMove() {
            return false;
        }

        @Override
        public boolean isPawnJump() {
            return false;
        }

        @Override
        public boolean isPawnAttackMove() {
            return false;
        }

        @Override
        public boolean isPawnPromotion() {
            return false;
        }

        @Override
        public boolean isEnPassant() {
            return false;
        }

        @Override
        public boolean isShortCastle() {
            return false;
        }

        @Override
        public boolean isLongCastle() {
            return true;
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

    public String checkDisambiguating(){
        StringBuilder stringBuilder = new StringBuilder();
        boolean rowAppended = false;
        boolean columnAppended = false;

        for(Move move : this.board.getCurrentPlayer().getMyMoves()){
            ChessPiece other = move.getMovedPiece();
            if ((getDestinationCoordinate() == move.getDestinationCoordinate()) &&
                    (this.movedPiece.toString() == other.toString()) &&
                    (this.movedPiece.getPiecePosition() != other.getPiecePosition())) {
                if(Utils.getRowNumber(movedPiece.getPiecePosition()) == Utils.getRowNumber(other.getPiecePosition()) && !columnAppended){
                    stringBuilder.append(Utils.getAlgebraicColumn(this.movedPiece.getPiecePosition()));
                    columnAppended = true;
                }
                if(Utils.getColumnNumber(movedPiece.getPiecePosition()) == Utils.getColumnNumber(other.getPiecePosition()) && !rowAppended){
                    stringBuilder.append(Utils.getAlgebraicRow(this.movedPiece.getPiecePosition()));
                    rowAppended = true;
                }
            }
        }
        return stringBuilder.toString();
    }

    public void endTurn(Board.Builder builder){

        if(board.getCurrentPlayer().getAlliance()){
            builder.keepWhiteTime(board.getWhitePlayer().getChessClock().getTimeLeft() + Utils.DEFAULT_INCREMENT);
            builder.keepBlackTime(board.getBlackPlayer().getChessClock().getTimeLeft() );
        } else {
            builder.keepWhiteTime(board.getWhitePlayer().getChessClock().getTimeLeft() );
            builder.keepBlackTime(board.getBlackPlayer().getChessClock().getTimeLeft() + Utils.DEFAULT_INCREMENT);
        }
        builder.keepWhitePlayerPanel(board.getWhitePlayer().getPlayerPanel());
        builder.keepBlackPanel(board.getBlackPlayer().getPlayerPanel());
        builder.setNextTurn(this.board.getCurrentPlayer().getOpponent().getAlliance());
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

            @Override
            public boolean isCheck() {
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

            @Override
            public boolean isCheck() {
                return false;
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

            @Override
            public boolean isCheck() {
                return true;
            }
        };

        public abstract boolean isDone();

        public abstract boolean isIllegal();

        public abstract boolean isCheck();
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
