package org.macan.minesweeper.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.macan.minesweeper.common.GameResult;
import org.macan.minesweeper.common.GameStats;
import org.macan.minesweeper.common.GridCell;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameBoardTest {

    private GameBoard gameBoard;
    private GameStats gameStats;
    private Map<String, GridCell> grid;
    private Set<String> mineKeys;

    @BeforeEach
    void setUp() {
        int gridRows = 5;
        int gridColumns = 5;
        int totalMines = 5;
        grid = new HashMap<>();
        mineKeys = new HashSet<>();
        gameStats = new GameStats();
        gameBoard = new GameBoard(gridRows, gridColumns, totalMines, grid, mineKeys, gameStats);
        gameBoard.initializeGame();
    }

    @Test
    void testInitializeGame() {
        assertEquals(5, mineKeys.size());
    }

    @Test
    void testPlaceMines() {
        long mineCount = grid.values().stream().filter(GridCell::isMine).count();
        assertEquals(5, mineCount);
    }

    @Test
    void testCalculateMineNumbers() {
        for (String mineKey : mineKeys) {
            String[] parts = mineKey.split("_");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            for (int i = row - 1; i <= row + 1; i++) {
                for (int j = col - 1; j <= col + 1; j++) {
                    if (i >= 0 && i < gameBoard.getGridRows() && j >= 0 && j < gameBoard.getGridColumns() && !(i == row && j == col)) {
                        GridCell cell = gameBoard.getGridCell(i, j);
                        if (!cell.isMine()) {
                            assertTrue(cell.getAdjacentMines() > 0);
                        }
                    }
                }
            }
        }
    }

    @Test
    void testRevealCell() {
        gameBoard.revealCell(0, 0);
        GridCell cell = gameBoard.getGridCell(0, 0);
        assertTrue(cell.isRevealed());
    }

    @Test
    void testRevealMineCell() {
        for (String mineKey : mineKeys) {
            String[] parts = mineKey.split("_");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            gameBoard.revealCell(row, col);
            assertEquals(GameResult.LOST, gameStats.getGameResult());
            break;
        }
    }

    @Test
    void testRevealsAllCells_ExceptMines() {
        for (int row = 0; row < gameBoard.getGridRows(); row++) {
            for (int col = 0; col < gameBoard.getGridColumns(); col++) {
                if (!mineKeys.contains(gameBoard.getGridKey(row, col))) {
                    gameBoard.revealCell(row, col);
                }
            }
        }
        assertEquals(GameResult.WON, gameStats.getGameResult());
    }

    @Test
    void testWinConditionIfCellAlreadyRevealed_ExceptMines() {
        for (int row = 0; row < gameBoard.getGridRows(); row++) {
            for (int col = 0; col < gameBoard.getGridColumns(); col++) {
                GridCell gridCell = gameBoard.getGridCell(row, col);
                if (!gridCell.isMine() && !gridCell.isRevealed()) {
                    gameBoard.revealCell(row, col);
                }
            }
        }
        assertEquals(GameResult.WON, gameStats.getGameResult());
    }

    @Test
    void testGetGridKey() {
        String key = gameBoard.getGridKey(2, 3);
        assertEquals("2_3", key);
    }

    @Test
    void testGetGridCell() {
        GridCell cell = gameBoard.getGridCell(2, 3);
        Assertions.assertNotNull(cell);
        assertEquals(2, cell.getRow());
        assertEquals(3, cell.getCol());
    }
}