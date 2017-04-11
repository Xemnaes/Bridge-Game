package bridgeGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
/**
 * Game
 * 
 * This objects performs the basic functions of a contract
 * bridge game. It handles the bidding and playing phases,
 * as well as the rules and AI.
 * 
 * @author Alexander Schulz
 * @version 04.09.2017
 *
 */
public class Game {
	//Initializes the deck object used in the game.
	private Deck deck;
	//Initilizes the list of bidding characters.
	private ArrayList<Suits> suitList = new ArrayList<Suits>(Arrays.asList(Suits.CLUBS,Suits.DIAMONDS,Suits.HEARTS,Suits.SPADES,Suits.NOTRUMP,Suits.PASS));
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
		Contract currentContract = new Contract(Suits.PASS,0);
		ArrayList<Contract> contracts = new ArrayList<Contract>();
		while(pass<3 || pass==3 && turns==3)
		{
			if(turns%4==0)
			{
				Contract contract = ContractPlayer(hands.get(turns%4), contracts, currentContract);
				if(contract.getCharVal()=='P' || contract.getLevel()==0)
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
				System.out.println("Player has bid: "+contract.getLevel()+" "+contract.getCharVal());
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
				if(contract.getCharVal()=='P' || contract.getLevel()==0)
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
				System.out.println(name+ " has bid: "+contract.getLevel()+" "+contract.getCharVal());
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
	
	/**
	 * Retrieves the position of the declarer of the bid.
	 * 
	 * @param contracts ArrayList of contract objects made
	 * @param curContract The current contract
	 * @return The position of the declarer
	 */
	public int getDeclarer(ArrayList<Contract> contracts, Contract curContract)
	{
		for(Contract tempContract: contracts)
		{
			if(tempContract.getStrain()==curContract.getStrain())
			{
				return contracts.indexOf(tempContract);
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
			return new Contract(Suits.PASS,0);
		}
		//If the AI is making their first bid
		if(partner==null || contracts.size()<4 && partner.getLevel()==0 && partner.getCharVal()=='P')
		{
			// If HCP are between 5 and 11 points and C&D have 3 cards, return a 1C contract.
			if(HCP>=5 && HCP<=11 && clubs==3 && diamonds==3 && levelIsValid(1,curContract) && strainIsValid(Suits.CLUBS,1,curContract))
			{
				return new Contract(Suits.CLUBS,1);
			}
			// If HCP are between 5 and 11 points and C&D have 4 cards, return a 1D contract.
			if(HCP>=5 && HCP<=11 && clubs==4 && diamonds==4 && levelIsValid(1,curContract) && strainIsValid(Suits.DIAMONDS,1,curContract))
			{
				return new Contract(Suits.DIAMONDS,1);
			}
			// If HCP is greater than 12 points and S<H and H has 5 or 6 cards, return a 1H contract.
			if(HCP>=13 && hearts==5 && hearts>spades && levelIsValid(1,curContract) && strainIsValid(Suits.HEARTS,1,curContract) ||
			   HCP>=13 && hearts==6 && hearts>spades && levelIsValid(1,curContract) && strainIsValid(Suits.HEARTS,1,curContract))
			{
				return new Contract(Suits.HEARTS,1);
			}
			// If HCP is greater than 12 points and S has 5 or 6 cards, return a 1S contract.
			if(HCP>=13 && spades==5 && levelIsValid(1,curContract) && strainIsValid(Suits.SPADES,1,curContract) ||
			   HCP>=13 && spades==6 && levelIsValid(1,curContract) && strainIsValid(Suits.SPADES,1,curContract))
			{
				return new Contract(Suits.SPADES,1);
			}
			// If HCP are between 15 and 17 points and contract can be played, return a 1NT contract.
			if(HCP>=15 && HCP<=17 && levelIsValid(1,curContract) && strainIsValid(Suits.NOTRUMP,1,curContract) &&
			   clubs>1 && diamonds>1 && hearts>1 && spades>1)
			{
				return new Contract(Suits.NOTRUMP,1);
			}
			// If HCP are greater than 22 points, return a 2C contract.
			if(HCP>=22 && levelIsValid(2,curContract) && strainIsValid(Suits.CLUBS,2,curContract))
			{
				return new Contract(Suits.CLUBS,2);
			}
			// If HCP are between 5 and 11 points with 6+ D cards, return a 2D contract.
			if(HCP>=5 && HCP<=11 && levelIsValid(2,curContract) && strainIsValid(Suits.DIAMONDS,2,curContract))
			{
				return new Contract(Suits.DIAMONDS,2);
			}
			// If HCP are between 5 and 11 points with 6+ H cards, return a 2H contract.
			if(HCP>=5 && HCP<=11 && levelIsValid(2,curContract) && strainIsValid(Suits.HEARTS,2,curContract))
			{
				return new Contract(Suits.HEARTS,2);
			}
			// If HCP are between 5 and 11 points with 6+ S cards, return a 2S contract.
			if(HCP>=5 && HCP<=11 && levelIsValid(2,curContract) && strainIsValid(Suits.SPADES,2,curContract))
			{
				return new Contract(Suits.SPADES,2);
			}
			// If HCP are between 20 and 21 points and contract can be played, return a 2NT contract.
			if(HCP>=20 && HCP<=21 && levelIsValid(2,curContract) && strainIsValid(Suits.NOTRUMP,2,curContract) &&
			   clubs>1 && diamonds>1 && hearts>1 && spades>1)
			{
				return new Contract(Suits.NOTRUMP,2);
			}
		}
		//AI responses to partner's bid.
		else
		{
			Contract contract = ContractAIResponse(hand,handMakeup,HCP,partner,curContract);
			return contract;
		}
		//In case of overshoot, pass automatically.
		return new Contract(Suits.PASS,0);
	}
	
	/**
	 * This method handles the player during the bidding phase. Using the given hand and
	 * previous contracts, the player should choose the best contract for their turn.
	 * 
	 * @param hand The given hand of the player
	 * @param contracts List of previous contracts 
	 * @param curContract The current contract to be played
	 * @return The player's contract
	 */
	public Contract ContractPlayer(Hand hand,ArrayList<Contract> contracts, Contract curContract)
	{
		Scanner sc = new Scanner(System.in);
		if(contracts.isEmpty())
		{
			String[] display = PlayBridge.showHand(hand);
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
		valid = false;
		attempts = 0;
		Suits strain = null;
		char testStrain = 0;
		System.out.println("Please enter your bid strain: ");
		while(valid==false || strain==null)
		{
			try
			{
				if(attempts>0)
				{
					System.out.println("Please enter a valid strain: ");
				}
				testStrain = sc.next(".").toUpperCase().charAt(0);
			}
			catch (InputMismatchException ime)
			{
				System.out.println("Please enter a valid strain: ");
				sc.nextLine();
				attempts=-1;
			}
			switch(testStrain)
			{
				case 'C':
					strain = Suits.CLUBS;
					break;
				case 'D':
					strain = Suits.DIAMONDS;
					break;
				case 'H':
					strain = Suits.HEARTS;
					break;
				case 'S':
					strain = Suits.SPADES;
					break;
				case 'T':
					strain = Suits.NOTRUMP;
					break;
				case 'P':
					strain = Suits.PASS;
					break;
			}
			if(attempts!=-1)
			{
				attempts++;
				valid = strainIsValid(strain, level, curContract);
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
	private boolean strainIsValid(Suits strain, int level, Contract curContract) {
		Contract tempContract = new Contract(strain,level);
		char curStrain = curContract.getCharVal();
		int curLevel = curContract.getLevel();
		boolean valST = suitList.contains(strain);
		if(tempContract.getCharVal()>curStrain && valST|| tempContract.getCharVal()<=curStrain && level>curLevel && valST || tempContract.getCharVal()=='P')
		{
			return true;
		}
		return false;
	}

	/**
	 * Determines whether or not the level for a newly made contract is valid.
	 * 
	 * @param l Given level from the contract.
	 * @param curContract The current contract
	 * @return True if level is valid; false otherwise.
	 */
	public boolean levelIsValid(int l, Contract curContract)
	{
		int curLevel = curContract.getLevel();
		boolean valL = l>=1 && l<=7;
		if(curLevel<l && valL || curLevel==l && curContract.getCharVal()!='T' && valL || l==0)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Handles the AI's response to a partner's contract.
	 * 
	 * @param hand The current hand
	 * @param handMakeup The current hand suite composition
	 * @param HCP The hand's HCP value
	 * @param partner The partner's last contract
	 * @param curContract The current contract
	 * @return The contract decided upon by the AI
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
		if(partner.getLevel()==1 && partner.getCharVal()=='C')
		{
			if(HCP>=13 && HCP<=15 && levelIsValid(2,curContract) && strainIsValid(Suits.NOTRUMP,2,curContract))
			{
				return new Contract(Suits.NOTRUMP,2);
			}
			if(HCP>=16 && HCP<=17 && levelIsValid(3,curContract) && strainIsValid(Suits.NOTRUMP,3,curContract))
			{
				return new Contract(Suits.NOTRUMP,3);
			}
		}
		//Response to 1D contract
		if(partner.getLevel()==1 && partner.getCharVal()=='D')
		{
			if(HCP>=13 && HCP<=15 && levelIsValid(2,curContract) && strainIsValid(Suits.NOTRUMP,2,curContract))
			{
				return new Contract(Suits.NOTRUMP,2);
			}
			if(HCP>=16 && HCP<=17 && levelIsValid(3,curContract) && strainIsValid(Suits.NOTRUMP,3,curContract))
			{
				return new Contract(Suits.NOTRUMP,3);
			}
		}
		//Response to 1H contract
		if(partner.getLevel()==1 && partner.getCharVal()=='H' || partner.getLevel()==1 && partner.getCharVal()=='S')
		{
			if(HCP>=6 && spades>=4 && hearts==0 && levelIsValid(1,curContract) && strainIsValid(Suits.SPADES,1,curContract))
			{
				return new Contract(Suits.SPADES,1);
			}
			if(HCP>=6 && HCP<=9 && spades!=4 && hearts!=3 && levelIsValid(1,curContract) && strainIsValid(Suits.NOTRUMP,1,curContract))
			{
				return new Contract(Suits.NOTRUMP,1);
			}
			if(HCP>=10 && diamonds>=4 && levelIsValid(1,curContract) && strainIsValid(Suits.DIAMONDS,1,curContract))
			{
				return new Contract(Suits.DIAMONDS,1);
			}
			if(HCP>=10 && clubs>=4 && levelIsValid(1,curContract) && strainIsValid(Suits.CLUBS,1,curContract))
			{
				return new Contract(Suits.CLUBS,1);
			}
			if(HCP>=13 && levelIsValid(2,curContract) && strainIsValid(Suits.NOTRUMP,2,curContract))
			{
				return new Contract(Suits.NOTRUMP,2);
			}
			if(HCP>=10 && HCP<=12 && hearts>=3 && levelIsValid(3,curContract) && strainIsValid(Suits.HEARTS,3,curContract))
			{
				return new Contract(Suits.HEARTS,3);
			}
			if(HCP>=15 && HCP<=17 && balanced && levelIsValid(3,curContract) && strainIsValid(Suits.NOTRUMP,3,curContract))
			{
				return new Contract(Suits.NOTRUMP,3);
			}
			if(HCP<10 && hearts>=5 && levelIsValid(4,curContract) && strainIsValid(Suits.HEARTS,4,curContract))
			{
				return new Contract(Suits.HEARTS,4);
			}
		}
		//Response to 1NT contract
		if(partner.getLevel()==1 && partner.getStrain()==Suits.NOTRUMP)
		{
			if(HCP>=8 && levelIsValid(2,curContract) && strainIsValid(Suits.CLUBS,2,curContract))
			{
				return new Contract(Suits.CLUBS,2);
			}
		}
		//Response to 2C contract
		if(partner.getLevel()==2 && partner.getStrain()==Suits.CLUBS)
		{
			if(HCP>=8 && spades>=5 && levelIsValid(2,curContract) && strainIsValid(Suits.SPADES,2,curContract))
			{
				return new Contract(Suits.SPADES,2);
			}
			if(HCP>=8 && hearts>=5 && levelIsValid(2,curContract) && strainIsValid(Suits.HEARTS,2,curContract))
			{
				return new Contract(Suits.HEARTS,2);
			}
			if(HCP>=8 && diamonds>=5 && levelIsValid(3,curContract) && strainIsValid(Suits.DIAMONDS,3,curContract))
			{
				return new Contract(Suits.DIAMONDS,3);
			}
			if(HCP>=8 && clubs>=5 && levelIsValid(3,curContract) && strainIsValid(Suits.CLUBS,3,curContract))
			{
				return new Contract(Suits.CLUBS,3);
			}
			if(HCP==8 && balanced && levelIsValid(2,curContract) && strainIsValid(Suits.NOTRUMP,2,curContract))
			{
				return new Contract(Suits.NOTRUMP,2);
			}
		}
		//Response to 2D,2H,2S contract
		if(partner.getLevel()==2 && partner.getCharVal()=='D' || partner.getLevel()==2 && partner.getCharVal()=='H' ||
		   partner.getLevel()==2 && partner.getCharVal()=='S')
		{
			if(levelIsValid(2,curContract) && strainIsValid(Suits.NOTRUMP,2,curContract))
			{
				return new Contract(Suits.NOTRUMP,2);
			}
		}
		//Response to 2NT contract
		if(partner.getLevel()==2 && partner.getCharVal()=='T')
		{
			if(levelIsValid(3,curContract) && strainIsValid(Suits.CLUBS,2,curContract))
			{
				return new Contract(Suits.CLUBS,3);
			}
		}
		//Response to 3NT contract
		if(partner.getLevel()==3 && partner.getStrain()==Suits.NOTRUMP)
		{
			if(levelIsValid(4,curContract) && strainIsValid(Suits.CLUBS,4,curContract))
			{
				return new Contract(Suits.CLUBS,4);
			}
		}
		//If no response can be made
		return new Contract(Suits.PASS,0);
	}
	
	/**
	 * Handles the playing phase of the game.
	 * 
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
		if(attacker && turns==2)
		{
			turns++;
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
					System.out.println(intToAI(handIndex)+" has placed card: "+PlayBridge.showCard(tCard));
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
		if(attacker && turns!=-1)
		{
			turns++;
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
						Card tCard = PlayerTrick(hands.get(handIndex),contract,tricks);
						hands.get(handIndex).getCards().remove(tCard);
						tricks.add(tCard);
						positions.add(handIndex);
						System.out.println("Player has placed card: "+PlayBridge.showCard(tCard));
						turns++;
					}
					//AI Goes
					else
					{
						Card tCard = AITrick(hands.get(handIndex),contract,tricks);
						hands.get(handIndex).getCards().remove(tCard);
						tricks.add(tCard);
						positions.add(handIndex);
						System.out.println(intToAI(handIndex)+" has placed card: "+PlayBridge.showCard(tCard));
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
						Card tCard = PlayerTrick(hands.get(handIndex),contract,tricks);
						hands.get(handIndex).getCards().remove(tCard);
						tricks.add(tCard);
						positions.add(handIndex);
						System.out.println("Player has placed card: "+PlayBridge.showCard(tCard));
						turns++;
					}
					//AI Goes
					else
					{
						Card tCard = AITrick(hands.get(handIndex),contract,tricks);
						hands.get(handIndex).getCards().remove(tCard);
						tricks.add(tCard);
						positions.add(handIndex);
						System.out.println(intToAI(handIndex)+" has placed card: "+PlayBridge.showCard(tCard));
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
	 * 
	 * @param hand The AI's hand
	 * @param contract The contract
	 * @param trickHand The cards played during a trick
	 * @return The card to be used for the trick
	 */
	public Card AITrick(Hand hand, Contract contract, ArrayList<Card> trickHand)
	{
		//Partner has gone
		Suits trump = contract.getStrain();
		Card tempCard;
		Hand tempHand = new Hand(trickHand);
		int[] comp = hand.getCardComp();
		int people = trickHand.size();
		if(people>=2)
		{
			Suits curSuit = tempHand.getCards().get(0).getSuit();
			int curSuitIndex = tempHand.getCards().get(0).getRankVal();
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
					int trumpIndex = tempCard.getRankVal();
					if(trumpIndex!=-1)
					{
						//You have a trump card
						if(comp[trumpIndex]>0)
						{
							int trumpMaxIndex = hand.highestInSuit(trump);
							return hand.getCards().get(trumpMaxIndex);
						}
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
				curSuitIndex = tempHand.getCards().get(0).getRankVal();
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
				Suits curSuit = tempHand.getCards().get(0).getSuit();
				int curSuitIndex = tempHand.getCards().get(0).getRankVal();
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
					int trumpIndex = tempCard.getRankVal();
					if(trumpIndex!=-1)
					{
						//You have a trump card
						if(comp[trumpIndex]>0)
						{
							int trumpMaxIndex = hand.highestInSuit(trump);
							return hand.getCards().get(trumpMaxIndex);
						}
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
		return tempCard;
	}
	
	/**
	 * Handles the player when making tricks.
	 * 
	 * @param hand The hand of the player
	 * @param contract The contract
	 * @param trickHand ArrayList of cards for the hand (in a given trick)
	 * @return The card to be used for the trick
	 */
	public Card PlayerTrick(Hand hand, Contract contract, ArrayList<Card> trickHand)
	{
		boolean hasSuit = false;
		Scanner sc = new Scanner(System.in);
		System.out.println("Please choose a card: ");
		System.out.println(PlayBridge.showHand(hand)[0]);
		System.out.println(PlayBridge.showHand(hand)[1]);
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
		while(hasSuit && startSuitMismatch || hasSuit!=true && selection<0 || hasSuit!=true && selection>hand.getCards().size()-1)
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
	 * Method for determining which card/partners made the trick.
	 * 
	 * @param bestIP Position of the player that won the trick
	 * @param attacker Whether or not the player is attacking
	 * @return If the attacker made a trick
	 */
	public boolean madeTrick(int bestIP, boolean attacker)
	{
		boolean made = false;
		if(attacker && bestIP==0 || attacker && bestIP==2 ||
				attacker==false && bestIP==1 || attacker==false && bestIP==3)
		{
			made = true;
		}
		return made;
	}
	
	/**
	 * Converts the AI position to name.
	 * 
	 * @param i The AI position
	 * @return AI name
	 */
	public String intToAI(int i)
	{
		switch(i)
		{
			case 0: 
				return "North";
			case 1: 
				return "East";
			case 2: 
				return "South";
			case 3: 
				return "West";
		}
		return null;
	}
}
