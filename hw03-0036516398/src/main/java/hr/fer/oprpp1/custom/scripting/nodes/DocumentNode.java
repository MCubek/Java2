package hr.fer.oprpp1.custom.scripting.nodes;

/**
 * Roditeljski, vršni node
 */
public class DocumentNode extends Node {
    @Override
    public void accept(INodeVisitor nodeVisitor) {
        nodeVisitor.visitDocumentNode(this);
    }
}
