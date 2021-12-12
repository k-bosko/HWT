package hwt.view;

import hwt.model.Direction;
import hwt.model.PerfectMaze;
import hwt.model.Player;
import hwt.model.Room;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JPanel;


//need a panel object upon which to draw
//Graphics object = pen
//need to override paintComponent method


public class SwingPanel extends JPanel {
  //data class to hold coordinates
  private class Coordinates{
    private int x;
    private int y;

    public Coordinates(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public int getX() {
      return x;
    }

    public int getY() {
      return y;
    }
  }
  private static final int ROOM_SIZE = 64;
  private static final int WUMPUS_SIZE = 32;
  private static final int BATS_SIZE = 30;
  private BufferedImage roombase_1;
  private BufferedImage roombase_3;
  private BufferedImage roombase_4;
  private BufferedImage hallway_straight;
  private BufferedImage hallway;
  private BufferedImage wumpus;
  private BufferedImage superbats;
  private BufferedImage pit;
  private BufferedImage player;

  private List<Room> rooms;
  private Room roomWithWumpus;
  private List<Room> roomsWithBats;

  public SwingPanel(PerfectMaze maze, Player player){
    //load images
    this.rooms = maze.getRooms();
    this.roomWithWumpus = maze.getCaveWithWumpus();
    this.roomsWithBats = maze.getCavesWithBats();


    try {
      this.roombase_1 = ImageIO.read(new File("./img/roombase-1.png"));
      this.roombase_3 = ImageIO.read(new File("./img/roombase-3.png"));
      this.roombase_4 = ImageIO.read(new File("./img/roombase-4.png"));
      this.hallway_straight = ImageIO.read(new File("./img/hallway-straight.png"));
      this.hallway = ImageIO.read(new File("./img/hallway.png"));
      this.player = ImageIO.read(new File("./img/player-original.png"));
      this.wumpus = ImageIO.read(new File("./img/wumpus.png"));
      this.superbats = ImageIO.read(new File("./img/superbat.png"));
      this.pit = ImageIO.read(new File("./img/slime-pit.png"));

    } catch (IOException e) {
      System.out.println("File doesn't exist");
    }
  }

  @Override
  protected void paintComponent(Graphics g){
                                //^^ for canvas
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    //show maze room by room
    for (int i = 0; i < this.rooms.size(); i++){
      Room currentRoom = rooms.get(i);
        //TODO implement in Room visited; all false in constructor
//      if ( !currentRoom.isVisited() ) {
//        continue;
//      }
      List<Direction> directions = currentRoom.getDirections();
      int numDoors = currentRoom.getNumDoors();
      BufferedImage img = getImage(numDoors, directions);
      int degrees = getRotationAngle(directions);
      Coordinates coord = getCoordinates(currentRoom, degrees);

      AffineTransform t = new AffineTransform();
      t.translate(coord.getX(), coord.getY());
      t.rotate(Math.toRadians(degrees));
      g2d.drawImage(img, t, this);
    }

    Coordinates coordWumpus = getCoordinates(this.roomWithWumpus, 0);
    int wumpusShift = (ROOM_SIZE - WUMPUS_SIZE)/2;
    g2d.drawImage(this.wumpus, coordWumpus.getX() + wumpusShift,
        coordWumpus.getY() + wumpusShift, this);

    for (int i = 0; i < this.roomsWithBats.size(); i++){
      Coordinates coordBats = getCoordinates(this.roomsWithBats.get(i), 0);
      int batsShift = (ROOM_SIZE - BATS_SIZE)/2;
      g2d.drawImage(this.superbats, coordWumpus.getX() + wumpusShift,
          coordWumpus.getY() + wumpusShift, this);
    }

//    if (currentRoom.getCaveType().contains(CaveType.SUPERBATS)){
//      g2d.drawImage(this.superbats, coord.getX(), coord.getY(), this);
//    }
//    if (currentRoom.getCaveType().contains(CaveType.PIT)){
//      g2d.drawImage(this.pit, coord.getX(), coord.getY(), this);
//    }
  }

  private BufferedImage getImage(int numDoors, List<Direction> directions) throws IllegalArgumentException{
    switch(numDoors) {
      case 1:
        return this.roombase_1;
      case 2:
        if ((directions.contains(Direction.NORTH) && directions.contains(Direction.SOUTH))
          || (directions.contains(Direction.EAST) && directions.contains(Direction.WEST))){
          return this.hallway_straight;
        } else {
          return this.hallway;
        }
      case 3:
        return this.roombase_3;
      case 4:
        return this.roombase_4;
      default:
        throw new IllegalArgumentException("No such number of doors");
    }
  }

  private int getRotationAngle(List<Direction> directions){
    int numDoors = directions.size();
    if (numDoors == 2){
      // original === transform into ||
      if (directions.contains(Direction.NORTH) && directions.contains(Direction.SOUTH)){
        return 90;
      }
      // original South East -> West South
      if (directions.contains(Direction.WEST) && directions.contains(Direction.SOUTH)){
        return 90;
      }
      // original South East -> West North
      if (directions.contains(Direction.WEST) && directions.contains(Direction.NORTH)){
        return 180;
      }
      // original South East -> East North
      if (directions.contains(Direction.EAST) && directions.contains(Direction.NORTH)){
        return 270;
      }
    }
    if (numDoors == 1 ){
      if (directions.contains(Direction.SOUTH)) {
        return 90;
      }
      if (directions.contains(Direction.WEST)) {
        return 180;
      }
      if (directions.contains(Direction.NORTH)) {
        return 270;
      }
    }
    if (numDoors == 3 ){
      if (directions.contains(Direction.SOUTH) && directions.contains(Direction.WEST)
          && directions.contains(Direction.NORTH)) {
        return 90;
      }
      if (directions.contains(Direction.EAST) && directions.contains(Direction.WEST)
          && directions.contains(Direction.NORTH)) {
        return 180;
      }
      if (directions.contains(Direction.SOUTH) && directions.contains(Direction.EAST)
          && directions.contains(Direction.NORTH)) {
        return 270;
      }
    }
    return 0;
  }

  private Coordinates getCoordinates(Room currentRoom, int degrees){
    int y = currentRoom.getRowId() * ROOM_SIZE;
    int x = currentRoom.getColId() * ROOM_SIZE;

    switch(degrees){
      case 90:
        x += ROOM_SIZE;
        break;
      case 180:
        x += ROOM_SIZE;
        y += ROOM_SIZE;
        break;
      case 270:
        y += ROOM_SIZE;
    }
    return new Coordinates(x, y);
  }
}
