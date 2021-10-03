package gstreamer;

import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.Pipeline;

public class Stream {
    private Pipeline pipeline;

    public void play(int port) {
        pipeline = (Pipeline) Gst.parseLaunch(
                "udpsrc port=" + port + " caps=\"application/x-rtp, media=(string)video, clock-rate=(int)90000, encoding-name=(string)H264, payload=(int)96\"!rtph264depay!decodebin!videoconvert!autovideosink");
        pipeline.play();
        System.out.println("Pipeline set to " + pipeline.getState());
        Gst.main();
    }

    public void stop() {
        pipeline.stop();
        System.out.println("Pipeline set to " + pipeline.getState());
        Gst.getExecutor().execute(Gst::quit);
    }
}
