package de.uni_passau.fim.se2.litterbox.refactor.metaheuristics.chromosomes;

import de.uni_passau.fim.se2.litterbox.refactor.metaheuristics.search_operators.Crossover;
import de.uni_passau.fim.se2.litterbox.refactor.metaheuristics.search_operators.Mutation;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RefactorSequenceGenerator implements ChromosomeGenerator<RefactorSequence> {

    private static final int MAX_REFACTORINGS_PER_SOLUTION = 10;

    private final Mutation<RefactorSequence> mutation;
    private final Crossover<RefactorSequence> crossover;
    private final Random random;

    public RefactorSequenceGenerator(Mutation<RefactorSequence> mutation, Crossover<RefactorSequence> crossover, Random random) {
        this.mutation = mutation;
        this.crossover = crossover;
        this.random = random;
    }

    /**
     * Creates and returns a random chromosome that represents a valid and admissible solution for the problem at hand.
     *
     * @return a random chromosome
     */
    @Override
    public RefactorSequence get() {
        List<Integer> productions = new LinkedList<>();
        int numberOfProductions = random.nextInt(MAX_REFACTORINGS_PER_SOLUTION);
        for (int i = 0; i < numberOfProductions; i++) {
            productions.add(random.nextInt(255));
        }
        return new RefactorSequence(mutation, crossover, productions);
    }
}
