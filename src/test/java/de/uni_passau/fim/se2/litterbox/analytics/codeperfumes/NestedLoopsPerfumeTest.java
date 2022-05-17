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
package de.uni_passau.fim.se2.litterbox.analytics.codeperfumes;

import de.uni_passau.fim.se2.litterbox.JsonTest;
import de.uni_passau.fim.se2.litterbox.ast.ParsingException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class NestedLoopsPerfumeTest implements JsonTest {

    @Test
    public void testNestedLoopsTwo() throws IOException, ParsingException {
        assertThatFinderReports(1, new NestedLoopsPerfume(), "./src/test/fixtures/goodPractice/nestedLoopsTwo.json");
    }

    @Test
    public void testNestedLoopsThree() throws IOException, ParsingException {
        //Only outer loop should be added as issue, no matter how deep the loops are
        assertThatFinderReports(1, new NestedLoopsPerfume(), "./src/test/fixtures/goodPractice/nestedLoopsThree.json");
    }
}
