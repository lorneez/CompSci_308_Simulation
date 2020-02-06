package cellsociety.Model.Grid;

import cellsociety.Model.Cell.PredatorPreyCell;

import java.util.ArrayList;

public class PredatorPreyGrid extends Grid {

    public static final int[] possibleStates = {0, 2, 5};

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