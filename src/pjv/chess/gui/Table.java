package pjv.chess.gui;

import pjv.chess.board.*;
import pjv.chess.pieces.ChessPiece;
import pjv.chess.players.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table {

    private JFrame gameFrame;
    public BoardTablePanel boardTablePanel;
    private PlayerPanel blackPlayerPanel;
    private PlayerPanel whitePlayerPanel;
    public Board chessBoard;

    private Tile sourceTile;
    private Tile destinationTile;
    private ChessPiece movedPiece;

    //Both height and width of Board table panel dimension should be divisible by ROW_COUNT and COLUMN_COUNT so tiles will be clean
    //TODO make Table and BoardTable dependant on Tile dimension not vice versa
    private static int FRAME_WIDTH = 750;
    private static int FRAME_HEIGHT = 950;

    private static int BOARD_TO_FRAME_DIFFERENCE = 200;
    private static int BOARD_WIDTH = FRAME_WIDTH - BOARD_TO_FRAME_DIFFERENCE;
    private static int BOARD_HEIGHT = FRAME_HEIGHT - BOARD_TO_FRAME_DIFFERENCE;

    private static Dimension FRAME_DIMENSION = new Dimension(FRAME_WIDTH,FRAME_HEIGHT);
    private static Dimension BOARD_TABLE_PANEL_DIMENSION = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    private static Dimension TILE_PANEL_DIMENSION = new Dimension(BOARD_WIDTH / Utils.ROW_LENGTH, BOARD_HEIGHT / Utils.COLUMN_HEIGHT);

    private static String CHESS_PIECES_IMAGES_PATH = "gui/chesspieces/";
    private static String HIGHLIGHT_DOT_PATH = "gui/move_highlighter.png";
    private static String WHITE_KING = "gui/white_K.png";
    private static String BLACK_KING = "gui/black_K.png";
    private static String WHITE_KING_HIGHLIGHTER = "gui/white_K_highlighted.png";
    private static String BLACK_KING_HIGHLIGHTER = "gui/black_K_highlighted.png";
    private static String PIECE_HIGHLIGHT_PATH = "gui/piece_highlighter.png";

    private Color lightTileColor = Color.decode("#FFFACC");
    private Color darkTileColor = Color.decode("#593E1B");

    private boolean whitePlayerAI;
    private boolean blackPlayerAI;
    private boolean highlightLegalMoves;

    private static Table INSTANCE = new Table();

    private Table(){
        this.chessBoard = Board.createStandardBoard();
        //this.chessBoard = Board.createBoardFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0, 1");
        //System.out.println(FENUtils.saveGameToFEN(this.chessBoard));

        this.gameFrame = new JFrame("Chess");
        this.gameFrame.setLayout(new BorderLayout());

        JMenuBar menuBar = createMenuBar();
        this.gameFrame.setJMenuBar(menuBar);

        this.whitePlayerAI = false;
        this.blackPlayerAI = false;
        this.highlightLegalMoves = true;

        this.gameFrame.setSize(FRAME_DIMENSION);

        this.boardTablePanel = new BoardTablePanel();
        this.whitePlayerPanel = chessBoard.getWhitePlayer().getPlayerPanel();
        this.blackPlayerPanel = chessBoard.getBlackPlayer().getPlayerPanel();


        this.gameFrame.add(this.boardTablePanel, BorderLayout.CENTER);
        this.gameFrame.add(this.blackPlayerPanel, BorderLayout.NORTH);
        this.gameFrame.add(this.whitePlayerPanel, BorderLayout.SOUTH);

        this.gameFrame.setVisible(true);

    }

    public static Table get(){return INSTANCE;}

    private BoardTablePanel getBoardPanel() {
        return this.boardTablePanel;
    }

    private Board getGameBoard() {
        return this.chessBoard;
    }

    private PlayerPanel getWhitePlayerPanel(){ return whitePlayerPanel;}

    private PlayerPanel getBlackPlayerPanel(){ return blackPlayerPanel;}

    private void setWhitePlayerAI(boolean value){ this.whitePlayerAI = value; }

    private boolean getWhitePlayerAI(){ return this.whitePlayerAI; }

    private void setBlackPlayerAI(boolean value){ this.blackPlayerAI = value; }

    private boolean getBlackPlayerAI(){ return this.blackPlayerAI; }

    private void setHighlightLegalMoves(boolean value){ this.highlightLegalMoves = value; }

    private boolean getHighlightLegalMoves(){ return this.highlightLegalMoves; }

    public void show() {
        Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
    }

    public void checkmatePopUpWindow(boolean alliance){
        int input = JOptionPane.showConfirmDialog(null, (alliance ? "Black " : "White ") + "player won by checkmate\n" +
                "Do you want to create a new game?", "Checkmate", JOptionPane.YES_NO_OPTION);
        if(input == JOptionPane.YES_OPTION){
            Table.get().setUpNextGame();
        }
    }

    public void setUpNextGame(){
        Table.get().getGameBoard().getWhitePlayer().stopClock();
        Table.get().getGameBoard().getBlackPlayer().stopClock();

        PlayerPanel whitePlayerPanel = Table.get().getGameBoard().getWhitePlayer().getPlayerPanel();
        PlayerPanel blackPlayerPanel = Table.get().getGameBoard().getBlackPlayer().getPlayerPanel();

        chessBoard = Board.createStandardBoard();
        chessBoard.getWhitePlayer().setPlayerPanel(whitePlayerPanel);
        chessBoard.getBlackPlayer().setPlayerPanel(blackPlayerPanel);

        Table.get().getBoardPanel().drawBoard(chessBoard);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(createFileMenu());
        menuBar.add(createPreferencesMenu());

        return menuBar;
    }

    private JMenu createPreferencesMenu(){
        JMenu preferencesMenu = new JMenu("Preferences");

        //White Player AI
        JCheckBoxMenuItem whitePlayerAI = new JCheckBoxMenuItem("White player AI", false);
        whitePlayerAI.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Table.get().setWhitePlayerAI(!Table.get().getWhitePlayerAI());
                System.out.println("White player AI changed");
            }
        });
        preferencesMenu.add(whitePlayerAI);

        //Black Player AI
        JCheckBoxMenuItem blackPlayerAI = new JCheckBoxMenuItem("Black player AI", false);
        blackPlayerAI.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Table.get().setBlackPlayerAI(!Table.get().getBlackPlayerAI());
                System.out.println("Black player AI changed");
            }
        });
        preferencesMenu.add(blackPlayerAI);

        //Highlight Legal Moves
       JCheckBoxMenuItem highlightMoves = new JCheckBoxMenuItem("Highlight legal moves", true);
        highlightMoves.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Table.get().setHighlightLegalMoves(!Table.get().getHighlightLegalMoves());
                System.out.println("Highlight legal moves changed");
            }
        });
       preferencesMenu.add(highlightMoves);

        return preferencesMenu;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");

        //New Game
        JMenuItem newGame = new JMenuItem("New Game");
        newGame.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Starting new game");
                Table.get().setUpNextGame();
            }
        });
        fileMenu.add(newGame);

        //Load FEN
        JMenuItem loadFEN = new JMenuItem("Load game from FEN");
        loadFEN.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                String fenCode = JOptionPane.showInputDialog("Type or copy a standard FEN code");
                Table.get().getGameBoard().getWhitePlayer().stopClock();
                Table.get().getGameBoard().getBlackPlayer().stopClock();

                PlayerPanel whitePlayerPanel = Table.get().getGameBoard().getWhitePlayer().getPlayerPanel();
                PlayerPanel blackPlayerPanel = Table.get().getGameBoard().getBlackPlayer().getPlayerPanel();

                System.out.println(fenCode);
                chessBoard = FENUtils.createGameFromFEN(fenCode);
                chessBoard.getWhitePlayer().setPlayerPanel(whitePlayerPanel);
                chessBoard.getBlackPlayer().setPlayerPanel(blackPlayerPanel);

                Table.get().getBoardPanel().drawBoard(chessBoard);

                System.out.println("Loading board from FEN");
            }
        });
        fileMenu.add(loadFEN);


        //Save FEN
        JMenuItem saveFEN = new JMenuItem("Save game to FEN");
        saveFEN.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                String fenFileName = JOptionPane.showInputDialog("Type the title of your saved board, you can find it in folder \"saves\"");
                File saveFile = new File(Utils.SAVE_PATH + fenFileName + ".txt");
                int counter = 0;
                while(true){
                    counter++;
                    try {
                        if(saveFile.createNewFile()){
                            break;
                        } else {
                            saveFile = new File(Utils.SAVE_PATH + fenFileName + counter + ".txt");
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                FileWriter myWriter = null;
                try {
                    myWriter = new FileWriter(Utils.SAVE_PATH + saveFile.getName());
                    String text = FENUtils.saveGameToFEN(Table.get().getGameBoard());
                    myWriter.write(text);
                    myWriter.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                System.out.println("Saving board to FEN");
            }
        });
        fileMenu.add(saveFEN);

        //Load from PGN
        JMenuItem loadPGN = new JMenuItem("Load game from PGN");
        loadPGN.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Loading game from PGN");
            }
        });
        fileMenu.add(loadPGN);

        //Save to PGN
        JMenuItem savePGN = new JMenuItem("Save game to PGN");
        savePGN.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Saving game to PGN");
            }
        });
        fileMenu.add(savePGN);

        return fileMenu;
    }

    private class BoardTablePanel extends JPanel{
        List<TilePanel> boardTiles;

        BoardTablePanel(){
            super(new GridLayout(Utils.ROW_LENGTH, Utils.COLUMN_HEIGHT));
            this.boardTiles = new ArrayList<>();
            for(int i = 0; i < Utils.TILE_COUNT; i++){
                TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setBounds(BOARD_TO_FRAME_DIFFERENCE/2, 0, BOARD_WIDTH, BOARD_HEIGHT);
            validate();
        }

        public void drawBoard(Board chessBoard) {
            removeAll();
            for(TilePanel tilePanel : boardTiles){
                tilePanel.drawTile(chessBoard);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }

    private class TilePanel extends JPanel{

        private int tileID;

        TilePanel(BoardTablePanel boardTablePanel, int tileID){
            super(new GridBagLayout());
            this.tileID = tileID;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignChessPieceIcon(chessBoard);

            addMouseListener(new MouseListener() { //will implement drag and drop and click and play - click and play will have right mouse button for release
                @Override
                public void mouseClicked(MouseEvent e) {

                    if(isRightMouseButton(e)){
                        clearSelection();
                    }

                    else if(isLeftMouseButton(e)){
                        if(sourceTile == null) {
                            sourceTile = chessBoard.getTile(tileID);
                            movedPiece = sourceTile.getPiece();
                            if (movedPiece == null) {
                                sourceTile = null;
                            }
                        } else {
                            destinationTile = chessBoard.getTile(tileID);
                            if(sourceTile.getTileCoordinates() == destinationTile.getTileCoordinates()){
                                clearSelection();
                            } else {
                                Move move = Move.moveMaker.createMove(chessBoard, sourceTile.getTileCoordinates(), destinationTile.getTileCoordinates());
                                BoardTransition transition = chessBoard.getCurrentPlayer().makeMove(move);
                                if(transition.getMoveStatus().isDone()){
                                    chessBoard.getCurrentPlayer().stopClock();
                                    if(chessBoard.getCurrentPlayer().getAlliance()){
                                        chessBoard.getWhitePlayer().getPlayerPanel().update(chessBoard.getWhitePlayer().getChessClock().getTimeLeft() + Utils.DEFAULT_INCREMENT);
                                    } else {
                                        chessBoard.getBlackPlayer().getPlayerPanel().update(chessBoard.getBlackPlayer().getChessClock().getTimeLeft() + Utils.DEFAULT_INCREMENT);
                                    }
                                    chessBoard = transition.getNewBoard();

                                    if(chessBoard.getCurrentPlayer().isInCheckMate()){
                                        SwingUtilities.invokeLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                boardTablePanel.drawBoard(chessBoard);
                                            }
                                        });
                                        checkmatePopUpWindow(chessBoard.getCurrentPlayer().getAlliance());
                                    }
                                    chessBoard.getCurrentPlayer().startClock();

                                    if((chessBoard.getCurrentPlayer().getAlliance() && Table.get().getWhitePlayerAI()) ||
                                            (!chessBoard.getCurrentPlayer().getAlliance() && Table.get().getBlackPlayerAI())){
                                        do{
                                            System.out.println("Am I stuck?");
                                            transition = chessBoard.getCurrentPlayer().makeMove(Utils.getRandomMove(chessBoard));
                                        } while(!transition.getMoveStatus().isDone());

                                        chessBoard.getCurrentPlayer().stopClock();
                                        chessBoard = transition.getNewBoard();

                                        if(chessBoard.getCurrentPlayer().isInCheckMate()){
                                            SwingUtilities.invokeLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                    boardTablePanel.drawBoard(chessBoard);
                                                }
                                            });
                                            checkmatePopUpWindow(chessBoard.getCurrentPlayer().getAlliance());
                                        }

                                        chessBoard.getCurrentPlayer().startClock();
                                    }
                                    //TODO add move to move log for PGN save
                                } else if(transition.getMoveStatus().isCheck()){
                                    try {
                                        //TODO
                                        highlightAttackedKing(chessBoard);
                                    } catch (IOException ioException) {
                                        ioException.printStackTrace();
                                    }
                                }
                                sourceTile = null;
                                movedPiece = null;
                                destinationTile = null;
                            }
                        }
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            boardTablePanel.drawBoard(chessBoard);
                        }
                    });
                }

                @Override
                public void mousePressed(MouseEvent e) { }

                @Override
                public void mouseReleased(MouseEvent e) { }

                @Override
                public void mouseEntered(MouseEvent e) { }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });

            validate();
        }

        private void highlightAttackedKing(Board board) throws IOException {
            //TODO
        }

        private void assignChessPieceIcon(Board board){
            this.removeAll();
            if(!board.getTile(this.tileID).isEmpty()){
                try{
                    BufferedImage pieceImage = ImageIO.read(new File(CHESS_PIECES_IMAGES_PATH +
                            (board.getTile(this.tileID).getPiece().getPieceAlliance() ? "white_" : "black_") +
                            board.getTile(this.tileID).getPiece().toString() + ".gif"));
                    add(new JLabel(new ImageIcon(pieceImage)));
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        private void highlightLegalMoves(Board board){ //used for debugging

            for(Move move : pieceLegalMoves(board)){
                if(move.getDestinationCoordinate() == this.tileID && highlightLegalMoves){
                    try {
                        add(new JLabel(new ImageIcon(ImageIO.read(new File(HIGHLIGHT_DOT_PATH)))));
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        private Collection<Move> pieceLegalMoves(final Board board) {
            if(movedPiece != null && movedPiece.getPieceAlliance() == board.getCurrentPlayer().getAlliance()) {
                Collection<Move> legalMoves;
                legalMoves = movedPiece.calculateAllLegalMoves(board);
                if(movedPiece.isKing()){
                    legalMoves.addAll(movedPiece.getPieceAlliance() ?
                            board.getWhitePlayer().calculateCastleMoves(board.getBlackPlayer().getMyMoves(), true) :
                            board.getBlackPlayer().calculateCastleMoves(board.getWhitePlayer().getMyMoves(), false));
                }
                return legalMoves;
            }

            return Collections.emptyList();
        }


        private void assignTileColor() {

            int rowNumber = Utils.getRowNumber(this.tileID);
            int columnNumber = Utils.getColumnNumber(this.tileID);

            setBackground(rowNumber % 2 == columnNumber % 2 ? lightTileColor : darkTileColor);
        }

        public void drawTile(Board board) {
            assignTileColor();
            assignChessPieceIcon(chessBoard);
            highlightLegalMoves(board);


            validate();
            repaint();
        }
    }

    private void clearSelection(){
        sourceTile = null;
        destinationTile = null;
        movedPiece = null;
    }

}
