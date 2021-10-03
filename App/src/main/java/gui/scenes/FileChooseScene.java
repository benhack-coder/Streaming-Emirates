package gui.scenes;

import gui.design.WidgetBuilder;
import gui.navigation.Navigator;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;
import javafx.stage.Modality;

import java.io.File;

public class FileChooseScene extends Scene {
    private static final BorderPane root = WidgetBuilder.buildBorderPane();
    private final VBox vBox = new VBox();
    private final Navigator navigator;

    public FileChooseScene(Navigator navigator) {
        super(root);
        this.navigator = navigator;
        setup();
    }

    private void setup() {
        Button fileButton = WidgetBuilder.buildButton("Choose File", 100);
        fileButton.setOnAction(e -> chooseFile());

        Button goBackBtn = WidgetBuilder.buildButton("Go Back", 100);
        goBackBtn.setOnAction(e -> Navigator.goBack(SceneType.LOGGED_IN, navigator));

        vBox.getChildren().addAll(fileButton, goBackBtn);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(50);

        root.setCenter(vBox);
    }

    private void chooseFile() {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(navigator.getStage());
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video Files", "*.mp4"));
        if (file != null) {
            openFile(file);
        }
    }

    private void openFile(File file) {
        try {
            Media media = new Media(file.toURI().toString());
            navigator.registerScene(SceneType.VIDEO_PLAY, new VideoPlayScene(media));
            navigator.navigateTo(SceneType.VIDEO_PLAY);
        } catch (Exception e) {
            Alert alert = WidgetBuilder.buildAlert(Alert.AlertType.ERROR, "Error", null, "Unsupported media type");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(navigator.getStage());
            alert.showAndWait();
        }
    }
}
