package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.nodes.ForLoopNode;
import hr.fer.oprpp1.custom.scripting.nodes.Node;
import hr.fer.oprpp1.custom.scripting.nodes.TextNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SmartScriptParserTest {
    @Test
    public void testSampleProblem() {
        String text = """
                This is sample text.\r
                {$ FOR i 1 10 1 $}\r
                This is {$= i $}-th time this message is generated.\r
                {$END$}\r
                {$FOR i 0 10 2 $}\r
                sin( {$=i$}^2) = {$= i i * @sin "0.000" @decfmt $}\r
                {$END$}""";


        SmartScriptParser parser = new SmartScriptParser(text);

        Node node = parser.getHeadNode();
        assertTrue(node instanceof DocumentNode);
        DocumentNode headNode = (DocumentNode) node;

        assertEquals(4, headNode.numberOfChildren());

        assertTrue(headNode.getChild(0) instanceof TextNode);

        assertTrue(headNode.getChild(1) instanceof ForLoopNode);
        assertEquals(((ForLoopNode) headNode.getChild(1)).getVariable().asText(), "i");

        assertEquals(5,headNode.getChild(3).numberOfChildren());

        TextNode textNode = (TextNode) headNode.getChild(3).getChild(2);

        assertEquals("^2) = ",textNode.getText());
    }

    @Test
    public void testInvalidForNotEnoughElements(){
        assertThrows(SmartScriptParserException.class,()-> new SmartScriptParser("{$FOR i 3 $} {$END$}"));
    }

    @Test
    public void testInvalidForTooManyElements(){
        assertThrows(SmartScriptParserException.class,()-> new SmartScriptParser("{$FOR i 3 4 5 1$} {$END$}"));
    }

    @Test
    public void testInvalidForNotVariable(){
        assertThrows(SmartScriptParserException.class,()-> new SmartScriptParser("{$FOR 1 3 4 5 $} {$END$}"));
    }

    @Test
    public void testTagDoubleStart(){
        assertThrows(SmartScriptParserException.class,()-> new SmartScriptParser("{$ {$ FOR i 3 4 1$} {$END$}"));
    }

    @Test
    public void testInvalidForDoesNotEndWithCorrectTag(){
        assertThrows(SmartScriptParserException.class,()-> new SmartScriptParser("{$FOR i 3 4 FOR$} {$END$}"));
    }

    @Test
    public void testNoTagEND(){
        assertThrows(SmartScriptParserException.class,()-> new SmartScriptParser("{$FOR i 3 4 5 {$END$}"));
    }

    @Test
    public void testMultipleVariables() {
        String text = """
                This is sample text.\r
                {$ FOR i as ew $}\r
                {$END$}""";


        SmartScriptParser parser = new SmartScriptParser(text);

        Node node = parser.getHeadNode();
        assertTrue(node instanceof DocumentNode);
        DocumentNode headNode = (DocumentNode) node;

        assertTrue(headNode.getChild(1) instanceof ForLoopNode);

        assertEquals(2, headNode.numberOfChildren());
    }

    @Test
    public void testNoEndTag() {
        String text = """
                This is sample text.\r
                {$ FOR i as ew $}""";

        assertThrows(SmartScriptParserException.class,()-> new SmartScriptParser(text));
    }
}
