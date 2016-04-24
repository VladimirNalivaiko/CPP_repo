package minesweeperPackage;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Client extends JPanel implements Runnable {

  private Thread clientThread;
  private static boolean isBot;
  private final int CELL_SIZE = 15;
  private Image[] img = new Image[13];
  private Server board;

  Client(Server board) {
    if(clientThread == null)
      imageLoad();
    this.board = board;
    board.setClient(this);
    clientThread = new Thread(this);
    clientThread.start();
    
    if (!board.isReplay() && !isBot)
      this.addMouseListener(new CellListener());
    repaint();
  }

  public void imageLoad() {
    for (int i = 0; i < 13; i++) {
      img[i] = (new ImageIcon(i + ".png")).getImage();
    }
  }

  public void ResetGame() throws InterruptedException {
    for (int i = 0; i < board.getNumOfRows(); i++) {
      for (int j = 0; j < board.getNumOfColumns(); j++) {
        board.getField()[i][j].StartNewGame();
      }
    }
    repaint();
    board.setNumOfUncoveredBombs(board.getNumOfBombs());
  }

  public void finish() throws InterruptedException, IOException {
    this.repaint();
    if (board.isReplay()) {
      JOptionPane.showMessageDialog(new JFrame(), "     Replay has stoped");
    }
    board.setReplay(false);
    isBot = false;
  }

  class CellListener implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent event) {
      int pressedCol = event.getX() / CELL_SIZE;
      int pressedRow = event.getY() / CELL_SIZE;
      if ((pressedCol >= board.getNumOfColumns()) || (pressedRow >= 
          board.getNumOfRows())) {
        return;
      }
      Cell pressedCell = (board.getField())[pressedRow][pressedCol];
      if (pressedCell.getIsAnyBanged()
          || board.getNumOfUncoveredBombs() == 0 && board.getNumOfWrongFlags()
          == 0) {
        return;
      }

      if (event.getButton() == MouseEvent.BUTTON3 && !pressedCell.getIsOpen()) {
        try {
          board.rightMouseButtonListener(pressedCol, pressedRow);
        } catch (InterruptedException | IOException e) {
          e.printStackTrace();
        }
        repaint();
        return;
      }

      try {
        board.actionAnalisys(pressedCol, pressedRow);
      } catch (InterruptedException | IOException e) {
        e.printStackTrace();
      }
      repaint();
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
    for (int i = 0; i < board.getNumOfRows(); i++) {
      for (int j = 0; j < board.getNumOfColumns(); j++) {
        if ((board.getField())[i][j].getToRepaint()) {
          int xPosition, yPosition;
          xPosition = (j * CELL_SIZE);
          yPosition = (i * CELL_SIZE);
          int imageType;
          imageType = (board.getField())[i][j].getVariantOfImage();
          g.drawImage(img[imageType], xPosition, yPosition, this);
        }
      }
    }
  }

  @Override
  public void run() {
    while (!board.getField()[0][0].getIsAnyBanged() && 
        board.getNumOfUncoveredBombs() != 0){
    }
  }
}