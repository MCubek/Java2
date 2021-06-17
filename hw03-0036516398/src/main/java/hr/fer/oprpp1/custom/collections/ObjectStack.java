package hr.fer.oprpp1.custom.collections;

/**
 * Klasa koja predstavlja stog
 *
 * @author matej
 */
public class ObjectStack {
    private final ArrayIndexedCollection arrayIndexedCollection;

    /**
     * Defaultni konstruktor
     */
    public ObjectStack() {
        arrayIndexedCollection = new ArrayIndexedCollection();
    }

    /**
     * Metoda koja ispituje je li stog prazan
     *
     * @return <code>true</code> ako je stog prazan, <code>false</code> inače
     */
    public boolean isEmpty() {
        return arrayIndexedCollection.isEmpty();
    }

    /**
     * Metoda koja vraća broj elemenata u polju
     *
     * @return broj elemenata u polju
     */
    public int size() {
        return arrayIndexedCollection.size();
    }

    /**
     * Metoda koja na vrh stoga stavlja predan element
     *
     * @param value element koji se stavlja na vrh stoga
     * @throws NullPointerException ako je predan null
     */
    public void push(Object value) {
        arrayIndexedCollection.add(value);
    }

    /**
     * Metoda koja vraća element sa vrha stoga i onda ga makne
     *
     * @return element s vrha stoga
     * @throws EmptyStackException ako nema elemenata na stogu
     */
    public Object pop() {
        if (isEmpty()) throw new EmptyStackException("Stack is empty!");
        var object = arrayIndexedCollection.get(arrayIndexedCollection.size() - 1);
        arrayIndexedCollection.remove(arrayIndexedCollection.size() - 1);
        return object;
    }

    /**
     * Metoda koja vraća element s vrha stoga i ne skida ga s stoga
     *
     * @return element s vrha stoga
     * @throws EmptyStackException ako nema elemenata na stogu
     */
    public Object peek() {
        if (isEmpty()) throw new EmptyStackException("Stack is empty!");
        return arrayIndexedCollection.get(arrayIndexedCollection.size() - 1);
    }

    /**
     * Metoda koja briše sve elemente sa stoga
     */
    public void clear() {
        arrayIndexedCollection.clear();
    }

}
