package de.uni_passau.fim.se2.litterbox.refactor.refactorings;

import de.uni_passau.fim.se2.litterbox.JsonTest;
import de.uni_passau.fim.se2.litterbox.analytics.refactorings.SequenceToLoopFinder;
import de.uni_passau.fim.se2.litterbox.ast.ParsingException;
import de.uni_passau.fim.se2.litterbox.ast.model.Program;
import de.uni_passau.fim.se2.litterbox.ast.model.Script;
import de.uni_passau.fim.se2.litterbox.ast.model.StmtList;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.Stmt;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class SequenceToLoopTest implements JsonTest {

    @Test
    public void testSequenceToLoopFinder() throws ParsingException, IOException {
        Program program = getAST("src/test/fixtures/refactoring/unrolledLoop.json");
        SequenceToLoopFinder finder = new SequenceToLoopFinder();
        List<Refactoring> refactorings = finder.check(program);
        assertThat(refactorings).hasSize(1);
        assertThat(refactorings.get(0)).isInstanceOf(SequenceToLoop.class);
    }

    @Test
    public void testSequenceToLoopRefactoring() throws ParsingException, IOException {
        Program program = getAST("src/test/fixtures/refactoring/unrolledLoop.json");
        Script script = program.getActorDefinitionList().getDefinitions().get(1).getScripts().getScriptList().get(0);
        StmtList stmtList = script.getStmtList();
        List<Stmt> subsequence = stmtList.getStmts().subList(1, 3);
        int times = 3;

        SequenceToLoop refactoring = new SequenceToLoop(stmtList, subsequence, times);
        Program refactored = refactoring.apply(program);

        Script refactoredScript = refactored.getActorDefinitionList().getDefinitions().get(1).getScripts().getScriptList().get(0);
        StmtList refactoredStmtList = refactoredScript.getStmtList();
        assertThat(refactoredStmtList.getNumberOfStatements()).isEqualTo(stmtList.getNumberOfStatements() + 1 - times * subsequence.size());
    }
}
