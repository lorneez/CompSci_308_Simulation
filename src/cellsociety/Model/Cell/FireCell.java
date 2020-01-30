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
        if(currentState == 1 & (rightNeighbor.getCurrentState() == 2 || leftNeighbor.getCurrentState() == 2 || upperNeighbor.getCurrentState() == 2 || lowerNeighbor.getCurrentState() == 2)){
            Random rand = new Random();
            double rand_int1 = rand.nextInt(100) / 100;
            if(rand_int1 < probCatch){
                nextState = 2;
            }
            else{
                nextState = 1;
            }
        }
        else if(currentState == 2){
            Random rand = new Random();
            double rand_int1 = rand.nextInt(100) / 100;
            if(rand_int1 < probCatch){
                nextState = 0;
            }
            else{
                nextState = 2;
            }
        }
        return currentState;
    }
}