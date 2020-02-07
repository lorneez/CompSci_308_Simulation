package cellsociety.Control;
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
import javax.xml.parsers.ParserConfigurationException;

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
import java.util.OptionalDouble;

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

    private final String MESSAGE_1 = "Error: Edit Configuration File";
    private final String MESSAGE_2 = "Error: Invalid Cell States Provided in Configuration File";
    private final String MESSAGE_3 = "Error: Block Percentages DO NOT Sum to 1";
    private final String MESSAGE_4 = "Error: Row, Columns do not multiply to Total Number of Blocks";
    private final String MESSAGE_5 = "Error: Invalid Neighbors Provided in Configuration File";

    private Timeline animation;
    private String simType;
    private ArrayList<Integer> cellStates;
    private ArrayList<Boolean> neighbors;
    private Grid myGrid;
    private GridViewer myViewer;
    private int row;
    private int col;
    private int totalBlocks;
    private ArrayList<Double> blockPercentages;
    private String lastSimulationRun;
    private ArrayList<Double> gridParameters;
    private Document doc;
    private int[] possibleStates;
    private int[] edgeBehavior;


    private boolean done;

    /**
     * Constructor method. initializes some things and cells start
     */
    public GameEngine(){
        start();
    }

    /**
     * Creates the viewer, starts the splash screen. Viewer will determine configuration file
     */
    public void start(){
        myViewer = new GridViewer();
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> {
            try {
                step();
            } catch (IOException ex) {
                System.out.println("Caught IO Exception");
            } catch (SAXException ex) {
                System.out.println("Caught SAXException");
            } catch (ParserConfigurationException ex) {
                System.out.println("Caught ParserConfigException");
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
    private void parseFile(String sim_xml_path) throws IOException, SAXException, ParserConfigurationException {
        File fXmlFile = new File(sim_xml_path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(fXmlFile);
        cellStates = new ArrayList<>();
        gridParameters = new ArrayList<>();
        neighbors = new ArrayList<>();
        String parseMethod = doc.getElementsByTagName("parse").item(0).getTextContent();
        edgeBehavior = new int[3];
        NodeList sizeList = doc.getElementsByTagName("size"); //eventually add row and col parameters in config file
        setRowCol(sizeList);
        NodeList paramList = doc.getElementsByTagName("param");
        setParameters(paramList);
        NodeList neighborList = doc.getElementsByTagName("neighbor");
        allocateNeighbors(neighborList);
        NodeList edgeTypesList = doc.getElementsByTagName("edge");
        determineEdgeBehavior(edgeTypesList);
        NodeList blocksInput = doc.getElementsByTagName("totalblocks");
        totalBlocks = Integer.parseInt(blocksInput.item(0).getTextContent());
        try{
            this.simType = doc.getElementsByTagName("sim_type").item(0).getTextContent();
        }catch(NullPointerException ne){
            System.out.println("Exception: No Simulation Type Given");
            myViewer.displayPopUp(MESSAGE_1);
            animation.stop();
        }
        try{
            isValidSimType(simType);
        }catch(Exception ex){
            System.out.println("Exception: Invalid Simulation Type");
            myViewer.displayPopUp(MESSAGE_1 );
            animation.stop();
        }

        try{
            checkBlockNumber();
        }catch(Exception ex){
            System.out.println("Exception: Row*Col does not Match BlockNumbers");
            myViewer.displayPopUp(MESSAGE_4);
            animation.stop();
        }

        try{
            checkNeighbors();
        }catch(Exception ex){
            System.out.println("Exception: Neighbors");
            myViewer.displayPopUp(MESSAGE_5);
            animation.stop();
        }


        this.assignPossibleStates();
        if (parseMethod.equals("percentage")){
            parseByPercentage();
        }else if (parseMethod.equals("longlist")){
            parseLongList();
        }
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
            System.out.println("Exception: One or more Cell States is Invaliddd");
            myViewer.displayPopUp(MESSAGE_2);
            animation.stop();
        }

        for(int i=0; i<states_list.getLength(); i++){
            cellStates.add(Integer.valueOf(states_list.item(i).getTextContent()));
        }
        NodeList neighbor_list = doc.getElementsByTagName("neighbor");
        for(int i=0; i<neighbor_list.getLength(); i++){
            String isNeighborConsidered = neighbor_list.item(i).getTextContent();

            if(isNeighborConsidered.equals("true")){
                neighbors.add(true);
            }
            else {
                neighbors.add(false);
            }
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

    private void assignPossibleStates(){
        switch (simType) {
            case "fire":
                possibleStates = FireGrid.possibleStates;
                break;
            case "gameoflife":
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

    private void step() throws IOException, SAXException, ParserConfigurationException {
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
                    parseFile(sim_xml_path);
                    myViewer.setUpSimulation(row,col,cellStates);
                    myViewer.setSplashScreenFinished(false);
                    done = false;
                    myViewer.setFileName("NONE");
            }
        }
    }

    private void isValidSimType(String s) throws Exception {

        for (String allSimType : allSimTypes) {
            if (allSimType.equals(s)) {
                return;
            }
        }

        throw new Exception("Simulation Type Invalid Exception");
    }

    private void checkValidCellStates(NodeList states) throws Exception{
        int state;
        System.out.println("HI");
        System.out.println("Possible States: " + possibleStates[0]);
        for(int i = 0; i<states.getLength(); i++){
            state = Integer.parseInt(states.item(i).getTextContent());
            System.out.println("State: " + state);

            if(!checkIfStateInSim(state)){
                throw new Exception("One Or More Cells Have Incorrect States");
            }
        }
    }

    private boolean checkIfStateInSim(int state){

        for(int i: possibleStates){
            if (state == i){
                return true;
            }
        }
        return false;
    }

    private void setRowCol(NodeList sizeList){
        row = Integer.parseInt(sizeList.item(0).getTextContent());
        col = Integer.parseInt(sizeList.item(1).getTextContent());
    }

    private void setParameters(NodeList paramList){
        for(int i=0; i<paramList.getLength(); i++){
            gridParameters.add(Double.valueOf(paramList.item(i).getTextContent()));
        }
    }

    private void determineEdgeBehavior(NodeList edges){
        for(int i=0; i<edges.getLength(); i++){
            edgeBehavior[i] = Integer.valueOf(edges.item(i).getTextContent());
        }
    }

    private void allocateNeighbors(NodeList neighborList){
        for(int i=0; i<neighborList.getLength(); i++){
            String isNeighborConsidered = neighborList.item(i).getTextContent();

            if(isNeighborConsidered.equals("true")){
                neighbors.add(true);
            }
            else if(isNeighborConsidered.equals("false")){
                neighbors.add(false);
            }else{
                neighbors.add(null);
            }

        }
    }

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

    private void checkProbSum() throws Exception{
        Double sum = blockPercentages.stream().mapToDouble(i-> i).sum();

        if(sum != 1.0){
            throw new Exception("Block Percentages DO NOT Sum to 1");
        }

        return;
    }

    private void checkBlockNumber() throws Exception{

        if (row*col != totalBlocks){
            throw new Exception("Row, Col do not multiple to total blocks");
        }

        return;
    }

    private void checkNeighbors() throws Exception{

        for(int i=0; i< neighbors.size(); i++){
            if(neighbors.get(i) == null){
                throw new Exception("Incorrect Neighbor Parameters");
            }
        }

        return;
    }
}