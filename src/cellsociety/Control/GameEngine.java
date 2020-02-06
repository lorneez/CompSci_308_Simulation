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
    private ArrayList<Double> blockPercentages;
    private String lastSimulationRun;
    private ArrayList<Double> gridParameters;
    private Document doc;
    private int[] possibleStates;


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

    private void parseFile(String sim_xml_path) throws IOException, SAXException, ParserConfigurationException {
        File fXmlFile = new File(sim_xml_path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(fXmlFile);

        cellStates = new ArrayList<>();
        gridParameters = new ArrayList<>();
        neighbors = new ArrayList<>();

        this.parseMethod = doc.getElementsByTagName("parse").item(0).getTextContent();


        try{
            this.simType = doc.getElementsByTagName("sim_type").item(0).getTextContent();
        }catch(NullPointerException ne){
            System.out.println("Exception: No Simulation Type Given");
            // restart simulation code
        }

        try{
            isValidSimType(simType);
        }catch(Exception ex){
            System.out.println("Exception: Invalid Simulation Type");
            // restart simulation code
        }


        if (parseMethod.equals("percentage")){
            parseByPercentage();
        }else if (parseMethod.equals("longlist")){
            parseLongList();
        }
        System.out.println("neighbors: " + neighbors);
        this.initializeGrid(gridParameters, row, col, neighbors);
    }

    private void parseLongList(){

        NodeList size_list = doc.getElementsByTagName("size"); //eventually add row and col parameters in config file
        row = Integer.valueOf(size_list.item(0).getTextContent());
        col = Integer.valueOf(size_list.item(1).getTextContent());

        NodeList param_list = doc.getElementsByTagName("param");
        for(int i=0; i<param_list.getLength(); i++){
            gridParameters.add(Double.valueOf(param_list.item(i).getTextContent()));
        }
        NodeList states_list = doc.getElementsByTagName("state");

        try{
            checkValidCellStates(states_list);
        }catch(Exception ex){
            System.out.println("Exception: One or more Cell States is Invalid");
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

    private void parseByPercentage(){
        blockPercentages = new ArrayList<>();
        ArrayList<Integer> blockTypes = new ArrayList<>();

        NodeList size_list = doc.getElementsByTagName("size"); //eventually add row and col parameters in config file
        row = Integer.valueOf(size_list.item(0).getTextContent());
        col = Integer.valueOf(size_list.item(1).getTextContent());
        NodeList blocksInput = doc.getElementsByTagName("totalblocks");
        totalBlocks = Integer.valueOf(blocksInput.item(0).getTextContent());
        NodeList blockTypesRead = doc.getElementsByTagName("blocktype");

        try{
            checkValidCellStates(blockTypesRead);
        }catch(Exception ex){
            System.out.println("Exception: One or more Cell States is Invalid");
        }

        for(int i=0; i<blockTypesRead.getLength(); i++){
            blockTypes.add(Integer.valueOf(blockTypesRead.item(i).getTextContent()));
        }
        int defaultBlock = blockTypes.get(0);
        NodeList blockPercentagesRead = doc.getElementsByTagName("percentage");
        for(int i=0; i<blockPercentagesRead.getLength(); i++){
            blockPercentages.add(Double.valueOf(blockPercentagesRead.item(i).getTextContent()));
        }
        NodeList param_list = doc.getElementsByTagName("param");
        for(int i=0; i<param_list.getLength(); i++){
            gridParameters.add(Double.valueOf(param_list.item(i).getTextContent()));
        }
        if(myViewer.getNewParameters()){
            gridParameters = myViewer.getGridParametersUpdated();
            blockPercentages = myViewer.getBlockPercentagesUpdated();
        }
        int count = 0;
        for(int i=0; i<blockTypes.size(); i++){
            System.out.println(totalBlocks*(blockPercentages.get(i)));
            for(int w=0; w<totalBlocks*(blockPercentages.get(i))-1; w++){
                cellStates.add(blockTypes.get(i));
                count ++;
            }
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
        for(int i=0; i<totalBlocks-count;i++){
            cellStates.add(defaultBlock);
        }
        Collections.shuffle(cellStates);
    }

    private void initializeGrid(ArrayList<Double> gridParameters, int rowSize, int colSize, ArrayList<Boolean> ignoredNeighbors){
        int[] edgeParams = new int[]{0, 0, 0}; // change this later to be dynamic... it is grid type, xShift, yShift
        switch (simType) {
            case "fire":
                myGrid = new FireGrid(rowSize, colSize, cellStates, ignoredNeighbors, edgeParams);
                FireCell.setProb(gridParameters.get(0), gridParameters.get(1));
                possibleStates = FireGrid.possibleStates;
                break;
            case "gameoflife":
                myGrid = new GameOfLifeGrid(rowSize, colSize, cellStates, ignoredNeighbors, edgeParams);
                possibleStates = GameOfLifeGrid.possibleStates;
                break;
            case "segregation":
                myGrid = new SegregationGrid(rowSize, colSize, cellStates, ignoredNeighbors, edgeParams);
                SegregationCell.setProb(gridParameters.get(0));
                possibleStates = SegregationGrid.possibleStates;
                break;
            case "predatorprey":
                myGrid = new PredatorPreyGrid(rowSize, colSize, cellStates, ignoredNeighbors, edgeParams);
                possibleStates = PredatorPreyGrid.possibleStates;
                break;
            case "percolation":
                myGrid = new PercolationGrid(rowSize, colSize, cellStates, ignoredNeighbors, edgeParams);
                possibleStates = PercolationGrid.possibleStates;
                break;
        }
    }

    private void step() throws IOException, SAXException, ParserConfigurationException {
        if(!myViewer.getSplashScreenFinished()){
            System.out.println("stepping");
            if(myViewer.getRestart()){
                myViewer.setSplashScreenFinished(true);
            }
            animation.setRate(myViewer.getScrollValue());
            if(!myViewer.getPause() && !myGrid.checkIfDone()){
                ArrayList<Integer> currStates = myGrid.updateGrid();
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

    private boolean isValidSimType(String s) throws Exception {

        for(int i =0; i<allSimTypes.length; i++){
            if(allSimTypes[i].equals(s)){
                return true;
            }
        }

        throw new Exception("Simulation Type Invalid Exception");
    }

    private boolean checkValidCellStates(NodeList states) throws Exception{
        int state = -1;
        for(int i = 0; i<states.getLength(); i++){
            state = Integer.valueOf(states.item(i).getTextContent());
            if(!checkIfStateInSim(state)){
                throw new Exception("One Or More Cells Have Incorrect States");
            }
        }
        return true;
    }

    private boolean checkIfStateInSim(int state){

        for(int i: possibleStates){
            if (state == i){
                return true;
            }
        }
        return false;
    }
}