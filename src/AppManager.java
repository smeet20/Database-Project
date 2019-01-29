public class AppManager {
    public static AppManager s_instance;
    private DatabaseManager m_databaseManager;

    public AppManager() {
        m_databaseManager = new DatabaseManager();
    }

    public static AppManager instance() {
        s_instance = new AppManager();
        return s_instance;
    }

    public DatabaseManager databaseManager() {
        return m_databaseManager;
    }

    public void quit() {
        System.exit(1);
    }

    public static void main(String[] args) {
        AppManager.instance().databaseManager();
    }
}
