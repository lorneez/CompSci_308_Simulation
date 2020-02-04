package cellsociety.Model.Cell;
import cellsociety.Model.Cell.Cell;

public class SegregationCell extends Cell{
    private static double satisfy;
    public static final int red = 1;
    public static final int blue = 5;
    public static final int rednotsatisfied = 0;
    public static final int bluenotsatisfied = 4;
    public static final int empty = 7;
    /**
     * Cell Constructor
     * @param state
     */
    public SegregationCell(int state){
        super(state);
    }

    public static void setProb(double param1){
        satisfy = param1;
    }
    public void setCurrentState(int x){
        currentState = x;
    }
    @Override
    public int calculateNextState(){
        Cell[] neighbors = getAllNeighbors();
        int numSameState = 0;
        int numNeighbors = 0;
        for(Cell check : neighbors){
            if(check != null){
                if(check.getCurrentState() == this.currentState){
                    numSameState ++;
                }
                numNeighbors ++;
            }
        }
        double satisfied = (double) numSameState/numNeighbors;
        if(satisfied <= satisfy){
            setNotSatisfied();
        }
        return currentState;
    }

    private void setNotSatisfied() {
        if(currentState == red){
            currentState = rednotsatisfied;
        }
        else if(currentState == blue){
            currentState = bluenotsatisfied;
        }
    }
}
