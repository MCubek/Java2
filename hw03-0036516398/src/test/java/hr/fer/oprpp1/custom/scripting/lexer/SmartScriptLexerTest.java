package hr.fer.oprpp1.custom.scripting.lexer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SmartScriptLexerTest {
    @Test
    public void testLexerWithNullArgument() {
        assertThrows(NullPointerException.class, () -> {
            String s = null;
            new SmartScriptLexer(s);
        });
    }

    @Test
    public void emptyStringInLexerOnlyEOF() {
        SmartScriptLexer lexer = new SmartScriptLexer("");
        assertEquals(new Token(TokenType.EOF, null), lexer.nextToken());
    }

    @Test
    public void noMoreTokensThrowsException() {
        SmartScriptLexer lexer = new SmartScriptLexer("");
        lexer.nextToken();
        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
    }

    @Test
    public void testNoTagExpectedTextWithoutEscape() {
        SmartScriptLexer lexer = new SmartScriptLexer("No tag follows");
        Token token = lexer.nextToken();
        assertEquals(TokenType.TEXT, token.getType());
        assertEquals("No tag follows", token.getValue().asText());
    }

    @Test
    public void testNoTagExpectedTextWithEscape() {
        SmartScriptLexer lexer = new SmartScriptLexer("No tag follows \\{$=1$} end");
        Token token = lexer.nextToken();
        assertEquals(TokenType.TEXT, token.getType());
        assertEquals("No tag follows {$=1$} end", token.getValue().asText());
    }

    @Test
    public void testTagStart() {
        SmartScriptLexer lexer = new SmartScriptLexer("Tag {$ $}");
        lexer.nextToken();
        assertEquals(TokenType.TAGSTART, lexer.nextToken().getType());
    }

    @Test
    public void testTagEnd() {
        SmartScriptLexer lexer = new SmartScriptLexer("Tag {$    $}");
        lexer.nextToken();
        lexer.nextToken();
        assertEquals(TokenType.TAGEND, lexer.nextToken().getType());
    }

    @Test
    public void testTagNameEquals() {
        SmartScriptLexer lexer = new SmartScriptLexer("Tag {$ =  $}");
        lexer.nextToken();
        lexer.nextToken();
        assertEquals(TokenType.TAGNAME, lexer.nextToken().getType());
        assertEquals("=", lexer.getCurrentToken().getValue().asText());
    }

    @Test
    public void testTagNameFor() {
        SmartScriptLexer lexer = new SmartScriptLexer("Tag {$ FoR  $}");
        lexer.nextToken();
        lexer.nextToken();
        assertEquals(TokenType.TAGNAME, lexer.nextToken().getType());
        assertEquals("FOR", lexer.getCurrentToken().getValue().asText());
    }

    @Test
    public void testTagNameEnd() {
        SmartScriptLexer lexer = new SmartScriptLexer("Tag {$  END$}");
        lexer.nextToken();
        lexer.nextToken();
        assertEquals(TokenType.TAGNAME, lexer.nextToken().getType());
        assertEquals("END", lexer.getCurrentToken().getValue().asText());
    }

    @Test
    public void testFunctionName() {
        SmartScriptLexer lexer = new SmartScriptLexer("Tag {$ @get5X$}");
        lexer.nextToken();
        lexer.nextToken();
        assertEquals(TokenType.FUNCTION, lexer.nextToken().getType());
        assertEquals("get5X", lexer.getCurrentToken().getValue().asText());
    }

    @Test
    public void testFunctionNameIncorrect() {
        SmartScriptLexer lexer = new SmartScriptLexer("Tag {$ @5get5X $}");
        lexer.nextToken();
        lexer.nextToken();
        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
    }

    @Test
    public void testGetNegativeInteger() {
        SmartScriptLexer lexer = new SmartScriptLexer("Tag {$ -9877  $}");
        lexer.nextToken();
        lexer.nextToken();
        assertEquals(TokenType.INTEGER, lexer.nextToken().getType());
        assertEquals("-9877", lexer.getCurrentToken().getValue().asText());
    }

    @Test
    public void testGetPositiveInteger() {
        SmartScriptLexer lexer = new SmartScriptLexer("Tag {$ 9877$}");
        lexer.nextToken();
        lexer.nextToken();
        assertEquals(TokenType.INTEGER, lexer.nextToken().getType());
        assertEquals("9877", lexer.getCurrentToken().getValue().asText());
    }

    @Test
    public void testGetNegativeDouble() {
        SmartScriptLexer lexer = new SmartScriptLexer("Tag {$ -9877.6  $}");
        lexer.nextToken();
        lexer.nextToken();
        assertEquals(TokenType.DOUBLE, lexer.nextToken().getType());
        assertEquals("-9877.6", lexer.getCurrentToken().getValue().asText());
    }

    @Test
    public void testGetPositiveDouble() {
        SmartScriptLexer lexer = new SmartScriptLexer("Tag {$ 9877.6$}");
        lexer.nextToken();
        lexer.nextToken();
        assertEquals(TokenType.DOUBLE, lexer.nextToken().getType());
        assertEquals("9877.6", lexer.getCurrentToken().getValue().asText());
    }

    @Test
    public void testGetSingleDigitInteger() {
        SmartScriptLexer lexer = new SmartScriptLexer("Tag {$ 1 10 1 $}");
        lexer.nextToken();
        lexer.nextToken();
        assertEquals(TokenType.INTEGER, lexer.nextToken().getType());
        assertEquals("1", lexer.getCurrentToken().getValue().asText());
    }

    @Test
    public void testStringInsideTagSimple() {
        SmartScriptLexer lexer = new SmartScriptLexer("Tag {$ \"String is inside  \" $}");
        lexer.nextToken();
        lexer.nextToken();
        assertEquals(TokenType.STRING, lexer.nextToken().getType());
        assertEquals("String is inside  ", lexer.getCurrentToken().getValue().asText());
    }
    @Test
    public void testOperator() {
        SmartScriptLexer lexer = new SmartScriptLexer("Tag {$ + - $}");
        lexer.nextToken();
        lexer.nextToken();
        assertEquals(TokenType.OPERATOR, lexer.nextToken().getType());
        assertEquals("+", lexer.getCurrentToken().getValue().asText());

        assertEquals(TokenType.OPERATOR, lexer.nextToken().getType());
        assertEquals("-", lexer.getCurrentToken().getValue().asText());
    }

    @Test
    public void testVariable() {
        SmartScriptLexer lexer = new SmartScriptLexer("Tag {$ var $}");

        lexer.nextToken();
        lexer.nextToken();

        assertEquals(TokenType.VARIABLE, lexer.nextToken().getType());
        assertEquals("var", lexer.getCurrentToken().getValue().asText());
    }

    @Test
    public void testNoTextJustTag() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$  $}");
        assertEquals(TokenType.TAGSTART, lexer.nextToken().getType());
        assertNull(lexer.getCurrentToken().getValue());

    }

    @Test
    public void testWrongVariable() {
        SmartScriptLexer lexer = new SmartScriptLexer("Tag {$ 5var $}");

        lexer.nextToken();
        lexer.nextToken();

        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
    }


}
