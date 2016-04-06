import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Initializes the start of the game.
 * 
 * @author Vladimir
 * 
 */
public class Start {
  private static boolean isBot;
  private static Board bd;
  private static JFrame frame;
  private int numOfBombs = 10;
  private int numOfRows = 10;
  private int numOfColumns = 10;
  private final int CELL_SIZE = 15;
  private final int RIGHT_INDENT = 6;
  private final int LEFT_INDENT = 52;
  private final int MIN_SIZE = 10;

  Start(int numOf[], boolean bot) throws InterruptedException {
    isBot = bot;
    if (numOf[0] > MIN_SIZE) {
      numOfColumns = numOf[0];
    }
    if (numOf[1] > MIN_SIZE) {
      numOfRows = numOf[1];
    }
    if (numOf[2] > MIN_SIZE) {
      numOfBombs = numOf[2];
    }
    frame = new JFrame("Miner");
    JMenuBar menu = new JMenuBar();
    JMenu gameMenu = new JMenu("Game");
    JMenuItem newGameMenuItem = new JMenuItem("New Game");
    JMenuItem backToMenuItem = new JMenuItem("Back to menu");
    JMenuItem exitMenuItem = new JMenuItem("Exit");

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(numOfColumns * CELL_SIZE + RIGHT_INDENT, 
        numOfRows * CELL_SIZE + LEFT_INDENT);
    frame.setLocationRelativeTo(null);

    newGameMenuItem.addActionListener(new NewGameMenuItemListener());
    backToMenuItem.addActionListener(new BackToMenuItemListener());
    exitMenuItem.addActionListener(new ExitMenuItemListener());

    gameMenu.add(newGameMenuItem);
    gameMenu.add(backToMenuItem);
    gameMenu.add(exitMenuItem);
    menu.add(gameMenu);
    frame.setJMenuBar(menu);
    frame.setVisible(true);
    frame.setResizable(false);
    frame.repaint();
    bd = new Board(numOfColumns, numOfRows, numOfBombs, isBot);
    frame.add(bd);
  }

  static class NewGameMenuItemListener implements ActionListener {
    public void actionPerformed(ActionEvent arg0) {
      if (!isBot) {
        try {
          bd.ResetGame();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      } 
    }
  }

  static class BackToMenuItemListener implements ActionListener {
    public void actionPerformed(ActionEvent arg0) {
      new FirstMenu();
      frame.dispose();
    }
  }

  static class ExitMenuItemListener implements ActionListener {
    public void actionPerformed(ActionEvent arg0) {
      frame.setVisible(false);
      System.exit(0);
    }
  }
}
