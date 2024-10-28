package org.example;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class BFSSolver implements Solver {
    private static final int[] ROW_OFFSETS = {-1, 0, 1, 0};
    private static final int[] COL_OFFSETS = {0, 1, 0, -1};

    @Override
    public List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end) {
        Queue<Coordinate> queue = new LinkedList<>();
        Map<Coordinate, Coordinate> parentMap = new HashMap<>();
        Set<Coordinate> visited = new HashSet<>();

        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Coordinate current = queue.poll();

            if (current.equals(end)) {
                return SolverUtils.reconstructPath(parentMap, start, end);
            }

            for (int i = 0; i < ROW_OFFSETS.length; i++) {
                Coordinate neighbor = new Coordinate(
                        current.row() + ROW_OFFSETS[i],
                        current.col() + COL_OFFSETS[i]
                );

                if (SolverUtils.isValidMove(maze, neighbor) && visited.add(neighbor)) {
                    queue.offer(neighbor);
                    parentMap.put(neighbor, current);
                }
            }
        }

        return Collections.emptyList();
    }
}
