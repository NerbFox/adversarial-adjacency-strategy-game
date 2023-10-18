package core;

import java.util.ArrayList;
import java.util.List;

import core.bot.BotMinimax;
import javafx.scene.control.Button;
import javafx.util.Pair;

public class Board {
  private static final int ROW = 8;
  private static final int COL = 8;
  public static final String bot = "O";
  public static final String player = "X";

  // print board
  public static void printBoard(String[][] board) {
    for (int i = 0; i < ROW; i++) {
      for (int j = 0; j < COL; j++) {
        System.out.print(board[i][j] + " ");
      }
      System.out.println();
    }
  }

  public static String[][] getBoard(Button[][] board) {
    String[][] newBoard = new String[8][8];
    for (int i = 0; i < ROW; i++) {
      for (int j = 0; j < COL; j++) {
        newBoard[i][j] = board[i][j].getText();
      }
    }
    return newBoard;
  }

  // return true if s is bot or player
  private static boolean isBotOrPlayer(String s) {
    return s.equals(bot) || s.equals(player);
  }

  // Function to get empty spaces
  public static List<int[]> getEmptySpaces(String[][] board) {
    // return getEmptySpacesNormal(board);
    // yg heuristic cuma untuk minimax
    return getEmptySpacesHeuristic(board);
  }

  // get empty spaces that return list of tuples int (all empty spaces)
  private static List<int[]> getEmptySpacesNormal(String[][] board) {
    List<int[]> emptySpaces = new ArrayList<int[]>();
    for (int i = 0; i < ROW; i++) {
      for (int j = 0; j < COL; j++) {
        if (board[i][j].equals("")) {
          emptySpaces.add(new int[] { i, j });
        }
      }
    }
    return emptySpaces;
  }

  // Heuristic function for empty spaces (empty spaces that have a piece on the
  // left, right, up, or down)
  private static List<int[]> getEmptySpacesHeuristic(String[][] board) {
    List<int[]> emptySpaces = new ArrayList<>();
    boolean moreSpace = true;
    for (int i = 0; i < ROW; i++) {
      for (int j = 0; j < COL; j++) {
        if (board[i][j].equals("")) {
          if (i - 1 >= 0) { // check if the bot or player is on the left
            if (isBotOrPlayer(board[i - 1][j])) {
              emptySpaces.add(new int[] { i, j });
              continue;
            }
          }
          if (i + 1 < ROW) { // check if the bot or player is on the right
            if (isBotOrPlayer(board[i + 1][j])) {
              emptySpaces.add(new int[] { i, j });
              continue;
            }

          }
          if (j - 1 >= 0) { // check if the bot or player is on the up
            if (isBotOrPlayer(board[i][j - 1])) {
              emptySpaces.add(new int[] { i, j });
              continue;
            }
          }
          if (j + 1 < COL) { // check if the bot or player is on the down
            if (isBotOrPlayer(board[i][j + 1])) {
              emptySpaces.add(new int[] { i, j });
            }
          }
          // if (moreSpace && !add) {
          // // add one more empty space that not have a piece on the left, right, up, or
          // down
          // moreSpace = false;
          // emptySpaces.add(new int[]{i, j});
          // }

        }
      }
    }
    // print empty spaces
    for (int[] emptySpace : emptySpaces) {
      System.out.println("empty space: " + emptySpace[0] + " " + emptySpace[1]);
    }
    return emptySpaces;
  }

  // move function, return the board and board value
  public static Pair<String[][], Integer> updateGameBoard(String[][] board, String player, int boardValue, int i,
      int j) {
    // Value of indices to control the lower/upper bound of rows and columns
    // in order to change surrounding/adjacent X's and O's only on the game board.
    // Four boundaries: First & last row and first & last column.

    int startRow, endRow, startColumn, endColumn;

    if (i - 1 < 0) // If clicked button in first row, no preceding row exists.
      startRow = i;
    else // Otherwise, the preceding row exists for adjacency.
      startRow = i - 1;

    if (i + 1 >= ROW) // If clicked button in last row, no subsequent/further row exists.
      endRow = i;
    else // Otherwise, the subsequent row exists for adjacency.
      endRow = i + 1;

    if (j - 1 < 0) // If clicked on first column, lower bound of the column has been reached.
      startColumn = j;
    else
      startColumn = j - 1;

    if (j + 1 >= COL) // If clicked on last column, upper bound of the column has been reached.
      endColumn = j;
    else
      endColumn = j + 1;

    // Search for adjacency for X's and O's or vice versa, and replace them.
    // Update scores for X's and O's accordingly.
    // copy the board
    String[][] newBoard1 = copyBoard(board);

    Pair<String[][], Integer> p = new Pair<>(newBoard1, boardValue);
    for (int x = startRow; x <= endRow; x++) {
      p = setBoard(p, player, x, j);
    }
    for (int y = startColumn; y <= endColumn; y++) {
      p = setBoard(p, player, i, y);
    }

    String[][] newBoard = p.getKey();
    int delta = p.getValue();
    // for center
    if (player.equals(bot)) {
      newBoard[i][j] = bot;
      delta++;
    } else {
      newBoard[i][j] = BotMinimax.player;
      delta--;
    }
    p = new Pair<>(newBoard, delta);
    return p;
  }

  // set board
  private static Pair<String[][], Integer> setBoard(Pair<String[][], Integer> p, String player, int i, int j) {
    int delta = p.getValue();
    String[][] board = p.getKey();
    if (player.equals(BotMinimax.player)) {
      if (board[i][j].equals(BotMinimax.bot)) {
        board[i][j] = BotMinimax.player;
        // x++, o--
        delta -= 2;
      }
    } else if (board[i][j].equals(BotMinimax.player)) {
      board[i][j] = BotMinimax.bot;
      // x--, o++
      delta += 2;
    }
    return new Pair<>(board, delta);
  }

  // the objective function is the sum of the values of each row, column, and
  // diagonal
  // where O is +1 and X is -1, value = sumOfO - sumOfX
  public static int boardValue(String[][] board) {
    int value = 0;
    for (int i = 0; i < ROW; i++) {
      for (int j = 0; j < COL; j++) {
        if (board[i][j].equals("O")) {
          value++;
        } else if (board[i][j].equals("X")) {
          value--;
        }
      }
    }
    return value;
  }

  private static String[][] copyBoard(String[][] board) {
    String[][] newBoard = new String[8][8];
    for (int x = 0; x < ROW; x++) {
      System.arraycopy(board[x], 0, newBoard[x], 0, COL);
    }
    return newBoard;
  }
}
