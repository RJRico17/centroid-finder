package io.github.rjrico17.centroidfinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Deque;
import java.util.ArrayDeque;

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
    * The groups are sorted in DESCENDING order according to Group's compareTo method.
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
        for (int r = 0; r < image.length; r++) {
            if (image[r] == null) throw new NullPointerException();
            for (int c = 0; c < image[r].length; c++) {
                if (image[r][c] != 0 && image[r][c] != 1) throw new IllegalArgumentException("Contains non binary values");
                if (image[r][c]==1 && !seen[r][c]) {
                    int xsum = 0;
                    int ysum = 0;
                    List<int[]> pixels = dfs(image, seen, r, c);
                    for (int[] location : pixels) {
                        xsum += location[0];
                        ysum += location[1];
                    }
                    groups.add(new Group(pixels.size(), new Coordinate(xsum/pixels.size(),ysum/pixels.size())));
                }
            }
        }
        return groups;
    }

    public static List<int[]> dfs(int[][] image, boolean[][] seen, int r, int c) {
        List<int[]> pixels = new ArrayList<>();
        return dfs(image, seen, pixels, r, c);
    }


    private static final int[][] direction = {
        {-1, 0}, // up
        { 1, 0}, // down
        { 0,-1}, // left
        { 0, 1}  // right
    };

    public static List < int[] > dfs(int[][] image, boolean[][] seen, List <int[]> pixels, int r, int c) {
        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[] {r,c});
        seen[r][c] = true;

        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int row = current[0];
            int col = current[1];

            // store them as (x,y) = (col, row)
            pixels.add(new int[]{col,row});

            for (int[] d: direction) {
                int nr = row + d[0];
                int nc = col + d[1];
                if (nr >= 0 && nr < image.length && 
                    nc >= 0 && nc < image[0].length &&
                    !seen[nr][nc] && image[nr][nc] == 1) {
                    seen[nr][nc] = true;
                    stack.push(new int[] {nr,nc});
                }
            }
        }
        return pixels;
    }
}
