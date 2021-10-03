package gstreamer;

import org.freedesktop.gstreamer.Bin;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.Pipeline;
import org.freedesktop.gstreamer.fx.FXImageSink;

public class CameraCapture {
    private Pipeline pipeline;
    private final FXImageSink fxImageSink;
    private final int width;
    private final int height;

    public CameraCapture(FXImageSink fxImageSink, int width, int height) {
        this.fxImageSink = fxImageSink;
        this.width = width;
        this.height = height;
        createPipeline();
    }

    private void createPipeline() {
        fxImageSink.requestFrameSize(width, height);
        Bin bin = Gst.parseBinFromDescription("autovideosrc ! videoscale ! videoconvert",
                true);
        pipeline = new Pipeline();
        pipeline.addMany(bin, fxImageSink.getSinkElement());
        Pipeline.linkMany(bin, fxImageSink.getSinkElement());
    }

    public Pipeline getPipeline() {
        return pipeline;
    }

    public FXImageSink getFxImageSink() {
        return fxImageSink;
    }

}
