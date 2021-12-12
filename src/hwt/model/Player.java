package hwt.model;

import java.util.HashMap;

/**
 * Player - class that creates a player
 * Player has arrows and location
 * it can also move north, south, east and west
 * (movement implementation is based on HashMap with directions as keys and adjacent caves as values)
 * player can increase/decrease number of arrows
 */
public class Player {
  private Room location;
  private int numArrows;

  public Player(Room start, int numArrows){
    this.location = start;
    this.numArrows = numArrows;
  }

  /**
   * getter for location
   */
  public Room getLocation(){
    return this.location;
  }

  /**
   * setter for location
   */
  public void setLocation(Room newCave){
    this.location = newCave;
  }

  /**
   * getter for number of arrows
   */
  public int getNumArrows(){
    return this.numArrows;
  }

  /**
   * decreaseArrows() decreases number of arrows by 1 (after shooting)
   */
  public int decreaseArrows(){
    return this.numArrows--;
  }

  /**
   * move() sets the new player location after moving east/west/north/south
   */
  public void move(Room currentCave, Direction direction){
    for (HashMap<Direction, Room> directionHashMap: currentCave.getDirectionsToAdjacentCaves()){
      if (directionHashMap.containsKey(direction)){
        this.location = directionHashMap.get(direction);
      }
    }
  }

}
