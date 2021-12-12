package hwt.model;

import java.util.ArrayList;

public interface Maze {
    void setStart(int start);
    int getStart();
    ArrayList<Room> getRooms();
}
