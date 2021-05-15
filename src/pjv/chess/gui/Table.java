package pjv.chess.gui;

import pjv.chess.board.*;
import pjv.chess.pieces.ChessPiece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table {

    private JFrame gameFrame;
    private BoardTablePanel boardTablePanel;
    private Board chessBoard;

    private Tile sourceTile;
    private Tile destinationTile;
    private ChessPiece movedPiece;

    //Both height and width of Board table panel dimension should be divisible by ROW_COUNT and COLUMN_COUNT so tiles will be clean
    //TODO make Table and BoardTable dependant on Tile dimension not vice versa
    private static int FRAME_WIDTH = 800;
    private static int FRAME_HEIGHT = 800;

    private static int BOARD_TO_FRAME_DIFFERENCE = 200;
    private static int BOARD_WIDTH = FRAME_WIDTH - BOARD_TO_FRAME_DIFFERENCE;
    private static int BOARD_HEIGHT = FRAME_HEIGHT - BOARD_TO_FRAME_DIFFERENCE;

    private static Dimension FRAME_DIMENSION = new Dimension(FRAME_WIDTH,FRAME_HEIGHT);
    private static Dimension BOARD_TABLE_PANEL_DIMENSION = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    private static Dimension TILE_PANEL_DIMENSION = new Dimension(BOARD_WIDTH / Utils.ROW_LENGTH, BOARD_HEIGHT / Utils.COLUMN_HEIGHT);

    private static String CHESS_PIECES_IMAGES_PATH = "gui/chesspieces/";
    private static String HIGHLIGHT_DOT_PATH = "gui/move_highlighter.gif";

    private Color lightTileColor = Color.decode("#FFFACC");
    private Color darkTileColor = Color.decode("#593E1B");

    public Table(){
        this.chessBoard = Board.createStandardBoard();
        this.gameFrame = new JFrame("Chess");
        this.gameFrame.setLayout(new BorderLayout());

        JMenuBar menuBar = createMenuBar();
        this.gameFrame.setJMenuBar(menuBar);

        this.gameFrame.setSize(FRAME_DIMENSION);
        this.boardTablePanel = new BoardTablePanel();
        this.gameFrame.add(this.boardTablePanel, BorderLayout.CENTER);

        this.gameFrame.setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(createFileMenu());

        return menuBar;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");

        //New Game
        JMenuItem newGame = new JMenuItem("New Game");
        newGame.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Starting new game");
            }
        });
        fileMenu.add(newGame);

        //Load FEN
        JMenuItem loadFEN = new JMenuItem("Load game from FEN");
        loadFEN.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Loading board from FEN");
            }
        });
        fileMenu.add(loadFEN);


        //Save FEN
        JMenuItem saveFEN = new JMenuItem("Save game to FEN");
        saveFEN.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
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
            setPreferredSize(BOARD_TABLE_PANEL_DIMENSION);
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
                        System.out.println("Rightclicked");
                        clearSelection();
                    }

                    else if(isLeftMouseButton(e)){
                        if(sourceTile == null) {
                            sourceTile = chessBoard.getTile(tileID);
                            movedPiece = sourceTile.getPiece();

                            System.out.println("Leftclicked");
                            System.out.println("Piece selected:" + movedPiece.toString());
                            if (movedPiece == null) {
                                sourceTile = null;
                            }
                        } else {
                            destinationTile = chessBoard.getTile(tileID);
                            if(sourceTile.getTileCoordinates() == destinationTile.getTileCoordinates()){

                                clearSelection();
                            } else {
                                System.out.println("Destination selected" + destinationTile.getTileCoordinates());
                                final Move move = Move.moveMaker.createMove(chessBoard, sourceTile.getTileCoordinates(), destinationTile.getTileCoordinates());
                                final BoardTransition transition = chessBoard.getCurrentPlayer().makeMove(move);
                                System.out.println("I got here");
                                System.out.println(transition.getMoveStatus().isIllegal() ? "its is illegal" : "it is legal") ;

                                if(transition.getMoveStatus().isDone()){
                                    System.out.println("I even got here");
                                    chessBoard = transition.getNewBoard();
                                    //TODO add move to move log for PGN save
                                }
                                sourceTile = null;
                                movedPiece = null;
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
                    if(move.getDestinationCoordinate() == this.tileID){
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
                return movedPiece.calculateAllLegalMoves(board);
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
        System.out.println("Piece selection reset");
        sourceTile = null;
        destinationTile = null;
        movedPiece = null;
    }

}
