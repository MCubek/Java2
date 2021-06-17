package hr.fer.oprpp1.custom.scripting.exec;

import hr.fer.oprpp1.custom.scripting.elems.*;
import hr.fer.oprpp1.custom.scripting.nodes.*;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Class tasked with parsing a script and readying
 * requestContext for sending to server.
 *
 * @author MatejCubek
 * @project hw03-0036516398
 * @created 29/03/2021
 */
public class SmartScriptEngine {
    private final DocumentNode documentNode;
    private final RequestContext requestContext;
    private final ObjectMultistack multistack = new ObjectMultistack();

    //Visitor tasked for goind through nodes
    private final INodeVisitor visitor = new INodeVisitor() {
        @Override
        public void visitTextNode(TextNode node) {
            try {
                requestContext.write(node.getText());
            } catch (IOException e) {
                System.out.println("Error while visiting text node: " + e.getMessage());
            }
        }

        @Override
        public void visitForLoopNode(ForLoopNode node) {
            var variable = node.getVariable().asText();
            int min = Integer.parseInt(node.getStartExpression().asText());
            int max = Integer.parseInt(node.getEndExpression().asText());
            int step = Integer.parseInt(node.getStepExpression().asText());

            multistack.push(variable, new ValueWrapper(min));

            ValueWrapper value;
            do {
                value = multistack.peek(variable);

                for (int i = 0; i < node.numberOfChildren(); i++) {
                    node.getChild(i).accept(this);
                }

                multistack.peek(variable).add(step);

            } while (value.numCompare(max) <= 0);
        }

        @Override
        public void visitEchoNode(EchoNode node) {
            Deque<Object> tempStack = new LinkedList<>();

            //Operations for each element
            for (int i = 0; i < node.getElements().length; i++) {
                Element element = node.getElements()[i];

                if (element instanceof ElementConstantInteger ||
                        element instanceof ElementConstantDouble ||
                        element instanceof ElementString) {
                    tempStack.push(element.asText());


                } else if (element instanceof ElementVariable elementVariable) {
                    tempStack.push(multistack.peek(elementVariable.getName()));


                } else if (element instanceof ElementOperator elementOperator) {
                    var operator2 = new ValueWrapper(tempStack.pop());
                    var operator1 = new ValueWrapper(tempStack.pop());

                    try {
                        switch (elementOperator.getSymbol()) {
                            case "+" -> operator1.add(operator2.getValue());
                            case "-" -> operator1.subtract(operator2.getValue());
                            case "*" -> operator1.multiply(operator2.getValue());
                            case "/" -> operator1.divide(operator2.getValue());
                            default -> throw new RuntimeException("Unsupported op");
                        }
                    } catch (RuntimeException e) {
                        System.out.println("Operation error!");
                        operator1 = new ValueWrapper(0);
                    }
                    tempStack.push(operator1);


                } else if (element instanceof ElementFunction elementFunction) {
                    switch (elementFunction.getName().toLowerCase()) {
                        case "sin" -> {
                            var arg = tempStack.pop();
                            double val = Double.parseDouble(arg.toString());
                            double res = Math.sin(val * Math.PI / 180);
                            tempStack.push(res);
                        }
                        case "decfmt" -> {
                            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                            symbols.setDecimalSeparator('.');
                            DecimalFormat df = new DecimalFormat(tempStack.pop().toString(), symbols);
                            var res = df.format(tempStack.pop());
                            tempStack.push(res);
                        }
                        case "dup" -> tempStack.push(tempStack.peek());
                        case "swap" -> {
                            var first = tempStack.pop();
                            var second = tempStack.pop();
                            tempStack.push(first);
                            tempStack.push(second);
                        }
                        case "setmimetype" -> {
                            var string = tempStack.pop();
                            requestContext.setMimeType(string.toString());
                        }
                        case "paramget" -> {
                            var dv = tempStack.pop();
                            var name = tempStack.pop();
                            var value = requestContext.getParameter(name.toString());

                            tempStack.push(value != null ? value : dv);
                        }
                        case "pparamget" -> {
                            var dv = tempStack.pop();
                            var name = tempStack.pop();
                            var value = requestContext.getPersistentParameter(name.toString());

                            tempStack.push(value != null ? value : dv);
                        }
                        case "pparamset" -> {
                            var name = tempStack.pop();
                            var value = tempStack.pop();
                            requestContext.setPersistentParameter(name.toString(), value.toString());
                        }
                        case "pparamdel" -> {
                            var name = tempStack.pop();
                            requestContext.removePersistentParameter(name.toString());
                        }
                        case "tparamget" -> {
                            var dv = tempStack.pop();
                            var name = tempStack.pop();
                            var value = requestContext.getTemporaryParameter(name.toString());

                            tempStack.push(value != null ? value : dv);
                        }
                        case "tparamset" -> {
                            var name = tempStack.pop();
                            var value = tempStack.pop();
                            requestContext.setTemporaryParameter(name.toString(), value.toString());
                        }
                        case "tparamdel" -> {
                            var name = tempStack.pop();
                            requestContext.removeTemporaryParameter(name.toString());
                        }
                    }


                }
            }
            //Push reversed onto stack
            var element = tempStack.pollLast();
            while (element != null) {
                try {
                    requestContext.write(element.toString());
                } catch (IOException ignored) {
                }
                element = tempStack.pollLast();
            }
        }

        @Override
        public void visitDocumentNode(DocumentNode node) {
            for (int i = 0; i < node.numberOfChildren(); i++) {
                node.getChild(i).accept(visitor);
            }
        }
    };

    public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
        this.documentNode = documentNode;
        this.requestContext = requestContext;
    }

    /**
     * Starts script execution.
     */
    public void execute() {
        documentNode.accept(visitor);
    }
}
