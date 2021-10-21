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
package de.uni_passau.fim.se2.litterbox.analytics.bugpattern;

import de.uni_passau.fim.se2.litterbox.JsonTest;
import de.uni_passau.fim.se2.litterbox.ast.ParsingException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class OrphanedParameterTest implements JsonTest {

    @Test
    public void testEmptyProgram() throws IOException, ParsingException {
        assertThatFinderReports(0, new OrphanedParameter(), "./src/test/fixtures/emptyProject.json");
    }

    @Test
    public void testOrphanedParameter() throws IOException, ParsingException {
        assertThatFinderReports(1, new OrphanedParameter(), "./src/test/fixtures/bugpattern/orphanedParameter.json");
    }

    @Test
    public void testOutsideParameter() throws IOException, ParsingException {
        assertThatFinderReports(0, new OrphanedParameter(), "./src/test/fixtures/bugpattern/parameterOutsideScope.json");
    }

    @Test
    public void testOrphanedParameterRichtung() throws IOException, ParsingException {
        assertThatFinderReports(1, new OrphanedParameter(), "./src/test/fixtures/bugpattern/orphanedParamRichtung.json");
    }
}
