package cellsociety.Model.Cell;
import cellsociety.Model.Cell.Cell;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Class representing a cell object for the predator prey simulation
 * @author caryshindell, lornezhang, ameersyedibrahim
 * Dependencies: Cell class
 * Example: a fish cell that has all water neighbors, which is free to move
 */
public class PredatorPreyCell extends Cell{

    protected ArrayList<PredatorPreyCell> neighbors;

    private double energy;
    private double chronons_passed;
    private final int REPRODUCTION_THRESHOLD_FISH = 10; // chronon threshold, not sure what this number should be
    private final int REPRODUCTION_THRESHOLD_SHARK = 4; // chronon threshold, not sure what this number should be

    @SuppressWarnings("FieldCanBeLocal")
    private final int FISH_ENERGY = 10;
    @SuppressWarnings("FieldCanBeLocal")
    private final int SHARK_ENERGY = 20;
    private boolean canReproduce = false;

    /**
     * Cell Constructor
     * @param state
     */
    public PredatorPreyCell(int state){
        super(state);
        if (this.currentState == 0){
            energy = SHARK_ENERGY;
        }
        this.chronons_passed = 0;
    }

    /**
     * Calculate and set the next state depending on the current chronon, state, and the nearby neighbors states
     * @return the current cell state prior to the update
     */
    public int calculateNextState(){
        /*
        water = state 5, fish = state 2, shark = state 0
        */
        // if cell is a fish
        if (this.currentState == 2){
            this.fishConditions();
        }
        // if the cell is a shark
        else if (this.currentState == 0){
            this.sharkConditions();
        }
        int saveState = currentState;
        currentState = nextState;
        return saveState;
    }

    /**
     * Determine which of the surrounding neighbors of the current cell are water
     * @return a randomly selected water cell (if more than one exists), otherwise null
     */
    public PredatorPreyCell neighborWater(){

        ArrayList<PredatorPreyCell> openWater = new ArrayList<>();
        for(int i=0; i<numNeighbors; i++){
            if( this.neighbors.get(i) != null && cellIsWater(this.neighbors.get(i))){
                openWater.add(this.neighbors.get(i));
            }
        }

        if(openWater.size()!= 0){
            Collections.shuffle(openWater);

            return openWater.get(0);
        }
        return null;
    }

    /**
     * Determine which of the surrounding neighbors of the current cell are fish
     * @return a randomly selected fish cell (if more than one exists), otherwise null
     */
    public PredatorPreyCell neighborFish(){
        ArrayList<PredatorPreyCell> nearbyFishes = new ArrayList<>();
        for(int i=0; i<numNeighbors; i++){
            if(this.neighbors.get(i) != null && cellIsFish(this.neighbors.get(i))){
                nearbyFishes.add(this.neighbors.get(i));
            }
        }

        if(nearbyFishes.size()!= 0){
            Collections.shuffle(nearbyFishes);

            return nearbyFishes.get(0);
        }
        return null;
    }


    /**
     * Given the state of the current cell, its neighboring cells and its chronon count, determine the next state of the cell.
     * Mehtod functionality is used to set the nextState instance variable for the cell.
     *
     * This particular method executes if the current cell is a fish
     */
    public void fishConditions(){
        if(chronons_passed == REPRODUCTION_THRESHOLD_FISH){
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

    /**
     * Given the state of the current cell, its neighboring cells and its chronon count, determine the next state of the cell.
     * Mehtod functionality is used to set the nextState instance variable for the cell.
     *
     * This particular method executes if the current cell is a shark
     */
    public void sharkConditions(){
        // enable reproduction if the number of chronons is reached
        if (this.chronons_passed == REPRODUCTION_THRESHOLD_SHARK){
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
            the_fish_neighbor.chronons_passed = this.chronons_passed + 1;
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
                the_neighbor.energy = this.energy - 1;
                the_neighbor.canReproduce = false;
                System.out.println("Shark reproduced");
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
            this.chronons_passed += 1;
        }
    }

    /**
     * Determine if the current cell is a water cell
     * @param cell any given cell in this class to be determined if it is water
     * @return true or false depending on whether the cell is water or not
     */
    public boolean cellIsWater(PredatorPreyCell cell){
        return cell.getCurrentState() == 5;
    }

    /**
     * Determine if the current cell is a fish cell
     * @param cell any given cell in this class to be determined if it is a fish
     * @return true or false depending on whether the cell is fish or not
     */
    public boolean cellIsFish(PredatorPreyCell cell){
        return cell.getCurrentState() == 2;
    }

    /**
     * Define a neighbor in a given position (e.g. left) with a certain cell
     * @param whichNeighbor position (as an index to the list)
     * @param neighborCell cell object representing the neighbor
     */
    @Override
    public void setNeighbor(int whichNeighbor, Cell neighborCell){
        neighbors.set(whichNeighbor, (PredatorPreyCell) neighborCell);
    }
}
