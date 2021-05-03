package de.uni_passau.fim.se2.litterbox.analytics.metric;

import de.uni_passau.fim.se2.litterbox.analytics.MetricExtractor;
import de.uni_passau.fim.se2.litterbox.ast.model.Program;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.string.StringExpr;
import de.uni_passau.fim.se2.litterbox.ast.model.literals.StringLiteral;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.spritelook.Say;
import de.uni_passau.fim.se2.litterbox.ast.visitor.ScratchVisitor;
import de.uni_passau.fim.se2.litterbox.utils.Preconditions;

public class SayHelloBlockCount implements MetricExtractor, ScratchVisitor {
    public static final String NAME = "say_hello_block_count";

    private int count = 0;

    @Override
    public double calculateMetric(Program program) {
        Preconditions.checkNotNull(program);
        count = 0;
        program.accept(this);
        return count;
    }

    @Override
    public void visit(Say node) {
        StringExpr expr = node.getString();
        if (expr instanceof StringLiteral) {
            String text = ((StringLiteral) expr).getText();
            if (text.equals("Hello!")) {
                count++;
            }
        }
        visitChildren(node);
    }

    @Override
    public String getName() {
        return NAME;
    }
}
