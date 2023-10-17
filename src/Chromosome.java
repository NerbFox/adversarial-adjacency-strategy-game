import java.util.ArrayList;
import java.util.List;

public class Chromosome {
    private ArrayList<int[]> sequence;
    private int fitnessValue;

    public Chromosome(List<int[]> sequence) {
        this.sequence = sequence;
        this.fitnessValue = Integer.MIN_VALUE;
    }

    public ArrayList<int[]> getSequence() {
        return this.sequence;
    }

    public int[] getAt(int i) {
        return this.sequence[i];
    }

    public int getFitness() {
        return this.fitnessValue;
    }

    public void setSequence(ArrayList<int[]> sequence) {
        this.sequence = sequence;
    }

    public void replaceAt(int i, int[] val) {
        this.sequence[i] = val;
    }

    public void setFitness(int f) {
        this.fitnessValue = f;
    }

    public int len() {
        return sequence.size();
    }
}