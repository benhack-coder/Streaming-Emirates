package gui.scenes;

import api.YoutubeAPI;
import com.github.kiulian.downloader.OnYoutubeDownloadListener;
import com.github.kiulian.downloader.YoutubeException;
import common.YTDownloader;
import entity.Video;
import gui.design.WidgetBuilder;
import gui.errorhandling.Alerts;
import gui.navigation.Navigator;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import request.HTTPRequestException;
import request.IRequest;
import request.RequestType;
import request.RequestUtil;
import util.PathUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class YouTubeScene extends Scene {
    private static final BorderPane root = WidgetBuilder.buildBorderPane();
    private final ScrollPane scrollPane = new ScrollPane();
    private final String username;
    private final Navigator navigator;
    private final Media music = new Media(getClass().getResource("/audio/elevmusic.mp3").toString());
    private final MediaPlayer mp = new MediaPlayer(music);
    private final ProgressBar videoLoading = new ProgressBar();
    private final VBox vBox = WidgetBuilder.buildVBox(50, Pos.CENTER);
    public static boolean isCreated = false;

    public YouTubeScene(String username, Navigator navigator) {
        super(root);
        this.username = username;
        this.navigator = navigator;
        setup();
    }

    private void setup() {
        Text text = WidgetBuilder.buildText("Welcome " + username, new Font("Verdana", 50));

        TextField input = WidgetBuilder.buildTextField(null, "Search a Video", 400);

        Button button = WidgetBuilder.buildButton("Search", 100);
        Button goBack = WidgetBuilder.buildButton("Go Back", 100);
        Button history = WidgetBuilder.buildButton("History", 100);


        ProgressBar historyProgress = new ProgressBar();
        TilePane tilePane = WidgetBuilder.buildTilePane(1, 20, 20, Pos.CENTER);

        HBox hBox = WidgetBuilder.buildHBox(50, Pos.TOP_CENTER);
        hBox.getChildren().addAll(goBack, history);

        vBox.getChildren().addAll(text, input, button, hBox);
        vBox.getChildren().add(tilePane);

        scrollPane.setContent(vBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        root.setCenter(scrollPane);

        button.setOnAction(e -> {
            mp.stop();
            try {
                YoutubeAPI vr = new YoutubeAPI();
                List<String> videoIds = vr.retrieveVideoIDs(input.getText());
                if (videoIds != null) {
                    Map<String, Video> videos = loadVideos(videoIds);
                    addTilePane(videos, tilePane);
                } else {
                    somethingWentWrong();
                }
                input.clear();
            } catch (IOException | HTTPRequestException ioException) {
                somethingWentWrong();
            }
        });
        history.setOnAction(e -> {
            try {
                hBox.getChildren().add(historyProgress);
            } catch (Exception ignored) {
            }
            if (isCreated) {
                navigator.navigateTo(SceneType.HISTORY);
                hBox.getChildren().remove(historyProgress);
            }
        });
        goBack.setOnAction(e -> Navigator.goBack(SceneType.LOGGED_IN, navigator));
    }

    private Map<String, Video> loadVideos(List<String> videoIds) throws IOException, HTTPRequestException {
        YoutubeAPI vr = new YoutubeAPI();
        return vr.generateVideos(videoIds);
    }

    private void addTilePane(Map<String, Video> videos, TilePane tilePane) throws IOException {
        playElevMusic();
        tilePane.getChildren().clear();

        for (Map.Entry<String, Video> video : videos.entrySet()) {
            URL url = video.getValue().getThumbnail();
            Image image = new Image(String.valueOf(url));
            ImageView imageView = new ImageView(image);
            String title = video.getValue().getTitle();

            Button button = WidgetBuilder.buildButton(null, 500);
            button.setOnAction(e -> {
                try {
                    downloadVideo(video.getValue().getVideoId());
                    String json = "{ \"username\": \"" + this.username + "\", \"videoId\": \"" + video.getValue().getVideoId() + "\"}";
                    IRequest request = new RequestUtil("http://anonymouslore.tk:8081/watched", json, RequestType.POST);
                    request.makeRequest();
                } catch (IOException | YoutubeException | InterruptedException | HTTPRequestException ioException) {
                    ioException.printStackTrace();
                    somethingWentWrong();
                }
            });
            button.setGraphic(imageView);

            VBox vBox2 = new VBox();
            vBox2.getChildren().addAll(button, new Text(title));
            vBox2.setAlignment(Pos.CENTER);

            tilePane.getChildren().addAll(vBox2);
        }
    }

    private void somethingWentWrong() {
        Alert alert = Alerts.generateSomethingWentWrongMessage();
        alert.showAndWait();
    }

    private void downloadVideo(String videoID) throws IOException, YoutubeException, InterruptedException {
        YTDownloader downloader = new YTDownloader();
        Alert download = WidgetBuilder.buildAlert(Alert.AlertType.INFORMATION, null, null, null);
        download.getDialogPane().setContent(videoLoading);
        download.initModality(Modality.APPLICATION_MODAL);
        download.initOwner(navigator.getStage());
        download.show();

        downloader.downloadAsync(videoID, PathUtil.getVideoFolder(), new OnYoutubeDownloadListener() {
            @Override
            public void onDownloading(int progress) {
                updateProgressBar(progress);
            }
            @Override
            public void onFinished(File file) {
                Platform.runLater(() -> {
                    download.hide();
                    Media video = new Media(file.toURI().toString());
                    navigator.registerScene(SceneType.VIDEO_PLAY, new VideoPlayScene(video));
                    navigator.navigateTo(SceneType.VIDEO_PLAY);
                });
            }
            @Override
            public void onError(Throwable throwable) {

            }
        });
        mp.stop();
    }

    private void updateProgressBar(double progress) {
        Platform.runLater(() -> videoLoading.setProgress(progress / 100));
    }

    private void playElevMusic() {
        mp.setVolume(0.07);
        mp.play();
    }
}

