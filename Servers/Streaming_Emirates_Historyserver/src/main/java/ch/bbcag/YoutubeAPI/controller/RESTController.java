package ch.bbcag.YoutubeAPI.controller;

import ch.bbcag.YoutubeAPI.common.JsonParser;
import ch.bbcag.YoutubeAPI.constants.Constants;
import ch.bbcag.YoutubeAPI.dataaccess.DAO;
import ch.bbcag.YoutubeAPI.entity.VideoInfo;
import ch.bbcag.YoutubeAPI.entity.WatchedVideo;
import ch.bbcag.YoutubeAPI.repository.Ids;
import ch.bbcag.YoutubeAPI.repository.IdsRepository;
import ch.bbcag.YoutubeAPI.request.IRequest;
import ch.bbcag.YoutubeAPI.request.RequestType;
import ch.bbcag.YoutubeAPI.request.RequestUtil;
import ch.bbcag.YoutubeAPI.session.Session;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RESTController {

    private final DAO dao;
    private Map <Integer, JsonObject> sessions = new HashMap<>();

    public RESTController(DAO dao) {
        this.dao = dao;
    }

    @PostMapping("/gethistory")
    public String returnObject(@RequestBody Session session) {
        if (sessions.get(session.getSessionId()) != null) {
            JsonObject object =  sessions.get(session.getSessionId());
            sessions.remove(session.getSessionId());
            return object.toString();
        } else {
            return "No videos watched yet";
        }
    }

    @PostMapping("/history")
    public void returnVideos(@RequestBody WatchedVideo video) throws IOException, JSONException {
        int sessionId = video.getSessionId();
        List<IdsRepository> ids = dao.getVideoIds(video);
        if (ids.size() > 0) {
            IdsRepository id = ids.get(0);
            Map<String, VideoInfo> videoInfos = makeRequest(id);
            JsonObject object = convertToJson(videoInfos);
            storeSessionObject(sessionId, object);
        }else {
            storeSessionObject(sessionId, null);
        }
    }

    @PostMapping("/watched")
    public int dataInserter(@RequestBody WatchedVideo video) {
        if (dao.insertData(video) == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    private Map<String, VideoInfo> makeRequest(IdsRepository repository) throws IOException, JSONException {
        List <Ids> ids = repository.videoIds;
        Map<String, VideoInfo> videos = new HashMap<>();
        for (Ids id : ids) {
            String url = Constants.BASE_URL + id.getId() + "&key=" + Constants.API_Key + Constants.urlEnding;
            IRequest request = new RequestUtil(url, "", RequestType.GET);
            String response = request.makeRequest();
            JSONObject object = new JSONObject(response);
            JsonParser parser = new JsonParser();
            String title = parser.getTitle(object);
            URL thumbnailURL = parser.getThumbNailURL(object);
            VideoInfo video = new VideoInfo(id.getId(), thumbnailURL, title);
            videos.put(id.getId(), video);
        }
        return videos;
    }

    private JsonObject convertToJson(Map <String, VideoInfo> videos) {
        JsonObject object = new JsonObject();
        JsonArray array = new JsonArray();
        for (Map.Entry<String, VideoInfo> video : videos.entrySet()) {
            JsonObject subObject = new JsonObject();
            subObject.addProperty("video_id", video.getValue().getVideoId());
            subObject.addProperty("title", video.getValue().getVideoTitle());
            subObject.addProperty("thumbnail", video.getValue().getThumbnailUrl().toString());
            array.add(subObject);
            object.add("video_ids", array);
        }
        return object;
    }

    private void storeSessionObject (int sessionId, JsonObject object) {
        this.sessions.put(sessionId, object);
    }
}
