package models;

public class Player {

  private char type;

  private int id;
  
  public Player(int id, char type) {
    this.id = id;
    this.type = type;
  }
  
  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }
  
  public void setType(char type) {
    this.type = type;
  }
  
  public char getType() {
    return type;
  }
}
