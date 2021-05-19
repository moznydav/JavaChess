package pjv.chess.players;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ChessClock implements Runnable{

    int timeLeft;
    int increment;
    boolean alliance;
    private boolean stopRequested = false;


    public ChessClock(int timeLeft, boolean alliance){
        this.timeLeft = timeLeft;
        this.alliance = alliance;
    }

    @Override
    public void run() {
        if (!isStopRequested()) {
            //sleep(1000);
            this.timeLeft -= 1;

            if(this.alliance){
                System.out.println("White has " + this.timeLeft + " seconds left");
            } else {

                System.out.println("Black has " + this.timeLeft + " seconds left");
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
