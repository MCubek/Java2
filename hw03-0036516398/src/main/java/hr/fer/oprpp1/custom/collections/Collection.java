package hr.fer.oprpp1.custom.collections;

/**
 * Sučelje predstavlja općenitu kolekciju objekata.
 *
 * @author matej
 */
public interface Collection {
    /**
     * Metoda koja provjerava je li kolekcija prazna.
     *
     * @return <code>true</code> ako je prazna, <code>false</code> ako nije
     */
    default boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Metoda koja izračunava veličinu kolekcije.
     *
     * @return velićina kolekcije kao <code>int</code>
     */
    int size();

    /**
     * Metoda koja dodaje element u kolekciju.
     *
     * @param value element koji se nadodaje u kolekciju
     */
    void add(Object value);

    /**
     * Metoda koja provjerava je li element u kolekciji i vraća <code>true</code> ako je ili <code>false</code> ako nije.
     *
     * @param value objekt nad kojim se provjerava je li u kolekciji
     * @return <code>true</code> ako je element u kolekciji ili <code>false</code> ako nije
     * @throws NullPointerException ako je value <code>null</code>
     */
    boolean contains(Object value);

    /**
     * Metoda koja iz kolekcija izbacuje jednu pojavu tog objekta i vraća je li izbačen.
     *
     * @param value objekt koji se iz kolekcije izbavuje van
     * @return <code>true</code> ako je element bio u kolekciji i ako je izbačen ili <code>false</code> ako nije
     */
    boolean remove(Object value);

    /**
     * Metoda koja stvori novi array veličine kolekcije, napuni ga s svim elementima kolekcije i vrati ga.
     * Metoda nikada neće vratiti null.
     *
     * @return array veličine broja elemenata kolekcije s svim njenim elementima
     */
    Object[] toArray();

    /**
     * Metoda koja za svaki element u kolekciji poziva <code>Processor.process()</code>
     * Redoslijed kojim se elementi šalju nije definiran.
     *
     * @param processor processor kojem se šalje svaki element u kolekciji
     */
    default void forEach(Processor processor) {
        ElementsGetter elementsGetter = this.createElementsGetter();
        while (elementsGetter.hasNextElement()) {
            processor.process(elementsGetter.getNextElement());
        }
    }

    /**
     * Metoda koja će u kolekciju dodati sve elemente iz predane kolekcije.
     * Predana kolekcija ostati će nepromijenjena
     *
     * @param other druga kolekcija čiji će se elementi dodati
     */
    default void addAll(Collection other) {
        class AddProcessor implements Processor {
            /**
             * Metoda dodaje element u trenutnu kolekciju
             * @param value objekt koji se dodaju u trenutnu kolekciju
             */
            @Override
            public void process(Object value) {
                add(value);
            }
        }

        Processor myProcessor = new AddProcessor();
        other.forEach(myProcessor);
    }

    /**
     * Metoda koja briše sve elemente iz kolekcije
     */
    void clear();

    /**
     * Metoda će kreireti i vratiti implementaciju sučelja ElementsGetter za prolaženje kolekcijom
     *
     * @return nova instanca ElementsGettera
     */
    ElementsGetter createElementsGetter();

    /**
     * Metoda koja će dohvaćati redom sve elemente iz predane kolekcije col,
     * te u trenutnu kolekciju dodati na kraj ukoliko ih prihvati tester
     *
     * @param col    kolekcija cije se metoda ispitivaju i dodaju
     * @param tester tester kojim ce se ispitivati uvjet
     */
    default void addAllSatisfying(Collection col, Tester tester) {
        ElementsGetter getter = col.createElementsGetter();
        while (getter.hasNextElement()) {
            var element = getter.getNextElement();
            if (tester.test(element))
                add(element);
        }
    }
}
