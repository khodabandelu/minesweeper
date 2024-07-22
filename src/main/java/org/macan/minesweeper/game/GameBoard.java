package org.macan.minesweeper.game;

import org.macan.minesweeper.common.GameResult;
import org.macan.minesweeper.common.GameStats;
import org.macan.minesweeper.common.GridCell;

import java.util.*;

/**
 * The GameBoard class represents the game board for the Minesweeper game.
 * It handles the initialization of the game board, placing of mines, calculation of adjacent mine numbers,
 * and revealing of cells during gameplay.
 */
public class GameBoard {
    private final int gridRows;
    private final int gridColumns;
    private final int totalMines;
    private final Map<String, GridCell> grid;
    private final Set<String> mineKeys;
    private int revealedCount;
    private final GameStats gameStats;

    /**
     * Constructor for creating a GameBoard object.
     *
     * @param gridRows    the number of rows in the grid
     * @param gridColumns the number of columns in the grid
     * @param totalMines  the total number of mines to be placed on the grid
     * @param grid        the map representing the grid of cells
     * @param mineKeys    the set of keys representing the mine locations
     * @param gameStats   the object representing the game statistics
     */
    public GameBoard(int gridRows, int gridColumns, int totalMines, Map<String, GridCell> grid, Set<String> mineKeys, GameStats gameStats) {
        this.gridRows = gridRows;
        this.gridColumns = gridColumns;
        this.totalMines = totalMines;
        this.grid = grid;
        this.mineKeys = mineKeys;
        this.revealedCount = 0;
        this.gameStats = gameStats;
    }

    /**
     * Initializes the game by placing mines and calculating the number of adjacent mines for each cell.
     */
    public void initializeGame() {
        placeMines();
        calculateMineNumbers();
    }

    /**
     * Places mines randomly on the grid.
     */
    private void placeMines() {
        Random rand = new Random();
        int placedMines = 0;
        while (placedMines < totalMines) {
            int row = rand.nextInt(gridRows);
            int col = rand.nextInt(gridColumns);
            GridCell cell = getGridCell(row, col);
            if (!cell.isMine()) {
                cell.setMine(true);
                mineKeys.add(getGridKey(row, col));
                placedMines++;
            }
        }
    }

    /**
     * Calculates the number of adjacent mines for each cell on the grid.
     */
    private void calculateMineNumbers() {
        for (String mineKey : mineKeys) {
            String[] parts = mineKey.split("_");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            updateAdjacentCells(row, col);
        }
    }

    /**
     * Updates the adjacent cells of a given mine cell to increment their adjacent mine count.
     *
     * @param mineRow the row index of the mine cell
     * @param mineCol the column index of the mine cell
     */
    private void updateAdjacentCells(int mineRow, int mineCol) {
        for (int i = mineRow - 1; i <= mineRow + 1; i++) {
            for (int j = mineCol - 1; j <= mineCol + 1; j++) {
                if (i >= 0 && i < gridRows && j >= 0 && j < gridColumns && !(i == mineRow && j == mineCol)) {
                    GridCell cell = getGridCell(i, j);
                    cell.incrementAdjacentMines();
                    grid.put(getGridKey(i, j), cell);
                }
            }
        }
    }

    /**
     * Reveals the cell at the specified position and propagates the reveal if the cell has no adjacent mines.
     *
     * @param row the row index of the cell to reveal
     * @param col the column index of the cell to reveal
     */
    public void revealCell(int row, int col) {
        if (row < 0 || row >= gridRows || col < 0 || col >= gridColumns) {
            return;
        }
        GridCell cell = getGridCell(row, col);
        if (cell.isRevealed()) {
            return;
        }
        Queue<GridCell> queue = new LinkedList<>();
        queue.add(cell);
        while (!queue.isEmpty()) {
            cell = queue.poll();
            int r = cell.getRow();
            int c = cell.getCol();
            if (cell.isRevealed()) {
                continue;
            }
            cell.setRevealed(true);
            revealedCount++;
            if (cell.isMine()) {
                gameStats.incrementMinesUncovered();
                gameStats.endGame(GameResult.LOST);
                revealAllCells();
                return;
            }
            if (cell.getAdjacentMines() == 0) {
                for (int i = r - 1; i <= r + 1; i++) {
                    for (int j = c - 1; j <= c + 1; j++) {
                        if (i >= 0 && i < gridRows && j >= 0 && j < gridColumns && !getGridCell(i, j).isRevealed()) {
                            queue.add(getGridCell(i, j));
                        }
                    }
                }
            }
        }
        if (revealedCount == (gridRows * gridColumns - totalMines)) {
            gameStats.endGame(GameResult.WON);
            revealAllCells();
        }
    }

    /**
     * Reveals all cells for end of the game.
     */
    private void revealAllCells() {
        grid.values().forEach(cell -> cell.setRevealed(true));
    }

    /**
     * Gets the key for a cell based on its row and column indices.
     *
     * @param row the row index of the cell
     * @param col the column index of the cell
     * @return the key representing the cell
     */
    String getGridKey(int row, int col) {
        return row + "_" + col;
    }

    /**
     * Gets the GridCell object for the specified row and column.
     *
     * @param row the row index of the cell
     * @param col the column index of the cell
     * @return the GridCell object
     */
    GridCell getGridCell(int row, int col) {
        String key = getGridKey(row, col);
        return grid.computeIfAbsent(key, k -> new GridCell(row, col));
    }

    /**
     * Gets the number of rows in the grid.
     *
     * @return the number of rows in the grid
     */
    public int getGridRows() {
        return gridRows;
    }

    /**
     * Gets the number of columns in the grid.
     *
     * @return the number of columns in the grid
     */
    public int getGridColumns() {
        return gridColumns;
    }

    /**
     * Gets the GameStats object representing the game statistics.
     *
     * @return the GameStats object
     */
    public GameStats getGameStats() {
        return gameStats;
    }

    /**
     * Gets the total of mines in the grid.
     *
     * @return the total of mines in the grid
     */
    public int getTotalMines() {
        return totalMines;
    }
}
