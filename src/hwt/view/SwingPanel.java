package hwt.view;

import hwt.input.Coordinates;
import hwt.Parameters;
import hwt.model.Direction;
import hwt.model.Room;
import hwt.input.KeyBoardHandler;
import hwt.input.MouseHandler;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * SwingPanel creates a customized panel that loads all the images and paints all components
 * registers MouseListener and KeyListener
 */

public class SwingPanel extends JPanel {

  private BufferedImage roombase_1Img;
  private BufferedImage roombase_3Img;
  private BufferedImage roombase_4Img;
  private BufferedImage hallway_straightImg;
  private BufferedImage hallwayImg;
  private BufferedImage wumpusImg;
  private BufferedImage superbatsImg;
  private BufferedImage pitImg;
  private BufferedImage pitNearbyImg;
  private BufferedImage wumpusNearbyImg;
  private BufferedImage targetImg;
  private BufferedImage playerImg;

  private List<Room> rooms;
  private Room caveWithWumpus;
  private List<Room> cavesWithBats;
  private List<Room> cavesWithPits;
  private Room playerLoc;
  private Room targetLoc;
  private boolean initialized = false;
  private boolean shoot = false;
  private List<Room> cavesNearbyWumpus;
  private List<Room> cavesNearbyPits;

  //initialized -> need to avoid calling paintComponent on uninitialized data (-> null pointer exception)
  // bc we don't transfer data we need in constructor but with paint call

  public SwingPanel(KeyBoardHandler input, MouseHandler inputMouse){
    //load images
    try {
      this.roombase_1Img = ImageIO.read(new File("./img/roombase-1.png"));
      this.roombase_3Img = ImageIO.read(new File("./img/roombase-3.png"));
      this.roombase_4Img = ImageIO.read(new File("./img/roombase-4.png"));
      this.hallway_straightImg = ImageIO.read(new File("./img/hallway-straight.png"));
      this.hallwayImg = ImageIO.read(new File("./img/hallway.png"));
      this.wumpusImg = ImageIO.read(new File("./img/wumpus.png"));
      this.superbatsImg = ImageIO.read(new File("./img/superbat.png"));
      this.pitImg = ImageIO.read(new File("./img/slime-pit.png"));
      this.pitNearbyImg = ImageIO.read(new File("./img/slime-pit-nearby.png"));
      this.wumpusNearbyImg = ImageIO.read(new File("./img/wumpus-nearby.png"));
      this.targetImg = ImageIO.read(new File("./img/target.png"));
      this.playerImg = ImageIO.read(new File("./img/player.png"));

    } catch (IOException e) {
      System.out.println("File doesn't exist");
    }
    //register mouse handler
    addMouseListener(inputMouse);

    // register the keyboard handler
    addKeyListener(input);
    this.setFocusable(true);
  }

  public void paint(List<Room> rooms, Room caveWithWumpus,
      List<Room> cavesWithBats, List<Room> cavesWithPits, List<Room> cavesNearbyPits,
      List<Room> cavesNearbyWumpus,
      Room playerLoc){
    this.rooms = rooms;
    this.caveWithWumpus = caveWithWumpus;
    this.cavesWithBats = cavesWithBats;
    this.cavesWithPits = cavesWithPits;
    this.cavesNearbyPits = cavesNearbyPits;
    this.cavesNearbyWumpus = cavesNearbyWumpus;
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
      if ( !currentRoom.isVisited() ) {
        continue;
      }
      List<Direction> directions = currentRoom.getDirections();
      int numDoors = currentRoom.getNumDoors();
      BufferedImage img = getImage(numDoors, directions);
      int degrees = getRotationAngle(directions);
      Coordinates coordCurrentRoom = getCoordinates(currentRoom, degrees);

      AffineTransform t = new AffineTransform();
      t.translate(coordCurrentRoom.getX(), coordCurrentRoom.getY());
      t.rotate(Math.toRadians(degrees));
      g2d.drawImage(img, t, this);
    }


    for (int i = 0; i < this.cavesWithPits.size(); i++){
      Room caveWithPit = this.cavesWithPits.get(i);
      if ( caveWithPit.isVisited() ) {
        Coordinates coordPit = getCoordinates(caveWithPit, 0);
        g2d.drawImage(this.pitImg, coordPit.getX(), coordPit.getY(), this);
      }
    }

    if ( caveWithWumpus.isVisited() ) {
      Coordinates coordWumpus = getCoordinates(this.caveWithWumpus, 0);
      int wumpusShift = (Parameters.ROOM_SIZE - Parameters.WUMPUS_SIZE) / 2;
      g2d.drawImage(this.wumpusImg, coordWumpus.getX() + wumpusShift,
          coordWumpus.getY() + wumpusShift, this);
    }

   for (Room caveNearbyWumpus: this.cavesNearbyWumpus){
     if ( caveNearbyWumpus.isVisited() ) {
       Coordinates coordCaveNearbyWumpus = getCoordinates(caveNearbyWumpus, 0);
       g2d.drawImage(this.wumpusNearbyImg, coordCaveNearbyWumpus.getX(),
           coordCaveNearbyWumpus.getY(), this);
     }
    }

   for (Room caveNearbyPit: this.cavesNearbyPits){
     if ( caveNearbyPit.isVisited() ) {
       Coordinates coordCaveNearbyPit = getCoordinates(caveNearbyPit, 0);
       g2d.drawImage(this.pitNearbyImg, coordCaveNearbyPit.getX(), coordCaveNearbyPit.getY(), this);
     }
   }

    for (int i = 0; i < this.cavesWithBats.size(); i++){
      Room caveWithBats = this.cavesWithBats.get(i);
      if ( caveWithBats.isVisited() ){
        Coordinates coordBats = getCoordinates(caveWithBats, 0);
        int batsShift = (Parameters.ROOM_SIZE - Parameters.BATS_SIZE)/2;
        g2d.drawImage(this.superbatsImg, coordBats.getX() + batsShift,
            coordBats.getY() + batsShift, this);
      }
    }

    Coordinates coordPlayer = getCoordinates(this.playerLoc, 0);
    int playerShift = (Parameters.ROOM_SIZE - Parameters.PLAYER_SIZE)/2;
    g2d.drawImage(this.playerImg, coordPlayer.getX() + playerShift,
        coordPlayer.getY() + playerShift, this);

    if (shoot){
      Coordinates coordTarget = getCoordinates(this.targetLoc, 0);
      g2d.drawImage(this.targetImg, coordTarget.getX(),
          coordTarget.getY(), this);
    }

  }

  private BufferedImage getImage(int numDoors, List<Direction> directions) throws IllegalArgumentException{
    switch(numDoors) {
      case 1:
        return this.roombase_1Img;
      case 2:
        if ((directions.contains(Direction.NORTH) && directions.contains(Direction.SOUTH))
          || (directions.contains(Direction.EAST) && directions.contains(Direction.WEST))){
          return this.hallway_straightImg;
        } else {
          return this.hallwayImg;
        }
      case 3:
        return this.roombase_3Img;
      case 4:
        return this.roombase_4Img;
      default:
        throw new IllegalArgumentException("No such number of doors");
    }
  }

  /**
   * getRotationAngle() - helper function to calculate the rotational angle for a room image
   *
   * @param directions
   * @return
   */
  private int getRotationAngle(List<Direction> directions){
    int numDoors = directions.size();
    if (numDoors == 2){
      // original Tunnel image with East West doors -> North South
      if (directions.contains(Direction.NORTH) && directions.contains(Direction.SOUTH)){
        return 90;
      }
      // original crooked Tunnel image with South East doors -> West South
      if (directions.contains(Direction.WEST) && directions.contains(Direction.SOUTH)){
        return 90;
      }
      // original crooked Tunnel image with South East doors -> West North
      if (directions.contains(Direction.WEST) && directions.contains(Direction.NORTH)){
        return 180;
      }
      // original crooked Tunnel image with South East doors -> East North
      if (directions.contains(Direction.EAST) && directions.contains(Direction.NORTH)){
        return 270;
      }
    }
    //rotate roombase
    if ( numDoors == 1 ){
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
    if ( numDoors == 3 ){
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
    int y = currentRoom.getRowId() * Parameters.ROOM_SIZE;
    int x = currentRoom.getColId() * Parameters.ROOM_SIZE;

    switch(degrees){
      case 90:
        x += Parameters.ROOM_SIZE;
        break;
      case 180:
        x += Parameters.ROOM_SIZE;
        y += Parameters.ROOM_SIZE;
        break;
      case 270:
        y += Parameters.ROOM_SIZE;
    }
    return new Coordinates(x, y);
  }

  public void repaintPlayer(Room playerLoc) {
    this.playerLoc = playerLoc;
    repaint();
  }

  public void paintTarget(Room targetLoc) {
    this.shoot = true;
    this.targetLoc = targetLoc;
    repaint();
  }


  public void paintAfterShooting(Room revealRoom){
    this.shoot = false;
    revealRoom.setVisited(true);
    repaint();
  }

  public void paintResetShoot(){
    this.shoot = false;
    repaint();
  }
}
