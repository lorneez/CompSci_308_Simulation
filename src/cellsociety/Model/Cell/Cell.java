package cellsociety.Model.Cell;

public abstract class Cell {
    protected int currentState;
    protected int nextState;
    protected Cell rightNeighbor;
    protected Cell leftNeighbor;
    protected Cell lowerNeighbor;
    protected Cell upperNeighbor;

    /**
     * Cell Constructor
     * @param state
     */
    public Cell(int state){
        this.currentState = state;
        this.nextState = state;
    }

    /**
     *
     * @param rightNeighbor
     */
    public void setRightNeighbor(Cell rightNeighbor){
        this.rightNeighbor = rightNeighbor;
    }

    /**
     *
     * @param leftNeighbor
     */
    public void setLeftNeighbor(Cell leftNeighbor){
        this.leftNeighbor = leftNeighbor;
    }

    /**
     *
     * @param upperNeighbor
     */
    public void setUpperNeighbor(Cell upperNeighbor){
        this.upperNeighbor = upperNeighbor;
    }

    /**
     *
     * @param lowerNeighbor
     */
    public void setLowerNeighbor(Cell lowerNeighbor){
        this.lowerNeighbor = lowerNeighbor;
    }

    /**
     *
     * @return
     */
    public int getCurrentState(){
        return currentState;
    }

    /**
     *
     * @return
     */
    public int getNextState(){
        return nextState;
    }

    /**
     *
     * @param theNextState
     */
    public void setNextState(int theNextState){
        this.nextState = theNextState;
    }

    /**
     *
     */
    public void update(){
        this.currentState = this.nextState;
    }

    /**
     *
     */
    public abstract int calculateNextState();



}