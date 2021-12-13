package hwt.view;

import hwt.model.PerfectMaze;
import hwt.model.Player;
import hwt.model.Room;
import java.util.ArrayList;
import java.util.List;

public interface View {
  void paint(List<Room> rooms, Room roomWithWumpus,
      List<Room> roomsWithBats, List<Room> roomsWithPits, Room playerLoc);

  void repaintPlayer(Room playerLoc);
  void repaintNearbyPit(Room nearby);
  void repaintNearbyWumpus(Room nearby);
}
