package hwt;

import java.util.regex.Pattern;

public class Parameters {
  //view.SwingView
  public static final int WINDOW_WIDTH = 500;
  public static final int WINDOW_HEIGHT = 500;
  //view.SwingPanel
  //size of images to calculate coordinates
  public static final int ROOM_SIZE = 64;
  public static final int WUMPUS_SIZE = 32;
  public static final int BATS_SIZE = 30;
  public static final int PLAYER_SIZE = 15;
  //controller
  public static final int TIMER_PERIOD = 150;
  public static final Pattern PATTERN_DIGITS = Pattern.compile("[^\\d]");
  //driver
  public static final int NUM_ARROWS = 2;
}
