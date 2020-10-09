package models;

public class Message {

  private boolean moveValidity;

  private int code;

  private String message;

  /**
   * Constructor for Message. Takes a moveValidity, code, and message.
   * @param moveValidity represents the moveValidity as a boolean
   * @param code represents the code as an integer
   * @param message represents a message
   */
  public Message(boolean moveValidity, int code, String message) {
    this.moveValidity = moveValidity;
    this.code = code;
    this.message = message;
  }
  
  /**
   * Default constructor for Message. Sets everything to a valid message send.
   */
  public Message() {
    this.moveValidity = true;
    this.code = 100;
    this.message = "";
  }

  /**
   * Returns moveValidity.
   * @return
   */
  public boolean isMoveValid() {
    return moveValidity;
  }

  /**
   * Sets moveValidity to a specified boolean.
   * @param moveValidity represents a moveValidity
   */
  public void setMoveValidity(boolean moveValidity) {
    this.moveValidity = moveValidity;
  }

  /**
   * Returns code.
   * @return
   */
  public int getCode() {
    return code;
  }

  /**
   * Sets code to a specified integer.
   * @param code represents a code
   */
  public void setCode(int code) {
    this.code = code;
  }

  /**
   * Returns message.
   * @return
   */
  public String getMessage() {
    return message;
  }

  /**
   * Sets message to a specified string.
   * @param message represents a message
   */
  public void setMessage(String message) {
    this.message = message;
  }
}
