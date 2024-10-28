package org.example;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConsoleRenderer implements Renderer {

    @Override
    public String render(Maze maze) {
        StringBuilder sb = new StringBuilder();
        Cell[][] grid = maze.getGrid();

        for (int row = 0; row < maze.getHeight(); row++) {
            for (int col = 0; col < maze.getWidth(); col++) {
                Cell cell = grid[row][col];
                sb.append((cell == null || cell.type() == Cell.Type.WALL) ? "#" : " ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    @Override
    public String render(Maze maze, List<Coordinate> path) {
        StringBuilder sb = new StringBuilder();
        Cell[][] grid = maze.getGrid();

        Set<Coordinate> pathSet = new HashSet<>(path);

        for (int row = 0; row < maze.getHeight(); row++) {
            for (int col = 0; col < maze.getWidth(); col++) {
                Coordinate coord = new Coordinate(row, col);
                if (pathSet.contains(coord)) {
                    sb.append("*");
                } else {
                    Cell cell = grid[row][col];
                    sb.append((cell == null || cell.type() == Cell.Type.WALL) ? "#" : " ");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
