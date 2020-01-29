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
    }

    /**
     *
     * @param state
     * @return
     */
    @Override
    public Cell makeCell(int state) {
        return new FireCell(state);
    }
}
