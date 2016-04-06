import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
  private Bot bot;
  private final int CELL_SIZE = 15;
  private static boolean isBot;
  private static int numOfUncoveredBombs = 10;
  private static int numOfWrongFlags = 0;
  private int numOfRows;
  private int numOfColumns;
  private int numOfBombs;
  private static boolean inGame;
  private int BOT_SLEEP = 500;

  static protected Image[] img = new Image[13];
  static protected Cell[][] field;

  public Board(int ñolumns, int rows, int bombs, boolean isBot) throws InterruptedException {
    this.isBot = isBot;
    numOfColumns = ñolumns;
    numOfRows = rows;
    numOfBombs = bombs;
    numOfUncoveredBombs = numOfBombs;
    img = new Image[13];
    for (int i = 0; i < 13; i++) {
      img[i] = (new ImageIcon(i + ".png")).getImage();
    }
    Init();
    if (!isBot) {
      this.addMouseListener(new CellListener());
      
    }
    else { 
      bot = new Bot(this);
      inGame = true;
    }
  }

  public void finish() throws InterruptedException {
    this.repaint();
    if (numOfUncoveredBombs != 0) {
      JOptionPane.showMessageDialog(new JFrame(), "                You lose");
    } else {
      JOptionPane.showMessageDialog(new JFrame(), "                You win");
    }
    inGame = true;
  }

  public void Init() throws InterruptedException {
    field = new Cell[numOfRows][numOfColumns];
    for (int i = 0; i < numOfRows; i++) {
      for (int j = 0; j < numOfColumns; j++) {
        field[i][j] = new Cell(j, i);
      }
    }    
  }

  public void ResetGame() throws InterruptedException {
    System.out.println("RESETGAME");
    for (int i = 0; i < numOfRows; i++) {
      for (int j = 0; j < numOfColumns; j++) {
        field[i][j].StartNewGame();
      }
    }
    repaint();
    numOfUncoveredBombs = numOfBombs;
    inGame = true;
  }

  public void newGame() throws InterruptedException {
    Random random = new Random();
    int numOfPastedBombs = 0;
    int xPositionOfPasteBomb = 0;
    int yPositionOfPasteBomb = 0;
    while (numOfPastedBombs < numOfBombs) {
      xPositionOfPasteBomb = Math.abs(random.nextInt() % numOfRows);
      yPositionOfPasteBomb = Math.abs(random.nextInt() % numOfColumns);
      if (!(field[xPositionOfPasteBomb][yPositionOfPasteBomb].getIsBomb())
          && !(field[xPositionOfPasteBomb][yPositionOfPasteBomb].getIsOpen())) {
        field[xPositionOfPasteBomb][yPositionOfPasteBomb].setIsBomb(true);
        numOfPastedBombs++;
      }
    }
    inGame = true;
  }

  public void botGaming() throws InterruptedException {
    Random random = new Random();
    int xPositionOfBotChoose = 0;
    int yPositionOfBotChoose = 0;
    while (!field[0][0].getIsAnyBanged()) {
      xPositionOfBotChoose = Math.abs(random.nextInt() % numOfRows);
      yPositionOfBotChoose = Math.abs(random.nextInt() % numOfColumns);
      if (!(field[yPositionOfBotChoose][xPositionOfBotChoose].getIsOpen())) {
        Thread.sleep(BOT_SLEEP);
        this.actionAnalisys(xPositionOfBotChoose, yPositionOfBotChoose);
      }
    }
  }

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

  public void actionAnalisys(int pressedCol, int pressedRow) throws InterruptedException {
    Cell pressedCell = field[pressedRow][pressedCol];
    if (pressedCell.getIsAnyBanged())
      return;
    if (!pressedCell.getIsAnyClicked()) {
      pressedCell.setIsOpen(true);
      newGame();
      findEmptyCells(pressedCol, pressedRow);
      pressedCell.setIsAnyClicked(true);
      repaint();
      return;
    }
    if (pressedCell.getIsBomb() && !(pressedCell.getIsOpen())) {
      pressedCell.setIsOpen(true);
      pressedCell.setIsAnyBanged(true);
      finish();
      return;
    }
    if (!pressedCell.getIsOpen()) {
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
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          return;
        }
        repaint();
        return;
      }
      try {
        actionAnalisys(pressedCol, pressedRow);
      } catch (InterruptedException e) {
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


