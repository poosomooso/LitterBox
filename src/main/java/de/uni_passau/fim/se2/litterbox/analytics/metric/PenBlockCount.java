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
package de.uni_passau.fim.se2.litterbox.analytics.metric;

import de.uni_passau.fim.se2.litterbox.analytics.MetricExtractor;
import de.uni_passau.fim.se2.litterbox.ast.model.ASTNode;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.Expression;
import de.uni_passau.fim.se2.litterbox.ast.model.extensions.ExtensionBlock;
import de.uni_passau.fim.se2.litterbox.ast.model.extensions.pen.PenStmt;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.Stmt;
import de.uni_passau.fim.se2.litterbox.ast.visitor.ExtensionVisitor;
import de.uni_passau.fim.se2.litterbox.ast.visitor.PenExtensionVisitor;
import de.uni_passau.fim.se2.litterbox.ast.visitor.ScratchVisitor;
import de.uni_passau.fim.se2.litterbox.utils.Preconditions;

public class PenBlockCount<T extends ASTNode> implements MetricExtractor<T>, ScratchVisitor {
    public static final String NAME = "pen_block_count";
    private int count = 0;
    private ExtensionVisitor vis;

    public PenBlockCount() {
        vis = new PenBlockCountExtensionVisitor(this);
    }

    @Override
    public double calculateMetric(T node) {
        Preconditions.checkNotNull(node);
        count = 0;
        node.accept(this);
        return count;
    }

    @Override
    public void visit(ExtensionBlock node) {
        node.accept(vis);
    }

    @Override
    public String getName() {
        return NAME;
    }

    private class PenBlockCountExtensionVisitor implements PenExtensionVisitor {
        ScratchVisitor parent;

        public PenBlockCountExtensionVisitor(ScratchVisitor parent) {
            this.parent = parent;
        }

        @Override
        public void visit(PenStmt node) {
            count++;
        }

        @Override
        public void visit(ExtensionBlock node) {
            if (node instanceof Stmt) {
                parent.visit((Stmt) node);
            } else if (node instanceof Expression) {
                parent.visit((Expression) node);
            } else {
                parent.visit((ASTNode) node);
            }
        }
    }
}
