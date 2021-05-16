package pjv.chess.players;

public class ChessClock implements Runnable{

    private static int DEFAULT_TIME = 300;

    int timeLeft = 300;
    int minutesShown = timeLeft / 60;
    int secondsShown = timeLeft % 60;
    private volatile boolean isPaused = false;


    @Override
    public void run() {
        while (true) {
            if(!isPaused){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                timeLeft -= 1;
                System.out.println("You have " + timeLeft + " seconds left");
            }
        }
    }


    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }
}


