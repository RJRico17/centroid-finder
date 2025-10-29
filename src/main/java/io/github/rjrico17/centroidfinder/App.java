package io.github.rjrico17.centroidfinder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import javax.imageio.ImageIO;

public class App {
    public static void main(String args[]) throws Exception {
        VideoArgProcessor config = new VideoArgProcessor(args);

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, config.getTargetColor(), config.getTargetColor());
        
        // int[][] binarized = binarizer.toBinaryArray(ImageIO.read(new File(config.getInputPath())));
        // BufferedImage buffered = binarizer.toBufferedImage(binarized);

        ImageGroupFinder groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());


        List<BufferedImage> frames = VideoProcessor.processVideo(config.getInputPath());
        try (PrintWriter writer = new PrintWriter("groups.csv")) {
            for (BufferedImage img : frames) {
                List<Group> groups = groupFinder.findConnectedGroups(img);
                for (Group group : groups) {
                    writer.println(group.toCsvRow());
                }
            }
            System.out.println("Groups summary saved as groups.csv");
        } catch (Exception e) {
            System.err.println("Error writing groups.csv");
            e.printStackTrace();
        }
        
    }
}
