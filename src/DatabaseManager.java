import java.sql.*;
import java.util.List;
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
        createAuthorTable();
        createAuthorTweetRelationTable();
        createHashTagTable();
        createEventTable();
        createEventHashtagRelationTable();
        createUsersTable();
        createUserQueryTable();
    }

    private void createTwitterTable() throws SQLException {
        PreparedStatement statement = m_connection.prepareStatement("" +
                "CREATE TABLE IF NOT EXISTS tweets" +
                "(" +
                "id BIGINT PRIMARY KEY," +
                "parent_id BIGINT," +
                "time TIMESTAMP NOT NULL," +
                "likes INTEGER UNSIGNED DEFAULT 0," +
                "replies INTEGER UNSIGNED DEFAULT 0," +
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

    private void createAuthorTable() throws SQLException {
        PreparedStatement statement = m_connection.prepareStatement("" +
                "CREATE TABLE IF NOT EXISTS author" +
                "(" +
                "id BIGINT PRIMARY KEY," +
                "name NVARCHAR(15) NOT NULL," +
                "followers INTEGER UNSIGNED DEFAULT 0," +
                "following INTEGER UNSIGNED DEFAULT 0" +
                ")");

        statement.execute();
    }

    private void createAuthorTweetRelationTable() throws SQLException {
        PreparedStatement statement = m_connection.prepareStatement("" +
                "CREATE TABLE IF NOT EXISTS author_tweet" +
                "(" +
                "author_id BIGINT NOT NULL," +
                "tweet_id BIGINT NOT NULL," +
                "PRIMARY KEY (author_id, tweet_id)" +
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
                "hashtag_id INTEGER UNSIGNED NOT NULL," +
                "PRIMARY KEY (event_id, hashtag_id)" +
                ")");

        statement.execute();
    }

    private void createUsersTable() throws SQLException {
        PreparedStatement statement = m_connection.prepareStatement("" +
                "CREATE TABLE IF NOT EXISTS users" +
                "(" +
                "id INTEGER UNSIGNED PRIMARY KEY," +
                "name NVARCHAR(40)," +
                "email VARCHAR(320)," +
                "password VARCHAR(64)," +
                "salt VARCHAR(8)" +
                ")");
        statement.execute();
    }

    private void createUserQueryTable() throws SQLException {
        PreparedStatement statement = m_connection.prepareStatement("" +
                "CREATE TABLE IF NOT EXISTS user_query" +
                "(" +
                "user_id INTEGER UNSIGNED NOT NULL," +
                "query NVARCHAR(140) NOT NULL," +
                "PRIMARY KEY (user_id, query)" +
                ")");
        statement.execute();
    }

    public boolean insertTweet(Tweet tweet) throws SQLException {
        PreparedStatement statement = m_connection.prepareStatement("" +
                "INSERT INTO tweets" +
                "(id, parent_id, time, likes, replies, retweets, text)" +
                "VALUES" +
                "(?, ?, ?, ?, ?, ?, ?)");
        statement.setString(1, tweet.tweetId());
        statement.setString(2, tweet.parentId());
        statement.setTimestamp(3, new Timestamp(tweet.timestamp()));
        statement.setInt(4, tweet.likes());
        statement.setInt(5, tweet.replies());
        statement.setInt(6, tweet.retweets());
        statement.setString(7, tweet.text());
        return statement.execute();
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
