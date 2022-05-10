package hwt;

import hwt.controller.Controller;
import hwt.menu.MenuParamsFileHandler;
import hwt.model.MazeBuilder;
import hwt.model.PerfectMaze;
import hwt.model.Player;
import hwt.model.Room;
import hwt.menu.MenuPanel;
import hwt.menu.Menu;
import hwt.view.SwingView;
import hwt.view.View;
import hwt.input.GameInput;
import hwt.input.KeyBoardHandler;
import hwt.input.MouseHandler;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Driver {
  private static int numRows;
  private static int numCols;
  private static boolean isWrapping;
  private static boolean isNonWrapping;
  private static int numPits;
  private static int numBats;
  private static boolean isSeed;
  private static int numArrows;
  private static boolean isGuiGame;
  private static boolean isTextGame;

  private static MazeBuilder mazeBuilder;

  public static void readMenuParameters(Menu menu){
    numRows = menu.getNumRows();
    numCols = menu.getNumCols();
    isWrapping = menu.isWrappingMaze();
    isNonWrapping = menu.isNonWrappingMaze();
    numPits = menu.getNumPits();
    numBats = menu.getNumBats();
    isSeed = menu.isSameMaze();
    numArrows = menu.getNumArrows();
    isGuiGame = menu.isGuiGame();
    isTextGame = menu.isTextGame();
  }

  public static void writeMenuParameters(){
    MenuParamsFileHandler fileHandler = new MenuParamsFileHandler();
    try{
      System.out.println("in writer - Text game - " + isTextGame);
      System.out.println("in writer - GUI game - " + isGuiGame);
      fileHandler.writeToFile(numRows, numCols, isNonWrapping, numPits, numBats, isGuiGame, isTextGame, numArrows);

    }
    catch (IOException err){
      System.out.println("error when writing to file");
    }
  }

  public static void main(String[] args) throws ArrayIndexOutOfBoundsException {

    Menu menu = new MenuPanel();
    menu.show();

    menu.getStartGameBtn().addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        menu.hide();
        readMenuParameters(menu);
        writeMenuParameters();
        startGame();
      }
    });

    menu.getRestartGameBtn().addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        menu.hide();
        MenuParamsFileHandler fileHandler = new MenuParamsFileHandler();
        try {
          fileHandler.readFromFile();
        } catch (Exception error) {
          System.out.println("file not found");
        }
        readMenuParameters(fileHandler);
        startGame();
      }
    });


  }

  private static void startGame() {
    mazeBuilder = new MazeBuilder(numRows, numCols, isNonWrapping, isWrapping, isSeed);
    mazeBuilder.specifyPitsNumber(numPits);
    mazeBuilder.specifyBatsNumber(numBats);
    PerfectMaze maze = mazeBuilder.build();

    int start = maze.getStart();
    Room startCave = maze.getRoomBy(start);
    Player player = new Player(startCave, numArrows);

    //switch between GUI and TEXT game modes
    if (isGuiGame) {
      GameInput inputKeyboard = new KeyBoardHandler();
      MouseHandler inputMouse = new MouseHandler();
      View view = new SwingView(inputKeyboard, inputMouse, maze.getNumRows(),
          maze.getNumCols());

      Controller c = new Controller(maze, player, view, inputKeyboard, inputMouse);
      c.start(GameType.GUI);
    } else if (isTextGame) {
      Controller c = new Controller(maze, player);
      c.start(GameType.TEXT);
    } else {
      //ignore
    }
    //for debugging
    //    maze.printWalls();
    //    maze.printRoomsInfo();
  }
}
