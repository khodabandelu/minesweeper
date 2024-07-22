package org.macan.minesweeper.game;

/**
 * The GameLauncher class serves as the entry point for launching the Minesweeper game.
 * By default, it launches the Minesweeper game in the CLI (Command-Line Interface) mode.
 */
public class GameLauncher {

    /**
     * The main method to launch the Minesweeper game.
     * It creates an instance of MinesweeperCLI and calls its launch method.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        MinesweeperGame game = new MinesweeperCLI();
        game.launch();
    }
}