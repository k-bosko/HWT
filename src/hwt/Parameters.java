package hwt;

import java.util.regex.Pattern;

public class Parameters {
  //view.SwingView
  public static final int WINDOW_WIDTH = 700;
  public static final int WINDOW_HEIGHT = 700;
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
  public static final int NUM_ROWS = 4;
  public static final int NUM_COLS = 6;

  public static final Integer NUM_ROWS_ARR[] = {3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
  public static final Integer NUM_COLS_ARR[] = {3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
  public static final Integer NUM_ARROWS_ARR[] = {1, 2, 3 ,4, 5};
  public static final Integer NUM_SUPERBATS_ARR[] = {1, 2, 3 ,4, 5, 6, 7, 8, 9, 10};
  public static final Integer NUM_PITS_ARR[] = {1, 2, 3 ,4, 5, 6, 7, 8, 9, 10};
}

