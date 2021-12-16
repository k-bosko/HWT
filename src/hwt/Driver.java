package hwt;

import hwt.controller.Controller;
import hwt.model.MazeBuilder;
import hwt.model.PerfectMaze;
import hwt.model.Player;
import hwt.model.Room;
import hwt.view.MenuPanel;
import hwt.view.SwingView;
import hwt.view.View;
import input.GameInput;
import input.KeyBoardHandler;
import input.MouseHandler;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Driver {

  private static MazeBuilder mazeBuilder;

  public static void main(String[] args) throws ArrayIndexOutOfBoundsException {

    MenuPanel menu = new MenuPanel();
    menu.show();

    menu.getStartGameBtn().addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        menu.hide();
        int numRows = menu.getNumRows();
        int numCols = menu.getNumCols();

        boolean isWrappingMaze = menu.isWrappingMaze();
        boolean isNonWrapping = menu.isNonWrappingMaze();
        int numPits = menu.getNumPits();
        int numBats = menu.getNumBats();
        boolean isSeed = menu.isSameMaze();

        mazeBuilder = new MazeBuilder(numRows, numCols, isNonWrapping, isWrappingMaze, isSeed);
        mazeBuilder.specifyPitsNumber(numPits);
        mazeBuilder.specifyBatsNumber(numBats);

        PerfectMaze maze = mazeBuilder.build();

        int start = maze.getStart();
        Room startCave = maze.getRoomBy(start);
        Player player = new Player(startCave, menu.getNumArrows());

        //switch between GUI and TEXT game modes
        if (menu.isGuiGame()) {
          GameInput inputKeyboard = new KeyBoardHandler();
          MouseHandler inputMouse = new MouseHandler();
          View view = new SwingView(inputKeyboard, inputMouse, maze.getNumRows(),
              maze.getNumCols());

          Controller c = new Controller(maze, player, view, inputKeyboard, inputMouse);
          c.start(GameType.GUI);
        } else if (menu.isTextGame()) {
          Controller c = new Controller(maze, player);
          c.start(GameType.TEXT);
        } else {
          //ignore
        }

        //for debugging
        //    maze.printWalls();
        //    maze.printRoomsInfo();
      }
    });

//    menu.getRestartBtn().addActionListener(new ActionListener() {
//
//      @Override
//      public void actionPerformed(ActionEvent e) {
//
//      }
//    });
  }
}
