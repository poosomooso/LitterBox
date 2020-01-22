/*
 * Copyright (C) 2019 LitterBox contributors
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
package newanalytics.utils;

import java.util.ArrayList;
import newanalytics.IssueFinder;
import newanalytics.IssueReport;
import scratch.ast.model.Program;
import scratch.ast.visitor.ScratchVisitor;

public class SpriteCount implements ScratchVisitor, IssueFinder {
    public static final String NAME = "sprite_count";
    public static final String SHORT_NAME = "spriteCnt";

    @Override
    public IssueReport check(Program program) {
        int count = program.getActorDefinitionList().getDefintions().size()-1;
        return new IssueReport(NAME, count, new ArrayList<>(), "");
    }

    @Override
    public String getName() {
        return NAME;
    }
}
