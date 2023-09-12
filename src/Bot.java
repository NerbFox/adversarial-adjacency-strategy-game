import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
public class Bot {
    private static final int ROW = 8;
    private static final int COL = 8;
    private String bot = "O";
    private String player = "X";
    public int[] move(Button[][] b, int rl, boolean isBotFirst) {
        // initialize board
        String[][] board = getBoard(b);
        // initialize values for board arraylist
        List<Integer> boardValues = new ArrayList<Integer>();
        // initialize empty spaces on board
        List<int[]> emptySpaces = getEmptySpaces(board);
        // initialize depth of game
        int depth_game = depthGame(rl, isBotFirst, emptySpaces.size());
        // initialize board value
        int boardValue = boardValue(board);
        System.out.println("board value a: " + boardValue);




        // create random move from empty spaces
        int randomMove = (int) (Math.random()*emptySpaces.size());
        int x = emptySpaces.get(randomMove)[0];
        int y = emptySpaces.get(randomMove)[1];
        System.out.println("before");
        printBoard(board);
        Pair<String[][], Integer> p = updateGameBoard(board, bot, boardValue, x, y);
        System.out.println("board value: " + p.getValue());
        System.out.println("after");
        printBoard(p.getKey());
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
        Pair<String[][], Integer> p = new Pair<String[][], Integer>(board, boardValue);
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
            newBoard[i][j] = player;
            delta--;
        }
        p = new Pair<>(newBoard, delta);
        return p;
    }

    // set board
    private Pair<String[][], Integer> setBoard(Pair<String[][], Integer> p, String player, int i, int j) {
        int delta = p.getValue();
        String[][] board = p.getKey();
        if (player.equals("X")) {
            if (board[i][j].equals("O")) {
                board[i][j] = "X";
                // x++, o--
                delta -= 2;
            }
        } else if (board[i][j].equals("X")) {
            board[i][j] = "O";
            // x--, o++
            delta += 2;
        }
        return new Pair<String[][], Integer>(board, delta);
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


    // minimax algorithm with alpha-beta pruning
    // minFunction is the opponent's move
    // maxFunction is the bot's move

}