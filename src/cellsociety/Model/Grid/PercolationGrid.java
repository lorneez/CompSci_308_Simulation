package cellsociety.Model.Grid;

import cellsociety.Model.Cell.Cell;
import cellsociety.Model.Cell.PercolationCell;

import java.util.ArrayList;

public class PercolationGrid extends Grid {
    private ArrayList<Cell> openCells;

    /**
     *
     * @param rowSize
     * @param colSize
     * @param initial_positions
     */
    public PercolationGrid(int rowSize, int colSize, ArrayList<Integer> initial_positions){
        super(rowSize, colSize, initial_positions);
        initializeGrid(initial_positions);
    }
    @Override
    protected void initializeGrid(ArrayList<Integer> initial_positions){
        openCells = new ArrayList<Cell>();
        int index = 0;
        for(int i=0; i<colSize; i++){
            for(int j=0; j<rowSize; j++){
                int cellState = initial_positions.get(index);
                cells[i][j] = makeCell(cellState);
                if(cellState == PercolationCell.openState){
                    openCells.add(cells[i][j]);
                }
                index++;
            }
        }
        setNeighbors();
        setDiagNeighbors();
    }

    /**
     *
     */
    @Override
    public ArrayList<Integer> updateGrid(){
        ArrayList<Integer> viewState = new ArrayList<Integer>();
        for(int i=0; i<colSize; i++){
            for(int j=0; j<rowSize; j++){
                cells[i][j].update();
            }
        }
        for(int i=0; i<colSize; i++){
            for(int j=0; j<rowSize; j++){
                if(openCells.contains(cells[i][j])){
                    int nextState = cells[i][j].calculateNextState();
                    viewState.add(nextState);
                    if(nextState != PercolationCell.openState){
                        openCells.remove(cells[i][j]);
                    }
                }
                else {
                    viewState.add(cells[i][j].getCurrentState());
                }
            }
        }
        return viewState;
    }

    @Override
    protected boolean checkIfDone(){

        return false;


    }
    /**
     *
     * @param state
     * @return
     */
    @Override
    public Cell makeCell(int state) {
        return new PercolationCell(state);
    }
}