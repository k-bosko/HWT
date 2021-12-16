package hwt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MenuParamsFileHandler {
  private Scanner scanner;
  private int numRows;
  private int numCols;

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
  }


  public void writeToFile(int numRows, int numCols, int numBats, int numPits) throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append(numRows).append(" ").append(numCols);
    BufferedWriter writer = new BufferedWriter(new FileWriter("./restart_game.txt"));
    writer.write(sb.toString());

    writer.close();
  }

  public int getNumRows(){
    return this.numRows;
  }

  public int getNumCols(){
    return this.numCols;
  }

}
