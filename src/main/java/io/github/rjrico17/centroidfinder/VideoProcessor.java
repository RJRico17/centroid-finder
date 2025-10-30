package io.github.rjrico17.centroidfinder;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

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
            // SimpleImageSequence<Planar<GrayU8>> seq = BoofVideoManager
            // .loadManagerDefault()
            // .loadVideo(path, ImageType.pl(3, GrayU8.class));
            MediaManager media = DefaultMediaManager.INSTANCE;
            SimpleImageSequence<Planar<GrayU8>> seq =
            media.openVideo(path, ImageType.pl(3, GrayU8.class));
        
            if (seq == null) System.err.println("Error processing video");

            int frameCount = 0;

            while (seq.hasNext()) {
                Planar<GrayU8> frame = seq.next();
                BufferedImage out = new BufferedImage(seq.getWidth(), seq.getHeight(), BufferedImage.TYPE_INT_RGB);
                frameCount++;

                if (frameCount % 60 == 0) {
                    System.out.println("Processing frame #" + frameCount);
                    list.add(ConvertBufferedImage.convertTo(frame, out, true));
                }
            }
        return list;
    }
}
