package hr.fer.oprpp2.hw5.dao.sql;

import hr.fer.oprpp2.hw5.dao.DAO;
import hr.fer.oprpp2.hw5.dao.DAOException;
import hr.fer.oprpp2.hw5.model.Poll;
import hr.fer.oprpp2.hw5.model.PollOption;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Ovo je implementacija podsustava DAO uporabom tehnologije SQL. Ova
 * konkretna implementacija očekuje da joj veza stoji na raspolaganju
 * preko {@link SQLConnectionProvider} razreda, što znači da bi netko
 * prije no što izvođenje dođe do ove točke to trebao tamo postaviti.
 * U web-aplikacijama tipično rješenje je konfigurirati jedan filter
 * koji će presresti pozive servleta i prije toga ovdje ubaciti jednu
 * vezu iz connection-poola, a po zavrsetku obrade je maknuti.
 * <p>
 * Javadoc copied from showcase by MarkoCupic
 *
 * @author MatejCubek
 */
@SuppressWarnings("DuplicatedCode")
public class SQLDAO implements DAO {
    @Override
    public List<Poll> getPollList() {
        List<Poll> polls = new ArrayList<>();

        Connection con = SQLConnectionProvider.getConnection();

        try {
            try (PreparedStatement ps = con.prepareStatement("""
                    SELECT ID,TITLE,MESSAGE FROM POLLS ORDER BY ID
                    """)) {

                try (ResultSet res = ps.executeQuery()) {
                    while (res != null && res.next()) {
                        Poll poll = new Poll();
                        poll.setId(res.getLong(1));
                        poll.setTitle(res.getString(2));
                        poll.setMessage(res.getString(3));
                        polls.add(poll);
                    }
                }
            }
        } catch (Exception e) {
            throw new DAOException("Error occurred while gathering polls.", e);
        }
        return polls;
    }

    @Override
    public Poll getPollById(long id) {
        Connection con = SQLConnectionProvider.getConnection();
        try {
            try (PreparedStatement ps = con.prepareStatement("""
                    SELECT ID,TITLE,MESSAGE FROM POLLS
                    WHERE ID = ?
                    """)) {
                ps.setLong(1, id);

                try (ResultSet res = ps.executeQuery()) {
                    if (res != null && res.next()) {
                        Poll poll = new Poll();
                        poll.setId(res.getLong(1));
                        poll.setTitle(res.getString(2));
                        poll.setMessage(res.getString(3));
                        return poll;
                    }
                }
            }
        } catch (Exception e) {
            throw new DAOException("Error occurred while gathering poll.", e);
        }
        return null;
    }

    @Override
    public List<PollOption> getPollOptionsForPoll(long pollId) {
        List<PollOption> pollOptions = new ArrayList<>();

        Connection con = SQLConnectionProvider.getConnection();

        try {
            try (PreparedStatement ps = con.prepareStatement("""
                    SELECT ID,OPTIONTITLE,OPTIONLINK,POLLID,VOTESCOUNT
                    FROM POLLOPTIONS
                    WHERE POLLID = ?
                    ORDER BY ID
                    """)) {

                ps.setLong(1, pollId);

                try (ResultSet res = ps.executeQuery()) {
                    while (res != null && res.next()) {
                        PollOption pollOption = new PollOption();
                        pollOption.setId(res.getLong(1));
                        pollOption.setOptionTitle(res.getString(2));
                        pollOption.setOptionLink(res.getString(3));
                        pollOption.setPollId(res.getLong(4));
                        pollOption.setVotesCount(res.getLong(5));
                        pollOptions.add(pollOption);
                    }
                }
            }
        } catch (Exception e) {
            throw new DAOException("Error occurred while gathering pollOptions.", e);
        }
        return pollOptions;
    }

    @Override
    public PollOption getPollOptionById(long pollOptionId) {
        Connection con = SQLConnectionProvider.getConnection();

        try {
            try (PreparedStatement ps = con.prepareStatement("""
                    SELECT ID,OPTIONTITLE,OPTIONLINK,POLLID,VOTESCOUNT
                    FROM POLLOPTIONS
                    WHERE ID = ?
                    """)) {

                ps.setLong(1, pollOptionId);

                try (ResultSet res = ps.executeQuery()) {
                    if (res != null && res.next()) {
                        PollOption pollOption = new PollOption();
                        pollOption.setId(res.getLong(1));
                        pollOption.setOptionTitle(res.getString(2));
                        pollOption.setOptionLink(res.getString(3));
                        pollOption.setPollId(res.getLong(4));
                        pollOption.setVotesCount(res.getLong(5));
                        return pollOption;
                    }
                }
            }
        } catch (Exception e) {
            throw new DAOException("Error occurred while gathering pollOption.", e);
        }
        return null;
    }

    @Override
    public void castVoteToPollOption(long pollOptionId) {
        Connection con = SQLConnectionProvider.getConnection();

        try {
            try (PreparedStatement ps = con.prepareStatement("""
                    UPDATE POLLOPTIONS SET VOTESCOUNT = VOTESCOUNT+1
                    WHERE ID = ?
                    """)) {

                ps.setLong(1, pollOptionId);

                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new DAOException("Error occurred while incrementing pollOption.", e);
        }
    }
}