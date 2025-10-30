package io.github.rjrico17.centroidfinder;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

import boofcv.io.MediaManager;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.SimpleImageSequence;
import boofcv.io.wrapper.DefaultMediaManager;
import boofcv.io.video.BoofVideoManager;
import boofcv.struct.image.Planar;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.ImageType;


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
    public static List<BufferedImage> processVideo(String path) {
        List<BufferedImage> list = new ArrayList<>(); 
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
