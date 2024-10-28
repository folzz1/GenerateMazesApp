package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

public class Menu {

    private static final int MIN_DIMENSION = 10;
    private static final int MAX_DIMENSION = 200;
    private static final String DIMENSION_ERROR = " должна быть в пределах от ";
    private static final String NOT_FOUND_ERROR = "Не удалось найти подходящий ";
    private static final int TOP_LEFT = 1;
    private static final int TOP_RIGHT = 2;
    private static final int BOTTOM_LEFT = 3;
    private static final int BOTTOM_RIGHT = 4;

    private final Generator kruskalGenerator = new KruskalMazeGenerator();
    private final Generator primGenerator = new PrimMazeGenerator();
    private final Solver aStarSolver = new AStarSolver();
    private final Solver bfsSolver = new BFSSolver();
    private final Renderer renderer = new ConsoleRenderer();

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final PrintStream out = System.out;

    public void start() {
        try {
            int generatorChoice = getGeneratorChoice();
            int height = getDimension("высоту");
            int width = getDimension("ширину");

            Maze maze = generateMaze(generatorChoice, height, width);

            out.println("Сгенерированный лабиринт:");
            out.println(renderer.render(maze));

            int solverChoice = getSolverChoice();
            Coordinate start = chooseCoordinate(maze, "вход");
            Coordinate end = chooseCoordinate(maze, "выход");

            List<Coordinate> path = solveMaze(solverChoice, maze, start, end);

            displayMazeAndPath(maze, path);
        } catch (IOException e) {
            out.println("Ошибка ввода/вывода: " + e.getMessage());
        }
    }

    private int getGeneratorChoice() throws IOException {
        out.println("Выберите алгоритм генерации лабиринта:");
        out.println("1. Алгоритм Краскала");
        out.println("2. Алгоритм Прима");
        return readInt();
    }

    private int getDimension(String dimensionName) throws IOException {
        int dimension;
        do {
            out.println("Введите " + dimensionName + " лабиринта (от " + MIN_DIMENSION
                    + DIMENSION_ERROR + MAX_DIMENSION + "):");
            dimension = readInt();
            if (dimension < MIN_DIMENSION || dimension > MAX_DIMENSION) {
                out.println("Ошибка: " + dimensionName + DIMENSION_ERROR + MIN_DIMENSION
                        + " до " + MAX_DIMENSION + ".");
            }
        } while (dimension < MIN_DIMENSION || dimension > MAX_DIMENSION);
        return dimension;
    }

    private int readInt() throws IOException {
        while (true) {
            try {
                return Integer.parseInt(reader.readLine().trim());
            } catch (NumberFormatException e) {
                out.println("Ошибка: введите корректное число.");
            }
        }
    }

    private Maze generateMaze(int choice, int height, int width) {
        if (choice == 1) {
            return kruskalGenerator.generate(height, width);
        } else if (choice == 2) {
            return primGenerator.generate(height, width);
        } else {
            throw new IllegalArgumentException("Неверный выбор генератора");
        }
    }

    private int getSolverChoice() throws IOException {
        out.println("Выберите алгоритм поиска пути:");
        out.println("1. A*");
        out.println("2. BFS");
        return readInt();
    }

    private Coordinate chooseCoordinate(Maze maze, String type) throws IOException {
        out.println("Выберите, где будет " + type + ":");
        out.println("1. Слева сверху");
        out.println("2. Справа сверху");
        out.println("3. Слева снизу");
        out.println("4. Справа снизу");
        int choice = readInt();

        return findCoordinate(maze, choice, type);
    }

    private Coordinate findCoordinate(Maze maze, int choice, String type) {
        switch (choice) {
            case TOP_LEFT:
                return findCoordinateFromTopLeft(maze, type);
            case TOP_RIGHT:
                return findCoordinateFromTopRight(maze, type);
            case BOTTOM_LEFT:
                return findCoordinateFromBottomLeft(maze, type);
            case BOTTOM_RIGHT:
                return findCoordinateFromBottomRight(maze, type);
            default:
                throw new IllegalArgumentException("Неверный выбор");
        }
    }

    private Coordinate findCoordinateFromTopLeft(Maze maze, String type) {
        for (int row = 1; row < maze.getHeight() - 1; row++) {
            if (maze.getGrid()[row][1].type() == Cell.Type.PASSAGE) {
                return new Coordinate(row, 1);
            }
        }
        for (int col = 1; col < maze.getWidth() - 1; col++) {
            if (maze.getGrid()[1][col].type() == Cell.Type.PASSAGE) {
                return new Coordinate(1, col);
            }
        }
        throw new IllegalStateException(NOT_FOUND_ERROR + type);
    }

    private Coordinate findCoordinateFromTopRight(Maze maze, String type) {
        for (int row = 1; row < maze.getHeight() - 1; row++) {
            if (maze.getGrid()[row][maze.getWidth() - 2].type() == Cell.Type.PASSAGE) {
                return new Coordinate(row, maze.getWidth() - 2);
            }
        }
        for (int col = maze.getWidth() - 2; col > 0; col--) {
            if (maze.getGrid()[1][col].type() == Cell.Type.PASSAGE) {
                return new Coordinate(1, col);
            }
        }
        throw new IllegalStateException(NOT_FOUND_ERROR + type);
    }

    private Coordinate findCoordinateFromBottomLeft(Maze maze, String type) {
        for (int row = maze.getHeight() - 2; row > 0; row--) {
            if (maze.getGrid()[row][1].type() == Cell.Type.PASSAGE) {
                return new Coordinate(row, 1);
            }
        }
        for (int col = 1; col < maze.getWidth() - 1; col++) {
            if (maze.getGrid()[maze.getHeight() - 2][col].type() == Cell.Type.PASSAGE) {
                return new Coordinate(maze.getHeight() - 2, col);
            }
        }
        throw new IllegalStateException(NOT_FOUND_ERROR + type);
    }

    private Coordinate findCoordinateFromBottomRight(Maze maze, String type) {
        for (int row = maze.getHeight() - 2; row > 0; row--) {
            if (maze.getGrid()[row][maze.getWidth() - 2].type() == Cell.Type.PASSAGE) {
                return new Coordinate(row, maze.getWidth() - 2);
            }
        }
        for (int col = maze.getWidth() - 2; col > 0; col--) {
            if (maze.getGrid()[maze.getHeight() - 2][col].type() == Cell.Type.PASSAGE) {
                return new Coordinate(maze.getHeight() - 2, col);
            }
        }
        throw new IllegalStateException(NOT_FOUND_ERROR + type);
    }

    private List<Coordinate> solveMaze(int choice, Maze maze, Coordinate start, Coordinate end) {
        if (choice == 1) {
            return aStarSolver.solve(maze, start, end);
        } else if (choice == 2) {
            return bfsSolver.solve(maze, start, end);
        } else {
            throw new IllegalArgumentException("Неверный выбор алгоритма поиска");
        }
    }

    private void displayMazeAndPath(Maze maze, List<Coordinate> path) {
        out.println("Лабиринт с путём:");
        out.println(renderer.render(maze, path));
    }
}
