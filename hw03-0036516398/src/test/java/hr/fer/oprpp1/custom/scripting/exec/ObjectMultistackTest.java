package hr.fer.oprpp1.custom.scripting.exec;

import hr.fer.oprpp1.custom.scripting.demo.ObjectMultistackDemo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

import javax.naming.OperationNotSupportedException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author MatejCubek
 * @project hw03-0036516398
 * @created 29/03/2021
 */
class ObjectMultistackTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final Path resources = Path.of("src/test/resources");

    @BeforeEach
    private void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    private void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void testDemo(){
        try {
            ObjectMultistackDemo.main(new String[0]);
        } catch (OperationNotSupportedException e) {
            fail();
        }
        assertEquals("""
                Current value for year: 2000
                Current value for price: 200.51
                Current value for year: 1900
                Current value for year: 1950
                Current value for year: 2000
                Current value for year: 2005
                Current value for year: 2010
                Current value for year: 2015.0
                """.trim().replace("\n","\r\n"), outputStreamCaptor.toString().trim());
    }
}