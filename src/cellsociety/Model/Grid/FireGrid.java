package cellsociety.Model.Grid;

import cellsociety.Model.Cell.Cell;
import cellsociety.Model.Cell.FireCell;

import java.util.ArrayList;

public class FireGrid extends Grid {

    /**
     *
     * @param rowSize
     * @param colSize
     * @param initial_positions
     */
    public FireGrid(int rowSize, int colSize, ArrayList<Integer> initial_positions){
        super(rowSize, colSize, initial_positions);
        this.initializeGrid(initial_positions);

    }
    protected void initializeGrid(ArrayList<Integer> initial_positions){
        int index = 0;

        for(int i=0; i<colSize; i++){
            for(int j=0; j<rowSize; j++){
                cells[i][j] = makeCell(initial_positions.get(index));
                index ++;
            }
        }
        setNeighbors();
    }

    /**
     *
     * @param state
     * @return
     */
    @Override
    public Cell makeCell(int state) {
        Cell newCell = new FireCell(state);
        return newCell;
    }

    @Override
    protected boolean checkIfDone(){

        for(int i=0; i<colSize; i++) {
            for (int j = 0; j < rowSize; j++) {
                if (cells[i][j].getCurrentState() == 6){
                    return false;
                }
            }
        }
        return true;

    }
}
