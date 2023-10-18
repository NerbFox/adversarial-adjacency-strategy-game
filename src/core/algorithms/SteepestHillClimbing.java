package core.algorithms;

import java.util.List;

import core.Board;
import core.Bot;
import javafx.util.Pair;

public class SteepestHillClimbing implements MoveCreator {
  public int[] makeMove(Bot bot, String[][] board, int depth) {
    // Get empty spaces
    List<int[]> emptySpaces = Board.getEmptySpaces(board);

    // Initialize current successor with the first empty space coordinates
    int[] currentCoord = emptySpaces.get(0);
    int bValue = Board.boardValue(bot, board);

    // Search loop
    for (int[] nextCoord : emptySpaces) {

      // Find next state and current state values
      Pair<String[][], Integer> currentState = Board.updateGameBoard(bot, board, bot.me, bValue, currentCoord[0],
          currentCoord[1]);
      Pair<String[][], Integer> nextState = Board.updateGameBoard(bot, board, bot.me, bValue, nextCoord[0], nextCoord[1]);
      System.out.println("NEXT");
      Board.printBoard(nextState.getKey());
      System.out.println(nextState.getValue());
      if (currentState.getValue() < nextState.getValue()) {
        System.out.println("CURRENT");
        Board.printBoard(currentState.getKey());
        System.out.println(currentState.getValue());
        currentCoord[0] = nextCoord[0];
        currentCoord[1] = nextCoord[1];
      }
    }

    return currentCoord;
  }
}
