package cellsociety.Model.Cell;

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
     * @param state
     */
    public Cell(int state){
        this.currentState = state;
        this.nextState = state;
    }

    protected Cell[] getAllNeighbors(){
        return new Cell[]{rightNeighbor, leftNeighbor, lowerNeighbor, upperNeighbor, upperRightNeighbor, upperLeftNeighbor, lowerRightNeighbor, lowerLeftNeighbor};
    }

    public void setUpperRightNeighbor(Cell upperRightNeighbor){
        this.upperRightNeighbor = upperRightNeighbor;
    }

    public void setUpperLeftNeighbor(Cell upperLeftNeighbor){
        this.upperLeftNeighbor = upperLeftNeighbor;
    }

    public void setLowerRightNeighbor(Cell lowerRightNeighbor){
        this.lowerRightNeighbor = lowerRightNeighbor;
    }

    public void setLowerLeftNeighbor(Cell lowerLeftNeighbor){
        this.lowerLeftNeighbor = lowerLeftNeighbor;
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