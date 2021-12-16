package hwt.view;

import hwt.Parameters;
import hwt.model.Room;
import input.GameInput;
import input.KeyBoardHandler;
import input.MouseHandler;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollPane;


public class SwingView implements View {
  private JFrame frame;
  private SwingPanel panel;

  public SwingView(GameInput input, MouseHandler inputMouse, int numRows, int numCols){
    frame = new JFrame("Hunt the Wumpus");
    frame.setSize(Parameters.WINDOW_WIDTH, Parameters.WINDOW_HEIGHT);
    frame.setLayout(new GridLayout(1, 1));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    frame.setResizable(false);

    panel = new SwingPanel((KeyBoardHandler) input, inputMouse);
    int width = numRows * Parameters.ROOM_SIZE;
    int height = numCols * Parameters.ROOM_SIZE;
    panel.setPreferredSize(new Dimension(width, height));

    JScrollPane scrollPane = new JScrollPane(panel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    frame.add(scrollPane);
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

  public void paintTarget(Room targetLoc) {
    panel.paintTarget(targetLoc);
  }

  public void paintAfterShooting(Room revealRoom){
    panel.paintAfterShooting(revealRoom);
  }

  public void paintResetShoot(){ panel.paintResetShoot();}

}
