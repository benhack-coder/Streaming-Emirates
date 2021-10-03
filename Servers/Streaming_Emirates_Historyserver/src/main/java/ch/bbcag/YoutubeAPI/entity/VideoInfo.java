package ch.bbcag.YoutubeAPI.entity;

import java.net.URL;

public class VideoInfo {

    private String videoId;
    private URL thumbnailUrl;
    private String videoTitle;


    public VideoInfo(String videoId, URL thumbnailUrl, String videoTitle) {
        this.videoId = videoId;
        this.thumbnailUrl = thumbnailUrl;
        this.videoTitle = videoTitle;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public URL getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(URL thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }
}
