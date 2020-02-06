package cellsociety.View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * Class that represents the user interfaces and displays the grid
 * @author caryshindell, lornezhang, ameersyedibrahim
 * Dependencies: GameEngine class
 * Example: a viewer object displaying the splash screen
 * Assumptions: rectangular grid
 */
public class GridViewer {
    private static final Paint[] COLORMAP = {Color.BLACK, Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.BROWN, Color.WHITE};
    private static final double INITIAL_SCROLL_SPEED = 1;
    private final int SIZE = 700;
    private final int MENU_SIZE = 200;
    private final int GRAPH_SIZE = 500;
    private final Paint SPLASHBACKGROUND = Color.GRAY;
    private static final Paint[] SIMBACKGROUND = {Color.ORANGE, Color.YELLOW};
    private ArrayList<Rectangle> cells;
    private ArrayList<Integer> cellStates;
    private ScrollBar speedBar;
    private final Stage myStage;
    private Scene myScene;
    private Group myRoot;
    private int step;
    private String file_name = "NONE";
    private boolean splashScreenFinished;
    private boolean pause = false;
    private boolean restart;
    private boolean newParameters;
    private Button endButton;
    private Button doneButton;
    private Button resetButton;
    private String lastSimulationRan;
    private LineChart linechart;
    private GridPane gridpane;
    private ArrayList<Double> gridParametersUpdated;
    private ArrayList<Double> blockPercentagesUpdated;
    private HashMap<Integer, Integer> cellCount;
    private XYChart.Series test;



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
        restart = false;
        newParameters = false;
        gridParametersUpdated = new ArrayList<Double>();
        blockPercentagesUpdated = new ArrayList<Double>();

        setUpGrid(rowSize, colSize, initial_states);
        initiateCellMap();
        setUpGraph();
        initiateChart();

        myScene = new Scene(myRoot, SIZE + MENU_SIZE + GRAPH_SIZE, SIZE, SPLASHBACKGROUND);
        
        myStage.setScene(myScene);
    }

    private void initiateChart() {
        for(int x : cellCount.keySet()){
            XYChart.Series dataSeries1 = new XYChart.Series();
            dataSeries1.getData().add(new XYChart.Data( 0, cellCount.get(x)));
            linechart.getData().add(dataSeries1);
        }

    }

    private void initiateCellMap() {
        cellCount = new HashMap<Integer,Integer>();
        for(int x : cellStates){
            cellCount.putIfAbsent(x,0);
            cellCount.put(x,cellCount.get(x));
        }
        for(int x: cellCount.values()){
            System.out.println(x);
        }
    }

    private void setUpGraph() {
        step = 0;
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Step");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Cells");
        linechart = new LineChart(xAxis, yAxis);



        linechart.setLayoutX(MENU_SIZE + SIZE);
        linechart.setLayoutY(100);
        myRoot.getChildren().add(linechart);

    }

    /**
     * Update the cells and change their colors
     * @param nextStates arraylist of next states, in top to bottom order
     */
    public void updateCellStates(ArrayList<Integer> nextStates){
        cellStates = nextStates;
        setCellColors();
        initiateCellMap();
        updateGraph();
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
        addEndButton();
        addResetButton();
        speedBar = makeScrollBar(0.1, 10, INITIAL_SCROLL_SPEED, SIZE*(0.95),SIZE/2);
        setCellColors();
        addAdditionalButtons(lastSimulationRan);
    }

    private void addAdditionalButtons(String file_name) {
        String sim = file_name.split("/")[4].split("_")[0];
        if(sim.equals("fire")){
            addFireButtons();
        }
        else if(sim.equals("gameoflife")){
            addGameOfLifeButtons();
        }
        else if(sim.equals("percolation")){
            addPercolationButtons();
        }
        else if(sim.equals("segregation")){
            addSegregationButtons();
        }
        else if(sim.equals("predprey")){
            addPredatorPreyButtons();
        }
    }

    private void addPredatorPreyButtons() {
        makeLabel("Percent Water:", MENU_SIZE/3, SIZE/2 - 150);
        TextField percentWater = makeTextField("%", MENU_SIZE/3,  SIZE/2 - 125);
        makeLabel("Percent Fish:", MENU_SIZE/3, SIZE/2 -100);
        TextField percentFish = makeTextField("%", MENU_SIZE/3,  SIZE/2 - 75);
        makeLabel("Percent Shark:", MENU_SIZE/3, SIZE/2 -50);
        TextField percentShark = makeTextField("%", MENU_SIZE/3,  SIZE/2-25);
        Button predatorPreyReset = makeButton("Reset With New Parameters", MENU_SIZE/3, SIZE/2 + 10);
        predatorPreyReset.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                blockPercentagesUpdated.add(Double.valueOf(percentWater.getText()));
                blockPercentagesUpdated.add(Double.valueOf(percentFish.getText()));
                blockPercentagesUpdated.add(Double.valueOf(percentShark.getText()));
                restart = true;
                newParameters = true;
            }
        });
    }

    private void addSegregationButtons() {
        makeLabel("Percent Empty:", MENU_SIZE/3, SIZE/2 - 150);
        TextField percentEmpty = makeTextField("%", MENU_SIZE/3,  SIZE/2 - 125);
        makeLabel("Percent Red:", MENU_SIZE/3, SIZE/2 -100);
        TextField percentRed = makeTextField("%", MENU_SIZE/3,  SIZE/2 - 75);
        makeLabel("Percent Blue:", MENU_SIZE/3, SIZE/2 -50);
        TextField percentBlue = makeTextField("%", MENU_SIZE/3,  SIZE/2-25);
        makeLabel("Percent Satisfy:", MENU_SIZE/3, SIZE/2);
        TextField satisfyPercent = makeTextField("%", MENU_SIZE/3,  SIZE/2 + 25);
        Button segregationReset = makeButton("Reset With New Parameters", MENU_SIZE/3, SIZE/2 + 60);
        segregationReset.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                gridParametersUpdated.add(Double.valueOf(satisfyPercent.getText()));
                blockPercentagesUpdated.add(Double.valueOf(percentEmpty.getText()));
                blockPercentagesUpdated.add(Double.valueOf(percentRed.getText()));
                blockPercentagesUpdated.add(Double.valueOf(percentBlue.getText()));
                restart = true;
                newParameters = true;
            }
        });
    }

    private void addPercolationButtons() {
        makeLabel("Percent Blocked:", MENU_SIZE/3, SIZE/2 - 150);
        TextField percentBocked = makeTextField("%", MENU_SIZE/3,  SIZE/2 - 125);
        makeLabel("Percent Open:", MENU_SIZE/3, SIZE/2 - 100);
        TextField percentOpen = makeTextField("%", MENU_SIZE/3,  SIZE/2 - 75);
        makeLabel("Percent Percolated:", MENU_SIZE/3, SIZE/2 -50);
        TextField percentPercolated = makeTextField("%", MENU_SIZE/3,  SIZE/2-25);
        Button percolationReset = makeButton("Reset With New Parameters", MENU_SIZE/3, SIZE/2 + 10);
        percolationReset.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                blockPercentagesUpdated.add(Double.valueOf(percentBocked.getText()));
                blockPercentagesUpdated.add(Double.valueOf(percentOpen.getText()));
                blockPercentagesUpdated.add(Double.valueOf(percentPercolated.getText()));
                restart = true;
                newParameters = true;
            }
        });
    }

    private void addGameOfLifeButtons() {
        makeLabel("Percent Alive:", MENU_SIZE/3, SIZE/2 - 150);
        TextField percentAlive = makeTextField("%", MENU_SIZE/3,  SIZE/2 - 125);
        makeLabel("Percent Dead:", MENU_SIZE/3, SIZE/2 -100);
        TextField percentDead = makeTextField("%", MENU_SIZE/3,  SIZE/2 - 75);
        Button gameOfLifeReset = makeButton("Reset With New Parameters", MENU_SIZE/3, SIZE/2 - 40);
        gameOfLifeReset.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                blockPercentagesUpdated.add(Double.valueOf(percentAlive.getText()));
                blockPercentagesUpdated.add(Double.valueOf(percentDead.getText()));
                restart = true;
                newParameters = true;
            }
        });
    }

    private void addFireButtons() {
        makeLabel("Percent Tree:", MENU_SIZE/3, SIZE/2 - 150);
        TextField percentTree = makeTextField("%", MENU_SIZE/3,  SIZE/2 - 125);
        makeLabel("Percent Fire:", MENU_SIZE/3, SIZE/2 -100);
        TextField percentFire = makeTextField("%", MENU_SIZE/3,  SIZE/2 - 75);
        makeLabel("Percent Dead:", MENU_SIZE/3, SIZE/2 -50);
        TextField percentDead = makeTextField("%", MENU_SIZE/3,  SIZE/2-25);
        makeLabel("Probability Catch:", MENU_SIZE/3, SIZE/2);
        TextField probCatch = makeTextField("%", MENU_SIZE/3,  SIZE/2 + 25);
        makeLabel("Probability Die:", MENU_SIZE/3, SIZE/2 + 50);
        TextField probDie = makeTextField("%", MENU_SIZE/3,  SIZE/2 + 75);
        Button fireReset = makeButton("Reset With New Parameters", MENU_SIZE/3, SIZE/2 + 110);
        fireReset.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                gridParametersUpdated.add(Double.valueOf(probCatch.getText()));
                gridParametersUpdated.add(Double.valueOf(probDie.getText()));
                blockPercentagesUpdated.add(Double.valueOf(percentTree.getText()));
                blockPercentagesUpdated.add(Double.valueOf(percentFire.getText()));
                blockPercentagesUpdated.add(Double.valueOf(percentDead.getText()));
                restart = true;
                newParameters = true;
            }
        });
    }

    public ArrayList<Double> getGridParametersUpdated(){
        return gridParametersUpdated;
    }
    public ArrayList<Double> getBlockPercentagesUpdated(){
        return blockPercentagesUpdated;
    }

    public boolean getNewParameters(){
        return newParameters;
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
                lastSimulationRan = file_name;
            }
        });
        gameOfLifeButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                file_name = "./src/cellsociety/View/gameoflife_config.xml";
                lastSimulationRan = file_name;

            }
        });
        percolationButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                file_name = "./src/cellsociety/View/percolation_config.xml";
                lastSimulationRan = file_name;

            }
        });
        segregationButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                file_name = "./src/cellsociety/View/segregation_config.xml";
                lastSimulationRan = file_name;

            }
        });
        predatorPreyButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                file_name = "./src/cellsociety/View/predprey_config.xml";
                lastSimulationRan = file_name;

            }
        });
        return new Scene(myRoot, SIZE + MENU_SIZE + GRAPH_SIZE, SIZE,  SPLASHBACKGROUND);
    }
    private Button makeButton(String text, int x, int y){
        Button myButton = new Button();
        myButton.setText(text);
        myButton.setLayoutX(x);
        myButton.setLayoutY(y);
        myRoot.getChildren().add(myButton);
        return myButton;
    }
    private Label makeLabel(String text, int x, int y){
        Label label = new Label();
        label.setText(text);
        label.setLayoutX(x);
        label.setLayoutY(y);
        myRoot.getChildren().add(label);
        return label;
    }
    private TextField makeTextField(String text, int x, int y){
        TextField textField = new TextField();
        textField.setText(text);
        textField.setLayoutX(x);
        textField.setLayoutY(y);
        myRoot.getChildren().add(textField);
        return textField;
    }
    public void addDoneButton(){
        doneButton = makeButton("Run Another Simulation", MENU_SIZE/3, SIZE/2 - 250);
        setSplashScreenFinished(true);
        doneButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                myRoot.getChildren().remove(endButton);
                myScene = setUpSplash();
                myStage.setScene(myScene);
                myStage.show();
            }
        });
    }
    private void addEndButton(){
        endButton = makeButton("End Simulation", MENU_SIZE/3, SIZE/2 - 250);
        setSplashScreenFinished(true);
        endButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                myRoot.getChildren().remove(doneButton);
                addDoneButton();
            }
        });
    }
    public void addResetButton(){
        resetButton = makeButton("Reset Simulation", MENU_SIZE/3, SIZE/2 + - 200);
        setSplashScreenFinished(true);
        resetButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                restart = true;
            }
        });
    }
    public boolean getRestart(){
        return restart;
    }
    
    private void updateGraph(){
        step = step + 1;
        //for(int i=0; i<linechart.getData().size(); i++){
        //    linechart.getData().
        //}
        ListIterator<XYChart.Series> namesIterator = linechart.getData().listIterator();
        Iterator<Integer> cellIterator = cellCount.values().iterator();


        // Traversing elements
        while(namesIterator.hasNext()){
            namesIterator.next().getData().add(new XYChart.Data( step, cellIterator.next()));
        }


        //linechart.getData().add(dataSeries1);

    }

}