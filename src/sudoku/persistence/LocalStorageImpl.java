package sudoku.persistence;

import sudoku.problemdomain.IStorage;
import sudoku.problemdomain.SudokuGame;

import java.io.*;

public class LocalStorageImpl implements IStorage
{
    private static File GAME_DATA = new File(
            System.getProperty("user.home"),
            "sudokuGameData.txt"
    );

    @Override
    public void updateGameData(SudokuGame game) throws IOException
    {
        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream(GAME_DATA);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(game);
            objectOutputStream.close();
        }
        catch (IOException exception)
        {
            throw new IOException("Unable to access Game Data");
        }
    }

    @Override
    public SudokuGame getGameData() throws IOException
    {
        FileInputStream fileInputStream = new FileInputStream(GAME_DATA);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        try
        {
            SudokuGame game = (SudokuGame) objectInputStream.readObject();
            objectInputStream.close();
            return game;
        }
        catch (ClassNotFoundException exception)
        {
            throw new IOException("File not found");
        }
    }
}
