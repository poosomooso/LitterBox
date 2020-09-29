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
package de.uni_passau.fim.se2.litterbox.ast.model.statement.spritemotion;

import de.uni_passau.fim.se2.litterbox.ast.model.ASTLeaf;
import de.uni_passau.fim.se2.litterbox.ast.model.ASTNode;
import de.uni_passau.fim.se2.litterbox.ast.model.metadata.block.BlockMetadata;
import de.uni_passau.fim.se2.litterbox.ast.model.metadata.block.NoBlockMetadata;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.actorsound.SoundEffect;
import de.uni_passau.fim.se2.litterbox.ast.visitor.CloneVisitor;
import de.uni_passau.fim.se2.litterbox.ast.visitor.ScratchVisitor;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DragMode implements ASTLeaf {

    public enum DragModeType {
        not_draggable("not draggable"),
        draggable("draggable");

        private final String token;

        DragModeType(String token) {
            this.token = token;
        }

        public static boolean contains(String opcode) {
            for (DragModeType value : DragModeType.values()) {
                if (value.toString().equals(opcode)) {
                    return true;
                }
            }
            return false;
        }

        public static DragModeType fromString(String type) {
            for (DragModeType f : values()) {
                if (f.getToken().equals(type.toLowerCase())) {
                    return f;
                }
            }
            throw new IllegalArgumentException("Unknown DragMode: " + type);
        }

        public String getToken() {
            return token;
        }
    }

    private DragModeType type;

    public DragMode(String typeName) {
        this.type = DragModeType.fromString(typeName);
    }

    public DragModeType getType() {
        return type;
    }

    public String getTypeName() {
        return type.getToken();
    }

    private ASTNode parent;

    public ASTNode getParentNode() {
        return parent;
    }

    @Override
    public void setParentNode(ASTNode node) {
        this.parent = node;
    }

    @Override
    public void accept(ScratchVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ASTNode accept(CloneVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public List<? extends ASTNode> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public String getUniqueName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String[] toSimpleStringArray() {
        String[] result = new String[1];
        result[0] = type.getToken();
        return result;
    }

    @Override
    public String toString() {
        return type.getToken();
    }

    @Override
    public BlockMetadata getMetadata() {
        return new NoBlockMetadata();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DragMode)) return false;
        DragMode dragMode = (DragMode) o;
        return type == dragMode.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
