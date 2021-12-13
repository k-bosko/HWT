package hwt.view;

import hwt.model.CaveType;
import hwt.model.Direction;
import hwt.model.Room;
import input.KeyBoardHandler;
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
  private static final int PLAYER_SIZE = 15;

  private BufferedImage roombase_1;
  private BufferedImage roombase_3;
  private BufferedImage roombase_4;
  private BufferedImage hallway_straight;
  private BufferedImage hallway;
  private BufferedImage wumpus;
  private BufferedImage superbats;
  private BufferedImage pit;
  private BufferedImage pitNearby;
  private BufferedImage wumpusNearby;
  private BufferedImage target;
  private BufferedImage playerImg;

  private List<Room> rooms;
  private Room caveWithWumpus;
  private List<Room> cavesWithBats;
  private List<Room> cavesWithPits;
  private Room playerLoc;
  private boolean initialized = false;
  private Room caveNearbyWumpus;
  private Room caveNearbyPit;

  //initialized -> need to avoid calling paintComponent on uninitialized data (-> null pointer exception)
  // bc we don't transfer data we need in constructor but with paint call

  public SwingPanel(KeyBoardHandler input){
    //load images
    try {
      this.roombase_1 = ImageIO.read(new File("./img/roombase-1.png"));
      this.roombase_3 = ImageIO.read(new File("./img/roombase-3.png"));
      this.roombase_4 = ImageIO.read(new File("./img/roombase-4.png"));
      this.hallway_straight = ImageIO.read(new File("./img/hallway-straight.png"));
      this.hallway = ImageIO.read(new File("./img/hallway.png"));
      this.wumpus = ImageIO.read(new File("./img/wumpus.png"));
      this.superbats = ImageIO.read(new File("./img/superbat.png"));
      this.pit = ImageIO.read(new File("./img/slime-pit.png"));
      this.pitNearby = ImageIO.read(new File("./img/slime-pit-nearby.png"));
      this.wumpusNearby = ImageIO.read(new File("./img/wumpus-nearby.png"));
      this.target = ImageIO.read(new File("./img/target.png"));
      this.playerImg = ImageIO.read(new File("./img/player.png"));

    } catch (IOException e) {
      System.out.println("File doesn't exist");
    }

    // register the keyboard handler
    addKeyListener(input);
    this.setFocusable(true);

  }

  public void paint(List<Room> rooms, Room caveWithWumpus,
      List<Room> cavesWithBats, List<Room> cavesWithPits, Room playerLoc){
    this.rooms = rooms;
    this.caveWithWumpus = caveWithWumpus;
    this.cavesWithBats = cavesWithBats;
    this.cavesWithPits = cavesWithPits;
    this.playerLoc = playerLoc;
    this.initialized = true;

  };

  @Override
  protected void paintComponent(Graphics g){
                                //^^ for canvas
    super.paintComponent(g);

    if (!initialized) {
      return;
    }

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

    for (int i = 0; i < this.cavesWithPits.size(); i++){
      Coordinates coordPit = getCoordinates(this.cavesWithPits.get(i), 0);
      g2d.drawImage(this.pit, coordPit.getX(), coordPit.getY(), this);
    }

    Coordinates coordWumpus = getCoordinates(this.caveWithWumpus, 0);
    int wumpusShift = (ROOM_SIZE - WUMPUS_SIZE)/2;
    g2d.drawImage(this.wumpus, coordWumpus.getX() + wumpusShift,
        coordWumpus.getY() + wumpusShift, this);

    for (int i = 0; i < this.cavesWithBats.size(); i++){
      Coordinates coordBats = getCoordinates(this.cavesWithBats.get(i), 0);
      int batsShift = (ROOM_SIZE - BATS_SIZE)/2;
      g2d.drawImage(this.superbats, coordBats.getX() + wumpusShift,
          coordBats.getY() + wumpusShift, this);
    }

    if (this.caveNearbyWumpus != null){
      Coordinates coordCaveNearbyWumpus = getCoordinates(this.caveNearbyWumpus, 0);
      g2d.drawImage(this.wumpusNearby, coordCaveNearbyWumpus.getX(), coordCaveNearbyWumpus.getY(), this);
    }
   if (this.caveNearbyPit != null){
     Coordinates coordCaveNearbyPit = getCoordinates(this.caveNearbyPit, 0);
     g2d.drawImage(this.pitNearby, coordCaveNearbyPit.getX(), coordCaveNearbyPit.getY(), this);
   }

    Coordinates coordPlayer = getCoordinates(this.playerLoc, 0);
    int playerShift = (ROOM_SIZE - PLAYER_SIZE)/2;
    g2d.drawImage(this.playerImg, coordPlayer.getX() + playerShift,
        coordPlayer.getY() + playerShift, this);

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

  public void repaintPlayer(Room playerLoc) {
    this.playerLoc = playerLoc;
    repaint();
  }

  public void repaintNearbyWumpus(Room caveNearby) {
    this.caveNearbyWumpus = caveNearby;
    repaint();
  }

  public void repaintNearbyPit(Room caveNearby){
    this.caveNearbyPit = caveNearby;
    repaint();
  }

}
