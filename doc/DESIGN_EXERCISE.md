# DESIGN EXERCISE

High-level design ideas
1. How does a Cell know about its neighbors? How can it update itself without effecting its neighbors update?
    - A cell will have its location in the 2D array (or it will be a node that knows its neighbors).
    - Each cell will have its state and "next" state.
    - Look at all cells, update its "next" state attribute
    - Once each cell's "next" state is updated, "commit" the update by switching each cell to its new state
2. What relationship exists between a Cell and a simulation's rules?
    - How do neighbors affect it
    - How does its own state affect it
    - How many cell types are there
3. What is the grid? Does it have any behaviors? Who needs to know about it?
    - The grid houses all the cells
    - It runs updates and holds cell information
    - The GUI needs to know about it
4. What information about a simulation needs to be in the configuration file?
    - Possible cell states
    - When a cell changes states
    - Shape of grid and initial cell states
5. How is the graphical view of the simulation updated after all the cells have been updated?
    - Each cell will update its display based on the state. The grid will show the cells as different colors.
    - Total activity will change.
    - Maybe: only update cells that changed

CRC cards
Cell:
Responsibilities:
    - Know its location (row, column)
    - Know its state
    - Know its updated state
    - Know its neighbors
Collaborators: 
    - Grid

Grid:
Responsibilities:
    - Know its size
    - Know which cells to update
    - Know what cells are in each state
Collaborators:
    - Cell
    - view

Simulation:
Responsibilities:
    - create grid
    - initiate cells
    - runs rounds
Collaborators: 

Game Engine:
Responsibilities:
    - Initiates the simulation by reading in the file, updating the rules
    - Starts a single simulation run
Collaborators:
    - Simulation

View:
Responsibilities:
    - displays all blocks
    - knows what state corresponds to what color
    - knows block size
    - has maps of blocks
Collaborators: 
    - grid

