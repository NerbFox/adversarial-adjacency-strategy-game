package core.algorithms;

import java.util.List;

import core.Board;
import core.bot.Bot;
import javafx.util.Pair;

public class SteepestHillClimbing {
  public int[] steepestHillClimbing(String[][] board) {
    // Get empty spaces
    List<int[]> emptySpaces = Board.getEmptySpaces(board);
    
    // Initialize current successor with the first empty space coordinates
    int[] currentCoord = emptySpaces.get(0);
    int bValue = Board.boardValue(board);

    // Search loop
    for (int[] nextCoord : emptySpaces) {

      // Find next state and current state values
      Pair<String[][], Integer> currentState = Board.updateGameBoard(board, Bot.bot, bValue, currentCoord[0],
          currentCoord[1]);
      Pair<String[][], Integer> nextState = Board.updateGameBoard(board, Bot.bot, bValue, nextCoord[0], nextCoord[1]);

      if (currentState.getValue() < nextState.getValue()) {
        currentCoord[0] = nextCoord[0];
        currentCoord[1] = nextCoord[1];
      }
    }

    return currentCoord;
  }
}
