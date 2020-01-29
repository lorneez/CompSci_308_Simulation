package cellsociety.Model;

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

    /**
     *
     * @param state
     * @return
     */
    @Override
    public Cell makeCell(int state) {
        return new SegregationCell(state);
    }
}