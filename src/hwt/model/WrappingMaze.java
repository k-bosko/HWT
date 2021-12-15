package hwt.model;

/**
 * WrappingMaze class creates wrapping maze that extends RoomMaze and
 *  is built by removing wrapping walls from room maze
 */
public class WrappingMaze extends PerfectMaze {

  public WrappingMaze(int numRows, int numCols, int numPits, int numBats, boolean isSeed) {
    super(numRows, numCols, numPits, numBats, isSeed);
    removeSomeAdditionalWallsFrom(remainingInnerWalls);
    removeSomeAdditionalWallsFrom(wrappingWalls);
    determineRoomType();
    setCaveType();
    addAdjacentCaves();
    updateInfoForNearbyCaves();
    recalibrateStart();
  }
}