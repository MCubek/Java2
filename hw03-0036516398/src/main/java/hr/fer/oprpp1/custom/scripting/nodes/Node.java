package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;

import java.util.Objects;

/**
 * Apstraktna klasa koje predstavlja čvor
 * Sadrži polje u koje pohranjuje sve elemente
 *
 * @author matej
 */
public abstract class Node {
    private final ArrayIndexedCollection collection;

    public Node() {
        collection = new ArrayIndexedCollection();
    }

    /**
     * Metoda koja dodaje čvor kao dijete
     *
     * @throws NullPointerException ako je predan null
     */
    public void addChildNode(Node childNode) {
        collection.add(Objects.requireNonNull(childNode));
    }

    /**
     * Metoda koja vraća broj djece čvora
     *
     * @return broj djece
     */
    public int numberOfChildren() {
        return collection.size();
    }

    /**
     * Metoda koja traži i vraća dijete čvora na zadanom indeksu
     *
     * @param index indeks djeteta
     * @return dijete na indeksu
     * @throws IndexOutOfBoundsException ako je predan neispravan index
     */
    public Node getChild(int index) {
        return (Node) collection.get(index);
    }

    /**
     * Metoda koja pokrece akciju visitora ili posjetitelja.
     *
     * @param nodeVisitor visitor/posjetitelj
     */
    public abstract void accept(INodeVisitor nodeVisitor);
}
