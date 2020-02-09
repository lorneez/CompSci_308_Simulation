# README





#### Error Checking for Incorrect File Data

1) Invalid Simulation Type or No Simulation Type Entered.
If the user misspells the name of the simulation or enters an incorrect
simulation name inside the <sim_type></sim_type> tag, a **SimulationTypeException** is thrown
and handled by displaying a popup window with a message. Similarly, if the user neglects
the <sim_type></sim_type> tag a window will be displayed.

fire_configA.XML file - Has an incorrect simulation name
fire_configB.XML file - Has no simulation tags

2) Invalid Cell State Values Given.
If the user enters cell state values that are not characteristic to
that particular simulation, then a General Exception is thrown
and handled by displaying a popup window with a message.

fire_configC.XML file - Has incorrect cell state numbers

3) Cell State Percentages DO NOT sum to 1 (Specific to percentage-based configuration)
   If the user enters a combination of cell state percentages that do
   not sum to 1, a General Exception is thrown and
   handled by displaying a popup window with a message.
   
   fire_configD.XML file - Has cell state percentages that do not sum to one
   
4) Total Number of blocks does not Equal the Row*Col parameters provided

If the user enters values for the row, column and total number of blocks
that does not obey the Row*Col = total # blocks formula, then a 
**RowColMismatchException** is thrown and handled by displaying 
a popup window with a message.

   fire_configE.XML file - Has has row, col, blockNumber parameters that do not obey formula (51*53 = 2500)
   
  
5) Neighbor Parameters are not set to either ON (true) or OFF (false) 

If the user enters values for the eight different neighbors that does not match either
'true' or 'false' strings, then a **InvalidNeighborException** is thrown and handled by displaying 
a popup window with a message.

 fire_configF.XML file - Has neighbors with parameters that are not true or false
 
 6) Any of the parameter tags are missing
 
 If any of the tags present in the configuration file are missing, a **MissingParametersException**
 is thrown and handled by displaying a popup window with a message.
 
   fire_configG.XML file - All the <edge></edge> tags are missing from the configuration file.
 



