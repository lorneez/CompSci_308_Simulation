package cellsociety.Model.Grid;

import cellsociety.Model.Cell.Cell;
import cellsociety.Model.Cell.GameOfLifeCell;

import java.util.ArrayList;

/**
 * Class representing a grid for the GameOfLife simulation
 * @author caryshindell, lornezhang, ameersyedibrahim
 * Dependencies: Grid class, Cell class, GameOfLifeCell class
 * Example: 8x8 grid with some alive cells and some dead cells
 */
public class GameOfLifeGrid extends Grid {

    /**
     * Construct a GameOfLife grid and initialize it
     * @param rowSize number of columns
     * @param colSize number of rows
     * @param initial_positions initial cell configurations
     */
    public GameOfLifeGrid(int rowSize, int colSize, ArrayList<Integer> initial_positions){
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

    /**
     * Check if the simulation has reached equilibrium (no one is dying and no one is becoming alive)
     * @return done
     */
    public boolean checkIfDone(){
        return done;
    }

    /**
     * make a GameOfLifeCell (rather than an abstract cell)
     * @param state initial cell state
     * @return cell object
     */
    public Cell makeCell(int state) {
        return new GameOfLifeCell(state);
    }
}