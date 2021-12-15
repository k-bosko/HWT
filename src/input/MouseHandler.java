package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandler implements MouseListener {
  private Coordinates mouseInputCoords;

  @Override
  public void mouseClicked(MouseEvent e) {

  }

  @Override
  public void mousePressed(MouseEvent e) {
    int clickX = e.getX();
    int clickY = e.getY();

    mouseInputCoords = new Coordinates(clickX, clickY);
  }

  @Override
  public void mouseReleased(MouseEvent e) {

  }

  @Override
  public void mouseEntered(MouseEvent e) {

  }

  @Override
  public void mouseExited(MouseEvent e) {

  }

  public Coordinates getMouseInputCoords(){
    return this.mouseInputCoords;
  }

  public void resetMouseInputCoords(){
    this.mouseInputCoords = null;
  }
}
