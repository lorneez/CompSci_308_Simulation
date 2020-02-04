package cellsociety.Model.Cell;
import cellsociety.Model.Cell.Cell;

public class PredatorPreyCell extends Cell{
    private static double energy;
    private static double chronons_passed;
    private final int REPRODUCTION_THRESHOLD = 6; // chronon threshold, not sure what this number should be
    private final int FISH_ENERGY = 10;
    private boolean canReproduce = false;

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
        water = state 0, fish = state 1, shark = state 2
        */

        /* pseudo-code for fish behavior

        if state == 1:

        // if at least one empty square exists, randomly find one and move the fish there
        if neighborAvailable() != -1 && canReproduce:
            the_neighbor = neighborAvailable()
            the_neighbor.state = 1
            the_neighbor.chronons_passed = 0;
            the_neighbor.canReproduce = false;
            this.state = 1
            this.chronons_passed = 0;
            this.canReproduce = false

        else if neighborAvailable() != -1:
            the_neighbor = neighborAvailable()
            the_neighbor.state = 1
            the_neighbor.chronons_passed = this.chronons_passed+=1
            this.state = 0
            this.chronons_passed = 0


        else if neighborAvailable() == -1:
            this.chronons_passed +=1;


        if chronons_passed == REPRODUCTION_THRESHOLD:
            this.canReproduce = true;

        */


        /* pseudo-code for shark behavior

        if state == 2:
            // if a shark reaches zero energy, it dies
            if this.energy == 0:
                this.state == 0
                this.chronons_passed == 0

            // if there is an adjacent square occupied by a fish, the shark will move there randomly
            else neighborFish() != -1:

                the_fish_neighbor = neighborFish();
                the_fish_neighbor.state = 2
                the_fish_neighbor.chronons_passed = this.chronons_passed+=1;
                the_fish_neighbor.energy = this.energy-=1 + FISH_ENERGY;
                this.chronons_passed = 0
                this.state = 0

            // if the shark can reproduce AND if no fish are neighbors, randomly move to an unoccupied square
            else if neighborFish == -1 && neighborAvailable() != -1 && canReproduce:
                the_neighbor = neighborAvailable()
                the_neighbor.state = 2
                the_neighbor.chronons_passed = 0;
                the_neighbor.energy = this.energy-=1;
                this.state = 2
                this.chronons_passed = 0

            // if no fish are neighbors, randomly move to an unoccupied square
            else if neighborFish == -1 && neighborAvailable() != -1:
                the_neighbor = neighborAvailable()
                the_neighbor.state = 2
                the_neighbor.chronons_passed = this.chronons_passed+=1

                the_neighbor.energy = this.energy-=1;
                this.state = 0
                this.chronons_passed = 0

           else if neighborFish == -1 && neighborAvailable() != -1:
                 this.chronons_passed+=1


            if chronons_passed == REPRODUCTION_THRESHOLD:
                this.canReproduce = true;












         */

        return 1;
    }



}
