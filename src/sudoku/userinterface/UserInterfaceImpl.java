package sudoku.userinterface;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sudoku.constants.GameState;
import sudoku.problemdomain.Coordinates;
import sudoku.problemdomain.SudokuGame;

import static sudoku.computationlogic.GameGenerator.EMPTY_CELL;


import java.util.HashMap;

public class UserInterfaceImpl implements IUserInterfaceContract.View,
        EventHandler<KeyEvent>
{
    private final Stage stage;
    private final Group root;

    public static final int SIZE_OF_GRID_TILE = 64;

    private HashMap<Coordinates, SudokuTextField> textFieldCoordinates;

    private IUserInterfaceContract.EventListener listener;

    public UserInterfaceImpl(Stage stage)
    {
        this.stage = stage;
        this.root = new Group();
        this.textFieldCoordinates = new HashMap<>(); 
        initializeUserInterface(); 
    }

    private void initializeUserInterface()
    {
        drawBackground(root);
        drawTitle(root);
        drawSudokuBoard(root);
        drawTextFields(root);
        drawGridLines(root);
        stage.show();
    }

    private void drawGridLines(Group root)
    {
        int xAndY = 114;
        for (int index = 0; index < SudokuGame.GRID_DIMENSION - 1; ++index)
        {
            int thickness;
            if (index == 2 || index == 5)
                thickness = 3;
            else
                thickness = 2;

            Rectangle verticalLine = getLine(
                    xAndY + SIZE_OF_GRID_TILE * index,
                    StyleUI.BOARD_PADDING, 
                    StyleUI.BOARD_X_AND_Y,
                    thickness
            );

            Rectangle horizontalLine = getLine(
                    StyleUI.BOARD_PADDING,
                    xAndY + SIZE_OF_GRID_TILE * index,
                    thickness,
                    StyleUI.BOARD_X_AND_Y
            );

            root.getChildren().addAll(verticalLine, horizontalLine);
        }
    }

    private Rectangle getLine(double x, double y, double height, double width)
    {
        Rectangle line = new Rectangle(x,y,width,height);
        line.setFill(Color.BLACK);
        return line;
    }

    private void drawTextFields(Group root)
    {
        final int xOrigin = 50;
        final int yOrigin = 50;

        for (int xIndex = 0; xIndex < SudokuGame.GRID_DIMENSION; ++xIndex)
            for (int yIndex = 0; yIndex < SudokuGame.GRID_DIMENSION; ++yIndex)
            {
                int x = xOrigin + xIndex * SIZE_OF_GRID_TILE;
                int y = yOrigin + yIndex * SIZE_OF_GRID_TILE;

                SudokuTextField tile = new SudokuTextField(xIndex, yIndex);
                styleSudokuTile(tile, x, y);

                tile.setOnKeyPressed(this);

                textFieldCoordinates.put(new Coordinates(xIndex, yIndex), tile);

                root.getChildren().add(tile);
            }
    }

    private void styleSudokuTile(SudokuTextField tile, double x, double y)
    {
        Font numberFont = new Font(32);
        tile.setFont(numberFont);
        tile.setAlignment(Pos.CENTER);

        tile.setLayoutX(x);
        tile.setLayoutY(y);
        tile.setPrefHeight(SIZE_OF_GRID_TILE);
        tile.setPrefWidth(SIZE_OF_GRID_TILE);

        tile.setBackground(Background.EMPTY);
    }

    private void drawSudokuBoard(Group root)
    {
        Rectangle boardBackground = new Rectangle(StyleUI.BOARD_PADDING, StyleUI.BOARD_PADDING,
                StyleUI.BOARD_X_AND_Y, StyleUI.BOARD_X_AND_Y);

        boardBackground.setFill(StyleUI.BOARD_BACKGROUND_COLOUR);

        root.getChildren().add(boardBackground);
    }

    private void drawTitle(Group root)
    {
        Text title = new Text(235, 690, StyleUI.SUDOKU);
        title.setFill(Color.WHITE);
        title.setFont(new Font(43));
        root.getChildren().add(title);
    }

    private void drawBackground(Group root)
    {
        Scene scene = new Scene(root, StyleUI.WINDOW_X, StyleUI.WINDOW_Y);
        scene.setFill(StyleUI.WINDOW_BACKGROUND_COLOUR);
        stage.setScene(scene);
    }

    @Override
    public void handle(KeyEvent event)
    {
        if (event.getEventType() == KeyEvent.KEY_PRESSED)
        {
            if (event.getText().matches("[0-9]"))
            {
                int value = Integer.parseInt(event.getText());
                handleInput(value, event.getSource());
            }
            else if (event.getCode() == KeyCode.BACK_SPACE)
            {
                handleInput(EMPTY_CELL, event.getSource());
            }
            else
            {
                String oldText = ((SudokuTextField) event.getSource()).getText();
                ((TextField) event.getSource()).setText(oldText);
            }
        }
    }

    private void handleInput(int value, Object source)
    {
        listener.onSudokuInput(
                ((SudokuTextField) source).getX(),
                ((SudokuTextField) source).getY(),
                value
        );
    }

    @Override
    public void setListener(IUserInterfaceContract.EventListener listener)
    {
        this.listener = listener; 
    }

    @Override
    public void updateSquare(int x, int y, int input)
    {
        SudokuTextField tile = textFieldCoordinates.get(new Coordinates(x, y));

        String value = Integer.toString(input);

        if (value.equals("0")) value = "";

        tile.textProperty().setValue(value);
    }

    @Override
    public void updateBoard(SudokuGame game)
    {
        for (int xIndex = 0; xIndex < SudokuGame.GRID_DIMENSION; ++xIndex)
            for (int yIndex = 0; yIndex < SudokuGame.GRID_DIMENSION; ++yIndex)
            {
                SudokuTextField tile = textFieldCoordinates.get(new Coordinates(xIndex, yIndex));
                String value = Integer.toString(game.getCopyOfGridState()[xIndex][yIndex]);

                if (value.equals("0")) value = "";

                tile.setText(value);

                if (game.getGameState() == GameState.NEW)
                    if (value.equals(""))
                    {
                        tile.setStyle("-fx-opacity: 1;");
                        tile.setDisable(false);
                    }
                    else
                    {
                        tile.setStyle("-fx-opacity: 0.8;");
                        tile.setDisable(true);
                    }
            }
    }

    @Override
    public void showDialog(String message)
    {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK);
        dialog.showAndWait();

        if (dialog.getResult() == ButtonType.OK) listener.onDialogClick();
    }

    @Override
    public void showError(String message)
    {
        Alert dialog = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        dialog.showAndWait();
    }
}
