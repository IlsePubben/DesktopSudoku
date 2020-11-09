package sudoku.computationlogic;

import sudoku.problemdomain.Coordinates;

import static sudoku.computationlogic.GameGenerator.EMPTY_CELL;
import static sudoku.computationlogic.GameGenerator.NUMBER_OF_EMPTY_CELLS;
import static sudoku.problemdomain.SudokuGame.GRID_DIMENSION;

public class SudokuSolver
{
    public static boolean puzzleIsSolvable(int[][] puzzle)
    {
        int [][] puzzleToSolve = SudokuUtilities.copyToNewArray(puzzle);
        return solveSudoku(puzzleToSolve); 

//        Coordinates[] emptyCells = getEmptyCells(puzzle);
//
//        int index = 0;
//        int input = 1;
//
//        while (index < 10)
//        {
//            Coordinates current = emptyCells[index];
//            input = 1;
//
//            while (input < NUMBER_OF_EMPTY_CELLS)
//            {
//                puzzle[current.getX()][current.getY()] = input;
//
//                if (GameLogic.sudokuIsValid(puzzle))
//                {
//                    if (index == 0 && input == GRID_DIMENSION)
//                        return false;
//                    else if (input == GRID_DIMENSION)
//                        index--;
//                    ++input;
//                }
//                else
//                {
//                    ++index;
//
//                    if (index == NUMBER_OF_EMPTY_CELLS - 1) return true;
//                }
//            }
//        }
//        return true;
    }

    public static boolean solveSudoku(int[][] puzzle) //Backtracking algorithm
    {
        for (int row = 0; row < GRID_DIMENSION; ++row)
        {
            for (int column = 0; column < GRID_DIMENSION; ++column)
            {
                if (puzzle[row][column] == EMPTY_CELL)
                {
                    for (int value = 1; value <= GRID_DIMENSION; ++value)
                    {
                        puzzle[row][column] = value;
                        if (GameLogic.cellIsValid(puzzle, new Coordinates(row, column))
                                && solveSudoku(puzzle))
                        {
                            return true;
                        }
                        puzzle[row][column] = EMPTY_CELL;
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static Coordinates[] getEmptyCells(int[][] puzzle)
    {
        Coordinates[] emptyCells = new Coordinates[NUMBER_OF_EMPTY_CELLS];
        int iterator = 0;
        int index = 0;

        for (int y = 0; y < GRID_DIMENSION; ++y)
            for (int x = 0; x < GRID_DIMENSION; ++x)
            {
                if (puzzle[x][y] == 0)
                {
                    emptyCells[index] = new Coordinates(x, y);
                    if (index == NUMBER_OF_EMPTY_CELLS - 1) return emptyCells;
                    ++index;
                }
            }
        return emptyCells;
    }
}
