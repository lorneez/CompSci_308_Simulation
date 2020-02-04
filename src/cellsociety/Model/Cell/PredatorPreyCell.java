package cellsociety.Model.Cell;
import cellsociety.Model.Cell.Cell;

public class PredatorPreyCell extends Cell{
    private static double energy;
    private static double chronons_passed;
    private final int REPRODUCTION_THRESHOLD = 6; // chronon threshold, not sure what this number should be

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
        /*
        // water = state 0, fish = state 1, shark = state 2

        // pseudo-code
        
        if state == 1 && neighborAvailable() != -1:
            **move the fish**


         */
        // if state == 1 && chronons_passed == REPRODUCTION_THRESHOLD:
        // this.chronons_passed = 0



        return 1;
    }



}
