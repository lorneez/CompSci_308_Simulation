package cellsociety.Control;
import cellsociety.Model.Grid.Grid;
import cellsociety.Model.Grid.FireGrid;
import cellsociety.Model.Cell.FireCell;
import cellsociety.View.GridViewer;
import cellsociety.Model.Cell.SegregationCell;
import cellsociety.Model.Grid.SegregationGrid;
import cellsociety.Model.Cell.GameOfLifeCell;
import cellsociety.Model.Grid.GameOfLifeGrid;
import cellsociety.Model.Cell.PredatorPreyCell;
import cellsociety.Model.Grid.PredatorPreyGrid;
import cellsociety.Model.Cell.PercolationCell;
import cellsociety.Model.Grid.PercolationGrid;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;

import java.io.File;
import java.util.ArrayList;

public class GameEngine {
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

    private String sim_type;
    private ArrayList<Integer> cellStates;
    private Grid myGrid;
    private ArrayList<Double> gridParameters;
    private GridViewer myViewer;

    /**
     *
     */
    public GameEngine(){
        start();
        gridParameters = new ArrayList<>();
        cellStates = new ArrayList<>();
    }

    private void parseFile(String sim_xml_path){

        File fXmlFile = new File(sim_xml_path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.new DocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        this.sim_type = doc.getElementsByTagName("sim_type").item(0).getTextContent();

        NodeList size_list = doc.getElementsByTagName("size");

        NodeList param_list = doc.getElementsByTagName("param");
        for(int i=0; i<param_list.getLength(); i++){
            gridParameters.add(Double.valueOf(param_list.item(i).getTextContent()));
        }

        NodeList states_list = doc.getElementsByTagName("state");
        for(int i=0; i<states_list.getLength(); i++){
            cellStates.add(Integer.valueOf(states_list.item(i).getTextContent()));
        }

        initializeGrid(gridParameters, Integer.valueOf(param_list.item(0).getTextContent()), Integer.valueOf(param_list.item(1).getTextContent()));



    }

    private void initializeGrid(ArrayList<Double> gridParameters, int rowSize, int colSize){

        if(sim_type == "fire"){
            myGrid = new FireGrid(rowSize, colSize, cellStates);
            FireCell.setProb(gridParameters.get(0), gridParameters.get(1));
        }else if (sim_type == "segregation"){
            myGrid = new SegregationGrid(rowSize, colSize, cellStates);
            SegregationCell.setProb(gridParameters);
        }else if (sim_type == "gameoflife"){
            myGrid = new GameOfLifeGrid(rowSize, colSize, cellStates);
            GameOfLifeCell.setProb(gridParameters);
        }else if (sim_type == "wator"){
            myGrid = new WatorGrid(rowSize, colSize, cellStates);
            WatorCell.setProb(gridParameters);
        }
    }

    /**
     *
     */
    public void start(){
        // create the viewer
        // splash screen
        // viewer will determine configuration file
        myViewer = new GridViewer();
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step());
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }


    private void step(){
        if(myViewer.splashScreen == false){
            myViewer.update(myGrid.updateGrid());

        }else{
            sim_xml_path = myViewer.getFileName(); // should return the path of the XML file
            // see if the viewer has determined the file name yet
            // if so, parse the file name and we are done with the splash screen
            if(sim_xml_path != null){
                parseFile(sim_xml_path);
                myViewer.setSplashScreen(false);
            }
        }
    }
}