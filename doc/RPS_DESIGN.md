# RPS_DESIGN

1 -  High-level Design Ideas

Run Simulation - The main class that handles all the components. It will read in the file containing data on the weapons into an array and initiate the different players and weapons. Creates an instance of armory class. Players are given access to that Armory instance.

Armory - Keep track of the different weapons that exist. Only names are public. The purpose of this class is to encapsulate the data structure of the available weapons. It will also update 

Game Engine - Contains a method that executes a single round of RPS. Keeps track of the players and tells them when it's time to choose and compare weapons, and outputs a result.

Weapon - This class represents a single, generic weapon that can be named and viewed as anything. It will allow different weapons to interact. The class will know its name, image, what defeats it and what it defeats. However, other information about weapons will be hidden from any particular instance.

Player - This class mainly functions to keep track of a player's history of interactions. Allows the Players to intelligently choose their weapon. Players can compare weapons and then determine a loss/tie/win. This class will know its weapon and its history of interactions. It will interact with the Run Simulation and Weapon classes.

2 -  CRC Cards

Player:
Responsibilities:
    - knows what it will throw next round
    - knows its history of moves
    - knows how to choose weapons
Collaborators:
    - weapon
    - game engine
    - armory

Weapon:
Responsibilities:
    - knows its image
    - knows what weapons it destroys
Collaborators:
    - player
    - armory

Game Engine:
Responsibilities:
    - knows what data is taken in
    - knows what players are in the simulaion
Collaborators:
    - run simulation
    - player
    - armory

Armory:
Responsibilities:
    - knows what weapons exist
Collaborators:
    - player
    - weapon

Run Simulation:
Responsibilities:
    - read in the weapons data 
    - initialize an Armory instance
    - initialize two Players.
Collaborartors:
    - Player
    - Game Engine
    - Armory

3 -  Example Code to solve Use Cases

A new game is started with two players, their scores are reset to 0:
    `
    run_simulation.start()
    game_engine.restart()
    player1.setScore(0)
    player2.setScore(0)`

A player chooses his RPS "weapon" with which he wants to play for this round:

    Weapon w1 = player1.choose() // The Player class has a choose method that intelligently chooses 
                                 // a Weapon based on the players prior weapon choices
                                 

Given two players' choices, one player wins the round, and their scores are updated:
`
**Inside Run Simulation class**
game_engine.round()
**Inside Game Engine**
public void round(){
    if(player1.compare(player2.getWeapon)){
        player1.win()
    }
    else if(player2.compare(player1.getWeapon){
        player2.win()
    }
    else{
        player1.tie()
        player2.tie()
    }
    player1.record()
    player2.record()
}
`

A new choice is added to an existing game and its relationship to all the other choices is updated:
`Weapon newWeapon = ...
armory.add(newWeapon)
`

A new game is added to the system, with its own relationships for its all its "weapons":
`
run_simulation.newGame(file)
`