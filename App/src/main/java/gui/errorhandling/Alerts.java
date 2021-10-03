package gui.errorhandling;

import gui.design.WidgetBuilder;
import javafx.scene.control.Alert;

public class Alerts {
    public static Alert InstallationFailedMessage() {
        return WidgetBuilder.buildAlert(Alert.AlertType.ERROR, "Error", "The Installation Failed", "Try Again!");
    }
    public static Alert generateNoInternetConnectionMessage() {
        return WidgetBuilder.buildAlert(Alert.AlertType.ERROR, "No internet", "Internet connection is required for this procedure", "");
    }
    public static Alert generateSomethingWentWrongMessage() {
        return WidgetBuilder.buildAlert(Alert.AlertType.ERROR, "Error", "Something went wrong", "");
    }
    public static Alert serversAreCurrentlyDownMessage(){
        return WidgetBuilder.buildAlert(Alert.AlertType.ERROR, "Error", "The Servers are Currently down, please try again later", "");
    }
}
