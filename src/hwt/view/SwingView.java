package hwt.view;

import hwt.model.PerfectMaze;
import hwt.model.Player;
import hwt.model.Room;
import input.GameInput;
import input.KeyBoardHandler;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SwingView implements View {
  public static final int WIDTH = 500;
  public static final int HEIGHT = 500;
  private JFrame frame;
  private SwingPanel panel;

  public SwingView(GameInput input){
    frame = new JFrame("Hunt the Wumpus");
    panel = new SwingPanel((KeyBoardHandler) input);
    panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    frame.add(panel);
    frame.pack();
    frame.setVisible(true);
  }

  public void paint(List<Room> rooms, Room roomWithWumpus,
      List<Room> roomsWithBats, List<Room> roomsWithPits, Room playerLoc) {
    panel.paint(rooms, roomWithWumpus,
        roomsWithBats, roomsWithPits, playerLoc);
  }

  @Override
  public void repaintPlayer(Room playerLoc) {
    panel.repaintPlayer(playerLoc);
  }

  @Override
  public void repaintNearbyWumpus(Room nearby) {
    panel.repaintNearbyWumpus(nearby);
  }

  @Override
  public void repaintNearbyPit(Room nearby) {
    panel.repaintNearbyPit(nearby);
  }

}
