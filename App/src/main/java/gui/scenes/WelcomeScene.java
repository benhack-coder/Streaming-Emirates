package gui.scenes;

import config.AppConfigHelper;
import gui.design.WidgetBuilder;
import gui.navigation.Navigator;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class WelcomeScene extends Scene {
    private static final BorderPane root = WidgetBuilder.buildBorderPane();
    private final Navigator navigator;

    public WelcomeScene(Navigator navigator) {
        super(root);
        this.navigator = navigator;
        setup();
    }

    private void setup() {
        Button register = WidgetBuilder.buildButton("Register", 100);
        Button login = WidgetBuilder.buildButton("Login", 100);
        Button switchMode = WidgetBuilder.buildButton("", 100);

        if ((boolean) AppConfigHelper.getConfigValue("darkmode")) {
            switchMode.setText("Light Mode");
        } else {
            switchMode.setText("Dark Mode");
        }

        Text text = WidgetBuilder.buildText("Please restart Application", new Font("Verdana", 20));

        HBox hBox = WidgetBuilder.buildHBox(50, Pos.CENTER);
        hBox.getChildren().add(register);
        hBox.getChildren().add(login);

        VBox vBox = WidgetBuilder.buildVBox(50, Pos.CENTER);
        vBox.getChildren().addAll(hBox, switchMode);

        root.setCenter(vBox);

        login.setOnAction(e -> navigator.navigateTo(SceneType.LOGIN));
        register.setOnAction(e -> navigator.navigateTo(SceneType.REGISTER));
        switchMode.setOnAction(e -> {
            if (!(boolean) AppConfigHelper.getConfigValue("darkmode")) {
                AppConfigHelper.overwriteProperty("darkmode", true);
                switchMode.setText("Light Mode");
            } else {
                AppConfigHelper.overwriteProperty("darkmode", false);
                switchMode.setText("Dark Mode");
            }
            try {
                vBox.getChildren().add(text);
            } catch (Exception ignored) {
            }
        });
    }
}
