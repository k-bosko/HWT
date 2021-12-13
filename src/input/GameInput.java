package input;

import hwt.model.Direction;

public interface GameInput {

  Direction getMoveDirection();
  boolean getShootStatus();
  Direction getShootDirection();
  boolean isKeyboard();
  void resetMoveDirection();
  void resetShootDirection();
}
