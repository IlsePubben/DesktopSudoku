package sudoku.computationlogic;

import sudoku.problemdomain.Coordinates;
import sudoku.problemdomain.SudokuGame;
import sudoku.tests.TesterClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static sudoku.problemdomain.SudokuGame.GRID_DIMENSION;

public class GameGenerator
{
    public static int NUMBER_OF_EMPTY_CELLS = 40;
    public static int EMPTY_CELL = 0;

    public static int[][] getNewGameGrid()
    {
        return unsolveGame(getSolvedGame());
    }

    private static int[][] unsolveGame(int[][] solvedGame)
    {
        Random random = new Random(System.currentTimeMillis());
        boolean solvable = false;
        int [][] solvableArray = new int[GRID_DIMENSION][GRID_DIMENSION];

        while (!solvable)
        {
            SudokuUtilities.copySudokuArrayValues(solvedGame, solvableArray);

            int index = 0;

            while (index < NUMBER_OF_EMPTY_CELLS) //we will remove 40 numbers
            {
                int xCoordinate = random.nextInt(GRID_DIMENSION);
                int yCoordinate = random.nextInt(GRID_DIMENSION);

                if (solvableArray[xCoordinate][yCoordinate] != EMPTY_CELL)
                {
                    solvableArray[xCoordinate][yCoordinate] = EMPTY_CELL;
                    ++index;
                }
            }

            int [][] toBeSolved = SudokuUtilities.copyToNewArray(solvableArray);
            solvable = SudokuSolver.puzzleIsSolvable(toBeSolved);
        }
        return solvableArray;
    }

    private static int[][] getSolvedGame()
    {
        Random random = new Random(System.currentTimeMillis());
        int [][] newGrid = new int[GRID_DIMENSION][GRID_DIMENSION];

        for (int value = 1; value <= GRID_DIMENSION; ++value)
        {
            int allocations = 0;
            int interrupt = 0;
            int attempts = 0;

            List<Coordinates> allocationTracker = new ArrayList<>();

            while (allocations < GRID_DIMENSION)
            {
                if (interrupt > 200) //we backtrack
                {
                    allocationTracker.forEach(coord ->
                    {
                        newGrid[coord.getX()][coord.getY()] = 0;
                    });

                    interrupt = 0;
                    allocations = 0;
                    allocationTracker.clear();
                    ++attempts;

                    if (attempts > 500) //algorithm got stuck, we start over
                    {
                        clearArray(newGrid);
                        attempts = 0;
                        value = 1;
                    }
                }

                int randomXCoordinate = random.nextInt(GRID_DIMENSION);
                int randomYCoordinate = random.nextInt(GRID_DIMENSION);

                if (newGrid[randomXCoordinate][randomYCoordinate] == EMPTY_CELL)
                {
                    newGrid[randomXCoordinate][randomYCoordinate] = value;

                    if (GameLogic.sudokuIsInvalid(newGrid))
                    {
                        newGrid[randomXCoordinate][randomYCoordinate] = EMPTY_CELL;
                        ++interrupt;
                    }
                    else
                    {
                        allocationTracker.add(new Coordinates(randomXCoordinate, randomYCoordinate));
                        ++allocations;
                    }
                }
            }
        }
        return newGrid;
    }

    private static void clearArray(int[][] grid)
    {
        for (int row = 0; row < GRID_DIMENSION; ++row)
            for (int column = 0; column < GRID_DIMENSION; ++column)
                grid[row][column] = EMPTY_CELL;
    }
}
