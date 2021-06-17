package hr.fer.oprpp2.hw5.model;

import java.util.Objects;

/**
 * Model for poll object
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 14/05/2021
 */
public class Poll {
    private Long id;
    private String title;
    private String message;

    public Poll(Long id, String title, String message) {
        this.id = id;
        this.title = title;
        this.message = message;
    }

    public Poll() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Poll poll = (Poll) o;

        if (! Objects.equals(id, poll.id)) return false;
        if (! Objects.equals(title, poll.title)) return false;
        return Objects.equals(message, poll.message);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
