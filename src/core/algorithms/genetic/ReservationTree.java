package core.algorithms.genetic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import core.Board;
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
      List<int[]> sequence = chromosome.getSequence();

      int depth = 1;
      int lastCommonDepth = 1;
      TreeNode currNode = this.root;

      if (currNode.getValue() != chromosome.getFitness()) {
        lastCommonDepth++;
      }

      for (int i = 0; i < sequence.size(); i++) {
        currNode = currNode.getChildren().get(sequence.get(i));
        depth++;

        if (currNode.getValue() != chromosome.getFitness()) {
          lastCommonDepth++;
        }
      }
      chromosome.setFitness(depth - lastCommonDepth + 1);

    });

    Collections.sort(chromosomes, Comparator.comparingInt(Chromosome::getFitness));
    Collections.reverse(chromosomes);

    return chromosomes.subList(0, 4);
  }

  private void updateTreeNode(Chromosome chromosome, String[][] board) {
    List<int[]> sequence = chromosome.getSequence();

    Pair<String[][], Integer> boardVal = new Pair<>(board, Board.boardValue(board));
    TreeNode currNode = this.root;
    boolean isBot = true;

    for (int i = 0; i < sequence.size(); i++) {
      int[] currMove = sequence.get(i);
      if (currNode.getChildren().containsKey(currMove)) {
        currNode = currNode.getChildren().get(currMove);
      } else {
        currNode.getChildren().put(currMove, new TreeNode(isBot));
        currNode = currNode.getChildren().get(currMove);
      }

      String currChar = isBot ? "O" : "X";
      boardVal = Board.updateGameBoard(boardVal.getKey(), currChar, boardVal.getValue(), currMove[0], currMove[1]);

      if (i == sequence.size() - 1) {
        currNode.setValue(boardVal.getValue());
        chromosome.setFitness(currNode.getValue());
      }

      isBot = !isBot;
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
}
