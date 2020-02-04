package cellsociety.Model.Cell;

/**
 * Class representing a cell object for the segregation simulation
 * @author caryshindell, lornezhang, ameersyedibrahim
 * Dependencies: Cell class
 * Example: a segregation cell with 3 neighbors in various states
 */
public class SegregationCell extends Cell{
    private static double satisfy;
    public static final int RED_STATE = 1;
    public static final int BLUE_STATE = 5;
    public static final int REDNOTSATISFIED_STATE = 0;
    public static final int BLUENOTSATISFIED_STATE = 4;
    /**
     * Cell Constructor
     * @param state initial cell state
     */
    public SegregationCell(int state){
        super(state);
    }

    /**
     * sets the satisfy variable
     * @param param1 the percentage of satisfication
     */
    public static void setProb(double param1){
        satisfy = param1;
    }

    /**
     * sets the currrent state
      * @param state the current state
     */
    public void setCurrentState(int state){
        currentState = state;
    }

    /**
     * Calculates the next state of the cell and checks if the cell is satisfied.
     * @return the current state of the cell after checking if it is satisfied
     */
    @Override
    public int calculateNextState(){
        Cell[] neighbors = getAllNeighbors();
        int numSameState = 0;
        int numNeighbors = 0;
        for(Cell check : neighbors){
            if(check != null){
                if(check.getCurrentState() == this.currentState || check.getCurrentState() == this.currentState - 1){
                    numSameState++;
                }
                numNeighbors++;
            }
        }
        double satisfied = (double) numSameState/numNeighbors;
        if(satisfied <= satisfy){
            setNotSatisfied();
        }
        return nextState;
    }

    /**
     * Sets the cell's next state to the not satisfied variable corresponding to its color.
     */
    private void setNotSatisfied() {
        if(currentState == RED_STATE){
            currentState = REDNOTSATISFIED_STATE;
        }
        else if(currentState == BLUE_STATE){
            currentState = BLUENOTSATISFIED_STATE;
        }
    }
}
