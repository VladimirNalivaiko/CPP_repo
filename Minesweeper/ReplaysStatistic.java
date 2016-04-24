package minesweeperPackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class ReplaysStatistic {
  private File REPLAY_FOLDER = new File("./replay/");
  File[] replayFolderFiles;
  private int NUM_OF_LABELS = 6;
  private final int FRAME_WIDTH = 650;
  private final int FRAME_HIGHT = 230;
  private int[] replayStatistic;
  Font FONT = new Font("Modern No. 20", Font.CENTER_BASELINE, 30);
  private final Color LABELS_COLOR = new Color(130, 130, 130);
  ArrayList<ReplayCharacteristics> replaysStaticticList;
  JFrame replaysStatisticFrame;
  JLabel[] statisticLabel;
  String[] label_text = {"The number of genereted replays : ",
      "The number of genereted columns : ",
      "The number of genereted rows : ",
      "The number of genereted bombs : ",
      "The number of genereted left clicks: ",
      "The number of genereted right clicks: "};
  
  ReplaysStatistic() throws IOException{
    replayStatistic = new int[NUM_OF_LABELS - 1];
    replaysStatisticFrame = new JFrame("Statistic");
    replaysStatisticFrame.setLayout(new GridLayout(NUM_OF_LABELS, 1));
    replaysStaticticList = new ArrayList<>();
    getStatisticFromFile();
    replaysStatisticFrame.setSize(FRAME_WIDTH, FRAME_HIGHT);
    replaysStatisticFrame.setLocationRelativeTo(null);
    replaysStatisticFrame.setResizable(false);
    replaysStatisticFrame.setVisible(true);    
  }
  
  public void createLabels(){
    statisticLabel = new JLabel[NUM_OF_LABELS];
    statisticLabel[0] = new JLabel(label_text[0] +
        Integer.toString(replayFolderFiles.length));
    for(int i = 1; i < NUM_OF_LABELS; i++){
      statisticLabel[i] = new JLabel(label_text[i] + replayStatistic[i - 1]);
      statisticLabel[i].setFont(FONT);
      statisticLabel[i].setForeground(LABELS_COLOR);
      replaysStatisticFrame.add(statisticLabel[i]);
    }
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
