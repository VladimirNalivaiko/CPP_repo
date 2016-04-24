package minesweeperPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class ReplayCharacteristics implements Comparable<ReplayCharacteristics> {
  private static ArrayList<Integer> clickList = new ArrayList<>();
  private String PATH_TO_REPLAY = "./replay/";
  private String replayFileName;
  private File replayFile;
  public int sequenceOfRows;
  public int sequenceOfColumns;
  public int sequenceOfBombs;
  public int sequenceOfClicks;
  public int sequenceOfRightClicks;
  public int sequenceOfLeftClicks;
  private static int sortVariant;

  ReplayCharacteristics(String fileName) throws IOException {
    
    replayFileName = PATH_TO_REPLAY + fileName;
    readReplayCharacteristics();
  }

  ReplayCharacteristics readReplayCharacteristics() throws IOException {
    replayFile = new File(replayFileName);
    InputStream boardInputStream = new FileInputStream(replayFile);
    int buf = 0;
    buf = boardInputStream.read();
    sequenceOfColumns = buf;
    buf = boardInputStream.read();
    sequenceOfRows = buf;
    buf = boardInputStream.read();
    sequenceOfBombs = buf;
    for (int i = 0; i < sequenceOfBombs; i++) {
      buf = boardInputStream.read();
      buf = boardInputStream.read();
    }
    int clicks = 0;
    while (!(boardInputStream.available() == 0)) {
      buf = boardInputStream.read();
      clicks++;
      if (clicks % 3 == 0) {
        clickList.add(buf);
        if (buf == 0) {
          sequenceOfLeftClicks++;
        } else if (buf == 1) {
          sequenceOfRightClicks++;
        }
      }
    }
    boardInputStream.close();
    sequenceOfClicks = clicks / 3;
    return this;
  }

  public static int[] getClickList() {
    int[] array = new int[clickList.size()];
    for(int i = 0; i < clickList.size(); i++){
      array[i] = clickList.get(i);
    }
    return array;
  }

  public int getSequenceOfRightClicks() {
    return sequenceOfRightClicks;
  }

  public int getSequenceOfLeftClicks() {
    return sequenceOfLeftClicks;
  }

  public int getNumOfRows() {
    return sequenceOfRows;
  }

  public void setNumOfRows(int numOfRows) {
    this.sequenceOfRows = numOfRows;
  }

  public int getNumOfColumns() {
    return sequenceOfColumns;
  }

  public void setNumOfColumns(int numOfColumns) {
    this.sequenceOfColumns = numOfColumns;
  }

  public int getNumOfBombs() {
    return sequenceOfBombs;
  }

  public void setNumOfBombs(int numOfBombs) {
    this.sequenceOfBombs = numOfBombs;
  }

  public int getSequenceOfClicks() {
    return sequenceOfClicks;
  }

  public void setSequenceOfClicks(int sequenceOfClicks) {
    this.sequenceOfClicks = sequenceOfClicks;
  }

  @Override
  public int compareTo(ReplayCharacteristics replay) {
    int resultVariant = 0;
    switch (sortVariant) {
      case 0: {
        resultVariant = replayFileName.compareTo(replay.replayFileName);
        break;
      }
      case 1: {

        resultVariant = Integer.compare(sequenceOfColumns, 
            replay.sequenceOfColumns);
        break;
      }
      case 2: {
        resultVariant = Integer.compare(sequenceOfRows, 
            replay.sequenceOfRows);
        break;
      }
      case 3: {
        resultVariant = Integer.compare(sequenceOfBombs, 
            replay.sequenceOfBombs);
        break;
      }
      case 4: {
        resultVariant = Integer.compare(sequenceOfLeftClicks, 
            replay.sequenceOfLeftClicks);
        break;
      }
      case 5: {
        resultVariant = Integer.compare(sequenceOfRightClicks, 
            replay.sequenceOfRightClicks);
        break;
      }
    }
    return resultVariant;
  }

  public static int getSortVariant() {
    return sortVariant;
  }

  public static void setSortVariant(int sortVariant) {
    ReplayCharacteristics.sortVariant = sortVariant;
  }

  public String getReplayFileName() {
    return replayFile.getName();
  }
}
