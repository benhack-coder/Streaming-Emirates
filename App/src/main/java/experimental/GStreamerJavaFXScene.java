package experimental;

import javafx.animation.AnimationTimer;
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
import org.freedesktop.gstreamer.*;
import org.freedesktop.gstreamer.fx.FXImageSink;

public class GStreamerJavaFXScene extends Application {

    private Pipeline pipeline;
    @Override
    public void start(Stage stage) throws Exception {
        Gst.init(Version.BASELINE, "FXCamera");
        pipeline = new Pipeline();
        FXImageSink imageSink = new FXImageSink();
        imageSink.requestFrameSize(640, 480);

        Caps caps = Caps.fromString("application/x-rtp, media=(string)video, clock-rate=(int)90000, encoding-name=(string)H264, payload=(int)96");
        Element src = ElementFactory.make("udpsrc", null);
        src.set("port", 1234);
        src.setCaps(caps);
        Bin bin = Gst.parseBinFromDescription("rtph264depay ! decodebin ! videoconvert ! autovideosink", true);

        pipeline.addMany(src, bin, imageSink.getSinkElement());
        Pipeline.linkMany(src, bin, imageSink.getSinkElement());

        stage.setTitle("FX Camera");
        BorderPane pane = new BorderPane();
        pane.setBackground(new Background(new BackgroundFill(
                Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        ImageView view = new ImageView();
        pane.setCenter(view);

        view.imageProperty().bind(imageSink.imageProperty());
        view.fitWidthProperty().bind(pane.widthProperty());
        view.fitHeightProperty().bind(pane.heightProperty());
        view.setPreserveRatio(true);

        stage.setScene(new Scene(pane, 640, 480));
        stage.show();

        pipeline.play();

        new AnimationTimer(){
            @Override
            public void handle(long now) {
                System.out.println(pipeline.getState());
            }
        }.start();

    }
}
