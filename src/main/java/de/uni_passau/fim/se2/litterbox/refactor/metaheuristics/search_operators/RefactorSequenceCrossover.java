package de.uni_passau.fim.se2.litterbox.refactor.metaheuristics.search_operators;

import de.uni_passau.fim.se2.litterbox.refactor.metaheuristics.chromosomes.Chromosome;
import de.uni_passau.fim.se2.litterbox.refactor.metaheuristics.chromosomes.RefactorSequence;
import de.uni_passau.fim.se2.litterbox.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RefactorSequenceCrossover implements Crossover<RefactorSequence> {

    private final Random random;

    public RefactorSequenceCrossover(Random random) {
        this.random = random;
    }

    /**
     * Applies this crossover operator to the two given non-null parent chromosomes {@code parent1}
     * and {@code parent2}, and returns the resulting pair of offspring chromosomes.
     * <p>
     * Note: an offspring can equal one of its parents (in terms of {@link Chromosome#equals
     * equals()}. While not an absolute requirement, it is generally advisable parents and offspring
     * be different in terms of reference equality ({@code offspring != parent}) as it tends to
     * simplify the implementation of some search algorithms.
     *
     * @param parent1 a parent
     * @param parent2 another parent
     * @return the offspring formed by applying crossover to the two parents
     * @throws NullPointerException if an argument is {@code null}
     */
    @Override
    public Pair<RefactorSequence> apply(RefactorSequence parent1, RefactorSequence parent2) {
        RefactorSequence child1 = parent1.copy();
        RefactorSequence child2 = parent2.copy();

        // TODO exclude cases 0 and size - 1? if so, how to handle chromosomes with length == 1
        int crossoverPoint = child1.getProductions().isEmpty() ? random.nextInt(child1.getProductions().size()) : 0;

        List<Integer> child1List = new ArrayList<>(child1.getProductions().subList(0, crossoverPoint));
        List<Integer> child2List = new ArrayList<>(child2.getProductions().subList(0, crossoverPoint));

        child1List.addAll(new ArrayList<>(child2.getProductions().subList(crossoverPoint, child2.getProductions().size())));
        child2List.addAll(new ArrayList<>(child1.getProductions().subList(crossoverPoint, child1.getProductions().size())));

        child1.getProductions().clear();
        child2.getProductions().clear();

        child1.getProductions().addAll(child1List);
        child2.getProductions().addAll(child2List);

        return Pair.of(child1, child2);
    }
}
