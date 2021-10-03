package gui.design;

import javafx.scene.control.Button;

public class VideoPlayerButton extends Button {

    private static final int PREF_WIDTH = 100;

    public VideoPlayerButton(String buttonText) {
        this.setText(buttonText);
        this.setPrefWidth(PREF_WIDTH);
        this.setId("vPButton");
        this.getStylesheets().add("css/buttons.css");
    }
}
