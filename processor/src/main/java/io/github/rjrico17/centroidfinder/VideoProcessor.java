package io.github.rjrico17.centroidfinder;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import org.bytedeco.javacv.Java2DFrameConverter; 
import org.bytedeco.javacv.Frame;  

import org.bytedeco.javacv.FFmpegFrameGrabber;

public class VideoProcessor {

    public static class TimestampedFrame {
        private final BufferedImage image;
        private final double timeInSeconds;
        
        public TimestampedFrame(BufferedImage image, double timeInSeconds) {
            this.image = image;
            this.timeInSeconds = timeInSeconds;
        }
        
        public BufferedImage getImage() {
            return image;
        }
        
        public double getTimeInSeconds() {
            return timeInSeconds;
        }
    }
    public static List<TimestampedFrame> processVideo(String path) {
        List<TimestampedFrame> list = new ArrayList<>(); 

        try {
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(path);
            grabber.start();

            Java2DFrameConverter converter = new Java2DFrameConverter();

            Frame frame;
            int frameCount = 0;

            while ((frame = grabber.grabImage()) != null) {
                long timestampUs = grabber.getTimestamp();
                double timeInSeconds = timestampUs / 1_000_000.0;

                BufferedImage bufferedImage = converter.convert(frame);

                if (bufferedImage != null) {
                    list.add(new TimestampedFrame(bufferedImage, timeInSeconds));
                    frameCount++;

                    if (frameCount % 30 == 0) {
                        System.out.printf("Processed frame %d at %.3fs%n", frameCount, timeInSeconds);
                    }
                }
            }
            // method from FFMPEGFrameGrabber to close the connection to the file or release system resources 
            grabber.stop();
            System.out.printf("Total frames processed: %d%n", frameCount);
        } catch (Exception e) {
            // prings detailed description of error in case things go south
            System.err.println("Error processing video: " + e.getMessage()); // err instead of out for error messages
            e.printStackTrace();
        }
        return list;
    }
}
