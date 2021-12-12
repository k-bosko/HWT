package hwt.model;

/**
 * Wall - class that creates a wall between two rooms
 * each wall has source room and destination room between which the wall is built
 */
public class Wall {
  protected int source;
  protected int destination;

  //constructor
  public Wall (int source, int destination){
    this.source = source;
    this.destination = destination;
  }
}
