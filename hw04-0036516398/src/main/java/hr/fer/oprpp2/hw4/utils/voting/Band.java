package hr.fer.oprpp2.hw4.utils.voting;

/**
 * Band record.
 * Getters are added alongide generated ones for JSTL support.
 *
 * @author MatejCubek
 * @project hw04-0036516398
 * @created 09/04/2021
 */
public record Band(long id, String name, String url) {
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
