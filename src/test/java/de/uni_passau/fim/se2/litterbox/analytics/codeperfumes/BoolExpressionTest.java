/*
 * Copyright (C) 2019-2021 LitterBox contributors
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
package de.uni_passau.fim.se2.litterbox.analytics.codeperfumes;

import de.uni_passau.fim.se2.litterbox.JsonTest;
import de.uni_passau.fim.se2.litterbox.analytics.Issue;
import de.uni_passau.fim.se2.litterbox.ast.ParsingException;
import de.uni_passau.fim.se2.litterbox.ast.model.Program;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BoolExpressionTest implements JsonTest {

    @Test
    public void testEmptyProgram() throws IOException, ParsingException {
        assertThatFinderReports(0, new BoolExpression(), "./src/test/fixtures/emptyProject.json");
    }

    @Test
    public void testBooleanExpressions() throws IOException, ParsingException {
        Program boolProg = JsonTest.parseProgram("./src/test/fixtures/goodPractice/boolExpressions.json");
        BoolExpression bool = new BoolExpression();
        List<Issue> reports = new ArrayList<>(bool.check(boolProg));
        Assertions.assertEquals(7, reports.size());
        Assertions.assertTrue(reports.get(0).isDuplicateOf(reports.get(1)));
        Assertions.assertTrue(reports.get(0).isDuplicateOf(reports.get(2)));
        Assertions.assertTrue(reports.get(1).isDuplicateOf(reports.get(2)));
    }
}
