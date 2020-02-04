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

/**
 * Class that drives the simulation and communicates with the grid and viewer classes
 * @author caryshindell, lornezhang, ameersyedibrahim
 * Dependencies: Grid class, GridViewer class, configuration files
 * Example: a game engine running the percolation simulation
 * Assumptions: rectangular grid
 */
public class GameEngine {
    public static double FRAMES_PER_SECOND = 1;
    public static final double MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

    private Timeline animation;
    private String sim_type;
    private ArrayList<Integer> cellStates;
    private Grid myGrid;
    private ArrayList<Double> gridParameters;
    private GridViewer myViewer;
    private int row;
    private int col;

    /**
     * Constructor method. initializes some things and cells start
     */
    public GameEngine(){
        start();
        gridParameters = new ArrayList<>();
        cellStates = new ArrayList<>();
    }

    /**
     * Creates the viewer, starts the splash screen. Viewer will determine configuration file
     */
    public void start(){
        myViewer = new GridViewer();
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step());
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private void parseFile(String sim_xml_path) throws ParserConfigurationException, IOException, SAXException {

        File fXmlFile = new File(sim_xml_path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        this.sim_type = doc.getElementsByTagName("sim_type").item(0).getTextContent();
        NodeList size_list = doc.getElementsByTagName("size"); //eventually add row and col parameters in config file
        row = Integer.valueOf(size_list.item(0).getTextContent());
        col = Integer.valueOf(size_list.item(1).getTextContent());

        NodeList param_list = doc.getElementsByTagName("param");
        for(int i=0; i<param_list.getLength(); i++){
            gridParameters.add(Double.valueOf(param_list.item(i).getTextContent()));
        }

        NodeList states_list = doc.getElementsByTagName("state");
        for(int i=0; i<states_list.getLength(); i++){
            cellStates.add(Integer.valueOf(states_list.item(i).getTextContent()));
        }

        this.initializeGrid(gridParameters, row, col);
    }

    private void initializeGrid(ArrayList<Double> gridParameters, int rowSize, int colSize){
        switch (sim_type) {
            case "fire":
                myGrid = new FireGrid(rowSize, colSize, cellStates);
                FireCell.setProb(gridParameters.get(0), gridParameters.get(1));
                break;
            case "gameoflife":
                myGrid = new GameOfLifeGrid(rowSize, colSize, cellStates);
                break;
            case "segregation":
                myGrid = new SegregationGrid(rowSize, colSize, cellStates);
                SegregationCell.setProb(gridParameters.get(0));
                break;
            case "predatorprey":
                myGrid = new PredatorPreyGrid(rowSize, colSize, cellStates);
                break;
            case "percolation":
                myGrid = new PercolationGrid(rowSize, colSize, cellStates);
                break;
        }
    }

    private void step(){
        if(!myViewer.getSplashScreenFinished()){
            animation.setRate(myViewer.getScrollValue());
            if(!myViewer.getPause() && !myGrid.checkIfDone()){
                ArrayList<Integer> currStates = myGrid.updateGrid();
                myViewer.updateCellStates(currStates);
            }

        }else{
            String sim_xml_path = myViewer.getFileName(); // should return the path of the XML file
            // see if the viewer has determined the file name yet
            // if so, parse the file name and we are done with the splash screen
            if(!sim_xml_path.equals("NONE")){
                try {
                    parseFile(sim_xml_path);
                    myViewer.setUpSimulation(row,col,cellStates);
                    myViewer.setSplashScreenFinished(false);

                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}