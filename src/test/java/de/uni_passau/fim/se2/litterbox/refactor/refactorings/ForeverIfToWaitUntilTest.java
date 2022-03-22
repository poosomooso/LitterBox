/*
 * Copyright (C) 2019-2022 LitterBox contributors
 *
 * This file is part of LitterBox.
 *
 * LitterBox is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * LitterBox is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LitterBox. If not, see <http://www.gnu.org/licenses/>.
 */
package de.uni_passau.fim.se2.litterbox.refactor.refactorings;

import de.uni_passau.fim.se2.litterbox.JsonTest;
import de.uni_passau.fim.se2.litterbox.analytics.refactorings.ForeverIfToWaitUntilFinder;
import de.uni_passau.fim.se2.litterbox.ast.ParsingException;
import de.uni_passau.fim.se2.litterbox.ast.model.Program;
import de.uni_passau.fim.se2.litterbox.ast.model.Script;
import de.uni_passau.fim.se2.litterbox.ast.model.StmtList;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.common.WaitUntil;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.control.IfThenStmt;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.control.RepeatForeverStmt;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class ForeverIfToWaitUntilTest implements JsonTest {

    @Test
    public void testForeverIfToWaitUntilFinder() throws ParsingException, IOException {
        Program program = getAST("src/test/fixtures/refactoring/foreverToWaitUntil.json");
        ForeverIfToWaitUntilFinder finder = new ForeverIfToWaitUntilFinder();
        List<Refactoring> refactorings = finder.check(program);
        assertThat(refactorings).hasSize(1);
        assertThat(refactorings.get(0)).isInstanceOf(ForeverIfToWaitUntil.class);
    }

    @Test
    public void testForeverIfToWaitUntilRefactoring() throws ParsingException, IOException {
        Program program = getAST("src/test/fixtures/refactoring/foreverToWaitUntil.json");
        Script script = program.getActorDefinitionList().getDefinitions().get(1).getScripts().getScriptList().get(0);
        StmtList stmtList = script.getStmtList();
        RepeatForeverStmt loopStmt = (RepeatForeverStmt) stmtList.getStmts().stream().filter(s -> s instanceof RepeatForeverStmt).findFirst().get();
        IfThenStmt ifThenStmt = (IfThenStmt) loopStmt.getStmtList().getStmts().stream().filter(s -> s instanceof IfThenStmt).findFirst().get();
        ForeverIfToWaitUntil refactoring = new ForeverIfToWaitUntil(loopStmt);
        Program refactored = refactoring.apply(program);

        Script refactoredScript = refactored.getActorDefinitionList().getDefinitions().get(1).getScripts().getScriptList().get(0);
        StmtList refactoredStmtList = refactoredScript.getStmtList();
        assertThat(refactoredStmtList.getNumberOfStatements()).isEqualTo(1);

        RepeatForeverStmt foreverStmt = (RepeatForeverStmt) refactoredScript.getStmtList().getStmts().stream().filter(s -> s instanceof RepeatForeverStmt).findFirst().get();
        assertThat(foreverStmt.getStmtList().getNumberOfStatements()).isEqualTo(1 + loopStmt.getStmtList().getNumberOfStatements());
        WaitUntil waitUntil = (WaitUntil) foreverStmt.getStmtList().getStatement(0);
        assertThat(waitUntil.getUntil().equals(ifThenStmt.getBoolExpr()));
    }
}
