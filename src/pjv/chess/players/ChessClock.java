package pjv.chess.players;

import pjv.chess.gui.PlayerPanel;
import pjv.chess.gui.Table;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    public synchronized void requestStop(){
        this.stopRequested = true;
    }
    public synchronized boolean isStopRequested(){
        return this.stopRequested;
    }

    private void sleep(long milisecs){
        try {
            Thread.sleep(milisecs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getTimeLeft(){ return this.timeLeft;}
}
