package hwt.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public abstract class PerfectMaze implements Maze {
  private int numRoomsWithSuperbats;
  private int numRoomsWithPits;
  private int start;
  private int mazeSize;
  private int numRows;
  private int numCols;
  private ArrayList<Room> rooms = new ArrayList<>();
  protected ArrayList<Wall> wrappingWalls;
  protected ArrayList<Wall> remainingInnerWalls;
  private int[] parent;
  private int[] componentSize;
  private ArrayList<Room> cavesWithPits = new ArrayList<>();
  private ArrayList<Room> cavesWithBats = new ArrayList<>();
  private Room caveWithWumpus;
  private ArrayList<Room> cavesNearbyWumpus = new ArrayList<>();
  private ArrayList<Room> cavesNearbyPits = new ArrayList<>();
  private ArrayList<Room> roomsWithCaves = new ArrayList<>();


  public PerfectMaze(int numRows, int numCols, int numPits, int numBats){
    this.numCols = numCols;
    this.numRows = numRows;
    this.mazeSize = numRows * numCols;
    this.numRoomsWithSuperbats = numBats;
    this.numRoomsWithPits = numPits;
    this.parent = new int[mazeSize];
    this.componentSize = new int[mazeSize];
    this.start = 0;
    ArrayList<Wall> innerWalls = generateInnerWallsAndRooms();
    this.wrappingWalls = generateWrappingWalls();
    this.remainingInnerWalls = removeSomeWallsFrom(innerWalls);
  }

  public Room getCaveWithWumpus(){
    return this.caveWithWumpus;
  }

  public ArrayList<Room> getCavesWithPits(){
    return this.cavesWithPits;
  }

  public ArrayList<Room> getCavesWithBats(){
    return this.cavesWithBats;
  }

  public ArrayList<Room> getRoomsWithCaves(){
    return this.roomsWithCaves;
  }

  public ArrayList<Room> getCavesNearbyWumpus() { return this.cavesNearbyWumpus; }

  public ArrayList<Room> getCavesNearbyPits() { return this.cavesNearbyPits; }

  /**
   * getNumCols() returns number of columns in a maze
   */
  public int getNumCols(){
    return this.numCols;
  }

  /**
   * getNumCols() returns number of rows in a maze
   */
  public int getNumRows(){
    return this.numRows;
  }


  /**
   * setStart() sets the index of player's start location
   * @param start
   * @throws IndexOutOfBoundsException if index is out of bounds
   */
  public void setStart(int start) throws IndexOutOfBoundsException{
    if (start > mazeSize || start < 0){
      throw new IndexOutOfBoundsException("Start index must be positive and less than maze size");
    }
    this.start = start;
  }


  /**
   * getRooms() returns array of rooms in a maze
   */
  public ArrayList<Room> getRooms(){
    return rooms;
  }

  /**
   * getRoomsBy() returns a room by specified index
   */
  public Room getRoomBy(int location){
    return rooms.get(location);
  }

  /**
   * getStart() returns index of start
   */
  public int getStart(){
    return this.start;
  }

  /**
   * generateInnerWallsAndRooms() generates inner walls and rooms
   * Wall 0-1 is the same as 1-0, so only half of all possible Walls needs to be generated
   * this is done by generating for each index (aka room) a north wall (except first row)
   * or east wall (except last column)
   * adds new room to a list of rooms
   * initializes parent and componentSize to be used by UnionFind algorithm
   * @return ArrayList<Wall> - list of walls
   */
  private ArrayList<Wall> generateInnerWallsAndRooms(){
    ArrayList<Wall> innerWalls = new ArrayList<>();
    //Wall 0-1 is the same as 1-0, so only half of all possible Walls needs to be generated
    for (int i = 0; i < mazeSize; i++){
      //generate rooms
      Room newRoom = new Room(i);
      int colId = i % this.numCols + 0;
      newRoom.setColId(colId);
      int newRowId = (i - newRoom.getColId())/this.numCols + 0;
      newRoom.setRowId(newRowId);

      rooms.add(newRoom);
      //north Wall - for every room except in the first row
      if (i - numCols >= 0){
        Wall w = new Wall(i, i - numCols);
        innerWalls.add(w);
      }
      //east Wall - for every room except in the last column
      if ( (i + 1) % numCols != 0 ){
        Wall w = new Wall(i, i + 1);
        innerWalls.add(w);
      }

      parent[i] = i; // Link to itself (self root)
      componentSize[i] = 1; // Each component is originally of size one
    }
    return innerWalls;
  }

  /**
   * generateWrappingWalls() generates wrapping walls of a perfect maze
   * @return ArrayList<Wall> - returns a list of walls
   */
  private ArrayList<Wall>  generateWrappingWalls(){
    ArrayList<Wall> wrappingWalls = new ArrayList<>();
    // 0 1 2 3
    // 4 5 6 7
    //walls on the top/bottom - 0-4, 1-5, 2-6, 3-7
    for (int i = 0; i < numCols; i++) {
      Wall w1 = new Wall(i, i + (numRows - 1) * numCols);
      wrappingWalls.add(w1);
    }
    //walls on the left/right - 0-3, 4-7
    for (int i = 0; i < mazeSize; i += numCols) {
      Wall w1 = new Wall(i, i + (numCols - 1));
      wrappingWalls.add(w1);
    }
    return wrappingWalls;
  }

  /**
   * find() searches for root of the connected component
   * the method is part of Union-Find Algorithm
   * which is used to check whether an undirected graph contains cycle or not
   * @return root of component
   */
  private int find(int p) {
    // Find the root of the component/set
    int root = p;
    while (root != parent[root]) {
      root = parent[root];
    }

    // Compress the path leading back to the root.
    // Doing this operation is called "path compression"
    // and is what gives us amortized time complexity.
    while (p != root) {
      int next = parent[p];
      parent[p] = root;
      p = next;
    }
    return root;
  }


  /**
   * connected() - part of Union-Find Algorithm
   * checks if two values (in our case rooms) are connected,
   * i.e. if they are part of the same component
   * @param p room (source for wall)
   * @param q another room (destination for wall)
   * @return true if connected, false otherwise
   */
  private boolean connected(int p, int q) {
    return find(p) == find(q);
  }

  /**
   * Unify - part of Union-Find Algorithm
   * unifies rooms 'source' and 'destination' into a single component/set
   * @param source
   * @param destination
   */
  protected void unify(int source, int destination) {
    int root1 = find(source);
    int root2 = find(destination);

    // Merge smaller component/set into the larger one.
    if (componentSize[root1] < componentSize[root2]) {
      componentSize[root2] += componentSize[root1];
      parent[root1] = root2;
      componentSize[root1] = 0;
    } else {
      componentSize[root1] += componentSize[root2];
      parent[root2] = root1;
      componentSize[root2] = 0;
    }
    updateRoomsInfo(source, destination);
  }

  /**
   * updateRoomsInfo() - updates information about doors for two rooms - source and
   * destination - between which the wall is being removed
   * every time we remove a wall, we open a door,
   * and we update this information as we build our maze
   * if the room has a door in some direction, then that direction is set to true
   * @param source  - room
   * @param destination - another room
   */
  private void updateRoomsInfo(int source, int destination){
    Room sourceRoom = rooms.get(source);
    Room destinationRoom = rooms.get(destination);
    //inner walls logic
    if (source - numCols == destination){
      //update directions which is ArrayList
      sourceRoom.getDirections().add(Direction.NORTH);
      destinationRoom.getDirections().add(Direction.SOUTH);
      //update array of adjacent rooms
      addAdjacentRooms(Direction.NORTH, source);
      addAdjacentRooms(Direction.SOUTH, destination);
      //every removed wall is a door
      sourceRoom.increaseNumDoors();
      destinationRoom.increaseNumDoors();

    }
    //inner walls logic
    else if (source + 1 == destination){
      //update directions
      sourceRoom.getDirections().add(Direction.EAST);
      destinationRoom.getDirections().add(Direction.WEST);
      //update array of adjacent rooms
      addAdjacentRooms(Direction.EAST, source);
      addAdjacentRooms(Direction.WEST, destination);
      //every removed wall is a door
      sourceRoom.increaseNumDoors();
      destinationRoom.increaseNumDoors();
    }
    //wrapping walls logic
    else if (source + numCols - 1 == destination){
      //update directions
      sourceRoom.getDirections().add(Direction.WEST);
      destinationRoom.getDirections().add(Direction.EAST);
      //update array of adjacent rooms
      addAdjacentRooms(Direction.WEST, source);
      addAdjacentRooms(Direction.EAST, destination);
      //every removed wall is a door
      sourceRoom.increaseNumDoors();
      destinationRoom.increaseNumDoors();
    }
    //wrapping walls logic
    else if (source + (numRows-1) * numCols == destination){
      //update directions
      sourceRoom.getDirections().add(Direction.NORTH);
      destinationRoom.getDirections().add(Direction.SOUTH);
      //update array of adjacent rooms
      addAdjacentRooms(Direction.NORTH, source);
      addAdjacentRooms(Direction.SOUTH, destination);
      //every removed wall is a door
      sourceRoom.increaseNumDoors();
      destinationRoom.increaseNumDoors();
    }
  }

  /**
   * pickRandomWall() picks a wall randomly from a list of walls
   * @param walls -array of walls
   * @return wall
   */
  protected Wall pickRandomWall(ArrayList<Wall> walls){
    //TODO remove seed to generate randomly - previous seed 12345
    Random rand = new Random(1213145);
    // Obtain a number between [0 - Wall list size].
    int randomIdx = rand.nextInt(walls.size());
    Wall randomWall = walls.get(randomIdx);
    return randomWall;
  }

  /**
   * removeSomeWallsFrom() removes random walls from a list of walls
   * removing a wall means to unify two rooms - source and destination of a wall -
   * into a single component, if they are in different components
   * the wall is then removed from the list of walls
   * if the two random rooms are already connected, then a cycle can be created
   * which we don't want because it will violate a condition of a perfect maze
   * in this case, the wall remains and is added to the list of remaining walls
   * for a perfect maze we need to go through all the walls and decide what to do
   * (remove or leave) with each wall separately
   * we do this with a while-loop, so destructing our original list of all walls
   * @param walls
   * @return ArrayList<Wall> - list of walls that we decided to keep
   */
  private ArrayList<Wall> removeSomeWallsFrom(ArrayList<Wall> walls){
    ArrayList<Wall> remainingWalls = new ArrayList<>();
    while (walls.size() != 0) {
      Wall randomWall = pickRandomWall(walls);
      if (connected(randomWall.source, randomWall.destination)) {
        //creates cycle, add to the saved list
        remainingWalls.add(randomWall);
      } else {
        unify(randomWall.source, randomWall.destination);
      }
      walls.remove(randomWall);
    }
    return remainingWalls;
  }

  /**
   * printWalls() prints the walls from two lists - list of wrapping walls
   * and list of inner walls in a form 'source'-'destination' where
   * source is one room and destination is another room
   */
  public void printWalls() {
    StringBuffer sb = new StringBuffer();
    sb.append(numRows).append("x").append(numCols).append(" dimensions\n");
    sb.append("Wrapping walls: \n");
    wrappingWalls.forEach(w -> sb.append(w.source).append("-").append(w.destination).append("\n"));
    sb.append("Inner walls: \n");
    remainingInnerWalls.forEach(w -> sb.append(w.source).append("-").append(w.destination).append("\n"));
    System.out.println(sb.toString());
    System.out.println("\n");
  }

  /**
   * printRoomsInfo() prints information for each room
   */
  public void printRoomsInfo(){
    for (int i = 0; i < this.mazeSize; i++){
      System.out.println("---------------");
      System.out.println("ROOM #" + i);
      System.out.println("---------------");
      String info = rooms.get(i).toString();
      System.out.println(info);
    }

    System.out.println("\nRooms with pits:");
    cavesWithPits.forEach(r -> System.out.println(r.getId()));
    System.out.println("Rooms with superbats:");
    cavesWithBats.forEach(r -> System.out.println(r.getId()));
    System.out.println("Room with wumpus:\n" + this.caveWithWumpus.getId());
  }

  /**
   * determineRoomType() goes through all rooms
   * and determines which type it is (CAVE or TUNNEL)
   * if room has 2 doors, then it's a hallway
   * if room has 1, 3 or 4 doors, then it's a cave
   */
  protected void determineRoomType(){
    for (int i = 0; i < mazeSize; i++) {
      if (rooms.get(i).getAdjacentRooms().size() == 2) {
        rooms.get(i).setType(RoomType.TUNNEL);
      } else {
        rooms.get(i).setType(RoomType.CAVE);
        roomsWithCaves.add(rooms.get(i));
      }
    }
  }

  /**
   * setCaveType() adds to a room that is cave a cave type - superbat, pit or wumpus
   * Note: cave types are added to an array because we can have wumpus and superbat in the same cave
   * as well as pit and superbat
   * a cave type is enum CaveType
   */
  protected void setCaveType() {
    int bats = 0;
    int pits = 0;
    int wumpus = 0;
    //add seed if you want the same result
    Random rand = new Random(3781);
    int randomIdx;

    while (bats < this.numRoomsWithSuperbats) {
      randomIdx = rand.nextInt(mazeSize);
      if (rooms.get(randomIdx).getType() == RoomType.CAVE &&
          !rooms.get(randomIdx).getCaveType().contains(CaveType.SUPERBATS) &&
          randomIdx != start) {
        rooms.get(randomIdx).getCaveType().add(CaveType.SUPERBATS);
        cavesWithBats.add(rooms.get(randomIdx));
        bats++;
      }
    }
    while (pits < this.numRoomsWithPits) {
      randomIdx = rand.nextInt(mazeSize);
      Room pitRoom = rooms.get(randomIdx);
      if (pitRoom.getType() == RoomType.CAVE &&
          !pitRoom.getCaveType().contains(CaveType.PIT) &&
          randomIdx != start) {
        pitRoom.getCaveType().add(CaveType.PIT);
        cavesWithPits.add(rooms.get(randomIdx));
        pits++;
      }
    }
    while (wumpus < 1) {
      randomIdx = rand.nextInt(mazeSize);
      Room wumpusRoom = rooms.get(randomIdx);
      if (wumpusRoom.getType() == RoomType.CAVE &&
//          !wumpusRoom.getCaveType().contains(CaveType.WUMPUS) &&
          randomIdx != start) {
        wumpusRoom.getCaveType().add(CaveType.WUMPUS);
        caveWithWumpus = wumpusRoom;
          wumpus++;
      }
    }
  }

  public void updateInfoForNearbyCaves(){
    //update info for nearby caves
    ArrayList<Room> adjacentWumpusRooms = this.caveWithWumpus.getAdjacentCaves();
    for (Room roomNearby : adjacentWumpusRooms) {
      roomNearby.getCaveType().add(CaveType.NEARBY_WUMPUS);
      cavesNearbyWumpus.add(roomNearby);
    }

    //update info for nearby caves
    for (Room pitRoom: this.cavesWithPits){
      ArrayList<Room> adjacentPitRooms = pitRoom.getAdjacentCaves();
      for (Room roomNearby: adjacentPitRooms) {
        roomNearby.getCaveType().add(CaveType.NEARBY_PIT);
        cavesNearbyPits.add(roomNearby);
      }
    }

  }
  /**
   * moveNorth() calculates adjacent room Id to the north
   */
  private int moveNorth(int currentId){
    int adjacentId;
    if (rooms.get(currentId).getRowId() > 0) {
      adjacentId = currentId - numCols;
    } else {
      adjacentId = currentId + (numRows - 1) * numCols;
    }
    return adjacentId;
  }

  /**
   * moveSouth() calculates adjacent room Id to the south
   */
  private int moveSouth(int currentId){
    int adjacentId;
    if (rooms.get(currentId).getRowId() < numRows - 1) {
      adjacentId = currentId + numCols;
    } else {
      adjacentId = currentId - (numRows - 1) * numCols;
    }
    return adjacentId;
  }

  /**
   * moveEast() calculates adjacent room Id to the east
   */
  private int moveEast(int currentId){
    int adjacentId;
    if (rooms.get(currentId).getColId() < numCols - 1) {
      adjacentId = currentId + 1;
    } else {
      adjacentId = currentId - (numCols - 1);
    }
    return adjacentId;
  }

  /**
   * moveWest() calculates adjacent room Id to the west
   */
  private int moveWest(int currentId){
    int adjacentId;
    if (rooms.get(currentId).getColId() > 0) {
      adjacentId = currentId - 1;
    } else {
      adjacentId = currentId + (numCols - 1);
    }
    return adjacentId;
  }

  /**
   * findAdjacentRoomId() returns id of the adjacent room in the given direction from current room
   */
  public int findAdjacentRoomId(Direction direction, int currentId) {
    int adjacentId;
    if (direction == Direction.NORTH) {
      adjacentId = moveNorth(currentId);
    } else if (direction == Direction.SOUTH) {
      adjacentId = moveSouth(currentId);
    } else if (direction == Direction.EAST) {
      adjacentId = moveEast(currentId);
    }
    //Direction WEST
    else {
      adjacentId = moveWest(currentId);
    }
    return adjacentId;
  }


  /**
   * addAdjacentRooms() - helper function that adds a room to a list of adjacent rooms
   * when the maze is built
   */
  private void addAdjacentRooms(Direction direction, int currentId){
    int adjacentRoomId = findAdjacentRoomId(direction, currentId);
    Room adjacentRoom = rooms.get(adjacentRoomId);
    rooms.get(currentId).getAdjacentRooms().add(adjacentRoom);
  }

  /**
   * addAdjacentCaves() - goes through all caves and follows through tunnels to look for adjacent caves
   * saves found adjacent cave into a list (adjacentCaves)
   * and also as hash map with starting direction as key and adjacent cave as value (directionsToAdjacentCaves)
   * Note: when we follow a tunnel, we don't want to go back -> need to calculate opposite direction
   */
  public void addAdjacentCaves(){
    Direction followedDirection;
    int newRoomId;
    for (Room cave : roomsWithCaves ){
      for (Direction direction : cave.getDirections()){
        followedDirection = direction;
        newRoomId = findAdjacentRoomId(direction, cave.getId());
        Room newRoom = rooms.get(newRoomId);
          while (newRoom.getType() == RoomType.TUNNEL) {
            //follow down the tunnel
            for (Direction followDirection : newRoom.getDirections()){
              // get opposite direction so not to follow back the route
              Direction opposite = findOppositeDirection(followDirection);
              //we don't want to go back so check if opposite direction is the same as we started
              //e.g. if we went north, we don't want to go south from new location
              //opposite of south is north, so if north == north, we don't go south
              if (direction != opposite){
                newRoomId = findAdjacentRoomId(followDirection, newRoom.getId());
                newRoom = rooms.get(newRoomId);
                //when we found a cave, exit the loop
                if (newRoom.getType() == RoomType.CAVE){
                  break;
                }
                //update direction to compare with opposite
                direction = followDirection;
                //after update (moved to new room) don't follow the directions left in for-loop
                break;
              }
            }
          }
        cave.getAdjacentCaves().add(newRoom);
        HashMap<Direction, Room> hm = new HashMap<>();
        hm.put(followedDirection, newRoom);
        cave.getDirectionsToAdjacentCaves().add(hm);
      }
    }
  }

  /**
   * findOppositeDirection() - helper function needed in addAdjacentCaves()
   * to avoid going back in the tunnel
   * finds opposite direction of the current direction
   */
  private Direction findOppositeDirection(Direction direction){
    if (direction == Direction.EAST){
      return Direction.WEST;
    } else if (direction == Direction.SOUTH){
      return Direction.NORTH;
    } else if (direction == Direction.NORTH){
      return Direction.SOUTH;
    } else {
      return Direction.EAST;
    }
  }

  /**
   * recalibrateStart() helps to avoid starting in a tunnel or
   * a cave with wumpus or in a cave with pit/superbats
   * is called when the maze is being built but after the cave types are assigned
   */
  public void recalibrateStart(){
    Random rand = new Random(112);
    int randomIdx = rand.nextInt(this.roomsWithCaves.size());
    Room randomCave = this.roomsWithCaves.get(randomIdx);
    //search for a cave without hazards to start
    while (randomCave.getCaveType().contains(CaveType.WUMPUS) ||
        randomCave.getCaveType().contains(CaveType.PIT) ||
        randomCave.getCaveType().contains(CaveType.SUPERBATS)){
      randomIdx = rand.nextInt(this.roomsWithCaves.size());
      randomCave = this.roomsWithCaves.get(randomIdx);
    }
    this.start = this.roomsWithCaves.get(randomIdx).getId();
  }
}
