package hwt.controller;

import hwt.Parameters;
import hwt.GameType;
import hwt.model.CaveType;
import hwt.model.PerfectMaze;
import hwt.model.Player;
import hwt.model.Direction;
import hwt.model.Room;
import hwt.view.DialogBox;
import hwt.view.View;
import input.GameInput;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import javax.swing.Timer;


public class Controller implements ActionListener {
  private final PerfectMaze maze;
  private final Player player;
  private View view = null;
  private GameInput input = null;
  private Timer timer = null;
  private boolean gameOver = false;
  private boolean firstShootPaint = true;

  private GameType gameType;
  private final Room caveWithWumpus;
  private final ArrayList<Room> cavesWithPits;
  private final ArrayList<Room> cavesWithBats;
  private final ArrayList<Room> cavesNearbyPits;
  private final ArrayList<Room> cavesNearbyWumpus;
  private final ArrayList<Room> rooms;
  private Room shootingTarget;
  private Direction shotDirection;
  private int numCavesShot;

  //constructor for TEXT game mode
  public Controller(PerfectMaze maze, Player player){
    this.maze = maze; //TODO check if I need whole maze -> used getStart() and getRoomsWithCaves()
    this.player = player;
    this.caveWithWumpus = maze.getCaveWithWumpus();
    this.cavesWithPits = maze.getCavesWithPits();
    this.cavesWithBats = maze.getCavesWithBats();
    this.rooms = maze.getRooms();
    this.cavesNearbyPits = maze.getCavesNearbyPits();
    this.cavesNearbyWumpus = maze.getCavesNearbyWumpus();
  }

  //constructor for GUI game mode
  public Controller(PerfectMaze maze, Player player, View view, GameInput input){
    this.maze = maze; //TODO check if I need whole maze -> used getStart() and getRoomsWithCaves()
    this.player = player;
    this.caveWithWumpus = maze.getCaveWithWumpus();
    this.cavesWithPits = maze.getCavesWithPits();
    this.cavesWithBats = maze.getCavesWithBats();
    this.rooms = maze.getRooms();
    this.cavesNearbyPits = maze.getCavesNearbyPits();
    this.cavesNearbyWumpus = maze.getCavesNearbyWumpus();

    this.view = view;
    this.input = input;
    this.timer = new Timer(Parameters.TIMER_PERIOD /* 60 fps */, this);
  }


  public void start(GameType type){
    this.gameType = type;
    if (type == GameType.GUI){
      this.startGUI();
    }
    else {
      this.startText();
    }
  }

  public void startText(){
    System.out.println("===============================================");
    System.out.println("WELCOME to HUNT THE WUMPUS!");
    System.out.println("===============================================");

    System.out.println("\nYou're in a maze and your task is to kill a wumpus that lives in one of the caves.\n"
        + "You can shoot the wumpus with your arrow.\n"
        + "But remember - if you miss and run out of arrows, you lose!\n"
        + "You can feel the wumpus in nearby caves.\n"
        + "You can also lose if you fall into a bottomless pit which you can also feel from nearby caves.\n"
        + "There are also caves with superbats. If you run into such a cave,\n"
        + "there is a 50% chance that they grab you and move to a different cave.\n"
        + "If there is a wumpus or bottomless pit in that cave, well, that's your fate!\n"
        + "Good luck in hunting  down the wumpus!\n");
    System.out.println("Your current number of arrows: " + player.getNumArrows());

    while (!gameOver) {
      Room currentCave = player.getLocation();
      System.out.println("You are in cave #" + currentCave.getId());
      checkAdjacentCaves(currentCave);
      printOptions(currentCave);
      shootOrMove(currentCave);
    }
//    System.exit(0);
  }
  public void startGUI() {

    Room playerLoc = player.getLocation();
    view.paint(rooms, caveWithWumpus,
        cavesWithBats, cavesWithPits, cavesNearbyPits, cavesNearbyWumpus, playerLoc);
    timer.start();

  }


  @Override
  public void actionPerformed(ActionEvent e) {

    Room beforeLoc = player.getLocation();
    beforeLoc.setVisited(true);
    Direction moveDirection = input.getMoveDirection();

    //need to reset direction to null because we use Timer, otherwise will move constantly in 1 direction
    input.resetMoveDirection();
    player.moveByRooms(beforeLoc, moveDirection);
    Room afterLoc = player.getLocation();
    //bc we use Timer, which calls actionPerformed every PERIOD, we don't want to repaint every period
    //only if the player location changed
    if (beforeLoc != afterLoc) {
      afterLoc.setVisited(true);
      checkAdjacentCaves(afterLoc);
      checkMoveForHazards(afterLoc);
      //needs to come last in case superbats worked
      view.repaintPlayer(player.getLocation());
    }
    boolean shoot = input.getShootStatus();
    boolean shot = input.isShot();
    //user pressed "s" to enter shoot mode
    if (shoot) {
      //target appears in location where player is
      if (firstShootPaint) {
        shootingTarget = player.getLocation();
        view.paintTarget(shootingTarget);
        firstShootPaint = false;
        //target moves independent of player location
      } else {
        Direction shootDir = input.getShootDirection();
        //need to reset shoot direction, otherwise will be moving in one direction all the time
        input.resetShootDirection();
        int adjacentRoomId = shootingTarget.findAdjacentRoomId(shootDir, maze.getNumRows(),
            maze.getNumCols());
        // findAdjacentRoomId() returns sentinel value - Integer.MIN_VALUE - when no Direction specified
        if (adjacentRoomId != Integer.MIN_VALUE) {
          Room newTargetRoom = rooms.get(adjacentRoomId);
          //make sure that target is moving only horizontally or vertically
          int playerRowId = player.getLocation().getRowId();
          int playerColId = player.getLocation().getColId();
          if ((newTargetRoom.getRowId() == playerRowId &&
              (shootDir == Direction.WEST || shootDir == Direction.EAST)) ||
              (newTargetRoom.getColId() == playerColId &&
              (shootDir == Direction.NORTH || shootDir == Direction.SOUTH)) ){
            view.paintTarget(newTargetRoom);
            shootingTarget = newTargetRoom;
            shotDirection = shootDir;
            //take the larger number - one of them will be 0
            // newRoom is either on the same rowId as player or same colId as player
            numCavesShot = Math.max(Math.abs(newTargetRoom.getRowId()-playerRowId),
                Math.abs(newTargetRoom.getColId()-playerColId));
          }
        }
      }
    }
    //user chose the target and pressed "Enter", i.e. made a shot
    if (shot) {
      Room targetCave = findCaveAfterShooting(shotDirection, player.getLocation(), numCavesShot);
      checkCaveAfterShooting(targetCave);
      checkArrows();
      //targetCave can be null if user shooting into unreachable cave
      if (targetCave != null){
        view.paintAfterShooting(targetCave); //reveals target cave after shooting
      }
      input.resetShooting();
      firstShootPaint = true;
    }
    //user pressed ESC to exit shooting mode without making a shot
    if (!shoot && !shot){
      view.paintResetShoot();
      firstShootPaint = true;
    }

    if (gameOver){
      printMessage("Game Over", "");
      System.exit(0);
    }
  }


  /**
   * checkAdjacentCaves() checks if there is a wumpus or a pit in adjacent caves
   * prints respective messages to the user
   * @param currentCave
   */
  public void checkAdjacentCaves(Room currentCave){

    if (currentCave.getCaveType().contains(CaveType.NEARBY_WUMPUS)){
      String messageWumpusNearby = "You smell something terrible nearby...";
      printMessage(messageWumpusNearby, "");
    }

    if (currentCave.getCaveType().contains(CaveType.NEARBY_PIT)) {
      String messagePitNearby = "You feel a cold wind blowing...";
      printMessage(messagePitNearby, "");
    }
  }

  /**
   * printOptions() prints available directions from current cave
   * @param currentCave
   */
  public void printOptions(Room currentCave){
    System.out.println("\nTunnels lead to: ");
    ArrayList<Direction> directions = currentCave.getDirections();
    for (Direction direction : directions){
      if (direction == Direction.EAST){
        System.out.println(direction + " [e]");
      }
      if (direction == Direction.WEST){
        System.out.println(direction + " [w]");
      }
      if (direction == Direction.NORTH){
        System.out.println(direction + " [n]");
      }
      if (direction == Direction.SOUTH){
        System.out.println(direction + " [s]");
      }
    }
//    System.out.println("[q] for quit");
  }

  /**
   * shootOrMove() provides the logic for a player to either shoot an arrow or move in specified
   * direction
   * also checks the input from the user for correctness
   * @param currentCave
   */
  public void shootOrMove(Room currentCave){
    System.out.println("\nShoot [s] or Move [m]?");
    Scanner sc = new Scanner(System.in);
    String inputShootOrMove = sc.nextLine();
    //if move
    if (inputShootOrMove.equals("m")){
      System.out.println("Where to?");
      Direction direction = readDirectionInput(sc);
      makeMove(currentCave, direction);
      //update current cave after move
      currentCave = player.getLocation();
      checkMoveForHazards(currentCave);
    }
    //shoot
    else if (inputShootOrMove.equals("s")) {
      System.out.println("No. of caves (1-5)?");
      int numCaves = readCavesInput(sc);
      System.out.println("In what direction?");
      Direction shootDirection = readDirectionInput(sc);
      Room targetCave = findCaveAfterShooting(shootDirection, currentCave, numCaves);
      checkCaveAfterShooting(targetCave);
      checkArrows();
    } else {
      System.out.println("Illegal command. Please enter either e, w, s, n or q for exit");
    }
  }

  public void checkArrows(){
    StringBuilder sb = new StringBuilder();
    if (!gameOver){
      player.decreaseArrows();
      int numArrows = player.getNumArrows();
      sb.append("Your number of arrows is now: ").append(numArrows);
//      System.out.println("Your number of arrows is now: " + numArrows);
      if (numArrows == 0) {
        sb.append("\nOh no! You ran out of arrows...");
//        System.out.println("Oh no! You ran out of arrows...");
        gameOver = true;
      }
      if (numArrows == 1) {
        sb.append("\nBe careful! If you miss next time, you lose");
//        System.out.println("Be careful! If you miss next time, you lose");
      }
      printMessage(sb.toString(),"");
    }
  }
  public void checkCaveAfterShooting(Room targetCave){
    StringBuilder sb = new StringBuilder();
    //targetCave is null if player specified larger distance than an arrow can travel
    if (targetCave != null) {
      sb.append("Your arrow reached cave #").append(targetCave.getId());
//      System.out.println("Your arrow reached cave #" + targetCave.getId());
      if (targetCave.getCaveType().contains(CaveType.WUMPUS)) {
        gameOver = true;
        sb.append("\nHee hee hee, you got the wumpus!");
        sb.append("\nNext time you won't be so lucky");
//        System.out.println("Hee hee hee, you got the wumpus!");
//        System.out.println("Next time you won't be so lucky\nGame Over");

      } else {
        sb.append("\nYou missed...");
//        System.out.println("You missed...");
      }
    } else {
      sb.append("\nYou missed...");
//      System.out.println("You missed...");
    }
    printMessage(sb.toString(), "");
  }

  private Direction readDirectionInput(Scanner sc) {
    String directionInput;
    while (true){
      directionInput = sc.nextLine();
      if (directionInput.equals("n") ||
          directionInput.equals("w") ||
          directionInput.equals("e") ||
          directionInput.equals("s") ||
          directionInput.equals("q")) {
        break;
      } else {
        System.out.println("Illegal command. Please enter either e, w, s, n or q for exit");
      }
    }
    Direction direction = translateInputIntoDirection(directionInput);
    return direction;
  }

  private int readCavesInput(Scanner sc){
    int numCaves;

    while (true){
      String inputNumCaves = sc.nextLine();
      Matcher matcher = Parameters.PATTERN_DIGITS.matcher(inputNumCaves);
      boolean matchFound = matcher.find();
      if (matchFound) {
        System.out.println("Your input is not a valid number");
      } else {
        numCaves = Integer.valueOf(inputNumCaves);
        if (numCaves >= 1 && numCaves <= 5) {
          break;
        } else {
          System.out.println("Can't shoot that number. Please enter a number between 1 and 5");
        }
      }
    }
    return numCaves;

  }

  private Direction translateInputIntoDirection(String input) {
    if (input.equals("s")){
      return Direction.SOUTH;
    }
    else if (input.equals("n")){
      return Direction.NORTH;
    }
    else if (input.equals("w")){
      return Direction.WEST;
    }
    else if (input.equals("e")){
      return Direction.EAST;
    }
    else if (input.equals("q")){
      System.out.println("Exiting the game");
      System.exit(0);
    }
    else {
      System.out.println("Illegal command. Please enter either e, w, s, n or q for exit");
    }
    return null;
  }

  public Room findCaveAfterShooting(Direction direction, Room targetCave, int numCaves){
    for (int i = 0; i < numCaves; i++) {
      if (targetCave != null && direction != null){
        targetCave = targetCave.findAdjacentCave(direction);
      }
    }
    return targetCave;
  }

  /**
   * makeMove() checks if the move is possible in the provided direction,
   *  if yes, moves the player to a new location
   * @param currentCave
   */
  public void makeMove(Room currentCave, Direction direction){

    ArrayList<Direction> possibleMoves = currentCave.getDirections();
    if (possibleMoves.contains(direction)){
      player.moveByCaves(currentCave, direction);
    }
  }

  /**
   * checkMoveForHazards() checks if there is wumpus, superbats or pit in a cave the player moved to
   * prints respective messages to the user
   * if the player moved into a cave with a pit, the game is over
   * if the player moved into a cave with a wumpus, the game is over
   * if the player moved into a cave with superbats, there's 50% chance to get carried over
   * by superbats to a new location
   * @param currentCave
   */
  public void checkMoveForHazards(Room currentCave){
    //check if cave has superbats first
    boolean superbatsWorked = checkSuperbats(currentCave);
    //if entered a cave with a pit and there are no superbats/superbats didn't work...
    if (currentCave.getCaveType().contains(CaveType.PIT) && !superbatsWorked){
      String messagePit = "Oh no! You fell into the bottomless pit... Better luck next time";
      printMessage(messagePit, "Game Over");
      gameOver = true;
    }

    //if entered a cave with wumpus and there are no superbats/superbats didn't work...
    if (currentCave.getCaveType().contains(CaveType.WUMPUS) && !superbatsWorked){
      String messageWumpus = "Chomp chomp chomp... Thank you for feeding the wumpus! Better luck next time";
      printMessage(messageWumpus, "Game Over");
      gameOver = true;
    }
  }

  /**
   * checkSuperbats() - helper function for checkMoveForHazards()
   * checks if the cave has superbats and takes appropriate actions if there are
   * (i.e. 50% chance to be carried over to a new random location)
   * prints respective messages to the user
   * @param currentCave
   * @return boolean if the room has superbats
   */
  private boolean checkSuperbats(Room currentCave){
    if (currentCave.getCaveType().contains(CaveType.SUPERBATS)) {
      Random rand = new Random();
      int randomIdx = Math.abs(rand.nextInt()) % 2; //generate 0 or 1
//      System.out.println("randomIdx: " + randomIdx);
      //you get carried away if randomIdx is 1
      String messageBatsWorked = "Snatch - you are grabbed by superbats";
      if (randomIdx == 1) {
        printMessage(messageBatsWorked, "");
        ArrayList<Room> roomsWithCaves = maze.getRoomsWithCaves();
        //generate newRoomId
        randomIdx = rand.nextInt(roomsWithCaves.size());
        Room newRoom = roomsWithCaves.get(randomIdx);
        player.setLocation(newRoom);
        checkMoveForHazards(newRoom);
        return true;
      }
      else {
        String messageBatsNotWorked = "Whoa -- you successfully duck superbats that try to grab you";
        printMessage(messageBatsNotWorked, "");
      }
    }
    return false;
  }

  public void printMessage(String message, String title){
    if (gameType == GameType.TEXT){
      System.out.println(message);
      System.out.print(title);
    }
    else {
      DialogBox.createDialogBox(message, title);
    }
  }

}
