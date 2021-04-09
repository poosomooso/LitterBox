package de.uni_passau.fim.se2.litterbox.analytics.goodpractices;

import de.uni_passau.fim.se2.litterbox.analytics.AbstractIssueFinder;
import de.uni_passau.fim.se2.litterbox.analytics.IssueSeverity;
import de.uni_passau.fim.se2.litterbox.analytics.IssueType;
import de.uni_passau.fim.se2.litterbox.ast.model.Script;
import de.uni_passau.fim.se2.litterbox.ast.model.StmtList;
import de.uni_passau.fim.se2.litterbox.ast.model.event.GreenFlag;
import de.uni_passau.fim.se2.litterbox.ast.model.literals.NumberLiteral;
import de.uni_passau.fim.se2.litterbox.ast.model.procedure.ProcedureDefinition;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.Stmt;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.spritemotion.*;

/**
 * This checks for an initialization for the sprite location. This initialization should usually happen in a
 * GreenFlag script or a CustomBlock. As initial position for the sprite we set X = 0, Y = 0.
 */
public class InitializeLocation extends AbstractIssueFinder {
    public static final String NAME = "initialize_location";
    private boolean initializedX = false;
    private boolean initializedY = false;
    private final int INIT_STATE = 0;

    @Override
    public void visit(Script node) {
        if (node.getEvent() instanceof GreenFlag) {
            visitChildren(node);
        }
    }

    @Override
    public void visit(ProcedureDefinition node) {
        visitChildren(node);
    }

    @Override
    public void visit(StmtList node) {
        for (Stmt stmt : node.getStmts()) {
            if (stmt instanceof GoToPosXY) {
                this.visit((GoToPosXY) stmt);
            } else if (stmt instanceof SetXTo){
                this.visit((SetXTo) stmt);
            } else if (stmt instanceof SetYTo) {
                this.visit((SetYTo) stmt);
            } else {
                visitChildren(node);
            }
        }
    }

    @Override
    public void visit(SetXTo stmt) {
        if (stmt.getNum() instanceof NumberLiteral) {
            if ((((NumberLiteral) stmt.getNum()).getValue() == INIT_STATE)) {
                initializedX = true;
                if (initializedX && initializedY) {
                    addIssue(stmt, stmt.getMetadata(), IssueSeverity.MEDIUM);
                    initializedX = false;
                    initializedY = false;
                }
            }
        }
    }

    @Override
    public void visit(SetYTo stmt) {
        if (stmt.getNum() instanceof NumberLiteral) {
            if ((((NumberLiteral) stmt.getNum()).getValue() == INIT_STATE)) {
                initializedY = true;
                if (initializedX && initializedY) {
                    addIssue(stmt, stmt.getMetadata(), IssueSeverity.MEDIUM);
                    initializedX = false;
                    initializedY = false;
                }
            }
        }
    }

    @Override
    public void visit(GoToPosXY stmt) {
        if (stmt.getX() instanceof NumberLiteral
                && stmt.getY() instanceof NumberLiteral) {
            if ((((NumberLiteral) stmt.getX()).getValue() == INIT_STATE &&
                    ((NumberLiteral) stmt.getX()).getValue() == INIT_STATE)) {
                addIssue(stmt, stmt.getMetadata(), IssueSeverity.MEDIUM);
            }
        }
    }
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public IssueType getIssueType() {
        return IssueType.GOOD_PRACTICE;
    }
}
