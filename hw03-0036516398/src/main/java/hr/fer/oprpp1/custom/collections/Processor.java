package hr.fer.oprpp1.custom.collections;

/**
 * Model Procesora predstavlja konceptualni uguvor između klijenata koji će imati objekte za procesuiranje
 * Svaki procesor zna izvesti svoju operaciju
 *
 * @author matej
 */
public interface Processor {
    /**
     * Metoda koja obavlja procesorsku radnju nad objektom koji je predan
     *
     * @param value objekt nad kojim se obavlja procesorska radnja
     */
    void process(Object value);
}
