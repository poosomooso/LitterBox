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
package newanalytics.bugpattern;

import java.util.LinkedList;
import java.util.List;
import newanalytics.IssueFinder;
import newanalytics.IssueReport;
import scratch.ast.model.ASTNode;
import scratch.ast.model.ActorDefinition;
import scratch.ast.model.Program;
import scratch.ast.model.statement.control.RepeatForeverStmt;
import scratch.ast.model.statement.control.RepeatTimesStmt;
import scratch.ast.model.statement.control.UntilStmt;
import scratch.ast.visitor.ScratchVisitor;
import utils.Preconditions;

public class ForeverInsideLoop implements IssueFinder, ScratchVisitor {
    public static final String NAME = "forever_inside_loop";
    public static final String SHORT_NAME = "ForeverInLoop";
    private static final String NOTE1 = "There are no forever loops inside other loops in your project.";
    private static final String NOTE2 = "Some of the sprites contain forever loops inside other loops.";
    private boolean found = false;
    private int count = 0;
    private List<String> actorNames = new LinkedList<>();
    private ActorDefinition currentActor;
    private int loopcounter;

    @Override
    public IssueReport check(Program program) {
        Preconditions.checkNotNull(program);
        found = false;
        count = 0;
        actorNames = new LinkedList<>();
        program.accept(this);
        String notes = NOTE1;
        if (count > 0) {
            notes = NOTE2;
        }
        return new IssueReport(NAME, count, actorNames, notes);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void visit(ActorDefinition actor) {
        currentActor = actor;
        loopcounter=0;
        if (!actor.getChildren().isEmpty()) {
            for (ASTNode child : actor.getChildren()) {
                child.accept(this);
            }
        }

        if (found) {
            found = false;
            actorNames.add(currentActor.getIdent().getName());
        }
    }

    @Override
    public void visit(UntilStmt node) {
        loopcounter++;
        if (!node.getChildren().isEmpty()) {
            for (ASTNode child : node.getChildren()) {
                child.accept(this);

            }
        }
        loopcounter--;
    }

    @Override
    public void visit(RepeatForeverStmt node) {
        if(loopcounter>0) {
            found = true;
            count++;
        }
        loopcounter++;
        if (!node.getChildren().isEmpty()) {
            for (ASTNode child : node.getChildren()) {

                child.accept(this);
            }
        }
        loopcounter--;
    }

    @Override
    public void visit(RepeatTimesStmt node){
        loopcounter++;
        if (!node.getChildren().isEmpty()) {
            for (ASTNode child : node.getChildren()) {
                child.accept(this);

            }
        }
        loopcounter--;
    }
}
