package xyz.gnarbot.gnar.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Thanks LavaPlayer <3.
 */
public class YouTube {
    private static final HttpClientBuilder httpClientBuilder =
            HttpClientBuilder.create().setDefaultCookieStore(new BasicCookieStore());

    public static List<Result> search(String query, int maxResults) {
        try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
            URI url = new URIBuilder("https://www.youtube.com/results").addParameter("search_query", query).build();

            try (CloseableHttpResponse response = httpClient.execute(new HttpGet(url))) {
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new IOException("Invalid status code for search response.");
                }

                Document document = Jsoup.parse(response.getEntity().getContent(), "UTF-8", "");
                return extractSearchResults(document, maxResults);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Result> extractSearchResults(Document document, int maxResults) {
        List<Result> tracks = new ArrayList<>();

        int count = 0;

        for (Element results : document.select("#page > #content #results")) {
            for (Element result : results.select(".yt-lockup-video")) {
                if (!result.hasAttr("data-ad-impressions") && result.select(".standalone-ypc-badge-renderer-label")
                        .isEmpty()) {
                    Result res = extractTrackFromResultEntry(result);
                    if (res != null) {
                        tracks.add(res);
                        count++;

                        if (count >= maxResults) break;
                    }
                }
            }
        }

        return tracks;
    }

    private static Result extractTrackFromResultEntry(Element element) {
        Element contentElement = element.select(".yt-lockup-content").first();
        Element thumbElement = element.select(".yt-lockup-thumbnail").first();
        Element durationElement = element.select(".video-time").first();
        String videoId = element.attr("data-context-item-id");

        if (durationElement == null || contentElement == null || videoId.isEmpty()) {
            return null;
        }

        String title = contentElement.select(".yt-lockup-title > a").text();
        String author = contentElement.select(".yt-lockup-byline > a").text();
        String description = contentElement.select(".yt-lockup-description").text();
        String thumbUrl = thumbElement.select(".yt-thumb-simple > img").attr("src");
        long duration = lengthTextToSeconds(durationElement.text()) * 1000L;

        return new Result(title, author, videoId, thumbUrl, description, duration);
    }

    private static int lengthTextToSeconds(String durationText) {
        int length = 0;

        for (String part : Utils.stringSplit(durationText, ':')) {
            length = length * 60 + Integer.valueOf(part);
        }

        return length;
    }

    public static class Result {
        private final String title;
        private final String author;
        private final String id;
        private final String thumbUrl;
        private final String description;
        private final long duration;

        public Result(String title, String author, String id, long duration) {
            this(title, author, id, null, null, duration);
        }

        public Result(String title, String author, String id, String thumbUrl, String description, long duration) {
            this.title = title;
            this.author = author;
            this.id = id;
            this.thumbUrl = thumbUrl;
            this.description = description;
            this.duration = duration;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public String getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public String getThumbUrl() {
            return thumbUrl;
        }

        public long getDuration() {
            return duration;
        }

        @Override
        public String toString() {
            return "Result(title=" + title + ", author=" + author + ", id=" + id + ", thumbUrl=" + thumbUrl + ", duration=" + duration + ")";
        }
    }
}
