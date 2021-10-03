package gui.scenes;

import gui.design.WidgetBuilder;
import gui.errorhandling.Alerts;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import util.Installer;

public class InstallationScene extends Scene {
    private static final BorderPane root = WidgetBuilder.buildBorderPane();

    public InstallationScene() {
        super(root);
        setup();
    }

    private void setup() {
        Text text = WidgetBuilder.buildText("After the installation it is necessary to restart the PC", new Font("Verdana", 20));

        Button installButton = WidgetBuilder.buildButton("Install", 100);
        Button quit = WidgetBuilder.buildButton("Quit", 100);

        HBox hBox = WidgetBuilder.buildHBox(20, Pos.CENTER);
        hBox.getChildren().addAll(installButton, quit);

        VBox vBox = WidgetBuilder.buildVBox(20, Pos.CENTER);
        vBox.getChildren().addAll(text, hBox);

        root.setCenter(vBox);

        installButton.setOnAction(e -> {
            vBox.getChildren().remove(hBox);
            text.setText("Please wait...");
            Thread thread = new Thread(this::startInstallation);
            thread.start();
        });

        quit.setOnAction(e -> Platform.exit());
    }

    private void startInstallation(){
        Installer installer = new Installer();
        if (!installer.install()) {
            Alert alert = Alerts.InstallationFailedMessage();
            alert.showAndWait();
        }
    }
}
