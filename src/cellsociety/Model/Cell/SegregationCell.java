package cellsociety.Model.Cell;
import cellsociety.Model.Cell.Cell;

public class SegregationCell extends Cell{

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
