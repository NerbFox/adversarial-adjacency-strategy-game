package core.bot;

import java.util.List;

import core.Board;
import core.algorithms.GeneticAlgorithm;
import javafx.scene.control.Button;

public class BotGenetic extends Bot {
  @Override
  public int[] move(Button[][] b, int rl, boolean isBotFirst) {
    String[][] board = Board.getBoard(b);
    List<int[]> emptySpaces = Board.getEmptySpaces(board);

    GeneticAlgorithm ga = new GeneticAlgorithm();
    int[] res = ga.GeneticAlgo(board, emptySpaces);

    return new int[] {res[0], res[1]};
  }
}
