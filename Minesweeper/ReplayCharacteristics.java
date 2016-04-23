package minesweeperPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ReplayCharacteristics implements Comparable<ReplayCharacteristics> {
  private String PATH_TO_REPLAY = "./replay/";
  private String replayFileName;
  private File replayFile;
  public int sequenceOfRows;
  public int sequenceOfColumns;
  public int sequenceOfBombs;
  public int sequenceOfClicks;
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
    }
    boardInputStream.close();
    sequenceOfClicks = clicks / 3;
    return this;
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
        resultVariant = Integer.compare(sequenceOfClicks, 
            replay.sequenceOfClicks);
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
