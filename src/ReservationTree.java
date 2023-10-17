
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javafx.util.Pair;

public class ReservationTree {
  TreeNode root;

  public ReservationTree() {
    this.root = new TreeNode(true);
  }

  public List<Chromosome> getTopChromosomes(List<Chromosome> chromosomes, String[][] board) {
    chromosomes.stream().forEach(chromosome -> {
      this.updateTreeNode(chromosome, board);
    });

    this.updateTreeValue(this.root);

    chromosomes.stream().forEach(chromosome -> {
      ArrayList<int[]> sequence = chromosome.getSequence();

      int depth = 1;
      int lastCommonDepth = 1;
      TreeNode currNode = this.root;

      for (int i = 0; i < sequence.size(); i++) {
        currNode = currNode.getChildren().get(sequence.get(i));
        depth++;

        if (currNode.getValue() != chromosome.getFitness()) {
          lastCommonDepth++;
        }
      }

      chromosome.setFitness(depth-lastCommonDepth+1);
    });

    Collections.sort(chromosomes, Comparator.comparingInt(Chromosome::getFitness));
    return chromosomes;
  }

  private void updateTreeNode(Chromosome chromosome, String[][] board) {
    ArrayList<int[]> sequence = chromosome.getSequence();

    Pair<String[][], Integer> boardVal = new Pair<>(board, this.boardValue(board));
    TreeNode currNode = this.root;
    boolean isBot = true;

    for (int i = 0; i < sequence.size(); i++) {
      int[] currMove = sequence.get(i);
      if (currNode.getChildren().containsKey(currMove)) {
        currNode = currNode.getChildren().get(currMove);
      } else {
        currNode = currNode.getChildren().put(currMove, new TreeNode(isBot));
      }

      String currChar = isBot ? "O" : "X";
      boardVal = updateGameBoard(boardVal.getKey(), currChar, boardVal.getValue(), currMove[0], currMove[1]);

      if (i == sequence.size() - 1) {
        currNode.setValue(boardVal.getValue());
        chromosome.setFitness(currNode.getValue());
      }
    }
  }

  private int updateTreeValue(TreeNode currNode) {
    if (currNode.getChildren().isEmpty()) {
      return currNode.getValue();
    }

    currNode.getChildren().entrySet().stream().forEach(branchMap -> {
      TreeNode child = branchMap.getValue();
      int childValue = this.updateTreeValue(child);

      if (currNode.isMaximizer() && childValue > currNode.getValue()) {
        currNode.setValue(childValue);
      } else if (!currNode.isMaximizer() && childValue < currNode.getValue()) {
        currNode.setValue(childValue);
      }
    });

    return currNode.getValue();
  }

  // ! INI FUNGSI HARUSNYA NTAR DIPINDAHIN
  private int boardValue(String[][] board) {
    int value = 0;
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (board[i][j].equals("O")) {
          value++;
        } else if (board[i][j].equals("X")) {
          value--;
        }
      }
    }
    return value;
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

    if (i + 1 >= 8) // If clicked button in last row, no subsequent/further row exists.
      endRow = i;
    else // Otherwise, the subsequent row exists for adjacency.
      endRow = i + 1;

    if (j - 1 < 0) // If clicked on first column, lower bound of the column has been reached.
      startColumn = j;
    else
      startColumn = j - 1;

    if (j + 1 >= 8) // If clicked on last column, upper bound of the column has been reached.
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
    if (player.equals("O")) {
      newBoard[i][j] = "O";
      delta++;
    } else {
      newBoard[i][j] = "X";
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
    return new Pair<>(board, delta);
  }

  private String[][] copyBoard(String[][] board) {
    String[][] newBoard = new String[8][8];
    for (int x = 0; x < 8; x++) {
      System.arraycopy(board[x], 0, newBoard[x], 0, 8);
    }
    return newBoard;
  }
}
