import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Stub implementation of Group (adjust based on actual Group definition)
// record Group(int size, int x, int y) implements Comparable<Group> {
//     @Override
//     public int compareTo(Group other) {
//         // Assume descending order by size for sorting
//         return Integer.compare(other.size, this.size);
//     }
// }

// Stub implementation of ImageBinarizer
class StubImageBinarizer implements ImageBinarizer {
    private final int[][] binaryArray;

    StubImageBinarizer(int[][] binaryArray) {
        this.binaryArray = binaryArray;
    }   
    
    @Override
    public BufferedImage toBufferedImage(int[][] image) { 
        return null; 
    }


    @Override
    public int[][] toBinaryArray(BufferedImage image) {
        if (image == null) {
            throw new NullPointerException("Image cannot be null");
        }
        if (image.getWidth() == 0 || image.getHeight() == 0) {
            return new int[0][0];
        }
        return binaryArray;
    }
}

// Stub implementation of BinaryGroupFinder
class StubBinaryGroupFinder implements BinaryGroupFinder {
    private final List<Group> groups;

    StubBinaryGroupFinder(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public List<Group> findConnectedGroups(int[][] binaryArray) {
        if (binaryArray == null) {
            throw new NullPointerException("Binary array cannot be null");
        }
        return groups;
    }
}

public class BinarizingImageGroupFinderTest {

    private BinarizingImageGroupFinder finder;

    @BeforeEach
    void setUp() {
        // Default setup with empty stubs; overridden in specific tests
        finder = new BinarizingImageGroupFinder(
            new StubImageBinarizer(new int[0][0]),
            new StubBinaryGroupFinder(Collections.emptyList())
        );
    }
    

    @Test
    void testConstructorWithValidDependencies() {
        assertNotNull(finder, "BinarizingImageGroupFinder should be instantiated with valid dependencies");
    }

    @Test
    void testFindConnectedGroupsWithValidImage() {
        // Arrange
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        int[][] binaryArray = {
            {1, 1, 0},
            {1, 0, 0},
            {0, 0, 1}
        };
        List<Group> expectedGroups = new ArrayList<>();
        finder = new BinarizingImageGroupFinder(
            new StubImageBinarizer(binaryArray),
            new StubBinaryGroupFinder(expectedGroups)
        );

        // Act
        List<Group> result = finder.findConnectedGroups(image);

        // Assert
        assertEquals(expectedGroups.size(), result.size(), "Should return correct number of groups");
        assertEquals(expectedGroups, result, "Should return the expected groups");
    }

    @Test
    void testFindConnectedGroupsWithNullImage() {
        // Arrange
        finder = new BinarizingImageGroupFinder(
            new StubImageBinarizer(new int[0][0]),
            new StubBinaryGroupFinder(Collections.emptyList())
        );

        // Act & Assert
        assertThrows(NullPointerException.class, () -> finder.findConnectedGroups(null),
                "Should throw NullPointerException for null image");
    }

    @Test
    void testFindConnectedGroupsWithEmptyImage() {
        // Arrange
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        finder = new BinarizingImageGroupFinder(
            new StubImageBinarizer(new int[0][0]),
            new StubBinaryGroupFinder(Collections.emptyList())
        );

        // Act
        List<Group> result = finder.findConnectedGroups(image);

        // Assert
        assertTrue(result.isEmpty(), "Should return an empty list for an empty image");
    }

    @Test
    void testFindConnectedGroupsWithNoWhitePixels() {
        // Arrange
        BufferedImage image = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
        int[][] binaryArray = new int[5][5]; // All zeros
        finder = new BinarizingImageGroupFinder(
            new StubImageBinarizer(binaryArray),
            new StubBinaryGroupFinder(Collections.emptyList())
        );

        // Act
        List<Group> result = finder.findConnectedGroups(image);

        // Assert
        assertTrue(result.isEmpty(), "Should return an empty list when no white pixels are present");
    }

    @Test
    void testFindConnectedGroupsWithSingleWhitePixel() {
        // Arrange
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        int[][] binaryArray = {
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 0}
        };
        List<Group> expectedGroups = new ArrayList<>();
        finder = new BinarizingImageGroupFinder(
            new StubImageBinarizer(binaryArray),
            new StubBinaryGroupFinder(expectedGroups)
        );

        // Act
        List<Group> result = finder.findConnectedGroups(image);

        // Assert
        assertSame(expectedGroups, result, "Should return the same list groupfinder got");
        // assertEquals(expectedGroups.get(0), result.get(0), "Should return the single pixel group");
    }

    // @Test
    // void testFindConnectedGroupsWithSortedGroups() {
    //     // Arrange
    //     BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
    //     int[][] binaryArray = {
    //         {1, 1, 0},
    //         {1, 0, 0},
    //         {0, 0, 1}
    //     };
    //     // Groups are in descending order by size
    //     List<Group> expectedGroups = Arrays.asList(
    //         new Group(3, 0, 0), // Larger group
    //         new Group(1, 2, 2)  // Smaller group
    //     );
    //     finder = new BinarizingImageGroupFinder(
    //         new StubImageBinarizer(binaryArray),
    //         new StubBinaryGroupFinder(expectedGroups)
    //     );

    //     // Act
    //     List<Group> result = finder.findConnectedGroups(image);

    //     // Assert
    //     assertEquals(expectedGroups, result, "Should return groups in descending order by size");
    //     assertEquals(3, result.get(0).size(), "First group should have size 3");
    //     assertEquals(1, result.get(1).size(), "Second group should have size 1");
    // }

    @Test
    void wiresBinarizerToGroupFinder_andReturnsSameListInstance() {
        BufferedImage input = new BufferedImage(3, 2, BufferedImage.TYPE_INT_RGB);

        int[][] expectedBinary = new int[][] {
            {1, 0, 1},
            {0, 1, 0}
        };

        // Always returns expectedBinary
        ImageBinarizer binarizer = new ImageBinarizer() {
            @Override public int[][] toBinaryArray(BufferedImage image) { 
                assertSame(input, image, "Binarizer must receive the same BufferedImage");
                return expectedBinary; 
            }
            @Override public BufferedImage toBufferedImage(int[][] image) { return null; }
        };

        List<Group> expectedGroups = new ArrayList<>();
        final boolean[] sawCall = { false };
        BinaryGroupFinder groupFinder = new BinaryGroupFinder() {
            @Override public List<Group> findConnectedGroups(int[][] binary) {
                sawCall[0] = true;
                assertSame(expectedBinary, binary, "GroupFinder must receive the binarizer's exact array");
                return expectedGroups;
            }
        };

        BinarizingImageGroupFinder sut = new BinarizingImageGroupFinder(binarizer, groupFinder);

        List<Group> actual = sut.findConnectedGroups(input);

        assertTrue(sawCall[0], "GroupFinder should be called");
        assertSame(expectedGroups, actual, "Should return exactly the list from GroupFinder");
    }

    @Test
    void propagatesIfBinarizerThrows_andDoesNotCallGroupFinder() {
        ImageBinarizer binarizer = new ImageBinarizer() {
            @Override public int[][] toBinaryArray(BufferedImage image) { 
                throw new RuntimeException("binarize boom"); 
            }
            @Override public BufferedImage toBufferedImage(int[][] image) { return null; }
        };

        final boolean[] gfCalled = { false };
        BinaryGroupFinder groupFinder = new BinaryGroupFinder() {
            @Override public List<Group> findConnectedGroups(int[][] image) {
                gfCalled[0] = true;
                return List.of();
            }
        };

        BinarizingImageGroupFinder sut = new BinarizingImageGroupFinder(binarizer, groupFinder);

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> sut.findConnectedGroups(new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB)));
        assertEquals("binarize boom", ex.getMessage());
        assertFalse(gfCalled[0], "GroupFinder must NOT be called if binarizer fails");
    }

    @Test
    void propagatesIfGroupFinderThrows_afterSuccessfulBinarize() {
        int[][] binary = new int[][] { {1} };

        ImageBinarizer binarizer = new ImageBinarizer() {
            @Override public int[][] toBinaryArray(BufferedImage image) { return binary; }
            @Override public BufferedImage toBufferedImage(int[][] image) { return null; }
        };

        BinaryGroupFinder groupFinder = new BinaryGroupFinder() {
            @Override public List<Group> findConnectedGroups(int[][] image) {
                assertSame(binary, image, "GroupFinder must receive the exact array from binarizer");
                throw new IllegalStateException("group boom");
            }
        };

        BinarizingImageGroupFinder sut = new BinarizingImageGroupFinder(binarizer, groupFinder);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
            () -> sut.findConnectedGroups(new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB)));
        assertEquals("group boom", ex.getMessage());
    }

    @Test
    void returnsEmptyListWhenGroupFinderReturnsEmpty() {
        ImageBinarizer binarizer = new ImageBinarizer() {
            @Override public int[][] toBinaryArray(BufferedImage image) { return new int[][] {{0,0},{0,0}}; }
            @Override public BufferedImage toBufferedImage(int[][] image) { return null; }
        };

        BinaryGroupFinder groupFinder = new BinaryGroupFinder() {
            @Override public List<Group> findConnectedGroups(int[][] image) { return List.of(); }
        };

        BinarizingImageGroupFinder sut = new BinarizingImageGroupFinder(binarizer, groupFinder);

        List<Group> out = sut.findConnectedGroups(new BufferedImage(2,2,BufferedImage.TYPE_INT_RGB));
        assertNotNull(out);
        assertTrue(out.isEmpty(), "Should return an empty list when group finder returns empty");
    }

    @Test
    void nullImage_propagatesFromBinarizer() {
        ImageBinarizer binarizer = new ImageBinarizer() {
            @Override public int[][] toBinaryArray(BufferedImage image) {
                if (image == null) throw new NullPointerException("image is null");
                return new int[][] {{0}};
            }
            @Override public BufferedImage toBufferedImage(int[][] image) { return null; }
        };

        BinaryGroupFinder groupFinder = new BinaryGroupFinder() {
            @Override public List<Group> findConnectedGroups(int[][] image) { return List.of(); }
        };

        BinarizingImageGroupFinder sut = new BinarizingImageGroupFinder(binarizer, groupFinder);

        assertThrows(NullPointerException.class, () -> sut.findConnectedGroups(null));
    }




    
}