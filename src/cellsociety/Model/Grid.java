package cellsociety.Model;

public class Grid {
    public Cell[][] cells;
    private int rowSize;
    private int colSize;


    public Grid(int rowSize, int colSize){
        this.rowSize = rowSize;
        this.colSize = colSize;
        this.cells = new Cell[rowSize][colSize];
    }

    public void initializeGrid(ArrayList initial_positions){
        int index = 0;
        for(int i=0; i<colSize; i++){
            for(int j=0; j<rowSize; j++){
                cells[i][j] = initial_positions.get(index);
                index ++;
            }
        }
    }

    public void setNeighbors(){
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
    public void updateGrid(){
        for(int i=0; i<colSize; i++){
            for(int j=0; j<rowSize; j++){
                // in order to keep both updating and calculating next state in the same iteration, the cell is updated
                // and then the next state is calculated. Note that therefore nothing will happen on the first step
                cells[i][j].update();
                cells[i][j].calculateNextState();
            }
        }
    }

    // public void setDiagnonalNeighbors

}
