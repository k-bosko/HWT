package hwt.view;

import hwt.model.PerfectMaze;
import hwt.model.Player;
import hwt.model.Room;
import java.util.ArrayList;

public interface View {
  void paint(PerfectMaze maze, Player player);
}
