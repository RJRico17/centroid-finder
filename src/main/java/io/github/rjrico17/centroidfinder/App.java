package io.github.rjrico17.centroidfinder;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class App {
    public static void main(String args[]) throws Exception {
        VideoArgProcessor config = new VideoArgProcessor(args);

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, config.getTargetColor(), config.getTargetColor());
        
        int[][] binarized = binarizer.toBinaryArray(ImageIO.read(new File(config.getInputPath())));
        BufferedImage buffered = binarizer.toBufferedImage(binarized);

        ImageGroupFinder groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());

        

    }
}
