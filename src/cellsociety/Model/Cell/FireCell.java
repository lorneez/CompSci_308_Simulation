package cellsociety.Model.Cell;
import java.util.Random;

public class FireCell extends Cell{
    private static double probCatch;
    private static double probDie;

    /**
     * Cell Constructor
     * @param state
     */
    public FireCell(int state){
        super(state);

    }

    /**
     *
     * @param param1
     * @param param2
     */
    public static void setProb(double param1, double param2){
        probCatch = param1;
        probDie = param2;
    }

    /**
     *
     */
    public int calculateNextState(){
        if(currentState == 4 & (rightNeighbor.getCurrentState() == 6 || leftNeighbor.getCurrentState() == 6 || upperNeighbor.getCurrentState() == 6 || lowerNeighbor.getCurrentState() == 6)){
            Random rand = new Random();
            double rand_int1 = rand.nextInt(100) / 100;
            if(rand_int1 < probCatch){
                this.setNextState(6);
            }
            else{
                this.setNextState(4);
            }
        }
        else if(currentState == 6){
            Random rand = new Random();
            double rand_int1 = rand.nextInt(100) / 100;
            if(rand_int1 < probDie){
                this.setNextState(3);
            }
            else{
                this.setNextState(6);
            }
        }
        return currentState;
    }
}