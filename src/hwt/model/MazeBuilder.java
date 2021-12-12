package hwt.model;

public class MazeBuilder {
  private final int numRows;
  private final int numCols;
  private final String mazeType;
  private int numPits;
  private int numBats;

  public MazeBuilder(int numRows, int numCols, String mazeType) throws IllegalArgumentException{
    if (numRows <= 0 || numCols <= 0){
      throw new IllegalArgumentException("Number of rows or columns cannot be negative or 0");
    }
    this.mazeType = mazeType;
    this.numRows = numRows;
    this.numCols = numCols;
    this.numPits = 2;
    this.numBats = 4;
  }

  public MazeBuilder specifyPitsNumber(int pitNum){
    this.numPits = pitNum;
    return this;
  }

  public MazeBuilder specifyBatsNumber(int batsNum){
    this.numBats = batsNum;
    return this;
  }


  public PerfectMaze build() throws IllegalArgumentException{
    PerfectMaze maze;
    if (this.mazeType.equals("non-wrapping")){
      maze = new RoomMaze(this.numRows, this.numCols, this.numPits, this.numBats);
    }
    else if (this.mazeType.equals("wrapping")){
      maze = new WrappingMaze(this.numRows, this.numCols, this.numPits, this.numBats);
    }
    else {
      throw new IllegalArgumentException("Illegal type - must be either wrapping or non-wrapping");
    }
    return maze;
  }

}
