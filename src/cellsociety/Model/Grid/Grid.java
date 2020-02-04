package cellsociety.Model.Grid;

import cellsociety.Model.Cell.Cell;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class representing an abstract grid object for any simulation
 * @author caryshindell, lornezhang, ameersyedibrahim
 * Dependencies: Cell class
 * Example: an 8x8 grid
 * Assumptions: grid is rectangular
 */
public abstract class Grid {
    //protected HashMap<Cell, ArrayList<Cell>> cells;
    public Cell[][] cells;
    protected int rowSize;
    protected int colSize;
    protected boolean done = false;
    protected boolean firstStep = true;

    /**
     * Construct a grid object
     * @param rowSize number of columns
     * @param colSize number of rows
     * @param initial_positions initial cell configurations
     */
    public Grid(int rowSize, int colSize, ArrayList<Integer> initial_positions){
        this.rowSize = rowSize;
        this.colSize = colSize;
        this.cells = new Cell[rowSize][colSize];
    }

    /**
     * Update the grid and keep track of whether any cell states changed
     * @return a list of the cell states to be given to the viewer
     */
    public ArrayList<Integer> updateGrid(){
        ArrayList<Integer> viewState = new ArrayList<Integer>();
        boolean equilibrium = true;
        for(int i=0; i<colSize; i++){
            for(int j=0; j<rowSize; j++){
                if(cells[i][j].getNextState() != cells[i][j].getCurrentState()){
                    equilibrium = false;
                }
                cells[i][j].update();
            }
        }
        if(!firstStep) done = equilibrium;
        firstStep = false;
        for(int i=0; i<colSize; i++){
            for(int j=0; j<rowSize; j++){
                viewState.add(cells[i][j].calculateNextState());
            }
        }
        return viewState;
    }

    /*public int getCell(int row, int col){
        return cells[row][col].getCurrentState();
    }*/

    /**
     * Check if the given coordinates are valid within the grid
     * @param x x coordinate
     * @param y y coordinate
     * @return whether or not the coordinates are valid
     */
    public boolean checkInBounds(int x,int y){
        if(x < 0 || x >= colSize) return false;
        if(y < 0 || y >= rowSize) return false;
        return true;
    }

    /**
     * make the cell object specific to the simulation we are running
     * @param state initial cell state
     * @return cell object (as an abstract CELL object not a specific simulation cell object)
     */
    public abstract Cell makeCell(int state);

    /**
     * Check if the simulation has reached equilibrium
     * @return done
     */
    public abstract boolean checkIfDone();

    protected void setNeighbors(){
        int[] x = {0,0,1,-1};
        int[] y = {1,-1,0,0};
        for(int j=0; j<colSize; j++){
            for(int w=0; w<rowSize; w++){
                for(int i=0; i<4;i++){
                    int neighborx = j+x[i];
                    int neighbory = w+y[i];
                    if(checkInBounds(neighborx,neighbory)){
                        if(i==0){
                            cells[j][w].setUpperNeighbor(cells[neighborx][neighbory]);
                        }
                        if(i==1){
                            cells[j][w].setLowerNeighbor(cells[neighborx][neighbory]);
                        }
                        if(i==2){
                            cells[j][w].setRightNeighbor(cells[neighborx][neighbory]);
                        }
                        if(i==3){
                            cells[j][w].setLeftNeighbor(cells[neighborx][neighbory]);
                        }
                    }
                }
            }

        }
    }
    protected void setDiagNeighbors(){
        int[] x = {1,1,-1,-1};
        int[] y = {1,-1,1,-1};
        for(int j=0; j<colSize; j++){
            for(int w=0; w<rowSize; w++){
                for(int i=0; i<4;i++){
                    int neighborx = j+x[i];
                    int neighbory = w+y[i];
                    if(checkInBounds(neighborx,neighbory)){
                        if(i==0){
                            cells[j][w].setUpperRightNeighbor(cells[neighborx][neighbory]);
                        }
                        if(i==1){
                            cells[j][w].setLowerRightNeighbor(cells[neighborx][neighbory]);
                        }
                        if(i==2){
                            cells[j][w].setUpperLeftNeighbor(cells[neighborx][neighbory]);
                        }
                        if(i==3){
                            cells[j][w].setLowerLeftNeighbor(cells[neighborx][neighbory]);
                        }
                    }
                }
            }

        }
    }

    protected void initializeGrid(ArrayList<Integer> initial_states){
        int index = 0;
        for(int i=0; i<colSize; i++){
            for(int j=0; j<rowSize; j++){
                cells[i][j] = makeCell(initial_states.get(index));
                index++;
            }
        }
        setNeighbors();
    }
}




