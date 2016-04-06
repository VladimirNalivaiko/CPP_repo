
public class Bot implements Runnable {
  private Board bd;
  private Thread thread;

  Bot(Board botBoard) throws InterruptedException {
    thread = new Thread(this);
    bd = botBoard;
    thread.start();
  }

  public void restart() {
    thread.start();
  }

  @Override
  public void run() {
    try {
      bd.botGaming();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}


