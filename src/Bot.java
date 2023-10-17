import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import java.lang.Math;
import java.util.Random;

public class Bot {
    private static final int ROW = 8;
    private static final int COL = 8;
    private String bot = "O";
    private String player = "X";

    public int[] move(Button[][] b, int rl, boolean isBotFirst) {
        // initialize board
        String[][] board = getBoard(b);
        // initialize empty spaces on board
        List<int[]> emptySpaces = getEmptySpaces(board);
        // initialize depth of game (terminal depth)
        int depth_game = depthGame(rl, isBotFirst, emptySpaces.size());
        // initialize board value
        int boardValue = boardValue(board);
        System.out.println("board value a before: " + boardValue);
        // print depth of game
        System.out.println("max depth: " + depth_game);
        printBoard(board);
        // start time
        long startTime = System.nanoTime();
        // int[] res = this.minimax(board, emptySpaces, depth_game, boardValue);
        GeneticAlgorithm ga = new GeneticAlgorithm();
        int[] res = ga.GeneticAlgo(board, emptySpaces);
        // end time
        long endTime = System.nanoTime();
        // print time
        System.out.println("time: " + (endTime - startTime) / 1000000 + " ms");

        int x = res[0];
        int y = res[1];
        System.out.println("x" + x);
        System.out.println("y" + y);
        return new int[] { x, y };

        // int randomMove = (int) (Math.random()*emptySpaces.size());
        // x = emptySpaces.get(randomMove)[0];
        // y = emptySpaces.get(randomMove)[1];
        // System.out.println("before");
        // printBoard(board);
        // Pair<String[][], Integer> p = updateGameBoard(board, bot, boardValue, x, y);
        // System.out.println("board value after: " + boardValue(board));
        // System.out.println("after");
        // printBoard(p.getKey());
        // return new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
    }

    // print board
    private void printBoard(String[][] board) {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    // return array of text buttons
    private String[][] getBoard(Button[][] board) {
        String[][] boardText = new String[8][8];
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                boardText[i][j] = board[i][j].getText();
            }
        }
        return boardText;
    }

    // return true if s is bot or player
    private boolean isBotOrPlayer(String s) {
        return s.equals(bot) || s.equals(player);
    }

    // Function to get empty spaces
    private List<int[]> getEmptySpaces(String[][] board) {
        return getEmptySpacesNormal(board);
    }

    // get empty spaces that return list of tuples int (all empty spaces)
    private List<int[]> getEmptySpacesNormal(String[][] board) {
        List<int[]> emptySpaces = new ArrayList<int[]>();
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (board[i][j].equals("")) {
                    emptySpaces.add(new int[] { i, j });
                }
            }
        }
        return emptySpaces;
    }

    // Heuristic function for empty spaces (empty spaces that have a piece on the
    // left, right, up, or down)
    // get empty spaces that return list of tuples int that the bot left or right or
    // up or down has a piece
    private List<int[]> getEmptySpacesHeuristic(String[][] board) {
        List<int[]> emptySpaces = new ArrayList<>();
        boolean moreSpace = true;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (board[i][j].equals("")) {
                    if (i - 1 >= 0) {
                        if (isBotOrPlayer(board[i - 1][j])) {
                            emptySpaces.add(new int[] { i, j });
                        }
                    } else if (i + 1 < ROW) {
                        if (isBotOrPlayer(board[i + 1][j])) {
                            emptySpaces.add(new int[] { i, j });
                        }
                    } else if (j - 1 >= 0) {
                        if (isBotOrPlayer(board[i][j - 1])) {
                            emptySpaces.add(new int[] { i, j });
                        }
                    } else if (j + 1 < COL) {
                        if (isBotOrPlayer(board[i][j + 1])) {
                            emptySpaces.add(new int[] { i, j });
                        }
                    }
                    // else if (moreSpace){
                    // // add one more empty space that not have a piece on the left, right, up, or
                    // down
                    // moreSpace = false;
                    // emptySpaces.add(new int[]{i, j});
                    // }
                }
            }
        }
        return emptySpaces;
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

    // move function, return the board and board value
    private Pair<String[][], Integer> updateGameBoard(String[][] board, String player, int boardValue, int i, int j) {
        // Value of indices to control the lower/upper bound of rows and columns
        // in order to change surrounding/adjacent X's and O's only on the game board.
        // Four boundaries: First & last row and first & last column.

        int startRow, endRow, startColumn, endColumn;

        if (i - 1 < 0) // If clicked button in first row, no preceding row exists.
            startRow = i;
        else // Otherwise, the preceding row exists for adjacency.
            startRow = i - 1;

        if (i + 1 >= ROW) // If clicked button in last row, no subsequent/further row exists.
            endRow = i;
        else // Otherwise, the subsequent row exists for adjacency.
            endRow = i + 1;

        if (j - 1 < 0) // If clicked on first column, lower bound of the column has been reached.
            startColumn = j;
        else
            startColumn = j - 1;

        if (j + 1 >= COL) // If clicked on last column, upper bound of the column has been reached.
            endColumn = j;
        else
            endColumn = j + 1;

        // Search for adjacency for X's and O's or vice versa, and replace them.
        // Update scores for X's and O's accordingly.
        // copy the board
        String[][] newBoard1 = this.copyBoard(board);

        Pair<String[][], Integer> p = new Pair<>(newBoard1, boardValue);
        for (int x = startRow; x <= endRow; x++) {
            p = this.setBoard(p, player, x, j);
        }
        for (int y = startColumn; y <= endColumn; y++) {
            p = this.setBoard(p, player, i, y);
        }

        String[][] newBoard = p.getKey();
        int delta = p.getValue();
        // for center
        if (player.equals(bot)) {
            newBoard[i][j] = bot;
            delta++;
        } else {
            newBoard[i][j] = this.player;
            delta--;
        }
        p = new Pair<>(newBoard, delta);
        return p;
    }

    // set board
    private Pair<String[][], Integer> setBoard(Pair<String[][], Integer> p, String player, int i, int j) {
        int delta = p.getValue();
        String[][] board = p.getKey();
        if (player.equals(this.player)) {
            if (board[i][j].equals(this.bot)) {
                board[i][j] = this.player;
                // x++, o--
                delta -= 2;
            }
        } else if (board[i][j].equals(this.player)) {
            board[i][j] = this.bot;
            // x--, o++
            delta += 2;
        }
        return new Pair<>(board, delta);
    }

    // objective function for board state
    // the objective function is the sum of the values of each row, column, and
    // diagonal where O is +1 and X is -1
    // value = sumOfO - sumOfX
    private int boardValue(String[][] board) {
        int value = 0;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (board[i][j].equals("O")) {
                    value++;
                } else if (board[i][j].equals("X")) {
                    value--;
                }
            }
        }
        return value;
    }

    private String[][] copyBoard(String[][] board) {
        String[][] newBoard = new String[8][8];
        for (int x = 0; x < ROW; x++) {
            System.arraycopy(board[x], 0, newBoard[x], 0, COL);
        }
        return newBoard;
    }

    // minimax algorithm with alpha-beta pruning
    // minFunction is the opponent's move
    // maxFunction is the bot's move
    private int[] minimax(String[][] board, List<int[]> emptySpace, int depth, int boardValue) {
        int alpha = Integer.MIN_VALUE; // -infinity
        int beta = Integer.MAX_VALUE; // +infinity
        System.out.println("alpha: " + alpha + " beta: " + beta);
        int[] res = this.minimaxDecision(board, emptySpace, boardValue, depth, alpha, beta, true);
        System.out.println("finish minmax with res: " + res[0] + " x: " + res[1] + " y: " + res[2]);
        System.out.println("alpha: " + res[3] + " beta: " + res[4]);
        return new int[] { res[1], res[2] };
        // int[] res = this.maxFunction(board, emptySpace, depth, bot, boardValue,
        // alpha, beta);
        // System.out.println("finish minmax with res: " + res[0] + " alpha: " + res[1]
        // + " beta: " + res[2] + " x: " + res[3] + " y: " + res[4]);
        // return new int[]{res[3], res[4]};
    }

    private int[] minimaxDecision(String[][] board, List<int[]> emptySpaces, int bvalue, int depth, int alpha, int beta,
            boolean isMaximizing) {
        if (depth == 0) {
            return new int[] { bvalue, -1, -1, alpha, beta };
        }

        int bestValue;
        int bestX = -1;
        int bestY = -1;
        int newBvalue = boardValue(board); // Why menghitung lagi????

        if (isMaximizing) {
            bestValue = Integer.MIN_VALUE;
            for (int[] space : emptySpaces) {
                int i = space[0];
                int j = space[1];
                Pair<String[][], Integer> p = updateGameBoard(board, bot, newBvalue, i, j);
                List<int[]> newEmptySpaces = new ArrayList<>(emptySpaces); // copy of emptySpaces;

                newEmptySpaces.remove(0);
                int[] res = minimaxDecision(p.getKey(), newEmptySpaces, p.getValue(), depth - 1, alpha, beta, false);
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
                Pair<String[][], Integer> p = updateGameBoard(board, player, newBvalue, i, j);
                List<int[]> newEmptySpaces = new ArrayList<>(emptySpaces); // copy of emptySpaces

                newEmptySpaces.remove(0);
                int[] res = minimaxDecision(p.getKey(), newEmptySpaces, p.getValue(), depth - 1, alpha, beta, true);
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

        return new int[] { bestValue, bestX, bestY, alpha, beta };
    }

    private double schedule(double T) {
        return T - 0.1d;
    }

    private int[] simAnnealing(String[][] board, int depth) {
        // Get empty spaces
        List<int[]> emptySpaces = getEmptySpaces(board);

        // Initialize current successor randomly
        Random rand = new Random();
        int[] currentCoord = emptySpaces.get(rand.nextInt(emptySpaces.size()));
        int bValue = boardValue(board);
        double T = depth;
        while (true) {
            T = schedule(T);
            if (T <= 0.0d) {
                return currentCoord;
            }
            int[] nextCoord = emptySpaces.get(rand.nextInt(emptySpaces.size()));

            Pair<String[][], Integer> currentState = updateGameBoard(board, bot, bValue, currentCoord[0],
                    currentCoord[1]);
            Pair<String[][], Integer> nextState = updateGameBoard(board, bot, bValue, nextCoord[0], nextCoord[1]);

            double deltaE = nextState.getValue() - currentState.getValue();
            // System.out.println("deltaE = " + deltaE);
            // System.out.println("currentCoord = " + currentCoord[0] + " " +
            // currentCoord[1]);
            // System.out.println("nextCoord = " + nextCoord[0] + " " + nextCoord[1]);
            // System.out.println();
            // System.out.println("T = " + T);
            // System.out.println("Threshold = " + threshold);
            if (deltaE > 0) {
                currentCoord[0] = nextCoord[0];
                currentCoord[1] = nextCoord[1];
            } else {
                double threshold = Math.exp(deltaE / T);
                // System.out.println("deltaE = " + deltaE);
                // System.out.println("T = " + T);
                // System.out.println("Threshold = " + threshold);
                if (Math.random() <= threshold) {
                    currentCoord[0] = nextCoord[0];
                    currentCoord[1] = nextCoord[1];
                }
            }

        }
    }

    private int[] stochasticHillClimbing(String[][] board) {
        // Get empty spaces
        List<int[]> emptySpaces = getEmptySpaces(board);

        // Initialize current successor randomly
        Random rand = new Random();
        int[] currentCoord = emptySpaces.get(rand.nextInt(emptySpaces.size()));
        int bValue = boardValue(board);

        //
        int maxIterations = 100;

        // Search loop
        for (int i = 0; i < maxIterations; i++) {
            int[] nextCoord = emptySpaces.get(rand.nextInt(emptySpaces.size()));

            // Find current state value and next state current value
            Pair<String[][], Integer> currentState = updateGameBoard(board, bot, bValue, currentCoord[0],
                    currentCoord[1]);
            Pair<String[][], Integer> nextState = updateGameBoard(board, bot, bValue, nextCoord[0], nextCoord[1]);
            if (currentState.getValue() < nextState.getValue()) {
                currentCoord[0] = nextCoord[0];
                currentCoord[1] = nextCoord[1];
            }
        }
        return currentCoord;
    }

    private int[] steepestHillClimbing(String[][] board) {
        // Get empty spaces
        List<int[]> emptySpaces = getEmptySpaces(board);

        // Initialize current successor with the first empty space coordinates
        int[] currentCoord = emptySpaces.get(0);
        int bValue = boardValue(board);

        // Search loop
        for (int[] nextCoord : emptySpaces) {

            // Find next state and current state values
            Pair<String[][], Integer> currentState = updateGameBoard(board, bot, bValue, currentCoord[0],
                    currentCoord[1]);
            Pair<String[][], Integer> nextState = updateGameBoard(board, bot, bValue, nextCoord[0], nextCoord[1]);

            if (currentState.getValue() < nextState.getValue()) {
                currentCoord[0] = nextCoord[0];
                currentCoord[1] = nextCoord[1];
            }
        }

        return currentCoord;
    }

}