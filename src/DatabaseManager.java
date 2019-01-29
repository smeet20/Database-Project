import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseManager {
    private Connection m_connection;

    public DatabaseManager() {
        Properties connectionProperties = new Properties();
        connectionProperties.put("user", getUserName());
        connectionProperties.put("password", getPassword());
        m_connection = null;
        try {
            m_connection = DriverManager.getConnection("jdbc:mysql://localhost/", connectionProperties);
        } catch (SQLException e) {
            e.printStackTrace();
            AppManager.instance().quit();
        }
    }

    private String getUserName() {
        String user = System.getenv("DBUSER");
        if (user == null) {
            return "";
        }

        return user;
    }

    private String getPassword() {
        String password = System.getenv("DBPASSWORD");
        if (password == null) {
            return "";
        }

        return password;
    }
}
