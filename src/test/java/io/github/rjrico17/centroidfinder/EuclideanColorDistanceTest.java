package io.github.rjrico17.centroidfinder;

//AI USED
//AI USED
//AI USED
//AI USED


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EuclideanColorDistanceTest {

    private EuclideanColorDistance finder;

    @BeforeEach
    void setUp() {
        finder = new EuclideanColorDistance();
    }

    // Tests for distance method
    @Test
    void testDistanceSameColor() {
        int colorA = 0xFF0000; // Pure red: (255, 0, 0)
        int colorB = 0xFF0000; // Pure red: (255, 0, 0)
        double distance = finder.distance(colorA, colorB);
        assertEquals(0.0, distance, 0.0001, "Distance between identical colors should be 0");
    }

    @Test
    void testDistanceBlackToWhite() {
        int colorA = 0x000000; // Black: (0, 0, 0)
        int colorB = 0xFFFFFF; // White: (255, 255, 255)
        // Expected: sqrt((255-0)^2 + (255-0)^2 + (255-0)^2) = sqrt(3 * 255^2) = sqrt(3 * 65025) = sqrt(195075) ≈ 441.67295593
        double expected = Math.sqrt(3 * 255 * 255);
        double distance = finder.distance(colorA, colorB);
        assertEquals(expected, distance, 0.0001, "Distance between black and white should be sqrt(3 * 255^2)");
    }

    @Test
    void testDistanceTypicalColors() {
        int colorA = 0xFF5733; // RGB: (255, 87, 51)
        int colorB = 0x33FF57; // RGB: (51, 255, 87)
        // Expected: sqrt((255-51)^2 + (87-255)^2 + (51-87)^2)
        // = sqrt(204^2 + (-168)^2 + (-36)^2)
        // = sqrt(41616 + 28224 + 1296) = sqrt(71136) ≈ 266.71437495
        double expected = Math.sqrt(204 * 204 + 168 * 168 + 36 * 36);
        double distance = finder.distance(colorA, colorB);
        assertEquals(expected, distance, 0.0001, "Distance between (255,87,51) and (51,255,87) should match expected");
    }

    @Test
    void testDistanceSingleComponentDifference() {
        int colorA = 0xFF0000; // Pure red: (255, 0, 0)
        int colorB = 0xCC0000; // Darker red: (204, 0, 0)
        // Expected: sqrt((255-204)^2 + (0-0)^2 + (0-0)^2) = sqrt(51^2) = sqrt(2601) ≈ 51.0
        double expected = 51.0;
        double distance = finder.distance(colorA, colorB);
        assertEquals(expected, distance, 0.0001, "Distance should be 51 for red component difference");
    }

    // Tests for redConverter
    @Test
    void testRedConverter() {
        int color = 0xFF5733; // RGB: (255, 87, 51)
        assertEquals(255, finder.redConverter(color), "Red component should be 255");
    }

    @Test
    void testRedConverterZero() {
        int color = 0x00FF00; // RGB: (0, 255, 0)
        assertEquals(0, finder.redConverter(color), "Red component should be 0");
    }

    // Tests for greenConverter
    @Test
    void testGreenConverter() {
        int color = 0xFF5733; // RGB: (255, 87, 51)
        assertEquals(87, finder.greenConverter(color), "Green component should be 87");
    }

    @Test
    void testGreenConverterZero() {
        int color = 0xFF00FF; // RGB: (255, 0, 255)
        assertEquals(0, finder.greenConverter(color), "Green component should be 0");
    }

    // Tests for blueConverter
    @Test
    void testBlueConverter() {
        int color = 0xFF5733; // RGB: (255, 87, 51)
        assertEquals(51, finder.blueConverter(color), "Blue component should be 51");
    }

    @Test
    void testBlueConverterZero() {
        int color = 0xFFFF00; // RGB: (255, 255, 0)
        assertEquals(0, finder.blueConverter(color), "Blue component should be 0");
    }
}