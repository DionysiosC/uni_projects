
# Blackjack
* This is a simple blackjack game project implemented in java
* ~2020

## Introduction
In blackjack the goal is to take cards (hand) so that their total points are as close to 21 as possible (without exceeding it),
and are more than the dealer's. The cards give as many points as their number, the suits give 10,
and the ace gives 1 or 11 points, whichever is best for the player's score. The game is played as follows.
Official rules [here](https://bicyclecards.com/how-to-play/blackjack/ "Official rules by Bicycle").

## Classes documentation
### Contents:
1. [Card](#card)
2. [River](#river)
3. [Hand](#hand)
4. [Casino Customer](#casinocustomer)
5. [Player](#player)
6. [Dealer](#dealer)
7. [Round](#round)
8. [BlackjackTable (the main class)](#blackjacktable)


### Card
Holds information about a card. For our game we don't care about its order/colour
card, but only its number/figure. The class holds the number/figure (String) that is initialized
in the constructor, and the value of the paper being calculated. We also have the following methods:
1. getValue: returns the value of the paper. Ace has a value of 1 and figures have a value of 10.
2. isAce: Returns boolean value whether it is an ace or not
3. equals: our familiar method that returns true if the cards have the same figure/animals.
4. toString: Returns just the figure/animation.

### River
The River class: implements the stream of cards. This class will be initialized with the number of decks the stream has. It has a field a table of Card objects that holds all the cards from the decks (the order doesn't matter), which is created and populated in the constructor; it also has the following methods:
1. nextCard: this is the base method of the class, which returns a random card (Card) from the remaining cards in the stream. 
2. shouldRestart: Returns true if the number of cards left falls below a quarter of the original cards 
3. restart: Shuffles the decks again. 

### Hand
Which holds information about a hand. The class has an ArrayList field of cards (Card). It also has the following methods:
1. addCard: takes a card as an argument and adds it to the hand.
2. score: Calculates the points of the hand. If the hand does not contain an ace, then it is simply the sum of If the hand contains no hand, it is the sum of the hand's values. If the hand contains at least one ace, and the sum of the hand values is P, and P+10 ≤ 21, then the actual points of the hand are P+10, because the ace is valued for 11 points.
3. canSplit: Returns a Boolean value if the hand can be split, i.e., it contains two identical cards.
4. split: Called when we want to split a hand and returns a table with two new hands, in the which we have added one of the original cards from the hand.
5. isBlackjack: Returns a Boolean value if the hand is blackjack, i.e. 21 with two cards.
6. isBust: Returns a Boolean value whether the hand is "bust" or not.
7. toString: Returns a String with the cards separated by a space.

### CasinoCustomer
Holds information on a casino patron. The class has fields for the player's name, and the money he has to play with. These fields are initialized in the constructor. The class handles everything to do with the player's money status. It will have the following methods:
1. payBet: It takes as an argument the amount of a bet that the player lost and subtracts it from the player's money.
2. collectBet: Takes as a prize the amount of a bet that the player won and adds it to the player's money. The player takes a bet that the player has won and adds it to the player's money.
3. canCover: Takes as an argument the amount of a bet and returns a Boolean value if the player has enough money to cover it.
4. isBroke: returns a Boolean value if the player's money drops below 1 euro.
5. toString: Returns a String with the player's name.
6. printState: Prints the player's name and money.

### Player
Holds information about a player playing at the table for one round. The class has fields a CasinoCustomer object, a Hand object, and an actual bet value which is the player's bet. The class manages the customer's play for one round. It will have the following methods:
1. wins: the method called when the player wins. It prints a message and the client gets the
The customer receives the bet money.
2. winsBlackJack: The method called when the player wins with blackjack. The player who wins when the jackpot is won is called by the customer. The customer receives one and a half times the bet money.
3. loses: the method called when the player loses. The caller who loses when the customer loses is called when the player loses. The customer sends a message and pays the bet money.
4. placeBet: It prints the financial status of the player and asks him to place a bet. Checks if the player has made a bet. The bettor asks if the player has the money and if the bet is acceptable (at least 1 euro). he gets an accepted bet.
5. doubleBet: Doubles the bet.
6. wantsToDouble: Checks if the player has enough money to cover the double bet, and if so, asks the user if he wants to double the bet. Returns a boolean value if the user can and wants to double.
7. wantsToSplit: Checks if the player has enough money to cover the double bet. If so, it asks the user if he wants to split. Returns a boolean value if the user can and wants to split. split.
 
### Dealer
Holds information on the table dealer for one round. The class has fields for a River object and a Hand object. The River is initialized in the constructor. It has also has the following methods:
1. draw: the dealer draws a card from the stream and adds it to his hand.
2. deal: Takes a Player object as an argument and adds a card from the stream to the player's hand.
3. play: implements the dealer's play. As long as the hand score is less than 17, he continues to draw cards
4. settle: settles the dealer's outstanding balances with a player. It takes as its argument a Player object. and checks which hand wins, if any. Depending on whether the player won or lost the player is paid or pays the bet (called wins/loses).

### Round
Implements a round of the game. It fields a Dealer object, and an ArrayList of Players that are the players in the round. It also has another ArrayList of Player objects that holds the players for whom the dealer has pending (must settle). The constructor takes a River object as an argument and creates the Dealer object. We also have the following methods:
1. addPlayer: takes as argument a CasinoCustomer object and creates and adds the corresponding Player to the players at the table.
2. playRound: implements the base game. Initially, all players bet. Then the dealer deals one of the cards. card to each player, and draws one for him. We print the dealer's hand to see the dealer's hand. his face-up card. The dealer then deals a second card to each player and draws one for each player. for him. We print the players' hands. We check to see if the dealer has blackjack. If he does, he collects the money. from the players who don't have blackjack and the round ends. If he doesn't have blackjack the dealer looks at each player individually. If the player has blackjack, the dealer pays 1.5 times the bet. If not, he must play the player. For this purpose it is convenient to do auxiliary methods. 
#### Auxiliary methods:

a. playNormalHand: implements the classic case where the player draws until he decides to stop. If he gets burned he loses and pays the bet.
b. playDoubledHand: implements the game when the player makes a double.
c. playSplitHand: implements when the player splits. Creates two new Player objects for each of the new hands (with the same bet and client) and calls playNormalHand for each.
d. playPlayer: checks if the hand is a candidate for a split and the player wants to and can split, or wants to and can double, or none of the above applies, and calls the corresponding one of the helper methods.

### BlackjackTable
Ιmplements a blackjack table. It has as fields a River object, a table of CasinoCustomer objects and the number of participants. For the River we will use 6 decks of cards. The number of participants is given as an argument to the constructor which creates and populates the table. There are the following methods defined in the class as well:
1. createCasinoCustomer: This is a helper method (private) that asks the user to provide for each player his name and available funds that has created the object and returns it.
2. play: implements the game. Each round a Round object is created in which only players with enough money are passed to. Before the round starts it is checked if the flow of cards needs to be refreshed, and if so, it restarts. The game continues until all players lose all their money (😥).

## Example:
Here is a generic example that communicates the basic idea. It is the example copied from the requirements file, does not represent my actual program.
```
C:\[...]>java Blackjack
Give the number of players:2
Give customer name and available money
Alice 100
Give customer name and available money
Bob 200
---- New Round! ---
Alice has 100.0 left
Alice place your bet: 20
Bob has 200.0 left
Bob place your bet: 40
Dealer: K
Alice: Q Q
Bob: 8 7
Player Alice: Q Q
Do you want to split: y
Alice: Q
Hit?: y
Alice: Q 6
Hit? n
Alice: Q
Hit?: y
Alice: Q 7
Hit? n
Player Bob: 8 7
Do you want to double?: n
Hit?: y
Bob: 8 7 9
Player Bob lost! Pay $40.0
Dealer: K 7
Player Alice lost! Pay $20.0
Tie with Alice. Nobody wins
---- New Round! ---
(...)
```
