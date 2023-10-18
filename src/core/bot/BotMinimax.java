package core.bot;

import javafx.scene.control.Button;
import java.util.List;

import core.Board;
import core.algorithms.MinimaxAlgorithm;

public class BotMinimax extends Bot {
    @Override
    public int[] move(Button[][] b, int rl, boolean isBotFirst) {
        // initialize board
        String[][] board = Board.getBoard(b);

        // initialize empty spaces on board
        List<int[]> emptySpaces = Board.getEmptySpaces(board);

        // initialize depth of game (terminal depth)
        int depth_game = depthGame(rl, isBotFirst, emptySpaces.size());

        // initialize board value
        int boardValue = Board.boardValue(board);
        System.out.println("board value a before: " + boardValue);

        // print depth of game
        System.out.println("max depth: " + depth_game);
        Board.printBoard(board);

        // start time
        long startTime = System.nanoTime();
        MinimaxAlgorithm mm = new MinimaxAlgorithm();
        int[] res = mm.minimax(board, emptySpaces, depth_game, boardValue);

        // end time
        long endTime = System.nanoTime();
        // print time
        System.out.println("time: " + (endTime - startTime) / 1000000 + " ms");

        int x = res[0];
        int y = res[1];
        System.out.println("x" + x);
        System.out.println("y" + y);
        return new int[] { x, y };
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