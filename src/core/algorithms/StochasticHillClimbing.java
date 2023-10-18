package core.algorithms;

import java.util.List;
import java.util.Random;

import core.Board;
import core.Bot;
import javafx.util.Pair;

public class StochasticHillClimbing implements MoveCreator {
    public int[] makeMove(Bot bot, String[][] board, int depth) {
    // Get empty spaces
    List<int[]> emptySpaces = Board.getEmptySpaces(board);

    // Initialize current successor randomly
    Random rand = new Random();
    int[] currentCoord = emptySpaces.get(rand.nextInt(emptySpaces.size()));
    int bValue = Board.boardValue(board);

    //
    int maxIterations = 100;

    // Search loop
    for (int i = 0; i < maxIterations; i++) {
      int[] nextCoord = emptySpaces.get(rand.nextInt(emptySpaces.size()));

      // Find current state value and next state current value
      Pair<String[][], Integer> currentState = Board.updateGameBoard(bot, board, bot.me, bValue, currentCoord[0],
          currentCoord[1]);
      Pair<String[][], Integer> nextState = Board.updateGameBoard(bot, board, bot.me, bValue, nextCoord[0], nextCoord[1]);
      if (currentState.getValue() < nextState.getValue()) {
        currentCoord[0] = nextCoord[0];
        currentCoord[1] = nextCoord[1];
      }
    }
    return currentCoord;
  }
}
