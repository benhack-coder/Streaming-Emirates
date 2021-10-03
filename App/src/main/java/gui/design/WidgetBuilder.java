package gui.design;

import config.AppConfigHelper;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class WidgetBuilder {
    public static TilePane buildTilePane(int setPrefColumns, int hgap, int vgap, Pos alignment) {
        TilePane tilePane = new TilePane();
        tilePane.setPrefColumns(setPrefColumns);
        tilePane.setHgap(hgap);
        tilePane.setVgap(vgap);
        tilePane.setAlignment(alignment);
        return tilePane;
    }

    public static HBox buildHBox(int setSpacing, Pos setAlignment) {
        HBox hBox = new HBox();
        hBox.setSpacing(setSpacing);
        hBox.setAlignment(setAlignment);
        return hBox;
    }

    public static VBox buildVBox(int spacing, Pos alignment) {
        VBox vBox = new VBox();
        vBox.setSpacing(spacing);
        vBox.setAlignment(alignment);
        return vBox;
    }

    public static Text buildText(String textString, Font font) {
        Text text = new Text(textString);
        text.setFont(font);
        return text;
    }

    public static TextField buildTextField(String id, String promptText, int maxWidth) {
        TextField username = new TextField();
        username.setId(id);
        username.setPromptText(promptText);
        username.setMaxWidth(maxWidth);
        return username;
    }

    public static PasswordField buildPasswordField(String id, String promptText, int maxWidth) {
        PasswordField password = new PasswordField();
        password.setId(id);
        password.setPromptText(promptText);
        password.setMaxWidth(maxWidth);
        return password;
    }

    public static Alert buildAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert;
    }

    public static Button buildButton(String text, int prefWidth) {
        Button button = new Button();
        button.setId("default");

        if ((boolean) AppConfigHelper.getConfigValue("darkmode")) {
            button.getStylesheets().remove("css/buttons.css");
            button.getStylesheets().add("css/darkmode.css");
        } else {
            button.getStylesheets().remove("css/darkmode.css");
            button.getStylesheets().add("css/buttons.css");
        }

        button.setText(text);
        button.setPrefWidth(prefWidth);
        return button;
    }

    public static CheckBox buildCheckBox(String text) {
        return new CheckBox(text);
    }

    public static BorderPane buildBorderPane() {
        BorderPane borderPane = new BorderPane();
        borderPane.setId("root");

        if ((boolean) AppConfigHelper.getConfigValue("darkmode")) {
            borderPane.getStylesheets().add("css/darkmode.css");
        } else {
            borderPane.getStylesheets().remove("css/darkmode.css");
        }

        return borderPane;
    }
}
