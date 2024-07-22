package org.macan.minesweeper.game;

/**
 * The MinesweeperGame interface defines a contract for launching the Minesweeper game.
 * It can be implemented by various classes to provide different user interfaces
 * for the game, such as a command-line interface or a graphical user interface.
 */
public interface MinesweeperGame {
    /**
     * Launches the Minesweeper game.
     */
    void launch();
}