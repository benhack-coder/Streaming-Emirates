package util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import entity.Video;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class has methods, which take in a Jsonobject
 * It reads the needed data out of the Json and returns it
 * The method parseHistory() reads the information and puts it instantly into a Hashmap, then returns this Hashmap
 */

public class JsonParser {

    public List<String> retrieveVideoId(JSONObject json) {
        List <String> videoIds = new ArrayList<>();
        JSONArray array = json.getJSONArray("items");
        int length = array.length();

        for (int i = 0; i < length; i++) {
            JSONObject video = array.getJSONObject(i);
            JSONObject id = video.getJSONObject("id");
            try {
                String videoId = id.getString("videoId");
                videoIds.add(videoId);
            } catch (Exception ignored) {
            }
        }
        return videoIds;
    }

    public String getTitle(JSONObject json) {
        JSONArray array = json.getJSONArray("items");
        JSONObject infos = array.getJSONObject(0);
        JSONObject snippet = infos.getJSONObject("snippet");
        return snippet.getString("title");
    }

    public URL getThumbNailURL(JSONObject json) throws MalformedURLException {
        JSONArray array = json.getJSONArray("items");
        JSONObject infos = array.getJSONObject(0);
        JSONObject snippet = infos.getJSONObject("snippet");
        JSONObject thumbnails = snippet.getJSONObject("thumbnails");
        JSONObject highQuality = thumbnails.getJSONObject("high");
        String url = highQuality.getString("url");
        return new URL(url);
    }

    public Map<String, Video> parseHistory (JsonObject json) throws MalformedURLException {
        Map<String, Video> videos = new HashMap<>();
        JsonArray array = json.getAsJsonArray("video_ids");
        for (JsonElement iterator : array ) {
            JsonObject object = iterator.getAsJsonObject();
            String videoId = object.get("video_id").getAsString();
            String title = object.get("title").getAsString();
            String thumbNail = object.get("thumbnail").getAsString();
            URL thumbNailURL = new URL(thumbNail);
            Video video = new Video(thumbNailURL, title, videoId);
            videos.put(videoId, video);
        }
        return videos;
    }
}
