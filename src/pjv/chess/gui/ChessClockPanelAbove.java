package pjv.chess.gui;

import pjv.chess.players.ChessClock;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class ChessClockPanelAbove extends JPanel {

    private JPanel takenPiecesPanel;
    private JPanel chessClockPanel;

    private static Color PANEL_COLOR = Color.decode("#d3d3d3");
    private static final Dimension CHESSCLOCK_PANEL_DIMENSION = new Dimension(80, 60);
    //private static EtchedBorder PANEL_BOARDER = new EtchedBorder(EtchedBorder.LOWERED);

    public ChessClockPanelAbove(){
        super(new BorderLayout());
        this.setBackground(PANEL_COLOR);
        //this.setBorder(PANEL_BOARDER);
        this.takenPiecesPanel = new JPanel((new GridLayout(8,2)));
        this.chessClockPanel = new JPanel(new GridLayout(1, 2));
        this.takenPiecesPanel.setBackground(PANEL_COLOR);
        this.chessClockPanel.setBackground(PANEL_COLOR);
        this.add(this.takenPiecesPanel, BorderLayout.EAST);
        this.add(this.chessClockPanel, BorderLayout.WEST);
        setPreferredSize(CHESSCLOCK_PANEL_DIMENSION);
    }

    public void update(ChessClock chessClock){
        chessClockPanel.removeAll();


        validate();
    }

}
