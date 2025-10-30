package io.github.rjrico17.centroidfinder;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.util.List;

public class App {
    public static void main(String args[]) throws Exception {
        VideoArgProcessor config = new VideoArgProcessor(args);

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, config.getTargetColor(), config.getThreshold());
        
        // int[][] binarized = binarizer.toBinaryArray(ImageIO.read(new File(config.getInputPath())));
        // BufferedImage buffered = binarizer.toBufferedImage(binarized);

        ImageGroupFinder groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());


        List<VideoProcessor.TimestampedFrame> frames = VideoProcessor.processVideo(config.getInputPath());
        try (PrintWriter writer = new PrintWriter(config.getOutputCsv())) {
            for (VideoProcessor.TimestampedFrame frame : frames) {
                double time = frame.getTimeInSeconds();
                BufferedImage image = frame.getImage();

                List<Group> groups = groupFinder.findConnectedGroups(image);

                if (groups.isEmpty()) {
                    // writer class lets us write character streams like shown below
                    writer.printf("%.3f,%d,%d%n", time, -1, -1);
                } else {
                    Group largest = groups.get(0);
                    writer.printf("%.3f,%d,%d%n", time, largest.centroid().x(), largest.centroid().y());
                }
            }
            System.out.println("Groups summary saved as groups.csv");
        } catch (Exception e) {
            System.err.println("Error writing groups.csv");
            e.printStackTrace();
        }
        
    }
}
