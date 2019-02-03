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
            m_connection = DriverManager.getConnection("jdbc:mysql://localhost/twitter", connectionProperties);
        } catch (SQLException e) {
            e.printStackTrace();
            AppManager.quit();
        }
    }

    private String getUserName() {
        String user = "root";
        return user;
    }

    private String getPassword() {
        String password = "1234";
        return password;
    }
}
