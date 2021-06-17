package hr.fer.oprpp1.custom.scripting.demo;

import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.nodes.*;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Demo klasa koja prima datoteku kao argument i ispisuje je uz pomoc
 * visitora.
 *
 * @author MatejCubek
 * @project hw03-0036516398
 * @created 28/03/2021
 */
public class TreeWriter {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("1 argument required!");
            return;
        }

        Path file = Path.of(args[0]);

        String doc;
        try {
            doc = Files.readString(file);
        } catch (IOException e) {
            System.out.println("File reading error!");
            return;
        }

        SmartScriptParser parser;
        try {
            parser = new SmartScriptParser(doc);
        } catch (SmartScriptParserException parserException) {
            System.out.println(parserException.getMessage());
            return;
        }


        WriterVisitor visitor = new WriterVisitor();
        parser.getHeadNode().accept(visitor);

    }

    private static class WriterVisitor implements INodeVisitor {
        @Override
        public void visitTextNode(TextNode node) {
            System.out.print(node.getText());
        }

        @Override
        public void visitForLoopNode(ForLoopNode node) {
            System.out.format("{$for %s %s %s %s$}",
                    node.getVariable().asText(),
                    node.getStartExpression().asText(),
                    node.getEndExpression().asText(),
                    node.getStartExpression() != null ? " " + node.getStepExpression() : "");
        }

        @Override
        public void visitEchoNode(EchoNode node) {
            String text = "{$";
            text += Arrays.stream(node.getElements())
                    .map(Element::asText)
                    .collect(Collectors.joining(" "));
            text += "$}";
            System.out.print(text);
        }

        @Override
        public void visitDocumentNode(DocumentNode node) {
            for (int i = 0; i < node.numberOfChildren(); i++) {
                node.getChild(i).accept(this);
            }
        }
    }
}
