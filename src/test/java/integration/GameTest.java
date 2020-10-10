package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import controllers.PlayGame;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import models.GameBoard;
import models.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


@TestMethodOrder(OrderAnnotation.class) 
public class GameTest {
  
  /**
  * Runs only once before the testing starts.
  */
  @BeforeAll
  public static void init() {
    // Start Server
    PlayGame.main(null);
    
    System.out.println("Before All");
  }
  
  /**
  * This method starts a new game before every test run. It will run every time before a test.
  */
  @BeforeEach
  public void startNewGame() {
    
    // Test if server is running. You need to have an endpoint /
    // If you do not wish to have this end point, it is okay to not have anything in this method.
    HttpResponse<String> response = Unirest.get("http://localhost:8080/").asString();
    int restStatus = response.getStatus();

    System.out.println("Before Each");
  }
  
  /**
  * This is a test case to evaluate the newgame endpoint.
  */
  @Test
  @Order(2)
  public void newGameTest() {
    
    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.get("http://localhost:8080/newgame").asString();
    int restStatus = response.getStatus();
    
    // Check assert statement (New Game has started)
    assertEquals(restStatus, 200);
    System.out.println("Test New Game");
  }
  
  /**
  * This is a test case to evaluate the startgame endpoint.
  */
  @Test
  @Order(3)
  public void startGameTest() {
    
    // Create a POST request to startgame endpoint and get the body
    // Remember to use asString() only once for an endpoint call. Every time you call asString(), 
    // a new request will be sent to the endpoint. Call it once and then use the data in the object.
    HttpResponse<String> response = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    String responseBody = (String) response.getBody();
    
    // --------------------------- JSONObject Parsing ----------------------------------
    
    System.out.println("Start Game Response: " + responseBody);
    
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);

    // Check if game started after player 1 joins: Game should not start at this point
    assertEquals(false, jsonObject.get("gameStarted"));
    
    // ---------------------------- GSON Parsing -------------------------
    
    // GSON use to parse data to object
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    Player player1 = gameBoard.getP1();
    
    // Check if player type is correct
    assertEquals('X', player1.getType());
      
    System.out.println("Test Start Game");
  }
  
  @Test
  @Order(4)
  public void joinGameTest() {
    HttpResponse<String> response = Unirest.get("http://localhost:8080/joingame").asString();
    int restStatus = response.getStatus();
    assertEquals(restStatus, 200);
    System.out.println("Test Join Game");
  }
  
  @Test
  @Order(5)
  public void moveAfterStartGame() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    
    assertEquals(1, numberOfMoves);
  }
  
  @Test
  @Order(6)
  public void invalidMoveAfterStartGame() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2").body("x=0&y=0").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    
    assertEquals(1, numberOfMoves);
  }
  
  @Test
  @Order(7)
  public void move2AfterStartGame() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2").body("x=0&y=1").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    
    assertEquals(2, numberOfMoves);
  }
  
  @Test
  @Order(8)
  public void move3AfterStartGame() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=1&y=0").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    
    assertEquals(3, numberOfMoves);
  }
  
  @Test
  @Order(9)
  public void move4AfterStartGame() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2").body("x=1&y=1").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    
    assertEquals(4, numberOfMoves);
  }
  
  @Test
  @Order(10)
  public void move5AfterStartGame() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=2&y=0").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int winner = gameBoard.getWinner();
    assertEquals(1, winner);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    assertEquals(5, numberOfMoves);
  }
  
  /**
  * This is a test case to evaluate the newgame endpoint.
  */
  @Test
  @Order(11)
  public void newGameTest2() {
    
    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.get("http://localhost:8080/newgame").asString();
    int restStatus = response.getStatus();
    
    // Check assert statement (New Game has started)
    assertEquals(restStatus, 200);
    System.out.println("Test New Game");
  }
  
  /**
  * This is a test case to evaluate the startgame endpoint.
  */
  @Test
  @Order(12)
  public void startGameTest2() {
    
    // Create a POST request to startgame endpoint and get the body
    // Remember to use asString() only once for an endpoint call. Every time you call asString(), 
    // a new request will be sent to the endpoint. Call it once and then use the data in the object.
    HttpResponse<String> response = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    String responseBody = (String) response.getBody();
    
    // --------------------------- JSONObject Parsing ----------------------------------
    
    System.out.println("Start Game Response: " + responseBody);
    
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);

    // Check if game started after player 1 joins: Game should not start at this point
    assertEquals(false, jsonObject.get("gameStarted"));
    
    // ---------------------------- GSON Parsing -------------------------
    
    // GSON use to parse data to object
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    Player player1 = gameBoard.getP1();
    
    // Check if player type is correct
    assertEquals('X', player1.getType());
      
    System.out.println("Test Start Game");
  }
  
  @Test
  @Order(13)
  public void joinGameTest2() {
    HttpResponse<String> response = Unirest.get("http://localhost:8080/joingame").asString();
    int restStatus = response.getStatus();
    assertEquals(restStatus, 200);
    System.out.println("Test Join Game");
  }
  
  @Test
  @Order(14)
  public void moveAfterStartGame2() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    
    assertEquals(1, numberOfMoves);
  }
  
  @Test
  @Order(15)
  public void invalidMoveAfterStartGame2() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2").body("x=0&y=0").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    
    assertEquals(1, numberOfMoves);
  }
  
  @Test
  @Order(16)
  public void move2AfterStartGame2() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2").body("x=1&y=0").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    
    assertEquals(2, numberOfMoves);
  }
  
  @Test
  @Order(17)
  public void move3AfterStartGame2() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=1&y=1").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    
    assertEquals(3, numberOfMoves);
  }
  
  @Test
  @Order(18)
  public void move4AfterStartGame2() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2").body("x=2&y=0").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    
    assertEquals(4, numberOfMoves);
  }
  
  @Test
  @Order(19)
  public void move5AfterStartGame2() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=2&y=2").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int winner = gameBoard.getWinner();
    assertEquals(1, winner);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    assertEquals(5, numberOfMoves);
  }
  
  /**
  * This is a test case to evaluate the newgame endpoint.
  */
  @Test
  @Order(20)
  public void newGameTest3() {
    
    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.get("http://localhost:8080/newgame").asString();
    int restStatus = response.getStatus();
    
    // Check assert statement (New Game has started)
    assertEquals(restStatus, 200);
    System.out.println("Test New Game");
  }
  
  /**
  * This is a test case to evaluate the startgame endpoint.
  */
  @Test
  @Order(21)
  public void startGameTest3() {
    
    // Create a POST request to startgame endpoint and get the body
    // Remember to use asString() only once for an endpoint call. Every time you call asString(), 
    // a new request will be sent to the endpoint. Call it once and then use the data in the object.
    HttpResponse<String> response = Unirest.post("http://localhost:8080/startgame").body("type=O").asString();
    String responseBody = (String) response.getBody();
    
    // --------------------------- JSONObject Parsing ----------------------------------
    
    System.out.println("Start Game Response: " + responseBody);
    
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);

    // Check if game started after player 1 joins: Game should not start at this point
    assertEquals(false, jsonObject.get("gameStarted"));
    
    // ---------------------------- GSON Parsing -------------------------
    
    // GSON use to parse data to object
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    Player player1 = gameBoard.getP1();
    
    // Check if player type is correct
    assertEquals('O', player1.getType());
      
    System.out.println("Test Start Game");
  }
  
  @Test
  @Order(22)
  public void joinGameTest3() {
    HttpResponse<String> response = Unirest.get("http://localhost:8080/joingame").asString();
    int restStatus = response.getStatus();
    assertEquals(restStatus, 200);
    System.out.println("Test Join Game");
  }
  
  @Test
  @Order(23)
  public void moveAfterStartGame3() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    
    assertEquals(1, numberOfMoves);
  }
  
  @Test
  @Order(24)
  public void invalidMoveAfterStartGame3() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2").body("x=0&y=0").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    
    assertEquals(1, numberOfMoves);
  }
  
  @Test
  @Order(25)
  public void move2AfterStartGame3() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2").body("x=0&y=2").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    
    assertEquals(2, numberOfMoves);
  }
  
  @Test
  @Order(26)
  public void move3AfterStartGame3() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=1&y=0").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    
    assertEquals(3, numberOfMoves);
  }
  
  @Test
  @Order(27)
  public void move4AfterStartGame3() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2").body("x=1&y=1").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    
    assertEquals(4, numberOfMoves);
  }
  
  @Test
  @Order(28)
  public void move5AfterStartGame3() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=2&y=2").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    assertEquals(5, numberOfMoves);
  }
  
  @Test
  @Order(29)
  public void move6AfterStartGame3() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2").body("x=2&y=0").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int winner = gameBoard.getWinner();
    assertEquals(2, winner);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    assertEquals(6, numberOfMoves);
  }
  
  /**
  * This is a test case to evaluate the newgame endpoint.
  */
  @Test
  @Order(30)
  public void newGameTest4() {
    
    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.get("http://localhost:8080/newgame").asString();
    int restStatus = response.getStatus();
    
    // Check assert statement (New Game has started)
    assertEquals(restStatus, 200);
    System.out.println("Test New Game");
  }
  
  /**
  * This is a test case to evaluate the startgame endpoint.
  */
  @Test
  @Order(31)
  public void startGameTest4() {
    
    // Create a POST request to startgame endpoint and get the body
    // Remember to use asString() only once for an endpoint call. Every time you call asString(), 
    // a new request will be sent to the endpoint. Call it once and then use the data in the object.
    HttpResponse<String> response = Unirest.post("http://localhost:8080/startgame").body("type=O").asString();
    String responseBody = (String) response.getBody();
    
    // --------------------------- JSONObject Parsing ----------------------------------
    
    System.out.println("Start Game Response: " + responseBody);
    
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);

    // Check if game started after player 1 joins: Game should not start at this point
    assertEquals(false, jsonObject.get("gameStarted"));
    
    // ---------------------------- GSON Parsing -------------------------
    
    // GSON use to parse data to object
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    Player player1 = gameBoard.getP1();
    
    // Check if player type is correct
    assertEquals('O', player1.getType());
      
    System.out.println("Test Start Game");
  }
  
  @Test
  @Order(32)
  public void joinGameTest4() {
    HttpResponse<String> response = Unirest.get("http://localhost:8080/joingame").asString();
    int restStatus = response.getStatus();
    assertEquals(restStatus, 200);
    System.out.println("Test Join Game");
  }
  
  @Test
  @Order(33)
  public void moveAfterStartGame4() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    
    assertEquals(1, numberOfMoves);
  }
  
  @Test
  @Order(34)
  public void invalidMoveAfterStartGame4() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2").body("x=0&y=0").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    
    assertEquals(1, numberOfMoves);
  }
  
  @Test
  @Order(35)
  public void move2AfterStartGame4() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2").body("x=2&y=0").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    
    assertEquals(2, numberOfMoves);
  }
  
  @Test
  @Order(36)
  public void move3AfterStartGame4() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=1&y=0").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    
    assertEquals(3, numberOfMoves);
  }
  
  @Test
  @Order(37)
  public void move4AfterStartGame4() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2").body("x=2&y=1").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    
    assertEquals(4, numberOfMoves);
  }
  
  @Test
  @Order(38)
  public void move5AfterStartGame4() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=1").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    assertEquals(5, numberOfMoves);
  }
  
  @Test
  @Order(39)
  public void move6AfterStartGame4() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2").body("x=2&y=2").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int winner = gameBoard.getWinner();
    assertEquals(2, winner);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    assertEquals(6, numberOfMoves);
  }
  
  /**
  * This is a test case to evaluate the newgame endpoint.
  */
  @Test
  @Order(40)
  public void newGameTest5() {
    
    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.get("http://localhost:8080/newgame").asString();
    int restStatus = response.getStatus();
    
    // Check assert statement (New Game has started)
    assertEquals(restStatus, 200);
    System.out.println("Test New Game");
  }
  
  /**
  * This is a test case to evaluate the startgame endpoint.
  */
  @Test
  @Order(41)
  public void startGameTest5() {
    
    // Create a POST request to startgame endpoint and get the body
    // Remember to use asString() only once for an endpoint call. Every time you call asString(), 
    // a new request will be sent to the endpoint. Call it once and then use the data in the object.
    HttpResponse<String> response = Unirest.post("http://localhost:8080/startgame").body("type=O").asString();
    String responseBody = (String) response.getBody();
    
    // --------------------------- JSONObject Parsing ----------------------------------
    
    System.out.println("Start Game Response: " + responseBody);
    
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);

    // Check if game started after player 1 joins: Game should not start at this point
    assertEquals(false, jsonObject.get("gameStarted"));
    
    // ---------------------------- GSON Parsing -------------------------
    
    // GSON use to parse data to object
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    Player player1 = gameBoard.getP1();
    
    // Check if player type is correct
    assertEquals('O', player1.getType());
      
    System.out.println("Test Start Game");
  }
  
  @Test
  @Order(42)
  public void joinGameTest5() {
    HttpResponse<String> response = Unirest.get("http://localhost:8080/joingame").asString();
    int restStatus = response.getStatus();
    assertEquals(restStatus, 200);
    System.out.println("Test Join Game");
  }
  
  @Test
  @Order(43)
  public void moveAfterStartGame5() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    
    assertEquals(1, numberOfMoves);
  }
  
  @Test
  @Order(44)
  public void invalidMoveAfterStartGame5() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2").body("x=0&y=0").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    
    assertEquals(1, numberOfMoves);
  }
  
  @Test
  @Order(45)
  public void move2AfterStartGame5() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2").body("x=0&y=1").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    
    assertEquals(2, numberOfMoves);
  }
  
  @Test
  @Order(46)
  public void move3AfterStartGame5() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=1&y=0").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    
    assertEquals(3, numberOfMoves);
  }
  
  @Test
  @Order(47)
  public void move4AfterStartGame5() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2").body("x=2&y=0").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    
    assertEquals(4, numberOfMoves);
  }
  
  @Test
  @Order(48)
  public void move5AfterStartGame5() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=2&y=1").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    assertEquals(5, numberOfMoves);
  }
  
  @Test
  @Order(49)
  public void move6AfterStartGame5() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2").body("x=1&y=1").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    assertEquals(6, numberOfMoves);
  }
  
  @Test
  @Order(50)
  public void move7AfterStartGame5() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=2").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    assertEquals(7, numberOfMoves);
  }
  
  @Test
  @Order(51)
  public void move8AfterStartGame5() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2").body("x=1&y=2").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    assertEquals(8, numberOfMoves);
  }
  
  @Test
  @Order(52)
  public void move9AfterStartGame5() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=2&y=2").asString();
    
    response = Unirest.post("http://localhost:8080/board").asString();
    JSONObject jsonObject = new JSONObject((String) response.getBody());
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    int winner = gameBoard.getWinner();
    assertEquals(0, winner);
    int numberOfMoves = gameBoard.getNumberOfMoves();
    assertEquals(9, numberOfMoves);
  }

  
  @Test
  @Order(53)
  public void joinGameTestO() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/startgame").body("type=O").asString();
    String responseBody = (String) response.getBody();
    JSONObject jsonObject = new JSONObject(responseBody);
    assertEquals(true, jsonObject.get("gameStarted"));
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    Player player1 = gameBoard.getP1();
    assertEquals('O', player1.getType());
    HttpResponse<String> response2 = Unirest.get("http://localhost:8080/joingame").asString();
    int restStatus = response2.getStatus();
    assertEquals(restStatus, 200);
  }
  
  @Test
  @Order(54)
  public void joinGameTestX() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    String responseBody = (String) response.getBody();
    JSONObject jsonObject = new JSONObject(responseBody);
    assertEquals(true, jsonObject.get("gameStarted"));
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    Player player1 = gameBoard.getP1();
    assertEquals('X', player1.getType());
    HttpResponse<String> response2 = Unirest.get("http://localhost:8080/joingame").asString();
    int restStatus = response2.getStatus();
    assertEquals(restStatus, 200);
  }

  /**
  * This will run every time after a test has finished.
  */
  @AfterEach
  public void finishGame() {
    System.out.println("After Each");
  }
  
  /**
   * This method runs only once after all the test cases have been executed.
   */
  @AfterAll
  public static void close() {
    // Stop Server
    PlayGame.stop();
    System.out.println("After All");
  }
}
