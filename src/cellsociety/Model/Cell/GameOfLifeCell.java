package cellsociety.Model.Cell;
import cellsociety.Model.Cell.Cell;

public class GameOfLifeCell extends Cell{

    /**
     * Cell Constructor
     * @param state
     */
    public GameOfLifeCell(int state){
        super(state);
    }

    public static void setProb(){
    }

    public int calculateNextState(){
        return 1;
    }
}