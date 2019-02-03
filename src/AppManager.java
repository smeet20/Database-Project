public class AppManager {
    public static AppManager s_instance = new AppManager();
    private static DatabaseManager m_databaseManager = new DatabaseManager();

    public static AppManager instance() {
        return s_instance;
    }

    public static DatabaseManager databaseManager() {
        return m_databaseManager;
    }

    public static void quit() {
        System.exit(1);
    }

    public static void main(String[] args) {
        AppManager.instance();
    }
}
