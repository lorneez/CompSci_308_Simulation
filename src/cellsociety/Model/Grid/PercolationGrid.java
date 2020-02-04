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
     * Construct a percolation grid and initialize it
     * @param rowSize number of columns
     * @param colSize number of rows
     * @param initial_states list of initial cell configurations
     */
    public PercolationGrid(int rowSize, int colSize, ArrayList<Integer> initial_states){
        super(rowSize, colSize, initial_states);
        initializeGrid(initial_states);
    }

    @Override
    protected void initializeGrid(ArrayList<Integer> initial_states){
        int index = 0;
        for(int i=0; i<colSize; i++){
            for(int j=0; j<rowSize; j++){
                cells[i][j] = makeCell(initial_states.get(index));
                index++;
            }
        }
        setNeighbors();
        setDiagNeighbors();
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