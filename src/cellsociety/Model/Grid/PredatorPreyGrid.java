package cellsociety.Model.Grid;

import cellsociety.Model.Cell.PredatorPreyCell;

import java.util.ArrayList;
import java.util.HashMap;

public class PredatorPreyGrid extends Grid {

    public static final int[] possibleStates = {0, 2, 5};
    public static final HashMap<Integer, String> stateNames = new HashMap<>() {{
        put(0, "Shark");
        put(2, "Fish");
        put(5, "Water");
    }};
    /**
     * Construct a predatorpreygrid object and initialize the grid configuration
     * @param rowSize number of columns
     * @param colSize number of rows
     * @param initial_positions initial grid configuration in 1D list form
     * @param ignoredNeighbors list of booleans representing whether a neighbor is considered or ignored. False means it is ignored
     * @param edgeParams grid edge type, xShift, yShift
     */
    public PredatorPreyGrid(int rowSize, int colSize, ArrayList<Integer> initial_positions, ArrayList<Boolean> ignoredNeighbors, int[] edgeParams){
        super(rowSize, colSize, initial_positions, ignoredNeighbors, edgeParams);
    }
    public HashMap<Integer,String> getStateNames(){
        return stateNames;
    }

    /**
     * Check if the simulation has reached equilibrium... for predator prey we just return false because there are cases
     * where no cell states change but e.g. sharks will die later
     * @return boolean done
     */
    @Override
    public boolean checkIfDone(){
        return false;
    }

    /**
     * Make a predator prey cell rather than an abstract cell
     * @param state
     * @return predator prey cell object
     */
    @Override
    public PredatorPreyCell makeCell(int state) {
        return new PredatorPreyCell(state);
    }

}