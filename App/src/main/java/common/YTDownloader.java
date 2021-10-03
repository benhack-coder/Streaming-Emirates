package common;

import com.github.kiulian.downloader.OnYoutubeDownloadListener;
import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.YoutubeException;
import com.github.kiulian.downloader.model.YoutubeVideo;
import com.github.kiulian.downloader.model.formats.AudioVideoFormat;
import com.github.kiulian.downloader.model.formats.Format;
import gui.errorhandling.Alerts;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Wrapper class of com.github.sealedtx:java-youtube-downloader:2.4.6
 */

public class YTDownloader {
    /**
     * @param videoReference has to be a YoutubeID or Url
     **/
    @Deprecated
    public void download(String videoReference, String PATH) throws YoutubeException {

        YoutubeDownloader downloader = new YoutubeDownloader();
        initDownloader(downloader);

        YoutubeVideo video = downloader.getVideo(formatVideoReference(videoReference));
        Format videoQuality = getBestQuality(video);

        Thread thread = new Thread(() -> {
            try {
                video.download(videoQuality, new File(PATH), "video");
            } catch (IOException | YoutubeException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void downloadAsync(String videoReference, String PATH, OnYoutubeDownloadListener onYoutubeDownloadListener) throws YoutubeException, IOException {
        try {
            FileManager.deleteAllFilesInDirectory(new File(PATH));
            } catch (Exception ignored) {
        }
        YoutubeDownloader downloader = new YoutubeDownloader();
        initDownloader(downloader);
        YoutubeVideo video = downloader.getVideo(formatVideoReference(videoReference));
        video.downloadAsync(getBestQuality(video), new File(PATH), onYoutubeDownloadListener);
    }

    private String formatVideoReference(String videoReference) {
        if (!isVideoID(videoReference)) {
            return getVideoId(videoReference);
        } else {
            return videoReference;
        }
    }

    private void initDownloader(YoutubeDownloader downloader) {
        downloader.setParserRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        downloader.setParserRetryOnFailure(1);
    }

    private boolean isVideoID(String videoId) {
        return videoId.length() == 11;
    }

    // Does this get video with best quality?
    private Format getBestQuality(YoutubeVideo video) {
        List<AudioVideoFormat> videoFormats = video.videoWithAudioFormats();
        if (videoFormats.size() != 0) {
            return videoFormats.get(0);
        }
        showErrorMessage();
        return null;
    }

    private static String getVideoId(String url) {
        String[] videoId = url.split("v=");
        if (videoId[1].length() != 11) {
            showErrorMessage();

            return null;
        } else {
            return videoId[1];
        }
    }

    private static void showErrorMessage() {
        Alert alert = Alerts.generateSomethingWentWrongMessage();
        alert.showAndWait();
    }
}
