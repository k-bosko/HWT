package hwt;

import hwt.controller.Controller;
import hwt.model.MazeBuilder;
import hwt.model.PerfectMaze;
import hwt.model.Player;
import hwt.model.Room;
import hwt.view.SwingView;
import hwt.view.View;
import input.GameInput;
import input.KeyBoardHandler;

public class Driver {

  public static void main(String[] args) throws ArrayIndexOutOfBoundsException{
    //default for arrows
    int numArrows = Parameters.NUM_ARROWS;

    if (args.length < 3) {
      throw new ArrayIndexOutOfBoundsException("Please specify number of rows, number of columns "
          + "and maze type - non-wrapping or wrapping");
    }
    int numRows = Integer.parseInt(args[0]);
    int numCols = Integer.parseInt(args[1]);
    String mazeType = args[2];

    MazeBuilder mazeBuilder = new MazeBuilder(numRows, numCols, mazeType);
    if (args.length == 4){
      int numPits = Integer.parseInt(args[3]);
      mazeBuilder.specifyPitsNumber(numPits);
    }

    if (args.length == 5){
      int numPits = Integer.parseInt(args[3]);
      mazeBuilder.specifyPitsNumber(numPits);

      int numBats = Integer.parseInt(args[4]);
      mazeBuilder.specifyBatsNumber(numBats);
    }

    if (args.length == 6){
      int numPits = Integer.parseInt(args[3]);
      mazeBuilder.specifyPitsNumber(numPits);

      int numBats = Integer.parseInt(args[4]);
      mazeBuilder.specifyBatsNumber(numBats);

      numArrows = Integer.parseInt(args[5]);

    }

    PerfectMaze maze = mazeBuilder.build();

    //TODO rethink implementation of start ? -> 1. either change start from int to Room or 2. change Player constructor and move from Room to int
    int start = maze.getStart();
    Room startCave = maze.getRoomBy(start);
    Player player = new Player(startCave, numArrows);

    //switch between GUI and TEXT game modes
    GameType gameType = GameType.GUI;

    //    maze.printWalls();
//    maze.printRoomsInfo();

    if (gameType == GameType.GUI){
      GameInput input = new KeyBoardHandler();
      View view = new SwingView(input);

      Controller c = new Controller(maze, player, view, input);
      c.start(GameType.GUI);
    }
    else {
      Controller c = new Controller(maze, player);
      c.start(GameType.TEXT);
    }




  }
}
