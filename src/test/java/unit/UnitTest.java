package unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import models.GameBoard;
import models.Move;
import models.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

class UnitTest {
  
  @Test
  @Order(1)
  public void testCheckWinner() {
    GameBoard gameBoard = new GameBoard();
    gameBoard.setWinner(0);
    gameBoard.setGameStart(true);
    Player p1 = new Player('1', 'O');
    Player p2 = new Player('2', 'X');
    Move move = new Move(p1, 1, 2);
    gameBoard.makeMove(move);
    move = new Move(p2, 0, 0);
    gameBoard.makeMove(move);
    move = new Move(p1, 1, 0);
    gameBoard.makeMove(move);
    move = new Move(p2, 2, 2);
    gameBoard.makeMove(move);
    move = new Move(p1, 2, 0);
    gameBoard.makeMove(move);
    move = new Move(p2, 0, 2);
    gameBoard.makeMove(move);
    move = new Move(p1, 0, 1);
    gameBoard.makeMove(move);
    move = new Move(p2, 2, 1);
    gameBoard.makeMove(move);
    move = new Move(p1, 1, 1);
    gameBoard.makeMove(move);
    
    assertEquals(true, gameBoard.checkWinner(move));
  }
  
  @Test
  @Order(2)
  public void testValidMove() {
    GameBoard gameBoard = new GameBoard();
    gameBoard.setTurn(1);
    Player p1 = new Player('2', 'X');
    Move move = new Move(p1, 1, 2);
    
    assertEquals(false, gameBoard.isValidMove(move));
  }
  
  @Test
  @Order(3)
  public void testDraw() {
    GameBoard gameBoard = new GameBoard();
    gameBoard.setNumberOfMoves(0);
    gameBoard.setGameStart(true);
    Player p1 = new Player('1', 'O');
    Player p2 = new Player('2', 'X');
    Move move = new Move(p1, 1, 2);
    gameBoard.makeMove(move);
    move = new Move(p2, 0, 0);
    gameBoard.makeMove(move);
    move = new Move(p1, 1, 0);
    gameBoard.makeMove(move);
    move = new Move(p2, 1, 1);
    gameBoard.makeMove(move);
    move = new Move(p1, 2, 2);
    gameBoard.makeMove(move);
    move = new Move(p2, 0, 2);
    gameBoard.makeMove(move);
    move = new Move(p1, 2, 0);
    gameBoard.makeMove(move);
    move = new Move(p2, 2, 1);
    gameBoard.makeMove(move);
    move = new Move(p1, 0, 1);
    gameBoard.makeMove(move);
    
    assertEquals(true, gameBoard.checkWinner(move));
    assertEquals(true, gameBoard.getDraw());
  }
}
