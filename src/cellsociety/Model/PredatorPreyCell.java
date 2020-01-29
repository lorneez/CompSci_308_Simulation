package cellsociety.Model;
import cellsociety.Model.Cell;
import java.util.Random;

public class PredatorPreyCell extends Cell{
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
    public PredatorPreyCell(int state){
        super(state);
    }

    public static void setProb(){
    }

    public int calculateNextState(){
        return 1;
    }
}
