package cellsociety.Control;
import cellsociety.Control.Exception.InvalidNeighborException;
import cellsociety.Control.Exception.MissingParametersException;
import cellsociety.Control.Exception.RowColMismatchException;
import cellsociety.Control.Exception.SimulationTypeException;
import cellsociety.Model.Grid.Grid;
import cellsociety.Model.Grid.FireGrid;
import cellsociety.Model.Cell.FireCell;
import cellsociety.View.GridViewer;
import cellsociety.Model.Cell.SegregationCell;
import cellsociety.Model.Grid.SegregationGrid;
import cellsociety.Model.Grid.GameOfLifeGrid;
import cellsociety.Model.Grid.PredatorPreyGrid;
import cellsociety.Model.Grid.PercolationGrid;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Class that drives the simulation and communicates with the grid and viewer classes
 * @author caryshindell, lornezhang, ameersyedibrahim
 * Dependencies: Grid class, GridViewer class, configuration files
 * Example: a game engine running the percolation simulation
 * Assumptions: rectangular grid
 */
public class GameEngine {
    public static final double FRAMES_PER_SECOND = 1;
    public static final double MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final String[] allSimTypes = {"fire", "percolation", "gameoflife", "segregation", "predatorprey"};

    private final String MESSAGE_1 = "Error: Edit Simulation Type in Configuration File";
    private final String MESSAGE_2 = "Error: Invalid Cell States Provided in Configuration File";
    private final String MESSAGE_3 = "Error: Block Percentages DO NOT Sum to 1";
    private final String MESSAGE_4 = "Error: Row, Columns do not multiply to Total Number of Blocks";
    private final String MESSAGE_5 = "Error: Invalid Neighbors Provided in Configuration File";
    private final String MESSAGE_6 = "Error: One or More Parameters Missing in Configuration File";
    public static final String defaultFilePath = "./src/cellsociety/View/default_config.xml";

    private Timeline animation;
    private String simType;
    private String parseMethod;
    private ArrayList<Integer> cellStates;
    private ArrayList<Boolean> neighbors;
    private Grid myGrid;
    private GridViewer myViewer;
    private int row;
    private int col;
    private int totalBlocks;
    private boolean enableDefault;
    private ArrayList<Double> blockPercentages;
    private String lastSimulationRun;
    private ArrayList<Double> gridParameters;
    private Document doc;
    private int[] possibleStates;
    private int[] edgeBehavior;


    private boolean done;
    private int validNumNeighbors;

    /**
     * Constructor method. initializes some things and cells start
     */
    public GameEngine() throws IOException {
        start();
    }

    /**
     * Creates the viewer, starts the splash screen. Viewer will determine configuration file
     */
    public void start() throws IOException {
        myViewer = new GridViewer();
        enableDefault = false;
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> {
            try {
                step();
            } catch (IOException ex) {
                System.out.println("Caught IO Exception");
            } catch (SAXException ex) {
                System.out.println("Caught SAXException");
            } catch (Exception ex) {

                    System.out.println("Caught Exception");

            }
        });
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }


    /**
     * Accept a String XML file path and extract all information from file
     * @param sim_xml_path string representation of file path
     * Assigns values from file to instance variables
     */
    private void parseFile(String sim_xml_path) throws Exception{
        File fXmlFile = new File(sim_xml_path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(fXmlFile);
        cellStates = new ArrayList<>();
        gridParameters = new ArrayList<>();
        neighbors = new ArrayList<>();
        edgeBehavior = new int[3];
        validNumNeighbors = 0;
        System.out.println("CP 1");
        //extractSimulationParameters();
        System.out.println("CP 2");
        if (errorChecking()){
            return;
        }
        System.out.println("CP 3");
        System.out.println("Type: " + simType);
        this.assignPossibleStates();
        System.out.println("CP 4");
        if (parseMethod.equals("percentage")){
            parseByPercentage();
        }else{
            parseLongList();
        }
        System.out.println("CP 5");
        this.initializeGrid(gridParameters, row, col, neighbors);
    }


    /**
     * 1 of 2 ways to parse the configuration file that looks at all the states specified in
     * the file and loads them into the grid.
     * Useful for small grid sizes or when users want to specify how the grid looks
     * */
    private void parseLongList(){

        NodeList states_list = doc.getElementsByTagName("state");

        try{
            checkValidCellStates(states_list);
        }catch(Exception ex){
            System.out.println("Exception: One or more Cell States is Invalid");
            myViewer.displayPopUp(MESSAGE_2);
            animation.stop();
        }

        for(int i=0; i<states_list.getLength(); i++){
            cellStates.add(Integer.valueOf(states_list.item(i).getTextContent()));
        }
    }

    /**
     * 2nd way to parse the configuration file. Allows for much more cells to appear on grid and allows user to
     * specify which percentage of each cell will appear.
     */

    private void parseByPercentage(){
        blockPercentages = new ArrayList<>();
        ArrayList<Integer> blockTypes = new ArrayList<>();

        NodeList blockTypesRead = doc.getElementsByTagName("blocktype");

        try{
            checkValidCellStates(blockTypesRead);
        }catch(Exception ex){
            System.out.println("Exception: One or more Cell States is Invalid");
            myViewer.displayPopUp(MESSAGE_2);
            animation.stop();
        }

        for(int i=0; i<blockTypesRead.getLength(); i++){
            blockTypes.add(Integer.valueOf(blockTypesRead.item(i).getTextContent()));
        }

        NodeList blockPercentagesRead = doc.getElementsByTagName("percentage");

        for(int i=0; i<blockPercentagesRead.getLength(); i++){
            blockPercentages.add(Double.valueOf(blockPercentagesRead.item(i).getTextContent()));
        }

        try{
            checkProbSum();
        }catch(Exception ex){
            System.out.println("Exception: Percentages Don't Sum to 1");
            myViewer.displayPopUp(MESSAGE_3);
            animation.stop();
        }

        if(myViewer.getNewParameters()){
            gridParameters = myViewer.getGridParametersUpdated();
            blockPercentages = myViewer.getBlockPercentagesUpdated();
        }
        assignCellStates(blockTypes);

    }


    /**
     * Depending on the simulation type, determine all the possible states that should be looked at in the simulation.
     * Used for error catching in case user enters an invalid state in the configuration file
     */
    private void assignPossibleStates(){
        switch (simType) {
            case "fire":
                possibleStates = FireGrid.possibleStates;
                break;
            case "gameoflife":
                possibleStates = GameOfLifeGrid.possibleStates;
                break;
            case "segregation":
                possibleStates = SegregationGrid.possibleStates;
                break;
            case "predatorprey":
                possibleStates = PredatorPreyGrid.possibleStates;
                break;
            case "percolation":
                possibleStates = PercolationGrid.possibleStates;
                break;

        }
    }

    /**
     * Depending on the simulation type, initialize the grid from its corresponding class
     * Also sets the possibleStates instance variable
     * */
    private void initializeGrid(ArrayList<Double> gridParameters, int rowSize, int colSize, ArrayList<Boolean> ignoredNeighbors){
        int[] edgeParams = edgeBehavior;
        switch (simType) {
            case "fire":
                myGrid = new FireGrid(rowSize, colSize, cellStates, ignoredNeighbors, edgeParams);
                FireCell.setProb(gridParameters.get(0), gridParameters.get(1));
                break;
            case "gameoflife":
                myGrid = new GameOfLifeGrid(rowSize, colSize, cellStates, ignoredNeighbors, edgeParams);
                break;
            case "segregation":
                myGrid = new SegregationGrid(rowSize, colSize, cellStates, ignoredNeighbors, edgeParams);
                SegregationCell.setProb(gridParameters.get(0));
                break;
            case "predatorprey":
                myGrid = new PredatorPreyGrid(rowSize, colSize, cellStates, ignoredNeighbors, edgeParams);
                break;
            case "percolation":
                myGrid = new PercolationGrid(rowSize, colSize, cellStates, ignoredNeighbors, edgeParams);
                break;

        }
        myViewer.setStateNames(myGrid.getStateNames());
    }


    /**
     * Step function that repeatedly runs to render the simulation
     */
    private void step() throws Exception {
        if(!myViewer.getSplashScreenFinished()){
            System.out.println("stepping");
            if(myViewer.getRestart()){
                myViewer.setSplashScreenFinished(true);
            }
            animation.setRate(myViewer.getScrollValue());
            if(!myViewer.getPause() && !myGrid.checkIfDone()){
                ArrayList<int[]> currStates = myGrid.updateGrid();
                myViewer.updateCellStates(currStates);
            }
            if(myGrid.checkIfDone() && !done){
                myViewer.addDoneButton();
                done = true;
            }

        }else{
            System.out.println("done stepping");
            String sim_xml_path = myViewer.getFileName(); // should return the path of the XML file
            if(myViewer.getRestart()){
                sim_xml_path = lastSimulationRun;
            }
            // see if the viewer has determined the file name yet
            // if so, parse the file name and we are done with the splash screen
            if(!sim_xml_path.equals("NONE")){
                    lastSimulationRun = sim_xml_path;
                    System.out.println(sim_xml_path);
                    parseFile(sim_xml_path);
                    myViewer.setUpSimulation(row,col,cellStates);
                    myViewer.setSplashScreenFinished(false);
                    done = false;
                    myViewer.setFileName("NONE");
            }
        }
    }


    /**
     * If an invalid simulation type is entered, an exception is thrown
     * @param s the string parsed which is the simulation type parameter entered in the config file
     */
    private void isValidSimType(String s) throws Exception {

        for (String allSimType : allSimTypes) {
            if (allSimType.equals(s)) {
                return;
            }
        }

        throw new Exception("Simulation Type Invalid Exception");
    }


    /**
     * Iterate through all the states specified in either format of the config file and determines if any are invalid
     * @param states list of all the state integers specified in the config file
     */
    private void checkValidCellStates(NodeList states) throws Exception{
        int state;
        System.out.println("HI");
        System.out.println("Possible States: " + possibleStates);
        for(int i = 0; i<states.getLength(); i++){
            state = Integer.parseInt(states.item(i).getTextContent());
            System.out.println("State: " + state);

            if(!checkIfStateInSim(state)){
                throw new Exception("One Or More Cells Have Incorrect States");
            }
        }
    }


    /**
     * Iterate through all the possible states and determine if the state in question is valid or invalid
     * @param state a single state parameter from config file
     */
    private boolean checkIfStateInSim(int state){

        for(int i: possibleStates){
            if (state == i){
                return true;
            }
        }
        return false;
    }

    /**
     * Assign the row and column instance variables from the config file
     * @param sizeList two element list containing rowand col parameters
     */

    private void setRowCol(NodeList sizeList){
        row = Integer.parseInt(sizeList.item(0).getTextContent());
        col = Integer.parseInt(sizeList.item(1).getTextContent());
    }


    /**
     * Assign the gridParameter isntance varaible from the config file
     * @param paramList list containing all the grid parameters specific to that simulation
     */
    private void setParameters(NodeList paramList){
        if (paramList.getLength() == 0){
            throw new NullPointerException();
        }
        for(int i=0; i<paramList.getLength(); i++){
            gridParameters.add(Double.valueOf(paramList.item(i).getTextContent()));
        }
    }

    /**
     * Assign the edgebehavior list from the config file
     * @param edges three element list that governs the relationship between cell edges
     */
    private void determineEdgeBehavior(NodeList edges){

        if (edges.getLength() == 0){
            throw new NullPointerException();
        }
        for(int i=0; i<edges.getLength(); i++){
            edgeBehavior[i] = Integer.valueOf(edges.item(i).getTextContent());
        }
    }

    /**
     * Assign the neighbors instance variable to determine which of a cells neighbors interact with one another
     * @param neighborList list of neighbors that either say true or false to indicate if the cells interact with
     *  the neighbors or not
     */
    private void allocateNeighbors(NodeList neighborList){

        if (neighborList.getLength() == 0){
            throw new NullPointerException();
        }
        for(int i=0; i<neighborList.getLength(); i++){
            String isNeighborConsidered = neighborList.item(i).getTextContent();
            if(isNeighborConsidered.equals("true")){
                neighbors.add(true);
                validNumNeighbors++;
            }
            else if(isNeighborConsidered.equals("false")){
                neighbors.add(false);
                validNumNeighbors++;
            }else{
                neighbors.add(false);

            }
        }
    }

    /**
     * For the percentage-based config file, look at the different block types and generate
     * a list of cellstate based on the frequency of each block type
     * @param blockTypes list contianing the different cell types present
     */
    private void assignCellStates(ArrayList<Integer> blockTypes){
        int count = 0;
        int defaultBlock = blockTypes.get(0);
        for(int i=0; i<blockTypes.size(); i++){
            System.out.println(totalBlocks*(blockPercentages.get(i)));
            for(int w=0; w<totalBlocks*(blockPercentages.get(i))-1; w++){
                cellStates.add(blockTypes.get(i));
                count ++;
            }
        }
        for(int i=0; i<totalBlocks-count;i++){
            cellStates.add(defaultBlock);
        }
        Collections.shuffle(cellStates);
    }

    /**
     * Check if the block percentages sum to 1, otherwise throw an exception
     */
    private void checkProbSum() throws Exception{
        Double sum = blockPercentages.stream().mapToDouble(i-> i).sum();

        if(sum != 1.0){
            throw new Exception("Block Percentages DO NOT Sum to 1");
        }

        return;
    }
    /**
     * If the row and column parameters do not multiply to the total number of blocks, throw a custom exception
     */
    private void checkBlockNumber() throws RowColMismatchException{

        if (row*col != totalBlocks){
            throw new RowColMismatchException("Row, Col do not multiply to total blocks");
        }

        return;
    }

    /**
     * If one or more of the neighbor parameters are invalid (i.e. not true or false), throw a custom exception
     */
    private void checkNeighbors() throws InvalidNeighborException{

        if(this.validNumNeighbors != neighbors.size()){
            throw new InvalidNeighborException("Incorrect Neighbor Parameters");
        }

        return;
    }

    /**
     * If the user enters no simulation type (includes no tag) in the config file, throw a custom exception
     */
    private void extractSimulationType() throws SimulationTypeException {
        try{
            this.simType = doc.getElementsByTagName("sim_type").item(0).getTextContent();
        }catch(NullPointerException ne){
            throw new SimulationTypeException(MESSAGE_1, ne);
        }
    }

    /**
     * If the user enters an invalid simulation type in the config file, throw a custom exception
     */
    private void determineValidSimulationType() throws SimulationTypeException {
        try{
            isValidSimType(simType);
        }catch(Exception ex){
            throw new SimulationTypeException(MESSAGE_1, ex);
        }
    }

    /**
     * Method that assigns many of the instance variables in this class by reading in values from the config file.
     * It is important to check if one or more of these values are not specified by the user in the config file. Thus,
     * we catch any exceptions
     */
    private void extractSimulationParameters() throws NullPointerException, MissingParametersException {
            try{
            parseMethod = doc.getElementsByTagName("parse").item(0).getTextContent();
            NodeList sizeList = doc.getElementsByTagName("size"); //eventually add row and col parameters in config file
            setRowCol(sizeList);
            extractParams();
            NodeList neighborList = doc.getElementsByTagName("neighbor");
            allocateNeighbors(neighborList);
            NodeList edgeTypesList = doc.getElementsByTagName("edge");
            determineEdgeBehavior(edgeTypesList);
            NodeList blocksInput = doc.getElementsByTagName("totalblocks");
            totalBlocks = Integer.parseInt(blocksInput.item(0).getTextContent());}
            catch (NullPointerException ne){
                throw new MissingParametersException("One or more parameters missing", ne);
            }


    }

    /**
     * Catches most the custom errors which throw specific exceptions and handles them
     * by stopping the simulation and displaying a popup window describing the error
     */
    private boolean errorChecking(){

        try{
            extractSimulationType();
            determineValidSimulationType();

        }catch(SimulationTypeException ex){
            myViewer.displayPopUp(MESSAGE_1);
            animation.stop();
            return true;
        }
        try{
            extractSimulationParameters();
        }catch(MissingParametersException ex){
            myViewer.displayPopUp(MESSAGE_6);
            animation.stop();
            return true;
        }


        try{
            checkBlockNumber();
        }catch(RowColMismatchException ex){
            myViewer.displayPopUp(MESSAGE_4);
            animation.stop();
            return true;
        }

        try{
            checkNeighbors();
        }catch(InvalidNeighborException ex){
            System.out.println("Exception: Neighbors");
            myViewer.displayPopUp(MESSAGE_5);
            animation.stop();
            return true;
        }
        return false;
    }

    /**
     * For the two classes with parameters, extract them
     */
    private void extractParams(){
        if(simType.equals("fire") || simType.equals("segregation")){
            NodeList paramList = doc.getElementsByTagName("param");
            setParameters(paramList);
        }
    }
}