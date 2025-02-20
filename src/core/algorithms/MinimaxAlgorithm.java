package core.algorithms;
import core.Bot;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

import core.Board;

public class MinimaxAlgorithm implements MoveCreator {
    private final int alpha = Integer.MIN_VALUE; // -infinity
    private final int beta = Integer.MAX_VALUE; // +infinity

    // minimax algorithm with alpha-beta pruning
    public int[] makeMove(Bot bot, String[][] board, int depth) {
        List<int[]> emptySpace = Board.getEmptySpacesHeuristic(board);
        System.out.println(emptySpace.size() + " empty space size");
        System.out.println(depth + " = depth");
        if (depth > 6){
            if (bot.me.equals("X")) { // First player
                depth = 6;
            } else { // Second player
                depth = 5;
            }
        }
        System.out.println(emptySpace.size() + " after empty space size");
        System.out.println(depth + " = after depth");

        int boardValue = Board.boardValue(bot, board);

        System.out.println("alpha: " + alpha + " beta: " + beta);
        int[] res = this.minimaxDecision(bot, board, emptySpace, boardValue, depth, alpha, beta, true);
        System.out.println("finish minmax with res: " + res[0] + " x: " + res[1] + " y: " + res[2]);
        System.out.println("alpha: " + res[3] + " beta: " + res[4]);
        return new int[] { res[1], res[2] };
    }

    private int[] minimaxDecision(Bot bot, String[][] board, List<int[]> emptySpaces, int bvalue, int depth, int alpha, int beta,
                                  boolean isMaximizing) {
        if (depth == 0) {
            bvalue = Board.boardValue(bot, board);
            return new int[] { bvalue, -1, -1, alpha, beta };
        }
        int bestValue;
        int bestX = -1;
        int bestY = -1;

        if (isMaximizing) {
            bestValue = Integer.MIN_VALUE;
            for (int[] space : emptySpaces) {
                int i = space[0];
                int j = space[1];
                Pair<String[][], Integer> p = Board.updateGameBoard(bot, board, bot.me, bvalue, i, j);
                List<int[]> newEmptySpaces = new ArrayList<>(emptySpaces); // copy of emptySpaces;
                newEmptySpaces.remove(0);

                int[] res = minimaxDecision(bot, p.getKey(), newEmptySpaces, p.getValue(), depth - 1, alpha, beta, false);
                int v = res[0];
                if (v > bestValue) {
                    bestValue = v;
                    bestX = i;
                    bestY = j;
                }

                alpha = Math.max(alpha, bestValue);
                if (beta <= alpha) {
                    break;
                }
            }
        } else {
            bestValue = Integer.MAX_VALUE;
            for (int[] space : emptySpaces) {
                int i = space[0];
                int j = space[1];
                Pair<String[][], Integer> p = Board.updateGameBoard(bot, board, bot.enemy, bvalue, i, j);
                List<int[]> newEmptySpaces = new ArrayList<>(emptySpaces); // copy of emptySpaces
                newEmptySpaces.remove(0);

                int[] res = minimaxDecision(bot, p.getKey(), newEmptySpaces, p.getValue(), depth - 1, alpha, beta, true);
                int v = res[0];
                if (v < bestValue) {
                    bestValue = v;

                }

                beta = Math.min(beta, bestValue);
                if (beta <= alpha) {
                    break;
                }
            }
        }
        return new int[] {bestValue, bestX, bestY, alpha, beta };
    }
}
