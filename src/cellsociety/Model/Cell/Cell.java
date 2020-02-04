package cellsociety.Model.Cell;

/**
 * Class representing an abstract cell object, including its current and next states, and neighbors
 * @author caryshindell, lornezhang, ameersyedibrahim
 * Dependencies: none
 * Example: a cell in state 0 with next state 1 and 3 unique neighbors
 * Assumptions: max of 8 neighbors
 */
public abstract class Cell {
    protected int currentState;
    protected int nextState;
    protected Cell rightNeighbor;
    protected Cell leftNeighbor;
    protected Cell lowerNeighbor;
    protected Cell upperNeighbor;
    protected Cell upperRightNeighbor;
    protected Cell upperLeftNeighbor;
    protected Cell lowerRightNeighbor;
    protected Cell lowerLeftNeighbor;

    /**
     * Cell Constructor
     * @param state initial cell state
     */
    public Cell(int state){
        this.currentState = state;
        this.nextState = state;
    }

    /**
     * Define the upper right neighbor
     * @param upperRightNeighbor cell object representing the upper right neighbor
     */
    public void setUpperRightNeighbor(Cell upperRightNeighbor){
        this.upperRightNeighbor = upperRightNeighbor;
    }

    /**
     * Define the upper left neighbor
     * @param upperLeftNeighbor cell object representing the upper left neighbor
     */
    public void setUpperLeftNeighbor(Cell upperLeftNeighbor){
        this.upperLeftNeighbor = upperLeftNeighbor;
    }

    /**
     * Define the lower right neighbor
     * @param lowerRightNeighbor cell object representing the lower right neighbor
     */
    public void setLowerRightNeighbor(Cell lowerRightNeighbor){
        this.lowerRightNeighbor = lowerRightNeighbor;
    }

    /**
     * Define the lower left neighbor
     * @param lowerLeftNeighbor cell object representing the lower left neighbor
     */
    public void setLowerLeftNeighbor(Cell lowerLeftNeighbor){
        this.lowerLeftNeighbor = lowerLeftNeighbor;
    }

    /**
     * Define the right neighbor
     * @param rightNeighbor cell object representing the right neighbor
     */
    public void setRightNeighbor(Cell rightNeighbor){
        this.rightNeighbor = rightNeighbor;
    }

    /**
     * Define the left neighbor
     * @param leftNeighbor cell object representing the left neighbor
     */
    public void setLeftNeighbor(Cell leftNeighbor){
        this.leftNeighbor = leftNeighbor;
    }

    /**
     * Define the upper neighbor
     * @param upperNeighbor cell object representing the upper neighbor
     */
    public void setUpperNeighbor(Cell upperNeighbor){
        this.upperNeighbor = upperNeighbor;
    }

    /**
     * Define the lower neighbor
     * @param lowerNeighbor cell object representing the lower neighbor
     */
    public void setLowerNeighbor(Cell lowerNeighbor){
        this.lowerNeighbor = lowerNeighbor;
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

    protected Cell[] getAllNeighbors(){
        return new Cell[]{rightNeighbor, leftNeighbor, lowerNeighbor, upperNeighbor, upperRightNeighbor, upperLeftNeighbor, lowerRightNeighbor, lowerLeftNeighbor};
    }
}