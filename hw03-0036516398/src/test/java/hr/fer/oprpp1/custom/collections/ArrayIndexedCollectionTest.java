package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public class ArrayIndexedCollectionTest {
    @Test
    public void testConstructorWrongCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection(- 5));
    }

    @Test
    public void testConstructorSize16() {
        assertEquals(16, new ArrayIndexedCollection().getAllocatedSize());
    }

    @Test
    public void testConstructorGivenCapacity() {
        assertEquals(14, new ArrayIndexedCollection(14).getAllocatedSize());
    }

    @Test
    public void testConstructor2GivenCapacity() {
        Collection startCollection = new ArrayIndexedCollection();
        startCollection.add(3);
        startCollection.add(10);

        assertEquals(14, new ArrayIndexedCollection(startCollection, 14).getAllocatedSize());
    }

    @Test
    public void testDoublingOfSize() {
        var startCollection = new ArrayIndexedCollection(2);
        startCollection.add(1);
        startCollection.add(2);
        startCollection.add(3);

        assertEquals(4, startCollection.getAllocatedSize());
    }

    @Test
    public void testConstructorGiveArray() {
        Collection startCollection = new ArrayIndexedCollection();
        startCollection.add(3);
        startCollection.add(10);

        ArrayIndexedCollection testCollecion = new ArrayIndexedCollection(startCollection);

        var expectedArray = new Object[]{3, 10};
        var gottenArray = testCollecion.toArray();


        assertArrayEquals(expectedArray, gottenArray);
    }

    @Test
    public void testSendNullCollection() {
        assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null));
    }

    @Test
    public void testAddMethod() {
        var testCollection = new ArrayIndexedCollection(2);
        testCollection.add(5);
        testCollection.add("Test");

        var expectedArray = new Object[]{5, "Test"};

        assertArrayEquals(expectedArray, testCollection.toArray());

    }

    @Test
    public void testGetMethod() {
        String test = "Test";
        var testCollection = new ArrayIndexedCollection();
        testCollection.add(test);

        assertEquals(test, testCollection.get(0));
    }

    @Test
    public void testClearMethod() {
        String test = "Test";
        var testCollection = new ArrayIndexedCollection();
        testCollection.add(test);

        testCollection.clear();
        assertFalse(testCollection.contains(test));
    }

    @Test
    public void testInsertMethod() {
        var first = 1;
        var second = "Second";
        var third = 3.0;
        var fourth = "4";

        var testCollection = new ArrayIndexedCollection(5);
        testCollection.add(first);
        testCollection.add(second);
        testCollection.add(third);
        testCollection.add(fourth);

        var test = "Test";

        testCollection.insert(test, 1);
        var array = testCollection.toArray();

        var expectedArray = new Object[]{first, test, second, third, fourth};

        assertArrayEquals(expectedArray, array);

    }

    @Test
    public void testIndexOfMethod() {
        var test = "Test";
        var testCollection = new ArrayIndexedCollection();
        testCollection.add(1);
        testCollection.add(2);
        testCollection.add(test);
        testCollection.add(3);

        assertEquals(2, testCollection.indexOf(test));
    }

    @Test
    public void testRemoveMethod() {
        var first = 1;
        var second = "Second";
        var third = 3.0;
        var fourth = "4";

        var testCollection = new ArrayIndexedCollection(5);
        testCollection.add(first);
        testCollection.add(second);
        testCollection.add(third);
        testCollection.add(fourth);

        testCollection.remove(1);
        var array = testCollection.toArray();

        var expectedArray = new Object[]{first, third, fourth};

        assertArrayEquals(expectedArray, array);
    }

    @Test
    public void testElementsGetter() {
        Collection col1 = new ArrayIndexedCollection();
        Collection col2 = new ArrayIndexedCollection();
        col1.add("Ivo");
        col1.add("Ana");
        col1.add("Jasna");
        col2.add("Jasmina");
        col2.add("Štefanija");
        col2.add("Karmela");
        ElementsGetter getter1 = col1.createElementsGetter();
        ElementsGetter getter2 = col1.createElementsGetter();
        ElementsGetter getter3 = col2.createElementsGetter();

        String result = "";
        result += getter1.getNextElement() + " ";
        result += getter1.getNextElement() + " ";
        result += getter2.getNextElement() + " ";
        result += getter3.getNextElement() + " ";
        result += getter3.getNextElement();

        assertEquals("Ivo Ana Ivo Jasmina Štefanija", result);
    }

    @Test
    public void testElementsGetterException() {
        assertThrows(NoSuchElementException.class, () -> {
            Collection col = new ArrayIndexedCollection(); // npr. new ArrayIndexedCollection();
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
        Collection col = new ArrayIndexedCollection(); // npr. new ArrayIndexedCollection();
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
            Collection col = new ArrayIndexedCollection();
            col.add("Ivo");
            col.add("Ana");
            col.add("Jasna");
            ElementsGetter getter = col.createElementsGetter();
            getter.getNextElement();
            getter.getNextElement();
            col.clear();
            getter.getNextElement();
        });
    }

}
