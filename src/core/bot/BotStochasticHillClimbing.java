package core.bot;

import core.Board;
import core.algorithms.StochasticHillClimbing;
import javafx.scene.control.Button;

public class BotStochasticHillClimbing extends Bot {

  @Override
  public int[] move(Button[][] b, int rl, boolean isBotFirst) {
    String[][] board = Board.getBoard(b);

    StochasticHillClimbing hillClimbing = new StochasticHillClimbing();
    int[] res = hillClimbing.stochasticHillClimbing(board);

    return new int[] { res[0], res[1] };
  }
}
