package de.uni_passau.fim.se2.litterbox.refactor.refactorings;

import de.uni_passau.fim.se2.litterbox.JsonTest;
import de.uni_passau.fim.se2.litterbox.analytics.refactorings.MergeScriptsFinder;
import de.uni_passau.fim.se2.litterbox.analytics.refactorings.SplitScriptFinder;
import de.uni_passau.fim.se2.litterbox.ast.ParsingException;
import de.uni_passau.fim.se2.litterbox.ast.model.Program;
import de.uni_passau.fim.se2.litterbox.ast.model.Script;
import de.uni_passau.fim.se2.litterbox.ast.model.StmtList;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.common.WaitSeconds;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class MergeScriptsTest implements JsonTest {

    @Test
    public void testMergeScriptsFinder_Bidirectional() throws ParsingException, IOException {
        Program program = getAST("src/test/fixtures/refactoring/mergableBothWays.json");
        MergeScriptsFinder finder = new MergeScriptsFinder();
        List<Refactoring> refactorings = finder.check(program);
        assertThat(refactorings).hasSize(2);
        assertThat(refactorings.get(0)).isInstanceOf(MergeScripts.class);
        assertThat(refactorings.get(1)).isInstanceOf(MergeScripts.class);
    }

    @Test
    public void testMergeScriptsFinder_OneWay() throws ParsingException, IOException {
        Program program = getAST("src/test/fixtures/refactoring/mergableOneWay.json");
        MergeScriptsFinder finder = new MergeScriptsFinder();
        List<Refactoring> refactorings = finder.check(program);
        assertThat(refactorings).hasSize(1);
        assertThat(refactorings.get(0)).isInstanceOf(MergeScripts.class);
    }

    @Test
    public void testMergeScriptsRefactoring() throws ParsingException, IOException {
        Program program = getAST("src/test/fixtures/refactoring/mergableBothWays.json");
        Script script1 = program.getActorDefinitionList().getDefinitions().get(1).getScripts().getScriptList().get(0);
        Script script2 = program.getActorDefinitionList().getDefinitions().get(1).getScripts().getScriptList().get(1);


        MergeScripts refactoring = new MergeScripts(script1, script2);
        Program refactored = refactoring.apply(program);

        assertThat(refactored.getActorDefinitionList().getDefinitions().get(1).getScripts().getSize()).isEqualTo(1);
        Script refactoredScript = refactored.getActorDefinitionList().getDefinitions().get(1).getScripts().getScriptList().get(0);
        assertThat(refactoredScript.getEvent()).isEqualTo(script1.getEvent());
        assertThat(refactoredScript.getEvent()).isEqualTo(script2.getEvent());

        StmtList refactoredStmtList = refactoredScript.getStmtList();
        assertThat(refactoredStmtList.getStmts().size()).isEqualTo(4);
    }

    @Test
    public void testMergeScriptsRefactoring2() throws ParsingException, IOException {
        Program program = getAST("src/test/fixtures/refactoring/mergableOneWay.json");
        Script script1 = program.getActorDefinitionList().getDefinitions().get(1).getScripts().getScriptList().get(0);
        Script script2 = program.getActorDefinitionList().getDefinitions().get(1).getScripts().getScriptList().get(1);


        MergeScripts refactoring = new MergeScripts(script1, script2);
        Program refactored = refactoring.apply(program);

        assertThat(refactored.getActorDefinitionList().getDefinitions().get(1).getScripts().getSize()).isEqualTo(1);
        Script refactoredScript = refactored.getActorDefinitionList().getDefinitions().get(1).getScripts().getScriptList().get(0);
        assertThat(refactoredScript.getEvent()).isEqualTo(script1.getEvent());
        assertThat(refactoredScript.getEvent()).isEqualTo(script2.getEvent());

        StmtList refactoredStmtList = refactoredScript.getStmtList();
        assertThat(refactoredStmtList.getStmts().size()).isEqualTo(5);
    }
}
