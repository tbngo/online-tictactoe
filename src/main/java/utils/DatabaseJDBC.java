package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import models.GameBoard;
import models.Move;
import models.Player;

public class DatabaseJDBC {
  
  /**
   * Main method.
   * @param args represents args in command line
   */
  public static void main(String[] args) {
    
    DatabaseJDBC jdbc = new DatabaseJDBC();
    
    Connection conn = jdbc.createConnection();
    jdbc.createTable(conn, "ASE_I3_MOVE");
    
  }
  
  /**
   * Creates a new connection.
   * @return Connection object
   */
  public Connection createConnection() {
    Connection conn = null; 
    
    try {
      Class.forName("org.sqlite.JDBC"); 
      conn = DriverManager.getConnection("jdbc:sqlite:ase.db"); 
      
    } catch (Exception e) {
      System.out.println(e.getClass().getName() + ":" + e.getMessage()); 
    }
    
    System.out.println("Opened database successfully"); 
    return conn;
  }
  
  /**
   * Creates new table.
   * @param conn is a Connection Object 
   */
  public boolean createTable(Connection conn, String tableName) {
    Statement stmt = null; 
    
    try {
      stmt = conn.createStatement(); 
      String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " " 
                   + "(PLAYER_ID INT NOT NULL, " 
                   + "MOVE_X INT NOT NULL, "
                   + "MOVE_Y INT NOT NULL);"; 
      stmt.executeUpdate(sql);
      stmt.close(); 
    } catch (Exception e) {
      System.out.println(e.getClass().getName() + ":" + e.getMessage());
      return false; 
    }
    System.out.println("Table created successfully"); 
    return true; 
  }
  
  /**
   * Adds a successful move to the specified table.
   * @param conn is a Connection object
   * @param move is a Move object containing data
   * @return Boolean that indicates whether or not the move was added
   */
  public boolean addMoveData(Connection conn, Move move) {
    Statement stmt = null; 
    
    try {
      conn.setAutoCommit(false);
      System.out.println("Opened database successfully.");
      
      stmt = conn.createStatement(); 
      String sql = "INSERT INTO ASE_I3_MOVES(PLAYER_ID, MOVE_X, MOVE_Y) "
                   + "VALUES (" + move.getPlayer().getId() + ", " 
                   + move.getMoveX() + ", " 
                   + move.getMoveY() + " );";
      stmt.executeUpdate(sql); 
      stmt.close(); 
      conn.commit(); 
      
    } catch (Exception e) {
      System.out.println(e.getClass().getName() + ":" + e.getMessage()); 
      return false; 
    }
    
    System.out.println("Move successfully added"); 
    return true; 
  }
  

  
  /**
   * Removes the specified table from the database. 
   * @param conn is a Connection object
   * @param tableName is a String representing the table to be dropped
   * @return Boolean indicating whether or not the sql query was successful
   */
  public boolean dropTable(Connection conn, String tableName) {
    Statement stmt = null; 
    try {
      stmt = conn.createStatement(); 
      String sql =  "DROP Table IF EXISTS " + tableName;  
      stmt.execute(sql); 
      stmt.close(); 
    } catch (Exception e) {
      System.out.println(e.getClass().getName() + ":" + e.getMessage()); 
      return false; 
    }
    System.out.println("Table dropped successfully"); 
    return true; 
  }
  
  /**
   * Gets all entries in database.
   * @param conn represents a connection
   * @param tableName represents the table to get entries frmo
   * @return
   */
  public ArrayList<Move> getMoves(Connection conn, String tableName, GameBoard gameBoard) {
    Statement stmt = null;
    Move move;
    Player player;
    ArrayList<Move> movesArray = new ArrayList<Move>();
    
    try {
      conn.setAutoCommit(false);
      System.out.println("Opened database successfully");
      
      stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + ";");
      
      while (rs.next()) {
        int id = rs.getInt("PLAYER_ID");
        int moveX = rs.getInt("MOVE_X");
        int moveY = rs.getInt("MOVE_Y");
        player = gameBoard.getP(id);
        move = new Move(player, moveX, moveY);
        movesArray.add(move);
      }
      rs.close();
      stmt.close();
      conn.close();
      return movesArray;
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
    }
    System.out.println("Operation done successfully");
    return movesArray;
  }

}