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
package de.uni_passau.fim.se2.litterbox.analytics.smells;

import de.uni_passau.fim.se2.litterbox.JsonTest;
import de.uni_passau.fim.se2.litterbox.analytics.Hint;
import de.uni_passau.fim.se2.litterbox.analytics.Issue;
import de.uni_passau.fim.se2.litterbox.analytics.bugpattern.MissingLoopSensing;
import de.uni_passau.fim.se2.litterbox.ast.ParsingException;
import de.uni_passau.fim.se2.litterbox.ast.model.Program;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UnnecessaryIfAfterUntilTest implements JsonTest {
    @Test
    public void testEmptyProgram() throws IOException, ParsingException {
        Program empty = getAST("./src/test/fixtures/emptyProject.json");
        UnnecessaryIfAfterUntil parameterName = new UnnecessaryIfAfterUntil();
        Set<Issue> reports = parameterName.check(empty);
        Assertions.assertEquals(0, reports.size());
    }

    @Test
    public void testIfThenUnnecessary() throws IOException, ParsingException {
        Program empty = getAST("./src/test/fixtures/smells/unnecessaryIf.json");
        UnnecessaryIfAfterUntil parameterName = new UnnecessaryIfAfterUntil();
        List<Issue> reports = new ArrayList<>(parameterName.check(empty));
        Assertions.assertEquals(1, reports.size());
        Hint hint = new Hint(parameterName.getName());
        Assertions.assertEquals(hint.getHintText(), reports.get(0).getHint());
    }

    @Test
    public void testCoupling() throws IOException, ParsingException {
        Program empty = getAST("./src/test/fixtures/smells/unnecessaryIf.json");
        UnnecessaryIfAfterUntil parameterName = new UnnecessaryIfAfterUntil();
        List<Issue> reportsUnnecessaryIf = new ArrayList<>(parameterName.check(empty));
        Assertions.assertEquals(1, reportsUnnecessaryIf.size());
        MissingLoopSensing mls = new MissingLoopSensing();
        List<Issue> reportsMLS = new ArrayList<>(mls.check(empty));
        Assertions.assertEquals(1, reportsMLS.size());
        Assertions.assertTrue(parameterName.areCoupled(reportsUnnecessaryIf.get(0), reportsMLS.get(0)));
        Assertions.assertTrue(mls.areCoupled(reportsMLS.get(0), reportsUnnecessaryIf.get(0)));
    }
}
