package org.macan.minesweeper.common;

/**
 * The GameInputValidator class provides validation methods for user inputs
 * such as grid dimensions and mine counts.
 */
public class GameInputValidator {

    /**
     * Validates if the provided grid dimension is within the allowed range.
     *
     * @param dimension the grid dimension to validate
     * @return true if the dimension is within the allowed range, false otherwise
     */
    public static boolean isValidGridDimension(int dimension) {
        return dimension >= GameConfig.MIN_GRID_DIMENSION && dimension <= GameConfig.MAX_GRID_DIMENSION;
    }

    /**
     * Validates if the provided mine count is within the allowed range based on the grid size.
     *
     * @param rows the number of rows in the grid
     * @param columns the number of columns in the grid
     * @param mineCount the number of mines to validate
     * @return true if the mine count is within the allowed range, false otherwise
     */
    public static boolean isValidMineCount(int rows, int columns, int mineCount) {
        int maxMines = (int) (GameConfig.DEFAULT_MAX_MINE_PERCENTAGE * rows * columns);
        return mineCount >= 1 && mineCount <= maxMines;
    }
}