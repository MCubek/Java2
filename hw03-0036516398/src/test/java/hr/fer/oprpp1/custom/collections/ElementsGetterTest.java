package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ElementsGetterTest {
    @Test
    public void testProcessRemaining() {
        Collection col = new ArrayIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");
        ElementsGetter getter = col.createElementsGetter();
        getter.getNextElement();
        StringBuilder stringBuilder = new StringBuilder();
        getter.processRemaining(stringBuilder::append);
        assertEquals("AnaJasna",stringBuilder.toString());
    }
}
