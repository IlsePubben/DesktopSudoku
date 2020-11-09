package sudoku.problemdomain;

import sudoku.computationlogic.SudokuUtilities;
import sudoku.constants.GameState;

import java.io.Serializable;

public class SudokuGame implements Serializable
{
    private final GameState gameState;
    private final int[][] gridState;

    public static final int GRID_DIMENSION = 9;

    public SudokuGame(GameState gameState, int[][] gridState)
    {
        this.gameState = gameState;
        this.gridState = gridState;
    }

    public int[][] getCopyOfGridState()
    {
        return SudokuUtilities.copyToNewArray(gridState);
    }

    public GameState getGameState()
    {
        return gameState;
    }
}
