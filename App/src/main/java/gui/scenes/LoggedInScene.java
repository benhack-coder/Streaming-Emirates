package gui.scenes;

import com.google.gson.JsonObject;
import config.AppConfigHelper;
import entity.Video;
import gui.design.WidgetBuilder;
import gui.errorhandling.Alerts;
import gui.navigation.Navigator;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import request.HTTPRequestException;
import request.IRequest;
import request.RequestType;
import request.RequestUtil;
import auth.Session;
import util.JsonParser;

import java.io.IOException;
import java.util.Map;

public class LoggedInScene extends Scene {
    private static final BorderPane root = WidgetBuilder.buildBorderPane();
    private final Navigator navigator;

    public LoggedInScene(Navigator navigator) {
        super(root);
        this.navigator = navigator;
        setup();
    }

    private void setup() {
        Button youTube = WidgetBuilder.buildButton("YouTube", 100);
        Button stream = WidgetBuilder.buildButton("Stream", 100);
        Button fileChoose = WidgetBuilder.buildButton("Play Video", 100);
        Button logout = WidgetBuilder.buildButton("Log out", 100);

        HBox hBox = WidgetBuilder.buildHBox(40, Pos.CENTER);
        hBox.getChildren().add(youTube);
        hBox.getChildren().add(stream);
        hBox.getChildren().add(fileChoose);
        hBox.getChildren().add(logout);

        VBox vBox = WidgetBuilder.buildVBox(60, Pos.CENTER);
        vBox.getChildren().addAll(hBox, logout);

        root.setCenter(vBox);

        youTube.setOnAction(e -> {
            if (!navigator.getSceneMap().containsKey(SceneType.YOUTUBE)){
                navigator.registerScene(SceneType.YOUTUBE, new YouTubeScene((String) AppConfigHelper.getConfigValue("username"), navigator));
            }
            Thread thread = new Thread(() -> {
                try {
                    handleRequest();
                } catch (IOException | InterruptedException ioException) {
                    System.out.println("Could not load history");
                }
            });
            thread.start();
            navigator.navigateTo(SceneType.YOUTUBE);
        });
        stream.setOnAction(e -> navigator.navigateTo(SceneType.DRONE_STREAM));
        fileChoose.setOnAction(e -> navigator.navigateTo(SceneType.FILE_CHOOSER));
        logout.setOnAction(e -> logOut());
    }

    private void handleRequest() throws IOException, InterruptedException {
        String json = "{ \"sessionId\": \"" + Session.sessionId + "\" }";
        IRequest request = new RequestUtil("http://anonymouslore.tk:8081/gethistory", json, RequestType.POST);
        String response = null;
        try {
            response = request.makeRequest();
        } catch (HTTPRequestException httpRequestException) {
            Alert alert = Alerts.generateNoInternetConnectionMessage();
            alert.showAndWait();
        }
        JsonParser parser = new JsonParser();
        if (response.equals("No videos watched yet")) {
            try {
                navigator.registerScene(SceneType.HISTORY, new HistoryScene(navigator, null));
            } catch (Exception ignored) {
            }
        } else {
            JsonObject object = convertToJsonObject(response);
            Map<String, Video> videos = parser.parseHistory(object);
            try {
                navigator.registerScene(SceneType.HISTORY, new HistoryScene(navigator, videos));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private JsonObject convertToJsonObject(String json) {
        com.google.gson.JsonParser googleParser = new com.google.gson.JsonParser();
        return (JsonObject) googleParser.parse(json);
    }

    private void logOut() {
        AppConfigHelper.overwriteProperty("loggedin", false);
        Platform.exit();
    }
}
