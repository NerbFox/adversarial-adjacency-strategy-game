import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.List;

public class Bot {
    public int[] move(Button[][] b, int rl, boolean isBotFirst) {
        // initialize board
        String[][] board = getBoard(b);
        // initialize values for board arraylist
        List<Integer> boardValues = new ArrayList<Integer>();
        // initialize empty spaces on board
        List<int[]> emptySpaces = getEmptySpaces(board);
        // initialize depth of game
        int depth_game = depthGame(rl, isBotFirst, emptySpaces.size());






        // create random move from empty spaces
        int randomMove = (int) (Math.random()*emptySpaces.size());
        int x = emptySpaces.get(randomMove)[0];
        int y = emptySpaces.get(randomMove)[1];
        return new int[]{x, y};
        // return new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
    }

    // return array of text buttons
    private String[][] getBoard(Button[][] board) {
        String[][] boardText = new String[8][8];
        for (int i = 0; i < boardText.length; i++) {
            for (int j = 0; j < boardText[i].length; j++) {
                boardText[i][j] = board[i][j].getText();
            }
        }
        return boardText;
    }

    // return empty spaces on board list of tuples int
    private List<int[]> getEmptySpaces(String[][] board) {
        List<int[]> emptySpaces = new ArrayList<int[]>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
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

    // move function



    // objective function for board state
    // the objective function is the sum of the values of each row, column, and diagonal where X is +1 and O is -1
    private int boardValue(String[][] board) {
        int value = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
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