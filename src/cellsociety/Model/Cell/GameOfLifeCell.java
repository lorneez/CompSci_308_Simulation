package cellsociety.Model.Cell;

/**
 * Class representing a cell object for the GameOfLife simulation
 * @author caryshindell, lornezhang, ameersyedibrahim
 * Dependencies: Cell class
 * Example: cell that is alive and has 3 alive neighbors
 */
public class GameOfLifeCell extends Cell{
    public static final int ALIVE_STATE = 4;
    public static final int DEAD_STATE = 0;
    
    /**
     * Cell Constructor
     * @param state initial cell state
     */
    public GameOfLifeCell(int state){
        super(state);
    }

    /**
     * Check how many neighbors cell has that are alive. If cell is alive and has 2 or 3 alive neighbors, stays alive,
     * otherwise it dies. If cell is dead and has exactly 3 alive neighbors it becomes alive.
     * @return current cell state (since updating is delayed)
     */
    public int calculateNextState(){
        int aliveNeighbors = DEAD_STATE;
        for(Cell neighbor : getAllNeighbors()){
            if(neighbor != null && neighbor.getCurrentState() == ALIVE_STATE){
                aliveNeighbors++;
            }
        }
        if(currentState == DEAD_STATE && aliveNeighbors == 3){
            nextState = ALIVE_STATE;
        }
        else if(currentState == ALIVE_STATE){
            if(aliveNeighbors == 2 || aliveNeighbors == 3){
                nextState = ALIVE_STATE;
            }
            else{
                nextState = DEAD_STATE;
            }
        }
        return currentState;
    }
}