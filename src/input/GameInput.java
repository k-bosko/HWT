package input;

import hwt.model.Direction;

public interface GameInput {

  Direction getDirection();
  boolean isKeyboard();
  void resetDirection();
}
