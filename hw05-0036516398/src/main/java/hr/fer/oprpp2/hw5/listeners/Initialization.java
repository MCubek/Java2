package hr.fer.oprpp2.hw5.listeners;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

@SuppressWarnings("SpellCheckingInspection")
@WebListener
public class Initialization implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String connectionURL = loadConnectionUrlFromProperties(sce);

        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        try {
            //noinspection SpellCheckingInspection
            comboPooledDataSource.setDriverClass("org.apache.derby.client.ClientAutoloadedDriver");
        } catch (PropertyVetoException e1) {
            throw new RuntimeException("Error while initializing pool.", e1);
        }
        comboPooledDataSource.setJdbcUrl(connectionURL);

        createDefaultTablesIfNotExist(comboPooledDataSource);

        //noinspection SpellCheckingInspection
        sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", comboPooledDataSource);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //noinspection SpellCheckingInspection
        ComboPooledDataSource cpds = (ComboPooledDataSource) sce.getServletContext().getAttribute("hr.fer.zemris.dbpool");
        if (cpds != null) {
            try {
                DataSources.destroy(cpds);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method for loading database parameters from properties file.
     *
     * @param sce Servlet context event
     * @return database connection url
     * @throws RuntimeException when error occurs while connecting
     */
    private String loadConnectionUrlFromProperties(ServletContextEvent sce) throws RuntimeException {
        //noinspection SpellCheckingInspection
        Path propertiesPath = Path.of(sce.getServletContext().getRealPath("WEB-INF/dbsettings.properties"));
        Properties properties = new Properties();
        try {
            properties.load(Files.newInputStream(propertiesPath));
        } catch (IOException e) {
            throw new RuntimeException("Error while loading properties.", e);
        }

        return String.format("jdbc:derby://%s:%s/%s;user=%s;password=%s",
                properties.getProperty("host"),
                properties.get("port"),
                properties.getProperty("name"),
                properties.get("user"),
                properties.get("password"));
    }

    /**
     * Method checks if default tables exist in database end if they dont
     * creates them with default data.
     *
     * @param comboPooledDataSource pooled data source
     * @throws RuntimeException when error occurs while checking or creating default tables
     */
    private void createDefaultTablesIfNotExist(ComboPooledDataSource comboPooledDataSource) throws RuntimeException {
        try (Connection connection = comboPooledDataSource.getConnection()) {

            Set<String> tables = new HashSet<>();
            try (ResultSet resultSet = connection.getMetaData()
                    .getTables(null, connection.getSchema().toUpperCase(), null, null)) {
                while (resultSet != null && resultSet.next()) {
                    tables.add(resultSet.getString("TABLE_NAME"));
                }
            }
            List<Long> pollIndexes = null;
            if (! tables.contains("POLLS")) {
                pollIndexes = createPollsTable(connection);
            }
            if (! tables.contains("POLLOPTIONS")) {
                createPollOptionsTable(connection, pollIndexes);
            }

        } catch (SQLException throwable) {
            throw new RuntimeException("Can't verify database tables.");
        }
    }

    /**
     * Method for creating polls table if one does not exist.
     * Method returns list of primary keys for all added entries into table
     * in order of addition.
     *
     * @param connection connection to database
     * @return list of primary keys for all added entries into table
     * @throws SQLException when error whiel adding occurs
     */
    private List<Long> createPollsTable(Connection connection) throws SQLException {
        List<Long> list = new ArrayList<>();
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("""
                             CREATE TABLE Polls
                                 (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                 title VARCHAR(150) NOT NULL,
                                 message CLOB(2048) NOT NULL
                             )""")) {
            preparedStatement.executeUpdate();


            //Demo podaci
            addPollAndInsertIdIntoList("Glasanje za omiljeni bend",
                    "Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!", connection, list);
            addPollAndInsertIdIntoList("Glasanje za omiljeno jezero u zagrebu",
                    "Od sljedećih jezera, koje Vam je jezero najdraže? Kliknite na link kako biste glasali!", connection, list);
        }
        return list;
    }

    /**
     * Helper method for adding poll entries into database
     *
     * @param title      poll title
     * @param message    poll message
     * @param connection database connection
     * @param list       list to which primary key will be appended
     * @throws SQLException when error occurs while adding poll entry
     */
    private void addPollAndInsertIdIntoList(String title, String message, Connection connection, List<Long> list) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("""
                INSERT INTO Polls (title,message) VALUES
                (?,?)
                """, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, message);
            preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet != null && resultSet.next()) {
                    list.add(resultSet.getLong(1));
                }
            }
        }

    }

    /**
     * Method for creating poll options table and adding default values into it
     * if they do not exist.
     *
     * @param connection  database connection
     * @param pollIndexes list od poll entry primary keys for which to add default values
     * @throws SQLException when database error occurs
     */
    private void createPollOptionsTable(Connection connection, List<Long> pollIndexes) throws SQLException {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("""
                             CREATE TABLE PollOptions
                                 (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                 optionTitle VARCHAR(100) NOT NULL,
                                 optionLink VARCHAR(150) NOT NULL,
                                 pollID BIGINT,
                                 votesCount BIGINT,
                                 FOREIGN KEY (pollID) REFERENCES Polls(id)
                             )""")) {
            preparedStatement.executeUpdate();
        }

        //Check if indexes are known for demo polls
        //If poll indexes are unknown skip
        if (pollIndexes == null || pollIndexes.size() < 2) return;

        //Demo values setup
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("""
                             INSERT INTO PollOptions (optionTitle,OptionLink,pollID,votesCount) VALUES
                                 (?,?,?,0)
                             """);) {
            long firstId = pollIndexes.get(0);
            insertPollOptionIntoBatch("The Beatles", "https://www.youtube.com/watch?v=z9ypq6_5bsg", firstId, preparedStatement);
            insertPollOptionIntoBatch("The Platters", "https://www.youtube.com/watch?v=H2di83WAOhU", firstId, preparedStatement);
            insertPollOptionIntoBatch("The Beach Boys", "https://www.youtube.com/watch?v=2s4slliAtQU", firstId, preparedStatement);
            insertPollOptionIntoBatch("The Four Seasons", "https://www.youtube.com/watch?v=y8yvnqHmFds", firstId, preparedStatement);
            insertPollOptionIntoBatch("The Marcels", "https://www.youtube.com/watch?v=qoi3TH59ZEs", firstId, preparedStatement);
            insertPollOptionIntoBatch("The Everly Brothers", "https://www.youtube.com/watch?v=tbU3zdAgiX8", firstId, preparedStatement);
            insertPollOptionIntoBatch("The Mamas And The Papas", "https:/www.youtube.com/watch?v=N-aK6JnyFmk", firstId, preparedStatement);

            long secondId = pollIndexes.get(1);
            insertPollOptionIntoBatch("Jarun", "https://upload.wikimedia.org/wikipedia/commons/5/53/Jarun_2.jpg", secondId, preparedStatement);
            insertPollOptionIntoBatch("Bundek", "https://www.infozagreb.hr/media/places/large_bundek1-54d0c975856c6.jpg", secondId, preparedStatement);
            insertPollOptionIntoBatch("Maksimir", "https://licegrada.hr/wp-content/uploads/2016/11/maksimir-jezera-01112016.jpg", secondId, preparedStatement);
            insertPollOptionIntoBatch("Podvožnjak u Miramarskoj nakon kiše", "https://direktno.hr/upload/publish/201878/pxl-250720-29628116_5f1bd1d076962.jpg", secondId, preparedStatement);

            preparedStatement.executeBatch();
        }

    }

    /**
     * Helper method for adding poll option into PreparedStatement batch
     *
     * @param name              poll opiton name
     * @param url               poll option url
     * @param id                poll id
     * @param preparedStatement prepared statement batch
     * @throws SQLException when error occurs while adding to batch
     */
    private void insertPollOptionIntoBatch(String name, String url, Long id, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, url);
        preparedStatement.setLong(3, id);
        preparedStatement.addBatch();
    }

}
