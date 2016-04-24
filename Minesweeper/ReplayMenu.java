package minesweeperPackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;

import minesweeperPackage.FirstMenu.BgPanel;

public class ReplayMenu{
  protected static BgPanel firstPanel;
  protected static FirstMenu firstMenu;
  private static JLabel replayTabelLabel;
  private static JLabel backToGameMenuLabel;
  private static JLabel statisticsLabel;
  private final int RIGHT_INDENT = 500;
  private final int LEFT_INDENT = 25;
  private final Color LABELS_COLOR = new Color(130, 130, 130);
  private final Color MOUSE_ENTERED_COLOR = new Color(230, 190, 130);
  int[] numOf = new int[3];
  ReplayMenu(BgPanel firstPanel, FirstMenu firstMenu, int[] numOf) {
    this.firstPanel = firstPanel;
    this.firstMenu = firstMenu;
    this.numOf = numOf;
    replayTabelLabel = new JLabel("Replay Table");
    statisticsLabel = new JLabel("Statistics");
    backToGameMenuLabel = new JLabel("Back To Menu");
    Font font = new Font("Modern No. 20", Font.PLAIN, 30);
    replayTabelLabel.setFont(font);
    statisticsLabel.setFont(font);
    backToGameMenuLabel.setFont(font);
    replayTabelLabel.setForeground(LABELS_COLOR);
    statisticsLabel.setForeground(replayTabelLabel.getForeground());
    backToGameMenuLabel.setForeground(replayTabelLabel.getForeground());
    firstPanel.add(replayTabelLabel,
        new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NORTH,
            GridBagConstraints.HORIZONTAL, 
            new Insets(230, 0, LEFT_INDENT, RIGHT_INDENT), 0, 0));
    firstPanel.add(statisticsLabel,
        new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER,
            GridBagConstraints.HORIZONTAL, 
            new Insets(0, 0, LEFT_INDENT, RIGHT_INDENT), 0, 0));
    firstPanel.add(backToGameMenuLabel,
        new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.CENTER,
            GridBagConstraints.HORIZONTAL, 
            new Insets(0, 0, LEFT_INDENT, RIGHT_INDENT), 0, 0));
    replayTabelLabel.addMouseListener(new ReplayTabelLabelListener());
    statisticsLabel.addMouseListener(new StatisticsLabelListener());
    backToGameMenuLabel.addMouseListener(new BackToMenuLabelListener());    
  }
  
  public void setVisibleReplayLabels(boolean visible){
    replayTabelLabel.setVisible(visible);
    statisticsLabel.setVisible(visible);
    backToGameMenuLabel.setVisible(visible);
  }
  
  public class ReplayTabelLabelListener implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent e) {
      try {
        firstMenu.getFrame().dispose();
        new Start(numOf, false, true);
      } catch (InterruptedException | IOException e1) {
        e1.printStackTrace();
      }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      replayTabelLabel.setForeground(MOUSE_ENTERED_COLOR);
    }

    @Override
    public void mouseExited(MouseEvent e) {
      replayTabelLabel.setForeground(LABELS_COLOR);
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}
  }
  public class StatisticsLabelListener implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent e) {
      try {
        new ReplaysStatistic();
      } catch (IOException e1) {
        e1.printStackTrace();
      }      
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      statisticsLabel.setForeground(MOUSE_ENTERED_COLOR);
    }

    @Override
    public void mouseExited(MouseEvent e) {
      statisticsLabel.setForeground(LABELS_COLOR);
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}
  }
  public class BackToMenuLabelListener implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent e) {
      setVisibleReplayLabels(false);
      firstMenu.setVisibleLabels(true);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      backToGameMenuLabel.setForeground(MOUSE_ENTERED_COLOR);
    }

    @Override
    public void mouseExited(MouseEvent e) {
      backToGameMenuLabel.setForeground(LABELS_COLOR);
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}
  }
}
