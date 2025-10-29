package io.github.rjrico17.centroidfinder;

public class VideoArgProcessor {
    private final String inputPath;
    private final String outputCsv;
    private final String targetHex;
    private final int targetColor;
    private final int threshold;

    

    // public VideoArgProcessor(String inputPath, String outputCsv, int targetColor, int threshold) {
    //     this.inputPath = inputPath;
    //     this.outputCsv = outputCsv;
    //     this.targetColor = targetColor;
    //     this.threshold = threshold;
    // }

    public VideoArgProcessor(String args[]) {
        if (args.length != 4) {
            System.err.println("Error use: java -jar videoprocessor.jar inputPath outputCsv targetColor threshold");
        }

        this.inputPath = args[0];
        this.outputCsv = args[1];
        this.targetHex = args[2];

        int parsedThreshold = 0;
        try {
            parsedThreshold = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            System.err.println("Threshold must be an integer.");
        }
        this.threshold = parsedThreshold;


        int convertedTargetColor = 0;
        try {
            convertedTargetColor = Integer.parseInt(targetHex, 16);
        } catch (NumberFormatException e) {
            System.err.println("Invalid hex color. Please provide in RRGGBB format.");
            System.exit(1);
        }
        this.targetColor = convertedTargetColor;
    }

    public String getInputPath() {
        return inputPath;
    }

    public String getOutputCsv() {
        return outputCsv;
    }

    public String getTargetHex() {
        return targetHex;
    }

    public int getTargetColor() {
        return targetColor;
    }

    public int getThreshold() {
        return threshold;
    }
}
