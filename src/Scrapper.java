import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Date;

public class Scrapper {
    private String getUrlFromQuery(String query) {
        return "https://twitter.com/search?q=" + query;
    }

    private void scrape(String query) throws IOException {
        String url = getUrlFromQuery(query);
        Document doc = Jsoup.connect(url).get();
        Elements tweets = doc.select("li[data-item-type='tweet']");
        for (Element tweet : tweets) {
            Element metaData = tweet.selectFirst("div");
            String userId = metaData.attr("data-user-id");
            String userName = metaData.attr("data-name");
            String tweetId = metaData.attr("tweet-id");

            long timeStamp = Long.parseLong(metaData.selectFirst("._timestamp").attr("data-time-ms"));

            Element content = metaData.selectFirst("p.tweet-text");
            String tweetText = content.text();

            Element likeData = metaData.selectFirst(".ProfileTweet-action--favorite > .ProfileTweet-actionCount");
            int likes = Integer.parseInt(likeData.attr("data-tweet-stat-count"));

            Element replyData = metaData.selectFirst(".ProfileTweet-action--reply > .ProfileTweet-actionCount");
            int replies = Integer.parseInt(replyData.attr("data-tweet-stat-count"));

            Element retweetData = metaData.selectFirst(".ProfileTweet-action--reply > .ProfileTweet-actionCount");
            int retweets = Integer.parseInt(retweetData.attr("data-tweet-stat-count"));

            System.out.println(userName);
            System.out.println(new Date(timeStamp).toString());
            System.out.println(tweetText);
            System.out.println(likes);
            System.out.println(replies);
            System.out.println(retweets);
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {
        Scrapper scrapper = new Scrapper();
        scrapper.scrape("india");
    }
}
