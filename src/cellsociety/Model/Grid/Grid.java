package cellsociety.Model.Grid;

import cellsociety.Model.Cell.Cell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing an abstract grid object for any simulation
 * @author caryshindell, lornezhang, ameersyedibrahim
 * Dependencies: Cell class
 * Example: an 8x8 grid
 * Assumptions: grid is rectangular
 */
public abstract class Grid {
    protected Map<Integer, Cell> cells;
    public static final String[] gridTypes = new String[]{"basic", "torus"};
    private final int HASH_MULTIPLIER = 10000;
    protected int rowSize;
    protected int colSize;
    protected boolean done = false;
    protected boolean firstStep = true;

    protected final String gridType;
    protected static final int[] possibleStates = {};
    private final int xShift;
    private final int yShift;


    /**
     * Construct a grid object
     * @param rowSize number of columns
     * @param colSize number of rows
     * @param initial_positions initial grid configuration in 1D list form
     * @param ignoredNeighbors list of booleans representing whether a neighbor is considered or ignored. False means it is ignored
     * @param edgeParams grid edge type, xShift, yShift
     */
    public Grid(int rowSize, int colSize, ArrayList<Integer> initial_positions, ArrayList<Boolean> ignoredNeighbors, int[] edgeParams){
        this.rowSize = rowSize;
        this.colSize = colSize;
        this.cells = new HashMap<>();
        this.gridType = gridTypes[edgeParams[0]];
        xShift = edgeParams[1];
        yShift = edgeParams[2];
        initializeGrid(initial_positions, ignoredNeighbors);
    }

    /**
     * Update the grid and keep track of whether any cell states changed
     * @return a list of the cell states to be given to the viewer
     */
    public ArrayList<int[]> updateGrid(){
        ArrayList<int[]> viewState = new ArrayList<>();
        boolean equilibrium = true;
        for(int i=0; i<colSize; i++){
            for(int j=0; j<rowSize; j++){
                if(cells.get(coordinatePair(i,j)).getNextState() != cells.get(coordinatePair(i,j)).getCurrentState()){
                    equilibrium = false;
                }
                cells.get(coordinatePair(i,j)).update();
            }
        }
        if(!firstStep) done = equilibrium;
        firstStep = false;
        for(int i=0; i<colSize; i++){
            for(int j=0; j<rowSize; j++){
                int state = cells.get(coordinatePair(i,j)).calculateNextState();
                viewState.add(new int[]{i,j,state});
            }
        }
        return viewState;
    }

    /**
     * check if coordinates are in bounds. If not, return null (-1) or what edge it maps to (e.g. torus will wrap around)
     * @param x x coordinate
     * @param y y coordinate
     * @return coordinates of the neighbor (or -1's if null)
     */
    private int[] checkInBounds(int x,int y){
        if(gridType.equals("torus")){
            // first do the shifts, if there are any
            // can only be one type of shift, x or y
            if(x<0) y -= yShift;
            else if(x>=rowSize) y += yShift;
            if(y<0) x -= xShift;
            else if(y>=colSize) x += xShift;
            // now do the wrap-arounds
            if(x<0) x += rowSize;
            else if(x>=rowSize) x -= rowSize;
            if(y<0) y += colSize;
            else if(y>=colSize) y -= colSize;
        }
        else if(gridType.equals("basic") && (x < 0 || x >= colSize || y < 0 || y >= rowSize)){
            return new int[]{-1, -1};
        }
        return new int[]{x,y};
    }
    public abstract HashMap<Integer,String> getStateNames();


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
        for(int j=0; j<colSize; j++){
            for(int w=0; w<rowSize; w++){
                for(int i=0; i<Cell.numNeighbors;i++){
                    int[] neighborCoords = checkInBounds(j+x[i], w+y[i]);
                    if(neighborCoords[0] != -1){
                        // 0, 1, 2, 3, 4, 5, 6, 7 are upper, lower, right, left, upper right, lower right, upper left, lower left respectively
                        if(!ignoredNeighbors.get(i)){
                            cells.get(coordinatePair(j,w)).setNeighbor(i, null);
                        }
                        else{
                            cells.get(coordinatePair(j,w)).setNeighbor(i, cells.get(coordinatePair(neighborCoords[0], neighborCoords[1])));
                        }
                    }
                }
            }

        }
    }

    /**
     * construct a single unique integer from a pair of coordinates. Assume size<10000
     */
    protected int coordinatePair(int x, int y){
        return x*HASH_MULTIPLIER + y;
    }

    protected void initializeGrid(ArrayList<Integer> initial_states, ArrayList<Boolean> ignoredNeighbors){
        int index = 0;
        for(int i=0; i<colSize; i++){
            for(int j=0; j<rowSize; j++){
                cells.put(coordinatePair(i,j), makeCell(initial_states.get(index)));
                index++;
            }
        }
        setNeighbors(ignoredNeighbors);
    }
}




