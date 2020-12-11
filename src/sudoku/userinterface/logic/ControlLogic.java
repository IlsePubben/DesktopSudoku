package sudoku.userinterface.logic;

import sudoku.computationlogic.GameLogic;
import sudoku.computationlogic.SudokuSolver;
import sudoku.constants.GameState;
import sudoku.constants.Messages;
import sudoku.problemdomain.IStorage;
import sudoku.problemdomain.SudokuGame;
import sudoku.userinterface.IUserInterfaceContract;
import sudoku.problemdomain.SudokuGame;

import java.io.IOException;

public class ControlLogic implements IUserInterfaceContract.EventListener
{
    private IStorage storage;
    private IUserInterfaceContract.View view;

    public ControlLogic(IStorage storage, IUserInterfaceContract.View view)
    {
        this.storage = storage;
        this.view = view;
    }

    @Override
    public void onSudokuInput(int x, int y, int input)
    {
        try
        {
            SudokuGame gameData = storage.getGameData();
            int[][] newGridState = gameData.getCopyOfGridState();
            newGridState[x][y] = input;

            gameData = new SudokuGame(
                    GameLogic.checkForCompletion(newGridState),
                    newGridState
            );

            storage.updateGameData(gameData);
            view.updateSquare(x, y, input);

            if (gameData.getGameState() == GameState.COMPLETE)
                view.showDialog(Messages.GAME_COMPLETE);

        }
        catch (IOException exception)
        {
            exception.printStackTrace();
            view.showError(Messages.ERROR);
        }
    }

    @Override
    public void onDialogClick()
    {
        try
        {
            storage.updateGameData(GameLogic.getNewGame());

            view.updateBoard(storage.getGameData());
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
            view.showError(Messages.ERROR);
        }
    }

    @Override
    public void onButtonNewGameClick()
    {
        try
        {
            storage.updateGameData(GameLogic.getNewGame());

            view.updateBoard(storage.getGameData());
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
            view.showError(Messages.ERROR);
        }
    }

    @Override
    public void onButtonCheckClick()
    {
        try
        {
            SudokuGame puzzle = storage.getGameData();

            if (SudokuSolver.puzzleIsSolvable(puzzle.getCopyOfGridState()))
            {
                view.showDialog(Messages.NO_MISTAKES);
            }
            else
            {
                view.showDialog(Messages.MISTAKES_FOUND);
            }
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
            view.showError(Messages.ERROR);
        }
    }
}

