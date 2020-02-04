package cellsociety.Model.Cell;
import cellsociety.Model.Cell.Cell;
import javafx.scene.paint.Paint;

public class PercolationCell extends Cell{
    public static final int blockedState = 0;
    public static final int openState = 7;
    public static final int percolatedState = 5;

    /**
     * Cell Constructor
     * @param state
     */
    public PercolationCell(int state){
        super(state);
    }

    /**
     * Note this will only be called on cells in the open state
     * @return
     */
    public int calculateNextState(){
        Cell[] neighbors = getAllNeighbors();
        for(Cell c : neighbors){
            if(c.getCurrentState() == percolatedState){
                return percolatedState;
            }
        }
        return openState;
    }
}
