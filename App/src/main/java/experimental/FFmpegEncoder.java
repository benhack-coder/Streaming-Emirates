package experimental;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;
import org.apache.log4j.BasicConfigurator;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class FFmpegEncoder {
    public static void main(String[] args) throws IOException {
        BasicConfigurator.configure();

        FFmpeg ffmpeg = new FFmpeg("ffmpeg");
        FFprobe ffprobe = new FFprobe("ffprobe");

        System.out.println(ffprobe.version());
        System.out.println(ffmpeg.version());
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput("src/main/resources/video/vid1.mp4")     // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists

                .addOutput("src/main/resources/video/output.mp4")   // Filename for the destination
                .setFormat("mp4")        // Format is inferred from filename, or can be set

                .disableSubtitle()       // No subtiles

                .setAudioChannels(1)         // Mono audio
                .setAudioCodec("aac")        // using the aac codec
                .setAudioSampleRate(48_000)  // at 48KHz
                .setAudioBitRate(32768)      // at 32 kbit/s

                .setVideoCodec("libx264")     // Video using x264
                .setVideoFrameRate(24, 1)     // at 24 frames per second
                .setVideoResolution(640, 480) // at 640x480 resolution
                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

// Run a one-pass encode
        FFmpegProbeResult in = ffprobe.probe("src/main/resources/video/vid1.mp4");

        FFmpegJob job = executor.createJob(builder, new ProgressListener() {

            // Using the FFmpegProbeResult determine the duration of the input
            final double duration_ns = in.getFormat().duration * TimeUnit.SECONDS.toNanos(1);

            @Override
            public void progress(Progress progress) {
                double percentage = progress.out_time_ns / duration_ns;
                System.out.printf("[%.0f%%]%n", percentage * 100);
            }
        });
        job.run();
    }





// Or run a two-pass encode (which is better quality at the cost of being slower)
        //executor.createTwoPassJob(builder).run();
}

