package hwt.view;

import hwt.model.PerfectMaze;
import hwt.model.Player;
import hwt.model.Room;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SwingView implements View {
  public static final int WIDTH = 500;
  public static final int HEIGHT = 500;
  private JFrame frame;

  public SwingView(){
    frame = new JFrame("Hunt the Wumpus");
  }
  @Override
  public void paint(PerfectMaze maze, Player player) {
    SwingPanel panel = new SwingPanel(maze, player);
    panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    frame.add(panel);
    frame.pack();
    frame.setVisible(true);
  }
}
