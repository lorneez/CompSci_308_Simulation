package cellsociety.Model.Grid;

import cellsociety.Model.Cell.Cell;
import cellsociety.Model.Cell.GameOfLifeCell;

import java.util.ArrayList;

/**
 * Class representing a grid for the GameOfLife simulation
 * @author caryshindell, lornezhang, ameersyedibrahim
 * Dependencies: Grid class, Cell class, GameOfLifeCell class
 * Example: 8x8 grid with some alive cells and some dead cells
 */
public class GameOfLifeGrid extends Grid {

    /**
     * Construct a GameOfLife grid and initialize it
     * @param rowSize number of columns
     * @param colSize number of rows
     * @param initial_states initial cell configurations
     * @param ignoredNeighbors list of booleans representing whether a neighbor is considered or ignored. False means it is ignored
     */
    public GameOfLifeGrid(int rowSize, int colSize, ArrayList<Integer> initial_states, ArrayList<Boolean> ignoredNeighbors, String gridType){
        super(rowSize, colSize, initial_states, ignoredNeighbors, gridType);
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