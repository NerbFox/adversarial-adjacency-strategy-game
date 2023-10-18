package core.bot;

import java.util.List;

import core.Board;
import core.algorithms.SimulatedAnnealing;
import javafx.scene.control.Button;

public class BotSimulatedAnnealing extends Bot {

  @Override
  public int[] move(Button[][] b, int rl, boolean isBotFirst) {
    String[][] board = Board.getBoard(b);
    List<int[]> emptySpaces = Board.getEmptySpaces(board);

    int depth_game = depthGame(rl, isBotFirst, emptySpaces.size());

    SimulatedAnnealing simAnnealing = new SimulatedAnnealing();
    int[] res = simAnnealing.simAnnealing(board, depth_game);

    return new int[] { res[0], res[1] };
  }

  // return dept of the game
  private int depthGame(int rl, boolean isBotFirst, int sizeEmptySpaces) {
    // one round is player move and bot move
    // if bot is first, then the depth of the game will 2 * rl
    // if bot is second, then the depth of the game will 2 * rl - 1
    int depth_game = 0;
    if (isBotFirst) {
      depth_game = 2 * rl;
    } else {
      depth_game = 2 * rl - 1;
    }
    // if the size of empty spaces < depth of game, then the depth of the game will
    // be the size of empty spaces
    if (sizeEmptySpaces < depth_game) {
      depth_game = sizeEmptySpaces;
    }
    return depth_game;
  }
}
