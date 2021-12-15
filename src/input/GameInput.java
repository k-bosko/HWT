package input;

import hwt.model.Direction;

public interface GameInput {
  Direction getMoveDirection();
  void resetMoveDirection();

  boolean getShootStatus();
  Direction getShootDirection();
  void resetShootDirection();
  boolean isShot();
  void resetShooting();
}
