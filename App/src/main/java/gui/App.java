package gui;

import auth.Login;
import config.AppConfig;
import config.AppConfigHelper;
import gui.errorhandling.Alerts;
import gui.navigation.Navigator;
import gui.scenes.SceneType;
import gui.scenes.*;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.Version;
import request.HTTPRequestException;
import auth.Session;
import util.PathUtil;

import java.io.File;
import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        PathUtil.OSPath();
        Navigator navigator = new Navigator(primaryStage);

        checkFolder(PathUtil.getApplicationFolder());
        checkFolder(PathUtil.getConfigFolder());
        checkFolder(PathUtil.getVideoFolder());

        generateConfigFileIfNotPresent();

        if (System.getProperty("os.name").equalsIgnoreCase("WINDOWS 10")){
            if (!(boolean) AppConfigHelper.getConfigValue("gstreamerInstalled")) {
                navigator.registerScene(SceneType.INSTALLATION, new InstallationScene());
                navigator.navigateTo(SceneType.INSTALLATION);
                return;
            }
        }

        navigator.registerScene(SceneType.WELCOME, new WelcomeScene(navigator));
        navigator.registerScene(SceneType.LOGIN, new LoginScene(navigator));
        navigator.registerScene(SceneType.REGISTER, new RegisterScene(navigator));
        navigator.registerScene(SceneType.LOGGED_IN, new LoggedInScene(navigator));
        navigator.registerScene(SceneType.FILE_CHOOSER, new FileChooseScene(navigator));
        navigator.navigateTo(SceneType.WELCOME);
        
        checkOS(navigator);
        try {
            checkLoginState(navigator);
        } catch (HTTPRequestException e) {
            navigator.navigateTo(SceneType.LOGIN);
            Alert alert = Alerts.serversAreCurrentlyDownMessage();
            alert.showAndWait();
        }
    }

    private void generateConfigFileIfNotPresent() throws IOException {
        File file = new File(PathUtil.getConfigFolder() + "/config.json");

        if (!file.exists() && file.createNewFile()){
            AppConfig appConfig = new AppConfig(AppConfig.defaultConfig());
            AppConfigHelper.save(appConfig);
        }
    }

    private void checkOS(Navigator navigator) {
        String operatingSystem = System.getProperty("os.name").toUpperCase();

        switch (operatingSystem) {
            case "MAC OS X" -> {
                System.out.println("GStreamer on Mac OS X not supported yet");
                navigator.registerScene(SceneType.DRONE_STREAM, new DroneStreamMacNotSupportedScene(navigator));
            }
            case "WINDOWS 10" -> {
                Gst.init(Version.BASELINE);
                navigator.registerScene(SceneType.DRONE_STREAM, new DroneStreamScene(navigator));
            }
        }
    }

    private void checkFolder(String dir) {
        File folder = new File(dir);
        folder.mkdir();
    }

    private void checkLoginState(Navigator navigator) throws HTTPRequestException {
        boolean loginState = (boolean) AppConfigHelper.getConfigValue("loggedin");

        if (loginState) {
            String password = (String) AppConfigHelper.getConfigValue("password");
            String username = (String) AppConfigHelper.getConfigValue("username");

            Login login = new Login(password, username);

            int result = login.getRequestResult(password);

            if (result != 0) {
                Session.sessionId = result;
                navigator.navigateTo(SceneType.LOGGED_IN);
            }
        } else {
            navigator.navigateTo(SceneType.WELCOME);
        }
    }
}
