package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.ObjectStack;
import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexerException;
import hr.fer.oprpp1.custom.scripting.lexer.Token;
import hr.fer.oprpp1.custom.scripting.lexer.TokenType;
import hr.fer.oprpp1.custom.scripting.nodes.*;

import java.util.Objects;

/**
 * Parser koji za zadan dokument izgradi sintaksno stablok
 *
 * @author matej
 */
public class SmartScriptParser {
    private final SmartScriptLexer lexer;
    private final ObjectStack stack;
    private final Node headNode;

    /**
     * Konstruktor za zadan zadan dokument izgradi sintaksno stablo
     *
     * @param document za koji se gradi sintaksno stablo
     */
    public SmartScriptParser(String document) {
        lexer = new SmartScriptLexer(Objects.requireNonNull(document));
        stack = new ObjectStack();
        headNode = new DocumentNode();

        try {
            parseDocument();
        } catch (SmartScriptLexerException e) {
            throw new SmartScriptParserException("Lexer Failed: " + e.getMessage());
        }
    }

    /**
     * Metoda koja gradi sintaksno stablo prolazom kroz sve tokene lexera
     */
    private void parseDocument() {
        Token token;
        stack.push(headNode);

        while ((token = lexer.nextToken()).getType() != TokenType.EOF) {

            if (token.getType() == TokenType.TEXT) {
                addAsChildToParent(new TextNode(token.getValue().asText()));
            } else if (token.getType() == TokenType.TAGSTART) {
                parseTag();
            } else {
                throw new SmartScriptParserException("Parsing Error, expected tag was EOF or TAGSTART, recieved was " + token.getType() + ".");
            }
        }
        if(stack.size()!=1)
            throw new SmartScriptParserException("Parsing Error, some loop remained open,");
    }

    /**
     * Metoda dodaje dani node trenutnom roditelju,
     * elementu koji se nalazi na vrhu stoga kao dijete
     *
     * @param node node koji se dodaje kao dijete
     */
    private void addAsChildToParent(Node node) {
        if (stack.isEmpty()) throw new SmartScriptParserException("Can't add child as there isn't a parent.");

        Node topNode = (Node) stack.peek();
        topNode.addChildNode(node);
    }

    /**
     * Metoda počinje parsirati tag
     */
    private void parseTag() {
        Token token = lexer.nextToken();
        if (token.getType() != TokenType.TAGNAME)
            throw new SmartScriptParserException("Expected tokentype TAGNAME missing.");

        switch (token.getValue().asText()) {
            case "=" -> parseEchoTag();
            case "FOR" -> parseForTag();
            case "END" -> parseEndTag();
            default -> throw new SmartScriptParserException("TagName " + token.getValue().asText() + " not recognized.");
        }
    }

    /**
     * Metoda parsiranja EchoTaga
     */
    private void parseEchoTag() {
        ArrayIndexedCollection tagsArray = new ArrayIndexedCollection(1);
        Token token;

        while ((token = lexer.nextToken()).getType() != TokenType.TAGEND) {
            if (checkInvalidTagName(token.getType())) throw new SmartScriptParserException("Wrong tag type received.");

            tagsArray.add(token.getValue());
        }
        Element[] array = new Element[tagsArray.size()];

        for (int i = 0; i < tagsArray.size(); i++) {
            array[i] = (Element) tagsArray.get(i);
        }

        addAsChildToParent(new EchoNode(array));
    }

    /**
     * Metoda parsiranje for petlje
     */
    private void parseForTag() {
        ArrayIndexedCollection forArray = new ArrayIndexedCollection(3);
        Token token = lexer.nextToken();
        ElementVariable variable;

        if (token.getValue() instanceof ElementVariable) {
            variable = (ElementVariable) token.getValue();
        } else {
            throw new SmartScriptParserException("Expected ElementVariable not received.");
        }

        while ((token = lexer.nextToken()).getType() != TokenType.TAGEND) {
            if (checkInvalidTagName(token.getType())) throw new SmartScriptParserException("Wrong tag type received.");

            forArray.add(token.getValue());
        }

        if (forArray.size() < 2 || forArray.size() > 3)
            throw new SmartScriptParserException("For loop has wrong number of elements.");

        ForLoopNode loopNode = new ForLoopNode(variable, (Element) forArray.get(0), (Element) forArray.get(1), forArray.size() == 3 ? (Element) forArray.get(2) : null);
        addAsChildToParent(loopNode);
        stack.push(loopNode);
    }

    /**
     * Parsiranje taga kraja
     */
    private void parseEndTag() {
        if (lexer.nextToken().getType() != TokenType.TAGEND) throw new SmartScriptParserException("Expected end tag.");

        if (stack.isEmpty()) throw new SmartScriptParserException("No elements for TAGEND");

        stack.pop();
    }

    /**
     * Metoda provjera je li došlo go pogreške u očekivanim tokenom kod obrade taga
     *
     * @param tokenType tip tokena
     * @return je li token prihvatljiv
     */
    private boolean checkInvalidTagName(TokenType tokenType) {
        return tokenType == TokenType.TAGSTART || tokenType == TokenType.EOF || tokenType == TokenType.TAGNAME;
    }

    public Node getHeadNode() {
        return headNode;
    }

}
