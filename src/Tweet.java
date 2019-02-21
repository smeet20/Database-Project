import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Tweet {
    private String m_tweetId;
    private String m_parentId;
    private long m_timestamp;
    private String m_text;
    private int m_likes;
    private int m_replies;
    private int m_retweets;

    public Tweet(String tweetId, String parentId, long timestamp, String text, int likes, int replies, int retweets) {
        m_tweetId = tweetId;
        m_parentId = parentId;
        m_timestamp = timestamp;
        m_text = text;
        m_likes = likes;
        m_replies = replies;
        m_retweets = retweets;
    }

    public List<String> getHashTags() {
        List<String> hashtags = new ArrayList<>();
        String[] words = m_text.split(" ");
        for (String word : words) {
            if (word.startsWith("#") && !word.contains(".")) {
                hashtags.add(word);
            }
        }

        return hashtags;
    }

    public String tweetId() {
        return m_tweetId;
    }

    public String parentId() {
        return m_parentId;
    }

    public long timestamp() {
        return m_timestamp;
    }

    public String text() {
        return m_text;
    }

    public int likes() {
        return m_likes;
    }

    public int replies() {
        return m_replies;
    }

    public int retweets() {
        return m_retweets;
    }
}
