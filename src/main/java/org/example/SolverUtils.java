package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SolverUtils {

    public static boolean isValidMove(Maze maze, Coordinate coord) {
        int row = coord.row();
        int col = coord.col();
        return row >= 0 && row < maze.getHeight() && col >= 0 && col < maze.getWidth()
                && maze.getGrid()[row][col].type() == Cell.Type.PASSAGE;
    }

    public static List<Coordinate> reconstructPath(
            Map<Coordinate, Coordinate> parentMap,
            Coordinate start,
            Coordinate end
    ) {
        List<Coordinate> path = new ArrayList<>();
        Coordinate current = end;
        while (!current.equals(start)) {
            path.add(current);
            current = parentMap.get(current);
        }
        path.add(start);
        Collections.reverse(path);
        return path;
    }
}
