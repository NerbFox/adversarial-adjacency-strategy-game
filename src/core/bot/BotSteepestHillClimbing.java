package core.bot;

import core.Board;
import core.algorithms.SteepestHillClimbing;
import javafx.scene.control.Button;

public class BotSteepestHillClimbing extends Bot {

  @Override
  public int[] move(Button[][] b, int rl, boolean isBotFirst) {
    String[][] board = Board.getBoard(b);

    SteepestHillClimbing hillClimbing = new SteepestHillClimbing();
    int[] res = hillClimbing.steepestHillClimbing(board);

    return new int[] { res[0], res[1] };
  }

}
