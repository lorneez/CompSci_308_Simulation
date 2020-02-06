package cellsociety.Model.Grid;

import cellsociety.Model.Cell.Cell;
import cellsociety.Model.Cell.FireCell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that represents a grid for the fire simulation
 * @author caryshindell, lornezhang, ameersyedibrahim
 * Dependencies: FireCell class, Grid class
 * Example: a 8x8 grid with some cells on fire, some trees, and some dead
 */
public class FireGrid extends Grid {

    public static final int[] possibleStates = {3, 4, 6};
    public static final HashMap<Integer, String> stateNames = new HashMap<Integer, String>() {{
        put(3, "Dead");
        put(4, "Tree");
        put(6, "Fire");
    }};

    /**
     * Construct a firegrid object and initialize the grid configuration
     * @param rowSize number of columns
     * @param colSize number of rows
     * @param initial_positions initial grid configuration in 1D list form
     * @param ignoredNeighbors list of booleans representing whether a neighbor is considered or ignored. False means it is ignored
     * @param edgeParams grid edge type, xShift, yShift
     */
    public FireGrid(int rowSize, int colSize, ArrayList<Integer> initial_positions, ArrayList<Boolean> ignoredNeighbors, int[] edgeParams){
        super(rowSize, colSize, initial_positions, ignoredNeighbors, edgeParams);
    }
    public HashMap<Integer,String> getStateNames(){
        return stateNames;
    }

    /**
     * make a new firecell object (as opposed to an abstract cell object)
     * @param state initial cell state
     * @return cell object
     */
    public Cell makeCell(int state) {
        return new FireCell(state);
    }

    /**
     * Checks if the simulation has reached equilibrium (no cells on fire)
     * @return done
     */
    public boolean checkIfDone(){
        for(int i=0; i<colSize; i++) {
            for (int j = 0; j < rowSize; j++) {
                if (cells.get(coordinatePair(i,j)).getCurrentState() == FireCell.FIRE_STATE){
                    return false;
                }
            }
        }
        return true;
    }
}
