# My Personal Project

## General Info

In this game, the user will be able to use their knowledge of flags to guess flags and compete with other players. Anyone with an interest in geography can play
it, but really this game is open to anyone to give a try! This game was inspired by my memorization of all __197 flags of the world__, so I decided to create a game for others to try, combining 2 of my interests, coding and geography into one role.


## User Stories

Phase 1: 
* As a user, I want to be able to type in my guess and receive feedback.
* As a user, I want to be able to choose between easy, normal and hard mode. 
* As a user, I want to be able to stop my current game and start a new game. 
* As a user, I want to be able to type in the number of flags I want to guess for each game.  


 Phase 2:
* As a user, I want to be able to resume from where I left off in my previous games. 
* As a user, I want to be able to have the option to save my game to file when I quit.

## Instructions for grader: 
- You can generate the first required action related to adding Xs to a Y by guessing the flag correctly or skipping it. (view the next X)
- You can generate the second required action related to adding Xs to a Y by clicking on the Answer Key button which displays the names of all the flags in the game. 
- You can locate my visual component in the flag panel and the background image. 
- You can save the state of my application by clicking the save button or press Yes when you click Quit button and it asks "Would you like to save your game". 
- You can reload the state of my application by re-opening the game and you will be prompted to load your game. 

Phase 4: Task 2<br />
Fri Apr 07 21:41:52 PDT 2023<br />
Created a new game with 5 flags and difficulty 1<br />
Fri Apr 07 21:41:54 PDT 2023<br />
Guessed Flag #1 correctly out of 5<br />
Fri Apr 07 21:41:56 PDT 2023<br />
Guessed Flag #2 correctly out of 5<br />
Fri Apr 07 21:41:56 PDT 2023<br />
Skipped Flag #3 out of 5<br />
Fri Apr 07 21:41:58 PDT 2023<br />
Skipped Flag #4 out of 5<br />
<br /><br />
Phase 4: Task 3<br />
If I had more time to work on my project, one refactoring actions I would perform is the Single Responsibility Principle. Looking at my UML Diagram, I noticed that I just have one single class, GameGUI, that represents every function of the game. The GameGUI class has too many responsibilities that can definitely be split into multiple classes. It makes sense for the GameGUI to only contain properties and functions that make up the GUI itself. For example, each action that the game performs can be in a separate class. The easy, medium and hard flag lists should be declared elsewhere and not initialized everytime I create a new GameGUI. That way, cohesion is increased without increasing the coupling too much. 
