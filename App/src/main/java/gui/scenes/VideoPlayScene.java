package gui.scenes;

import gui.design.VideoPlayerButton;
import gui.design.WidgetBuilder;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class VideoPlayScene extends Scene {
    private static final BorderPane root = WidgetBuilder.buildBorderPane();
    private boolean isPlaying = false;
    private final Media media;

    public VideoPlayScene(Media media) {
        super(root);
        this.media = media;
        load();
    }

    private void load() {
        //Instantiating MediaPlayer class
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        //Instantiating MediaView class
        MediaView mediaView = new MediaView(mediaPlayer);
        DoubleProperty mvw = mediaView.fitWidthProperty();
        DoubleProperty mvh = mediaView.fitHeightProperty();
        mvw.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
        mvh.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
        mediaView.setPreserveRatio(true);

        //by setting this property to true, the Video will be played
        mediaPlayer.setAutoPlay(false);

        //Sliders
        Slider volume = new Slider();
        volume.setOrientation(Orientation.HORIZONTAL);
        volume.setValue(mediaPlayer.getVolume() * 100);
        volume.valueProperty().addListener(observable -> mediaPlayer.setVolume(volume.getValue() / 100));

        Slider video = new Slider();
        video.setPrefWidth(mediaPlayer.getMedia().getWidth());
        video.setPrefHeight(10);
        video.setMin(0);

        //Video Time
        Text time = new Text("0 / " + time(mediaPlayer.getTotalDuration().toSeconds()));

        //Buttons
        VideoPlayerButton play = new VideoPlayerButton("Play");
        play.setOnAction(e -> {
            if (!isPlaying) {
                play(mediaPlayer, play);
                video.setMax(mediaPlayer.getTotalDuration().toSeconds());
            } else {
                pause(mediaPlayer, play);
            }
        });

        VideoPlayerButton fast = new VideoPlayerButton("Fast");
        fast.setOnAction(e -> fast(mediaPlayer));

        VideoPlayerButton slow = new VideoPlayerButton("Slow");
        slow.setOnAction(e -> slow(mediaPlayer));

        VideoPlayerButton normal = new VideoPlayerButton("Normal");
        normal.setOnAction(e -> normal(mediaPlayer));

        VideoPlayerButton reload = new VideoPlayerButton("Reload");
        reload.setOnAction(e -> reload(mediaPlayer));

        //Boxes
        HBox hBox = WidgetBuilder.buildHBox(30, Pos.CENTER);
        hBox.getChildren().addAll(play, fast, slow, normal, reload, volume, time);

        VBox vBox = WidgetBuilder.buildVBox(0, Pos.CENTER);
        vBox.getChildren().addAll(video, hBox);

        root.setCenter(mediaView);
        root.setBottom(vBox);
        root.setPadding(new Insets(0, 0, 5, 0));

        //Video Slider
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            video.setValue(newValue.toSeconds());
            time.setText(time(newValue.toSeconds()) + " / " + time(mediaPlayer.getTotalDuration().toSeconds()));
        });

        video.setOnDragDetected(event -> pause(mediaPlayer, play));
        video.setOnMouseReleased(e -> {
            mediaPlayer.seek(Duration.seconds(video.getValue()));
            play(mediaPlayer, play);
        });
    }

    private void play(MediaPlayer mp, Button playButton) {
        mp.play();
        isPlaying = true;
        playButton.setText("Pause");
    }

    private void pause(MediaPlayer mp, Button playButton) {
        mp.pause();
        isPlaying = false;
        playButton.setText("Play");
    }

    private void fast(MediaPlayer mp) {
        mp.setRate(2);
    }

    private void slow(MediaPlayer mp) {
        mp.setRate(0.5);
    }

    private void normal(MediaPlayer mp) {
        mp.setRate(1);
    }

    private void reload(MediaPlayer mp) {
        mp.seek(mp.getStartTime());
        mp.play();
    }

    private String time(Double dSeconds) {
        int seconds = (int) Math.round(dSeconds);

        int tSeconds = seconds % 60;
        int tHours = seconds / 60;
        int tMinutes = tHours % 60;
        tHours /= 60;

        String secondsString;

        if (tSeconds < 10) {
            secondsString = "0" + tSeconds;
        } else {
            secondsString = "" + tSeconds;
        }


        if (tHours == 0) {
            return tMinutes + ":" + secondsString;
        } else {
            return tHours + ":" + tMinutes + ":" + secondsString;
        }
    }
}