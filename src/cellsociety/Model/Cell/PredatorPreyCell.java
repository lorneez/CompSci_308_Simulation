package cellsociety.Model.Cell;
import cellsociety.Model.Cell.Cell;

public class PredatorPreyCell extends Cell{

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
