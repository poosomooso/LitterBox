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
import de.uni_passau.fim.se2.litterbox.analytics.Issue;
import de.uni_passau.fim.se2.litterbox.ast.ParsingException;
import de.uni_passau.fim.se2.litterbox.ast.model.Program;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

public class MultipleProjectTest implements JsonTest {

    @Test
    public void testBoatRace() throws IOException, ParsingException {
        assertThatFinderReports(0, new MissingLoopSensing(), "./src/test/fixtures/bugpattern/boatrace.json");
    }

    @Test
    public void testBallgame() throws IOException, ParsingException {
        assertThatFinderReports(2, new MissingLoopSensing(), "./src/test/fixtures/bugpattern/ballgame.json");
    }

    @Test
    public void testCombined() throws IOException, ParsingException {
        Program ballgame = JsonTest.parseProgram("./src/test/fixtures/bugpattern/ballgame.json");
        Program boatrace = JsonTest.parseProgram("./src/test/fixtures/bugpattern/boatrace.json");
        MissingLoopSensing parameterName = new MissingLoopSensing();
        Set<Issue> reports = parameterName.check(ballgame);
        Assertions.assertEquals(2, reports.size());
        Set<Issue> reports2 = parameterName.check(boatrace);
        Assertions.assertEquals(0, reports2.size());
    }
}
