package pjv.chess.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Table {

    private JFrame gameFrame;

    private static int FRAME_WIDTH = 800;
    private static int FRAME_HEIGHT = 800;
    private static Dimension FRAME_DIMENSION = new Dimension(FRAME_WIDTH,FRAME_HEIGHT);

    public Table(){
        this.gameFrame = new JFrame("Chess");
        JMenuBar menuBar = createMenuBar();
        this.gameFrame.setJMenuBar(menuBar);
        this.gameFrame.setSize(FRAME_DIMENSION);
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


}
