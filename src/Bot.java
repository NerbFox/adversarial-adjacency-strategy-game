import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import java.lang.Math;

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
        System.out.println("board value a: " + boardValue);
        // print depth of game
        System.out.println("max depth: " + depth_game);
        printBoard(board);
        // start time
        long startTime = System.nanoTime();
        int[] res = this.minimaxDecision(board, emptySpaces, depth_game, boardValue);
        // end time
        long endTime = System.nanoTime();
        // print time
        System.out.println("time: " + (endTime - startTime)/1000000 + " ms");

        int x = res[0];
        int y = res[1];
        System.out.println("x" + x);
        System.out.println("y" + y);

        // create random move from empty spaces
//        int randomMove = (int) (Math.random()*emptySpaces.size());
//        x = emptySpaces.get(randomMove)[0];
//        y = emptySpaces.get(randomMove)[1];
//        System.out.println("before");
//        printBoard(board);
//        Pair<String[][], Integer> p = updateGameBoard(board, bot, boardValue, x, y);
//        System.out.println("board value: " + p.getValue());
//        System.out.println("after");
//        printBoard(p.getKey());
        return new int[]{x, y};
        // return new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
    }

    // print board
    private void printBoard(String[][] board) {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++){
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    // return array of text buttons
    private String[][] getBoard(Button[][] board) {
        String[][] boardText = new String[8][8];
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++){
                boardText[i][j] = board[i][j].getText();
            }
        }
        return boardText;
    }

    // return empty spaces on board list of tuples int
    private List<int[]> getEmptySpaces(String[][] board) {
        List<int[]> emptySpaces = new ArrayList<int[]>();
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++){
                if (board[i][j].equals("")) {
                    emptySpaces.add(new int[]{i, j});
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
        // if the size of empty spaces < depth of game, then the depth of the game will be the size of empty spaces
        if (sizeEmptySpaces < depth_game) {
            depth_game = sizeEmptySpaces;
        }
        return depth_game;
    }

    // move function, return the board and board value
    private Pair<String[][], Integer> updateGameBoard(String[][] board, String player, int boardValue, int i, int j) {
        // Value of indices to control the lower/upper bound of rows and columns
        // in order to change surrounding/adjacent X's and O's only on the game board.
        // Four boundaries:  First & last row and first & last column.

        int startRow, endRow, startColumn, endColumn;

        if (i - 1 < 0)     // If clicked button in first row, no preceding row exists.
            startRow = i;
        else               // Otherwise, the preceding row exists for adjacency.
            startRow = i - 1;

        if (i + 1 >= ROW)  // If clicked button in last row, no subsequent/further row exists.
            endRow = i;
        else               // Otherwise, the subsequent row exists for adjacency.
            endRow = i + 1;

        if (j - 1 < 0)     // If clicked on first column, lower bound of the column has been reached.
            startColumn = j;
        else
            startColumn = j - 1;

        if (j + 1 >= COL)  // If clicked on last column, upper bound of the column has been reached.
            endColumn = j;
        else
            endColumn = j + 1;

        // Search for adjacency for X's and O's or vice versa, and replace them.
        // Update scores for X's and O's accordingly.
        // copy the board
        String[][] newBoard1 = new String[8][8];
        for (int x = 0; x < ROW; x++) {
            for (int y = 0; y < COL; y++){
                newBoard1[x][y] = board[x][y];
            }
        }

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
    // the objective function is the sum of the values of each row, column, and diagonal where O is +1 and X is -1
    // value = sumOfO - sumOfX
    private int boardValue(String[][] board) {
        int value = 0;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++){
                if (board[i][j].equals("O")) {
                    value++;
                } else if (board[i][j].equals("X")) {
                    value--;
                }
            }
        }
        return value;
    }

    private List<int[]> copyEmptySpaces(List<int[]> emptySpaces) {
        List<int[]> newEmptySpaces = new ArrayList<int[]>();
        for (int[] space : emptySpaces) {
            newEmptySpaces.add(new int[]{space[0], space[1]});
        }
        return newEmptySpaces;
    }

    private String[][] copyBoard(String[][] board) {
        String[][] newBoard = new String[8][8];
        for (int x = 0; x < ROW; x++) {
            System.arraycopy(board[x], 0, newBoard[x], 0, COL);
        }
        return newBoard;
    }

    private int[] minimaxDecision2(String[][] board, List<int[]> emptySpaces, int bvalue, int depth, int alpha, int beta, boolean isMaximizing) {
        if (depth == 0) {
            return new int[]{bvalue, -1, -1, alpha, beta};
        }

        int bestValue;
        int bestX = -1;
        int bestY = -1;
        int newBvalue = boardValue(board);

        if (isMaximizing) {
            bestValue = Integer.MIN_VALUE;
            for (int[] space : emptySpaces) {
//                System.out.println("length: " + emptySpaces.size());
                int i = space[0];
                int j = space[1];
                Pair<String[][], Integer> p = updateGameBoard(board, bot, newBvalue, i, j);
                List<int[]> newEmptySpaces = new ArrayList<>(emptySpaces); // copy of emptySpaces;



                newEmptySpaces.remove(0);
                int[] res = minimaxDecision2(p.getKey(), newEmptySpaces, p.getValue(), depth - 1, alpha, beta, false);
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
                // copy the emptySpaces, (
//                newEmptySpaces = copyEmptySpaces(emptySpaces);

                newEmptySpaces.remove(0);
                int[] res = minimaxDecision2(p.getKey(), newEmptySpaces, p.getValue(), depth - 1, alpha, beta, true);
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

        return new int[]{bestValue, bestX, bestY, alpha, beta};
    }



    // minimax algorithm with alpha-beta pruning
    // minFunction is the opponent's move
    // maxFunction is the bot's move
    private int[] minimaxDecision(String[][] board, List<int[]> emptySpace, int depth, int boardValue) {
        int alpha = Integer.MIN_VALUE; // -infinity
        int beta = Integer.MAX_VALUE; // +infinity
        System.out.println("alpha: " + alpha + " beta: " + beta);
        int[] res = this.minimaxDecision2(board, emptySpace, boardValue, depth, alpha, beta, true);
        System.out.println("finish minmax with res: " + res[0] + " x: " + res[1] + " y: " + res[2]);
        System.out.println("alpha: " + res[3] + " beta: " + res[4]);
        return new int[]{res[1], res[2]};
//        int[] res = this.maxFunction(board, emptySpace, depth, bot, boardValue, alpha, beta);
//        System.out.println("finish minmax with res: " + res[0] + " alpha: " + res[1] + " beta: " + res[2] + " x: " + res[3] + " y: " + res[4]);
//        return new int[]{res[3], res[4]};
    }

    // maxFunction is the bot's move, return the board value, alpha, and beta
    private int[] maxFunction(String[][] board, List<int[]> emptySpace, int depth, String player, int boardValue, int alpha, int beta) {
        System.out.println("mx depth: " + depth);
//        System.out.println("max empty space: ");
//        for (int[] space : emptySpace) {
//            System.out.print("[" + space[0] + ", " + space[1] + "] ");
//        }
//        System.out.println();
        if (depth == 0 ) {
            System.out.println("max depth == 0");
            return new int[]{boardValue, alpha, beta, -1, -1};
        }

        // maxFunction is the bot's move
        int bestValue = Integer.MIN_VALUE;
        int bestMoveX = 0;
        int bestMoveY = 0;
        for (int[] space : emptySpace) {
//            print len of empty space
            System.out.println("max empty space len 1: " + emptySpace.size());
            int i = space[0];
            int j = space[1];
            Pair<String[][], Integer> p = this.updateGameBoard(board, this.bot, boardValue, i, j);
            String[][] newBoard = p.getKey();
            int newBoardValue = p.getValue();

            // list of empty spaces, remove the first empty space
            List<int[]> copyEmptySpace = new ArrayList<>(emptySpace); // Create a copy of the list
            copyEmptySpace.remove(0);

            int[] res = this.minFunction(newBoard, copyEmptySpace, depth - 1, player, newBoardValue, alpha, beta);
            int v = res[0];
            alpha = res[1];
            beta = res[2];
            if (v > bestValue) {
                bestValue = v;
                bestMoveX = i;
                bestMoveY = j;
            }
            // undo the move
//            copyEmptySpace.add(0, space);

            if (bestValue > alpha) {
                alpha = bestValue;
            }
            if (beta <= alpha) {
                System.out.println("prune");
                break;
            }
            System.out.println("max empty space len 2: " + emptySpace.size());
        }
        return new int[]{bestValue, alpha, beta, bestMoveX, bestMoveY};
    }

    // minFunction is the opponent's move, return the board value, alpha, and beta
    private int[] minFunction(String[][] board, List<int[]> emptySpace, int depth, String player, int boardValue, int alpha, int beta) {
        System.out.println("mn depth: " + depth);
//        System.out.println("min empty space: ");
//        for (int[] space : emptySpace) {
//            System.out.print("[" + space[0] + ", " + space[1] + "] ");
//        }
//        System.out.println();
        // if depth = 0 (terminal state), then return
        if (depth == 0) {
            System.out.println("min depth == 0");
            return new int[]{boardValue, alpha, beta};
        }

        // minFunction is the opponent's move
        int bestValue = Integer.MAX_VALUE;
        for (int[] space : emptySpace) {
            int i = space[0];
            int j = space[1];
            Pair<String[][], Integer> p = this.updateGameBoard(board, this.player, boardValue, i, j);
            String[][] newBoard = p.getKey();
            int newBoardValue = p.getValue();

            // list of empty spaces, remove the first empty space
            List<int[]> copyEmptySpace = new ArrayList<>(emptySpace); // Create a copy of the list
            copyEmptySpace.remove(0);
            int[] res = this.maxFunction(newBoard, copyEmptySpace, depth - 1, bot, newBoardValue, alpha, beta);
            int v = res[0];
            alpha = res[1];
            beta = res[2];
            if (v < bestValue) {
                bestValue = v;
            }
            // undo the move
//            copyEmptySpace.add(0, space);

            if (bestValue < beta) {
                beta = bestValue;
            }
            if (beta <= alpha) {
                System.out.println("beta <= alpha");
                break;
            }
        }
        return new int[]{bestValue, alpha, beta};
    }


}