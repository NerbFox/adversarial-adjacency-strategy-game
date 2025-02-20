package core.algorithms;

import java.util.List;
import java.util.Random;

import core.Board;
import core.Bot;
import javafx.util.Pair;

public class SimulatedAnnealing implements MoveCreator {
    private double schedule(double T) {
    return T - 0.1d;
  }

    public int[] makeMove(Bot bot, String[][] board, int depth) {
    // Get empty spaces
    List<int[]> emptySpaces = Board.getEmptySpaces(board);
    
    // Initialize current successor randomly
    Random rand = new Random();
    int[] currentCoord = emptySpaces.get(rand.nextInt(emptySpaces.size()));
    int bValue = Board.boardValue(bot, board);
    double T = depth;
    while (true) {
      T = schedule(T);
      if (T <= 0.0d) {
        return currentCoord;
      }
      int[] nextCoord = emptySpaces.get(rand.nextInt(emptySpaces.size()));
      
      Pair<String[][], Integer> currentState = Board.updateGameBoard(bot, board, bot.me, bValue, currentCoord[0],
          currentCoord[1]);
      Pair<String[][], Integer> nextState = Board.updateGameBoard(bot, board, bot.me, bValue, nextCoord[0], nextCoord[1]);

      double deltaE = nextState.getValue() - currentState.getValue();

      if (deltaE > 0) {
        currentCoord[0] = nextCoord[0];
        currentCoord[1] = nextCoord[1];
      } else {
        double threshold = Math.exp(deltaE / T);

        if (Math.random() <= threshold) {
          currentCoord[0] = nextCoord[0];
          currentCoord[1] = nextCoord[1];
        }
      }

    }
  }
}
