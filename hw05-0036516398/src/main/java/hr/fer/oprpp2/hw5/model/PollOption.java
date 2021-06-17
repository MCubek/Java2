package hr.fer.oprpp2.hw5.model;

import java.util.Objects;

/**
 * PollOption model
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 14/05/2021
 */
public class PollOption {
    private Long id;
    private String optionTitle;
    private String optionLink;
    private Long pollId;
    private Long votesCount;

    public PollOption(Long id, String optionTitle, String optionLink, Long pollId, Long votesCount) {
        this.id = id;
        this.optionTitle = optionTitle;
        this.optionLink = optionLink;
        this.pollId = pollId;
        this.votesCount = votesCount;
    }

    public PollOption() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionTitle() {
        return optionTitle;
    }

    public void setOptionTitle(String optionTitle) {
        this.optionTitle = optionTitle;
    }

    public String getOptionLink() {
        return optionLink;
    }

    public void setOptionLink(String optionLink) {
        this.optionLink = optionLink;
    }

    public Long getPollId() {
        return pollId;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }

    public Long getVotesCount() {
        return votesCount;
    }

    public void setVotesCount(Long votesCount) {
        this.votesCount = votesCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PollOption that = (PollOption) o;

        if (! Objects.equals(id, that.id)) return false;
        if (! Objects.equals(optionTitle, that.optionTitle)) return false;
        if (! Objects.equals(optionLink, that.optionLink)) return false;
        if (! Objects.equals(pollId, that.pollId)) return false;
        return Objects.equals(votesCount, that.votesCount);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (optionTitle != null ? optionTitle.hashCode() : 0);
        result = 31 * result + (optionLink != null ? optionLink.hashCode() : 0);
        result = 31 * result + (pollId != null ? pollId.hashCode() : 0);
        result = 31 * result + (votesCount != null ? votesCount.hashCode() : 0);
        return result;
    }
}
