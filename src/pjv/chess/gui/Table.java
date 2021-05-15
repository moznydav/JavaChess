package pjv.chess.gui;

import pjv.chess.board.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Table {

    private JFrame gameFrame;
    private BoardTablePanel boardTablePanel;

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

    private Color lightTileColor = Color.decode("#FFFACC");
    private Color darkTileColor = Color.decode("#593E1B");

    public Table(){
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
    }

    private class TilePanel extends JPanel{


        private int tileID;

        TilePanel(BoardTablePanel boardTablePanel, int tileID){
            super(new GridBagLayout());
            this.tileID = tileID;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            validate();

        }

        private void assignTileColor() {
            if(Utils.getRowNumber(this.tileID) % 2 == 0 && this.tileID % 2 == 0 ||
                    (Utils.getRowNumber(this.tileID) % 2 == 1 && this.tileID % 2 == 1)){
                setBackground(lightTileColor);
            } else {
                setBackground(darkTileColor);
            }
        }
    }

}
