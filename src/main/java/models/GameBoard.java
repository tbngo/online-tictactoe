package models;

public class GameBoard {

  private Player p1;

  private Player p2;

  private boolean gameStarted;

  private int turn;

  private char[][] boardState;

  private int winner;

  private boolean isDraw;
  
  private int numberOfMoves;
  
  private Message message;

  /**
   * Default constructor for GameBoard. Initializes game to starting state.
   */
  public GameBoard() {
    this.gameStarted = false;
    
    this.turn = 0;

    this.boardState = new char[][]{
      {'\u0000', '\u0000', '\u0000'},
      {'\u0000', '\u0000', '\u0000'},
      {'\u0000', '\u0000', '\u0000'},
    };
    this.winner = 0;
    this.isDraw = false;
  }
  
  /**
   * Set turn to specified turn.
   * @param turn represents whose turn it is
   */
  public void setTurn(int turn) {
    this.turn = turn;
  }
  
  /**
   * Set p1 to a specified player.
   * @param p1 represents p1 
   */
  public void setP1(Player p1) {
    this.p1 = p1;
  }
  
  /**
   * Set p2 to a specified player.
   * @param p2 represents p2
   */
  public void setP2(Player p2) {
    this.p2 = p2;
  }
  
  /**
   * Set gameStarted to a specified boolean.
   * @param gameStatus represents gameStatus
   */
  public void setGameStart(boolean gameStatus) {
    this.gameStarted = gameStatus;
  }
  
  /**
   * Set winner to a specified winner.
   * @param winner represents winner
   */
  public void setWinner(int winner) {
    this.winner = winner;
  }
  
  /**
   * Set message to a specified message.
   * @param message represents message
   */
  public void setMessage(Message message) {
    this.message = message;
  }
  
  /**
   * Set numberOfMOves to specified number.
   * @param numberOfMoves represents a number
   */
  public void setNumberOfMoves(int numberOfMoves) {
    this.numberOfMoves = numberOfMoves;
  }
  
  /**
   * Returns p1.
   * @return
   */
  public Player getP1() {
    return this.p1;
  }
  
  /**
   * Returns p2.
   * @return
   */
  public Player getP2() {
    return this.p2;
  }
  
  /**
   * Returns player depending on playerId.
   * @param playerId represents playerId
   * @return
   */
  public Player getP(int playerId) {
    if (playerId == 1) {
      return this.p1;
    } 
    return this.p2;
  }
  
  /**
   * Returns whose turn it is.
   * @return
   */
  public int getTurn() {
    return this.turn;
  }
  
  /**
   * Return the winner.
   * @return
   */
  public int getWinner() {
    return this.winner;
  }
  
  /**
   * Returns isDraw.
   * @return
   */
  public boolean getDraw() {
    return this.isDraw;
  }
  
  /**
   * Returns message.
   * @return
   */
  public Message getMessage() {
    return this.message;
  }
  
  public int getNumberOfMoves() {
    return this.numberOfMoves;
  }
  
  /**
   * Checks if move is valid or not. Checks whether it is the player's turn and if
   * the move is in an empty space.
   * @param move represents a move
   * @return
   */
  public boolean isValidMove(Move move) {
    if (move.getPlayer().getId() != turn) {
      return false;
    } else if (boardState[move.getMoveX()][move.getMoveY()] != '\u0000') {
      return false;
    } else {
      return true;
    }
  }
  
  /**
   * Swaps the turn to the other player.
   */
  public void swapTurns() {
    this.turn = this.turn % 2 + 1;
  }
  
  /**
   * Edits boardState to reflect a player move.
   * @param move represents a move
   */
  public void makeMove(Move move) {
    if (gameStarted) {
      boardState[move.getMoveX()][move.getMoveY()] = move.getPlayer().getType();
      this.numberOfMoves++;
    }
  }
  
  /**
   * Checks boardState to see if there has been a winner or not.
   * @param move represents a move
   * @return
   */
  public boolean checkWinner(Move move) { 
    int x = move.getMoveX();
    int y = move.getMoveY();
    
    if (numberOfMoves == 9) {
      if (winner == 0) {
        isDraw = true;
        return true;
      }
    }
    
    if (boardState[0][y] == boardState[1][y] && boardState[0][y] == boardState[2][y]) {
      this.winner = move.getPlayer().getId();
      return true;
    } else if (boardState[x][1] == boardState[x][0] && boardState[x][2] == boardState[x][0]) {
      this.winner = move.getPlayer().getId();
      return true;
    } else if (x == y && boardState[0][0] == boardState[1][1] 
        && boardState[0][0] == boardState[2][2]) {
      this.winner = move.getPlayer().getId();
      return true;
    } else if (x + y == 2 && boardState[0][2] == boardState[2][0] 
        && boardState[0][2] == boardState[1][1]) {
      this.winner = move.getPlayer().getId();
      return true;
    }
    
    return false;
  }
  
  /**
   * Checks if both players are null or not.
   * @return
   */
  public boolean checkPlayerNull() {
    if (this.p1 != null && this.p2 != null) {
      return true;
    }
    return false;
  }
  
  /**
   * Clears the board
   */
  public void clearBoard() {
    this.boardState = new char[][]{
      {'\u0000', '\u0000', '\u0000'},
      {'\u0000', '\u0000', '\u0000'},
      {'\u0000', '\u0000', '\u0000'},
    };
    this.numberOfMoves = 0;
  }
}
