package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Implementacija linked-list-backed kolekcije
 * Duplikati su dozvoljeni, ali spremanje <code>null</code> referenci nije
 *
 * @author matej
 */
public class LinkedListIndexedCollection implements List {
    private int size;
    private ListNode first;
    private ListNode last;
    private long modificationCount = 0;

    /**
     * Defaultni konstuktor koji ne stvara niti jedan cvor
     */
    public LinkedListIndexedCollection() {
        this.size = 0;
        this.first = null;
        this.last = null;
    }

    /**
     * Konstruktor koji će spremiti u kolekciju sve elemente iz dane bez da mijenja danu kolekciju
     *
     * @param collection kolekcija čiji će se članovi kopirati
     * @throws NullPointerException ako je predan null kao kolekcija
     */
    public LinkedListIndexedCollection(Collection collection) {
        addAll(Objects.requireNonNull(collection));
        /* // Bez addAll
        if (collection == null) throw new NullPointerException("Collection must not be null");
        var collectionArray = collection.toArray();
        size = 0;
        ListNode curr = null;

        for (var element : collectionArray) {
            if (element == null) continue;
            ListNode listNode = new ListNode(element);
            if (size == 0) {
                first = listNode;
            } else {
                curr.next = listNode;
            }
            listNode.previous = curr;
            curr = listNode;
            size++;
        }
        last = curr;*/
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Metoda koja dodaje element na kraj u kolekciju.
     * Kompleksnost O(1)
     *
     * @param value element koji se nadodaje u kolekciju
     * @throws NullPointerException ako je element <code>null</code>
     */
    @Override
    public void add(Object value) {
        if (value == null) throw new NullPointerException("Value can't be null!");
        ListNode node = new ListNode(value);
        if (size == 0) {
            first = node;
        } else {
            last.next = node;
            node.previous = last;
        }
        last = node;
        size++;
        modificationCount++;
    }

    @Override
    public boolean contains(Object value) {
        ListNode node = first;
        while (node != null) {
            if (node.element.equals(value)) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean remove(Object value) {
        ListNode node = first;
        while (node != null) {
            if (node.element.equals(value)) {
                node.next.previous = node.previous;
                node.previous.next = node.next;
                size--;
                modificationCount++;
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        var array = new Object[size];
        ListNode node = first;
        for (int i = 0; i < size; i++, node = node.next) {
            array[i] = node.element;
        }
        return array;
    }

/*    @Override
    public void forEach(Processor processor) {
        ListNode node = first;
        while (node != null) {
            processor.process(node);
            node = node.next;
        }
    }*/

    @Override
    public void addAll(Collection other) {
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
        modificationCount++;
    }

    @Override
    public void clear() {
        size = 0;
        first = null;
        last = null;
        modificationCount++;
    }

    /**
     * Metoda koja vraca objekt koji se nalazi na zadanom indexu u kolekciji
     * Validni indexi su [0,size-1]
     *
     * @param index objekta koji se trazi
     * @return objekt na zadanom indeksu
     * @throws IndexOutOfBoundsException ako je index izvan intervala [0,size-1]
     */
    public Object get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index is invalid!");
        ListNode node = findNodeInPosition(index);
        return node.element;
    }

    /**
     * Metoda koja na danu poziciju stavlja dan element bez da se izgubi iti jedan element iz polja.
     * Elementi koji smetaju će se pokaknuti tako da stane.
     * Kompleksnost O(n)
     *
     * @param value    vrijednost koju zelimo upisati u kolekciju
     * @param position pozicija na koju zelimo upisti vrijednost u kolekciju
     * @throws IndexOutOfBoundsException ako je pozicija izvan intervala [0,size]
     * @throws NullPointerException      ako je predan element <code>null</code>
     */
    public void insert(Object value, int position) {
        if (value == null) throw new NullPointerException("Value can't be null!");
        if (position < 0 || position >= size) throw new IndexOutOfBoundsException("Index is invalid!");
        ListNode node = findNodeInPosition(position);
        ListNode newNode = new ListNode(value, node.previous, node);
        if (node.previous != null)
            node.previous.next = newNode;
        if (node.next != null)
            node.next.previous = newNode;
        if (position == 0)
            first = newNode;
        if (position == size - 1)
            last = newNode;

        size++;
        modificationCount++;
    }

    /**
     * Privatna metoda koja pronalazi traženi čvor na zadanom indeksu u linked-listi i vrati ga
     *
     * @param position indeks čvora
     * @return ListNode na poziciji indeksa
     */
    private ListNode findNodeInPosition(int position) {
        ListNode node;
        if (position < size / 2) {
            node = first;
            for (int i = 0; i < size / 2; i++, node = node.next) {
                if (i == position) {
                    break;
                }
            }
        } else {
            node = last;
            for (int i = size - 1; i >= size / 2; i--, node = node.previous) {
                if (i == position) {
                    break;
                }
            }
        }
        return node;
    }

    /**
     * Metoda koja vraća prvi pronađeni index predanog elementa u kolekciji.
     * Ukoliko element nije u kolekciji vraća -1.
     * <p>
     * O(n) kompleksnost
     *
     * @param value element za koji se pretražuje index
     * @return index ako je elemnt pronađen, inače -1
     */
    public int indexOf(Object value) {
        ListNode node = first;
        int i = 0;
        while (node != null) {
            if (node.element.equals(value)) {
                return i;
            }
            i++;
            node = node.next;
        }
        return - 1;
    }

    /**
     * Metoda koja iz kolekcija element na zadanom indexu.
     *
     * @throws IndexOutOfBoundsException ako je index izvan raspona [0,size-1]
     */
    public void remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        ListNode node = findNodeInPosition(index);
        node.previous.next = node.next;
        node.next.previous = node.previous;
        size--;
        modificationCount++;
    }

    @Override
    public ElementsGetter createElementsGetter() {
        return new LinkedListIndexedCollectionElementsGetter(this);
    }

    /**
     * Statička klasa koja predstavlja čvor linked-liste
     * Sadrži  prijašnji i sljedeći pokazivač te element koji sadrži vrijednost
     */
    private static class ListNode {
        ListNode previous;
        ListNode next;
        Object element;

        /**
         * Konstruktor koji prima element i sprema ga u cvor
         *
         * @param element element koji se sprema
         * @throws NullPointerException ako je poslan null kao element
         */
        private ListNode(Object element) {
            this(element, null, null);
        }

        /**
         * Konstrukor koji prima element i pokazivace na susjedne cvorove
         *
         * @param element  element koji se sprema
         * @param previous pokazivac na prijasnji element
         * @param next     pokazivac na iduci element
         * @throws NullPointerException ako je poslan null kao element
         */
        private ListNode(Object element, ListNode previous, ListNode next) {
            this.element = Objects.requireNonNull(element);
            this.previous = previous;
            this.next = next;
        }
    }

    /**
     * Klasa ElekementsGettera za LinkedListIndexedCollection
     * Podrazumijeva se da se polje neće mijenjati za vrijeme korištenja.
     */
    private static class LinkedListIndexedCollectionElementsGetter implements ElementsGetter {
        private final LinkedListIndexedCollection collection;
        private final long savedModificationCount;
        private ListNode positionNode;

        /**
         * Konstruktor ElementsGettera LinkedListe koji prima referencu na listu
         *
         * @param collection referenca na linkedListu
         * @throws NullPointerException ako je predan null kao referenca
         */
        private LinkedListIndexedCollectionElementsGetter(LinkedListIndexedCollection collection) {
            this.collection = Objects.requireNonNull(collection);
            positionNode = collection.first;
            savedModificationCount = collection.modificationCount;
        }

        @Override
        public boolean hasNextElement() {
            if (savedModificationCount != collection.modificationCount)
                throw new ConcurrentModificationException("Collection has been modified!");
            return positionNode != null;
        }

        @Override
        public Object getNextElement() {
            if (savedModificationCount != collection.modificationCount)
                throw new ConcurrentModificationException("Collection has been modified!");
            if (! hasNextElement()) throw new NoSuchElementException("There are no elements!");

            ListNode currentNode = positionNode;
            positionNode = positionNode.next;
            return currentNode.element;
        }
    }
}

