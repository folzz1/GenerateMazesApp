package org.example;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class AStarSolver implements Solver {
    private static final int[] ROW_OFFSETS = {-1, 0, 1, 0};
    private static final int[] COL_OFFSETS = {0, 1, 0, -1};

    @Override
    public List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end) {
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(node -> node.f));
        Map<Coordinate, Coordinate> parentMap = new HashMap<>();
        Map<Coordinate, Integer> gScores = new HashMap<>();
        Set<Coordinate> closedList = new HashSet<>();

        gScores.put(start, 0);
        openList.offer(new Node(start, 0, heuristic(start, end)));

        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();
            Coordinate current = currentNode.coord;

            if (current.equals(end)) {
                return SolverUtils.reconstructPath(parentMap, start, end);
            }

            if (closedList.contains(current)) {
                continue;
            }

            closedList.add(current);

            for (int i = 0; i < ROW_OFFSETS.length; i++) {
                Coordinate neighbor = new Coordinate(
                        current.row() + ROW_OFFSETS[i],
                        current.col() + COL_OFFSETS[i]
                );

                if (!SolverUtils.isValidMove(maze, neighbor) || closedList.contains(neighbor)) {
                    continue;
                }

                int tentativeG = gScores.getOrDefault(current, Integer.MAX_VALUE) + 1;

                if (tentativeG < gScores.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    parentMap.put(neighbor, current);
                    gScores.put(neighbor, tentativeG);
                    int f = tentativeG + heuristic(neighbor, end);
                    openList.offer(new Node(neighbor, tentativeG, f));
                }
            }
        }

        return Collections.emptyList();
    }

    private int heuristic(Coordinate a, Coordinate b) {
        return Math.abs(a.row() - b.row()) + Math.abs(a.col() - b.col());
    }

    private static class Node {
        Coordinate coord;
        int g;
        int f;

        Node(Coordinate coord, int g, int f) {
            this.coord = coord;
            this.g = g;
            this.f = f;
        }
    }
}
