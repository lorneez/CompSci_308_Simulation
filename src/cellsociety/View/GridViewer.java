package cellsociety.View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Class that represents the user interfaces and displays the grid
 * @author caryshindell, lornezhang, ameersyedibrahim
 * Dependencies: GameEngine class
 * Example: a viewer object displaying the splash screen
 * Assumptions: rectangular grid
 */
public class GridViewer {
    private static final Paint[] COLORMAP = {Color.BLACK, Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.BROWN, Color.WHITE};
    private final int SIZE = 700;
    private final int MENU_SIZE = 200;
    private final Paint SPLASHBACKGROUND = Color.GRAY;
    private static final Paint[] SIMBACKGROUND = {Color.ORANGE, Color.YELLOW};
    private ArrayList<Rectangle> cells;
    private ArrayList<Integer> cellStates;
    private ScrollBar speedBar;
    private Stage myStage;
    private Scene myScene;
    private Group myRoot;
    private String file_name = "NONE";
    private boolean splashScreenFinished;
    private boolean pause = false;

    /**
     * Constructor method, sets up the stage and splash screen
     */
    public GridViewer(){
        splashScreenFinished = true;
        myStage = new Stage();
        myScene = setUpSplash();
        myStage.setScene(myScene);
        myStage.show();
    }

    /**
     * get whether or not we are paused
     * @return boolean paused
     */
    public boolean getPause(){
        return pause;
    }

    /**
     * get scroll bar value
     * @return scroll bar value
     */
    public double getScrollValue(){
        return speedBar.getValue();
    }

    /**
     * Set up the simulation scene and grid
     * @param rowSize number of columns
     * @param colSize number of rows
     * @param initial_states intial cell configurations
     */
    public void setUpSimulation(int rowSize, int colSize, ArrayList<Integer> initial_states){
        setUpGrid(rowSize, colSize, initial_states);
        myScene = new Scene(myRoot, SIZE + MENU_SIZE + MENU_SIZE, SIZE, SIMBACKGROUND[1]);
        myStage.setScene(myScene);
    }

    /**
     * Update the cells and change their colors
     * @param nextStates arraylist of next states, in top to bottom order
     */
    public void updateCellStates(ArrayList<Integer> nextStates){
        cellStates = nextStates;
        setCellColors();
    }

    // assume grid is always square
    public void setUpGrid(int rowSize, int colSize, ArrayList<Integer> initial_states){
        myRoot = new Group();
        cellStates = new ArrayList<Integer>();
        cells = new ArrayList<Rectangle>();
        double cellSize = (double) (SIZE*0.8 / colSize);
        int row = 0;
        int col = 0;
        for(Integer state : initial_states){
            cellStates.add(state);
            Rectangle cell = new Rectangle(MENU_SIZE + SIZE * 0.1 + col * cellSize, SIZE * 0.1 + row * cellSize, cellSize, cellSize);
            cells.add(cell);
            myRoot.getChildren().add(cell);
            col++;
            if(col >= rowSize){
                col = 0;
                row++;
            }
        }
        Button pauseButton = makeButton("Pause", SIZE/2, (25));
        pauseButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                pause = !pause;
            }
        });
        speedBar = makeScrollBar(0.1, 10, 1, SIZE*(0.95),SIZE/2);
        setCellColors();
    }

    public boolean getSplashScreenFinished(){
        return splashScreenFinished;
    }

    public void setSplashScreenFinished(boolean target){
        splashScreenFinished = target;
    }

    public String getFileName(){
        return file_name;
    }

    public void setFileName(String newFileName){
        file_name = newFileName;
    }

    private ScrollBar makeScrollBar(double min, double max, double val, double y, double x) {
        ScrollBar myScrollBar = new ScrollBar();
        myScrollBar.setMin(min);
        myScrollBar.setMax(max);
        myScrollBar.setValue(val);
        myScrollBar.setLayoutY(y);
        myScrollBar.setLayoutX(x);
        myRoot.getChildren().add(myScrollBar);
        return myScrollBar;
    }

    private void setCellColors(){
        for(int i=0; i<cellStates.size(); i++){
            int cellstate = cellStates.get(i);
            cells.get(i).setFill(COLORMAP[cellstate]);
        }
    }

    private Scene setUpSplash(){
        myRoot = new Group();
        Button fireButton = makeButton("Fire", SIZE/2, (SIZE/2)+50);
        Button gameOfLifeButton = makeButton("GameOfLife", SIZE/2, SIZE/2);
        Button segregationButton = makeButton("Segregation", SIZE/2, SIZE/2 - 50);
        Button predatorPreyButton = makeButton("PredatorPrey", SIZE/2, SIZE/2 - 100);
        Button percolationButton = makeButton("Percolation", SIZE/2, SIZE/2 + 100);

        fireButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                file_name = "./src/cellsociety/View/fire_config.xml";
            }
        });
        gameOfLifeButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                file_name = "./src/cellsociety/View/gameoflife_config.xml";
            }
        });
        percolationButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                file_name = "./src/cellsociety/View/percolation_config.xml";
            }
        });
        segregationButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                file_name = "./src/cellsociety/View/segregation_config.xml";
            }
        });
        predatorPreyButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                file_name = "./src/cellsociety/View/predprey_config.xml";
            }
        });
        return new Scene(myRoot, SIZE + MENU_SIZE + MENU_SIZE, SIZE,  SPLASHBACKGROUND);
    }

    private Button makeButton(String text, int x, int y){
        Button myButton = new Button();
        myButton.setText(text);
        myButton.setLayoutX(x);
        myButton.setLayoutY(y);
        myRoot.getChildren().add(myButton);
        return myButton;
    }
    public void addDoneButton(){
        Button doneButton = makeButton("Run Another Simulation", SIZE/2 + 100, SIZE - 25);
        setSplashScreenFinished(true);
        doneButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                myScene = setUpSplash();
                myStage.setScene(myScene);
                myStage.show();
            }
        });
    }

}