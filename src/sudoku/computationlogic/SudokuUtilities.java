package sudoku.computationlogic;

import sudoku.problemdomain.SudokuGame;

public class SudokuUtilities
{
    public static void copySudokuArrayValues(int[][] oldArray, int[][] newArray)
    {
        for (int row = 0; row < SudokuGame.GRID_DIMENSION; ++row)
            for (int column = 0; column < SudokuGame.GRID_DIMENSION; ++column)
                newArray[row][column] = oldArray[row][column];
    }

    public static int[][] copyToNewArray(int[][] oldArray)
    {
        int[][] newArray = new int[SudokuGame.GRID_DIMENSION][SudokuGame.GRID_DIMENSION];

        copySudokuArrayValues(oldArray, newArray);

        return newArray;
    }
}
