package hwt.view;


import hwt.model.Room;
import java.util.List;

public interface View {
  void paint(List<Room> rooms, Room roomWithWumpus,
      List<Room> roomsWithBats, List<Room> roomsWithPits,
      List<Room> cavesNearbyPits, List<Room> cavesNearbyWumpus, Room playerLoc);

  void repaintPlayer(Room playerLoc);
  void paintTarget(Room targetLoc);
}
