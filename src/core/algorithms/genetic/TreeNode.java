package core.algorithms.genetic;

import java.util.HashMap;
import java.util.Map;

public class TreeNode {
  private int value;
  private boolean isMax;
  private Map<int[], TreeNode> children;

  public TreeNode(boolean isMax) {
    this.value = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
    this.isMax = isMax;
    this.children = new HashMap<>();
  }

  public void addChild(int[] branch, TreeNode child) {
    this.children.put(branch, child);
  }

  public Map<int[], TreeNode> getChildren() {
    return this.children;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public int getValue() {
    return this.value;
  }

  public boolean isMaximizer() {
    return this.isMax;
  }
}
