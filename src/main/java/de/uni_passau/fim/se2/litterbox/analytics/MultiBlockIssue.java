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
package de.uni_passau.fim.se2.litterbox.analytics;

import de.uni_passau.fim.se2.litterbox.ast.model.ASTNode;
import de.uni_passau.fim.se2.litterbox.ast.model.ActorDefinition;
import de.uni_passau.fim.se2.litterbox.ast.model.Program;
import de.uni_passau.fim.se2.litterbox.ast.model.Script;
import de.uni_passau.fim.se2.litterbox.ast.model.metadata.Metadata;
import de.uni_passau.fim.se2.litterbox.ast.model.procedure.ProcedureDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiBlockIssue extends Issue {

    private List<ASTNode> nodes = new ArrayList<>();

    public MultiBlockIssue(IssueFinder finder, IssueSeverity severity, Program program, ActorDefinition actor, Script script, List<ASTNode> nodes, Metadata metaData, Hint hint) {
        super(finder, severity, program, actor, script, nodes.get(0), metaData, hint);
        this.nodes.addAll(nodes);
    }

    public MultiBlockIssue(IssueFinder finder, IssueSeverity severity, Program program, ActorDefinition actor, ProcedureDefinition procedure, List<ASTNode> nodes, Metadata metaData, Hint hint) {
        super(finder, severity, program, actor, procedure, nodes.get(0), metaData, hint);
        this.nodes.addAll(nodes);
    }

    @Override
    public boolean isCodeLocation(ASTNode node) {
        return nodes.contains(node);
    }

    @Override
    public boolean hasMultipleBlocks() {
        return nodes.size() > 1;
    }

    public List<ASTNode> getNodes() {
        return Collections.unmodifiableList(nodes);
    }
}
