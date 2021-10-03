package ch.bbcag.YoutubeAPI.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class JsonParser {

    public String getTitle(JSONObject json) throws JSONException {
        JSONArray array = json.getJSONArray("items");
        JSONObject infos = array.getJSONObject(0);
        JSONObject snippet = infos.getJSONObject("snippet");
        return snippet.getString("title");
    }

    public URL getThumbNailURL(JSONObject json) throws MalformedURLException, JSONException {
        JSONArray array = json.getJSONArray("items");
        JSONObject infos = array.getJSONObject(0);
        JSONObject snippet = infos.getJSONObject("snippet");
        JSONObject thumbnails = snippet.getJSONObject("thumbnails");
        JSONObject highQuality = thumbnails.getJSONObject("high");
        String url = highQuality.getString("url");
        return new URL(url);
    }
}
