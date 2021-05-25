package pjv.chess.gui;

import pjv.chess.board.*;
import pjv.chess.pieces.*;
import pjv.chess.players.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public List<Board> exploredGame;
    public int exploredGamePosition;

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
    private boolean customizeChessBoard;

    private int result; //white gain = 0 - white lost, 1 - draw, 2  - win, 3 - not finished;

    private List<String> moveLog;

    private static Table INSTANCE = new Table();

    private Table(){
        this.chessBoard = Board.createStandardBoard();
        this.moveLog = new ArrayList<>();
        this.result = 3;
        //this.chessBoard = Board.createBoardFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0, 1");
        //System.out.println(FENUtils.saveGameToFEN(this.chessBoard));

        this.gameFrame = new JFrame("Chess");
        this.gameFrame.setLayout(new BorderLayout());

        JMenuBar menuBar = createMenuBar();
        this.gameFrame.setJMenuBar(menuBar);

        this.whitePlayerAI = false;
        this.blackPlayerAI = false;
        this.highlightLegalMoves = true;
        this.customizeChessBoard = false;

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

    private boolean getCustomizeChessBoard(){ return this.customizeChessBoard; }

    private void setCustomizeChessBoard(boolean value){ this.customizeChessBoard = value; }

    private int getResult(){ return this.result;}

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

    public void staleMatePopUpWindow(){
        int input = JOptionPane.showConfirmDialog(null, "Draw by stalemate\n" +
                "Do you want to create a new game?", "Stalemate", JOptionPane.YES_NO_OPTION);
        if(input == JOptionPane.YES_OPTION){
            Table.get().setUpNextGame();
        }
    }

    public void endGameCheck(Player currentPlayer){
        if(currentPlayer.isInCheckMate()){
            this.result = currentPlayer.getAlliance() ? 0 : 2;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    boardTablePanel.drawBoard(chessBoard);
                }
            });
            checkmatePopUpWindow(currentPlayer.getAlliance());
        } else if(currentPlayer.isInStaleMate()){
            this.result = 1;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    boardTablePanel.drawBoard(chessBoard);
                }
            });
            staleMatePopUpWindow();
        }
    }

    public void setUpNextGame(){
        Table.get().getGameBoard().getWhitePlayer().stopClock();
        Table.get().getGameBoard().getBlackPlayer().stopClock();

        PlayerPanel whitePlayerPanel = Table.get().getGameBoard().getWhitePlayer().getPlayerPanel();
        PlayerPanel blackPlayerPanel = Table.get().getGameBoard().getBlackPlayer().getPlayerPanel();

        moveLog.clear();

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

        //Customize chessBoard
        JMenuItem customizeChessBoard = new JMenuItem("Game with custom layout");

        JButton startButton = new JButton("Start Game");


        customizeChessBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Table.get().setCustomizeChessBoard(true);
                chessBoard = Board.createCustomBoard();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        boardTablePanel.drawBoard(chessBoard);
                    }
                });
                chessBoard.getWhitePlayer().setPlayerPanel(whitePlayerPanel);
                chessBoard.getBlackPlayer().setPlayerPanel(blackPlayerPanel);

                PlayerPanel whitePlayerPanel = Table.get().getGameBoard().getWhitePlayer().getPlayerPanel();
                startButton.setBounds(270,5,180,50);


                startButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        startButton.setVisible(false);
                        Table.get().setCustomizeChessBoard(false);
                        chessBoard.getWhitePlayer().setPlayerPanel(whitePlayerPanel);
                        chessBoard.getBlackPlayer().setPlayerPanel(blackPlayerPanel);
                    }
                });
                whitePlayerPanel.add(startButton);

                System.out.println("Customize chess board changed");
            }

        });
        fileMenu.add(customizeChessBoard);

        //Load FEN
        JMenuItem loadFEN = new JMenuItem("Load game from FEN");
        loadFEN.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                String fenCodeFile = JOptionPane.showInputDialog("Type name of your save (format \"your input\".txt)");
                String fenCode = null;

                Table.get().getGameBoard().getWhitePlayer().stopClock();
                Table.get().getGameBoard().getBlackPlayer().stopClock();

                try {
                    fenCode = Files.readString(Path.of(FENUtils.SAVE_PATH + fenCodeFile + ".txt"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

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
                File saveFile = new File(FENUtils.SAVE_PATH + fenFileName + ".txt");
                int counter = 0;
                while(true){
                    counter++;
                    try {
                        if(saveFile.createNewFile()){
                            break;
                        } else {
                            saveFile = new File(FENUtils.SAVE_PATH + fenFileName + counter + ".txt");
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                FileWriter myWriter = null;
                try {
                    myWriter = new FileWriter(FENUtils.SAVE_PATH + saveFile.getName());
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

                String pgnCodePath = JOptionPane.showInputDialog("Type name of your save (format \"your input\".txt)");

                Table.get().getGameBoard().getWhitePlayer().stopClock();
                Table.get().getGameBoard().getBlackPlayer().stopClock();

                PlayerPanel whitePlayerPanel = Table.get().getGameBoard().getWhitePlayer().getPlayerPanel();
                PlayerPanel blackPlayerPanel = Table.get().getGameBoard().getBlackPlayer().getPlayerPanel();

                String pgnCode = null;

                try {
                    pgnCode = Files.readString(Path.of(PGNUtils.SAVE_PATH + pgnCodePath + ".txt"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                chessBoard = PGNUtils.createGameFromPGN(pgnCode);

                chessBoard.getWhitePlayer().setPlayerPanel(whitePlayerPanel);
                chessBoard.getBlackPlayer().setPlayerPanel(blackPlayerPanel);

                Table.get().getBoardPanel().drawBoard(chessBoard);

                System.out.println("Loading game from PGN");
            }
        });
        fileMenu.add(loadPGN);

        //Save to PGN
        JMenuItem savePGN = new JMenuItem("Save game to PGN");
        savePGN.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                String pgnFileName = JOptionPane.showInputDialog("Type the title of your saved board, you can find it in folder \"saves\"");
                File saveFile = new File(PGNUtils.SAVE_PATH + pgnFileName + ".txt");
                int counter = 0;
                while(true){
                    counter++;
                    try {
                        if(saveFile.createNewFile()){
                            break;
                        } else {
                            saveFile = new File(PGNUtils.SAVE_PATH + pgnFileName + counter + ".txt");
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                FileWriter myWriter = null;
                try {
                    myWriter = new FileWriter(PGNUtils.SAVE_PATH + saveFile.getName());
                    String text = PGNUtils.saveGameToPGN(moveLog, Table.get().getResult());
                    myWriter.write(text);
                    myWriter.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                System.out.println("Saving board to PGN");
            }
        });
        fileMenu.add(savePGN);

        //Explore with PGN
        JMenuItem exploreGame = new JMenuItem("Explore game with PGN");
        exploreGame.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                String pgnCodePath = JOptionPane.showInputDialog("Type name of your save (format \"your input\".txt)");

                Table.get().getGameBoard().getWhitePlayer().stopClock();
                Table.get().getGameBoard().getBlackPlayer().stopClock();

                PlayerPanel whitePlayerPanel = Table.get().getGameBoard().getWhitePlayer().getPlayerPanel();
                PlayerPanel blackPlayerPanel = Table.get().getGameBoard().getBlackPlayer().getPlayerPanel();

                String pgnCode = null;

                try {
                    pgnCode = Files.readString(Path.of(PGNUtils.SAVE_PATH + pgnCodePath + ".txt"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                exploredGamePosition = 0;
                exploredGame = PGNUtils.createExploreGameFromPGN(pgnCode);
                chessBoard = exploredGame.get(exploredGamePosition);
                Table.get().getBoardPanel().drawBoard(chessBoard);

                //previous button
                JButton previousButton = new JButton("Previous");
                previousButton.setBounds(235,5,90,50);
                previousButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(exploredGamePosition > 0){
                            exploredGamePosition--;
                            chessBoard = exploredGame.get(exploredGamePosition);
                            Table.get().getBoardPanel().drawBoard(chessBoard);
                        }
                    }
                });
                whitePlayerPanel.add(previousButton);

                //previous button
                JButton nextButton = new JButton("Next");
                nextButton.setBounds(405,5,90,50);
                nextButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(exploredGamePosition < (exploredGame.size() - 1)){
                            exploredGamePosition++;
                            chessBoard = exploredGame.get(exploredGamePosition);
                            Table.get().getBoardPanel().drawBoard(chessBoard);
                        }
                    }
                });
                whitePlayerPanel.add(nextButton);
                whitePlayerPanel.invalidate();
                whitePlayerPanel.repaint();
            }
        });
        fileMenu.add(exploreGame);

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

                    if(!customizeChessBoard){
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
                                        System.out.println(PGNUtils.checkedPGNMove(move.toString(), chessBoard, move));
                                        moveLog.add(PGNUtils.checkedPGNMove(move.toString(), chessBoard, move));
                                        endGameCheck(chessBoard.getCurrentPlayer());
                                        chessBoard.getCurrentPlayer().startClock();

                                        if((chessBoard.getCurrentPlayer().getAlliance() && Table.get().getWhitePlayerAI()) ||
                                                (!chessBoard.getCurrentPlayer().getAlliance() && Table.get().getBlackPlayerAI())){
                                            do{
                                                move = Utils.getRandomMove(chessBoard);
                                                transition = chessBoard.getCurrentPlayer().makeMove(move);
                                            } while(!transition.getMoveStatus().isDone());

                                            chessBoard.getCurrentPlayer().stopClock();
                                            chessBoard = transition.getNewBoard();
                                            System.out.println(move.toString());
                                            moveLog.add(move.toString());
                                            endGameCheck(chessBoard.getCurrentPlayer());
                                            chessBoard.getCurrentPlayer().startClock();
                                        }

                                    } else if(transition.getMoveStatus().isCheck()){
                                        //TODO highlight attacked king highlightAttackedKing();
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
                    } else {
                        if(isLeftMouseButton(e)){
                            Board.Builder builder = new Board.Builder();

                            builder.keepBoard(chessBoard);

                            String[] options = {"Queen", "Knight", "Rook", "Bishop", "Pawn", "Cancel"};

                            int input = JOptionPane.showOptionDialog(null, "Select piece to place on board", "Select piece",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                            switch(input){
                                case 0:
                                    builder.setPiece(new Queen(tileID, tileID>31));
                                    break;
                                case 1:
                                    builder.setPiece(new Knight(tileID, tileID>31));
                                    break;
                                case 2:
                                    builder.setPiece(new Rook(tileID, tileID>31));
                                    break;
                                case 3:
                                    builder.setPiece(new Bishop(tileID, tileID>31));
                                    break;
                                case 4:
                                    builder.setPiece(new Pawn(tileID, tileID>31));
                                    break;
                                case 5:
                                    System.out.println("Canceled");
                                    break;
                            }
                            builder.setNextTurn(true);
                            builder.keepWhiteTime(Utils.DEFAULT_TIME - Utils.DEFAULT_INCREMENT);
                            builder.keepBlackTime(Utils.DEFAULT_TIME);
                            chessBoard = builder.build();
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    boardTablePanel.drawBoard(chessBoard);
                                }
                            });
                        }
                    }
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

        private void highlightAttackedKing() throws IOException, InterruptedException {
            int kingPosition = chessBoard.getCurrentPlayer().getPlayerKing().getPiecePosition();

            BufferedImage redKing = ImageIO.read(new File(CHESS_PIECES_IMAGES_PATH +
                    (chessBoard.getCurrentPlayer().getAlliance() ? "white_K_highlighted.png" : "black_K_highlighted.png")));

            BufferedImage normalKing = ImageIO.read(new File(CHESS_PIECES_IMAGES_PATH +
                    (chessBoard.getCurrentPlayer().getAlliance() ? "white_K.gif" : "black_K.gif")));

            for(int i = 0; i < 2; i++){
                if(!chessBoard.getTile(kingPosition).isEmpty()){
                    this.removeAll();
                    add(new JLabel(new ImageIcon(redKing)));
                    revalidate();
                    repaint();
                    Thread.sleep(300);
                    this.removeAll();
                    add(new JLabel(new ImageIcon(normalKing)));
                    revalidate();
                    repaint();
                    Thread.sleep(300);
                }
            }

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
