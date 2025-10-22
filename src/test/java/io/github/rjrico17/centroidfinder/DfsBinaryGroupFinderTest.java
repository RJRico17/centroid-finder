package io.github.rjrico17.centroidfinder;

//AI USED
//AI USED
//AI USED
//AI USED

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class DfsBinaryGroupFinderTest {

    private DfsBinaryGroupFinder finder;

    @BeforeEach
    void setUp() {
        finder = new DfsBinaryGroupFinder();
    }

    @Test
    void testNullImageThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> finder.findConnectedGroups(null));
    }

    @Test
    void testEmptyImageThrowsIllegalArgumentException() {
        int[][] image = {};
        assertThrows(IllegalArgumentException.class, () -> finder.findConnectedGroups(image));
    }

    @Test
    void testSinglePixelOne() {
        int[][] image = {{1}};
        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(1, groups.size());
        Group expected = new Group(1, new Coordinate(0, 0));
        assertEquals(expected, groups.get(0));
    }

    @Test
    void testSinglePixelZero() {
        int[][] image = {{0}};
        List<Group> groups = finder.findConnectedGroups(image);
        assertTrue(groups.isEmpty());
    }

    @Test
    void testSingleGroup() {
        int[][] image = {
            {1, 1},
            {1, 0}
        };
        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(1, groups.size());
        // Size = 3, x-sum = 0+1+0 = 1, y-sum = 0+0+1 = 1, centroid = (1/3, 1/3) = (0, 0)
        Group expected = new Group(3, new Coordinate(0, 0));
        assertEquals(expected, groups.get(0));
    }

    @Test
    void testMultipleGroups() {
        int[][] image = {
            {1, 0, 1},
            {0, 1, 0},
            {1, 0, 1}
        };
        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(5, groups.size());
        List<Group> expected = Arrays.asList(
            new Group(1, new Coordinate(0, 0)), // Top-left
            new Group(1, new Coordinate(2, 0)), // Top-right
            new Group(1, new Coordinate(1, 1)), // Middle
            new Group(1, new Coordinate(0, 2)),  // Bottom-left
            new Group(1, new Coordinate(2, 2))  // Bottom-right
        );
        assertEquals(expected, groups);
    }

    @Test
    void testLargerGroupWithSorting() {
        int[][] image = {
            {1, 1, 0, 0},
            {1, 1, 0, 1},
            {0, 0, 0, 1}
        };
        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(2, groups.size());
        // Group 1: (0,0), (1,0), (0,1), (1,1) -> size=4, x-sum=0+0+1+1=2, y-sum=0+1+0+1=2, centroid=(2/4, 2/4)=(0,0)
        // Group 2: (3,1), (3,2) -> size=2, x-sum=3+3=6, y-sum=1+2=3, centroid=(6/2, 3/2)=(3,1)
        List<Group> expected = Arrays.asList(
            new Group(4, new Coordinate(0, 0)), // Larger group first
            new Group(2, new Coordinate(3, 1))
        );
        assertEquals(expected, groups);
    }

    @Test
    void testAllZeros() {
        int[][] image = {
            {0, 0, 0},
            {0, 0, 0}
        };
        List<Group> groups = finder.findConnectedGroups(image);
        assertTrue(groups.isEmpty());
    }

    @Test
    void testAllOnes() {
        int[][] image = {
            {1, 1, 1},
            {1, 1, 1}
        };
        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(1, groups.size());
        // Size=6, x-sum=0+1+2+0+1+2=6, y-sum=0+0+0+1+1+1=3, centroid=(6/6, 3/6)=(1,0)
        Group expected = new Group(6, new Coordinate(1, 0));
        assertEquals(expected, groups.get(0));
    }

    @Test
    void testInvalidValues() {
        int[][] image = {
            {1, 2, 0},
            {0, 1, 1}
        };
        // Assuming the method should throw IllegalArgumentException for non-0/1 values
        assertThrows(IllegalArgumentException.class, () -> finder.findConnectedGroups(image));
    }
}