import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Replay implements Runnable {
  private Server board;
  private Thread thread;
  private int SLEEP_TIME = 1000;
  
  private File userFile = new File("Replay.txt");

  Replay(Server replayBoard) throws InterruptedException {
    thread = new Thread(this);
    board = replayBoard;
    thread.start();
  }

  @Override
  public void run() {
    try {
      InputStream userInputStream = new FileInputStream(userFile);
      int xPositionOfBotChoose = 0;
      int yPositionOfBotChoose = 0;
      int mouseButtonVariant = 0;
      while (!board.getField()[0][0].getIsAnyBanged()) {
        xPositionOfBotChoose = userInputStream.read();
        yPositionOfBotChoose = userInputStream.read();
        mouseButtonVariant = userInputStream.read();
        Thread.sleep(SLEEP_TIME);
        if (mouseButtonVariant == 0) {
          board.actionAnalisys(xPositionOfBotChoose, yPositionOfBotChoose);
        }
        if (mouseButtonVariant == 1) {
          board.rightMouseButtonListener(xPositionOfBotChoose, yPositionOfBotChoose);
        }
      }
      userInputStream.close();
    } catch (InterruptedException | IOException e) {
      e.printStackTrace();
    }
  }
}
