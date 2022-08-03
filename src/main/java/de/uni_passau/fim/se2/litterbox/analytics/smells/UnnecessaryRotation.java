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
package de.uni_passau.fim.se2.litterbox.analytics.smells;

import de.uni_passau.fim.se2.litterbox.analytics.AbstractIssueFinder;
import de.uni_passau.fim.se2.litterbox.analytics.IssueSeverity;
import de.uni_passau.fim.se2.litterbox.analytics.IssueType;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.num.NumExpr;
import de.uni_passau.fim.se2.litterbox.ast.model.literals.NumberLiteral;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.spritemotion.TurnLeft;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.spritemotion.TurnRight;

public class UnnecessaryRotation extends AbstractIssueFinder {
    public static final String NAME = "unnecessary_rotation";

    @Override
    public void visit(TurnLeft node) {
        if (hasUnnecessaryValue(node.getDegrees())) {
            addIssue(node, node.getMetadata(), IssueSeverity.LOW);
        }
    }

    @Override
    public void visit(TurnRight node) {
        if (hasUnnecessaryValue(node.getDegrees())) {
            addIssue(node, node.getMetadata(), IssueSeverity.LOW);
        }
    }

    private boolean hasUnnecessaryValue(NumExpr degrees) {
        if (degrees instanceof NumberLiteral) {
            double value = ((NumberLiteral) degrees).getValue();
            return value % 360 == 0;
        }
        return false;
    }

    @Override
    public IssueType getIssueType() {
        return IssueType.SMELL;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
