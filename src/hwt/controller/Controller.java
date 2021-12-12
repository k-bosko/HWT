package hwt.controller;

import hwt.model.CaveType;
import hwt.model.PerfectMaze;
import hwt.model.Player;
import hwt.model.Direction;
import hwt.model.Room;
import hwt.view.View;
import input.GameInput;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.Timer;


public class Controller implements ActionListener {
  private final PerfectMaze maze;
  private final Player player;
  private final View view;
  private final GameInput input;
  private final Timer timer;
  static Pattern patternDigits = Pattern.compile("[^\\d]");
  private static boolean wumpusKilled = false;
  private static final int PERIOD = 150;

  public Controller(PerfectMaze maze, Player player, View view, GameInput input){
    this.maze = maze;
    this.player = player;
    this.view = view;
    this.input = input;
    this.timer = new Timer(PERIOD /* 60 fps */, this);
  }

  public void startText(){
    Room caveWithWumpus = maze.getCaveWithWumpus();
    ArrayList<Room> cavesWithPits = maze.getCavesWithPits();

    System.out.println("===============================================");
    System.out.println("WELCOME to HUNT THE WUMPUS!");
    System.out.println("===============================================");

    System.out.println("\nYou're in cave #" + maze.getStart());
    System.out.println("Your current number of arrows: " + player.getNumArrows());

    while (!wumpusKilled) {
      Room currentCave = player.getLocation();
      checkAdjacentCaves(currentCave, caveWithWumpus, cavesWithPits);
      printOptions(currentCave);
      shootOrMove(currentCave);
    }
  }
  public void startGUI() {
    Room caveWithWumpus = maze.getCaveWithWumpus();
    ArrayList<Room> cavesWithPits = maze.getCavesWithPits();
    ArrayList<Room> cavesWithBats = maze.getCavesWithBats();
    ArrayList<Room> rooms = maze.getRooms();
    Room playerLoc = player.getLocation();

    view.paint(rooms, caveWithWumpus,
        cavesWithBats, cavesWithPits, playerLoc);
    timer.start();
  }


  @Override
  public void actionPerformed(ActionEvent e) {
    Room beforeLoc = player.getLocation();
    Direction targetDir = input.getDirection();
    input.resetDirection();
    player.move(beforeLoc, targetDir);
    Room afterLoc = player.getLocation();
    if (beforeLoc != afterLoc){
      System.out.println("new location - " + afterLoc.getId());
      view.repaintPlayer(afterLoc);
    }


  }

  /**
   * checkAdjacentCaves() checks if there is a wumpus or a pit in adjacent caves
   * prints respective messages to the user
   * @param currentCave
   * @param roomWithWumpus
   * @param cavesWithPits
   */
  public void checkAdjacentCaves(Room currentCave, Room roomWithWumpus,
      ArrayList<Room> cavesWithPits){

    ArrayList<Room> adjacentCaves = currentCave.getAdjacentCaves();
    if (adjacentCaves.contains(roomWithWumpus)){
      System.out.println("You smell something terrible nearby...");
    }

    for (Room caveWithPit : cavesWithPits) {
      if (adjacentCaves.contains(caveWithPit)) {
        System.out.println("You feel a cold wind blowing...");
      }
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
    String input = sc.nextLine();
    //if move
    if (input.equals("m")){
      System.out.println("Where to?");
      makeMove(currentCave);
      //update current cave after move
      currentCave = player.getLocation();
      checkMoveForHazards(currentCave);
      //in case you get snatched away by superbats, update location
      currentCave = player.getLocation();
      System.out.println("You are now in cave #" + currentCave.getId());
    }
    else if (input.equals("s")){
      //validate input for number of caves
      boolean invalidInput = true;
      int numCaves = 0;
      while (invalidInput){
        System.out.println("No. of caves (1-5)?");
        String cavesInput = sc.nextLine();
        Matcher matcher = patternDigits.matcher(cavesInput);
        boolean matchFound = matcher.find();
        if (matchFound) {
          System.out.println("Your input is not a valid number");
        }
        else {
          numCaves = Integer.valueOf(cavesInput);
          if (numCaves >= 1 && numCaves <= 5){
            invalidInput = false;
          }
          else {
            System.out.println("Can't shoot that number. Please enter a number between 1 and 5");
          }
        }
      }
      System.out.println("In what direction?");
      //validate input
      invalidInput = true;
      String directionInput = null;
      while (invalidInput){
        directionInput = sc.nextLine();
        if (directionInput.equals("n") ||
            directionInput.equals("w") ||
            directionInput.equals("e") ||
            directionInput.equals("s")){
          invalidInput = false;
        }
        else {
          System.out.println("Illegal command. Please enter either e, w, s, n");
        }
      }
      Room targetCave = currentCave;
      for (int i = 0; i < numCaves; i++){
        if (directionInput.equals("s") && targetCave != null){
          targetCave = targetCave.findAdjacentCave(Direction.SOUTH);
        }
        else if (directionInput.equals("n") && targetCave != null){
          targetCave = targetCave.findAdjacentCave(Direction.NORTH);
        }
        else if (directionInput.equals("w") && targetCave != null){
          targetCave = targetCave.findAdjacentCave(Direction.WEST);
        }
        else if (directionInput.equals("e") && targetCave != null){
          targetCave = targetCave.findAdjacentCave(Direction.EAST);
        }
      }
      //targetCave is null if player specified larger distance than an arrow can travel
      if (targetCave != null){
        System.out.println("Your arrow reached cave #" + targetCave.getId());
        if (targetCave.getCaveType().contains(CaveType.WUMPUS)){
          wumpusKilled = true;
          System.out.println("Hee hee hee, you got the wumpus!");
          System.out.println("Next time you won't be so lucky\nGame Over");
          System.exit(0);
        }
        else {
          System.out.println("You missed...");
        }
      }
      else {
        System.out.println("You missed...");
      }
      player.decreaseArrows();
      int numArrows = player.getNumArrows();
      System.out.println("Your number of arrows is now: " + numArrows);
      if (numArrows == 0){
        System.out.println("Oh no! You ran out of arrows...\nGame Over");
        System.exit(0);
      }
      if (numArrows == 1){
        System.out.println("Be careful! If you miss next time, you lose");
      }

    } else {
      System.out.println("Illegal command. Please enter either e, w, s, n or q for exit");
    }
  }

  /**
   * makeMove() checks the input from the user for direction to move and moves the player
   * to new location
   * @param currentCave
   */
  public void makeMove(Room currentCave){
    Scanner sc = new Scanner(System.in);
    String input = sc.nextLine();
    ArrayList<Direction> possibleMoves = currentCave.getDirections();

    if (input.equals("n") && possibleMoves.contains(Direction.NORTH)){
      player.move(currentCave, Direction.NORTH);
    }
    else if (input.equals("s") && possibleMoves.contains(Direction.SOUTH)){
      player.move(currentCave, Direction.SOUTH);
    }
    else if (input.equals("w") && possibleMoves.contains(Direction.WEST)){
      player.move(currentCave, Direction.WEST);
    }
    else if (input.equals("e") && possibleMoves.contains(Direction.EAST)){
      player.move(currentCave, Direction.EAST);
    }
    else if (input.equals("q")){
      System.exit(0);
    }
    else {
      System.out.println("Illegal command. Please enter either e, w, s, n or q for exit");
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
      System.out.println("Oh no! You fell into the bottomless pit...");
      System.out.println("Better luck next time\nGame Over");
      System.exit(0);
    }

    //if entered a cave with wumpus and there are no superbats/superbats didn't work...
    if (currentCave.getCaveType().contains(CaveType.WUMPUS) && !superbatsWorked){
      System.out.println("Chomp chomp chomp... Thank you for feeding the wumpus!");
      System.out.println("Better luck next time\nGame Over");
      System.exit(0);
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
      if (randomIdx == 1) {
        System.out.println("Snatch - you are grabbed by superbats and ...");
        ArrayList<Room> roomsWithCaves = maze.getRoomsWithCaves();
        //generate newRoomId
        randomIdx = rand.nextInt(roomsWithCaves.size());
        Room newRoom = roomsWithCaves.get(randomIdx);
        player.setLocation(newRoom);
//        System.out.println("new location: " + player.getLocation().getId());
        checkMoveForHazards(newRoom);
        return true;
      }
      else {
        System.out.println("Whoa -- you successfully duck superbats that try to grab you");
      }
    }
    return false;
  }

}
