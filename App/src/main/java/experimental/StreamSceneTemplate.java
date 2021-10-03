package experimental;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.freedesktop.gstreamer.Bin;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.Pipeline;
import org.freedesktop.gstreamer.Version;
import org.freedesktop.gstreamer.fx.FXImageSink;

public class StreamSceneTemplate extends Application {

    /**
     * Always store the top-level pipeline reference to stop it being garbage
     * collected.
     */
    private Pipeline pipeline;

    @Override
    public void init() throws Exception {

        Gst.init(Version.BASELINE, "FXCamera");
    }

    @Override
    public void start(Stage stage) throws Exception {

        /**
         * FXImageSink from gst1-java-fx wraps the native data from the
         * GStreamer AppSink in a JavaFX image. Requested dimensions will be set
         * on the caps for the AppSink.
         */
        FXImageSink imageSink = new FXImageSink();
        imageSink.requestFrameSize(640, 480);

        /**
         * Parse a Bin to contain the autovideosrc from a GStreamer string
         * representation. The alternative approach would be to create and link
         * the elements in code using ElementFactory::make.
         *
         * The Bin uses a videoconvert element to convert the video format to
         * that required by the FXImageSink, a videoscale in case the source
         * does not support the required resolution.
         *
         * The bin is added to a top-level pipeline and linked to the AppSink
         * from the image sink.
         */
        Bin bin = Gst.parseBinFromDescription(
                "autovideosrc ! videoscale ! videoconvert",
                true);
        pipeline = new Pipeline();
        pipeline.addMany(bin, imageSink.getSinkElement());
        Pipeline.linkMany(bin, imageSink.getSinkElement());

        /**
         * Set up a simple JavaFX window with ImageView.
         */
        stage.setTitle("FX Camera");
        BorderPane pane = new BorderPane();
        pane.setBackground(new Background(new BackgroundFill(
                Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        ImageView view = new ImageView();
        pane.setCenter(view);

        /**
         * Bind the ImageView to the image from the FXImageSink, and scale the
         * view within the window.
         */
        view.imageProperty().bind(imageSink.imageProperty());
        view.fitWidthProperty().bind(pane.widthProperty());
        view.fitHeightProperty().bind(pane.heightProperty());
        view.setPreserveRatio(true);

        /**
         * Show the window and start the pipeline.
         */
        stage.setScene(new Scene(pane, 640, 480));
        stage.show();
        pipeline.play();
    }
}
