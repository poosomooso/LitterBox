package scratch.newast.model;

import com.google.common.collect.ImmutableList;
import java.util.List;

public class DeclarationList implements ASTNode {

    List<Declaration> declarationList;
    private final ImmutableList<ASTNode> children;

    public DeclarationList(List<Declaration> declarationList) {
        this.declarationList = declarationList;
        children = ImmutableList.<ASTNode>builder().build();
    }

    public List<Declaration> getDeclarationList() {
        return declarationList;
    }

    public void setDeclarationList(List<Declaration> declarationList) {
        this.declarationList = declarationList;
    }

    @Override
    public void accept(ScratchVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ImmutableList<ASTNode> getChildren() {
        return children;
    }
}
