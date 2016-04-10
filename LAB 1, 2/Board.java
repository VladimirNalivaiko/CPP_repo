import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Panel, that contains cells.
 * 
 * @author Vladimir
 *
 */
public class Board extends JPanel {
  private File userFile = new File("Replay.txt");
  private File boardFile = new File("Board.txt");
  private Bot bot;
  private static boolean isBot;
  private Replay replay;
  private static boolean isReplay;
  private ArrayList<Integer> boardReplayArrayList = new ArrayList<Integer>();
  private ArrayList<Integer> userArrayList = new ArrayList<Integer>();
  private ArrayList<Integer> bombArrayList = new ArrayList<Integer>();
  private final int CELL_SIZE = 15;
  private static int numOfUncoveredBombs = 10;
  private static int numOfWrongFlags = 0;
  private int numOfRows;
  private int numOfColumns;
  private int numOfBombs;
  private static boolean inGame;
  private int SLEEP_TIME = 800;

  static protected Image[] img = new Image[13];
  static protected Cell[][] field;

  public Board(int ñolumns, int rows, int bombs, boolean isBot, boolean isReplay)
      throws InterruptedException, IOException {
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
      imageLoad();
      Init();
      repaint();
      inGame = true;
      this.isReplay = isReplay;
      replay = new Replay(this);
    } else {
      boardReplayArrayList.add(ñolumns);
      boardReplayArrayList.add(rows);
      boardReplayArrayList.add(bombs);

      this.isBot = isBot;
      numOfColumns = ñolumns;
      numOfRows = rows;
      numOfBombs = bombs;
      numOfUncoveredBombs = numOfBombs;
      imageLoad();
      Init();
      if (!isBot) {
        this.addMouseListener(new CellListener());

      } else {
        bot = new Bot(this);
        inGame = true;
      }
    }
  }
  public void imageLoad(){
    for (int i = 0; i < 13; i++) {
      img[i] = (new ImageIcon(i + ".png")).getImage();
    }
  }
  /**
   * When mine banged.
   * 
   * @throws InterruptedException throws InterruptedException
   * @throws IOException throws IOException
   */
  public void finish() throws InterruptedException, IOException {
    this.repaint();
    if (isReplay) {
      JOptionPane.showMessageDialog(new JFrame(), "     Replay has stoped");
    } else if (numOfUncoveredBombs != 0) {
      JOptionPane.showMessageDialog(new JFrame(), "                You lose");
    } else {
      JOptionPane.showMessageDialog(new JFrame(), "                You win");
    }

    if (!isReplay) {
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
    }

    inGame = true;
  }

  /**
   * Initialize board.
   * 
   * @throws InterruptedException throws InterruptedException
   */
  public void Init() throws InterruptedException {
    field = new Cell[numOfRows][numOfColumns];
    for (int i = 0; i < numOfRows; i++) {
      for (int j = 0; j < numOfColumns; j++) {
        field[i][j] = new Cell(j, i);
      }
    }
    repaint();
  }

  public void ResetGame() throws InterruptedException {
    for (int i = 0; i < numOfRows; i++) {
      for (int j = 0; j < numOfColumns; j++) {
        field[i][j].StartNewGame();
      }
    }
    repaint();
    numOfUncoveredBombs = numOfBombs;
    inGame = true;
  }

  /**
   * Paste bomb after player's click
   * 
   * @throws InterruptedException throws InterruptedException
   */
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
    }
    inGame = true;
  }

  /**
   * Function for bot
   * 
   * @throws InterruptedException throws InterruptedException
   * @throws IOException throws IOException
   */
  public void botGaming() throws InterruptedException, IOException {
    Random random = new Random();
    int xPositionOfBotChoose = 0;
    int yPositionOfBotChoose = 0;
    while (!field[0][0].getIsAnyBanged()) {
      xPositionOfBotChoose = Math.abs(random.nextInt() % numOfRows);
      yPositionOfBotChoose = Math.abs(random.nextInt() % numOfColumns);
      if (!(field[yPositionOfBotChoose][xPositionOfBotChoose].getIsOpen())) {
        Thread.sleep(SLEEP_TIME);
        this.actionAnalisys(xPositionOfBotChoose, yPositionOfBotChoose);
      }
    }
  }

  public void replayGaming() throws IOException, InterruptedException {
    InputStream userInputStream = new FileInputStream(userFile);
    int xPositionOfBotChoose = 0;
    int yPositionOfBotChoose = 0;
    int mouseButtonVariant = 0;
    while (!field[0][0].getIsAnyBanged()) {
      xPositionOfBotChoose = userInputStream.read();
      yPositionOfBotChoose = userInputStream.read();
      mouseButtonVariant = userInputStream.read();
      Thread.sleep(SLEEP_TIME);
      if (mouseButtonVariant == 0) {
        this.actionAnalisys(xPositionOfBotChoose, yPositionOfBotChoose);
      }
      if (mouseButtonVariant == 1) {
        this.rightMouseButtonListener(xPositionOfBotChoose, yPositionOfBotChoose);
      }
    }
    userInputStream.close();
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
      findEmptyCells(pressedCol, pressedRow);
      pressedCell.setIsAnyClicked(true);
      repaint();
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
      finish();
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
      repaint();
    }
  }

  class CellListener implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent event) {

      int pressedCol = event.getX() / CELL_SIZE;
      int pressedRow = event.getY() / CELL_SIZE;
      if ((pressedCol >= numOfColumns) || (pressedRow >= numOfRows))
        return;


      Cell pressedCell = field[pressedRow][pressedCol];
      if (pressedCell.getIsAnyBanged() || numOfUncoveredBombs == 0 && numOfWrongFlags == 0)
        return;

      if (event.getButton() == MouseEvent.BUTTON3 && !pressedCell.getIsOpen()) {
        rightMouseButtonListener(pressedCol, pressedRow);
        return;
      }

      try {
        actionAnalisys(pressedCol, pressedRow);
      } catch (InterruptedException | IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    @Override
    public void mouseEntered(MouseEvent event) {

    }

    @Override
    public void mouseExited(MouseEvent arg0) {

    }

    @Override
    public void mousePressed(MouseEvent event) {

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {

    }

  }

  void rightMouseButtonListener(int pressedCol, int pressedRow) {
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
      try {
        finish();
      } catch (InterruptedException | IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return;
    }
    repaint();
    return;
  }

  public void paint(Graphics g) {
    for (int i = 0; i < numOfRows; i++) {
      for (int j = 0; j < numOfColumns; j++) {
        if (field[i][j].getToRepaint()) {
          int xPosition, yPosition;
          xPosition = (j * CELL_SIZE);
          yPosition = (i * CELL_SIZE);
          int imageType;
          imageType = field[i][j].getVariantOfImage();
          g.drawImage(img[imageType], xPosition, yPosition, this);
        }
      }
    }
  }

}


