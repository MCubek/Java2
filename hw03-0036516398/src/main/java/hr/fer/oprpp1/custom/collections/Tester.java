package hr.fer.oprpp1.custom.collections;

/**
 * Sučelje koje će ispitivati objekte i filtrirati ih tako da kaže zadovoljavaju li uvjet
 *
 * @author matej
 */
public interface Tester {
    /**
     * Metoda koja testira objekt i vraća zadovoljava li uvjet
     *
     * @param obj objekt koji se testira
     * @return <code>true</code> ako zadovoljava uvjet, inače <code>false</code>
     */
    boolean test(Object obj);
}
