import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

public class Server implements Runnable {

  private Bot bot;
  private static boolean isBot;
  private Replay replay;
  private static boolean isReplay;
  private File boardFile = new File("Board.txt");
  private File userFile = new File("Replay.txt");
  private ArrayList<Integer> boardReplayArrayList = new ArrayList<Integer>();
  private ArrayList<Integer> userArrayList = new ArrayList<Integer>();
  private ArrayList<Integer> bombArrayList = new ArrayList<Integer>();
  private Client client;
  private Thread serverThread;
  private int numOfUncoveredBombs = 10;
  private int numOfWrongFlags = 0;
  private int numOfRows;
  private int numOfColumns;
  private int numOfBombs;
  private boolean isStoped;

  private static boolean inGame;

  protected Cell[][] field;

  Server(int ñolumns, int rows, int bombs, boolean isBot, boolean isReplay)
      throws InterruptedException, IOException {
    if (serverThread == null) {
      serverThread = new Thread(this);
      serverThread.start();
    }
    if (isReplay) {
      InputStream boardInputStream = new FileInputStream(boardFile);
      numOfColumns = boardInputStream.read();
      numOfRows = boardInputStream.read();
      numOfBombs = boardInputStream.read();
      numOfUncoveredBombs = numOfBombs;
      int buf = 0;
      for (int i = 0; i < numOfBombs; i++) {
        buf = boardInputStream.read();
        bombArrayList.add(buf);
        buf = boardInputStream.read();
        bombArrayList.add(buf);
      }
      boardInputStream.close();
      Init();
      inGame = true;
      this.isReplay = isReplay;
      replay = new Replay(this);
    } else {
      numOfColumns = ñolumns;
      numOfRows = rows;
      numOfBombs = bombs;
      numOfUncoveredBombs = numOfBombs;
      Init();
      boardReplayArrayList.add(ñolumns);
      boardReplayArrayList.add(rows);
      boardReplayArrayList.add(bombs);
      isStoped = false;
      this.isBot = isBot;
      if (isBot) {
        bot = new Bot(this);
        inGame = true;
      }
    }    
  }

  public static boolean isReplay() {
    return isReplay;
  }

  public static void setReplay(boolean isReplay) {
    Server.isReplay = isReplay;
  }

  void setClient(Client client) {
    this.client = client;
  }

  public boolean getThreadCondition() {
    return serverThread.isAlive();
  }

  void writeReplay() throws IOException {
    OutputStream boardOutPutStream = new FileOutputStream(boardFile);
    for (int i = 0; i < boardReplayArrayList.size(); i++) {
      boardOutPutStream.write(boardReplayArrayList.get(i).intValue());
    }
    boardOutPutStream.flush();
    boardOutPutStream.close();

    OutputStream userOutPutStream = new FileOutputStream(userFile);
    for (int i = 0; i < userArrayList.size(); i++) {
      userOutPutStream.write(userArrayList.get(i).intValue());
    }
    userOutPutStream.flush();
    userOutPutStream.close();
    userArrayList.clear();
    boardReplayArrayList.clear();
    bombArrayList.clear();
  }

  public void Init() throws InterruptedException {

    field = new Cell[numOfRows][numOfColumns];
    for (int i = 0; i < numOfRows; i++) {
      for (int j = 0; j < numOfColumns; j++) {
        field[i][j] = new Cell(j, i);
      }
    }
  }

  public void newGame() throws InterruptedException {
    Random random = new Random();
    int numOfPastedBombs = 0;
    int xPositionOfPasteBomb = 0;
    int yPositionOfPasteBomb = 0;

    if (isReplay) {
      for (int i = 0; i < numOfBombs * 2;) {
        xPositionOfPasteBomb = bombArrayList.get(i++);
        yPositionOfPasteBomb = bombArrayList.get(i++);
        field[yPositionOfPasteBomb][xPositionOfPasteBomb].setIsBomb(true);
      }
    } else {
      while (numOfPastedBombs < numOfBombs) {
        xPositionOfPasteBomb = Math.abs(random.nextInt() % numOfColumns);
        yPositionOfPasteBomb = Math.abs(random.nextInt() % numOfRows);
        if (!(field[yPositionOfPasteBomb][xPositionOfPasteBomb].getIsBomb())
            && !(field[yPositionOfPasteBomb][xPositionOfPasteBomb].getIsOpen())) {
          field[yPositionOfPasteBomb][xPositionOfPasteBomb].setIsBomb(true);
          numOfPastedBombs++;
          boardReplayArrayList.add(xPositionOfPasteBomb);
          boardReplayArrayList.add(yPositionOfPasteBomb);
        }
      }
      inGame = true;
    }
  }

  /**
   * This function finds empty cells
   *
   * @param x cell's column
   * @param y cell's row
   */
  public void findEmptyCells(int x, int y) {
    if (!field[y][x].getIsBomb()) {
      if (findNearBombs(x, y) != 0) {
        field[y][x].setToRepaint(true);
        field[y][x].setIsOpen(true);
        return;
      }
      if (y != 0) {
        if (findNearBombs(x, y - 1) == 0 && !field[y - 1][x].getIsOpen()) {
          field[y - 1][x].setIsOpen(true);
          field[y - 1][x].setToRepaint(true);
          findEmptyCells(x, y - 1);
        } else {
          field[y - 1][x].setIsOpen(true);
          field[y - 1][x].setToRepaint(true);
        }
      }
      if (x != 0) {
        if (findNearBombs(x - 1, y) == 0 && !field[y][x - 1].getIsOpen()) {
          field[y][x - 1].setToRepaint(true);
          field[y][x - 1].setIsOpen(true);
          findEmptyCells(x - 1, y);
        } else {
          field[y][x - 1].setIsOpen(true);
          field[y][x - 1].setToRepaint(true);
        }
      }
      if (y != numOfRows - 1) {
        if (findNearBombs(x, y + 1) == 0 && !field[y + 1][x].getIsOpen()) {
          field[y + 1][x].setIsOpen(true);
          field[y + 1][x].setToRepaint(true);
          findEmptyCells(x, y + 1);
        } else {
          field[y + 1][x].setIsOpen(true);
          field[y + 1][x].setToRepaint(true);
        }
      }
      if (x != numOfColumns - 1) {
        if (findNearBombs(x + 1, y) == 0 && !field[y][x + 1].getIsOpen()) {
          field[y][x + 1].setIsOpen(true);
          field[y][x + 1].setToRepaint(true);
          findEmptyCells(x + 1, y);
        } else {
          field[y][x + 1].setIsOpen(true);
          field[y][x + 1].setToRepaint(true);
        }
      }
      if (y != 0 && x != 0) {
        if (findNearBombs(x - 1, y - 1) == 0 && !field[y - 1][x - 1].getIsOpen()) {
          field[y - 1][x - 1].setIsOpen(true);
          field[y - 1][x - 1].setToRepaint(true);
          findEmptyCells(x - 1, y - 1);
        } else {
          field[y - 1][x - 1].setIsOpen(true);
          field[y - 1][x - 1].setToRepaint(true);
        }
      }
      if (y != numOfRows - 1 && x != 0) {
        if (findNearBombs(x - 1, y + 1) == 0 && !field[y + 1][x - 1].getIsOpen()) {
          field[y + 1][x - 1].setIsOpen(true);
          field[y + 1][x - 1].setToRepaint(true);
          findEmptyCells(x - 1, y + 1);
        } else {
          field[y + 1][x - 1].setIsOpen(true);
          field[y + 1][x - 1].setToRepaint(true);
        }
      }
      if (y != numOfRows - 1 && x != numOfColumns - 1) {
        if (findNearBombs(x + 1, y + 1) == 0 && !field[y + 1][x + 1].getIsOpen()) {
          field[y + 1][x + 1].setIsOpen(true);
          field[y + 1][x + 1].setToRepaint(true);
          findEmptyCells(x + 1, y + 1);
        } else {
          field[y + 1][x + 1].setIsOpen(true);
          field[y + 1][x + 1].setToRepaint(true);
        }
      }
      if (y != 0 && x != numOfColumns - 1) {
        if (findNearBombs(x + 1, y - 1) == 0 && !field[y - 1][x + 1].getIsOpen()) {
          field[y - 1][x + 1].setIsOpen(true);
          field[y - 1][x + 1].setToRepaint(true);
          findEmptyCells(x + 1, y - 1);
        } else {
          field[y - 1][x + 1].setIsOpen(true);
          field[y - 1][x + 1].setToRepaint(true);
        }
      }
    }
  }

  /**
   * This function finds quantity of near bombs
   * 
   * @param x cell's column
   * @param y cell's row
   * @return return from function
   */
  public int findNearBombs(int x, int y) {
    int numOfNearBombs = 0;
    if (y != 0) {
      if (field[y - 1][x].getIsBomb()) {
        numOfNearBombs++;
      }
    }
    if (y != 0 && x != 0) {
      if (field[y - 1][x - 1].getIsBomb()) {
        numOfNearBombs++;
      }
    }
    if (x != 0) {
      if (field[y][x - 1].getIsBomb()) {
        numOfNearBombs++;
      }
    }
    if (x != 0 && y != numOfRows - 1) {
      if (field[y + 1][x - 1].getIsBomb()) {
        numOfNearBombs++;
      }
    }
    if (y != numOfRows - 1) {
      if (field[y + 1][x].getIsBomb()) {
        numOfNearBombs++;
      }
    }
    if (x != numOfColumns - 1 && y != numOfRows - 1) {
      if (field[y + 1][x + 1].getIsBomb()) {
        numOfNearBombs++;
      }
    }
    if (x != numOfColumns - 1) {
      if (field[y][x + 1].getIsBomb()) {
        numOfNearBombs++;
      }
    }
    if (x != numOfColumns - 1 && y != 0) {
      if (field[y - 1][x + 1].getIsBomb()) {
        numOfNearBombs++;
      }
    }
    field[y][x].setNumOfNeighborBombs(numOfNearBombs);
    return numOfNearBombs;
  }

  /**
   * This function is for analysis players or bot actions
   * 
   * @param pressedCol cell's column
   * @param pressedRow cell's row
   * @throws InterruptedException throws InterruptedException
   * @throws IOException throws IOException
   */
  public void actionAnalisys(int pressedCol, int pressedRow)
      throws InterruptedException, IOException {
    Cell pressedCell = field[pressedRow][pressedCol];
    if (pressedCell.getIsAnyBanged())
      return;
    if (!pressedCell.getIsAnyClicked()) {
      pressedCell.setIsOpen(true);
      newGame();

      if (!isReplay) {
        userArrayList.add(pressedCol);
        userArrayList.add(pressedRow);
        userArrayList.add(0);
      }
      client.repaint();
      findEmptyCells(pressedCol, pressedRow);
      pressedCell.setIsAnyClicked(true);
      return;
    }
    if (pressedCell.getIsBomb() && !(pressedCell.getIsOpen())) {
      pressedCell.setIsOpen(true);
      pressedCell.setIsAnyBanged(true);
      if (!isReplay) {
        userArrayList.add(pressedCol);
        userArrayList.add(pressedRow);
        userArrayList.add(0);
      }
      if (!isReplay) {
        writeReplay();
      }
      isReplay = false;
      isBot = false;
      client.finish();
      return;
    }
    if (!pressedCell.getIsOpen()) {
      if (!isReplay) {
        userArrayList.add(pressedCol);
        userArrayList.add(pressedRow);
        userArrayList.add(0);
      }
      findEmptyCells(pressedCol, pressedRow);
      pressedCell.setIsOpen(true);
      client.repaint();
    }
  }

  void rightMouseButtonListener(int pressedCol, int pressedRow)
      throws InterruptedException, IOException {
    Cell pressedCell = field[pressedRow][pressedCol];
    if (!isReplay) {
      userArrayList.add(pressedCol);
      userArrayList.add(pressedRow);
      userArrayList.add(1);
    }
    if (!pressedCell.getIsSooposedToBeBomb()) {
      if (pressedCell.getIsBomb())
        numOfUncoveredBombs--;
      else
        numOfWrongFlags++;
      pressedCell.setIsSooposedToBeBomb(true);
    } else {
      pressedCell.setIsSooposedToBeBomb(false);
      if (pressedCell.getIsBomb())
        numOfUncoveredBombs++;
      else
        numOfWrongFlags--;
    }
    if (numOfUncoveredBombs == 0 && numOfWrongFlags == 0) {
      if (!isReplay) {
        writeReplay();
      }
      client.finish();
      return;
    }
    return;
  }


  public Cell[][] getField() {
    return field;
  }

  public Cell getFieldElement(int i, int j) {
    return field[i][j];
  }

  public int getNumOfUncoveredBombs() {
    return numOfUncoveredBombs;
  }

  public void setNumOfUncoveredBombs(int numOfUncoveredBombs) {
    this.numOfUncoveredBombs = numOfUncoveredBombs;
  }

  public int getNumOfWrongFlags() {
    return numOfWrongFlags;
  }

  public void setNumOfWrongFlags(int numOfWrongFlags) {
    this.numOfWrongFlags = numOfWrongFlags;
  }

  public int getNumOfRows() {
    return numOfRows;
  }

  public void setNumOfRows(int numOfRows) {
    this.numOfRows = numOfRows;
  }

  public int getNumOfColumns() {
    return numOfColumns;
  }

  public void setNumOfColumns(int numOfColumns) {
    this.numOfColumns = numOfColumns;
  }

  public int getNumOfBombs() {
    return numOfBombs;
  }

  public void setNumOfBombs(int numOfBombs) {
    this.numOfBombs = numOfBombs;
  }

  @Override
  public void run() {

    while (!serverThread.isInterrupted()) {

    }
  }

}
