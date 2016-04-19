import java.io.IOException;
import java.util.Random;

/**
 * Class for bot.
 * 
 * @author Vladimir
 *
 */
public class Bot implements Runnable {
  private Server board;
  private Thread thread;
  private int SLEEP_TIME = 3000;

  Bot(Server botBoard) throws InterruptedException {
    thread = new Thread(this);
    board = botBoard;
    thread.start();
  }

  @Override
  public void run() {
    try {
      Random random = new Random();
      int xPositionOfBotChoose = 0;
      int yPositionOfBotChoose = 0;
      while (!board.getField()[0][0].getIsAnyBanged()) {
        xPositionOfBotChoose = Math.abs(random.nextInt() % board.getNumOfRows());
        yPositionOfBotChoose = Math.abs(random.nextInt() % board.getNumOfColumns());
        if (!(board.getField()[yPositionOfBotChoose][xPositionOfBotChoose].getIsOpen())) {
          Thread.sleep(SLEEP_TIME);
          board.actionAnalisys(xPositionOfBotChoose, yPositionOfBotChoose);
        }
      }
    } catch (InterruptedException | IOException e) {
      e.printStackTrace();
    }
  }
}


