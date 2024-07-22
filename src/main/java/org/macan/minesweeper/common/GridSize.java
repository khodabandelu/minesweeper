package org.macan.minesweeper.common;

/**
 * The GridSize enum represents different grid sizes for the Minesweeper game.
 * Each grid size has a name, number of rows, and number of columns.
 */
public enum GridSize {
    SMALL("Small", 8, 10),
    MEDIUM("Medium", 14, 18),
    LARGE("Large", 20, 24);

    private final String name;
    private final int rows;
    private final int cols;

    GridSize(String name, int rows, int cols) {
        this.name = name;
        this.rows = rows;
        this.cols = cols;
    }

    /**
     * Gets the name of the grid size.
     *
     * @return the name of the grid size
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the number of rows in the grid.
     *
     * @return the number of rows in the grid
     */
    public int getRows() {
        return rows;
    }

    /**
     * Gets the number of columns in the grid.
     *
     * @return the number of columns in the grid
     */
    public int getCols() {
        return cols;
    }

    @Override
    public String toString() {
        return name + " (" + rows + "x" + cols + ")";
    }
}