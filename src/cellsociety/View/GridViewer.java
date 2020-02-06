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

import java.util.*;

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
    private static final int SIZE = 700;
    private static final int MENU_SIZE = 200;
    private static final int GRAPH_SIZE = 500;
    private static final Paint SPLASHBACKGROUND = Color.GRAY;
    private static final Paint[] SIMBACKGROUND = {Color.ORANGE, Color.YELLOW};
    private Map<Integer, Rectangle> cells;
    private Map<Integer, Integer> cellStates;
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
    private String lastSimulationRan;
    private LineChart linechart;
    private GridPane gridpane;
    private ArrayList<Double> gridParametersUpdated;
    private ArrayList<Double> blockPercentagesUpdated;
    private Map<Integer, Integer> cellCount;
    private XYChart.Series test;
    private Map<Integer, String> stateNames;

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
        gridParametersUpdated = new ArrayList<>();
        blockPercentagesUpdated = new ArrayList<>();

        setUpGrid(rowSize, colSize, initial_states);
        initiateCellMap();
        setUpGraph();
        initiateChart();

        myScene = new Scene(myRoot, SIZE + MENU_SIZE + GRAPH_SIZE, SIZE, SPLASHBACKGROUND);
        
        myStage.setScene(myScene);
    }

    public void setStateNames(HashMap<Integer, String> x){
        stateNames = x;
    }

    /**
     * construct a single unique integer from a pair of coordinates. Assume size<10000
     */
    private int coordinatePair(int x, int y){
        return x*10000 + y;
    }

    private void initiateChart() {
        for(int x : cellCount.keySet()){
            XYChart.Series dataSeries1 = new XYChart.Series();
            dataSeries1.getData().add(new XYChart.Data( 0, cellCount.get(x)));
            dataSeries1.setName(stateNames.get(x));
            linechart.getData().add(dataSeries1);
        }

    }

    public void displayPopUp(){
        Group errorRoot = new Group();
        Stage errorStage = new Stage();
        Scene errorScene = setUpPopUp(errorStage, errorRoot);
        errorStage.setScene(errorScene);
        errorStage.show();
        setSplashScreenFinished(true);
        myStage.close();


    }

    private Scene setUpPopUp(Stage errorStage, Group errorRoot){
        //TextField  message = makeTextField("Edit Configuration File to Input Correct Simulation Type", MENU_SIZE/3,  SIZE/2 + 75, errorRoot);
        Label message = makeLabel("Error: Edit Configuration File",GRAPH_SIZE/4,  (GRAPH_SIZE/3)+50, errorRoot);
        Button exitButton = makeButton("Exit", (GRAPH_SIZE/4) + 25 , (GRAPH_SIZE/3)+75, errorRoot);
        exitButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                errorStage.close();
            }
        });
        return new Scene(errorRoot, GRAPH_SIZE, GRAPH_SIZE,  SPLASHBACKGROUND);
    }

    private void initiateCellMap() {
        cellCount = new HashMap<>();
        for(int x : cellStates.values()){
            cellCount.putIfAbsent(x,0);
            cellCount.put(x,cellCount.get(x) + 1);
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
    public void updateCellStates(ArrayList<int[]> nextStates){
        for (int[] cellInfo : nextStates) {
            cellStates.put(coordinatePair(cellInfo[0], cellInfo[1]), cellInfo[2]);
            cells.get(coordinatePair(cellInfo[0], cellInfo[1])).setFill(COLORMAP[cellInfo[2]]);
        }
        initiateCellMap();
        updateGraph();
    }

    /**
     * assume grid is always square. For different grid shapes, just need to change this method (and stuff in grid class)
     */
    public void setUpGrid(int rowSize, int colSize, ArrayList<Integer> initial_states){
        myRoot = new Group();
        cellStates = new HashMap<>();
        cells = new HashMap<>();
        double cellSize = SIZE*0.8 / colSize;
        int row = 0;
        int col = 0;
        for(Integer state : initial_states){
            cellStates.put(coordinatePair(row,col), state);
            Rectangle cell = new Rectangle(MENU_SIZE + SIZE * 0.1 + col * cellSize, SIZE * 0.1 + row * cellSize, cellSize, cellSize);
            cell.setFill(COLORMAP[state]);
            cells.put(coordinatePair(row,col), cell);
            myRoot.getChildren().add(cell);
            col++;
            if(col >= rowSize){
                col = 0;
                row++;
            }
        }
        Button pauseButton = makeButton("Pause", SIZE/2, (25), myRoot);
        pauseButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                pause = !pause;
            }
        });
        addEndButton();
        addResetButton();
        speedBar = makeScrollBar(0.1, 10, INITIAL_SCROLL_SPEED, SIZE*(0.95),SIZE/2);
        addAdditionalButtons(lastSimulationRan);
    }

    private void addAdditionalButtons(String file_name) {
        String sim = file_name.split("/")[4].split("_")[0];
        switch (sim) {
            case "fire":
                addFireButtons();
                break;
            case "gameoflife":
                addGameOfLifeButtons();
                break;
            case "percolation":
                addPercolationButtons();
                break;
            case "segregation":
                addSegregationButtons();
                break;
            case "predprey":
                addPredatorPreyButtons();
                break;
        }
    }

    private void addPredatorPreyButtons() {
        makeLabel("Percent Water:", MENU_SIZE/3, SIZE/2 - 150, myRoot);
        TextField percentWater = makeTextField("%", MENU_SIZE/3,  SIZE/2 - 125, myRoot);
        makeLabel("Percent Fish:", MENU_SIZE/3, SIZE/2 -100, myRoot);
        TextField percentFish = makeTextField("%", MENU_SIZE/3,  SIZE/2 - 75, myRoot);
        makeLabel("Percent Shark:", MENU_SIZE/3, SIZE/2 -50, myRoot);
        TextField percentShark = makeTextField("%", MENU_SIZE/3,  SIZE/2-25, myRoot);
        Button predatorPreyReset = makeButton("Reset With New Parameters", MENU_SIZE/3, SIZE/2 + 10, myRoot);
        predatorPreyReset.setOnAction(new EventHandler<>() {
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
        makeLabel("Percent Empty:", MENU_SIZE/3, SIZE/2 - 150, myRoot);
        TextField percentEmpty = makeTextField("%", MENU_SIZE/3,  SIZE/2 - 125, myRoot);
        makeLabel("Percent Red:", MENU_SIZE/3, SIZE/2 -100, myRoot);
        TextField percentRed = makeTextField("%", MENU_SIZE/3,  SIZE/2 - 75, myRoot);
        makeLabel("Percent Blue:", MENU_SIZE/3, SIZE/2 -50, myRoot);
        TextField percentBlue = makeTextField("%", MENU_SIZE/3,  SIZE/2-25, myRoot);
        makeLabel("Percent Satisfy:", MENU_SIZE/3, SIZE/2, myRoot);
        TextField satisfyPercent = makeTextField("%", MENU_SIZE/3,  SIZE/2 + 25, myRoot);
        Button segregationReset = makeButton("Reset With New Parameters", MENU_SIZE/3, SIZE/2 + 60, myRoot);
        segregationReset.setOnAction(new EventHandler<>() {
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
        makeLabel("Percent Blocked:", MENU_SIZE/3, SIZE/2 - 150, myRoot);
        TextField percentBlocked = makeTextField("%", MENU_SIZE/3,  SIZE/2 - 125, myRoot);
        makeLabel("Percent Open:", MENU_SIZE/3, SIZE/2 - 100, myRoot);
        TextField percentOpen = makeTextField("%", MENU_SIZE/3,  SIZE/2 - 75, myRoot);
        makeLabel("Percent Percolated:", MENU_SIZE/3, SIZE/2 -50, myRoot);
        TextField percentPercolated = makeTextField("%", MENU_SIZE/3,  SIZE/2-25, myRoot);
        Button percolationReset = makeButton("Reset With New Parameters", MENU_SIZE/3, SIZE/2 + 10, myRoot);
        percolationReset.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                blockPercentagesUpdated.add(Double.valueOf(percentBlocked.getText()));
                blockPercentagesUpdated.add(Double.valueOf(percentOpen.getText()));
                blockPercentagesUpdated.add(Double.valueOf(percentPercolated.getText()));
                restart = true;
                newParameters = true;
            }
        });
    }

    private void addGameOfLifeButtons() {
        makeLabel("Percent Alive:", MENU_SIZE/3, SIZE/2 - 150, myRoot);
        TextField percentAlive = makeTextField("%", MENU_SIZE/3,  SIZE/2 - 125, myRoot);
        makeLabel("Percent Dead:", MENU_SIZE/3, SIZE/2 -100, myRoot);
        TextField percentDead = makeTextField("%", MENU_SIZE/3,  SIZE/2 - 75, myRoot);
        Button gameOfLifeReset = makeButton("Reset With New Parameters", MENU_SIZE/3, SIZE/2 - 40, myRoot);
        gameOfLifeReset.setOnAction(new EventHandler<>() {
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
        makeLabel("Percent Tree:", MENU_SIZE/3, SIZE/2 - 150, myRoot);
        TextField percentTree = makeTextField("%", MENU_SIZE/3,  SIZE/2 - 125, myRoot);
        makeLabel("Percent Fire:", MENU_SIZE/3, SIZE/2 -100, myRoot);
        TextField percentFire = makeTextField("%", MENU_SIZE/3,  SIZE/2 - 75, myRoot);
        makeLabel("Percent Dead:", MENU_SIZE/3, SIZE/2 -50, myRoot);
        TextField percentDead = makeTextField("%", MENU_SIZE/3,  SIZE/2-25, myRoot);
        makeLabel("Probability Catch:", MENU_SIZE/3, SIZE/2, myRoot);
        TextField probCatch = makeTextField("%", MENU_SIZE/3,  SIZE/2 + 25, myRoot);
        makeLabel("Probability Die:", MENU_SIZE/3, SIZE/2 + 50, myRoot);
        TextField probDie = makeTextField("%", MENU_SIZE/3,  SIZE/2 + 75, myRoot);
        Button fireReset = makeButton("Reset With New Parameters", MENU_SIZE/3, SIZE/2 + 110, myRoot);
        fireReset.setOnAction(new EventHandler<>() {
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

    private Scene setUpSplash(){
        myRoot = new Group();
        Button fireButton = makeButton("Fire", SIZE/2, (SIZE/2)+50, myRoot);
        Button gameOfLifeButton = makeButton("GameOfLife", SIZE/2, SIZE/2, myRoot);
        Button segregationButton = makeButton("Segregation", SIZE/2, SIZE/2 - 50, myRoot);
        Button predatorPreyButton = makeButton("PredatorPrey", SIZE/2, SIZE/2 - 100, myRoot);
        Button percolationButton = makeButton("Percolation", SIZE/2, SIZE/2 + 100, myRoot);
        Button[] buttons = new Button[]{fireButton, gameOfLifeButton, segregationButton, predatorPreyButton, percolationButton};
        String[] simulationNames = new String[]{"fire", "gameoflife", "percolation", "segregation", "predprey"};
        for(int i=0; i<buttons.length; i++){
            int finalI = i;
            buttons[i].setOnAction(new EventHandler<>() {
                @Override
                public void handle(ActionEvent event) {
                    file_name = "./src/cellsociety/View/" + simulationNames[finalI] + "_config.xml";
                    lastSimulationRan = file_name;
                }
            });
        }
        return new Scene(myRoot, SIZE + MENU_SIZE + GRAPH_SIZE, SIZE,  SPLASHBACKGROUND);
    }

    private Button makeButton(String text, int x, int y, Group the_group){
        Button myButton = new Button();
        myButton.setText(text);
        myButton.setLayoutX(x);
        myButton.setLayoutY(y);
        the_group.getChildren().add(myButton);
        return myButton;
    }
    private Label makeLabel(String text, int x, int y, Group the_group){
        Label label = new Label();
        label.setText(text);
        label.setLayoutX(x);
        label.setLayoutY(y);
        the_group.getChildren().add(label);
        return label;
    }
    private TextField makeTextField(String text, int x, int y, Group the_group){
        TextField textField = new TextField();
        textField.setText(text);
        textField.setLayoutX(x);
        textField.setLayoutY(y);
        the_group.getChildren().add(textField);
        return textField;
    }
    public void addDoneButton(){
        doneButton = makeButton("Run Another Simulation", MENU_SIZE/3, SIZE/2 - 250, myRoot);
        setSplashScreenFinished(true);
        doneButton.setOnAction(new EventHandler<>() {
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
        endButton = makeButton("End Simulation", MENU_SIZE/3, SIZE/2 - 250, myRoot);
        setSplashScreenFinished(true);
        endButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                myRoot.getChildren().remove(doneButton);
                addDoneButton();
            }
        });
    }
    public void addResetButton(){
        Button resetButton = makeButton("Reset Simulation", MENU_SIZE / 3, SIZE / 2 + -200, myRoot);
        setSplashScreenFinished(true);
        resetButton.setOnAction(new EventHandler<>() {
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