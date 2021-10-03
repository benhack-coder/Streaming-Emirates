package gui.scenes;

import auth.Login;
import common.PasswordHasher;
import config.AppConfigHelper;
import gui.design.WidgetBuilder;
import gui.errorhandling.Alerts;
import gui.navigation.Navigator;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import request.HTTPRequestException;
import auth.Session;


public class LoginScene extends Scene {
    private final Navigator navigator;
    private static final BorderPane root = WidgetBuilder.buildBorderPane();
    private static String userName;
    private static String passwordText;

    public LoginScene(Navigator navigator) {
        super(root);
        this.navigator = navigator;
        setup();
    }

    private void setup() {
        Button loginbnt = WidgetBuilder.buildButton("Login", 100);
        Button goBack = WidgetBuilder.buildButton("Go Back", 100);

        Text text = WidgetBuilder.buildText("Login", Font.font("Verdana", 25));
        TextField username = WidgetBuilder.buildTextField("usernameID", "Username", 200);

        PasswordField password = WidgetBuilder.buildPasswordField("passwordID", "Password", 200);

        Alert loginFailed = WidgetBuilder.buildAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Username / Password", "Please try again");

        CheckBox checkBox = WidgetBuilder.buildCheckBox("Stay logged in?");

        VBox vbox = WidgetBuilder.buildVBox(20, Pos.CENTER);
        vbox.getChildren().addAll(text, username, password, loginbnt, goBack, checkBox);

        root.setCenter(vbox);

        goBack.setOnAction(e -> Navigator.goBack(SceneType.WELCOME, navigator));
        loginbnt.setOnAction(e -> {
            userName = username.getText();
            passwordText = PasswordHasher.getsha512(password.getText());
            if (stateCheckBox(checkBox)) {
                writeLogInProperty();
            }
            Login login = new Login(password.getText(), userName);
            try {
                int id = login.getRequestResult();
                if (id != 0) {
                    Session.sessionId = id;
                    try {
                        navigator.registerScene(SceneType.YOUTUBE, new YouTubeScene(username.getText(), navigator));
                    } catch  (Exception ignored) {
                    }
                    navigator.navigateTo(SceneType.LOGGED_IN);
                } else {
                    loginFailed.showAndWait();
                }
            } catch (HTTPRequestException ioException) {
                Alert alert = Alerts.generateNoInternetConnectionMessage();
                alert.showAndWait();
            }
            username.setText("");
            password.setText("");
        });
    }

    public static String getUserName() {
        return userName;
    }

    private boolean stateCheckBox(CheckBox checkBox) {
        return checkBox.isSelected();
    }

    private void writeLogInProperty () {
        AppConfigHelper.overwriteProperty("loggedin", true);
        AppConfigHelper.overwriteProperty("username", userName);
        AppConfigHelper.overwriteProperty("password", passwordText);
    }
}