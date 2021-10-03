package gui.scenes;

import gui.design.WidgetBuilder;
import gui.navigation.Navigator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class DroneStreamMacNotSupportedScene extends Scene {
    private final Navigator navigator;
    private static final BorderPane root = WidgetBuilder.buildBorderPane();

    public DroneStreamMacNotSupportedScene(Navigator navigator) {
        super(root);
        this.navigator = navigator;
        setup();
    }
    private void setup() {
        Text text = WidgetBuilder.buildText("Stream is only on Windows.", new Font("Verdana", 20));

        Button goBack = WidgetBuilder.buildButton("Go Back", 100);
        goBack.setOnAction(e -> Navigator.goBack(SceneType.LOGGED_IN, navigator));

        VBox vBox = WidgetBuilder.buildVBox(20, Pos.CENTER);
        vBox.getChildren().addAll(text, goBack);

        root.setBottom(vBox);
        root.setPadding(new Insets(0, 0, 100, 0));
    }
}
