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
   * moveByCaves() sets the new player location after moving east/west/north/south to a respective cave
   * as a result ignores tunnels
   * utilizes ArrayList<HashMap> directionsToAdjacentCaves saved for every Cave
   * to be used in TEXT game mode
   */
  public void moveByCaves(Room currentCave, Direction direction){
    for (HashMap<Direction, Room> directionHashMap: currentCave.getDirectionsToAdjacentCaves()){
      if (directionHashMap.containsKey(direction)){
        this.location = directionHashMap.get(direction);
      }
    }
  }

//  /**
//   * moveByRoom() sets the new player location after moving east/west/north/south to a respective room
//   * irrespective if it's Tunnel or
//   * utilizes ArrayList<HashMap> directionsToAdjacentRooms saved for every Room
//   * to be used in GUI game mode (bc we need to reveal map room by room)
//   */
//  public void moveByRooms(Room currentCave, Direction direction){
//    for (HashMap<Direction, Room> directionHashMap: currentCave.getDirectionsToAdjacentRooms()){
//      if (directionHashMap.containsKey(direction)){
//        this.location = directionHashMap.get(direction);
//      }
//    }
//  }

}
