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

        // if state is shark, set energy to 5
    }

    public int calculateNextState(){
        /*
        water = state 5, fish = state 2, shark = state 0
        */
        // if cell is a fish
        if (this.currentState == 2){
            // if the fish can reproduce AND at least one empty square exists, randomly find one and move the fish there
            if (neighborAvailable() != null){
                PredatorPreyCell the_neighbor = neighborAvailable();
                the_neighbor.currentState = 2;

                if (canReproduce){
                    the_neighbor.chronons_passed = 0;
                    the_neighbor.canReproduce = true;

                    this.currentState = 2;
                    this.chronons_passed = 0;
                    this.canReproduce = false;
                }else{
                    the_neighbor.chronons_passed = this.chronons_passed+=1;

                    this.currentState = 5;
                    this.chronons_passed = 0;
                }
            }else{
                this.chronons_passed+=1;
            }

            if(chronons_passed == REPRODUCTION_THRESHOLD){
                this.canReproduce = true;
            }

        }

        // if the cell is a shark
        if (this.currentState == 0){

            // if a shark reaches zero energy, it dies
            if (this.energy == 0){
                this.currentState = 5;
                this.chronons_passed = 0;
            }
            // if there is an adjacent square occupied by a fish, the shark will move there randomly
            else if (neighborFish()!= null){
                PredatorPreyCell the_fish_neighbor = neighborFish();

                the_fish_neighbor.currentState = 0;
                the_fish_neighbor.chronons_passed = this.chronons_passed+=1;
                the_fish_neighbor.energy = this.energy-=1 + FISH_ENERGY;
                this.chronons_passed = 0;
                this.currentState = 5;

            }
            // if the shark can reproduce AND if no fish are neighbors, randomly move to an unoccupied square
            else if (neighborFish() == null && neighborAvailable() != null && this.canReproduce){
                PredatorPreyCell the_neighbor = neighborAvailable();
                the_neighbor.currentState = 0;

                if (this.canReproduce){
                    the_neighbor.chronons_passed = 0;
                    the_neighbor.energy = this.energy-=1;
                    the_neighbor.canReproduce = false;

                    this.currentState = 0;
                    this.canReproduce = false;
                }else{
                    the_neighbor.chronons_passed = this.chronons_passed+=1;
                    the_neighbor.energy = this.energy-=1;

                    this.currentState = 5;
                }
                this.chronons_passed = 0;

            }
            // if the fish can't move anywhere, update the number of chronons
            else if(neighborFish() == null && neighborAvailable() == null){
                this.chronons_passed+=1;
            }
            // enable reproduction if the number of chronons is reached
            if (this.chronons_passed == REPRODUCTION_THRESHOLD){
                this.canReproduce = true;
            }

        }

        return currentState;
    }

    // return an integer which indicates that if any of the 4 neighbors is empty (the state is 0)
    // return if there are many, randomly select a neighbor
    // return 1 for left, 2 for right, 3 for up, 4 for down
    public PredatorPreyCell neighborAvailable(){
        return null;
    }

    // return an integer which indicates that if any of the 4 neighbors is a fish (the state is 1)
    // return if there are many, randomly select a neighbor
    // return 1 for left, 2 for right, 3 for up, 4 for down
    public PredatorPreyCell neighborFish(){
        return null;
    }



}
