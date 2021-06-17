package hr.fer.oprpp1.custom.scripting.nodes;

/**
 * @author MatejCubek
 * @project hw03-0036516398
 * @created 28/03/2021
 */
public interface INodeVisitor {
    void visitTextNode(TextNode node);

    void visitForLoopNode(ForLoopNode node);

    void visitEchoNode(EchoNode node);

    void visitDocumentNode(DocumentNode node);
}
