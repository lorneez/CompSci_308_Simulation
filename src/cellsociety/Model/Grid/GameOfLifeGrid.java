package cellsociety.Model.Grid;

import cellsociety.Model.Cell.Cell;
import cellsociety.Model.Cell.GameOfLifeCell;

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
     * @param state
     * @return
     */
    @Override
    public Cell makeCell(int state) {
        return new GameOfLifeCell(state);
    }
}