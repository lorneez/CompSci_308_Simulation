package cellsociety.Model.Cell;
import cellsociety.Model.Cell.Cell;

import java.util.ArrayList;
import java.util.Random;

public class PredatorPreyCell extends Cell{

    protected PredatorPreyCell rightNeighbor;
    protected PredatorPreyCell leftNeighbor;
    protected PredatorPreyCell lowerNeighbor;
    protected PredatorPreyCell upperNeighbor;

    private double energy;
    private double chronons_passed;
    private final int REPRODUCTION_THRESHOLD = 10; // chronon threshold, not sure what this number should be
    private final int FISH_ENERGY = 4;
    private final int SHARK_ENERGY = 10;
    private boolean canReproduce = false;

    /**
     * Cell Constructor
     * @param state
     */
    public PredatorPreyCell(int state){
        super(state);
        // if state is shark, set energy to 5

        if (this.currentState == 0){
            energy = SHARK_ENERGY;
        }

        this.chronons_passed = 0;

    }

    public int calculateNextState(){
        /*
        water = state 5, fish = state 2, shark = state 0
        */
        // if cell is a fish
        if (this.currentState == 2){
            if(chronons_passed == REPRODUCTION_THRESHOLD){
                this.canReproduce = true;

            }
            // if the fish can reproduce AND at least one empty square exists, randomly find one and move the fish there
            if (neighborWater() != null){
                PredatorPreyCell the_neighbor = neighborWater();
                the_neighbor.nextState = 2;

                if (canReproduce){
                    the_neighbor.chronons_passed = 0;
                    the_neighbor.canReproduce = false;

                    this.nextState = 2;
                    this.chronons_passed = 0;
                    this.canReproduce = false;
                }else{
                    the_neighbor.chronons_passed = this.chronons_passed+1;

                    this.nextState = 5;
                    this.chronons_passed = 0;
                }
            }else{

                this.chronons_passed+=1;
            }

        }

        // if the cell is a shark
        else if (this.currentState == 0){

            // enable reproduction if the number of chronons is reached
            if (this.chronons_passed == REPRODUCTION_THRESHOLD){
                this.canReproduce = true;
            }
            // if a shark reaches zero energy, it dies
            if (this.energy == 0){
                this.nextState = 5;
                this.chronons_passed = 0;
                this.canReproduce = false;
            }

            // if there is an adjacent square occupied by a fish, the shark will move there randomly
            else if (neighborFish()!= null){
                PredatorPreyCell the_fish_neighbor = neighborFish();
                the_fish_neighbor.nextState = 0;
                the_fish_neighbor.chronons_passed = this.chronons_passed+1;
                the_fish_neighbor.energy = this.energy - 1 + FISH_ENERGY;
                this.chronons_passed = 0;
                this.nextState = 5;

            }
            // if the shark can reproduce AND if no fish are neighbors, randomly move to an unoccupied square
            else if (neighborFish() == null && neighborWater() != null){
                PredatorPreyCell the_neighbor = neighborWater();
                the_neighbor.nextState = 0;

                if (this.canReproduce){
                    the_neighbor.chronons_passed = 0;
                    the_neighbor.energy = this.energy-1;
                    the_neighbor.canReproduce = false;

                    this.nextState = 0;
                    this.canReproduce = false;
                }else{
                    the_neighbor.chronons_passed = this.chronons_passed+1;
                    the_neighbor.energy = this.energy-1;

                    this.nextState = 5;
                }
                this.chronons_passed = 0;

            }
            // if the shark can't move anywhere, update the number of chronons
            else if(neighborFish() == null && neighborWater() == null){
                this.chronons_passed++;
            }


        }

        int saveState = currentState;
        currentState = nextState;
        return saveState;
    }

    public PredatorPreyCell neighborWater(){

        ArrayList<PredatorPreyCell> openWater = new ArrayList<>();
        if( this.leftNeighbor != null && cellIsWater(this.leftNeighbor)){
            openWater.add(this.leftNeighbor);
        }
        if( this.rightNeighbor != null && cellIsWater(this.rightNeighbor)){
            openWater.add(this.rightNeighbor);
        }
        if( this.upperNeighbor != null && cellIsWater(this.upperNeighbor)){
            openWater.add(this.upperNeighbor);
        }
        if( this.lowerNeighbor != null && cellIsWater(this.lowerNeighbor)){
            openWater.add(this.lowerNeighbor);
        }

        if(openWater.size()!= 0){
            Random rand = new Random();
            PredatorPreyCell slot = openWater.get(rand.nextInt(openWater.size()));

            return slot;
        }

        return null;
    }

    public PredatorPreyCell neighborFish(){
        ArrayList<PredatorPreyCell> nearbyFishes = new ArrayList<>();
        if(this.leftNeighbor != null && cellIsFish(this.leftNeighbor)){
            nearbyFishes.add(this.leftNeighbor);
        }
        if(this.rightNeighbor != null && cellIsFish(this.rightNeighbor)){
            nearbyFishes.add(this.rightNeighbor);
        }
        if( this.upperNeighbor != null && cellIsFish(this.upperNeighbor)){
            nearbyFishes.add(this.upperNeighbor);
        }
        if(this.lowerNeighbor != null && cellIsFish(this.lowerNeighbor)){
            nearbyFishes.add(this.lowerNeighbor);
        }

        if(nearbyFishes.size()!= 0){

            Random rand = new Random();
            PredatorPreyCell slot =  nearbyFishes.get(rand.nextInt(nearbyFishes.size()));

            return slot;
        }

        return null;
    }

    public boolean cellIsWater(PredatorPreyCell cell){
        return cell.getCurrentState() == 5;
    }

    public boolean cellIsFish(PredatorPreyCell cell){
        return cell.getCurrentState() == 2;
    }

    /**
     * Define the right neighbor
     * @param rightNeighbor cell object representing the right neighbor
     */
    @Override
    public void setRightNeighbor(Cell rightNeighbor){
        this.rightNeighbor = (PredatorPreyCell) rightNeighbor;
    }

    /**
     * Define the left neighbor
     * @param leftNeighbor cell object representing the left neighbor
     */
    @Override
    public void setLeftNeighbor(Cell leftNeighbor){
        this.leftNeighbor = (PredatorPreyCell) leftNeighbor;
    }

    /**
     * Define the upper neighbor
     * @param upperNeighbor cell object representing the upper neighbor
     */
    @Override
    public void setUpperNeighbor(Cell upperNeighbor){
        this.upperNeighbor = (PredatorPreyCell)  upperNeighbor;
    }

    /**
     * Define the lower neighbor
     * @param lowerNeighbor cell object representing the lower neighbor
     */
    @Override
    public void setLowerNeighbor(Cell lowerNeighbor){
        this.lowerNeighbor = (PredatorPreyCell) lowerNeighbor;
    }

}
