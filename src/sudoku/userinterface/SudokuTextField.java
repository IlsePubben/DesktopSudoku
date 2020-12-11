package sudoku.userinterface;

import javafx.scene.control.TextField;

public class SudokuTextField extends TextField
{
    private final int x;
    private final int y;

    public SudokuTextField(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public void replaceText(int start, int end, String text)
    {
        String oldText = getText();
        if (!text.matches("[0-9]"))
        {
            end = getText().length();
            super.replaceText(start, end, oldText);
        }
    }

    @Override
    public void replaceSelection(String text)
    {
        String oldText = getText();
        if (!text.matches("[0-9]"))
        {
            super.replaceSelection(oldText);
        }
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
}
