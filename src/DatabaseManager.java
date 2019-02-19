import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private Connection m_connection;

    public DatabaseManager() {
        Properties connectionProperties = new Properties();
        connectionProperties.put("user", getUserName());
        connectionProperties.put("password", getPassword());
        m_connection = null;
        try {
            m_connection = DriverManager.getConnection("jdbc:mysql://localhost/twitter", connectionProperties);
            createTables();
        } catch (SQLException e) {
            e.printStackTrace();
            AppManager.quit();
        }
    }

    private void createTables() throws SQLException {
        createTwitterTable();
        createUserTable();
        createUserTweetRelationTable();
        createHashTagTable();
        createEventTable();
        createEventHashtagRelationTable();
    }

    private void createTwitterTable() throws SQLException {
        PreparedStatement statement = m_connection.prepareStatement("" +
                "CREATE TABLE IF NOT EXISTS tweets" +
                "(" +
                "id INTEGER UNSIGNED AUTO_INCREMENT PRIMARY KEY," +
                "parent_id INTEGER UNSIGNED," +
                "time TIMESTAMP NOT NULL," +
                "likes INTEGER UNSIGNED DEFAULT 0," +
                "retweets INTEGER UNSIGNED DEFAULT 0," +
                "text NVARCHAR(140) NOT NULL" +
                ")");
        statement.execute();
    }

    private void createHashTagTable() throws SQLException {
        PreparedStatement statement = m_connection.prepareStatement("" +
                "CREATE TABLE IF NOT EXISTS hashtags" +
                "(" +
                "id INTEGER UNSIGNED AUTO_INCREMENT PRIMARY KEY," +
                "text NVARCHAR(140) NOT NULL" +
                ")");

        statement.execute();
    }

    private void createUserTable() throws SQLException {
        PreparedStatement statement = m_connection.prepareStatement("" +
                "CREATE TABLE IF NOT EXISTS users" +
                "(" +
                "id INTEGER UNSIGNED AUTO_INCREMENT PRIMARY KEY," +
                "username NVARCHAR(15) NOT NULL," +
                "followers INTEGER UNSIGNED DEFAULT 0," +
                "following INTEGER UNSIGNED DEFAULT 0" +
                ")");

        statement.execute();
    }

    private void createUserTweetRelationTable() throws SQLException {
        PreparedStatement statement = m_connection.prepareStatement("" +
                "CREATE TABLE IF NOT EXISTS user_tweet" +
                "(" +
                "user_id INTEGER UNSIGNED NOT NULL," +
                "tweet_id INTEGER UNSIGNED NOT NULL" +
                ")");

        statement.execute();
    }

    private void createEventTable() throws SQLException {
        PreparedStatement statement = m_connection.prepareStatement("" +
                "CREATE TABLE IF NOT EXISTS events_table" +
                "(" +
                "id INTEGER UNSIGNED AUTO_INCREMENT PRIMARY KEY," +
                "name NVARCHAR(40) NOT NULL" +
                ")");

        statement.execute();
    }

    private void createEventHashtagRelationTable() throws SQLException {
        PreparedStatement statement = m_connection.prepareStatement("" +
                "CREATE TABLE IF NOT EXISTS event_hashtag" +
                "(" +
                "event_id INTEGER UNSIGNED NOT NULL," +
                "hashtag_id INTEGER UNSIGNED NOT NULL" +
                ")");

        statement.execute();
    }

    private String getUserName() {
        String user = "anmol";
        return user;
    }

    private String getPassword() {
        String password = "";
        return password;
    }
}
