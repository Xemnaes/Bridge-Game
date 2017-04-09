# Bridge-Game
Play a game of Contract Bridge using this java application. Utilizes the ACBL Standard American Yellow Card guidelines for bidding.

# Basics of Contract Bridge

Contract Bridge is a card game play with 4 people, each positioned at different corners of the table. Partnerships are formed from players sitting opposite of each other, North-South and East-West. The game is comprised of two parts: The bidding phase and the playing phase.

### The Bidding Phase

The first phase of bridge begins when each player is dealt a hand of 13 cards each. Based on the composition of their hands, players will "bid" in a clockwise manor, creating what is known as a "contract". A contract is comprised of 2 parts: The level and the strain. In contract bridge, the goal is to for the partners who made the resultant contract to make the amount of "tricks" they said they could make. The number of tricks is determined by adding the contract level and six. Thus, contract levels range from 1 to 7 (7+6=13, the maximum number of tricks that can be made). The strain, also known as a "trump suit", can be either club, diamond, heart, spade, or No Trump. This variable is also determined by hand composition.

In this phase, you can either call a contract or "pass", which means the player is satisfied with the current contract. Once three players have passed in a row, the playing phase may begin. The bidding convention used in this program is the Standard American Yellow Card (SAYC) system. While other calls during the bidding phase, such as penalties and doubles, we do not uses these conventions for simplicity sake.

### The Playing Phase

In the seocnd phase, the player left of the declarer (the player who first bid the strain of the contract) begins the first trick. The person who played the contract on the attacking side also plays their partner's hand, who displays their hand by suit once their turn has arrived during the first trick. They are what's known as the "dummy". The first card in a trick is the set suit for that trick. A player must play a card of that suit if they have one in their possesion. The player with the highest card of that suite during the round wins the trick.

The contracts, though, also contain the previously mentioned trump suit. If a player does not have a card that is currently being played, but does have a card with the given trump suit, when played, that card "trumps" all other cards other than that suit, depsite it's value.

Once all tricks have been completed, the amount of tricks the partners on the attacking side made are then reduced by adding the contract level and six, then subtracting that value from the number of tricks committed. 

This is by no means a comprehensive description, but should provide a basic understanding of the game's flow.

# Using the program

This game utilizies the command prompt for user input and game display. The game will first generate a hand for the user, and then ask them for a contract. The user continues to bid until three consecutive passes are made (AI bids are displayed as well). Afterwards, the game enters the playing phase. Here, the player chooses a card from their hand via numberpad to play for a given trick. The AI will display their cards for each trick, similar to how they showed their bids. Once all thirteen tricks have been completed, a score is calculated for the attacker (negative # for amount of tricks below contract, positive # for amount of tricks above contract). Lastly, the program asks if the user would like to play again.

### Inputs for the program

  ##### Bidding Phase
  Levels: 1 2 3 4 5 6 7
  
  Strains: C = Club  D = Diamond  H = Heart  S = Spade  T = No Trump

  ##### Playing Phase
  The number under each card displayed
