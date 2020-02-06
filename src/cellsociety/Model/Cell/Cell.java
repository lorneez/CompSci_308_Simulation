package cellsociety.Model.Cell;

import java.util.ArrayList;

/**
 * Class representing an abstract cell object, including its current and next states, and neighbors
 * @author caryshindell, lornezhang, ameersyedibrahim
 * Dependencies: none
 * Example: a cell in state 0 with next state 1 and 3 unique neighbors
 * Assumptions: max of 8 neighbors
 */
public abstract class Cell {
    public static final int numNeighbors = 8;
    protected int currentState;
    protected int nextState;
    protected ArrayList<Cell> neighbors;

    /**
     * Cell Constructor
     * @param state initial cell state
     */
    public Cell(int state){
        this.currentState = state;
        this.nextState = state;
        neighbors = new ArrayList<>();
        for(int i=0; i<numNeighbors; i++){
            neighbors.add(null);
        }
    }

    /**
     * Define a neighbor in a given position (e.g. left) with a certain cell
     * @param whichNeighbor position (as an index to the list)
     * @param neighborCell cell object representing the neighbor
     */
    public void setNeighbor(int whichNeighbor, Cell neighborCell){
        neighbors.set(whichNeighbor, neighborCell);
    }

    /**
     * Fetch the current cell state
     * @return integer representing current cell state
     */
    public int getCurrentState(){
        return currentState;
    }

    /**
     * Fetch the cell next state
     * @return integer representing cell next state
     */
    public int getNextState(){
        return nextState;
    }

    /**
     * Set the cell next state (should only be used by certain grid classes...)
     * @param theNextState integer value to set to
     */
    public void setNextState(int theNextState){
        this.nextState = theNextState;
    }

    /**
     * Set current cell state to cell next state
     */
    public void update(){
        this.currentState = this.nextState;
    }

    /**
     * Calculate and set the cell's next state based on the simulation rules
     * @return cell's CURRENT state
     */
    public abstract int calculateNextState();

    protected ArrayList<Cell> getAllNeighbors(){
        return neighbors;
    }
}