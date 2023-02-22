package de.uni_passau.fim.se2.litterbox.ast.model.extensions.cs2n.statements.movement;

import de.uni_passau.fim.se2.litterbox.ast.model.ASTNode;
import de.uni_passau.fim.se2.litterbox.ast.model.AbstractNode;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.num.NumExpr;
import de.uni_passau.fim.se2.litterbox.ast.model.extensions.cs2n.expression.inputs.spindirection.SpikeMovementDirectionPicker;
import de.uni_passau.fim.se2.litterbox.ast.model.extensions.cs2n.expression.unit.Unit;
import de.uni_passau.fim.se2.litterbox.ast.model.metadata.block.BlockMetadata;
import de.uni_passau.fim.se2.litterbox.ast.visitor.CloneVisitor;
import de.uni_passau.fim.se2.litterbox.ast.visitor.ScratchVisitor;

public class SpikeMovementDirectionForDuration extends AbstractNode implements CS2NMovementStmt {

    private final BlockMetadata metadata;
    private final SpikeMovementDirectionPicker direction;
    private final NumExpr rate;

    public SpikeMovementDirectionPicker getDirection() {
        return direction;
    }

    public NumExpr getRate() {
        return rate;
    }

    public Unit getUnit() {
        return unit;
    }

    private final Unit unit;

    public SpikeMovementDirectionForDuration(SpikeMovementDirectionPicker direction, NumExpr rate, Unit unit, BlockMetadata metadata) {
        this.metadata = metadata;
        this.direction = direction;
        this.rate = rate;
        this.unit = unit;
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
    public BlockMetadata getMetadata() {
        return metadata;
    }
}
