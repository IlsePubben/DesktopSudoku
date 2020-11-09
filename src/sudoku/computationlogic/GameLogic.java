package sudoku.computationlogic;

import sudoku.constants.GameState;
import sudoku.problemdomain.Coordinates;
import sudoku.problemdomain.SudokuGame;

import java.util.stream.IntStream;

import static sudoku.problemdomain.SudokuGame.GRID_DIMENSION;
import static sudoku.computationlogic.GameGenerator.EMPTY_CELL;

public class GameLogic
{
    public static int SUBSECTION_SIZE = 3;

    public static boolean sudokuIsValid(int[][] puzzle)
    {
        for (int row = 0; row < GRID_DIMENSION; ++row)
            for (int column = 0; column < GRID_DIMENSION; ++column)
            {
                if (!cellIsValid(puzzle, new Coordinates(row,column))) return false;
            }
        return true;
    }

    public static boolean sudokuIsInvalid(int[][] puzzle)
    {
        return !sudokuIsValid(puzzle);
    }

    public static SudokuGame getNewGame()
    {
        return new SudokuGame(GameState.NEW, GameGenerator.getNewGameGrid());
    }

    public static GameState checkForCompletion(int[][] grid)
    {
        if (!allTilesAreFilled(grid)) return GameState.ACTIVE;
        if (sudokuIsInvalid(grid)) return GameState.ACTIVE;
        return GameState.COMPLETE; 
    }

    protected static boolean allTilesAreFilled(int[][] grid)
    {
        for (int row = 0; row < GRID_DIMENSION; ++row)
            for (int column = 0; column < GRID_DIMENSION; ++column)
                if (grid[row][column] == EMPTY_CELL) return false;

        return true;
    }

    public static boolean cellIsValid(int[][] puzzle, Coordinates cellLocation)
    {
        return ( rowIsValid(puzzle, cellLocation.getX())
                && columnIsValid(puzzle, cellLocation.getY())
                && squareIsValid(puzzle, cellLocation)
        );
    }

    private static boolean squareIsValid(int[][] puzzle, Coordinates cellLocation)
    {
        boolean[] constraint = new boolean[GRID_DIMENSION];
        int squareRowStart = (cellLocation.getX() / SUBSECTION_SIZE) * SUBSECTION_SIZE;
        int squareRowEnd = squareRowStart + SUBSECTION_SIZE;

        int squareColumnStart = (cellLocation.getY() / SUBSECTION_SIZE) * SUBSECTION_SIZE;
        int squareColumnEnd = squareColumnStart + SUBSECTION_SIZE;

        for (int row = squareRowStart; row < squareRowEnd; ++row)
            for (int column = squareColumnStart; column < squareColumnEnd; ++column)
                if (!constraintMet(puzzle, row, constraint, column))
                    return false;
        return true;
    }

    private static boolean columnIsValid(int[][] puzzle, int column)
    {
        boolean[] constraint = new boolean[GRID_DIMENSION];

        return IntStream.range(0, GRID_DIMENSION)
                .allMatch(row -> constraintMet(puzzle, row, constraint, column));
    }

    private static boolean rowIsValid(int[][] puzzle, int row)
    {
        boolean[] constraint = new boolean[GRID_DIMENSION];

        return IntStream.range(0, GRID_DIMENSION)
                .allMatch(column -> constraintMet(puzzle, row, constraint, column));
    }

    private static boolean constraintMet(int[][] puzzle, int row, boolean[] constraint, int column)
    {
        if (puzzle[row][column] != EMPTY_CELL)
        {
            if (!constraint[puzzle[row][column] - 1])
                constraint[puzzle[row][column] - 1] = true;
            else
                return false;
        }
        return true;
    }
}
