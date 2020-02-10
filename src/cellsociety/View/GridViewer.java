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

import java.io.*;
import java.nio.charset.StandardCharsets;
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
    private String PAUSE_TEXT;
    private String RESET_TEXT;
    private String END_TEXT;
    private String RUN_SIM_TEXT;
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
    private final int[] OFFSETS = {10, 25, 40, 50, 60, 75, 100, 110, 125, 150, 200, 250};

    /**
     * Constructor method, sets up the stage and splash screen
     */
    public GridViewer() throws IOException {
        initateButtonText();
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
    public void setUpSimulation(int rowSize, int colSize, ArrayList<Integer> initial_states) throws IOException {
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

    /**
     * Set the map of state number to name
     * @param stateNames map of state number to name
     */
    public void setStateNames(HashMap<Integer, String> stateNames){
        this.stateNames = stateNames;
    }

    /**
     * Displays a pop up when an error occurs, telling the user to exit
     */
    public void displayPopUp(String errorMessage){
        Group errorRoot = new Group();
        Stage errorStage = new Stage();
        Scene errorScene = setUpPopUp(errorStage, errorRoot, errorMessage);
        errorStage.setScene(errorScene);
        errorStage.show();
        setSplashScreenFinished(true);
        myStage.close();
    }

    /**
     * assume grid is always square. For different grid shapes, just need to change this method (and stuff in grid class)
     * @param rowSize number of columns
     * @param colSize number of rows
     * @param initial_states integer list representing initial cell states. Order is "arbitrary", this method interprets it
     */
    public void setUpGrid(int rowSize, int colSize, ArrayList<Integer> initial_states) throws IOException {
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
        Button pauseButton = makeButton(PAUSE_TEXT, SIZE/2, (25), myRoot);
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
     * getter for gridParametersUpdated
     * @return gridParametersUpdated
     */
    public ArrayList<Double> getGridParametersUpdated(){
        return gridParametersUpdated;
    }

    /**
     * getter for getBlockPercentagesUpdated
     * @return getBlockPercentagesUpdated
     */
    public ArrayList<Double> getBlockPercentagesUpdated(){
        return blockPercentagesUpdated;
    }

    /**
     * getter for getNewParameters
     * @return getNewParameters
     */
    public boolean getNewParameters(){
        return newParameters;
    }

    /**
     * getter for getSplashScreenFinished
     * @return getSplashScreenFinished
     */
    public boolean getSplashScreenFinished(){
        return splashScreenFinished;
    }

    /**
     * setter for setSplashScreenFinished
     * @param target is the setSplashScreenFinished value
     */
    public void setSplashScreenFinished(boolean target){
        splashScreenFinished = target;
    }

    /**
     * getter for file_name
     * @return file_name
     */
    public String getFileName(){
        return file_name;
    }

    /**
     * setter for file_name
     * @param newFileName is file_name value
     */
    public void setFileName(String newFileName){
        file_name = newFileName;
    }

    /**
     * Add a button to take you back to splash screen
     */
    public void addDoneButton(){
        doneButton = makeButton(RUN_SIM_TEXT, MENU_SIZE/3, SIZE/2 - OFFSETS[11], myRoot);
        setSplashScreenFinished(true);
        doneButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                myRoot.getChildren().remove(endButton);
                try {
                    myScene = setUpSplash();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                myStage.setScene(myScene);
                myStage.show();
            }
        });
    }

    /**
     * getter for restart that determines whether to restart simulation or not
     * @return restart value
     */
    public boolean getRestart(){
        return restart;
    }

    /**
     * Add button to end simulation
     */
    public void addResetButton(){
        Button resetButton = makeButton(RESET_TEXT, MENU_SIZE / 3, SIZE / 2 - OFFSETS[10], myRoot);
        setSplashScreenFinished(true);
        resetButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                restart = true;
            }
        });
    }

    /**
     * sets the pop up error screen
     * @param errorStage stage to set
     * @param errorRoot group to set
     * @param errorMessage message to show
     * @return
     */
    private Scene setUpPopUp(Stage errorStage, Group errorRoot, String errorMessage){
        //TextField  message = makeTextField("Edit Configuration File to Input Correct Simulation Type", MENU_SIZE/3,  SIZE/2 + 75, errorRoot)
        Label message = makeLabel(errorMessage,GRAPH_SIZE/4,  (GRAPH_SIZE/3)+OFFSETS[3], errorRoot);
        Button exitButton = makeButton("Exit", (GRAPH_SIZE/4) + 25 , (GRAPH_SIZE/3)+OFFSETS[5], errorRoot);
        exitButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                errorStage.close();
            }
        });
        return new Scene(errorRoot, GRAPH_SIZE, GRAPH_SIZE,  SPLASHBACKGROUND);
    }
    /**
     * initates the main button texts
     * @throws IOException
     */
    private void initateButtonText() throws IOException {
        File file = new File("./src/cellsociety/View/Text_Files/generic_buttons.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        PAUSE_TEXT = br.readLine();
        END_TEXT = br.readLine();
        RUN_SIM_TEXT = br.readLine();
        RESET_TEXT = br.readLine();
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

    private void addAdditionalButtons(String file_name) throws IOException {
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

    private void addPredatorPreyButtons() throws IOException {
        File file = new File("./src/cellsociety/View/Text_Files/predatorprey_buttons.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        makeLabel(br.readLine(), MENU_SIZE/3, SIZE/2 - OFFSETS[9], myRoot);
        TextField percentWater = makeTextField("%", MENU_SIZE/3,  SIZE/2 - OFFSETS[8], myRoot);
        makeLabel(br.readLine(), MENU_SIZE/3, SIZE/2 -OFFSETS[6], myRoot);
        TextField percentFish = makeTextField("%", MENU_SIZE/3,  SIZE/2 - OFFSETS[5], myRoot);
        makeLabel(br.readLine(), MENU_SIZE/3, SIZE/2 -OFFSETS[3], myRoot);
        TextField percentShark = makeTextField("%", MENU_SIZE/3,  SIZE/2-OFFSETS[1], myRoot);
        Button predatorPreyReset = makeButton(br.readLine(), MENU_SIZE/3, SIZE/2 + OFFSETS[0], myRoot);
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

    private void addSegregationButtons() throws IOException {
        File file = new File("./src/cellsociety/View/Text_Files/segregation_buttons.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        makeLabel(br.readLine(), MENU_SIZE/3, SIZE/2 - OFFSETS[9], myRoot);
        TextField percentEmpty = makeTextField("%", MENU_SIZE/3,  SIZE/2 - OFFSETS[8], myRoot);
        makeLabel(br.readLine(), MENU_SIZE/3, SIZE/2 -OFFSETS[6], myRoot);
        TextField percentRed = makeTextField("%", MENU_SIZE/3,  SIZE/2 - OFFSETS[5], myRoot);
        makeLabel(br.readLine(), MENU_SIZE/3, SIZE/2 -OFFSETS[3], myRoot);
        TextField percentBlue = makeTextField("%", MENU_SIZE/3,  SIZE/2-OFFSETS[1], myRoot);
        makeLabel(br.readLine(), MENU_SIZE/3, SIZE/2, myRoot);
        TextField satisfyPercent = makeTextField("%", MENU_SIZE/3,  SIZE/2 + OFFSETS[1], myRoot);
        Button segregationReset = makeButton(br.readLine(), MENU_SIZE/3, SIZE/2 + OFFSETS[4], myRoot);
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

    private void addPercolationButtons() throws IOException {
        File file = new File("./src/cellsociety/View/Text_Files/percolation_buttons.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        makeLabel(br.readLine(), MENU_SIZE/3, SIZE/2 - OFFSETS[9], myRoot);
        TextField percentBlocked = makeTextField("%", MENU_SIZE/3,  SIZE/2 - OFFSETS[8], myRoot);
        makeLabel(br.readLine(), MENU_SIZE/3, SIZE/2 - OFFSETS[6], myRoot);
        TextField percentOpen = makeTextField("%", MENU_SIZE/3,  SIZE/2 - OFFSETS[5], myRoot);
        makeLabel(br.readLine(), MENU_SIZE/3, SIZE/2 -OFFSETS[3], myRoot);
        TextField percentPercolated = makeTextField("%", MENU_SIZE/3,  SIZE/2-OFFSETS[1], myRoot);
        Button percolationReset = makeButton(br.readLine(), MENU_SIZE/3, SIZE/2 + OFFSETS[0], myRoot);
        percolationReset.setOnAction(new EventHandler<ActionEvent>(){
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

    private void addGameOfLifeButtons() throws IOException {
        File file = new File("./src/cellsociety/View/Text_Files/gameoflife_buttons.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        makeLabel(br.readLine(), MENU_SIZE/3, SIZE/2 - OFFSETS[9], myRoot);
        TextField percentAlive = makeTextField("%", MENU_SIZE/3,  SIZE/2 - OFFSETS[8], myRoot);
        makeLabel(br.readLine(), MENU_SIZE/3, SIZE/2 -OFFSETS[6], myRoot);
        TextField percentDead = makeTextField("%", MENU_SIZE/3,  SIZE/2 - OFFSETS[5], myRoot);
        Button gameOfLifeReset = makeButton(br.readLine(), MENU_SIZE/3, SIZE/2 - OFFSETS[2], myRoot);
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

    private void addFireButtons() throws IOException {
        File file = new File("./src/cellsociety/View/Text_Files/fire_buttons.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        makeLabel(br.readLine(), MENU_SIZE/3, SIZE/2 - OFFSETS[9], myRoot);
        TextField percentTree = makeTextField("%", MENU_SIZE/3,  SIZE/2 - OFFSETS[8], myRoot);
        makeLabel(br.readLine(), MENU_SIZE/3, SIZE/2 -OFFSETS[6], myRoot);
        TextField percentFire = makeTextField("%", MENU_SIZE/3,  SIZE/2 - OFFSETS[5], myRoot);
        makeLabel(br.readLine(), MENU_SIZE/3, SIZE/2 -OFFSETS[3], myRoot);
        TextField percentDead = makeTextField("%", MENU_SIZE/3,  SIZE/2-OFFSETS[1], myRoot);
        makeLabel(br.readLine(), MENU_SIZE/3, SIZE/2, myRoot);
        TextField probCatch = makeTextField("%", MENU_SIZE/3,  SIZE/2 + OFFSETS[1], myRoot);
        makeLabel(br.readLine(), MENU_SIZE/3, SIZE/2 + OFFSETS[3], myRoot);
        TextField probDie = makeTextField("%", MENU_SIZE/3,  SIZE/2 + OFFSETS[5], myRoot);
        Button fireReset = makeButton(br.readLine(), MENU_SIZE/3, SIZE/2 + OFFSETS[7], myRoot);

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

    private Scene setUpSplash() throws IOException {
        File file = new File("./src/cellsociety/View/Text_Files/splash_buttons.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        myRoot = new Group();
        Button fireButton = makeButton(br.readLine(), SIZE/2, (SIZE/2)+ OFFSETS[3], myRoot);
        Button gameOfLifeButton = makeButton(br.readLine(), SIZE/2, SIZE/2, myRoot);
        Button segregationButton = makeButton(br.readLine(), SIZE/2, SIZE/2 - OFFSETS[3], myRoot);
        Button predatorPreyButton = makeButton(br.readLine(), SIZE/2, SIZE/2 - OFFSETS[6], myRoot);
        Button percolationButton = makeButton(br.readLine(), SIZE/2, SIZE/2 + OFFSETS[6], myRoot);
        Button[] buttons = new Button[]{fireButton, gameOfLifeButton, segregationButton, predatorPreyButton, percolationButton};
        String[] simulationNames = new String[]{"fire", "gameoflife", "segregation", "predprey", "percolation"};
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

    private void addEndButton(){
        endButton = makeButton(END_TEXT, MENU_SIZE/3, SIZE/2 - OFFSETS[11], myRoot);
        setSplashScreenFinished(true);
        endButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                myRoot.getChildren().remove(doneButton);
                addDoneButton();
            }
        });
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

    }

}