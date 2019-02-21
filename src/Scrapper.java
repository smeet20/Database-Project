import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class Scrapper {
    private String getUrlFromQuery(String query) {
        return "https://twitter.com/search?q=" + query;
    }

    public void scrap(String query) throws IOException, SQLException {
        String url = getUrlFromQuery(query);
        Document doc = Jsoup.connect(url).get();
        Elements tweets = doc.select("li[data-item-type='tweet']");
        for (Element rawTweet : tweets) {
            Element metaData = rawTweet.selectFirst("div");
            String userId = metaData.attr("data-user-id");
            String userName = metaData.attr("data-name");
            String tweetId = metaData.attr("data-tweet-id");
            String parentId = metaData.attr("data-conversation-id");

            long timeStamp = Long.parseLong(metaData.selectFirst("._timestamp").attr("data-time-ms"));

            Element content = metaData.selectFirst("p.tweet-text");
            String tweetText = content.text();

            Element likeData = metaData.selectFirst(".ProfileTweet-action--favorite > .ProfileTweet-actionCount");
            int likes = Integer.parseInt(likeData.attr("data-tweet-stat-count"));

            Element replyData = metaData.selectFirst(".ProfileTweet-action--reply > .ProfileTweet-actionCount");
            int replies = Integer.parseInt(replyData.attr("data-tweet-stat-count"));

            Element retweetData = metaData.selectFirst(".ProfileTweet-action--reply > .ProfileTweet-actionCount");
            int retweets = Integer.parseInt(retweetData.attr("data-tweet-stat-count"));

            Author author = new Author(userId, userName);
            Tweet tweet = new Tweet(tweetId, parentId, timeStamp, tweetText, likes, replies, retweets);

            System.out.println(tweet.text().length() + " " + tweet.text());
            try {
                AppManager.databaseManager().insertTweet(tweet);
            } catch (Exception ignored) {

            }
        }
    }
}
