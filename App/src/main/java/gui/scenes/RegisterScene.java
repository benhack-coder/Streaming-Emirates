package gui.scenes;

import auth.Register;
import gui.design.WidgetBuilder;
import gui.errorhandling.Alerts;
import gui.navigation.Navigator;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import request.HTTPRequestException;

import java.util.Objects;

public class RegisterScene extends Scene {
    private final Navigator navigator;
    private static final BorderPane root = WidgetBuilder.buildBorderPane();

    public RegisterScene(Navigator navigator) {
        super(root);
        this.navigator = navigator;
        setup();
    }

    private void setup() {
        Button registerButton = WidgetBuilder.buildButton("Register", 100);
        Button goBack = WidgetBuilder.buildButton("Go Back", 100);

        Text text = WidgetBuilder.buildText("Register", new Font("Verdana", 25));

        TextField username = WidgetBuilder.buildTextField("registerUsernameID", "Username", 200);

        PasswordField password = WidgetBuilder.buildPasswordField("registerPasswordID", "Password", 200);
        PasswordField repeatPassword = WidgetBuilder.buildPasswordField("registerRepeatPasswordID", "Repeat Password", 200);

        Alert notMatch = WidgetBuilder.buildAlert(Alert.AlertType.ERROR, "Password Error", "The passwords do not match!", "Please Try again");
        Alert nullPointer = WidgetBuilder.buildAlert(Alert.AlertType.ERROR, "Password Error", "The password can't be null", "Please enter a valid Password (min. 8 Chars)");
        Alert toShort = WidgetBuilder.buildAlert(Alert.AlertType.ERROR, "Password Error", "The password is to short", "Please enter a valid Password (min. 8 Chars)");
        Alert registerFailed = WidgetBuilder.buildAlert(Alert.AlertType.ERROR, "Register Failed", "Please Try again", null);

        VBox vbox = WidgetBuilder.buildVBox(20, Pos.CENTER);
        vbox.getChildren().add(text);
        vbox.getChildren().add(username);
        vbox.getChildren().add(password);
        vbox.getChildren().add(repeatPassword);
        vbox.getChildren().add(registerButton);
        vbox.getChildren().add(goBack);

        root.setCenter(vbox);

        registerButton.setOnAction(e -> {
            if (password.getText().equals(repeatPassword.getText()) && password.getText() != null && password.getText().length() >= 8) {
                Register register = new Register(password.getText(), username.getText());
                try {
                    if (register.getRequestResult() == 1) {
                        navigator.navigateTo(SceneType.LOGIN);
                    } else {
                        registerFailed.showAndWait();
                    }
                } catch (HTTPRequestException ioException) {
                    showErrorMessage();
                }
            }
            if (!Objects.equals(password.getText(), repeatPassword.getText())) {
                notMatch.showAndWait();
            }
            if (password.getText() == null) {
                nullPointer.showAndWait();
            }
            if (password.getText().length() < 8 && password.getText() != null) {
                toShort.showAndWait();
            }
            username.setText("");
            password.setText("");
            repeatPassword.setText("");
        });
        goBack.setOnAction(e -> {
            Navigator.goBack(SceneType.WELCOME, navigator);
            username.setText("");
            password.setText("");
            repeatPassword.setText("");
        });
    }

    private void showErrorMessage() {
        Alert alert = Alerts.generateNoInternetConnectionMessage();
        alert.showAndWait();
    }
}