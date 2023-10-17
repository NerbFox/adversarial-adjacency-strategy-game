import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.random;
import Chromosome;


public class GeneticAlgorithm {
    public static GeneticAlgo(board, emptySpaces) {
        long startTime = System.currentTimeMillis();

        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        for (int i = 0; i < 5; i++) {
            Chromosome chrome = new Chromosome(Collections.shuffle(emptySpaces));
            chromosomes.add(chrome);
        }

        ReservationTree resTree = new ResetvationTree();
        int i = 0;
        while (i < 3 && System.currentTimeMillis() - startTime <= 5000) {
            List<Chromosome> parentGeneration = resTree.getTopChromosomes(chromosomes, board);
            List<Chromosome> selected = this.selection(parentGeneration);
            List<Chromosome> crossed = this.crossover(selected);
            chromosomes = this.mutateAll(crossed);

            i++;
        }

        List<Chromosome> resList = new List<Chromosome>();
        resList = resTree.getTopChromosomes(resList, board);

        return resList[0].getAt(0);
    }

    private double totalFitness(List<Chromosome> chromosomes) {
        
        double val = 0;
        for (Chromosome chrome : chromosomes) {
            val += chrome.getFitness();
        }

        return val;
    }

    private double probability(Chromosome chrome, double total) {
        return (chrome.getFitness() / total);
    }

    private List<Chromosome> selection(List<Chromosome> parentChromosomes) {
        Random random = new Random();
        double randNum = random.nextDouble();

        double totalFitness = this.totalFitness(parentChromosomes);
        List<Chromosome> selected = new ArrayList<Chromosome>();

        for (int i = 0; i < 4; i++) {
            if (randNum <= this.probability(parentChromosomes[0], totalFitness)) {
                selected.add(parentChromosomes[0]);
            } else if (this.probability(parentChromosomes[0], totalFitness) < randNum <= this.probability(parentChromosomes[1], totalFitness)) {
                selected.add(parentChromosomes[1]);
            } else if (this.probability(parentChromosomes[1], totalFitness) < randNum <= this.probability(parentChromosomes[2], totalFitness)) {
                selected.add(parentChromosomes[2]);
            } else if (this.probability(parentChromosomes[2], totalFitness) < randNum <= this.probability(parentChromosomes[3], totalFitness)) {
                selected.add(parentChromosomes[3]);
            } else if (this.probability(parentChromosomes[3], totalFitness) < randNum <= this.probability(parentChromosomes[4], totalFitness)) {
                selected.add(parentChromosomes[4]);
            }
        }

        return selected;
    }

    private void cross(Chromosome a, Chromosome b) {
        Random random = new Random();
        int crossPoint = (int) (random.nextDouble() * a.len());

        for (int i = crossPoint; i < a.len(); i++) {
            int temp = a.getAt(i);
            a.replaceAt(i, b.getAt(i));
            b.replaceAt(i, temp);
        }
    }

    private List<Chromosome> crossover(List<Chromosome> selected) {
        
        for (int i = 0; i < selected.size() - 2; i += 2) {
            this.cross(selected[i], selected[i+1]);
        }

        return selected;
    }

    private void mutate(Chromosome a) {
        Random random = new Random();
        int rand1 = (int) (random.nextDouble() * a.size());
        int rand2 = (int) (random.nextDouble() * a.size());

        int[] temp = a.getAt(rand1);
        a.replaceAt(rand1, a.getAt(rand2));
        a.replaceAt(rand2, temp);
    }

    private List<Chromosome> mutateAll(List<Chromosome> crossed) {
        
        List<Chromosome> result = new ArrayList<Chromosome>();
        for (int i = 0; i < crossed.size(); i++) {
            this.muatate(crossed[i])
            result.add(crossed[i]);
        }

        return result;
    }
}