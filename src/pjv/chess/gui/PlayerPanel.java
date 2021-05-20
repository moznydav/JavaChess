package pjv.chess.gui;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class PlayerPanel extends JPanel{
    private JPanel takenPiecesPanel;
    private JPanel chessClockPanel;
    private JLabel chessClockText;

    private static int DEFAULT_TIME = 300;

    private static Color PANEL_COLOR = Color.decode("#d3d3d3");
    private static Color BLACK = Color.decode("#000000");
    private static Color WHITE = Color.decode("#FFFFFF");

    private static final Dimension PLAYER_PANEL_DIMENSION = new Dimension(80, 60);
    private static EtchedBorder PANEL_BOARDER = new EtchedBorder(EtchedBorder.LOWERED);

    public PlayerPanel(boolean alliance){ //true = white, false = black;
        super(new BorderLayout());
        this.setBackground(PANEL_COLOR);
        this.takenPiecesPanel = new JPanel((new GridLayout(1,7)));
        this.chessClockPanel = new JPanel();
        this.chessClockText = new JLabel();
        this.chessClockPanel.setBorder(PANEL_BOARDER);

        chessClockPanel.add(chessClockText);

        this.takenPiecesPanel.setBackground(PANEL_COLOR);
        this.add(this.takenPiecesPanel, BorderLayout.WEST);
        this.add(this.chessClockPanel, BorderLayout.EAST);


        if(alliance){ //is white's player panel
            this.chessClockPanel.setBackground(WHITE);
            this.chessClockText.setForeground(BLACK);
        } else {
            this.chessClockPanel.setBackground(BLACK);
            this.chessClockText.setForeground(WHITE);
        }
        chessClockText.setFont(new Font("Dialog", Font.BOLD, 40));
        chessClockText.setText(formatTime(DEFAULT_TIME));

        setPreferredSize(PLAYER_PANEL_DIMENSION);
    }

    public void update(int timeLeft){
        chessClockText.setText(formatTime(timeLeft));
    }

    private String formatTime(int timeLeft){

        Integer minutes = new Integer(timeLeft / 60);
        Integer seconds = new Integer(timeLeft % 60);
        String formatedTime;

        if(seconds < 10){
            formatedTime = minutes.toString() + ":0" + seconds.toString();
        } else {
            formatedTime = minutes.toString() + ":" + seconds.toString();
        }
        return formatedTime;
    }

}
