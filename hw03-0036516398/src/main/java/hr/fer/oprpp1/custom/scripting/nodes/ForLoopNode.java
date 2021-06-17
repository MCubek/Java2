package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;

import java.util.Objects;

/**
 * Node for petlje
 * Sadrži varijable petlje te izraze
 *
 * @author matej
 */
public class ForLoopNode extends Node {
    private final ElementVariable variable;
    private final Element startExpression;
    private final Element endExpression;
    private final Element stepExpression;

    public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression, Element stepExpression) {
        this.variable = Objects.requireNonNull(variable);
        this.startExpression = Objects.requireNonNull(startExpression);
        this.endExpression = Objects.requireNonNull(endExpression);
        this.stepExpression = stepExpression;
    }

    public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression) {
        this(variable, startExpression, endExpression, null);
    }

    public ElementVariable getVariable() {
        return variable;
    }

    public Element getStartExpression() {
        return startExpression;
    }

    public Element getEndExpression() {
        return endExpression;
    }

    public Element getStepExpression() {
        return stepExpression;
    }

    @Override
    public void accept(INodeVisitor nodeVisitor) {
        nodeVisitor.visitForLoopNode(this);
    }
}


