import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
/**
 * Initializes the start of the game.
 * @author Vladimir
 * 
 */
public class Start {
  private static Board bd;
  private final int CELL_SIZE = 15;
  private static JFrame frame;
  private int NUM_OF_BOMBS = 10;
  private int NUM_OF_ROWS = 10;
  private int NUM_OF_COLUMNS = 10;
  private final int rightIndent = 6;
  private final int downIndent = 52;
  private final int minSize = 10;
  Start(int numOf[]) {
    if (numOf[0] > minSize){
      NUM_OF_COLUMNS = numOf[0];
    }
    if (numOf[1] > minSize){
      NUM_OF_ROWS = numOf[1];
    }
    if (numOf[2] > minSize) {
      NUM_OF_BOMBS = numOf[2];
    }
    frame = new JFrame("Miner");
    JMenuBar menu = new JMenuBar();
    JMenu gameMenu = new JMenu("Game");
    JMenuItem newGameMenuItem = new JMenuItem("New Game");
    JMenuItem backToMenuItem = new JMenuItem("Back to menu");
    JMenuItem exitMenuItem = new JMenuItem("Exit");
    bd = new Board(NUM_OF_ROWS, NUM_OF_COLUMNS, NUM_OF_BOMBS);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(NUM_OF_ROWS * CELL_SIZE + rightIndent,
        NUM_OF_COLUMNS * CELL_SIZE + downIndent);
    frame.setLocationRelativeTo(null);

    newGameMenuItem.addActionListener(new NewGameMenuItemListener());
    backToMenuItem.addActionListener(new BackToMenuItemListener());
    exitMenuItem.addActionListener(new ExitMenuItemListener());

    gameMenu.add(newGameMenuItem);
    gameMenu.add(backToMenuItem);
    gameMenu.add(exitMenuItem);
    menu.add(gameMenu);
    frame.setJMenuBar(menu);
    frame.add(bd);
    frame.setVisible(true);
    frame.setResizable(false);
  }

  static class NewGameMenuItemListener implements ActionListener {
    public void actionPerformed(ActionEvent arg0) {
      bd.ResetGame();
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
