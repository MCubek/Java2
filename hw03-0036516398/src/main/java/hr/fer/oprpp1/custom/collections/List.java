package hr.fer.oprpp1.custom.collections;

/**
 * Sučelje koje nasljećuje kolekciju i predstavlja listu.
 *
 * @author matej
 */
public interface List extends Collection {
    /**
     * Metoda vraća element u listi na zadanom indeksu.
     *
     * @param index indeks na kojem se nalazi traženi element
     * @return element na traženom indeksu
     */
    Object get(int index);

    /**
     * Metoda upisuje element u listu na zadanu poziciju tako da sve ostale element
     * pomakne u desno.
     *
     * @param value    element koji se upisuje
     * @param position pozicija na koju se upisuje element
     */
    void insert(Object value, int position);

    /**
     * Metoda vraća indeks zadanog elementa u listi
     *
     * @param value element čiji se indeks traži
     * @return indeks traženog elementa
     */
    int indexOf(Object value);

    /**
     * Metoda koja briše element na zadanom indeksu.
     *
     * @param index indeks čiji se element briše
     */
    void remove(int index);
}
