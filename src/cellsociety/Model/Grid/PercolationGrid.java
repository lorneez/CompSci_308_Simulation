package cellsociety.Model.Grid;

import cellsociety.Model.Cell.Cell;
import cellsociety.Model.Cell.PercolationCell;

import java.util.ArrayList;

public class PercolationGrid extends Grid {

    private boolean done = false;
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
     * Update the grid and keep track of whether any cell states changed
     * @return a list of the cell states to be given to the viewer
     */
    @Override
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
        done = equilibrium;
        for(int i=0; i<colSize; i++){
            for(int j=0; j<rowSize; j++){
                viewState.add(cells[i][j].calculateNextState());
            }
        }
        return viewState;
    }

    /**
     * Check if the simulation has reached a standstill
     * @return boolean done
     */
    @Override
    public boolean checkIfDone(){
        return done;
    }
    /**
     * Construct a percolation cell (rather than an abstract cell)
     * @param state cell state
     * @return the cell object
     */
    @Override
    public Cell makeCell(int state) {
        return new PercolationCell(state);
    }
}