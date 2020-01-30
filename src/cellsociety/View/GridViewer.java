package cellsociety.View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class GridViewer {

    private static final Paint[] COLORMAP = {Color.BLACK, Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE};
    private ArrayList<Rectangle> cells;
    private ArrayList<Integer> cellStates;
    private final int SIZE = 1000;
    private final Paint BACKGROUND = Color.WHITE;
    private Stage myStage;
    private Scene myScene;
    private Group myRoot;
    private String file_name = "NONE";
    private boolean splashScreenFinished;

    public GridViewer(){
        myStage = new Stage();
        myScene = setUpSplash();
        myStage.setScene(myScene);
        myStage.show();
    }

    public void setUpSimulation(int rowSize, int colSize, ArrayList<Integer> initial_states){
        myRoot = new Group();
        setUpGrid(rowSize, colSize, initial_states);
        myScene = new Scene(myRoot, SIZE, SIZE, BACKGROUND);
        //scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        //scene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
    }

    public void updateCellStates(ArrayList<Integer> nextStates){
        cellStates = nextStates;
        setCellColors();
    }

    // assume grid is always square
    public void setUpGrid(int rowSize, int colSize, ArrayList<Integer> initial_states){
        cellStates = new ArrayList<Integer>();
        cells = new ArrayList<Rectangle>();
        int cellSize = (int) (SIZE*0.8 / colSize);
        int row = 0;
        int col = 0;
        for(Integer state : initial_states){
            cellStates.add(state);
            Rectangle cell = new Rectangle(SIZE/10 + col*cellSize, SIZE/10 + row*cellSize, cellSize, cellSize);
            cells.add(cell);
            myRoot.getChildren().add(cell);
            col++;
            if(col >= rowSize){
                col = 0;
                row++;
            }
        }
        setCellColors();
    }

    private void setCellColors(){
        for(int i=0; i<cellStates.size(); i++){
            int cellstate = cellStates.get(i);
            cells.get(i).setFill(COLORMAP[cellstate]);
        }
    }

    public boolean getSplashScreenFinished(){
        return splashScreenFinished;
    }

    public void setSplashScreenFinished(boolean target){
        splashScreenFinished = target;
    }

    private Scene setUpSplash(){
        myRoot = new Group();
        // add text
        // add 5 buttons to choose simulation
        Button fireButton = makeButton("Fire", .., ..)
        Button predatorPreyButton = makeButton("PredatorPrey", .., ..)
        Button percolationButton = makeButton("Percolation", .., ..)
        Button segregationButton = makeButton("Segregation", .., ..)
        Button gameOfLifeButton = makeButton("GameOfLife", .., ..)

        fireButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                file_name = "Fire";
            }
        });
        predatorPreyButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                file_name = "PredatorPrey";
            }
        });
        percolationButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                file_name = "Percolation";
            }
        });
        segregationButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                file_name = "Segregation";
            }
        });
        gameOfLifeButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                file_name = "GameOfLife";
            }
        });
        return new Scene(myRoot, SIZE, SIZE, BACKGROUND);
    }
    public String getFileName(){
        return file_name;
    }
    public void setFileName(String newFileName){
        file_name = newFileName;
    }


    private Button makeButton(String text, int width, int height){
        Button myButton = new Button();
        myButton.setText(text);
        myButton.setLayoutX(width/2 - 70);
        myButton.setLayoutY(height/2);
        myRoot.getChildren().add(myButton);
        return myButton;
    }

}