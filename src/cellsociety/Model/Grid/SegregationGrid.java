package cellsociety.Model.Grid;

import cellsociety.Model.Cell.Cell;
import cellsociety.Model.Cell.SegregationCell;

import java.util.ArrayList;

public class SegregationGrid extends Grid {

    /**
     *
     * @param rowSize
     * @param colSize
     * @param initial_positions
     */
    public SegregationGrid(int rowSize, int colSize, ArrayList<Integer> initial_positions){
        super(rowSize, colSize, initial_positions);
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