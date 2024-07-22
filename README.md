# Minesweeper App

## Introduction

This project simulates a Minesweeper game on a square grid. The game can be played through both a command-line interface (CLI) and a graphical user interface (GUI). The application is designed to be simple and intuitive, following clean code principles and adhering to SOLID design principles.

## Features

- **Command-Line Interface (CLI)**:
    - Prompts user for grid size and number of mines.
    - Allows user to uncover cells and flag potential mines.
    - Automatically uncovers cells with no adjacent mines.
    - Displays the game board after each move.
    - Tracks and displays game statistics.

- **Graphical User Interface (GUI)**:
    - Allows selection of grid size and difficulty.
    - Allows user to uncover cells and flag potential mines with mouse clicks.
    - Highlights cells on hover.
    - Displays the game board visually with colors and labels.
    - Tracks and displays game statistics in a status bar.

## Design and Assumptions

### Design

The application follows an object-oriented design with a clear separation of concerns. The main components are:

- **GameBoard**: Manages the state of the game board, including the placement of mines and the uncovering of squares.
- **GameStats**: Tracks the statistics of the game, such as the number of moves made and the game result.
- **GridCell**: Represents a single cell on the game board.
- **MinesweeperCLI**: Provides a command-line interface for playing the game.
- **MinesweeperGUI**: Provides a graphical user interface for playing the game.
- **GameConfig**: Holds configuration constants for the game.
- **GameInputValidator**: Validates user inputs for grid size and mine count.
- **GridSize**: Enum representing different grid sizes used for GUI.
- **Difficulty**: Enum representing different difficulty levels for GUI.

### Assumptions

- The grid size and number of mines are determined by user input.
- The game ends immediately if a mine is uncovered.
- The game is won when all non-mine squares are uncovered.
- The application runs on a standard Java environment.

## Requirements

- Java Development Kit (JDK) 8 or higher.
- Maven for building and running tests.

## Instructions to Run the Application

Using GameLauncher

You can also use the GameLauncher class to launch the default game (CLI):

1. Compile the project:
   ```sh
   mvn compile

2.	Run the cli application:
      ```sh
      mvn exec:java -Dexec.mainClass="org.macan.minesweeper.game.GameLauncher"
      
### Command-Line Interface (CLI)

1. Compile the project:
   ```sh
   mvn compile

2.	Run the cli application:
      ```sh
      mvn exec:java -Dexec.mainClass="org.macan.minesweeper.game.MinesweeperCLI"

Graphical User Interface (GUI)

1. Compile the project:
   ```sh
   mvn compile

2.	Run the cli application:
      ```sh
      mvn exec:java -Dexec.mainClass="org.macan.minesweeper.game.MinesweeperGUI"

Running Tests

The project includes unit tests for the core functionality. To run the tests, you can use Maven.

Run Tests:
```sh
mvn test