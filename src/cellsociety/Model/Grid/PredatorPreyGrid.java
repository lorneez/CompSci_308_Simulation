package cellsociety.Model.Grid;

import cellsociety.Model.Cell.Cell;
import cellsociety.Model.Cell.PredatorPreyCell;

import java.util.ArrayList;

public class PredatorPreyGrid extends Grid {

    /**
     *
     * @param rowSize
     * @param colSize
     * @param initial_positions
     */
    public PredatorPreyGrid(int rowSize, int colSize, ArrayList<Integer> initial_positions){
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
        return new PredatorPreyCell(state);
    }

    // return an integer which indicates that if any of the 4 neighbors is empty (the state is 0)
    // return if there are many, randomly select a neighbor
    // return 1 for left, 2 for right, 3 for up, 4 for down
    public int neighborAvailable(){
        return -1;
    }

    // return an integer which indicates that if any of the 4 neighbors is a fish (the state is 1)
    // return if there are many, randomly select a neighbor
    // return 1 for left, 2 for right, 3 for up, 4 for down
    public int neighborFish(){
        return -1;
    }
}