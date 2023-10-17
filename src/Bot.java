import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import java.lang.Math;
import java.util.Random;

public class Bot {
    private static final int ROW = 8;
    private static final int COL = 8;
    public static final String bot = "O";
    public static final String player = "X";

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
        MinimaxAlgorithm mm = new MinimaxAlgorithm();
        int[] res = mm.minimax(board, emptySpaces, depth_game, boardValue);
//        GeneticAlgorithm ga = new GeneticAlgorithm();
//        int[] res = ga.GeneticAlgo(board, emptySpaces);
        // end time
        long endTime = System.nanoTime();
        // print time
        System.out.println("time: " + (endTime - startTime) / 1000000 + " ms");

        int x = res[0];
        int y = res[1];
        System.out.println("x" + x);
        System.out.println("y" + y);
        return new int[]{x, y};
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
//        return getEmptySpacesNormal(board);
        // yg heuristic cuma untuk minimax
        return getEmptySpacesHeuristic(board);
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

    // Heuristic function for empty spaces (empty spaces that have a piece on the left, right, up, or down)
    private List<int[]> getEmptySpacesHeuristic(String[][] board) {
        List<int[]> emptySpaces = new ArrayList<>();
        boolean moreSpace = true;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (board[i][j].equals("")) {
                    if (i - 1 >= 0) { // check if the bot or player is on the left
                        if (isBotOrPlayer(board[i - 1][j])) {
                            emptySpaces.add(new int[] { i, j }); continue;
                        }
                    }  if (i + 1 < ROW) { // check if the bot or player is on the right
                        if (isBotOrPlayer(board[i + 1][j])) {
                            emptySpaces.add(new int[] { i, j }); continue;
                        }

                    }  if (j - 1 >= 0) { // check if the bot or player is on the up
                        if (isBotOrPlayer(board[i][j - 1])) {
                            emptySpaces.add(new int[] { i, j }); continue;
                        }
                    }  if (j + 1 < COL) { // check if the bot or player is on the down
                        if (isBotOrPlayer(board[i][j + 1])) {
                            emptySpaces.add(new int[] { i, j });
                        }
                    }
//                     if (moreSpace && !add) {
//                     // add one more empty space that not have a piece on the left, right, up, or down
//                     moreSpace = false;
//                     emptySpaces.add(new int[]{i, j});
//                     }

                }
            }
        }
//        print empty spaces
        for (int[] emptySpace : emptySpaces) {
            System.out.println("empty space: " + emptySpace[0] + " " + emptySpace[1]);
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
    public static Pair<String[][], Integer> updateGameBoard(String[][] board, String player, int boardValue, int i, int j) {
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
        String[][] newBoard1 = copyBoard(board);

        Pair<String[][], Integer> p = new Pair<>(newBoard1, boardValue);
        for (int x = startRow; x <= endRow; x++) {
            p = setBoard(p, player, x, j);
        }
        for (int y = startColumn; y <= endColumn; y++) {
            p = setBoard(p, player, i, y);
        }

        String[][] newBoard = p.getKey();
        int delta = p.getValue();
        // for center
        if (player.equals(bot)) {
            newBoard[i][j] = bot;
            delta++;
        } else {
            newBoard[i][j] = Bot.player;
            delta--;
        }
        p = new Pair<>(newBoard, delta);
        return p;
    }

    // set board
    private static Pair<String[][], Integer> setBoard(Pair<String[][], Integer> p, String player, int i, int j) {
        int delta = p.getValue();
        String[][] board = p.getKey();
        if (player.equals(Bot.player)) {
            if (board[i][j].equals(Bot.bot)) {
                board[i][j] = Bot.player;
                // x++, o--
                delta -= 2;
            }
        } else if (board[i][j].equals(Bot.player)) {
            board[i][j] = Bot.bot;
            // x--, o++
            delta += 2;
        }
        return new Pair<>(board, delta);
    }

    // the objective function is the sum of the values of each row, column, and diagonal
    // where O is +1 and X is -1, value = sumOfO - sumOfX
    public static int boardValue(String[][] board) {
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
    private static String[][] copyBoard(String[][] board) {
        String[][] newBoard = new String[8][8];
        for (int x = 0; x < ROW; x++) {
            System.arraycopy(board[x], 0, newBoard[x], 0, COL);
        }
        return newBoard;
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