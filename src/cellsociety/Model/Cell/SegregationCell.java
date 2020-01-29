package cellsociety.Model.Cell;
import cellsociety.Model.Cell.Cell;

public class SegregationCell extends Cell{
    private int currentState;
    private int nextState;
    private Cell rightNeighbor;
    private Cell leftNeighbor;
    private Cell lowerNeighbor;
    private Cell upperNeighbor;

    /**
     * Cell Constructor
     * @param state
     */
    public SegregationCell(int state){
        super(state);
    }

    public static void setProb(){
    }

    public int calculateNextState(){
        return 1;
    }
}
