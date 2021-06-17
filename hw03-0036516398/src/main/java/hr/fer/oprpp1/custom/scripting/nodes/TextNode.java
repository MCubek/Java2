package hr.fer.oprpp1.custom.scripting.nodes;

import java.util.Objects;

/**
 * Node koji predstavlaj tekst izvan taga tipa <code>NodeType.TEXT</code>
 *
 * @author matej
 */
public class TextNode extends Node {
    private final String text;

    public TextNode(String text) {
        this.text = Objects.requireNonNull(text);
    }

    public String getText() {
        return text;
    }

    @Override
    public void accept(INodeVisitor nodeVisitor) {
        nodeVisitor.visitTextNode(this);
    }
}

