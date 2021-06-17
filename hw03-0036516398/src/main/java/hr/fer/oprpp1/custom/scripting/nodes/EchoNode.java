package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;

import java.util.Objects;

/**
 * Node s poljem elemenata tipa Element
 *
 * @author matej
 */
public class EchoNode extends Node {
    private final Element[] elements;

    public EchoNode(Element[] elements) {
        this.elements = Objects.requireNonNull(elements);
    }

    public Element[] getElements() {
        return elements;
    }

    @Override
    public void accept(INodeVisitor nodeVisitor) {
        nodeVisitor.visitEchoNode(this);
    }
}
