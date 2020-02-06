package cellsociety.Model.Grid;

import cellsociety.Model.Cell.Cell;
import cellsociety.Model.Cell.GameOfLifeCell;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class representing a grid for the GameOfLife simulation
 * @author caryshindell, lornezhang, ameersyedibrahim
 * Dependencies: Grid class, Cell class, GameOfLifeCell class
 * Example: 8x8 grid with some alive cells and some dead cells
 */
public class GameOfLifeGrid extends Grid {

    public static final int[] possibleStates = {0, 4};
    public static final HashMap<Integer, String> stateNames = new HashMap<Integer, String>() {{
        put(0, "Dead");
        put(4, "Alive");
    }};
    /**
     * Construct a GameOfLife grid and initialize it
     * @param rowSize number of columns
     * @param colSize number of rows
     * @param initial_states initial cell configurations
     * @param ignoredNeighbors list of booleans representing whether a neighbor is considered or ignored. False means it is ignored
     * @param edgeParams grid edge type, xShift, yShift
     */
    public GameOfLifeGrid(int rowSize, int colSize, ArrayList<Integer> initial_states, ArrayList<Boolean> ignoredNeighbors, int[] edgeParams){
        super(rowSize, colSize, initial_states, ignoredNeighbors, edgeParams);
    }
    public HashMap<Integer,String> getStateNames(){
        return stateNames;
    }
    /**
     * Check if the simulation has reached equilibrium (no one is dying and no one is becoming alive)
     * @return done
     */
    public boolean checkIfDone(){
        return done;
    }

    /**
     * make a GameOfLifeCell (rather than an abstract cell)
     * @param state initial cell state
     * @return cell object
     */
    public Cell makeCell(int state) {
        return new GameOfLifeCell(state);
    }
}