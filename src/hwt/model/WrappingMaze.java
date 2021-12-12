package hwt.model;

/**
 * WrappingMaze class creates wrapping maze that extends RoomMaze and
 *  is built by removing wrapping walls from room maze
 */
public class WrappingMaze extends RoomMaze {

  public WrappingMaze(int numRows, int numCols, int numPits, int numBats) {
    super(numRows, numCols, numPits, numBats);
    removeSomeAdditionalWallsFrom(wrappingWalls);
    determineRoomType();
    setCaveType();
    addAdjacentCaves();
    recalibrateStart();
  }
}