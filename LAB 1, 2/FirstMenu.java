import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * First menu of the game.
 * Contains New Game, Settings and Exit buttons.
 * @author Vladimir
 *
 */
public class FirstMenu {
  private static JLabel newGameLabel;
  private static JLabel settingsLabel;
  private static JLabel exitLabel;
  private static BgPanel firstPanel;
  private static JFrame firstFrame;
  private final int rightIndent = 500;
  private final int leftIndent = 25;
  private final int minNumber = 10;
  private final int maxNumber = 100;
  private final Color labelsColor = new Color(130, 130, 130);
  private final Color mouseEnteredColor = new Color(230, 190, 130);
  int[] numOf = new int[3];

  public FirstMenu() {
    firstFrame = new JFrame("MinerSweeper");
    newGameLabel = new JLabel("New Game");
    settingsLabel = new JLabel("Settings");
    exitLabel = new JLabel("Exit");
    firstFrame.setSize(800, 600);
    firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Font font = new Font("Modern No. 20", Font.PLAIN, 30);
    newGameLabel.setFont(font);
    settingsLabel.setFont(font);
    exitLabel.setFont(font);
    newGameLabel.setForeground(labelsColor);
    settingsLabel.setForeground(newGameLabel.getForeground());
    exitLabel.setForeground(newGameLabel.getForeground());
    firstFrame.setLocationRelativeTo(null);

    firstPanel = new BgPanel();
    firstPanel.setLayout(new GridBagLayout());

    firstPanel.add(newGameLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
        new Insets(230, 0, leftIndent, rightIndent), 0, 0));
    firstPanel.add(settingsLabel, new GridBagConstraints(0, 1, 1, 1, 0, 0,
        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
        new Insets(0, 0, leftIndent, rightIndent), 0, 0));
    firstPanel.add(exitLabel, new GridBagConstraints(0, 2, 1, 1, 0, 0,
        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
        new Insets(0, 0, leftIndent, rightIndent), 0, 0));

    firstFrame.add(firstPanel);
    firstFrame.setVisible(true);
    settingsLabel.addMouseListener(new settingsLabelListener());
    exitLabel.addMouseListener(new ExitLabelListener());
    newGameLabel.addMouseListener(new NewGameLabelListener());
  }

  public class ExitLabelListener implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent e) {
      firstFrame.setVisible(false);
      System.exit(0);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      exitLabel.setForeground(mouseEnteredColor);
    }

    @Override
    public void mouseExited(MouseEvent e) {
      exitLabel.setForeground(labelsColor);
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}
  }
  public class settingsLabelListener implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent e) {
      new Settings();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      settingsLabel.setForeground(mouseEnteredColor);
    }

    @Override
    public void mouseExited(MouseEvent e) {
      settingsLabel.setForeground(labelsColor);
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}
  }
  public class NewGameLabelListener implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent e) {
      new Start(numOf);
      firstFrame.dispose();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      newGameLabel.setForeground(mouseEnteredColor);
    }

    @Override
    public void mouseExited(MouseEvent e) {
      newGameLabel.setForeground(labelsColor);
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}
  }

  class BgPanel extends JPanel {
    public void paintComponent(Graphics g) {
      Image im = null;
      try {
        im = ImageIO.read(new File("BOMB.jpg"));
      } catch (IOException e) {
      }
      g.drawImage(im, 0, 0, null);
    }
  }
  class Settings {
    private boolean flag;
    private JFrame settingsFrame;
    private JLabel rowsLabel;
    private JLabel columnsLabel;
    private JLabel bombsLabel;
    private JButton okButton;
    private JComboBox<Integer> rows;
    private JComboBox<Integer> columns;
    private JComboBox<Integer> bombs;

    Settings() {
      okButton = new JButton("OK");
      rows = new JComboBox<Integer>();
      columns = new JComboBox<Integer>();
      bombs = new JComboBox<Integer>();
      rowsLabel = new JLabel("Choose number of rows");
      columnsLabel = new JLabel("Choose number of columns");
      bombsLabel = new JLabel("Choose number of bombs");
      for (int i = minNumber; i < maxNumber; i++) {
        rows.addItem(i);
        columns.addItem(i);
        bombs.addItem(i);
      }
      settingsFrame = new JFrame("Settings");
      settingsFrame.setLayout(new GridLayout(4, 2));
      settingsFrame.add(rowsLabel);
      settingsFrame.add(rows);
      settingsFrame.add(columnsLabel);
      settingsFrame.add(columns);
      settingsFrame.add(bombsLabel);
      settingsFrame.add(bombs);
      settingsFrame.add(okButton, new BorderLayout().EAST);
      settingsFrame.pack();
      settingsFrame.setAlwaysOnTop(true);
      settingsFrame.setLocationRelativeTo(null);
      settingsFrame.setVisible(true);
      okButton.addActionListener(new ButtonListener());
    }
    class ButtonListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        numOf[0] = (int) columns.getSelectedItem();
        numOf[1] = (int) rows.getSelectedItem();
        numOf[2] = (int) bombs.getSelectedItem();
        settingsFrame.dispose();
      }
    }
  }
}
