package hwt.menu;

import javax.swing.JButton;

public interface Menu {
  void show();
  void hide();
  JButton getStartGameBtn();
  JButton getRestartGameBtn();
  int getNumRows();
  int getNumCols();
  boolean isNonWrappingMaze();
  boolean isWrappingMaze();
  boolean isNewMaze();
  boolean isSameMaze();
  boolean isTextGame();
  boolean isGuiGame();
  int getNumPits();
  int getNumBats();
  int getNumArrows();

}
