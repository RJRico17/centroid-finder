package io.github.rjrico17.centroidfinder;

public class App {
    public static void main(String args[]) throws Exception {
        VideoArgProcessor config = new VideoArgProcessor(args);

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, config.getTargetColor(), config.getTargetColor());
        ImageGroupFinder groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());
    }
}
