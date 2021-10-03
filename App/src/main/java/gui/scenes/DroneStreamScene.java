package gui.scenes;

import gstreamer.CameraCapture;
import gstreamer.Stream;
import gui.design.WidgetBuilder;
import gui.navigation.Navigator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.freedesktop.gstreamer.fx.FXImageSink;

public class DroneStreamScene extends Scene {
    private static final BorderPane root = WidgetBuilder.buildBorderPane();
    private boolean isCamStarted = false;
    private final Navigator navigator;
    private final Stream stream = new Stream();
    private boolean isStreamStarted = false;
    private Thread thread;

    public DroneStreamScene(Navigator navigator) {
        super(root);
        this.navigator = navigator;
        setup();
    }

    private void setup() {
        Button startCam = WidgetBuilder.buildButton("Start Cam", 100);
        startCam.setPrefHeight(50);

        TextField portEntry = WidgetBuilder.buildTextField("Port", "Enter Port", 200);
        portEntry.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                portEntry.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        Button startStream = WidgetBuilder.buildButton("Start Stream", 100);
        startStream.setOnAction(e -> {
            if (!isStreamStarted) {
                if (!portEntry.getText().isEmpty()){
                    thread = new Thread(()-> stream.play(Integer.parseInt(portEntry.getText())));
                    thread.start();
                    isStreamStarted = true;
                    startStream.setText("Pause");
                }
            } else {
                isStreamStarted = false;
                startStream.setText("Start");
                stream.stop();
                thread.interrupt();
            }
        });

        Button goBack = WidgetBuilder.buildButton("Go Back", 100);
        goBack.setOnAction(e -> navigator.navigateTo(SceneType.LOGGED_IN));

        CameraCapture cameraCapture = new CameraCapture(new FXImageSink(), 640, 480);
        BorderPane pane = new BorderPane();

        ImageView view = new ImageView();
        view.imageProperty().bind(cameraCapture.getFxImageSink().imageProperty());
        view.fitWidthProperty().bind(pane.widthProperty());
        view.fitHeightProperty().bind(pane.heightProperty());
        view.setPreserveRatio(true);

        VBox vBox = WidgetBuilder.buildVBox(20, Pos.CENTER);
        vBox.getChildren().addAll(pane, startCam, portEntry, startStream, goBack, view);

        root.setBottom(vBox);
        root.setPadding(new Insets(0, 0, 100, 0));

        startCam.setOnAction(e -> {
            if (!isCamStarted) {
                isCamStarted = true;
                startCam.setText("Pause");
                cameraCapture.getPipeline().play();
            } else {
                isCamStarted = false;
                startCam.setText("Start");
                cameraCapture.getPipeline().stop();

            }
        });
    }
}
