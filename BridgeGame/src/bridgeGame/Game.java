package bridgeGame;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Game {
	//Initializes the deck object used in the game.
	private Deck deck;
	//Initializes who starts first during the play.
	private int declarer;
	//Initializes whether or not the player is the attacker.
	private boolean attacker = false;
	/**
	 * Creates a Contract Bridge game. Uses a given (shuffled) deck.
	 * 
	 * @param d The given deck.
	 */
	public Game(Deck d)
	{
		deck = d;
	}
	
	/**
	 * This method creates the hands to be used in the bidding and play phases.
	 * The first hand will be the players. The other 3 will be for the AI.
	 * 
	 * @return (4) Hands to be used for the bidding and play phases.
	 */
	public ArrayList<Hand> deal()
	{
		ArrayList<Hand> hands = new ArrayList<Hand>();
		for(int i = 0; i<4; i++)
		{
			ArrayList<Card> cards = new ArrayList<Card>();
			for(int j = 0; j<13; j++)
			{
				cards.add(deck.getCards().get(0));
				deck.getCards().remove(0);
			}
			Hand hand = new Hand(cards);
			hands.add(hand);
		}
		return hands;
	}
	
	/**
	 * This method simulates the bidding phase of contract bridge. For simplicity sake,
	 * we will be using the ABCL Standard American Yellow Card system. The play will
	 * always start the bid, and the AI will respond. After 3 consecutive passes are made,
	 * the player/AI who last bid will be the attackers, and vice versa.
	 * 
	 * <p> The first hand (0) belongs to the player. Hands 1 and 3 belong to the opposing partners,
	 * and hand 2 is your AI partner. </p>
	 * 
	 * @param ArrayList of hands for the game
	 * @return The contract to be used during the playing phase
	 */
	public Contract biddingPhase(ArrayList<Hand> hands)
	{
		int pass = 0;
		int turns = 0;
		GameOutput output = new GameOutput();
		Contract currentContract = new Contract('P',0);
		ArrayList<Contract> contracts = new ArrayList<Contract>();
		while(pass<3 || pass==3 && turns==3)
		{
			if(turns%4==0)
			{
				Contract contract = ContractPlayer(hands.get(turns%4), contracts, currentContract, output);
				if(contract.getStrain()=='P' || contract.getLevel()==0)
				{
					pass++;
				}
				else
				{
					currentContract = contract;
					pass = 0;
				}
				contracts.add(contract);
				turns++;
				System.out.println("Player has bid: "+contract.getLevel()+" "+contract.getStrain());
			}
			else
			{
				//Retrieve your partner's last contract
				Contract partner = null;
				if(turns>=2)
				{
					partner = contracts.get(contracts.size()-turns%4);
				}
				Contract contract = this.ContractAI(hands.get(turns%4), contracts, currentContract, partner);
				if(contract.getStrain()=='P' || contract.getLevel()==0)
				{
					pass++;
				}
				else
				{	
					currentContract = contract;
					pass = 0;
				}
				contracts.add(contract);
				String name = intToAI(turns%4);
				System.out.println(name+ " has bid: "+contract.getLevel()+" "+contract.getStrain());
				turns++;
			}
		}
		declarer = getDeclarer(contracts, currentContract)%4;
		if(contracts.indexOf(currentContract)%4==0 || contracts.indexOf(currentContract)%4==2)
		{
			attacker = true;
		}
		return currentContract;
	}
	
	public int getDeclarer(ArrayList<Contract> contracts, Contract curContract)
	{
		for(int i = 0; i<contracts.size(); i++)
		{
			if(contracts.get(i).getStrain()==curContract.getStrain())
			{
				return i;
			}
		}
		return contracts.size();
	}
	
	
	/**
	 * This method handles the AI during the bidding phase. Using the given hand and
	 * previous contracts, the AI will choose the best contract for their turn.
	 * 
	 * @param hand The given hand for the AI
	 * @param contracts The list of previous contracts
	 * @param curContract The current contract to be played
	 * @param partner Your partner's last contract
	 * @return A new contract for the player
	 */
	public Contract ContractAI(Hand hand,ArrayList<Contract> contracts, Contract curContract, Contract partner)
	{
		int HCP = hand.getHCP();
		int[] handMakeup = hand.getCardComp();
		int clubs = handMakeup[0];
		int diamonds = handMakeup[1];
		int hearts = handMakeup[2];
		int spades = handMakeup[3];
		// If the AI has less than 5 HCP, pass automatically.
		if(HCP<5)
		{
			return new Contract('P',0);
		}
		//If the AI is making their first bid
		if(partner==null || contracts.size()<4 && partner.getLevel()==0 && partner.getStrain()=='P')
		{
			// If HCP are between 5 and 11 points and C&D have 3 cards, return a 1C contract.
			if(HCP>=5 && HCP<=11 && clubs==3 && diamonds==3 && levelIsValid(1,curContract) && strainIsValid('C',1,curContract))
			{
				return new Contract('C',1);
			}
			// If HCP are between 5 and 11 points and C&D have 4 cards, return a 1D contract.
			if(HCP>=5 && HCP<=11 && clubs==4 && diamonds==4 && levelIsValid(1,curContract) && strainIsValid('D',1,curContract))
			{
				return new Contract('D',1);
			}
			// If HCP is greater than 12 points and S<H and H has 5 or 6 cards, return a 1H contract.
			if(HCP>=13 && hearts==5 && hearts>spades && levelIsValid(1,curContract) && strainIsValid('H',1,curContract) ||
			   HCP>=13 && hearts==6 && hearts>spades && levelIsValid(1,curContract) && strainIsValid('H',1,curContract))
			{
				return new Contract('H',1);
			}
			// If HCP is greater than 12 points and S has 5 or 6 cards, return a 1S contract.
			if(HCP>=13 && spades==5 && levelIsValid(1,curContract) && strainIsValid('S',1,curContract) ||
			   HCP>=13 && spades==6 && levelIsValid(1,curContract) && strainIsValid('S',1,curContract))
			{
				return new Contract('S',1);
			}
			// If HCP are between 15 and 17 points and contract can be played, return a 1NT contract.
			if(HCP>=15 && HCP<=17 && levelIsValid(1,curContract) && strainIsValid('T',1,curContract) &&
			   clubs>1 && diamonds>1 && hearts>1 && spades>1)
			{
				return new Contract('T',1);
			}
			// If HCP are greater than 22 points, return a 2C contract.
			if(HCP>=22 && levelIsValid(2,curContract) && strainIsValid('C',2,curContract))
			{
				return new Contract('C',2);
			}
			// If HCP are between 5 and 11 points with 6+ D cards, return a 2D contract.
			if(HCP>=5 && HCP<=11 && levelIsValid(2,curContract) && strainIsValid('D',2,curContract))
			{
				return new Contract('D',2);
			}
			// If HCP are between 5 and 11 points with 6+ H cards, return a 2H contract.
			if(HCP>=5 && HCP<=11 && levelIsValid(2,curContract) && strainIsValid('H',2,curContract))
			{
				return new Contract('H',2);
			}
			// If HCP are between 5 and 11 points with 6+ S cards, return a 2S contract.
			if(HCP>=5 && HCP<=11 && levelIsValid(2,curContract) && strainIsValid('S',2,curContract))
			{
				return new Contract('S',2);
			}
			// If HCP are between 20 and 21 points and contract can be played, return a 2NT contract.
			if(HCP>=20 && HCP<=21 && levelIsValid(2,curContract) && strainIsValid('T',2,curContract) &&
			   clubs>1 && diamonds>1 && hearts>1 && spades>1)
			{
				return new Contract('T',2);
			}
		}
		//AI responses to partner's bid.
		else
		{
			Contract contract = ContractAIResponse(hand,handMakeup,HCP,partner,curContract);
			return contract;
		}
		//In case of overshoot, pass automatically.
		return new Contract('P',0);
	}
	
	/**
	 * This method handles the player during the bidding phase. Using the given hand and
	 * previous contracts, the player should choose the best contract for their turn.
	 * @param hand The given hand of the player
	 * @param contracts List of previous contracts 
	 * @param curContract The current contract to be played
	 * @param output The GameOutput object used for displaying the hand
	 * @return The player's contract
	 */
	public Contract ContractPlayer(Hand hand,ArrayList<Contract> contracts, Contract curContract, GameOutput output)
	{
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		if(contracts.isEmpty())
		{
			String[] display = output.showHand(hand);
			System.out.println(display[0]);
		}
		int level = -1;
		int attempts = 0;
		boolean valid = levelIsValid(level, curContract);
		System.out.println("Please enter your bid level: ");
		while(valid==false || level==-1)
		{
			try
			{
				if(attempts>0)
				{
					System.out.println("Please enter a valid level: ");
				}
				level = sc.nextInt();
			}
			catch (InputMismatchException ime)
			{
				System.out.println("Please enter a valid level: ");
				sc.nextLine();
				attempts = -1;
			}
			valid = levelIsValid(level, curContract);
			if(attempts!=-1)
			{
				attempts++;	
			}
		}
		attempts = 0;
		char strain = 'Z';
		char testStrain;
		valid = strainIsValid(strain, level, curContract);
		System.out.println("Please enter your bid strain: ");
		while(valid==false || strain=='Z')
		{
			try
			{
				if(attempts>0)
				{
					System.out.println("Please enter a valid strain: ");
				}
				testStrain = sc.next(".").charAt(0);
				strain = testStrain;
			}
			catch (InputMismatchException ime)
			{
				System.out.println("Please enter a valid strain: ");
				sc.nextLine();
				attempts=-1;
			}
			valid = strainIsValid(strain, level, curContract);
			if(attempts!=-1)
			{
				attempts++;
			}
		}
		Contract contract = new Contract(strain, level);
		return contract;
	}
	
	/**
	 * Determines whether or not the strain for a newly made contract is valid.
	 * 
	 * @param strain The suit of the contract
	 * @param level The level of the contract
	 * @param curContract The current played contract
	 * @return True if strain is valid; false otherwise
	 */
	private boolean strainIsValid(char strain, int level, Contract curContract) {
		char curStrain = curContract.getStrain();
		int curLevel = curContract.getLevel();
		ArrayList<Character> suitList = new ArrayList<Character>();
		suitList.add('C');
		suitList.add('D');
		suitList.add('H');
		suitList.add('S');
		suitList.add('T');
		suitList.add('P');
		boolean valST = suitList.contains(strain);
		if(strain>curStrain && valST==true|| strain<=curStrain && level>curLevel && valST==true || strain=='P')
		{
			return true;
		}
		return false;
	}

	/**
	 * Determines whether or not the level for a newly made contract is valid.
	 * @param l Given level from the contract.
	 * @param curContract The current contract
	 * @return True if level is valid; false otherwise.
	 */
	public boolean levelIsValid(int l, Contract curContract)
	{
		int curLevel = curContract.getLevel();
		boolean valL = l>=1 && l<=7;
		if(curLevel<l && valL==true || curLevel==l && curContract.getStrain()!='T' && valL==true || l==0)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Handles the AI's response to a partner's contract.
	 * @param hand The current hand
	 * @param handMakeup The current hand suite composition
	 * @param HCP The hand's HCP value
	 * @param partner The partner's last contract
	 * @param curContract The current contract
	 * @return
	 */
	public Contract ContractAIResponse(Hand hand, int[] handMakeup, int HCP, Contract partner, Contract curContract)
	{
		int clubs = handMakeup[0];
		int diamonds = handMakeup[1];
		int hearts = handMakeup[2];
		int spades = handMakeup[3];
		boolean balanced = false;
		if(clubs>1 && diamonds >1 && hearts>1 && spades>1)
		{
			balanced = true;
		}
		//Response to a 1C contract
		if(partner.getLevel()==1 && partner.getStrain()=='C')
		{
			if(HCP>=13 && HCP<=15 && levelIsValid(2,curContract) && strainIsValid('T',2,curContract))
			{
				return new Contract('T',2);
			}
			if(HCP>=16 && HCP<=17 && levelIsValid(3,curContract) && strainIsValid('T',3,curContract))
			{
				return new Contract('T',3);
			}
		}
		//Response to 1D contract
		if(partner.getLevel()==1 && partner.getStrain()=='D')
		{
			if(HCP>=13 && HCP<=15 && levelIsValid(2,curContract) && strainIsValid('T',2,curContract))
			{
				return new Contract('T',2);
			}
			if(HCP>=16 && HCP<=17 && levelIsValid(3,curContract) && strainIsValid('T',3,curContract))
			{
				return new Contract('T',3);
			}
		}
		//Response to 1H contract
		if(partner.getLevel()==1 && partner.getStrain()=='H' || partner.getLevel()==1 && partner.getStrain()=='S')
		{
			if(HCP>=6 && spades>=4 && hearts==0 && levelIsValid(1,curContract) && strainIsValid('S',1,curContract))
			{
				return new Contract('S',1);
			}
			if(HCP>=6 && HCP<=9 && spades!=4 && hearts!=3 && levelIsValid(1,curContract) && strainIsValid('T',1,curContract))
			{
				return new Contract('T',1);
			}
			if(HCP>=10 && diamonds>=4 && levelIsValid(1,curContract) && strainIsValid('D',1,curContract))
			{
				return new Contract('D',1);
			}
			if(HCP>=10 && clubs>=4 && levelIsValid(1,curContract) && strainIsValid('C',1,curContract))
			{
				return new Contract('C',1);
			}
			if(HCP>=13 && levelIsValid(2,curContract) && strainIsValid('T',2,curContract))
			{
				return new Contract('T',2);
			}
			if(HCP>=10 && HCP<=12 && hearts>=3 && levelIsValid(3,curContract) && strainIsValid('H',3,curContract))
			{
				return new Contract('H',3);
			}
			if(HCP>=15 && HCP<=17 && balanced && levelIsValid(3,curContract) && strainIsValid('T',3,curContract))
			{
				return new Contract('T',3);
			}
			if(HCP<10 && hearts>=5 && levelIsValid(4,curContract) && strainIsValid('H',4,curContract))
			{
				return new Contract('H',4);
			}
		}
		//Response to 1NT contract
		if(partner.getLevel()==1 && partner.getStrain()=='T')
		{
			if(HCP>=8 && levelIsValid(2,curContract) && strainIsValid('C',2,curContract))
			{
				return new Contract('C',2);
			}
		}
		//Response to 2C contract
		if(partner.getLevel()==2 && partner.getStrain()=='C')
		{
			if(HCP>=8 && spades>=5 && levelIsValid(2,curContract) && strainIsValid('S',2,curContract))
			{
				return new Contract('S',2);
			}
			if(HCP>=8 && hearts>=5 && levelIsValid(2,curContract) && strainIsValid('H',2,curContract))
			{
				return new Contract('H',2);
			}
			if(HCP>=8 && diamonds>=5 && levelIsValid(3,curContract) && strainIsValid('D',3,curContract))
			{
				return new Contract('D',3);
			}
			if(HCP>=8 && clubs>=5 && levelIsValid(3,curContract) && strainIsValid('C',3,curContract))
			{
				return new Contract('C',3);
			}
			if(HCP==8 && balanced && levelIsValid(2,curContract) && strainIsValid('T',2,curContract))
			{
				return new Contract('T',2);
			}
		}
		//Response to 2D,2H,2S contract
		if(partner.getLevel()==2 && partner.getStrain()=='D' || partner.getLevel()==2 && partner.getStrain()=='H' ||
		   partner.getLevel()==2 && partner.getStrain()=='S')
		{
			if(levelIsValid(2,curContract) && strainIsValid('T',2,curContract))
			{
				return new Contract('T',2);
			}
		}
		//Response to 2NT contract
		if(partner.getLevel()==2 && partner.getStrain()=='T')
		{
			if(levelIsValid(3,curContract) && strainIsValid('C',2,curContract))
			{
				return new Contract('C',3);
			}
		}
		//Response to 3NT contract
		if(partner.getLevel()==3 && partner.getStrain()=='T')
		{
			if(levelIsValid(4,curContract) && strainIsValid('C',4,curContract))
			{
				return new Contract('C',4);
			}
		}
		//If no response can be made
		return new Contract('P',0);
	}
	
	/**
	 * Handles the playing phase of the game
	 * @param hands The hands that are being played
	 * @param contract The contract for the play
	 * @return The score of the attackers
	 */
	public int playingPhase(ArrayList<Hand> hands, Contract contract)
	{
		int turns = declarer;
		int rounds = 1;
		int score = 0;
		ArrayList<Integer> positions = new ArrayList<Integer>();
		ArrayList<Card> tricks = new ArrayList<Card>();
		//Player is dummy
		if(attacker==true && turns==2)
		{
			turns++;
			GameOutput output = new GameOutput();
			//13 Tricks are going to be made
			for(int i = 0; i<13; i++)
			{
				//Making the hand for the current round
				for(int j = 0; j<4; j++)
				{
					int handIndex = turns%4;
					//AI always plays
					Card tCard = AITrick(hands.get(handIndex),contract,tricks);
					hands.get(handIndex).getCards().remove(tCard);
					tricks.add(tCard);
					positions.add(handIndex);
					System.out.println(intToAI(handIndex)+" has placed card: "+output.showCard(tCard));
					turns++;
				}
				Hand tempHand = new Hand(tricks);
				int bestIndex = tempHand.highestCard(contract);
				turns = positions.get(bestIndex);
				boolean made = madeTrick(turns,attacker);
				if(made)
				{
					score++;
				}
				rounds++;
				tricks.clear();
				positions.clear();
				if(rounds<14)
				{
					System.out.println("-----------------------------");
					System.out.println("Trick Complete: Moving on to Trick # "+rounds);
					System.out.println("-----------------------------");
				}
			}
			turns = -1;
		}
		//Player made the contract
		if(attacker==true && turns!=-1)
		{
			turns++;
			GameOutput output = new GameOutput();
			//13 Tricks are going to be made
			for(int i = 0; i<13; i++)
			{
				//Making the hand for the current round
				for(int j = 0; j<4; j++)
				{
					int handIndex = turns%4;
					//Player goes
					if(handIndex==0 || handIndex==2)
					{
						Card tCard = PlayerTrick(hands.get(handIndex),contract,output,tricks);
						hands.get(handIndex).getCards().remove(tCard);
						tricks.add(tCard);
						positions.add(handIndex);
						System.out.println("Player has placed card: "+output.showCard(tCard));
						turns++;
					}
					//AI Goes
					else
					{
						Card tCard = AITrick(hands.get(handIndex),contract,tricks);
						hands.get(handIndex).getCards().remove(tCard);
						tricks.add(tCard);
						positions.add(handIndex);
						System.out.println(intToAI(handIndex)+" has placed card: "+output.showCard(tCard));
						turns++;
					}
				}
				Hand tempHand = new Hand(tricks);
				int bestIndex = tempHand.highestCard(contract);
				turns = positions.get(bestIndex);
				boolean made = madeTrick(turns,attacker);
				if(made)
				{
					score++;
				}
				rounds++;
				positions.clear();
				tricks.clear();
				if(rounds<14)
				{
					System.out.println("-----------------------------");
					System.out.println("Trick Complete: Moving on to Trick # "+rounds);
					System.out.println("-----------------------------");
				}
			}
		}
		//Player is defending
		if(attacker==false)
		{
			turns++;
			GameOutput output = new GameOutput();
			//13 Tricks are going to be made
			for(int i = 0; i<13; i++)
			{
				//Making the hand for the current round
				for(int j = 0; j<4; j++)
				{
					int handIndex = turns%4;
					//Player goes
					if(handIndex==0)
					{
						Card tCard = PlayerTrick(hands.get(handIndex),contract,output,tricks);
						hands.get(handIndex).getCards().remove(tCard);
						tricks.add(tCard);
						positions.add(handIndex);
						System.out.println("Player has placed card: "+output.showCard(tCard));
						turns++;
					}
					//AI Goes
					else
					{
						Card tCard = AITrick(hands.get(handIndex),contract,tricks);
						hands.get(handIndex).getCards().remove(tCard);
						tricks.add(tCard);
						positions.add(handIndex);
						System.out.println(intToAI(handIndex)+" has placed card: "+output.showCard(tCard));
						turns++;
					}
					
				}
				Hand tempHand = new Hand(tricks);
				int bestIndex = tempHand.highestCard(contract);
				turns = positions.get(bestIndex);
				boolean made = madeTrick(turns,attacker);
				if(made)
				{
					score++;
				}
				rounds++;
				tricks.clear();
				positions.clear();
				if(rounds<14)
				{
					System.out.println("-----------------------------");
					System.out.println("Trick Complete: Moving on to Trick # "+rounds);
					System.out.println("-----------------------------");
				}
			}
		}
		return score;
	}
	
	/**
	 * Handles the AI when making tricks.
	 * @param hand The AI's hand
	 * @param contract The contract
	 * @return The card to be used for the trick
	 */
	public Card AITrick(Hand hand, Contract contract, ArrayList<Card> trickHand)
	{
		//Partner has gone
		char trump = contract.getStrain();
		Card tempCard;
		Hand tempHand = new Hand(trickHand);
		int[] comp = hand.getCardComp();
		int people = trickHand.size();
		if(people>=2)
		{
			char curSuit = tempHand.getCards().get(0).getSuit();
			int curSuitIndex = tempHand.getCards().get(0).getCompIndex();
			int winningIndex = tempHand.highestCard(contract);
			int winningValue = tempHand.getCards().get(winningIndex).getValue();
			//If your partner lost
			if(people==2 && winningIndex==1 || people==3 && winningIndex==2)
			{
				//If you have the current suit
				if(comp[curSuitIndex]>0)
				{
					int yourMaxIndex = hand.highestInSuit(curSuit);
					int yourMaxValue = hand.getCards().get(yourMaxIndex).getValue();
					//If you can beat their card
					if(winningValue<yourMaxValue)
					{
						return hand.getCards().get(yourMaxIndex);
					}
					//If you cannot beat their card
					{
						int yourMinIndex = hand.lowestInSuit(curSuit);
						return hand.getCards().get(yourMinIndex);
					}
				}
				//If you don't have the current suit
				else
				{
					tempCard = new Card(trump,0);
					int trumpIndex = tempCard.getCompIndex();
					//You have a trump card
					if(comp[trumpIndex]>0)
					{
						int trumpMaxIndex = hand.highestInSuit(trump);
						return hand.getCards().get(trumpMaxIndex);
					}
					//You don't have the trump card
					else
					{
						int lowestCardIndex = hand.lowestCard(hand.getCards());
						return hand.getCards().get(lowestCardIndex);
					}
				}
			}
			//If your partner is winning
			else
			{
				curSuit = tempHand.getCards().get(0).getSuit();
				curSuitIndex = tempHand.getCards().get(0).getCompIndex();
				//If you have the suit
				if(comp[curSuitIndex]>0)
				{
					int lowestSuitIndex = hand.lowestInSuit(curSuit);
					return hand.getCards().get(lowestSuitIndex);
				}
				//If you don't have the suit
				else
				{
					int lowestCardIndex = hand.lowestCard(hand.getCards());
					return hand.getCards().get(lowestCardIndex);
				}
			}
		}
		//Partner hasn't gone
		else
		{
			//If you're first
			if(trickHand.isEmpty())
			{
				return hand.getCards().get(0);
			}
			//If you're not first
			else
			{
				char curSuit = tempHand.getCards().get(0).getSuit();
				int curSuitIndex = tempHand.getCards().get(0).getCompIndex();
				int winningIndex = tempHand.highestCard(contract);
				int winningValue = tempHand.getCards().get(winningIndex).getValue();
				//If you have the suit
				if(comp[curSuitIndex]>0)
				{
					int yourMaxIndex = hand.highestInSuit(curSuit);
					int yourMaxValue = hand.getCards().get(yourMaxIndex).getValue();
					//If you can beat their card
					if(winningValue<yourMaxValue)
					{
						return hand.getCards().get(yourMaxIndex);
					}
					//If you cannot beat their card
					{
						int yourMinIndex = hand.lowestInSuit(curSuit);
						return hand.getCards().get(yourMinIndex);
					}
				}
				//If you don't have the suit
				else
				{
					tempCard = new Card(trump,0);
					int trumpIndex = tempCard.getCompIndex();
					//You have a trump card
					if(comp[trumpIndex]>0)
					{
						int trumpMaxIndex = hand.highestInSuit(trump);
						return hand.getCards().get(trumpMaxIndex);
					}
					//You don't have the trump card
					else
					{
						int lowestCardIndex = hand.lowestCard(hand.getCards());
						return hand.getCards().get(lowestCardIndex);
					}
				}
			}
		}
	}
	
	/**
	 * Handles the player when making tricks.
	 * @param hand The hand of the player
	 * @param contract The contract
	 * @param output The GameOutput object for game output
	 * @param trickHand ArrayList of cards for the hand (in a given trick)
	 * @return The card to be used for the trick
	 */
	public Card PlayerTrick(Hand hand, Contract contract, GameOutput output, ArrayList<Card> trickHand)
	{
		boolean hasSuit = false;
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("Please choose a card: ");
		System.out.println(output.showHand(hand)[0]);
		System.out.println(output.showHand(hand)[1]);
		if(!trickHand.isEmpty())
		{
			hasSuit = hand.hasSuit(trickHand.get(0).getSuit());	
		}
		int selection = -1;
		int attempts = 0;
		boolean startSuitMismatch = true;
		boolean dontCall;
		//Continue to ask for inputs as long as they have the starting suit and don't select a card in that suit,
		// or if they don't have the suit but make an invalid selection.
		while(hasSuit && startSuitMismatch || hasSuit!=true && selection==-1)
		{
			try
			{
				//If they called the correct data type, but an invalid selection [Display purposes only].
				if(attempts!=-1)
				{
					System.out.println("Please select an appropriate card: ");
				}
				dontCall = false;
				attempts = 0;
				selection = sc.nextInt();
			}
			catch(InputMismatchException e)
			{
				System.out.println("Please select an appropriate card: ");
				sc.nextLine();
				dontCall = true;
				attempts = -1;
			}
			//Only call startSuitMismatch if they can compare with an initial card.
			if(!trickHand.isEmpty() && dontCall==false && selection>=0 && selection<hand.getCards().size())
			{
				startSuitMismatch = hand.getCards().get(selection).getSuit()!=trickHand.get(0).getSuit();
			}
			//If there is no starting card, a card selected cannot be mismatched.
			if(trickHand.isEmpty())
			{
				startSuitMismatch = false;
			}
		}
		return hand.getCards().get(selection);
	}
	
	/**
	 * Method for determining which card/partners made the trick
	 * @param bestIndexPosition Player/AI position who made the trick
	 * @param attacker Whether or not the player is attacking
	 * @return If the attacker made a trick.
	 */
	public boolean madeTrick(int bestIP, boolean attacker)
	{
		boolean made = false;
		if(attacker==true && bestIP==0 || attacker==true && bestIP==2 ||
				attacker==false && bestIP==1 || attacker==false && bestIP==3)
		{
			made = true;
		}
		return made;
	}
	
	/**
	 * Converts the AI position to name.
	 * @param i The AI position
	 * @return AI name
	 */
	public String intToAI(int i)
	{
		if(i==0)
		{
			return "North";
		}
		if(i==1)
		{
			return "East";
		}
		if(i==2)
		{
			return "South";
		}
		if(i==3)
		{
			return "West";
		}
		return null;
	}
}
