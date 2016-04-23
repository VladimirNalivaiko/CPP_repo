package minesweeperPackage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Replay implements Runnable {
  private Server board;
  private String replayFileName;
  private Thread thread;
  private int SLEEP_TIME = 700;
  private File boardFile;
  ArrayList<Integer> clickArrayList = new ArrayList<Integer>();

  Replay(Server replayBoard, String replayName) throws InterruptedException {
    replayFileName = replayName;
    thread = new Thread(this);
    board = replayBoard;
    thread.start();
  }
  
  public void startReplayThread(){
    thread = new Thread(this);
    thread.start();
  }
  
  public ArrayList<Integer> setBoard() throws IOException {
    ArrayList<Integer> boardOfElemnts = new ArrayList<Integer>();
    boardFile = new File(replayFileName);
    InputStream boardInputStream = new FileInputStream(boardFile);
    int buf = 0;
    for (int i = 0; i < 3; i++) {
      buf = boardInputStream.read();
      boardOfElemnts.add(buf);
    }
    for (int i = 0; i < boardOfElemnts.get(2); i++) {
      buf = boardInputStream.read();
      buf = boardInputStream.read();
    }
    while (!(boardInputStream.available() == 0)) {
      buf = boardInputStream.read();
      clickArrayList.add(buf);
    }
    boardInputStream.close();
    
    return boardOfElemnts;
  }

  @Override
  public void run() {
    try {
      int xPositionOfBotChoose = 0;
      int yPositionOfBotChoose = 0;
      int mouseButtonVariant = 0;
      setBoard();
      int currentElement = 0;
      while (!board.getField()[0][0].getIsAnyBanged() && 
          board.getNumOfUncoveredBombs() != 0) {
        xPositionOfBotChoose = clickArrayList.get(currentElement++);
        yPositionOfBotChoose = clickArrayList.get(currentElement++);
        mouseButtonVariant = clickArrayList.get(currentElement++);
        Thread.sleep(SLEEP_TIME);
        if (mouseButtonVariant == 0) {
          board.actionAnalisys(xPositionOfBotChoose, yPositionOfBotChoose);
        }
        if (mouseButtonVariant == 1) {
          board.rightMouseButtonListener(xPositionOfBotChoose,
              yPositionOfBotChoose);
        }
      }
    } catch (InterruptedException | IOException e) {
      e.printStackTrace();
    }
  }
}
