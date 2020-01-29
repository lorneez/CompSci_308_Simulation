package cellsociety.Model;

import java.util.ArrayList;

public class GameOfLifeGrid extends Grid {

    /**
     *
     * @param rowSize
     * @param colSize
     * @param initial_positions
     */
    public GameOfLifeGrid(int rowSize, int colSize, ArrayList<Integer> initial_positions){
        super(rowSize, colSize, initial_positions);
    }

    /**
     *
     * @param state
     * @return
     */
    @Override
    public Cell makeCell(int state) {
        return new GameOfLifeCell(state);
    }
}