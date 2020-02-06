package cellsociety.Model.Grid;

import cellsociety.Model.Cell.Cell;

import java.util.ArrayList;

/**
 * Class representing an abstract grid object for any simulation
 * @author caryshindell, lornezhang, ameersyedibrahim
 * Dependencies: Cell class
 * Example: an 8x8 grid
 * Assumptions: grid is rectangular
 */
public abstract class Grid {
    //protected HashMap<Cell, ArrayList<Cell>> cells;
    protected Cell[][] cells;
    protected int rowSize;
    protected int colSize;
    protected boolean done = false;
    protected boolean firstStep = true;
    protected String gridType;

    /**
     * Construct a grid object
     * @param rowSize number of columns
     * @param colSize number of rows
     * @param initial_positions initial grid configuration in 1D list form
     * @param ignoredNeighbors list of booleans representing whether a neighbor is considered or ignored. False means it is ignored
     */
    public Grid(int rowSize, int colSize, ArrayList<Integer> initial_positions, ArrayList<Boolean> ignoredNeighbors, String gridType){
        this.rowSize = rowSize;
        this.colSize = colSize;
        this.cells = new Cell[rowSize][colSize];
        this.gridType = gridType;
        initializeGrid(initial_positions, ignoredNeighbors);
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
     * check if coordinates are in bounds. If not, return null (-1) or what edge it maps to (e.g. torus will wrap around)
     * @param x x coordinate
     * @param y y coordinate
     * @return coordinates of the neighbor (or -1's if null)
     */
    public int[] checkInBounds(int x,int y){
        if(gridType.equals("torus")){
            if(x<0) x += rowSize;
            else if(x>=rowSize) x -= rowSize;
            if(y<0) y += colSize;
            else if(y>=colSize) y -= colSize;
        }
        else if(gridType.equals("basic")){
            if(x < 0 || x >= colSize || y < 0 || y >= rowSize){
                return new int[]{-1, -1};
            }
        }
        return new int[]{x,y};
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

    protected void setNeighbors(ArrayList<Boolean> ignoredNeighbors){
        int[] x = {0,0,1,-1,1,1,-1,-1};
        int[] y = {1,-1,0,0,1,-1,1,-1};
        // torus:
        for(int j=0; j<colSize; j++){
            for(int w=0; w<rowSize; w++){
                for(int i=0; i<Cell.numNeighbors;i++){
                    int[] neighborCoords = checkInBounds(j+x[i], w+y[i]);
                    if(neighborCoords[0] != -1){
                        // 0, 1, 2, 3, 4, 5, 6, 7 are upper, lower, right, left, upper right, lower right, upper left, lower left respectively
                        if(!ignoredNeighbors.get(i)){
                            cells[j][w].setNeighbor(i, null);
                        }
                        else{
                            cells[j][w].setNeighbor(i, cells[neighborCoords[0]][neighborCoords[1]]);
                        }
                    }
                }
            }

        }
    }

    protected void initializeGrid(ArrayList<Integer> initial_states, ArrayList<Boolean> ignoredNeighbors){
        int index = 0;
        for(int i=0; i<colSize; i++){
            for(int j=0; j<rowSize; j++){
                cells[i][j] = makeCell(initial_states.get(index));
                index++;
            }
        }
        setNeighbors(ignoredNeighbors);
    }
}




