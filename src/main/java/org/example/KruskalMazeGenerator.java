package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KruskalMazeGenerator implements Generator {

    private final Random random = new Random();

    @Override
    public Maze generate(int height, int width) {
        Cell[][] grid = new Cell[height][width];
        List<Edge> edges = new ArrayList<>();
        DisjointSet disjointSet = new DisjointSet(height * width);

        MazeUtils.initializeGrid(grid, height, width);

        for (int row = 1; row < height; row += 2) {
            for (int col = 1; col < width; col += 2) {
                grid[row][col] = new Cell(row, col, Cell.Type.PASSAGE);
                if (row + 2 < height) {
                    edges.add(new Edge(row, col, row + 2, col));
                }
                if (col + 2 < width) {
                    edges.add(new Edge(row, col, row, col + 2));
                }
            }
        }

        Collections.shuffle(edges, random);

        for (Edge edge : edges) {
            int set1 = disjointSet.find(edge.row1 * width + edge.col1);
            int set2 = disjointSet.find(edge.row2 * width + edge.col2);

            if (set1 != set2) {
                disjointSet.union(set1, set2);
                int middleRow = (edge.row1 + edge.row2) / 2;
                int middleCol = (edge.col1 + edge.col2) / 2;
                grid[middleRow][middleCol] = new Cell(middleRow, middleCol, Cell.Type.PASSAGE);
            }
        }

        MazeUtils.setBorders(grid, height, width);

        return new Maze(height, width, grid);
    }

    private static class Edge {
        int row1;
        int col1;
        int row2;
        int col2;

        Edge(int row1, int col1, int row2, int col2) {
            this.row1 = row1;
            this.col1 = col1;
            this.row2 = row2;
            this.col2 = col2;
        }
    }

    private static class DisjointSet {
        private final int[] parent;

        DisjointSet(int size) {
            parent = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) {
                parent[rootX] = rootY;
            }
        }
    }
}
