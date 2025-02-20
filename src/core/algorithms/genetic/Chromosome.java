package core.algorithms.genetic;
import java.util.ArrayList;
import java.util.List;

public class Chromosome {
  private List<int[]> sequence;
  private int fitnessValue;

  public Chromosome(List<int[]> sequence) {
    // this.sequence = sequence;
    this.sequence = new ArrayList<int[]>();
    for (int i = 0; i < sequence.size(); i++) {
      this.sequence.add(sequence.get(i));
    }
    this.fitnessValue = Integer.MIN_VALUE;
  }

  public List<int[]> getSequence() {
    return this.sequence;
  }

  public int[] getAt(int i) {
    return this.sequence.get(i);
  }

  public int getFitness() {
    return this.fitnessValue;
  }

  public void setSequence(ArrayList<int[]> sequence) {
    this.sequence = sequence;
  }

  public void replaceAt(int i, int[] val) {
    this.sequence.set(i, val);
  }

  public void setFitness(int f) {
    this.fitnessValue = f;
  }

  public int len() {
    return sequence.size();
  }
}
