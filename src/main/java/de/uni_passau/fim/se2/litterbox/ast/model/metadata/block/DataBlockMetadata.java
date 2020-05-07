package de.uni_passau.fim.se2.litterbox.ast.model.metadata.block;

import de.uni_passau.fim.se2.litterbox.ast.model.ASTLeaf;
import de.uni_passau.fim.se2.litterbox.ast.model.AbstractNode;
import de.uni_passau.fim.se2.litterbox.ast.visitor.ScratchVisitor;

public class DataBlockMetadata extends AbstractNode implements BlockMetadata, ASTLeaf {
    private int dataType;
    private String dataName;
    private String dataReference;
    private double x;
    private double y;

    public DataBlockMetadata(int dataType, String dataName, String dataReference, double x, double y) {
        super();
        this.dataType = dataType;
        this.dataName = dataName;
        this.dataReference = dataReference;
        this.x = x;
        this.y = y;
    }

    public int getDataType() {
        return dataType;
    }

    public String getDataName() {
        return dataName;
    }

    public String getDataReference() {
        return dataReference;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public void accept(ScratchVisitor visitor) {
        visitor.visit(this);
    }
}
