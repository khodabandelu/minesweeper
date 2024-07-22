package org.macan.minesweeper.game;

import org.macan.minesweeper.common.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * The MinesweeperCLI class implements the Minesweeper game for the command-line interface.
 * It handles user input for grid size and mine count, initializes the game board,
 * and manages the game loop, including user moves and displaying the game state.
 */
public class MinesweeperCLI implements MinesweeperGame {
    private final Scanner scanner;
    private GameBoard board;
    private GameStats gameStats;

    /**
     * Constructs a MinesweeperCLI object and initializes the Scanner.
     */
    public MinesweeperCLI() {
        scanner = new Scanner(System.in);
    }

    /**
     * Launches the Minesweeper game by starting a new game.
     */
    @Override
    public void launch() {
        try {
            startNewGame();
        } finally {
            scanner.close();
        }
    }

    /**
     * Starts a new game by prompting the user for grid size and mine count,
     * initializing the game board, and managing the game loop.
     */
    private void startNewGame() {
        System.out.println("Welcome to Minesweeper!");

        while (true) {
            // Get game settings from user
            int gridSize = promptGridSize();
            int mineCount = promptMineCount(gridSize);

            // Initialize the board and game stats
            Map<String, GridCell> grid = new HashMap<>();
            Set<String> mineKeys = new HashSet<>();
            gameStats = new GameStats();

            board = new GameBoard(gridSize, gridSize, mineCount, grid, mineKeys, gameStats);
            board.initializeGame();
            displayBoard();

            while (gameStats.getGameResult() == GameResult.IN_PROGRESS) {
                String move = makeMove();
                int[] moveCoordinates = parseMove(move);
                if (moveCoordinates != null) {
                    board.revealCell(moveCoordinates[0], moveCoordinates[1]);
                    gameStats.incrementMovesMade();
                    displayBoard();
                }

                if (gameStats.getGameResult() == GameResult.LOST) {
                    System.out.println("Oh no, you detonated a mine! Game over.");
                } else if (gameStats.getGameResult() == GameResult.WON) {
                    System.out.println("Congratulations, you have won the game!");
                }

                displayStats();
            }

            System.out.println("Press any key to play again or type 'exit' to quit.");
            String input = scanner.nextLine();
            if ("exit".equalsIgnoreCase(input)) {
                System.out.println("Thank you for playing Minesweeper!");
                break;
            }
        }
    }

    /**
     * Prompts the user to enter the grid size for the game.
     * @return the grid size entered by the user.
     */
    private int promptGridSize() {
        int size;
        while (true) {
            System.out.print("Enter the grid size (both rows and columns) (" + GameConfig.MIN_GRID_DIMENSION + "-" + GameConfig.MAX_GRID_DIMENSION + "): ");
            try {
                size = Integer.parseInt(scanner.nextLine());
                if (GameInputValidator.isValidGridDimension(size)) {
                    break;
                } else {
                    System.out.println("Grid size must be between " + GameConfig.MIN_GRID_DIMENSION + " and " + GameConfig.MAX_GRID_DIMENSION + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Incorrect input.");
            }
        }
        return size;
    }

    /**
     * Prompts the user to enter the number of mines for the game based on the grid size.
     * @param gridSize the size of the grid.
     * @return the number of mines entered by the user.
     */
    private int promptMineCount(int gridSize) {
        int mineCount;
        while (true) {
            System.out.print("Enter the number of mines to place on the grid (maximum is " + (int) (GameConfig.DEFAULT_MAX_MINE_PERCENTAGE * gridSize * gridSize) + "): ");
            try {
                mineCount = Integer.parseInt(scanner.nextLine());
                if (GameInputValidator.isValidMineCount(gridSize, gridSize, mineCount)) {
                    break;
                } else {
                    System.out.println("Number of mines must be between 1 and " + (int) (GameConfig.DEFAULT_MAX_MINE_PERCENTAGE * gridSize * gridSize) + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Incorrect input.");
            }
        }
        return mineCount;
    }

    /**
     * Prompts the user to enter their move.
     * @return the move entered by the user.
     */
    private String makeMove() {
        System.out.print("Enter your move (e.g., A1, B2): ");
        return scanner.nextLine();
    }

    /**
     * Displays the current state of the game board.
     */
    private void displayBoard() {
        int rows = board.getGridRows();
        int columns = board.getGridColumns();

        System.out.print("  ");
        for (int i = 1; i <= columns; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < rows; i++) {
            System.out.print((char) ('A' + i) + " ");
            for (int j = 0; j < columns; j++) {
                GridCell cell = board.getGridCell(i, j);
                if (cell != null && cell.isRevealed()) {
                    if (cell.isMine()) {
                        System.out.print("X ");
                    } else if (cell.getAdjacentMines() > 0) {
                        System.out.print(cell.getAdjacentMines() + " ");
                    } else {
                        System.out.print("0 ");
                    }
                } else {
                    System.out.print("_ ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Displays the game statistics, including moves made, mines uncovered, total time, and game result.
     */
    private void displayStats() {
        System.out.println("Game Stats:");
        System.out.println("Moves made: " + gameStats.getMovesMade());
        System.out.println("Mines uncovered: " + gameStats.getMinesUncovered());
        System.out.println("Total time: " + (gameStats.getTotalCurrentTime() / 1000) + " seconds");
        System.out.println("Game result: " + gameStats.getGameResult());
    }

    /**
     * Parses the user's move input into row and column indices.
     * @param move the move input entered by the user.
     * @return an array containing the row and column indices of the move, or null if the input is invalid.
     */
    private int[] parseMove(String move) {
        try {
            int row = move.charAt(0) - 'A';
            int col = Integer.parseInt(move.substring(1)) - 1;
            return new int[]{row, col};
        } catch (Exception e) {
            System.out.println("Invalid move format. Please use the format 'A1'.");
            return null;
        }
    }

    /**
     * Main method to launch the MinesweeperCLI game.
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        MinesweeperCLI cli = new MinesweeperCLI();
        cli.launch();
    }
}