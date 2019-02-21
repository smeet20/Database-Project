import java.io.IOException;
import java.sql.SQLException;

public class AppManager {
    public static AppManager s_instance = new AppManager();
    private static DatabaseManager m_databaseManager = new DatabaseManager();
    private static Scrapper m_scrapper = new Scrapper();

    public static AppManager instance() {
        return s_instance;
    }

    public static DatabaseManager databaseManager() {
        return m_databaseManager;
    }

    public static Scrapper scrapper() {
        return m_scrapper;
    }

    public static void quit() {
        System.exit(1);
    }

    public static void main(String[] args) throws IOException, SQLException {
        AppManager.instance();

        AppManager.scrapper().scrap("pakistan");
    }
}
