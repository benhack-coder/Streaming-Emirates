package experimental;

import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.Pipeline;
import org.freedesktop.gstreamer.Version;

import java.util.concurrent.TimeUnit;

public class GStreamerBasicPipeline {
    private static Pipeline pipeline;

    public static void main(String[] args) {
        Gst.init(Version.BASELINE, "FXCamera");
        pipeline = (Pipeline) Gst.parseLaunch(
                "udpsrc port=1234 caps=\"application/x-rtp, media=(string)video, clock-rate=(int)90000, encoding-name=(string)H264, payload=(int)96\"!rtph264depay!decodebin!videoconvert!autovideosink");
        pipeline.play();
        Gst.getExecutor().schedule(Gst::quit, 10, TimeUnit.SECONDS);
        Gst.main();
    }
}
