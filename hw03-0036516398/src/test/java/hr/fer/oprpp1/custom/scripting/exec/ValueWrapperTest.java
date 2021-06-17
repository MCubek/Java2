package hr.fer.oprpp1.custom.scripting.exec;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import javax.naming.OperationNotSupportedException;

/**
 * @author MatejCubek
 * @project hw03-0036516398
 * @created 29/03/2021
 */
class ValueWrapperTest {

    @Test
    void testAddition() throws OperationNotSupportedException {
        ValueWrapper v1 = new ValueWrapper(null);
        ValueWrapper v2 = new ValueWrapper(null);
        v1.add(v2.getValue());
        // v1 now stores Integer(0); v2 still stores null.
        assertEquals(0, v1.getValue());
        assertNull(v2.getValue());


        ValueWrapper v3 = new ValueWrapper("1.2E1");
        ValueWrapper v4 = new ValueWrapper(1);
        v3.add(v4.getValue()); // v3 now stores Double(13); v4 still stores Integer(1).

        assertEquals(13.0, v3.getValue());
        assertEquals(1, v4.getValue());


        ValueWrapper v7 = new ValueWrapper("Ankica");
        ValueWrapper v8 = new ValueWrapper(1);
        assertThrows(RuntimeException.class, () -> v7.add(v8.getValue()));
    }

    @Test
    void testSubtract() throws OperationNotSupportedException {
        ValueWrapper v1 = new ValueWrapper(null);
        ValueWrapper v2 = new ValueWrapper(null);
        v1.subtract(v2.getValue());
        // v1 now stores Integer(0); v2 still stores null.
        assertEquals(0, v1.getValue());
        assertNull(v2.getValue());


        ValueWrapper v3 = new ValueWrapper("1.2E1");
        ValueWrapper v4 = new ValueWrapper(1);
        v3.subtract(v4.getValue()); // v3 now stores Double(13); v4 still stores Integer(1).

        assertEquals(11.0, v3.getValue());
        assertEquals(1, v4.getValue());
    }


    @Test
    void testMultiply() throws OperationNotSupportedException {
        ValueWrapper v1 = new ValueWrapper(null);
        ValueWrapper v2 = new ValueWrapper(null);
        v1.multiply(v2.getValue());
        // v1 now stores Integer(0); v2 still stores null.
        assertEquals(0, v1.getValue());
        assertNull(v2.getValue());


        ValueWrapper v3 = new ValueWrapper("1.2E1");
        ValueWrapper v4 = new ValueWrapper(2);
        v3.multiply(v4.getValue()); // v3 now stores Double(24); v4 still stores Integer(1).

        assertEquals(24.0, v3.getValue());
        assertEquals(2, v4.getValue());
    }

    @Test
    void testDivision() throws OperationNotSupportedException {
        ValueWrapper v3 = new ValueWrapper("1.2E1");
        ValueWrapper v4 = new ValueWrapper(2);
        v3.divide(v4.getValue()); // v3 now stores Double(6); v4 still stores Integer(1).

        assertEquals(6.0, v3.getValue());
        assertEquals(2, v4.getValue());
    }

}