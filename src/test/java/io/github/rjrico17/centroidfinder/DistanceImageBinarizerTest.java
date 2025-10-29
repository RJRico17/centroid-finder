package io.github.rjrico17.centroidfinder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.image.BufferedImage;
import static org.junit.jupiter.api.Assertions.*;

// Mock implementation of ColorDistanceFinder for testing
// interface ColorDistanceFinder {
//     double distance(int color1, int color2);
// }

class MockColorDistanceFinder implements ColorDistanceFinder {
    @Override
    public double distance(int color1, int color2) {
        // Simple mock: compute absolute difference of RGB values for testing
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;
        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;
        return Math.sqrt(Math.pow(r1 - r2, 2) + Math.pow(g1 - g2, 2) + Math.pow(b1 - b2, 2));
    }
}

public class DistanceImageBinarizerTest {
    private DistanceImageBinarizer binarizer;
    private ColorDistanceFinder distanceFinder;
    private final int targetColor = 0xFF0000; // Red (RGB: 255, 0, 0)
    private final int threshold = 100;

    @BeforeEach
    void setUp() {
        distanceFinder = new MockColorDistanceFinder();
        binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);
    }

    @Test
    void testConstructor() {
        assertNotNull(binarizer, "Binarizer should be instantiated");
    }

    @Test
    void testToBinaryArray_SinglePixelCloseToTargetColor() {
        // Create a 1x1 image with a color close to target (red)
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0xFF3333); // Close to red (distance < 100)

        int[][] result = binarizer.toBinaryArray(image);

        assertEquals(1, result[0][0], "Pixel close to target color should be white (1)");
    }

    @Test
    void testToBinaryArray_SinglePixelFarFromTargetColor() {
        // Create a 1x1 image with a color far from target (red)
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0x00FF00); // Green (distance > 100)

        int[][] result = binarizer.toBinaryArray(image);

        assertEquals(0, result[0][0], "Pixel far from target color should be black (0)");
    }

    // @Test
    // void testToBinaryArray_MultiPixelImage() {
    //     // Create a 2x2 image with mixed colors
    //     BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
    //     image.setRGB(0, 0, 0xFF3333); // Close to red (white)
    //     image.setRGB(0, 1, 0x00FF00); // Green (black)
    //     image.setRGB(1, 0, 0xFF0000); // Exact red (white)
    //     image.setRGB(1, 1, 0x0000FF); // Blue (black)

    //     int[][] result = binarizer.toBinaryArray(image);

    //     assertEquals(2, result.length, "Result array should have width 2");
    //     assertEquals(2, result[0].length, "Result array should have height 2");
    //     assertEquals(1, result[0][0], "Pixel close to red should be white (1)");
    //     assertEquals(0, result[0][1], "Green pixel should be black (0)");
    //     assertEquals(1, result[1][0], "Exact red pixel should be white (1)");
    //     assertEquals(0, result[1][1], "Blue pixel should be black (0)");
    // }

    // @Test
    // void testToBufferedImage_BinaryArrayToImage() {
    //     // Create a 2x2 binary array
    //     int[][] binaryArray = {
    //         {1, 0},
    //         {0, 1}
    //     };

    //     BufferedImage result = binarizer.toBufferedImage(binaryArray);

    //     assertEquals(2, result.getWidth(), "Image width should be 2");
    //     assertEquals(2, result.getHeight(), "Image height should be 2");
    //     assertEquals(0xFFFFFF, result.getRGB(0, 0), "White (1) should be black RGB (0x000000)");
    //     assertEquals(0x000000, result.getRGB(0, 1), "Black (0) should be white RGB (0xFFFFFF)");
    //     assertEquals(0x000000, result.getRGB(1, 0), "Black (0) should be white RGB (0xFFFFFF)");
    //     assertEquals(0xFFFFFF, result.getRGB(1, 1), "White (1) should be black RGB (0x000000)");
    // }

    @Test
    void testToBinaryArray_NullImage() {
        assertThrows(NullPointerException.class, () -> {
            binarizer.toBinaryArray(null);
        }, "Should throw NullPointerException for null image");
    }

    @Test
    void testToBufferedImage_NullArray() {
        assertThrows(NullPointerException.class, () -> {
            binarizer.toBufferedImage(null);
        }, "Should throw NullPointerException for null array");
    }

    @Test
    void testToBufferedImage_InvalidBinaryValues() {
        // Create a 1x1 array with invalid value (not 0 or 1)
        int[][] invalidArray = {{2}};

        assertThrows(IllegalArgumentException.class, () -> {
            binarizer.toBufferedImage(invalidArray);
        }, "Should throw IllegalArgumentException for invalid binary values");
    }
}