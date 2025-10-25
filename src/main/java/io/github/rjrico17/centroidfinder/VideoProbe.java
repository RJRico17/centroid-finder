/**
 * Probe Class to Learn the API
 */

package io.github.rjrico17.centroidfinder;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayU8;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Small probe to learn video I/O and frame extraction.
 *
 * Usage:
 *   mvn exec:java -Dexec.mainClass=io.github.rjrico17.centroidfinder.VideoProbe \
 *     -Dexec.args="path/to/video.mp4 outDir"
 */
public class VideoProbe {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("args: <videoPath> <outDir>");
            System.exit(2);
        }

        String path = args[0];
        File outDir = new File(args[1]);
        outDir.mkdirs();

        // Preflight
        File f = new File(path);
        System.out.println("Reading video from: " + f.getAbsolutePath());
        if (!f.exists()) {
            System.err.println("ERROR: file not found.");
            System.exit(1);
        }

        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(path)) {
            // grabber.setFormat("mp4"); // uncomment if your file needs a hint
            grabber.start();

            double fps = grabber.getVideoFrameRate();
            int w = grabber.getImageWidth();
            int h = grabber.getImageHeight();
            long durationUs = grabber.getLengthInTime(); // microseconds

            System.out.printf("Video: %s%n", path);
            System.out.printf("Resolution: %dx%d%n", w, h);
            System.out.printf("FPS: %.3f%n", fps);
            System.out.printf("Duration: %.2fs%n", durationUs / 1_000_000.0);

            Java2DFrameConverter toBI = new Java2DFrameConverter();

            int idx = 0;            // frame counter
            int saved = 0;          // number of frames saved

            // ---- choose ONE sampling strategy ----
            final int everyN = 30;      // ~1 per second at ~30fps
            final double strideSec = 0.5; // or: save every 0.5 seconds
            double nextSaveSec = 0.0;

            Frame frame;
            while ((frame = grabber.grabImage()) != null) {
                long tsUs = grabber.getTimestamp();
                double tSec = tsUs / 1_000_000.0;

                BufferedImage bi = toBI.convert(frame);

                // Convert to BoofCV image so you can reuse your pipeline later
                GrayU8 gray = ConvertBufferedImage.convertFrom(bi, (GrayU8) null);

                // --- Strategy A (by frame index):
                // boolean shouldSave = (idx % everyN == 0);

                // --- Strategy B (by timestamp stride):
                boolean shouldSave = tSec + 1e-9 >= nextSaveSec; // epsilon to avoid float miss

                if (shouldSave) {
                    File out = new File(outDir, String.format("frame_%05d_%.3fs.png", idx, tSec));
                    ImageIO.write(bi, "png", out);
                    saved++;
                    nextSaveSec += strideSec;
                }

                // periodic log
                if (idx % 30 == 0) {
                    System.out.printf("frame=%d  t=%.3fs  size=%dx%d  saved=%d%n",
                            idx, tSec, bi.getWidth(), bi.getHeight(), saved);
                }

                idx++;
            }

            grabber.stop();
            System.out.println("Done. Frames saved to: " + outDir.getAbsolutePath());
        }
    }
}
