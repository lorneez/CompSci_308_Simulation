package cellsociety.Model;

import java.util.ArrayList;

public class FireGrid extends Grid {

    public FireGrid(int rowSize, int colSize, ArrayList<Integer> initial_positions){
        super(rowSize, colSize, initial_positions);
    }

    @Override
    public Cell makeCell(int state) {
        return new FireCell(state);
    }
}
