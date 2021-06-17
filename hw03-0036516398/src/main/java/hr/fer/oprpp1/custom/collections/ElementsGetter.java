package hr.fer.oprpp1.custom.collections;

/**
 * Sučelje koje definira jednostavan način obilaska kolekcije
 *
 * @author matej
 */
public interface ElementsGetter {
    /**
     * Apstraktna metoda koja gleda ima li jos elemenata koje nismo obišli
     *
     * @return <code>true</code> ako još ima elemenata, inače <code>false</code>
     * @throws java.util.ConcurrentModificationException ako je kolekcija mijenjana
     */
    boolean hasNextElement();

    /**
     * Apstraktna metoda koja vaća sljedeći element koji nismo obišli ako ih još ima.
     * U suprotnom baca exception.
     *
     * @return sljedeći element u kolekciji.
     * @throws java.util.NoSuchElementException          ako nema više elemenata
     * @throws java.util.ConcurrentModificationException ako je kolekcija mijenjana
     */
    Object getNextElement();

    /**
     * Metoda koja nad svim neobiđenim elementima kolekcija poziva zadani proceesor
     *
     * @param p procesor koji se poziva nad neobiđenim elementima
     */
    default void processRemaining(Processor p) {
        while (hasNextElement()) {
            p.process(getNextElement());
        }
    }
}
