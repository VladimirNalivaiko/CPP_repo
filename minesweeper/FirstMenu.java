package minesweeperPackage;

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

import minesweeperPackage.ReplayMenu.BackToMenuLabelListener;
import minesweeperPackage.ReplayMenu.ReplayTabelLabelListener;
import minesweeperPackage.ReplayMenu.StatisticsLabelListener;

/**
 * First menu of the game. Contains New Game, Settings and Exit buttons.
 * 
 * @author Vladimir
 *
 */
public class FirstMenu {
  private static JLabel newGameLabel;
  private static JLabel botLabel;
  private static JLabel replayLabel;
  private static JLabel settingsLabel;
  private static JLabel exitLabel;

  protected static BgPanel firstPanel;
  protected static JFrame firstFrame;
  private final int RIGHT_INDENT = 500;
  private final int LEFT_INDENT = 25;
  private final int MIN_NUMBER = 10;
  private final int MAX_NUMBER = 100;
  private final int FRAME_WIDTH = 800;
  private final int FRAME_HIGHT = 600;
  Font FONT = new Font("Modern No. 20", Font.PLAIN, 30);
  private final Color LABELS_COLOR = new Color(130, 130, 130);
  private final Color MOUSE_ENTERED_COLOR = new Color(230, 190, 130);
  int[] numOf = new int[3];

  public FirstMenu() {
    firstFrame = new JFrame("MinerSweeper");
    newGameLabel = new JLabel("New Game");
    replayLabel = new JLabel("Replay");
    settingsLabel = new JLabel("Settings");
    botLabel = new JLabel("Bot");
    exitLabel = new JLabel("Exit");
    firstFrame.setSize(FRAME_WIDTH, FRAME_HIGHT);
    firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    newGameLabel.setFont(FONT);
    replayLabel.setFont(FONT);
    settingsLabel.setFont(FONT);
    exitLabel.setFont(FONT);
    botLabel.setFont(FONT);
    newGameLabel.setForeground(LABELS_COLOR);
    replayLabel.setForeground(newGameLabel.getForeground());
    settingsLabel.setForeground(newGameLabel.getForeground());
    botLabel.setForeground(newGameLabel.getForeground());
    exitLabel.setForeground(newGameLabel.getForeground());
    firstFrame.setLocationRelativeTo(null);

    firstPanel = new BgPanel();
    firstPanel.setLayout(new GridBagLayout());

    firstPanel.add(newGameLabel, 
        new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NORTH,
        GridBagConstraints.HORIZONTAL, 
        new Insets(230, 0, LEFT_INDENT, RIGHT_INDENT), 0, 0));
    firstPanel.add(replayLabel, 
        new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL, 
        new Insets(0, 0, LEFT_INDENT, RIGHT_INDENT), 0, 0));
    firstPanel.add(settingsLabel,
        new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.CENTER,
            GridBagConstraints.HORIZONTAL,
            new Insets(0, 0, LEFT_INDENT, RIGHT_INDENT), 0, 0));
    firstPanel.add(botLabel, 
        new GridBagConstraints(0, 3, 1, 1, 0, 0, GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL, 
        new Insets(0, 0, LEFT_INDENT, RIGHT_INDENT), 0, 0));
    firstPanel.add(exitLabel, 
        new GridBagConstraints(0, 4, 1, 1, 0, 0, GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL, 
        new Insets(0, 0, LEFT_INDENT, RIGHT_INDENT), 0, 0));

    firstFrame.add(firstPanel);
    firstFrame.setVisible(true);
    replayLabel.addMouseListener(new ReplayLabelListener());
    settingsLabel.addMouseListener(new SettingsLabelListener());
    botLabel.addMouseListener(new BotLabelListener());
    exitLabel.addMouseListener(new ExitLabelListener());
    newGameLabel.addMouseListener(new NewGameLabelListener());
  }
  
  public void showReplayMenu(){
    ReplayMenu replayMenu = new ReplayMenu(firstPanel, this, numOf);
  }
  
  public void setVisibleLabels(boolean visible){
    newGameLabel.setVisible(visible);
    replayLabel.setVisible(visible);
    settingsLabel.setVisible(visible);
    botLabel.setVisible(visible);
    exitLabel.setVisible(visible);
  }

  public JFrame getFrame() {
    return firstFrame;
  }

  public class ExitLabelListener implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent e) {
      firstFrame.setVisible(false);
      System.exit(0);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      exitLabel.setForeground(MOUSE_ENTERED_COLOR);
    }

    @Override
    public void mouseExited(MouseEvent e) {
      exitLabel.setForeground(LABELS_COLOR);
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}
  }
  public class SettingsLabelListener implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent e) {
      new Settings();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      settingsLabel.setForeground(MOUSE_ENTERED_COLOR);
    }

    @Override
    public void mouseExited(MouseEvent e) {
      settingsLabel.setForeground(LABELS_COLOR);
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}
  }
  public class ReplayLabelListener implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent e) {
      setVisibleLabels(false);
      showReplayMenu();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      replayLabel.setForeground(MOUSE_ENTERED_COLOR);
    }

    @Override
    public void mouseExited(MouseEvent e) {
      replayLabel.setForeground(LABELS_COLOR);
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}
  }
  public class BotLabelListener implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent e) {
      try {
        firstFrame.dispose();
        new Start(numOf, true, false);
      } catch (InterruptedException | IOException e1) {
        e1.printStackTrace();
      }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      botLabel.setForeground(MOUSE_ENTERED_COLOR);
    }

    @Override
    public void mouseExited(MouseEvent e) {
      botLabel.setForeground(LABELS_COLOR);
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}
  }
  public class NewGameLabelListener implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent e) {
      try {
        firstFrame.dispose();
        new Start(numOf, false, false);
      } catch (InterruptedException | IOException e1) {
        e1.printStackTrace();
      }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      newGameLabel.setForeground(MOUSE_ENTERED_COLOR);
    }

    @Override
    public void mouseExited(MouseEvent e) {
      newGameLabel.setForeground(LABELS_COLOR);
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
      columnsLabel = new JLabel("Choose number of columns");
      rowsLabel = new JLabel("Choose number of rows");
      bombsLabel = new JLabel("Choose number of bombs");
      for (int i = MIN_NUMBER; i < MAX_NUMBER; i++) {
        columns.addItem(i);
        rows.addItem(i);
        bombs.addItem(i);
      }
      settingsFrame = new JFrame("Settings");
      settingsFrame.setLayout(new GridLayout(4, 2));
      settingsFrame.add(columnsLabel);
      settingsFrame.add(columns);
      settingsFrame.add(rowsLabel);
      settingsFrame.add(rows);
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
        System.out.println("numOf[0] = " + numOf[0]);
        numOf[1] = (int) rows.getSelectedItem();
        numOf[2] = (int) bombs.getSelectedItem();
        System.out.println("Before settingsFrame.dispose()");
        settingsFrame.dispose();
      }
    }
  }
}
