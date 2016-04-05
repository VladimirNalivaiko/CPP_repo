import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Board extends JPanel {
  private final int CELL_SIZE = 15;
  private int NUM_OF_BOMBS = 10;
  private static int NUM_OF_UNCOVED_BOMBS = 10;
  private static int NUM_OF_WRONG_FLAGS = 0;
  private int NUM_OF_ROWS = 10;
  private int NUM_OF_COLUMNS = 10;
  private static boolean inGame;

  static Image[] img = new Image[13];
  static private Cell[][] field;

  public Board(int numOfColumns, int numOfRows, int numOfBombs) {
    NUM_OF_COLUMNS = numOfColumns;
    NUM_OF_ROWS = numOfRows;
    NUM_OF_BOMBS = numOfBombs;
    NUM_OF_UNCOVED_BOMBS = NUM_OF_BOMBS;
    img = new Image[13];
    for (int i = 0; i < 13; i++) {
      img[i] = (new ImageIcon(i + ".png")).getImage();
    }
    this.addMouseListener(new CellListener());
    Init();
    this.setBackground(Color.BLUE);
  }

  public void finish() {
    this.repaint();
    if (NUM_OF_UNCOVED_BOMBS != 0) {
      JOptionPane.showMessageDialog(new JFrame(), "     You lose");
    } else {
      JOptionPane.showMessageDialog(new JFrame(), "     You win");
    }
    inGame = false;
  }

  public void Init() {
    field = new Cell[NUM_OF_ROWS][NUM_OF_COLUMNS];
    for (int i = 0; i < NUM_OF_ROWS; i++) {
      for (int j = 0; j < NUM_OF_COLUMNS; j++) {
        field[i][j] = new Cell(j, i);
      }
    }
  }

  public void ResetGame() {
    System.out.println("RESETGAME");
    for (int i = 0; i < NUM_OF_ROWS; i++) {
      for (int j = 0; j < NUM_OF_COLUMNS; j++) {
        field[i][j].StartNewGame();
      }
      repaint();
    }
    NUM_OF_UNCOVED_BOMBS = NUM_OF_BOMBS;
    inGame = true;
  }

  public void newGame() {
    Random random = new Random();
    int numOfPastedBombs = 0;
    int xPositionOfPasteBomb = 0;
    int yPositionOfPasteBomb = 0;
    while (numOfPastedBombs < NUM_OF_BOMBS) {
      xPositionOfPasteBomb = Math.abs(random.nextInt() % NUM_OF_ROWS);
      yPositionOfPasteBomb = Math.abs(random.nextInt() % NUM_OF_COLUMNS);
      if (!(field[xPositionOfPasteBomb][yPositionOfPasteBomb].getIsBomb())
          && !(field[xPositionOfPasteBomb][yPositionOfPasteBomb].getIsOpen())) {
        field[xPositionOfPasteBomb][yPositionOfPasteBomb].setIsBomb(true);
        numOfPastedBombs++;
      }
    }

    inGame = true;
  }

  public void findEmptyCells(int x, int y) {
    if (!field[y][x].getIsBomb()) {
      if (findNearBombs(x, y) != 0) {
        field[y][x].setToRepaint(true);
        field[y][x].setIsOpen(true);
        return;
      }
      if (y != 0) { // Этим блоком if мы ищем своболные клетки сверху
        if (findNearBombs(x, y - 1) == 0 && !field[y - 1][x].getIsOpen()) {
          field[y - 1][x].setIsOpen(true);
          field[y - 1][x].setToRepaint(true);
          findEmptyCells(x, y - 1);
        } else {
          field[y - 1][x].setIsOpen(true);
          field[y - 1][x].setToRepaint(true);
        }
      }
      if (x != 0) { // Этим блоком if мы ищем своболные клетки слева
        if (findNearBombs(x - 1, y) == 0 && !field[y][x - 1].getIsOpen()) {
          field[y][x - 1].setToRepaint(true);
          field[y][x - 1].setIsOpen(true);
          findEmptyCells(x - 1, y);
        } else {
          field[y][x - 1].setIsOpen(true);
          field[y][x - 1].setToRepaint(true);
        }
      }
      if (y != NUM_OF_ROWS - 1) { // Этим блоком if мы ищем своболные клетки снизу
        if (findNearBombs(x, y + 1) == 0 && !field[y + 1][x].getIsOpen()) {
          field[y + 1][x].setIsOpen(true);
          field[y + 1][x].setToRepaint(true);
          findEmptyCells(x, y + 1);
        } else {
          field[y + 1][x].setIsOpen(true);
          field[y + 1][x].setToRepaint(true);
        }
      }
      if (x != NUM_OF_COLUMNS - 1) { // Этим блоком if мы ищем своболные клетки справа
        if (findNearBombs(x + 1, y) == 0 && !field[y][x + 1].getIsOpen()) {
          field[y][x + 1].setIsOpen(true);
          field[y][x + 1].setToRepaint(true);
          findEmptyCells(x + 1, y);
        } else {
          field[y][x + 1].setIsOpen(true);
          field[y][x + 1].setToRepaint(true);
        }
      }
      if (y != 0 && x != 0) { // Этим блоком if мы ищем своболные клетки сверху слева
        if (findNearBombs(x - 1, y - 1) == 0 && !field[y - 1][x - 1].getIsOpen()) {
          field[y - 1][x - 1].setIsOpen(true);
          field[y - 1][x - 1].setToRepaint(true);
          findEmptyCells(x - 1, y - 1);
        } else {
          field[y - 1][x - 1].setIsOpen(true);
          field[y - 1][x - 1].setToRepaint(true);
        }
      }
      if (y != NUM_OF_ROWS - 1 && x != 0) { // Этим блоком if мы ищем своболные клетки снизу слева
        if (findNearBombs(x - 1, y + 1) == 0 && !field[y + 1][x - 1].getIsOpen()) {
          field[y + 1][x - 1].setIsOpen(true);
          field[y + 1][x - 1].setToRepaint(true);
          findEmptyCells(x - 1, y + 1);
        } else {
          field[y + 1][x - 1].setIsOpen(true);
          field[y + 1][x - 1].setToRepaint(true);
        }
      }
      if (y != NUM_OF_ROWS - 1 && x != NUM_OF_COLUMNS - 1) { // Этим блоком if мы ищем своболные
                                                             // клетки снизу справа
        if (findNearBombs(x + 1, y + 1) == 0 && !field[y + 1][x + 1].getIsOpen()) {
          field[y + 1][x + 1].setIsOpen(true);
          field[y + 1][x + 1].setToRepaint(true);
          findEmptyCells(x + 1, y + 1);
        } else {
          field[y + 1][x + 1].setIsOpen(true);
          field[y + 1][x + 1].setToRepaint(true);
        }
      }
      if (y != 0 && x != NUM_OF_COLUMNS - 1) { // Этим блоком if мы ищем своболные клетки сверху
                                               // справа
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
    if (y != 0) { // сверху
      if (field[y - 1][x].getIsBomb()) {
        numOfNearBombs++;
      }
    }
    if (y != 0 && x != 0) { // слева сверху
      if (field[y - 1][x - 1].getIsBomb()) {
        numOfNearBombs++;
      }
    }
    if (x != 0) { // слева
      if (field[y][x - 1].getIsBomb()) {
        numOfNearBombs++;
      }
    }
    if (x != 0 && y != NUM_OF_ROWS - 1) { // слева снизу
      if (field[y + 1][x - 1].getIsBomb()) {
        numOfNearBombs++;
      }
    }
    if (y != NUM_OF_ROWS - 1) { // снизу
      if (field[y + 1][x].getIsBomb()) {
        numOfNearBombs++;
      }
    }
    if (x != NUM_OF_COLUMNS - 1 && y != NUM_OF_ROWS - 1) { // справа снизу
      if (field[y + 1][x + 1].getIsBomb()) {
        numOfNearBombs++;
      }
    }
    if (x != NUM_OF_COLUMNS - 1) { // справа
      if (field[y][x + 1].getIsBomb()) {
        numOfNearBombs++;
      }
    }
    if (x != NUM_OF_COLUMNS - 1 && y != 0) { // справа сверху
      if (field[y - 1][x + 1].getIsBomb()) {
        numOfNearBombs++;
      }
    }
    field[y][x].setNumOfNeighborBombs(numOfNearBombs);
    return numOfNearBombs;
  }

  class CellListener implements MouseListener {


    @Override
    public void mouseClicked(MouseEvent event) {

      int pressedCol = event.getX() / CELL_SIZE;
      int pressedRow = event.getY() / CELL_SIZE;

      if ((pressedCol >= NUM_OF_COLUMNS) || (pressedRow >= NUM_OF_ROWS))
        return;

      Cell pressedCell = field[pressedRow][pressedCol];
      if (pressedCell.getIsAnyBanged() || NUM_OF_UNCOVED_BOMBS == 0 && NUM_OF_WRONG_FLAGS == 0)
        return;
      if (event.getButton() == MouseEvent.BUTTON3 && !pressedCell.getIsOpen()) {
        if (!pressedCell.getIsSooposedToBeBomb()) {
          if (pressedCell.getIsBomb())
            NUM_OF_UNCOVED_BOMBS--;
          else
            NUM_OF_WRONG_FLAGS++;
          pressedCell.setIsSooposedToBeBomb(true);
        } else {
          pressedCell.setIsSooposedToBeBomb(false);
          if (pressedCell.getIsBomb())
            NUM_OF_UNCOVED_BOMBS++;
          else
            NUM_OF_WRONG_FLAGS--;
        }
        if (NUM_OF_UNCOVED_BOMBS == 0 && NUM_OF_WRONG_FLAGS == 0) {
          finish();
          return;
        }
        repaint();
        return;
      }
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
    for (int i = 0; i < NUM_OF_ROWS; i++) {
      for (int j = 0; j < NUM_OF_COLUMNS; j++) {
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


