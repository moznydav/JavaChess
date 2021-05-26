package pjv.chess.players;

import pjv.chess.gui.PlayerPanel;
import pjv.chess.gui.Table;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Runnable class that's main purpose is counting down time left as normal chess clock would do
 *
 * @author David Mozny
 */
public class ChessClock implements Runnable{

    int timeLeft;
    boolean alliance;
    PlayerPanel playerPanel;

    private boolean stopRequested = false;


    public ChessClock(int timeLeft, boolean alliance, PlayerPanel playerPanel){
        this.timeLeft = timeLeft;
        this.alliance = alliance;
        this.playerPanel = playerPanel;
    }

    /**
     * Main method of class that decreases time left of active player and updates chess clock GUI
     */
    @Override
    public void run() {
        if (!isStopRequested()) {
            //sleep(1000);
            this.timeLeft -= 1;

            if(this.alliance){
                playerPanel.update(this.timeLeft);
                //System.out.println("White has " + timeLeft + " seconds left");

            } else {
                //System.out.println("Black has " + timeLeft + " seconds left");
                playerPanel.update(this.timeLeft);
            }
            if(this.timeLeft <= 0){
                requestStop();
                JOptionPane.showMessageDialog(null, (this.alliance ? "White " : "Black ") + "player lost on time");
            }
        }
    }

    /**
     * Method used for stopping active thread
     */
    public synchronized void requestStop(){
        this.stopRequested = true;
    }

    /**
     * Getter used for stopping active thread
     * @return
     */
    private synchronized boolean isStopRequested(){
        return this.stopRequested;
    }

    private void sleep(long milisecs){
        try {
            Thread.sleep(milisecs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Rest of getters of the ChessClock class
     * @return
     */
    public int getTimeLeft(){ return this.timeLeft;}
}
