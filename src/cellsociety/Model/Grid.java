package cellsociety.Model;

import java.util.ArrayList;

public abstract class Grid {
    public Cell[][] cells;
    private int rowSize;
    private int colSize;

    /**
     *
     * @param rowSize
     * @param colSize
     * @param initial_positions
     */
    public Grid(int rowSize, int colSize, ArrayList<Integer> initial_positions){
        this.rowSize = rowSize;
        this.colSize = colSize;
        this.cells = new Cell[rowSize][colSize];
        initializeGrid(initial_positions);
    }

    /**
     *
     */
    public ArrayList<Integer> updateGrid(){
        ArrayList<Integer> viewState = new ArrayList<Integer>();
        for(int i=0; i<colSize; i++){
            for(int j=0; j<rowSize; j++){
                cells[i][j].update();
            }
        }
        for(int i=0; i<colSize; i++){
            for(int j=0; j<rowSize; j++){
                viewState.add(cells[i][j].calculateNextState());
            }
        }
        return viewState;
    }

    /**
     *
     * @param initial_positions
     */
    private void initializeGrid(ArrayList<Integer> initial_positions){
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
     */
    private void setNeighbors(){
        for(int i=0; i<colSize; i++){
            for(int j=0; j<rowSize; j++){
                if(j>1){
                    cells[i][j].setLeftNeighbor(cells[i][j-1]);
                }
                if(j<rowSize-1){
                    cells[i][j].setRightNeighbor(cells[i][j+1]);
                }
                if(i>1){
                    cells[i][j].setUpperNeighbor(cells[i-1][j]);
                }
                if(j<colSize-1){
                    cells[i][j].setLowerNeighbor(cells[i+1][j]);
                }
            }
        }
    }

    /**
     *
     * @param state
     * @return
     */
    public abstract Cell makeCell(int state);

    // public void setDiagnonalNeighbors

}
