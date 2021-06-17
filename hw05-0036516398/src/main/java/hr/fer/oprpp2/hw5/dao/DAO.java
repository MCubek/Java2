package hr.fer.oprpp2.hw5.dao;

import hr.fer.oprpp2.hw5.model.Poll;
import hr.fer.oprpp2.hw5.model.PollOption;

import java.util.List;

/**
 * Interface to data persistance subsystem.
 *
 * @author MatejCubek
 */
public interface DAO {

    /**
     * Gets all polls in database and returns it as a list.
     *
     * @return list of polls
     * @throws DAOException when error occurs
     */
    List<Poll> getPollList();

    /**
     * Gets poll in database with given id.
     * If poll with id doesn't exit returns null
     *
     * @param id poll id
     * @return Poll or null if doesn't exit
     */
    Poll getPollById(long id);

    /**
     * Gets all PollOptions in database for poll with id that is
     * sent in the argument and returns them in a list.
     *
     * @param pollId id of poll for which options are returned
     * @return list of pollOptions for poll with pollId
     * @throws DAOException when error occurs
     */
    List<PollOption> getPollOptionsForPoll(long pollId);

    /**
     * Gets pollOptions for given id or null if doesn't exist.
     *
     * @param pollOptionId id
     * @return PollOptions or null if none
     */
    PollOption getPollOptionById(long pollOptionId);

    /**
     * Casts vote to candaidate with given id
     *
     * @param pollOptionId id of candidate to cast vote for
     */
    void castVoteToPollOption(long pollOptionId);

}