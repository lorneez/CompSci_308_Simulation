package cellsociety.Model;
import cellsociety.Model.Cell;
import java.util.Random;

public class FireCell extends Cell{
    private int currentState;
    private int nextState;
    private Cell rightNeighbor;
    private Cell leftNeighbor;
    private Cell lowerNeighbor;
    private Cell upperNeighbor;
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
        if(this.currentState == 1 & (this.rightNeighbor.getCurrentState() == 2 || this.leftNeighbor.getCurrentState() == 2 || this.upperNeighbor.getCurrentState() == 2 || this.lowerNeighbor.getCurrentState() == 2)){
            Random rand = new Random();
            double rand_int1 = rand.nextInt(100) / 100;
            if(rand_int1 < probCatch){
                nextState = 2;
            }
            else{
                nextState = 1;
            }
        }
        else if(this.currentState == 2){
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