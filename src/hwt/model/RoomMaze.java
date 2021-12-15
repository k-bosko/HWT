package hwt.model;

import java.util.ArrayList;
import java.util.Random;

/**
 * RoomMaze class creates room maze that extends Perfect Maze
 * and is built by removing inner walls from perfect maze
 */
public class RoomMaze extends PerfectMaze {

  public RoomMaze(int numRows, int numCols, int numPits, int numBats, boolean isSeed) {
    super(numRows, numCols, numPits, numBats, isSeed);
    removeSomeAdditionalWallsFrom(remainingInnerWalls);
    determineRoomType();
    setCaveType();
    addAdjacentCaves();
    updateInfoForNearbyCaves();
    recalibrateStart();
  }

}