package hwt.model;

public class MazeBuilder {
  private final int numRows;
  private final int numCols;
  private final boolean isNonWrappingMaze;
  private final boolean isWrappingMaze;
  private int numPits;
  private int numBats;
  private boolean isSeed;

  public MazeBuilder(int numRows, int numCols, boolean isNonWrappingMaze,
      boolean isWrappingMaze, boolean isSeed) throws IllegalArgumentException{
    if (numRows <= 0 || numCols <= 0){
      throw new IllegalArgumentException("Number of rows or columns cannot be negative or 0");
    }
    this.isNonWrappingMaze = isNonWrappingMaze;
    this.isWrappingMaze = isWrappingMaze;
    this.numRows = numRows;
    this.numCols = numCols;
    this.numPits = 2;
    this.numBats = 4;
    this.isSeed = isSeed;
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
    if (isNonWrappingMaze){
      maze = new RoomMaze(this.numRows, this.numCols, this.numPits, this.numBats, this.isSeed);
    }
    else if (isWrappingMaze){
      maze = new WrappingMaze(this.numRows, this.numCols, this.numPits, this.numBats, this.isSeed);
    }
    else {
      throw new IllegalArgumentException("Illegal type - must be either wrapping or non-wrapping");
    }
    return maze;
  }

}
