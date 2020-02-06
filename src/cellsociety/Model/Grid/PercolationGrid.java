package cellsociety.Model.Grid;

import cellsociety.Model.Cell.Cell;
import cellsociety.Model.Cell.PercolationCell;

import java.util.ArrayList;

/**
 * Class representing a grid object for the percolation simulation
 * @author caryshindell, lornezhang, ameersyedibrahim
 * Dependencies: Grid class, Cell class, PercolationCell class
 * Example: a 8x8 grid with some cells empty, some blocked, and some percolated
 * Assumptions: percolation can happen diagonally
 */
public class PercolationGrid extends Grid {

    /**
     * Construct a percolationgrid object and initialize the grid configuration
     * @param rowSize number of columns
     * @param colSize number of rows
     * @param initial_positions initial grid configuration in 1D list form
     * @param ignoredNeighbors list of booleans representing whether a neighbor is considered or ignored. False means it is ignored
     * @param edgeParams grid edge type, xShift, yShift
     */
    public PercolationGrid(int rowSize, int colSize, ArrayList<Integer> initial_positions, ArrayList<Boolean> ignoredNeighbors, int[] edgeParams){
        super(rowSize, colSize, initial_positions, ignoredNeighbors, edgeParams);
    }

    /**
     * Check if the simulation has reached a standstill
     * @return boolean done
     */
    public boolean checkIfDone(){
        return done;
    }

    /**
     * Construct a percolation cell (rather than an abstract cell)
     * @param state cell state
     * @return the cell object
     */
    public Cell makeCell(int state) {
        return new PercolationCell(state);
    }
}