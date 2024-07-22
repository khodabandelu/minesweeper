package org.macan.minesweeper.game;

import org.macan.minesweeper.common.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.HashSet;

/**
 * The MinesweeperGUI class implements the Minesweeper game with a graphical user interface (GUI).
 * It allows users to play the game with different grid sizes and difficulty levels, and handles
 * user interactions, including clicking and flagging cells.
 */
public class MinesweeperGUI extends JFrame implements MinesweeperGame {
    private static final Color GREEN = new Color(192, 243, 97);
    private static final Color LIGHT_GREEN = new Color(174, 218, 87);
    private static final Color HIGHLIGHT_GREEN = new Color(213, 239, 159);
    private static final Color FLAG_COLOR = new Color(255, 165, 0);
    private final JPanel gridPanel;
    private final JLabel statusLabel;
    private GameBoard gameBoard;
    private Timer timer;
    private int elapsedTime;
    private GameDifficulty currentGameDifficulty;
    private GridSize currentGridSize;

    /**
     * Constructs a MinesweeperGUI object and initializes the GUI components.
     */
    public MinesweeperGUI() {
        setTitle("Minesweeper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Create the top bar with difficulty levels and grid sizes
        JPanel topBar = new JPanel();
        topBar.setLayout(new FlowLayout(FlowLayout.LEFT));

        currentGridSize = GridSize.SMALL;
        currentGameDifficulty = GameDifficulty.EASY;

        JComboBox<GridSize> gridSizeComboBox = new JComboBox<>(GridSize.values());
        gridSizeComboBox.setSelectedItem(currentGridSize); // Set Medium as the default selection
        gridSizeComboBox.addActionListener(e -> {
            currentGridSize = (GridSize) gridSizeComboBox.getSelectedItem();
            startNewGame();
        });

        JComboBox<GameDifficulty> difficultyComboBox = new JComboBox<>(GameDifficulty.values());
        difficultyComboBox.setSelectedItem(currentGameDifficulty); // Set Medium as the default selection
        difficultyComboBox.addActionListener(e -> {
            currentGameDifficulty = (GameDifficulty) difficultyComboBox.getSelectedItem();
            startNewGame();
        });

        statusLabel = new JLabel("Time: 0s | Uncovered Mines: 0 | Total Mines: 0");

        topBar.add(gridSizeComboBox);
        topBar.add(difficultyComboBox);
        topBar.add(statusLabel);

        add(topBar, BorderLayout.NORTH);

        // Initialize grid panel
        gridPanel = new JPanel();
        add(gridPanel, BorderLayout.CENTER);

        startNewGame();
    }

    /**
     * Main method to launch the MinesweeperGUI game.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MinesweeperGUI gui = new MinesweeperGUI();
            gui.setVisible(true);
        });
    }

    /**
     * Starts a new game with the current grid size and difficulty settings.
     */
    private void startNewGame() {
        elapsedTime = 0;
        if (timer != null) {
            timer.stop();
        }
        timer = new Timer(1000, e -> {
            elapsedTime++;
            updateStatusLabel();
        });
        timer.start();

        int mines = (int) (currentGridSize.getRows() * currentGridSize.getCols() * currentGameDifficulty.getMineFactor());
        gameBoard = new GameBoard(currentGridSize.getRows(), currentGridSize.getCols(), mines, new HashMap<>(), new HashSet<>(), new GameStats());
        gameBoard.initializeGame();
        updateStatusLabel();

        initializeGridPanel();
    }

    /**
     * Sets up the grid panel by creating the cell panels and adding mouse listeners to handle user interactions.
     */
    private void initializeGridPanel() {
        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(currentGridSize.getRows(), currentGridSize.getCols()));

        for (int row = 0; row < currentGridSize.getRows(); row++) {
            for (int col = 0; col < currentGridSize.getCols(); col++) {
                final int currentRow = row;
                final int currentCol = col;
                JPanel cellPanel = new JPanel();
                cellPanel.setBackground((currentRow + currentCol) % 2 == 0 ? GREEN : LIGHT_GREEN);
                cellPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                cellPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (!cellPanel.getBackground().equals(Color.GREEN) && !cellPanel.getBackground().equals(Color.RED) && !cellPanel.getBackground().equals(FLAG_COLOR)) {
                            cellPanel.setBackground(HIGHLIGHT_GREEN);
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (!cellPanel.getBackground().equals(Color.GREEN) && !cellPanel.getBackground().equals(Color.RED) && !cellPanel.getBackground().equals(FLAG_COLOR)) {
                            cellPanel.setBackground((currentRow + currentCol) % 2 == 0 ? GREEN : LIGHT_GREEN);
                        }
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            toggleFlagCell(currentRow, currentCol, cellPanel);
                        } else {
                            handleCellClick(currentRow, currentCol);
                        }
                    }
                });

                gridPanel.add(cellPanel);
            }
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    /**
     * Handles a cell click event, revealing the cell and updating the game state.
     *
     * @param row the row index of the clicked cell.
     * @param col the column index of the clicked cell.
     */
    private void handleCellClick(int row, int col) {
        gameBoard.revealCell(row, col);
        updateGrid();
        if (GameResult.LOST.equals(gameBoard.getGameStats().getGameResult())) {
            endGame("Game Over! Try Again?");
        } else if (GameResult.WON.equals(gameBoard.getGameStats().getGameResult())) {
            endGame("Congratulations! You Win!");
        }
    }

    /**
     * Updates the entire grid, refreshing the state of each cell.
     */
    private void updateGrid() {
        for (Component comp : gridPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel cellPanel = (JPanel) comp;
                int index = gridPanel.getComponentZOrder(cellPanel);
                int row = index / gameBoard.getGridColumns();
                int col = index % gameBoard.getGridColumns();
                GridCell cell = gameBoard.getGridCell(row, col);
                updateCellPanel(cellPanel, cell);
            }
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    /**
     * Toggles the flagged state of a cell.
     *
     * @param row       the row index of the cell.
     * @param col       the column index of the cell.
     * @param cellPanel the JPanel representing the cell.
     */
    private void toggleFlagCell(int row, int col, JPanel cellPanel) {
        GridCell cell = gameBoard.getGridCell(row, col);
        if (!cell.isRevealed()) {
            if (cell.isFlagged()) {
                cell.setFlagged(false);
                cellPanel.setBackground((row + col) % 2 == 0 ? GREEN : LIGHT_GREEN);
                gameBoard.getGameStats().decrementMinesUncovered();
            } else {
                cell.setFlagged(true);
                cellPanel.setBackground(FLAG_COLOR);
                gameBoard.getGameStats().incrementMinesUncovered();
            }
            updateStatusLabel();
        }
    }

    /**
     * Updates the appearance of a cell panel based on the state of the corresponding cell.
     *
     * @param cellPanel the JPanel representing the cell.
     * @param cell      the GridCell object containing the state of the cell.
     */
    private void updateCellPanel(JPanel cellPanel, GridCell cell) {
        cellPanel.removeAll();
        if (cell.isMine() && cell.isRevealed()) {
            cellPanel.setBackground(Color.RED);
            cellPanel.add(new JLabel("M"));
        } else if (cell.isRevealed()) {
            cellPanel.setBackground(Color.GREEN);
            if (cell.getAdjacentMines() > 0) {
                cellPanel.add(new JLabel(String.valueOf(cell.getAdjacentMines())));
            }
        } else if (cell.isFlagged()) {
            cellPanel.setBackground(FLAG_COLOR);
        } else {
            cellPanel.setBackground((cell.getRow() + cell.getCol()) % 2 == 0 ? GREEN : LIGHT_GREEN);
        }
        cellPanel.revalidate();
        cellPanel.repaint();
    }

    /**
     * Updates the status label with the current elapsed time and mine counts.
     */
    private void updateStatusLabel() {
        statusLabel.setText(String.format("Time: %ds | Uncovered Mines: %d | Total Mines: %d", elapsedTime, gameBoard.getGameStats().getMinesUncovered(), gameBoard.getTotalMines()));
    }

    /**
     * Ends the current game, displaying a message to the user and restarting the game.
     *
     * @param message the message to display to the user.
     */
    private void endGame(String message) {
        timer.stop();
        JOptionPane.showMessageDialog(this, message);
        startNewGame(); // Restart with the current grid size and difficulty
    }

    /**
     * Launches the Minesweeper GUI game.
     */
    @Override
    public void launch() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }
}