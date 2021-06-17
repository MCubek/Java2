package hr.fer.oprpp1.custom.collections;

import hr.fer.oprpp1.custom.collections.demo.EvenIntegerTester;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ListTest {
    @Test
    public void testAddAllSatisfying(){
        List col1 = new ArrayIndexedCollection();
        List col2 = new LinkedListIndexedCollection();
        col1.add("Ivana");
        col2.add("Jasna");
        Collection col3 = col1;
        Collection col4 = col2;
        col1.get(0);
        col2.get(0);
/*        col3.get(0); // neće se prevesti! Razumijete li zašto?
        col4.get(0); // neće se prevesti! Razumijete li zašto?*/


        StringBuilder sb = new StringBuilder();
        col1.forEach(sb::append); // Ivana
        col2.forEach(sb::append); // Jasna
        col3.forEach(sb::append); // Ivana
        col4.forEach(sb::append); // Jasna

        assertEquals("IvanaJasnaIvanaJasna",sb.toString());
    }
}
