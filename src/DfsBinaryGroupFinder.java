import java.util.ArrayList;
import java.util.List;

public class DfsBinaryGroupFinder implements BinaryGroupFinder {
   /**
    * Finds connected pixel groups of 1s in an integer array representing a binary image.
    * 
    * The input is a non-empty rectangular 2D array containing only 1s and 0s.
    * If the array or any of its subarrays are null, a NullPointerException
    * is thrown. If the array is otherwise invalid, an IllegalArgumentException
    * is thrown.
    *
    * Pixels are considered connected vertically and horizontally, NOT diagonally.
    * The top-left cell of the array (row:0, column:0) is considered to be coordinate
    * (x:0, y:0). Y increases downward and X increases to the right. For example,
    * (row:4, column:7) corresponds to (x:7, y:4).
    *
    * The method returns a list of sorted groups. The group's size is the number 
    * of pixels in the group. The centroid of the group
    * is computed as the average of each of the pixel locations across each dimension.
    * For example, the x coordinate of the centroid is the sum of all the x
    * coordinates of the pixels in the group divided by the number of pixels in that group.
    * Similarly, the y coordinate of the centroid is the sum of all the y
    * coordinates of the pixels in the group divided by the number of pixels in that group.
    * The division should be done as INTEGER DIVISION.
    *
    * The groups are sorted in DESCENDING order according to Group's compareTo method
    * (size first, then x, then y). That is, the largest group will be first, the 
    * smallest group will be last, and ties will be broken first by descending 
    * y value, then descending x value.
    * 
    * @param image a rectangular 2D array containing only 1s and 0s
    * @return the found groups of connected pixels in descending order
    */
    @Override
    public List<Group> findConnectedGroups(int[][] image) {
        if (image==null) throw new NullPointerException();
        if (image.length==0) throw new IllegalArgumentException();
        boolean[][] seen = new boolean[image.length][image[0].length];
        List<Group> groups = new ArrayList<>();
        return findConnectedGroups(image, seen, groups);
    }
    public List<Group> findConnectedGroups(int[][] image, boolean[][] seen, List<Group> groups) {
        int xsum = 0;
        int ysum = 0;
        for (int r = 0; r < image.length; r++) {
            for (int c = 0; c < image[0].length; c++) {
                if (image[r][c]==1) {
                    List<int[]> pixels = new ArrayList<>();
                    List<int[]> pixelsLocations = dfs(image, seen, pixels, r, c);
                    for (int[] location : pixelsLocations) {
                        xsum += location[0];
                        ysum += location[1];
                    }
                    groups.add(new Group(pixels.size(), new Coordinate(xsum/pixels.size(),ysum/pixels.size())));
                }
            }
        }
        return groups;
    }
    
    public static List<int[]> dfs(int[][] image, boolean[][] seen, List<int[]> pixels, int r, int c) {
        int[][] directions = {{-1,0},{1,0},{0,-1},{0,1}};
        if (seen[r][c]) return null;
        seen[r][c] = true;
        pixels.add(new int[]{r,c});
        for (int[] dir : directions) {
            if (r+dir[0]<image.length&&
                r+dir[0]>=0&&
                c+dir[1]<image[0].length&&
                c+dir[1]>=0) return dfs(image, seen, pixels, r+dir[0], c+dir[1]);
        }
        return pixels;
    }
}
