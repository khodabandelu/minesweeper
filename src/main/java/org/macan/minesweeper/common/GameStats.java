package org.macan.minesweeper.common;


/**
 * The GameStats class tracks and manages statistics for a game of Minesweeper.
 * It records the number of moves made, mines uncovered, game start and end times, and the game result.
 */
public class GameStats {
    private int movesMade;
    private int minesUncovered;
    private final long startTime;
    private long endTime;
    private GameResult gameResult;

    /**
     * Constructor for creating a GameStats object.
     * Initializes the game statistics and sets the start time to the current system time.
     */
    public GameStats() {
        this.movesMade = 0;
        this.minesUncovered = 0;
        this.startTime = System.currentTimeMillis();
        this.gameResult = GameResult.IN_PROGRESS;
    }

    /**
     * Increments the number of moves made by the player.
     */
    public void incrementMovesMade() {
        this.movesMade++;
    }

    /**
     * Increments the number of mines uncovered by the player.
     */
    public void incrementMinesUncovered() {
        this.minesUncovered++;
    }

    /**
     * Increments the number of mines uncovered by the player.
     */
    public void decrementMinesUncovered() {
        this.minesUncovered--;
    }

    /**
     * Ends the game and records the end time and result.
     *
     * @param result the result of the game (WON, LOST, or IN_PROGRESS)
     */
    public void endGame(GameResult result) {
        this.endTime = System.currentTimeMillis();
        this.gameResult = result;
    }

    /**
     * Gets the number of moves made by the player.
     *
     * @return the number of moves made
     */
    public int getMovesMade() {
        return movesMade;
    }

    /**
     * Gets the number of mines uncovered by the player.
     *
     * @return the number of mines uncovered
     */
    public int getMinesUncovered() {
        return minesUncovered;
    }

    /**
     * Gets the total time elapsed from the start to the end of the game.
     *
     * @return the total time in milliseconds
     */
    public long getTotalTime() {
        return endTime - startTime;
    }


    /**
     * Gets the total time elapsed from the start to the current of the game.
     *
     * @return the total time in milliseconds
     */
    public long getTotalCurrentTime() {
        return System.currentTimeMillis() - startTime;
    }

    /**
     * Gets the result of the game.
     *
     * @return the game result (WON, LOST, or IN_PROGRESS)
     */
    public GameResult getGameResult() {
        return gameResult;
    }
}