package cellsociety.Model.Cell;

/**
 * Class representing a cell object for the percolation simulation
 * @author caryshindell, lornezhang, ameersyedibrahim
 * Dependencies: Cell class
 * Example: a percolated cell with 5 neighbors in various states
 */
public class PercolationCell extends Cell{
    public static final int BLOCKED_STATE = 0;
    public static final int OPEN_STATE = 7;
    public static final int PERCOLATED_STATE = 5;

    /**
     * Cell Constructor
     * @param state the initial state of the cell
     */
    public PercolationCell(int state){
        super(state);
    }

    /** Calculates the cell's next state and sets its next state parameter to that state
     *  Percolation cell state only changes if it is open and has a percolated neighbor
     * @return the cell's current state (since updates are delayed)
     */
    public int calculateNextState(){
        if(currentState == BLOCKED_STATE || currentState == PERCOLATED_STATE){
            return currentState;
        }
        Cell[] neighbors = getAllNeighbors();
        for(Cell c : neighbors){
            if(c != null && c.getCurrentState() == PERCOLATED_STATE){
                setNextState(PERCOLATED_STATE);
            }
        }
        return currentState;
    }
}
