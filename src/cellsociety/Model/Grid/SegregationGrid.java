package cellsociety.Model.Grid;
import java.util.Random;

import cellsociety.Model.Cell.Cell;
import cellsociety.Model.Cell.SegregationCell;

import java.util.ArrayList;

public class SegregationGrid extends Grid {
    ArrayList<Cell> emptyCells;
    ArrayList<Cell> notSatisfiedCells;

    /**
     *
     * @param rowSize
     * @param colSize
     * @param initial_positions
     */
    public SegregationGrid(int rowSize, int colSize, ArrayList<Integer> initial_positions){
        super(rowSize, colSize, initial_positions);
        initializeGrid(initial_positions);
    }
    @Override
    protected void initializeGrid(ArrayList<Integer> initial_positions){
        int index = 0;
        for(int i=0; i<colSize; i++){
            for(int j=0; j<rowSize; j++){
                cells[i][j] = makeCell(initial_positions.get(index));
                index ++;
            }
        }
        setNeighbors();
        setDiagNeighbors();
    }
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
        swapCells();
        for(int q=0; q<colSize; q++) {
            for (int w = 0; w < rowSize; w++) {
                viewState.add(cells[q][w].getCurrentState());
            }
        }
        return viewState;
    }

    private boolean isNotSatisfied(Cell cell) {
        return (cell.getCurrentState() == 0 ) || (cell.getCurrentState() == 4);
    }

    private boolean isEmpty(Cell cell) {
        return cell.getCurrentState() != 7;
    }

    private void swapCells() {
        for(Cell swap : notSatisfiedCells){
            Random rand = new Random();
            int rand_int1 = rand.nextInt(emptyCells.size());
            if(swap.getCurrentState() == 0){
                switchCurrentState(emptyCells.get(rand_int1), 1);
            }
            else{
                switchCurrentState(emptyCells.get(rand_int1), 5);
            }

            emptyCells.remove(rand_int1);
            switchCurrentState(swap, 7);
            emptyCells.add(swap);
        }
    }

    private void switchCurrentState(Cell cell, int nextState) {
        cell.setNextState(nextState);
        cell.update();
    }


    /**
     *
     * @return
     */
    public boolean checkIfDone(){
        return false;
    }

    /**
     *
     * @param state
     * @return
     */
    public Cell makeCell(int state) {
        return new SegregationCell(state);
    }
}