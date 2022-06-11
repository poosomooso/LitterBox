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
package de.uni_passau.fim.se2.litterbox.analytics.bugpattern;

import de.uni_passau.fim.se2.litterbox.JsonTest;
import de.uni_passau.fim.se2.litterbox.analytics.Hint;
import de.uni_passau.fim.se2.litterbox.analytics.Issue;
import de.uni_passau.fim.se2.litterbox.ast.ParsingException;
import de.uni_passau.fim.se2.litterbox.ast.model.Program;
import de.uni_passau.fim.se2.litterbox.ast.model.Script;
import de.uni_passau.fim.se2.litterbox.ast.visitor.ScriptReplacementVisitor;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;

public class ImmediateDeleteCloneAfterBroadcastTest implements JsonTest {

    @Test
    public void testEmptyProgram() throws IOException, ParsingException {
        assertThatFinderReports(0, new ImmediateDeleteCloneAfterBroadcast(), "./src/test/fixtures/emptyProject.json");
    }

    @Test
    public void testImmediateDeleteCloneAfterBroadcast() throws IOException, ParsingException {
        Program program = JsonTest.parseProgram("./src/test/fixtures/bugpattern/immediateDeleteCloneAfterBroadcast.json");
        ImmediateDeleteCloneAfterBroadcast issueFinder = new ImmediateDeleteCloneAfterBroadcast();
        Set<Issue> reports = issueFinder.check(program);
        assertThat(reports).hasSize(1);

        Issue theIssue = reports.iterator().next();

        Hint expectedHint = new Hint(issueFinder.getName());
        expectedHint.setParameter(Hint.HINT_SPRITE, "Sprite1");
        expectedHint.setParameter(Hint.HINT_MESSAGE, "Nachricht1");
        assertThat(theIssue.getHint()).isEqualTo(expectedHint.getHintText());

        ScriptReplacementVisitor visitor = new ScriptReplacementVisitor(theIssue.getScript(), (Script) theIssue.getRefactoredScriptOrProcedureDefinition());
        Program refactoredProgram = (Program) program.accept(visitor);
        Set<Issue> refactoredIssues = issueFinder.check(refactoredProgram);
        assertThat(refactoredIssues).isEmpty();
    }
}

