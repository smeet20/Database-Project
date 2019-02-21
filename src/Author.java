public class Author {
    private String m_id;
    private String m_name;

    public Author(String id, String name) {
        m_id = id;
        m_name = name;
    }

    public String id() {
        return m_id;
    }

    public String name() {
        return m_name;
    }
}
