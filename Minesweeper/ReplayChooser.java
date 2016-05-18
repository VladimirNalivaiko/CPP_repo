package minesweeperPackage;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ReplayChooser {
  private final String[] HEADERS = {
      "Replay's Name",
      "Num Of Columns",
      "Num Of Rows",
      "Num Of Bombs",
      "Num Of  Left Clicks",
      "Num Of Right Clicks"
      };
  private final File REPLAY_FOLDER = new File("./replay/");
  private final int NUM_OF_COLUMNS = 6;
  private final int CELL_HIEGHT = 16;
  private final int FRAME_WIDTH = 850;
  private final int FRAME_HIGHT = 550;
  private Object[][] replayContent;
  private int numOfReplay;
  private ArrayList<ReplayCharacteristics> replayCharacteristicsList;
  private String choosedFile;
  private File[] replayFolderFiles;
  private JFrame replayFrame;
  private Start start;
  private JTable table;

  ReplayChooser(Start start) throws IOException {
    replayCharacteristicsList = new ArrayList<>();
    this.start = start;
    choosedFile = new String();
    replayFrame = new JFrame("Choose Replay");
    replayFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    getFiles();
    replayFrame.setAlwaysOnTop(true);
    addContentToTable();
    replayFrame.setSize(FRAME_WIDTH, FRAME_HIGHT);
    replayFrame.setLocationRelativeTo(null);
    replayFrame.setVisible(true);
  }

  public void sortArray() {
    long sortStartTime = 0;
    long sortEndTime = 0;
    long sortTraceTime = 0;
    ScalaFunctions scalaObject = new ScalaFunctions();
    Sort javaSort = new Sort();
    int[] array = new int[replayCharacteristicsList.size()];
    for (int i = 0; i < replayCharacteristicsList.size(); i++) {
      array[i] = replayCharacteristicsList.get(i).getSequenceOfLeftClicks();
    }

    sortStartTime = System.nanoTime();
    scalaObject.sort(array, 0, array.length - 1);
    sortEndTime = System.nanoTime();
    sortTraceTime = sortEndTime - sortStartTime;
    System.out.println("Time of sorting in Scala: " + sortTraceTime);

    for (int i = 0; i < replayCharacteristicsList.size(); i++) {
      array[i] = replayCharacteristicsList.get(i).getSequenceOfLeftClicks();
    }

    sortStartTime = System.nanoTime();
    javaSort.quickSort(array, 0, array.length - 1);
    sortEndTime = System.nanoTime();
    sortTraceTime = sortEndTime - sortStartTime;
    System.out.println("Time of sorting in Java : " + sortTraceTime);
    String[] replayString = new String[replayCharacteristicsList.size()];
    for (int i = 0; i < replayCharacteristicsList.size(); i++) {
      replayString[i] = replayCharacteristicsList.get(i).getClickList().
          toString();
    }
    System.out.println("Max = " + scalaObject.
        findMaxRepeatedReplaySeq(replayString));

    int[] clickArray = replayCharacteristicsList.get(0)
        .getAllReplaysClickList();
    System.out.println("Left Clicks : " + scalaObject.
        countLeftButtonClicked(clickArray));
    System.out.println("Right Clicks : " + scalaObject.
        countRightButtonClicked(clickArray));
  }


  public void getFiles() throws IOException {
    replayFolderFiles = REPLAY_FOLDER.listFiles();
    for (int i = 0; i < replayFolderFiles.length; i++) {
      numOfReplay = i + 1;
      replayCharacteristicsList.add(new ReplayCharacteristics(
          replayFolderFiles[i].getName()));
    }
  }

  public String chooseReplay() {
    return choosedFile;
  }

  public void updateContent() {
    for (int i = 0; i < numOfReplay; i++) {
      replayContent[i][0] = ((replayCharacteristicsList.get(i).
          getReplayFileName()));
      replayContent[i][1] = (Integer.toString(replayCharacteristicsList.get(i).
          getNumOfColumns()));
      replayContent[i][2] = (Integer.toString(replayCharacteristicsList.get(i).
          getNumOfRows()));
      replayContent[i][3] = (Integer.toString(replayCharacteristicsList.get(i).
          getNumOfBombs()));
      replayContent[i][4] =
          (Integer.toString(replayCharacteristicsList.get(i).
              getSequenceOfLeftClicks()));
      replayContent[i][5] =
          (Integer.toString(replayCharacteristicsList.get(i).
              getSequenceOfRightClicks()));
    }
  }

  public void setContent() {
    for (int i = 0; i < numOfReplay; i++) {
      replayContent[i][0] = new String((replayFolderFiles[i].getName()));
      replayContent[i][1] =
          new String(Integer.toString(replayCharacteristicsList.get(i).
              getNumOfColumns()));
      replayContent[i][2] =
          new String(Integer.toString(replayCharacteristicsList.get(i).
              getNumOfRows()));
      replayContent[i][3] =
          new String(Integer.toString(replayCharacteristicsList.get(i).
              getNumOfBombs()));
      replayContent[i][4] =
          new String(Integer.toString(replayCharacteristicsList.get(i).
              getSequenceOfLeftClicks()));
      replayContent[i][5] =
          new String(Integer.toString(replayCharacteristicsList.get(i).
              getSequenceOfRightClicks()));
    }
  }

  public void addContentToTable() {
    replayContent = new String[numOfReplay][NUM_OF_COLUMNS];
    setContent();

    table = new JTable(replayContent, HEADERS);
    table.addMouseListener(new ReplayTableListener());
    table.getTableHeader().addMouseListener(new HeaderTableListener());
    table.setCellSelectionEnabled(true);
    table.setEnabled(false);

    JScrollPane scroll = new JScrollPane(table);
    replayFrame.add(scroll);
  }

  class HeaderTableListener implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent event) {
      sortArray();
      ReplayCharacteristics.setSortVariant(table.columnAtPoint(event.getPoint()));
      System.out.println(table.columnAtPoint(event.getPoint()));
      replayCharacteristicsList.sort(ReplayCharacteristics::compareTo);
      updateContent();
      table.updateUI();
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {

    }

    @Override
    public void mouseExited(MouseEvent arg0) {

    }

    @Override
    public void mousePressed(MouseEvent arg0) {

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {

    }

  }

  class ReplayTableListener implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent event) {

      replayFrame.dispose();
      start.setReplayFileName(
          (replayCharacteristicsList.get(event.getY() / CELL_HIEGHT).
              getReplayFileName()));
      try {
        start.createBoard();
      } catch (InterruptedException | IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {

    }

    @Override
    public void mouseExited(MouseEvent arg0) {

    }

    @Override
    public void mousePressed(MouseEvent arg0) {

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {

    }

  }
}
