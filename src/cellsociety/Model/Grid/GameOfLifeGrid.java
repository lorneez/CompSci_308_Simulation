package cellsociety.Model.Grid;

import cellsociety.Model.Cell.Cell;
import cellsociety.Model.Cell.FireCell;
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
        System.out.println("Game Of Life Grid Constructor");
        this.initializeGrid(initial_positions);
    }
    @Override
    protected void initializeGrid(ArrayList<Integer> initial_positions){
        int index = 0;
        System.out.println("Game Of Life Grid Initialize");
        for(int i=0; i<colSize; i++){
            for(int j=0; j<rowSize; j++){
                System.out.println("State:" +initial_positions.get(index));
                System.out.println(makeCell(initial_positions.get(index)).getCurrentState());
                cells[i][j] = makeCell(initial_positions.get(index));
                index ++;
            }
        }
        setNeighbors();
        setDiagNeighbors();
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
        System.out.println("Made Game Of Life Cell");
        Cell newCell = new GameOfLifeCell(state);
        return newCell;
    }
}