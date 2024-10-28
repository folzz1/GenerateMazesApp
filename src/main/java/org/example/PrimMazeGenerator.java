package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PrimMazeGenerator implements Generator {

    private static final int[] OFFSETS = {0, 2, 0, -2, 2, 0, -2, 0};
    private final Random random = new Random();

    @Override public Maze generate(int height, int width) {
        Cell[][] grid = new Cell[height][width];
        List<Coordinate> frontier = new ArrayList<>();

        MazeUtils.initializeGrid(grid, height, width);

        int startRow = random.nextInt(height);
        int startCol = random.nextInt(width);
        grid[startRow][startCol] = new Cell(startRow, startCol, Cell.Type.PASSAGE);

        addFrontierCells(grid, frontier, startRow, startCol);

        while (!frontier.isEmpty()) {
            Coordinate current = frontier.remove(random.nextInt(frontier.size()));
            int row = current.row();
            int col = current.col();

            List<Coordinate> neighbors = getPassageNeighbors(grid, row, col);
            if (!neighbors.isEmpty()) {
                Coordinate neighbor = neighbors.get(random.nextInt(neighbors.size()));
                connectCells(grid, current, neighbor);
                grid[row][col] = new Cell(row, col, Cell.Type.PASSAGE);

                addFrontierCells(grid, frontier, row, col);
            }
        }

        MazeUtils.setBorders(grid, height, width);

        return new Maze(height, width, grid);
    }

    private void addFrontierCells(Cell[][] grid, List<Coordinate> frontier, int row, int col) {
        for (int i = 0; i < OFFSETS.length; i += 2) {
            int newRow = row + OFFSETS[i];
            int newCol = col + OFFSETS[i + 1];
            if (isValidCell(grid, newRow, newCol) && grid[newRow][newCol].type() == Cell.Type.WALL) {
                frontier.add(new Coordinate(newRow, newCol));
            }
        }
    }

    private List<Coordinate> getPassageNeighbors(Cell[][] grid, int row, int col) {
        List<Coordinate> neighbors = new ArrayList<>();
        for (int i = 0; i < OFFSETS.length; i += 2) {
            int newRow = row + OFFSETS[i];
            int newCol = col + OFFSETS[i + 1];
            if (isValidCell(grid, newRow, newCol) && grid[newRow][newCol].type() == Cell.Type.PASSAGE) {
                neighbors.add(new Coordinate(newRow, newCol));
            }
        }
        return neighbors;
    }

    private void connectCells(Cell[][] grid, Coordinate current, Coordinate neighbor) {
        int row = (current.row() + neighbor.row()) / 2;
        int col = (current.col() + neighbor.col()) / 2;
        grid[row][col] = new Cell(row, col, Cell.Type.PASSAGE);
    }

    private boolean isValidCell(Cell[][] grid, int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }
}
