package cellsociety.Model.Grid;

import cellsociety.Model.Cell.Cell;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Grid {
    //protected HashMap<Cell, ArrayList<Cell>> cells;
    public Cell[][] cells;
    protected int rowSize;
    protected int colSize;

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
    public int getCell(int row, int col){
        return cells[row][col].getCurrentState();
    }

    /**
     *
     */
    protected void setNeighbors(){
        int[] x = {0,0,1,-1};
        int[] y = {1,-1,0,0};
        for(int j=0; j<colSize; j++){
            for(int w=0; w<rowSize; w++){
                for(int i=0; i<4;i++){
                    System.out.println(cells[j][w].getCurrentState());
                    System.out.println("row" + j);
                    System.out.println("col" + w);
                    int neighborx = j+x[i];
                    int neighbory = w+y[i];
                    if(checkInBounds(neighborx,neighbory)){
                        if(i==0){
                            cells[j][w].setUpperNeighbor(cells[neighborx][neighbory]);
                        }
                        if(i==1){
                            cells[j][w].setLowerNeighbor(cells[neighborx][neighbory]);
                        }
                        if(i==2){
                            cells[j][w].setRightNeighbor(cells[neighborx][neighbory]);
                        }
                        if(i==3){
                            cells[j][w].setLeftNeighbor(cells[neighborx][neighbory]);
                        }
                    }
                }
            }

        }
    }

    private boolean checkInBounds(int x,int y){
        if(x < 0 || x >= colSize) return false;
        if(y < 0 || y >= rowSize) return false;
        return true;

    }

    /**
     *
     * @param initial_positions
     */
    protected abstract void initializeGrid(ArrayList<Integer> initial_positions);

    /**
     *
     * @param state
     * @return
     */
    public abstract Cell makeCell(int state);

    // public void setDiagnonalNeighbors

}
