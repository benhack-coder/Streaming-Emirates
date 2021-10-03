package experimental;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import nu.pattern.OpenCV;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class OpenCVTest extends Application {

    @Override
    public void start(Stage primaryStage) {
        initCV();
        StackPane root = new StackPane();
        VideoCapture video = openCam();
        Canvas canvas = new Canvas(500, 500);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(getFrame(video), 0, 0);
        root.getChildren().add(canvas);

        new AnimationTimer() {
            int count = 0;
            double a = System.nanoTime() / 1e9 + 1;
            @Override
            public void handle(long now) {
                count +=1;
                //System.out.println(a + " " + now/1e9);
                if (a < now/1e9){
                    System.out.println(count);
                    a += 1;
                    count = 0;
                }
                update(gc, canvas, video);
            }
        }.start();

        Scene scene = new Scene(root, 370, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    private void initCV(){
        OpenCV.loadLocally();
    }

    private VideoCapture openCam(){
        VideoCapture video = new VideoCapture();
        video.open("udp://0.0.0.0:1234");
        return video;
    }

    private Image getFrame(VideoCapture camera){
        Mat mat = new Mat();
        camera.read(mat);
        InputStream bytesArray = convertToBytesArray(mat);
        return new Image(bytesArray);
    }

    private void update(GraphicsContext gc, Canvas canvas, VideoCapture video){
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(getFrame(video), 0, 0);
    }

    private InputStream convertToBytesArray(Mat mat) {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", mat, matOfByte);
        //Storing the encoded Mat in a byte array
        byte[] byteArray = matOfByte.toArray();
        System.out.println((byteArray.length));
        //Preparing the Buffered Image
        return new ByteArrayInputStream(byteArray);
    }
}
