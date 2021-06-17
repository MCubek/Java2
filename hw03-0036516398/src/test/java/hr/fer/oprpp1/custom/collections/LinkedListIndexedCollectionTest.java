package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public class LinkedListIndexedCollectionTest {
    @Test
    public void testDefaultConstructorCreatesEmptyList() {
        var testList = new LinkedListIndexedCollection();

        assertTrue(testList.isEmpty() && testList.size() == 0);
    }

    @Test
    public void testConstructorThrowsExceptionWhenSendsNull() {
        assertThrows(NullPointerException.class, () -> new LinkedListIndexedCollection(null));
    }

    @Test
    public void testConstructorCopiesElements() {
        var array = new ArrayIndexedCollection(4);
        var first = "1";
        var second = 2;
        var third = "3";
        var fourth = 4;
        array.add(first);
        array.add(second);
        array.add(third);
        array.add(fourth);
        var expected = new Object[]{first, second, third, fourth};
        var linkedList = new LinkedListIndexedCollection(array);

        assertArrayEquals(expected, linkedList.toArray());
    }

    @Test
    public void testAddMethodEmpty() {
        var testList = new LinkedListIndexedCollection();
        var testElement = "Test";
        testList.add(testElement);

        assertTrue(testList.contains(testElement));
    }

    @Test
    public void testAddMethodNotEmpty() {
        var testList = new LinkedListIndexedCollection();
        testList.add(1);
        var testElement = "Test";
        testList.add(testElement);

        assertArrayEquals(new Object[]{1, testElement}, testList.toArray());
    }

    @Test
    public void testGetMethodCorrectIndexNotEmpty() {
        var testList = new LinkedListIndexedCollection();
        testList.add(1);
        var testElement = "Test";
        testList.add(testElement);

        assertEquals(testList.get(1), testElement);
    }

    @Test
    public void testGetMethodCorrectIndexEmpty() {
        var testList = new LinkedListIndexedCollection();
        var testElement = "Test";
        testList.add(testElement);

        assertEquals(testList.get(0), testElement);
    }

    @Test
    public void testClearMethod() {
        var testList = new LinkedListIndexedCollection();
        testList.add("1");
        testList.add(2);

        testList.clear();
        assertEquals(0, testList.toArray().length);
    }

    @Test
    public void testInsertMethod() {
        var testList = new LinkedListIndexedCollection();
        testList.add("1");
        testList.add(2);

        testList.insert("New", 0);
        assertArrayEquals(new Object[]{"New", "1", 2}, testList.toArray());
    }

    @Test
    public void testIndexOfMethod() {
        var testList = new LinkedListIndexedCollection();
        testList.add("1");
        testList.add(2);
        testList.add("3");

        assertEquals(1, testList.indexOf(2));
    }

    @Test
    public void testRemoveMethod() {
        var testList = new LinkedListIndexedCollection();
        testList.add("1");
        testList.add(2);
        testList.add("3");

        testList.remove(1);

        assertArrayEquals(new Object[]{"1", "3"}, testList.toArray());
    }

    @Test
    public void testRemoveMethodWrongArgument() {
        var testList = new LinkedListIndexedCollection();
        testList.add("1");
        testList.add(2);
        testList.add("3");

        assertThrows(IndexOutOfBoundsException.class, () -> testList.remove(10));
    }

    @Test
    public void testIndexOfMethodDoesntExist() {
        var testList = new LinkedListIndexedCollection();
        testList.add("1");
        testList.add(2);
        testList.add("3");

        assertEquals(- 1, testList.indexOf(4));
    }

    @Test
    public void testInsertWrongArgumentNullPointer() {
        var testList = new LinkedListIndexedCollection();
        testList.add("1");
        testList.add(2);

        assertThrows(NullPointerException.class, () -> testList.insert(null, 1));
    }

    @Test
    public void testInsertWrongArgumentInvalidIndex() {
        var testList = new LinkedListIndexedCollection();
        testList.add("1");
        testList.add(2);

        assertThrows(IndexOutOfBoundsException.class, () -> testList.insert("3", 10));
    }

    @Test
    public void testGetWrongArgumentInvalidIndex() {
        var testList = new LinkedListIndexedCollection();

        assertThrows(IndexOutOfBoundsException.class, () -> testList.get(10));
    }

    @Test
    public void testAddWrongArgumentNullPointer() {
        var testList = new LinkedListIndexedCollection();
        testList.add("1");
        testList.add(2);

        assertThrows(NullPointerException.class, () -> testList.add(null));
    }

    @Test
    public void testElementsGetter() {
        Collection col = new LinkedListIndexedCollection(); // npr. new ArrayIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");
        ElementsGetter getter1 = col.createElementsGetter();
        ElementsGetter getter2 = col.createElementsGetter();
        String result = "";
        result += getter1.getNextElement() + " ";
        result += getter1.getNextElement() + " ";
        result += getter2.getNextElement() + " ";
        result += getter1.getNextElement() + " ";
        result += getter2.getNextElement();

        assertEquals("Ivo Ana Ivo Jasna Ana", result);
    }

    @Test
    public void testElementsGetterException() {
        assertThrows(NoSuchElementException.class, () -> {
            Collection col = new LinkedListIndexedCollection(); // npr. new ArrayIndexedCollection();
            col.add("Ivo");
            col.add("Ana");
            col.add("Jasna");
            ElementsGetter getter1 = col.createElementsGetter();
            String result = "";
            result += getter1.getNextElement() + " ";
            result += getter1.getNextElement() + " ";
            result += getter1.getNextElement() + " ";
            result += getter1.getNextElement();
        });
    }

    @Test
    public void hasNextElementsGetterTest() {
        Collection col = new LinkedListIndexedCollection(); // npr. new ArrayIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");
        ElementsGetter getter = col.createElementsGetter();
        getter.getNextElement();
        getter.getNextElement();
        var result = new boolean[2];
        result[0] = getter.hasNextElement();
        getter.getNextElement();
        result[1] = getter.hasNextElement();
        assertArrayEquals(new boolean[]{true, false}, result);
    }

    @Test
    public void testElementsGetterThrowsConcurrentModificationException() {
        assertThrows(ConcurrentModificationException.class, () -> {
            Collection col = new LinkedListIndexedCollection();
            col.add("Ivo");
            col.add("Ana");
            col.add("Jasna");
            ElementsGetter getter = col.createElementsGetter();
            getter.getNextElement();
            getter.getNextElement();
            col.add("David");
            getter.getNextElement();
        });
    }

}
