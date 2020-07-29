/*
 * Copyright (C) 2020 LitterBox contributors
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

import de.uni_passau.fim.se2.litterbox.analytics.Issue;
import de.uni_passau.fim.se2.litterbox.analytics.IssueFinder;
import de.uni_passau.fim.se2.litterbox.ast.model.ActorDefinition;
import de.uni_passau.fim.se2.litterbox.ast.model.Program;
import de.uni_passau.fim.se2.litterbox.ast.visitor.ScratchVisitor;
import de.uni_passau.fim.se2.litterbox.utils.Preconditions;

import java.util.LinkedHashSet;
import java.util.Set;

public class EmptyProject implements ScratchVisitor, IssueFinder {
    public static final String NAME = "empty_project";
    public static final String HINT_TEXT = "empty_project_hint";
    private boolean foundScript = false;
    private Set<Issue> issues = new LinkedHashSet<>();

    @Override
    public Set<Issue> check(Program program) {
        Preconditions.checkNotNull(program);
        foundScript = false;
        issues = new LinkedHashSet<>();
        program.accept(this);
        if (!foundScript) {
            // TODO -- there are no actors and no nodes, so what to pass in here?
            issues.add(new Issue(this, null, null));
        }
        return issues;
    }

    @Override
    public void visit(ActorDefinition actor) {
        if (!(actor.getScripts().getScriptList().isEmpty() && actor.getProcedureDefinitionList().getList().isEmpty())) {
            foundScript = true;
        }
    }

    @Override
    public String getName() {
        return NAME;
    }
}
