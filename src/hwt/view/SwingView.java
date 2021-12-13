package hwt.view;

import hwt.Parameters;
import hwt.model.Room;
import input.GameInput;
import input.KeyBoardHandler;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JFrame;

public class SwingView implements View {
  private JFrame frame;
  private SwingPanel panel;

  public SwingView(GameInput input){
    frame = new JFrame("Hunt the Wumpus");
    panel = new SwingPanel((KeyBoardHandler) input);
    panel.setPreferredSize(new Dimension(Parameters.WINDOW_WIDTH, Parameters.WINDOW_HEIGHT));
    frame.add(panel);
    frame.pack();
    frame.setVisible(true);
  }

  public void paint(List<Room> rooms, Room caveWithWumpus,
      List<Room> cavesWithBats, List<Room> cavesWithPits,
      List<Room> cavesNearbyPits, List<Room> cavesNearbyWumpus,Room playerLoc) {
    panel.paint(rooms, caveWithWumpus,
        cavesWithBats, cavesWithPits, cavesNearbyPits, cavesNearbyWumpus, playerLoc);
  }

  @Override
  public void repaintPlayer(Room playerLoc) {
    panel.repaintPlayer(playerLoc);
  }

}
