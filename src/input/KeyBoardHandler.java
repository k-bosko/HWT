package input;

import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_UP;

import hwt.Parameters;
import hwt.model.Direction;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyBoardHandler implements KeyListener, GameInput {
  private Direction moveDirection;
  private Direction shootDirection;
  private boolean shoot = false;
  private boolean shot = false;

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    if (!shoot) {
      this.moveDirection = registerDirection(key);
    }
    if (shoot){
      this.shootDirection = registerDirection(key);
    }
    if (key == KeyEvent.VK_S) {
      this.shoot = true;
    }
    if (key == KeyEvent.VK_ENTER) {
      this.shoot = false;
      this.shot = true;
    }
    if (key == KeyEvent.VK_ESCAPE) {
      this.shoot = false;
      this.shot = false;
    }
    else {
      //ignore
    }

  }

  private Direction registerDirection(int key){
    if (key == VK_DOWN) {
      return Direction.SOUTH;
    } else if (key == VK_UP) {
      return Direction.NORTH;
    } else if (key == VK_LEFT) {
      return Direction.WEST;
    } else if (key == VK_RIGHT) {
      return Direction.EAST;
    }
    return null;
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }

  @Override
  public Direction getMoveDirection() {
    return this.moveDirection;
  }

  @Override
  public boolean getShootStatus() {
    return this.shoot;
  }

  @Override
  public boolean isShot(){
    return this.shot;
  }

  @Override
  public Direction getShootDirection() {
    return this.shootDirection;
  }


  //TODO add record file functionality or delete isKeyboard
  @Override
  public boolean isKeyboard() {
    return true;
  }

  @Override
  public void resetMoveDirection(){
    this.moveDirection = null;
  }

  @Override
  public void resetShootDirection(){
    this.shootDirection = null;
  }

  @Override
  public void resetShooting() {this.shot = false;}
}
