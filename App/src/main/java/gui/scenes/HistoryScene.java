package gui.scenes;

import entity.Video;
import gui.design.WidgetBuilder;
import gui.navigation.Navigator;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Map;

public class HistoryScene extends Scene {
    private static final BorderPane root = WidgetBuilder.buildBorderPane();
    private final ScrollPane scrollPane = new ScrollPane();
    private final Navigator navigator;
    private final Map<String, Video> videos;

    public HistoryScene(Navigator navigator, Map<String, Video> videos) {
        super(root);
        this.navigator = navigator;
        this.videos = videos;
        setup();
        YouTubeScene.isCreated = true;
    }

    public void setup() {
        Button goBack = WidgetBuilder.buildButton("Go back", 100);

        TilePane history = WidgetBuilder.buildTilePane(2, 20, 20, Pos.CENTER);

        Text error = WidgetBuilder.buildText("No Videos watched yet", Font.font("Verdana", 25));

        if (videos != null) {
            showVideoInfo(videos, history);
        } else {
            history.getChildren().add(error);
        }

        VBox vBox = WidgetBuilder.buildVBox(20, Pos.CENTER);
        vBox.getChildren().addAll(goBack, history);

        scrollPane.setContent(vBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        root.setCenter(scrollPane);

        goBack.setOnAction(e -> Navigator.goBack(SceneType.YOUTUBE, navigator));
    }

    private void showVideoInfo(Map<String, Video> videos, TilePane history) {
        for (Map.Entry<String, Video> video : videos.entrySet()) {
            URL url = video.getValue().getThumbnail();
            Image image = new Image(String.valueOf(url));
            ImageView imageView = new ImageView(image);

            Button vB = WidgetBuilder.buildButton("", 100);
            vB.setGraphic(imageView);

            VBox vBox = WidgetBuilder.buildVBox(20, Pos.CENTER);
            vBox.getChildren().addAll(vB, new Text(video.getValue().getTitle()));

            history.getChildren().addAll(vBox);
        }
    }
}
