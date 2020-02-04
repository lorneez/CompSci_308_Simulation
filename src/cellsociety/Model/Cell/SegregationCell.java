package cellsociety.Model.Cell;

/**
 * Class representing a cell object for the segregation simulation
 * @author caryshindell, lornezhang, ameersyedibrahim
 * Dependencies: Cell class
 * Example: a not satisfied blue cell with some red and blue neighbors
 */
public class SegregationCell extends Cell{
    private static double satisfy;
    public static final int RED = 1;
    public static final int BLUE = 5;
    public static final int RED_NOT_SATISFIED = 0;
    public static final int BLUE_NOT_SATISFIED = 4;
    public static final int EMPTY = 7;

    /**
     * Cell Constructor
     * @param state initial cell state
     */
    public SegregationCell(int state){
        super(state);
    }

    /**
     *
     * @param param1
     */
    public static void setProb(double param1){
        satisfy = param1;
    }
    public void setCurrentState(int x){
        currentState = x;
    }

    /**
     *
     * @return
     */
    @Override
    public int calculateNextState(){
        Cell[] neighbors = getAllNeighbors();
        int numSameState = 0;
        int numNeighbors = 0;
        for(Cell check : neighbors){
            if(check != null){
                if(check.getCurrentState() == this.currentState){
                    numSameState++;
                }
                numNeighbors++;
            }
        }
        double satisfied = (double) numSameState/numNeighbors;
        if(satisfied <= satisfy){
            setNotSatisfied();
        }
        return currentState;
    }

    private void setNotSatisfied() {
        if(currentState == RED){
            currentState = RED_NOT_SATISFIED;
        }
        else if(currentState == BLUE){
            currentState = BLUE_NOT_SATISFIED;
        }
    }
}
