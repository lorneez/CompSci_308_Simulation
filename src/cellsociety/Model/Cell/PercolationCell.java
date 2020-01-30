package cellsociety.Model.Cell;
import cellsociety.Model.Cell.Cell;

public class PercolationCell extends Cell{

    /**
     * Cell Constructor
     * @param state
     */
    public PercolationCell(int state){
        super(state);
    }

    public static void setProb(){
    }

    public int calculateNextState(){
        return 1;
    }
}
