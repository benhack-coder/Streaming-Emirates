package gui.navigation;

import gui.scenes.SceneType;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class Navigator {
    private final Stage stage;

    private final Map<SceneType, Scene> sceneMap = new HashMap<>();

    public Navigator(Stage stage) {
        this.stage = stage;
    }

    public void registerScene(SceneType sceneType, Scene scene) {
        sceneMap.put(sceneType, scene);
    }

    public void navigateTo(SceneType sceneType) {
        if (sceneType == SceneType.LOGIN || sceneType == SceneType.REGISTER || sceneType == SceneType.WELCOME || sceneType == SceneType.LOGGED_IN || sceneType == SceneType.FILE_CHOOSER) {
            stage.setWidth(400);
            stage.setHeight(400);
            stage.setResizable(false);
        } else {
            stage.setWidth(1200);
            stage.setHeight(800);
        }

        stage.setTitle("Streaming Emirates");
        stage.setScene(sceneMap.get(sceneType));
        stage.show();
    }

    public Map<SceneType, Scene> getSceneMap() {
        return sceneMap;
    }

    public Stage getStage() {
        return stage;
    }

    public static void goBack(SceneType sceneType, Navigator navigator) {
        navigator.navigateTo(sceneType);
    }
}
