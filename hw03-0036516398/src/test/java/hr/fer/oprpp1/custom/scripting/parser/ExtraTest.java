package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.scripting.nodes.Node;
import hr.fer.oprpp1.custom.scripting.nodes.TextNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class ExtraTest {
    @Test
    public void testText1() {
        String test = readExample(1);
        SmartScriptParser parser = new SmartScriptParser(test);
        int textNodeCounter = 0;
        Node head = parser.getHeadNode();
        for (int i = 0; i < head.numberOfChildren(); i++) {
            if(head.getChild(i) instanceof TextNode)
                    textNodeCounter++;
        }


        assertEquals(1, textNodeCounter);
    }

    @Test
    public void testText2() {
        String test = readExample(2);
        SmartScriptParser parser = new SmartScriptParser(test);
        int textNodeCounter = 0;
        Node head = parser.getHeadNode();
        for (int i = 0; i < head.numberOfChildren(); i++) {
            if(head.getChild(i) instanceof TextNode)
                textNodeCounter++;
        }


        assertEquals(1, textNodeCounter);
    }

    @Test
    public void testText3() {
        String test = readExample(3);
        SmartScriptParser parser = new SmartScriptParser(test);
        int textNodeCounter = 0;
        Node head = parser.getHeadNode();
        for (int i = 0; i < head.numberOfChildren(); i++) {
            if(head.getChild(i) instanceof TextNode)
                textNodeCounter++;
        }


        assertEquals(1, textNodeCounter);
    }

    @Test
    public void testText4() {
        String test = readExample(4);

        assertThrows(SmartScriptParserException.class,()->{
            new SmartScriptParser(test);
        });

    }

    @Test
    public void testText5() {
        String test = readExample(5);

        assertThrows(SmartScriptParserException.class,()->{
            new SmartScriptParser(test);
        });
    }

    @Test
    public void testText6() {
        String test = readExample(6);
        SmartScriptParser parser = new SmartScriptParser(test);
        int textNodeCounter = 0;
        Node head = parser.getHeadNode();
        for (int i = 0; i < head.numberOfChildren(); i++) {
            if(head.getChild(i) instanceof TextNode)
                textNodeCounter++;
        }

        assertEquals(1, textNodeCounter);
    }

    @Test
    public void testText7() {
        String test = readExample(7);
        SmartScriptParser parser = new SmartScriptParser(test);
        int textNodeCounter = 0;
        Node head = parser.getHeadNode();
        for (int i = 0; i < head.numberOfChildren(); i++) {
            if(head.getChild(i) instanceof TextNode)
                textNodeCounter++;
        }

        assertEquals(1, textNodeCounter);
    }

    @Test
    public void testText8() {
        String test = readExample(8);

        assertThrows(SmartScriptParserException.class,()->{
            new SmartScriptParser(test);
        });
    }

    @Test
    public void testText9(){
        String test = readExample(5);

        assertThrows(SmartScriptParserException.class,()->{
            new SmartScriptParser(test);
        });
    }

    private String readExample(int n) {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("extra/primjer" + n + ".txt")) {
            if (is == null) throw new RuntimeException("Datoteka extra/primjer" + n + ".txt je nedostupna.");
            byte[] data = is.readAllBytes();
            String text = new String(data, StandardCharsets.UTF_8);
            return text;
        } catch (IOException ex) {
            throw new RuntimeException("Greška pri čitanju datoteke.", ex);
        }
    }
}
