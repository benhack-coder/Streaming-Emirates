import api.YoutubeAPI;
import entity.Video;
import org.junit.jupiter.api.Test;
import request.HTTPRequestException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class   YoutubeAPITest {

    @Test
    void Test() throws IOException, InterruptedException, HTTPRequestException {
        YoutubeAPI youtubeAPI = new YoutubeAPI();
        System.out.println(youtubeAPI.retrieveVideoIDs("test % test"));
        Thread.sleep(1000L);
        System.out.println(youtubeAPI.retrieveVideoIDs("dog"));
        Thread.sleep(1000L); //API Cooldown
        System.out.println(youtubeAPI.retrieveVideoIDs("\\"));
        Thread.sleep(1000L);
        System.out.println(youtubeAPI.retrieveVideoIDs("%"));
        Thread.sleep(1000L);
        System.out.println(youtubeAPI.retrieveVideoIDs("test"));
        Thread.sleep(1000L);
        System.out.println(youtubeAPI.retrieveVideoIDs("test test"));
        Thread.sleep(1000L);
        System.out.println(youtubeAPI.retrieveVideoIDs("test  test"));
        Thread.sleep(1000L);


        List<String> ids = youtubeAPI.retrieveVideoIDs("car");
        if (ids == null) {
            return;
        }

        Map<String, Video> videos = youtubeAPI.generateVideos(ids);
        if (ids.size() == 1) {
            Video video = videos.get(ids.get(0));
            System.out.println(video.getVideoId());
        }
    }
}
