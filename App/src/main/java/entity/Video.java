package entity;

import java.net.URL;

public class Video {
    private final URL thumbnail;
    private final String title;
    private final String videoId;

    public Video(URL thumbnail, String title, String videoId) {
        this.thumbnail = thumbnail;
        this.title = title;
        this.videoId = videoId;
    }

    public URL getThumbnail() {
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getVideoId() {
        return videoId;
    }
}
