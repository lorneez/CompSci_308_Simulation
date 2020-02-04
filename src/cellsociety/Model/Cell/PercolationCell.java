package cellsociety.Model.Cell;

public class PercolationCell extends Cell{
    public static final int blockedState = 0;
    public static final int openState = 7;
    public static final int percolatedState = 5;

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
        if(currentState == blockedState || currentState == percolatedState){
            return currentState;
        }
        Cell[] neighbors = getAllNeighbors();
        for(Cell c : neighbors){
            if(c != null && c.getCurrentState() == percolatedState){
                setNextState(percolatedState);
            }
        }
        return currentState;
    }
}
