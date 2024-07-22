package org.macan.minesweeper.common;

/**
 * The Difficulty enum represents different difficulty levels for the Minesweeper game.
 * Each difficulty level has a corresponding mine factor, which determines the proportion of mines on the grid.
 */
public enum GameDifficulty {
    EASY(0.15),
    MEDIUM(0.25),
    HARD(0.35);

    private final double mineFactor;

    GameDifficulty(double mineFactor) {
        this.mineFactor = mineFactor;
    }

    /**
     * Gets the mine factor for the difficulty level.
     *
     * @return the mine factor
     */
    public double getMineFactor() {
        return mineFactor;
    }

    @Override
    public String toString() {
        return name() + " (" + (int)(mineFactor * 100) + "% mines)";
    }
}