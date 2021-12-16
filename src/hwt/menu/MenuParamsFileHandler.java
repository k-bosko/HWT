package hwt.menu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JButton;

public class MenuParamsFileHandler implements Menu {
  private Scanner scanner;
  private int numRows;
  private int numCols;
  private boolean isNonWrapping;
  private boolean isWrapping;
  private int numPits;
  private int numBats;
  private boolean isSeed;
  private boolean isGuiGame;
  private boolean isTextGame;
  private int numArrows;

  public MenuParamsFileHandler() {
  }

  public void readFromFile() throws FileNotFoundException {
    try {
      this.scanner = new Scanner(new File("./restart_game.txt"));
    } catch(Exception e) {
      System.out.println("file not found");
      scanner = null;
    }
    // read one line from the record file and decode it
    String record = scanner.nextLine();
    String[] vals = record.split(" ");

    this.numRows = Integer.parseInt(vals[0]);
    this.numCols = Integer.parseInt(vals[1]);
    switch(vals[2]){
      case "1":
        isNonWrapping = true;
        isWrapping = false;
        break;
      case "0":
        isNonWrapping = false;
        isWrapping = true;
        break;
    }
    this.numPits = Integer.parseInt(vals[3]);
    this.numBats = Integer.parseInt(vals[4]);
    switch(vals[5]){
      case "10":
        isGuiGame = true;
        isTextGame = false;
        break;
      case "20":
        isGuiGame = false;
        isTextGame = true;
        break;
    }
    this.isSeed = true;
    this.numArrows = Integer.parseInt(vals[5]);
  }


  public void writeToFile(int numRows, int numCols, boolean isNonWrapping,
                          int numPits, int numBats, boolean isGuiGame,
                          boolean isTextGame, int numArrows) throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append(numRows).append(" ").append(numCols).append(" ");
    if (isNonWrapping){
      sb.append("1");
    }
    else {
      sb.append("0");
    }
    sb.append(" ").append(numPits).append(" ").append(numBats).append(" ");
    if (isGuiGame) {
      sb.append("10");
    }
    if (isTextGame){
      //TEXT
      System.out.println("appending TEXT");
      sb.append("20");
    }
    sb.append(" ").append(numArrows);
    BufferedWriter writer = new BufferedWriter(new FileWriter("./restart_game.txt"));
    writer.write(sb.toString());
    writer.close();
  }

  @Override
  public void show() {

  }

  @Override
  public void hide() {

  }

  @Override
  public JButton getStartGameBtn() {
    return null;
  }

  @Override
  public JButton getRestartGameBtn() {
    return null;
  }

  @Override
  public int getNumRows(){
    return this.numRows;
  }

  @Override
  public int getNumCols(){
    return this.numCols;
  }

  @Override
  public boolean isNonWrappingMaze() {
    return isNonWrapping;
  }

  @Override
  public boolean isWrappingMaze() {
    return isWrapping;
  }

  @Override
  public boolean isNewMaze() {
    return false;
  }

  @Override
  public boolean isSameMaze() {
    return true;
  }

  @Override
  public boolean isTextGame() {
    return isTextGame;
  }

  @Override
  public boolean isGuiGame() {
    return isGuiGame;
  }

  @Override
  public int getNumPits() {
    return numPits;
  }

  @Override
  public int getNumBats() {
    return numBats;
  }

  @Override
  public int getNumArrows() {
    return numArrows;
  }

}
