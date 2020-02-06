package cellsociety.Model.Cell;
import java.util.Random;

/**
 * Class representing a cell for the fire simulation
 * @author caryshindell, lornezhang,
 * Dependencies: Cell Class
 * Example: a cell on fire with 4 neighbors
 * Assumptions: 4 neighbors
 */
public class FireCell extends Cell{
    public static final int TREE_STATE = 4;
    public static final int DEAD_STATE = 3;
    public static final int FIRE_STATE = 6;
    private static double probCatch;
    private static double probDie;

    /**
     * Cell Constructor
     * @param state initial cell state
     */
    public FireCell(int state){
        super(state);
    }

    /**
     * Set the probability parameters
     * @param param1 probCatch (probability a cell catches fire)
     * @param param2 probDie (probability a cell dies)
     */
    public static void setProb(double param1, double param2){
        probCatch = param1;
        probDie = param2;
    }

    /**
     * Calculate and set the next state according to whether neighbors and itself are on fire/trees
     * @return current cell state (since updates are delayed)
     */
    public int calculateNextState(){
        if(currentState == TREE_STATE){
            if((neighbors.get(2)!=null && neighbors.get(2).getCurrentState()==FIRE_STATE) || (neighbors.get(3)!=null && neighbors.get(3).getCurrentState()==FIRE_STATE) || (neighbors.get(0)!=null && neighbors.get(0).getCurrentState()==FIRE_STATE) || (neighbors.get(1)!=null && neighbors.get(1).getCurrentState()==FIRE_STATE)){
                Random rand = new Random();
                double rand_int1 = rand.nextDouble();
                if(rand_int1 < probCatch){
                    nextState = FIRE_STATE;
                }
                else{
                    nextState = TREE_STATE;
                }
            }
        }
        else if(currentState == FIRE_STATE){
            Random rand = new Random();
            double rand_int2 = rand.nextDouble();
            if(rand_int2 < probDie){
                nextState = DEAD_STATE;
            }
            else{
                nextState = FIRE_STATE;
            }
        }
        return currentState;
    }
}