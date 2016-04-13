import java.io.File;
import java.io.IOException;

public class Replay implements Runnable{
  private Board bd;
  private Thread thread;
  Replay(Board botBoard) throws InterruptedException {
    thread = new Thread(this);
    bd = botBoard;
    thread.start();
  }
  
  @Override
  public void run() {
    try {
      bd.replayGaming();
    } catch (InterruptedException | IOException e) {
      e.printStackTrace();
    }
  }
}
