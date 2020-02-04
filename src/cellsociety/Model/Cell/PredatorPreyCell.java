package cellsociety.Model.Cell;
import cellsociety.Model.Cell.Cell;

public class PredatorPreyCell extends Cell{
    private static double energy;
    private static double chronons_passed;

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
        // water = state 0, fish = state 1, shark = state 2



        return 1;
    }
}
