# Refactor

## Longest Method
Our longest method was the setUpSplash() method in the GridViewer class. The method was extremely long because we implemented many buttons on the splash screen and each button had its own handle method. In order to refactor this method and make it more consise, we condensed the handle methods into one forloop that looped through an array of the files and buttons to initiate inside the handle method.

## Duplication Refactoring
Within the GridViewer class, there are 5 different methods to add different buttons, text fields, and labels in based on each simulation. For example, the addPredatorPreyButtons() method creates buttons, text fields, and labels for specifically resetting the predator prey simulation with new parameters. The reason why these 5 methods exhibit duplicate code is because the code needed to create these fields is all the same. We originally coded it as separate methods because each simulation requires different buttons and different information. In order to refactor this code, we will create ArrayLists to store the necessary button, text field, and label information inside the parse method of the GameEngine. Then, we will pass all of the information into GridViewer where a method will initiate all the necessary buttons, text fields, and labels.

## Checklist Refactoring
We can also replace some instances of HashMaps and ArrayLists with Maps and Lists respectively.

We also need to create a resource file to store all the button texts and rewrite the code that initiates buttons to take the text from the resource file.

## Code Smells
We have a lot of magic numbers and random strings scattered throughout our code that need to be cleaned up. We created some
final constants that specify the meaning of the constants used.


## Java Notes
###### Creating and destroying objects
- We currently do not destroy any objects.
###### Methods common to all objects
- We have made grid and cell abstract classes because each grid and cell type do have common methods. We are currently debating on making the GridViewer an abstract class in order to refactor some duplicate code. If we do this, then every GridViewer type will use common methods existing in the abstract GridViewer class.
###### Classes and interfaces
- We have divided our program into many classes. Some of our classes extend abstract classes.
###### Enums and annotations
- We need to add more Java Doc to some classes in order to make it more understandable. We are also thinking of adding minimal inline comments.
###### General programming
- In general, we are doing a good job designing and structuring the code, but there are small areas here and there where we can rewrite. We need to get rid of a lot of constants in the GridViewer class and set them as local variables.
###### Exceptions
- We have added many methods to handle exceptions that may arise in our code.
