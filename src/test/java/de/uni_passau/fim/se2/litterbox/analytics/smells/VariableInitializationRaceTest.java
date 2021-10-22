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
import de.uni_passau.fim.se2.litterbox.ast.ParsingException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class VariableInitializationRaceTest implements JsonTest {
    @Test
    public void testEmptyProgram() throws IOException, ParsingException {
        assertThatFinderReports(0, new VariableInitializationRace(), "./src/test/fixtures/emptyProject.json");
    }

    @Test
    public void testVariableInitializationOnGreenFlag() throws IOException, ParsingException {
        assertThatFinderReports(1, new VariableInitializationRace(), "./src/test/fixtures/smells/raceConditionVarGreenFlag.json");
    }

    @Test
    public void testVariableInitializationOn3GreenFlags() throws IOException, ParsingException {
        assertThatFinderReports(1, new VariableInitializationRace(), "./src/test/fixtures/smells/raceConditionVarGreenFlag3.json");
    }

    @Test
    public void testVariableInitializationOnClick() throws IOException, ParsingException {
        assertThatFinderReports(1, new VariableInitializationRace(), "./src/test/fixtures/smells/raceConditionOnClick.json");
    }

    @Test
    public void testVariableInitializationOnDifferentEvents() throws IOException, ParsingException {
        assertThatFinderReports(0, new VariableInitializationRace(), "./src/test/fixtures/smells/raceConditionDifferentEvents.json");
    }

    @Test
    public void testVariableInitializationOnDifferentVariables() throws IOException, ParsingException {
        assertThatFinderReports(0, new VariableInitializationRace(), "./src/test/fixtures/smells/raceConditionDifferentVariables.json");
    }

    @Test
    public void testVariableInitializationSetAndChangeVariable() throws IOException, ParsingException {
        assertThatFinderReports(1, new VariableInitializationRace(), "./src/test/fixtures/smells/raceConditionDifferentVariableStatements.json");
    }

    @Test
    public void testVariableInitializationDifferentActors() throws IOException, ParsingException {
        assertThatFinderReports(1, new VariableInitializationRace(), "./src/test/fixtures/smells/raceConditionDifferentActors.json");
    }

    @Test
    public void testVariableInitializationLocalVariables() throws IOException, ParsingException {
        assertThatFinderReports(0, new VariableInitializationRace(), "./src/test/fixtures/smells/raceConditionLocalVariables.json");
    }

    @Test
    public void testVariableAfterInitializationInLoop() throws IOException, ParsingException {
        assertThatFinderReports(0, new VariableInitializationRace(), "./src/test/fixtures/smells/raceConditionAfterInitialization.json");
    }

    @Test
    public void testVariableAfterInitialization() throws IOException, ParsingException {
        assertThatFinderReports(0, new VariableInitializationRace(), "./src/test/fixtures/smells/raceConditionAfterInitialization2.json");
    }

    @Test
    public void testNoVariableRace() throws IOException, ParsingException {
        assertThatFinderReports(0, new VariableInitializationRace(), "./src/test/fixtures/smells/variableInitialisationWithoutRace.json");
    }
}
