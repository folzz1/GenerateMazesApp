package org.example;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MazeUtils {

    public static void initializeGrid(Cell[][] grid, int height, int width) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col] = new Cell(row, col, Cell.Type.WALL);
            }
        }
    }

    public static void setBorders(Cell[][] grid, int height, int width) {
        for (int row = 0; row < height; row++) {
            grid[row][0] = new Cell(row, 0, Cell.Type.WALL);
            grid[row][width - 1] = new Cell(row, width - 1, Cell.Type.WALL);
        }
        for (int col = 0; col < width; col++) {
            grid[0][col] = new Cell(0, col, Cell.Type.WALL);
            grid[height - 1][col] = new Cell(height - 1, col, Cell.Type.WALL);
        }
    }
}

