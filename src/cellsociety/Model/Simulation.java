package cellsociety.Model;
import cellsociety.Model.Grid;

public abstract class Simulation {
    private Grid GameGrid;
    abstract void updateEdgeCell(int x, int y);
    abstract void updateMiddleCell(int x, int y);


}
