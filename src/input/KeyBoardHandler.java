package input;

import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_UP;

import hwt.model.Direction;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyBoardHandler implements KeyListener, GameInput {
  private Direction targetDir;

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    if (key == VK_DOWN) {
      targetDir = Direction.SOUTH;
    } else if (key == VK_UP) {
      targetDir = Direction.NORTH;
    } else if (key == VK_LEFT) {
      targetDir = Direction.WEST;
    } else if (key == VK_RIGHT) {
      targetDir = Direction.EAST;
    } else {
      // ignore
    }

  }

  @Override
  public void keyReleased(KeyEvent e) {
  }

  @Override
  public Direction getDirection() {
    return this.targetDir;
  }


  //TODO add record file functionality or delete isKeyboard
  @Override
  public boolean isKeyboard() {
    return true;
  }

  public void resetDirection(){
    this.targetDir = null;
  }
}
