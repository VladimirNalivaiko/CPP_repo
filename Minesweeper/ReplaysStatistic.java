package minesweeperPackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ReplaysStatistic {
  private final File REPLAY_FOLDER = new File("./replay/");
  File[] replayFolderFiles;
  private final int NUM_OF_LABELS = 6;
  private final int FRAME_WIDTH = 650;
  private final int FRAME_HIGHT = 230;
  private final int RIGHT_INDENT = 10;
  private final int LEFT_INDENT = 10;
  private final int UP_INDENT = 10;
  private final int DOWN_INDENT = 100;
  private final Color BACKGROUND_COLOR = new Color(210, 150, 100);
  private int[] replayStatistic;
  private final Font FONT = new Font("Modern No. 20", Font.CENTER_BASELINE, 30);
  private final Color LABELS_COLOR = new Color(60, 60, 60);
  private ArrayList<ReplayCharacteristics> replaysStaticticList;
  private JFrame replaysStatisticFrame;
  private JPanel replaysStatisticPanel;
  private JLabel[] statisticLabel;
  private String[] label_text = {
      "The number of genereted replays : ",
      "The number of genereted columns : ",
      "The number of genereted rows : ",
      "The number of genereted bombs : ",
      "The number of genereted left clicks: ",
      "The number of genereted right clicks: "};
  
  ReplaysStatistic() throws IOException{
    replaysStatisticPanel = new JPanel();
    replayStatistic = new int[NUM_OF_LABELS - 1];
    replaysStatisticFrame = new JFrame("Statistic");
    replaysStatisticPanel.setBackground(BACKGROUND_COLOR);
    replaysStatisticPanel.setLayout(new GridBagLayout());
    replaysStaticticList = new ArrayList<>();
    getStatisticFromFile();
    replaysStatisticFrame.setSize(FRAME_WIDTH, FRAME_HIGHT);
    replaysStatisticFrame.setLocationRelativeTo(null);
    replaysStatisticFrame.setResizable(false);
    replaysStatisticFrame.setForeground(BACKGROUND_COLOR);
    replaysStatisticFrame.add(replaysStatisticPanel);
    replaysStatisticFrame.setVisible(true);    
  }
  
  public void setLabelStyle(JLabel label){
    label.setFont(FONT);
    label.setForeground(LABELS_COLOR);
  }
  
  public void createLabels(){
    statisticLabel = new JLabel[NUM_OF_LABELS];
    statisticLabel[0] = new JLabel(label_text[0] +
        Integer.toString(replayFolderFiles.length));
    setLabelStyle( statisticLabel[0]);
    for(int i = 1; i < NUM_OF_LABELS; i++){
      statisticLabel[i] = new JLabel(label_text[i] + replayStatistic[i - 1]);
      setLabelStyle( statisticLabel[i]);
    }
    replaysStatisticPanel.add(statisticLabel[0], 
        new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL, 
        new Insets(0, 0, 0, 0), 0, 0));
    replaysStatisticPanel.add(statisticLabel[1], 
        new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL, 
        new Insets(0, 0, 0, 0), 0, 0));
    replaysStatisticPanel.add(statisticLabel[2], 
        new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL, 
        new Insets(0, 0, 0, 0), 0, 0));
    replaysStatisticPanel.add(statisticLabel[3], 
        new GridBagConstraints(0, 3, 1, 1, 0, 0, GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL, 
        new Insets(0, 0, 0, 0), 0, 0));
    replaysStatisticPanel.add(statisticLabel[4], 
        new GridBagConstraints(0, 4, 1, 1, 0, 0, GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL, 
        new Insets(0, 0, 0, 0), 0, 0));
    replaysStatisticPanel.add(statisticLabel[5], 
        new GridBagConstraints(0, 5, 1, 1, 0, 0, GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL, 
        new Insets(0, 0, 0, 0), 0, 0));
    
  }  
  public void getStatisticFromFile() throws IOException{
    replayFolderFiles = REPLAY_FOLDER.listFiles();
    for (int i = 0; i < replayFolderFiles.length; i++) {
      replaysStaticticList.add(new ReplayCharacteristics(
          replayFolderFiles[i].getName()));
      replayStatistic[0] += replaysStaticticList.get(i).getNumOfColumns();
      replayStatistic[1] += replaysStaticticList.get(i).getNumOfRows();
      replayStatistic[2] += replaysStaticticList.get(i).getNumOfBombs();
      replayStatistic[3] += replaysStaticticList.get(i).
          getSequenceOfLeftClicks();
      replayStatistic[4] += replaysStaticticList.get(i).
          getSequenceOfRightClicks();
    }
    createLabels();
  }
}
