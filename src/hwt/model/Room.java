package hwt.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Room class has information about possible directions depending on how our maze is being built
 * directions for each room are saved as list
 * each room can be either a cave (has 1,3,4 directions) or tunnel (only 2 directions) -> type
 * each room that is a cave can have additional caveType set to either wumpus, superbats or pit
 * each room saves a list of adjacent rooms
 * each cave saves a list of adjacent caves and also directions to adjacent caves as hash map
 * with direction as key and adjacent cave as value
 */
public class Room {
  private int id;
  private int rowId;
  private int colId;
  private RoomType type;
  private ArrayList<Direction> directions = new ArrayList<>();
  private ArrayList<CaveType> caveType = new ArrayList<>();
  private ArrayList<Room> adjacentRooms = new ArrayList<>();
  private ArrayList<Room> adjacentCaves = new ArrayList<>();
  private ArrayList<HashMap> directionsToAdjacentCaves = new ArrayList<>();
  private ArrayList<HashMap> directionsToAdjacentRooms = new ArrayList<>();

  public Room(int id){
    this.id = id;
  }

  public int getNumDoors(){
    return this.directions.size();
  }

  /**
   * getter for room id
   */
  public int getId(){
    return this.id;
  }

  /**
   * getter for room row id
   */
  public int getRowId(){
    return this.rowId;
  }

  /**
   * setter for room row id
   */
  public void setRowId(int newId){
    this.rowId = newId;
  }

  /**
   * getter for room column id
   */
  public int getColId(){
    return this.colId;
  }

  /**
   * setter for room column id
   */
  public void setColId(int newColId){
    this.colId = newColId;
  }

  /**
   * getter for room type
   * @return enum Direction
   */
  public RoomType getType(){
    return this.type;
  }

  /**
   * setter for room type
   */
  public void setType(RoomType type){
    this.type = type;
  }

  /**
   * getter for directions
   */
  public ArrayList<Direction> getDirections(){
    return this.directions;
  }

  /**
   * getter for cave type (wumpus/pit/superbats)
   */
  public ArrayList<CaveType> getCaveType(){
    return this.caveType;
  }

  /**
   * getter for adjacent caves
   */
  public ArrayList<Room> getAdjacentCaves(){
    return this.adjacentCaves;
  }

  /**
   * getter for adjacent rooms
   */
  public ArrayList<Room> getAdjacentRooms(){
    return this.adjacentRooms;
  }

  /**
   * getter for directions to adjacent caves
   */
  public ArrayList<HashMap> getDirectionsToAdjacentCaves(){
    return this.directionsToAdjacentCaves;
  }

  public ArrayList<HashMap> getDirectionsToAdjacentRooms(){
    return this.directionsToAdjacentRooms;
  }

  /**
   * findAdjacentCave() returns id of the adjacent cave in the given direction from current cave
   * extracts information from a hash map with direction as key and adjacent cave as value
   * Note: ignores rooms with tunnels
   */
  public Room findAdjacentCave(Direction direction){
    Room adjacentCave = null;
    for (HashMap<Direction, Room> directionHM: this.directionsToAdjacentCaves){
      if (directionHM.containsKey(direction)){
        adjacentCave = directionHM.get(direction);
      }
    }
    return adjacentCave;
  }

  public String toString(){
    StringBuffer sb = new StringBuffer();
    sb.append("row id: ").append(this.getRowId()).append("\ncol id: ").append(this.getColId());
    sb.append("\ndoors: ").append(this.getNumDoors()).append("\ntype: ").append(this.getType());
    if (this.getType() == RoomType.CAVE) {
      //if cave is a cave with bats, pits or wumpus (i.e. caveType (which is array) size != 0)
      if (this.getCaveType().size() != 0){
        sb.append("\ncave type: ");
        this.getCaveType().forEach(ct -> sb.append(ct).append(" "));
      }
      sb.append("\ndirections to adjacent CAVES (no tunnels):\n");
      for (HashMap<Direction, Room> directionCaveHashMap : this.getDirectionsToAdjacentCaves()){
        for (Direction directionAsKey : directionCaveHashMap.keySet()){
          sb.append("    ").append(directionAsKey).append(" : ");
          sb.append(directionCaveHashMap.get(directionAsKey).getId());
        }
      }
    }
    sb.append("\ndirections to adjacent ROOMS (tunnels or caves):\n");
    for (HashMap<Direction, Room> directionRoomHashMap : this.getDirectionsToAdjacentRooms()){
      for (Direction directionAsKey : directionRoomHashMap.keySet()){
        sb.append("    ").append(directionAsKey).append(" : ");
        sb.append(directionRoomHashMap.get(directionAsKey).getId());
      }
    }

    return sb.toString();
  }
}
