package hwt.input;

import hwt.Parameters;
import hwt.model.Direction;
import hwt.model.Room;
import java.util.HashMap;
import java.util.Map.Entry;

public class Coordinates{
  private int x;
  private int y;

  public Coordinates(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Direction getDirectionFromMouseClick(Room playerLocation ) {
    int clickedRowId = this.y / Parameters.ROOM_SIZE;
    int clickedColId = this.x / Parameters.ROOM_SIZE;

    for (HashMap<Direction, Room> directionRoomHashMap : playerLocation.getDirectionsToAdjacentRooms()) {
      for (Entry entry : directionRoomHashMap.entrySet()) {
        Room adjacentRoom = (Room) entry.getValue();
        if (adjacentRoom.getRowId() == clickedRowId && adjacentRoom.getColId() == clickedColId) {
          return (Direction) entry.getKey();
        }
      }
    }
    return null;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }
}
