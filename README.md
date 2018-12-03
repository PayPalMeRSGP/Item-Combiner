# Item-Combiner
Osbot script designed to automate any 2 item combination action. 

Common use cases: 
-stringing bows 

-creating unf potions 

-finishing potions 

-doing hosidious favor by making those special compost

 
# Usage Instructions
Currently this script ONLY works with NPC bankers, so no bank chests. 

Start script with an inventory setup with the 2 items to combine. The items are auto detected and the session's recipe will be set. 
Script will execute until either constitutient item runs out.

# Installation
A precompiled jar of the source code has been provided for you under the compiled folder. Drop this under the scripts folder in your osbot user files directory located under your user folder on your computer. 

# Special Notes
This script is written to simulate a markov chain. Most actions such as item creation have been redundantly coded to be solved in multiple distinct ways. 
For example, When interacting with the widget to confirm to combine items, there are 3 choices of which one is randomly chosen.
- AFK (62.5%; 100/160): interact with the widget, move mouse offscreen, remain idle for some random time even after all items have been combined
- HoverBank (31.25%; 50/160): interact with widget, then right click hover the bank option, when all items have combined immedietly click to open the bank
- PrematureStop (6.25%; 10/160): interact with widget, right click hover the bank, before all items have combined, click to open the bank canceling combining the remaining items. This is meant to simulate a EHP player mistiming the game ticks. 

Percentages are calculated from relative probability weights I've set. The following line defines the above branch probabilities.

Item-Combiner/src/nodes/bankingnodes/withdraw/Withdraw.java
```
private List<Edge> bothItemsWithdrawn = Arrays.asList(
            new Edge(AFKCreation.class, 100),
            new Edge(HoverBankerCreation.class, 50),
            new Edge(PrematureStopCreation.class, 10));
```

The AFK branch probability is calcuated by taking the weight of that branch (100) divided by the total weight sum of every branch (100 + 50 + 10). So 100/160 = 0.625. 

If desired you can modify these probabilites to your own desires. Doing so may grant your script a different profile than every other user. 
