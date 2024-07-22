package org.macan.minesweeper.common;


/**
 * The GridCell class represents a single cell in the Minesweeper game grid.
 * It contains information about the cell's position, whether it contains a mine,
 * whether it has been revealed, and the number of adjacent mines.
 */
public class GridCell {
    private final int row;
    private final int col;
    private boolean isMine;
    private boolean isRevealed;
    private boolean isFlagged;
    private int adjacentMines;

    /**
     * Constructor for creating a GridCell object.
     * Initializes the cell's position and sets default values for mine, revealed, and adjacent mines.
     *
     * @param row the row index of the cell
     * @param col the column index of the cell
     */
    public GridCell(int row, int col) {
        this.row = row;
        this.col = col;
        this.isMine = false;
        this.isRevealed = false;
        this.adjacentMines = 0;
    }

    /**
     * Gets the row index of the cell.
     *
     * @return the row index
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column index of the cell.
     *
     * @return the column index
     */
    public int getCol() {
        return col;
    }

    /**
     * Checks if the cell contains a mine.
     *
     * @return true if the cell contains a mine, false otherwise
     */
    public boolean isMine() {
        return isMine;
    }

    /**
     * Sets whether the cell contains a mine.
     *
     * @param mine true if the cell should contain a mine, false otherwise
     */
    public void setMine(boolean mine) {
        isMine = mine;
    }

    /**
     * Checks if the cell has been revealed.
     *
     * @return true if the cell has been revealed, false otherwise
     */
    public boolean isRevealed() {
        return isRevealed;
    }

    /**
     * Sets whether the cell has been revealed.
     *
     * @param revealed true if the cell should be revealed, false otherwise
     */
    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    /**
     * Gets the number of adjacent mines.
     *
     * @return the number of adjacent mines
     */
    public int getAdjacentMines() {
        return adjacentMines;
    }

    /**
     * Increments the count of adjacent mines by one.
     */
    public void incrementAdjacentMines() {
        this.adjacentMines++;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }
}