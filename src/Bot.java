import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.List;

public class Bot {
    public int[] move(Button[][] b, int roundleft) {
        // initialize board
        String[][] board = getBoard(b);
        // initialize values for board arraylist
        List<Integer> boardValues = new ArrayList<Integer>();
        // initialize empty spaces on board
        List<int[]> emptySpaces = getEmptySpaces(board);


        // display round left
        System.out.println("Round left: " + roundleft);
        // display board state
        System.out.println("Board state:");
        for (int i = 0; i < board.length; i++) {
            System.out.print("  ");
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();


        // create random move from empty spaces
        int randomMove = (int) (Math.random()*emptySpaces.size());
        int x = emptySpaces.get(randomMove)[0];
        int y = emptySpaces.get(randomMove)[1];
        return new int[]{x, y};
//        return new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
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


}
