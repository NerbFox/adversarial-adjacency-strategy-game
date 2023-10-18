package core;

import core.Board;
import core.algorithms.MoveCreator;
import javafx.scene.control.Button;

import java.util.List;

public class Bot  {
  public String me; //
  public String enemy; //
  private MoveCreator moveCreator;

  public Bot(boolean isFirst, MoveCreator moveCreator) {
    if (isFirst) {
      me = "X";
      enemy = "O";
    } else {
      enemy = "X";
      me = "O";
    }

    this.moveCreator = moveCreator;
  }
  public int[] move(Button[][] b, int rl, boolean isBotFirst) {
    String[][] board = Board.getBoard(b);
    int emptySpaceSize = Board.getEmptySpaces(board).size();
    int depth = depthGame(rl, isBotFirst, emptySpaceSize);

    return moveCreator.makeMove(this, board, depth);
  }

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
