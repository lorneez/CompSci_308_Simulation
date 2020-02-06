package cellsociety.Model.Grid;

import cellsociety.Model.Cell.PredatorPreyCell;

import java.util.ArrayList;
import java.util.HashMap;

public class PredatorPreyGrid extends Grid {

    public static final int[] possibleStates = {0, 2, 5};
    public static final HashMap<Integer, String> stateNames = new HashMap<Integer, String>() {{
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
     */
    public PredatorPreyGrid(int rowSize, int colSize, ArrayList<Integer> initial_positions, ArrayList<Boolean> ignoredNeighbors, String gridType){
        super(rowSize, colSize, initial_positions, ignoredNeighbors, gridType);
    }
    public HashMap<Integer,String> getStateNames(){
        return stateNames;
    }
    /**
     *
     * @return
     */
    @Override
    public boolean checkIfDone(){
        return done;
    }
    /**
     *
     * @param state
     * @return
     */
    @Override
    public PredatorPreyCell makeCell(int state) {
        return new PredatorPreyCell(state);
    }

}