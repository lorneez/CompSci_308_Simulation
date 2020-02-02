package cellsociety.Model.Cell;
import cellsociety.Model.Cell.Cell;

public class GameOfLifeCell extends Cell{
    /**
     * Cell Constructor
     * @param state
     */
    public GameOfLifeCell(int state){
        super(state);
    }

    public int calculateNextState(){
        int aliveNeighbors = 0;
        if((rightNeighbor!=null && rightNeighbor.getCurrentState()==4)){
            aliveNeighbors++;
        }
        if((leftNeighbor!=null && leftNeighbor.getCurrentState()==4)){
            aliveNeighbors++;
        }
        if((upperNeighbor!=null && upperNeighbor.getCurrentState()==4)){
            aliveNeighbors++;
        }
        if((lowerNeighbor!=null && lowerNeighbor.getCurrentState()==4)){
            aliveNeighbors++;
        }
        if((lowerLeftNeighbor!=null && lowerLeftNeighbor.getCurrentState()==4)){
            aliveNeighbors++;
        }
        if((lowerRightNeighbor!=null && lowerRightNeighbor.getCurrentState()==4)){
            aliveNeighbors++;
        }
        if((upperRightNeighbor!=null && upperRightNeighbor.getCurrentState()==4)){
            aliveNeighbors++;
        }
        if((upperLeftNeighbor!=null && upperLeftNeighbor.getCurrentState()==4)){
            aliveNeighbors++;
        }
        if(currentState == 0 && aliveNeighbors == 3){
            this.setNextState(4);
        }
        else if(currentState == 4){
            if((aliveNeighbors == 2 || aliveNeighbors == 3)){
                this.setNextState(4);
            }
            else{
                this.setNextState(0);
            }
        }
        return currentState;
    }
}