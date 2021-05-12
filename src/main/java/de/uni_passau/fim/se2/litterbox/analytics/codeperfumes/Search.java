package de.uni_passau.fim.se2.litterbox.analytics.codeperfumes;

import de.uni_passau.fim.se2.litterbox.analytics.AbstractIssueFinder;
import de.uni_passau.fim.se2.litterbox.analytics.IssueSeverity;
import de.uni_passau.fim.se2.litterbox.analytics.IssueType;
import de.uni_passau.fim.se2.litterbox.ast.model.StmtList;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.common.Broadcast;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.common.StopOtherScriptsInSprite;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.control.IfElseStmt;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.control.IfThenStmt;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.control.RepeatForeverStmt;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.termination.StopAll;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.termination.StopThisScript;

/**
 * The Search pattern means inside a loop a certain condition is checked (if-statement). If the condition
 * is met, a stop-block or a broadcast is triggered.
 */
public class Search extends AbstractIssueFinder {

    public static final String NAME = "search";
    private boolean inLoop = false;

    @Override
    public void visit(RepeatForeverStmt node) {
        inLoop = true;

        // makes sense only for loops that contain other blocks along with the if-stmt
        if (node.getStmtList().getStmts().size() > 1) {
            visitChildren(node);
        }
        inLoop = false;
    }

    @Override
    public void visit(IfElseStmt node) {
        if (inLoop) {
            checkStmtList(node.getStmtList());
        }
    }

    @Override
    public void visit(IfThenStmt node) {
        if (inLoop) {
            checkStmtList(node.getThenStmts());
        }
    }

    private void checkStmtList(StmtList node) {
        node.getStmts().forEach(stmt -> {
            if (stmt instanceof StopAll || stmt instanceof StopThisScript
                    || stmt instanceof StopOtherScriptsInSprite || stmt instanceof Broadcast) {
                addIssue(stmt, stmt.getMetadata(), IssueSeverity.MEDIUM);
            }
        });
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public IssueType getIssueType() {
        return IssueType.PERFUME;
    }
}
