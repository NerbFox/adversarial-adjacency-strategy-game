package core.algorithms;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import core.algorithms.genetic.Chromosome;
import core.algorithms.genetic.ReservationTree;

public class GeneticAlgorithm {
    public int[] GeneticAlgo(String[][] board, List<int[]> emptySpaces) {
        long startTime = System.currentTimeMillis();

        List<Chromosome> chromosomes = new ArrayList<Chromosome>(5);
        for (int i = 0; i < 5; i++) {
            chromosomes.add(new Chromosome(emptySpaces));
        }
        for (int i = 0; i < 5; i++) {
            Collections.shuffle(emptySpaces);
            Chromosome chrome = new Chromosome(emptySpaces);
            chromosomes.set(i, chrome);
        }

        ReservationTree resTree = new ReservationTree();
        int i = 0;
        while (i < 20 && System.currentTimeMillis() - startTime <= 5000) {

            List<Chromosome> parentGeneration = resTree.getTopChromosomes(chromosomes, board);
            List<Chromosome> selected = this.selection(parentGeneration);
            List<Chromosome> crossed = this.crossover(selected);
            List<Chromosome> mutated = this.mutateAll(crossed);

            for (Chromosome chrome : mutated) {
                chrome.setFitness(Integer.MIN_VALUE);
                chromosomes.add(chrome);
            }

            i++;
        }

        List<Chromosome> resList = resTree.getTopChromosomes(chromosomes, board);

        return resList.get(0).getAt(0);
    }

    private double totalFitness(List<Chromosome> chromosomes) {

        double val = 0.0;
        for (Chromosome chrome : chromosomes) {
            val += chrome.getFitness();
        }

        return val;
    }

    private double probability(Chromosome chrome, double total) {
        return (chrome.getFitness() / total);
    }

    private List<Chromosome> selection(List<Chromosome> parentChromosomes) {

        double totalFitness = this.totalFitness(parentChromosomes);
        List<Chromosome> selected = new ArrayList<Chromosome>();

        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int addIdx = 0;

            double randNum = random.nextDouble();

            double p1 = this.probability(parentChromosomes.get(0), totalFitness);
            double p2 = this.probability(parentChromosomes.get(0), totalFitness)
                    + this.probability(parentChromosomes.get(1), totalFitness);
            double p3 = this.probability(parentChromosomes.get(0), totalFitness)
                    + this.probability(parentChromosomes.get(1), totalFitness)
                    + this.probability(parentChromosomes.get(2), totalFitness);
            double p4 = this.probability(parentChromosomes.get(0), totalFitness)
                    + this.probability(parentChromosomes.get(1), totalFitness)
                    + this.probability(parentChromosomes.get(2), totalFitness)
                    + this.probability(parentChromosomes.get(3), totalFitness);

            if (randNum <= p1) {
                // selected.add(parentChromosomes.get(0));
                addIdx = 0;
            } else if ((p1 < randNum) && (randNum <= p2)) {
                // selected.add(parentChromosomes.get(1));
                addIdx = 1;
            } else if ((p2 < randNum) && (randNum <= p3)) {
                // selected.add(parentChromosomes.get(2));
                addIdx = 2;
            } else if ((p3 < randNum) && (randNum <= p4)) {
                // selected.add(parentChromosomes.get(3));
                addIdx = 3;
            }
            Chromosome temp = new Chromosome(parentChromosomes.get(addIdx).getSequence());
            selected.add(temp);
        }

        return selected;
    }

    private void cross(Chromosome a, Chromosome b) {
        Random random = new Random();
        int crossPoint = (int) (random.nextDouble() * a.len());

        for (int i = crossPoint; i < a.len(); i++) {
            int[] temp = a.getAt(i);
            a.replaceAt(i, b.getAt(i));
            b.replaceAt(i, temp);
        }
    }

    private List<Chromosome> crossover(List<Chromosome> selected) {

        for (int i = 0; i < selected.size() - 2; i += 2) {
            this.cross(selected.get(i), selected.get(i + 1));
        }

        return selected;
    }

    private void mutate(Chromosome a) {
        Random random = new Random();
        int rand1 = (int) (random.nextDouble() * a.len());
        int rand2 = (int) (random.nextDouble() * a.len());

        int[] temp = a.getAt(rand1);
        a.replaceAt(rand1, a.getAt(rand2));
        a.replaceAt(rand2, temp);
    }

    private List<Chromosome> mutateAll(List<Chromosome> crossed) {

        List<Chromosome> result = new ArrayList<Chromosome>();
        for (int i = 0; i < crossed.size(); i++) {
            this.mutate(crossed.get(i));
            result.add(crossed.get(i));
        }

        return result;
    }
}