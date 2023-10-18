package core.algorithms;

import core.Bot;

public interface MoveCreator {
    public int[] makeMove(Bot bot, String[][] board, int depth);
}
