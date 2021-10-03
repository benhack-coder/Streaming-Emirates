package api;

import request.HTTPRequestException;
import util.JsonParser;
import common.UserInputChecker;
import entity.Video;
import org.json.JSONObject;
import request.IRequest;
import request.RequestType;
import request.RequestUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class makes the Http Requests to the Youtube Data API from Google
 * It returns the video-ids as a List
 * It also returns the infos to the videos as a Hashmap with the video-id as key
 */

public class YoutubeAPI {
    private final String BASE_URL1 = "https://youtube.googleapis.com/youtube/v3/search?q=";
    private final String BASE_URL2 = "https://youtube.googleapis.com/youtube/v3/videos?id=";
    private final String API_Key = "AIzaSyALA2gpxbYWcRuepv_1DxJfiwN-OYFkkso";
    private final String API_KEY2 = "AIzaSyBZFbME6DxzjfbGbJfdZMvkDRxrmaf28BQ";
    private final String urlEnding = "&part=snippet,statistics";


    public List<String> retrieveVideoIDs(String search) throws HTTPRequestException {
        search = UserInputChecker.userInputAsUrl(search);
        String url = BASE_URL1 + search + "&key=" + API_KEY2;
        IRequest request = new RequestUtil(url, "", RequestType.GET);
        String response = request.makeRequest();
        if (response == null){
            return null;
        } else {
            JSONObject object = new JSONObject(response);
            JsonParser parser = new JsonParser();
            return parser.retrieveVideoId(object);
        }
    }

    public Map<String, Video> generateVideos(List<String> videoIds) throws HTTPRequestException, MalformedURLException {
        if (videoIds == null) {
            return null;
        } else {
            Map <String, Video> videos = new HashMap<>();
            for (String videoId : videoIds) {
                String url = BASE_URL2 + videoId + "&key=" + API_KEY2 + urlEnding;

                IRequest request = new RequestUtil(url, "", RequestType.GET);
                String response = request.makeRequest();

                JSONObject object = new JSONObject(response);
                JsonParser parser = new JsonParser();

                String title = parser.getTitle(object);
                URL thumbnailURL = parser.getThumbNailURL(object);

                Video video = new Video(thumbnailURL, title, videoId);
                videos.put(videoId, video);
            }
            return videos;
        }
    }
}
