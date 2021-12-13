package hwt.model;

import java.util.ArrayList;
import java.util.Random;

/**
 * RoomMaze class creates room maze that extends Perfect Maze
 * and is built by removing inner walls from perfect maze
 */
public class RoomMaze extends PerfectMaze {

  public RoomMaze(int numRows, int numCols, int numPits, int numBats) {
    super(numRows, numCols, numPits, numBats);
    removeSomeAdditionalWallsFrom(remainingInnerWalls);
    determineRoomType();
    setCaveType();
    addAdjacentCaves();
    updateInfoForNearbyCaves();
    recalibrateStart();
  }

  /**
   * removeSomeAdditionalWallsFrom() removes walls from a provided list of walls
   * until a constraint (which specifies the number of remaining walls) is met
   * currently constraint is generated randomly
   * constraint can't be bigger than the number of remaining walls in a perfect maze
   * upon which room maze is built
   * @param walls - a list of walls from which we need to remove some walls
   */
  protected void removeSomeAdditionalWallsFrom(ArrayList<Wall> walls){
    Random rand = new Random(1212);
    //constraint - how many walls should stay
    int constraint = rand.nextInt(walls.size());
    while (walls.size() != constraint){
      // Obtain a number between [0 - Wall list size].
      int randomIdx = rand.nextInt(walls.size());
      Wall randomWall = walls.get(randomIdx);
      unify(randomWall.source, randomWall.destination);
      walls.remove(randomWall);
    }
  }
}