package cellsociety.Model.Grid;
import java.util.Collections;

import cellsociety.Model.Cell.Cell;
import cellsociety.Model.Cell.SegregationCell;

import java.util.ArrayList;

public class SegregationGrid extends Grid {
    private ArrayList<Cell> emptyCells;
    private ArrayList<Cell> notSatisfiedCells;
    private boolean done = false;

    /**
     * Construct a segregationgrid object and initialize the grid configuration
     * @param rowSize number of columns
     * @param colSize number of rows
     * @param initial_positions initial grid configuration in 1D list form
     * @param ignoredNeighbors list of booleans representing whether a neighbor is considered or ignored. False means it is ignored
     */
    public SegregationGrid(int rowSize, int colSize, ArrayList<Integer> initial_positions, ArrayList<Boolean> ignoredNeighbors, String gridType){
        super(rowSize, colSize, initial_positions, ignoredNeighbors, gridType);
    }

    /**
     * updates the grid. Checks for cells that are not satisfied and cells that are empty. Then, it swaps the cells that are not satisfied one by one with the empty cells.
     * @return a new set of current state for the GridViewer to display
     */
    @Override
    public ArrayList<Integer> updateGrid(){
        emptyCells = new ArrayList<Cell>();
        notSatisfiedCells = new ArrayList<Cell>();
        ArrayList<Integer> viewState = new ArrayList<Integer>();
        for(int i=0; i<colSize; i++){
            for(int j=0; j<rowSize; j++){
                if(isEmpty(cells[i][j])){
                    cells[i][j].calculateNextState();
                }
                if(isNotSatisfied(cells[i][j])){
                    notSatisfiedCells.add(cells[i][j]);
                }
                else if(cells[i][j].getCurrentState() == 7){
                    emptyCells.add(cells[i][j]);
                }
            }
        }
        if(notSatisfiedCells.size() == 0){
            done = true;
        }
        swapCells();
        for(int q=0; q<colSize; q++) {
            for (int w = 0; w < rowSize; w++) {
                viewState.add(cells[q][w].getCurrentState());
            }
        }
        return viewState;
    }

    /**
     * checks if cells are satisfied
     * @param cell is the cell to check if it is satisfied
     * @return whether the cell is satisfied
     */
    private boolean isNotSatisfied(Cell cell) {
        return (cell.getCurrentState() == 0 ) || (cell.getCurrentState() == 4);
    }

    /**
     * checks if cells are empty
     * @param cell is the cell to check if it is empty
     * @return whether the cell is empty
     */
    private boolean isEmpty(Cell cell) {
        return cell.getCurrentState() != 7;
    }

    /**
     * swaps empty cells with cells that are not satisfied
     */
    private void swapCells() {
        for(Cell swap : notSatisfiedCells){
            //Random rand = new Random();
            //int rand_int1 = rand.nextInt(emptyCells.size());
            Collections.shuffle(emptyCells);
            if(swap.getCurrentState() == 0){
                switchCurrentState(emptyCells.get(0), 1);
            }
            else{
                switchCurrentState(emptyCells.get(0), 5);
            }

            emptyCells.remove(0);
            switchCurrentState(swap, 7);
            emptyCells.add(swap);
        }
    }

    /**
     * switches the current state of a cell
     * @param cell cell whose current state is being switched
     * @param nextState the next state of the cell
     */
    private void switchCurrentState(Cell cell, int nextState) {
        cell.setNextState(nextState);
        cell.update();
    }


    /**
     * checks if the simulation is over
     * @return whether the simulation is over
     */
    public boolean checkIfDone(){
        return done;
    }

    /**
     * makes a new segregation cell
     * @param state state to initiate the cell to
     * @return segregation cell
     */
    public Cell makeCell(int state) {
        return new SegregationCell(state);
    }
}