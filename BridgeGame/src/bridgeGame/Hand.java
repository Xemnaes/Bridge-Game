package bridgeGame;

import java.util.ArrayList;
import java.util.Collections;

/** 
* Hand
* 
* This class handles the Hand object. A hand contains 13 card objects.
* A game of bridge allows for (4) hands to be used at one time. Hands
* are displayed when the player is playing their hand.
*
* @author Alexander Schulz
* @version 04.09.2017
*/
public class Hand
{
	//Initialize the collection of cards in a hand
	private ArrayList<Card> cards;
	
	/**
	 * Creates the hand for a given player/AI. Contains 13 card objects.
	 * 
	 * @param c Cards in the hand.
	 */
	public Hand(ArrayList<Card> c) 
	{
		cards = c;
	}
	
	/**
	 * Obtains the cards used in this hand.
	 * @return ArrayList of cards in this hand.
	 */
	public ArrayList<Card> getCards()
	{
		return cards;
	}
	
	/**
	 * Sorts the hand by major/minor suits from highest to lowest.
	 * This is to be used for the user.
	 */
	public void sortHand()
	{
		ArrayList<Integer> rankings = new ArrayList<Integer>();
		ArrayList<Card> tempCards = new ArrayList<Card>();
		
		for(Card tempCard: cards)
		{
			int suitRank = tempCard.sortValue();
			rankings.add(suitRank*10+tempCard.getValue());
		}
		for(Card tempCard: cards)
		{
			int tempNext = Collections.max(rankings);
			int tempIndex = rankings.indexOf(tempNext);
			tempCards.add(cards.get(tempIndex));
			rankings.set(tempIndex, -1);
		}
		for(int i = 0; i < cards.size(); i++)
		{
			cards.set(i, tempCards.get(i));
		}
	}
	
	/**
	 * Obtains the High Card Point value of the hand.
	 * 
	 * @return High Card Point value
	 */
	public int getHCP()
	{
		int HCP = 0;
		for(Card tempCard: cards)
		{
			HCP = HCP + tempCard.getHCP();
		}
		return HCP;
	}
	
	/**
	 * Returns a list of integers corresponding to how many cards of each suit
	 * are in the hand.
	 * 
	 * Index 0: Clubs
	 * Index 1: Diamond
	 * Index 2: Hearts
	 * Index 3: Spades
	 * 
	 * @return ArrayList of integers for each suit in the hand.
	 */
	public int[] getCardComp()
	{
		char tempSuit;
		int[] cardAmounts = new int[4];
		for(Card tempCard: cards)
		{
			tempSuit = tempCard.getSuit();
			if(tempSuit=='C')
			{
				cardAmounts[Suits.CLUBS.getRankValue()]++;
			}
			if(tempSuit=='D')
			{
				cardAmounts[Suits.DIAMONDS.getRankValue()]++;
			}
			if(tempSuit=='H')
			{
				cardAmounts[Suits.HEARTS.getRankValue()]++;
			}
			if(tempSuit=='S')
			{
				cardAmounts[Suits.SPADES.getRankValue()]++;
			}
		}
		return cardAmounts;
	}
	
	/**
	 * Returns the highest card in a given suit for the hand.
	 * 
	 * @param c Given suit
	 * @return Index of highest valued card in a given suit
	 */
	public int highestInSuit(char c)
	{
		int tempIndex = 0;
		int tempValue = 0;
		for(Card tempCard: cards)
		{
			if(tempCard.getValue()>tempValue && tempCard.getSuit()==c)
			{
				tempIndex = cards.indexOf(tempCard);
				tempValue = tempCard.getValue();
			}
		}
		return tempIndex;
	}
	
	/**
	 * Returns the lowest card in a given suit for the hand.
	 * 
	 * @param c Given suit
	 * @return Index of lowest valued card in a given suit
	 */
	public int lowestInSuit(char c)
	{
		int tempIndex = 0;
		int tempValue = 15;
		for(Card tempCard: cards)
		{
			if(tempCard.getValue()<tempValue && tempCard.getSuit()==c)
			{
				tempIndex = cards.indexOf(tempCard);
				tempValue = tempCard.getValue();
			}
		}
		return tempIndex;
	}
	
	/**
	 * Returns an ArrayList of the lowest cards of the hand 
	 * per each suit.
	 * 
	 * @return ArrayList<Card> of the lowest cards per suit.
	 */
	public ArrayList<Card> lowestCards()
	{
		ArrayList<Card> low = new ArrayList<Card>();
		for(Suits tempSuits: Suits.values())
		{
			char tempSuit = tempSuits.getCharValue();
			int index = lowestInSuit(tempSuit);
			low.add(cards.get(index));
		}
		return low;
	}
	
	/**
	 * Finds the lowest card by value in an ArrayList of card objects.
	 * 
	 * @param cds The ArrayList of card objects
	 * @return Index of the lowest card in the ArrayList of cards
	 */
	public int lowestCard(ArrayList<Card> cds)
	{
		int tempIndex = 0;
		int tempValue = 15;
		for(Card tempCard: cds)
		{
			if(tempValue>tempCard.getValue())
			{
				tempValue = tempCard.getValue();
				tempIndex = cds.indexOf(tempCard);
			}
		}
		return tempIndex;
	}
	
	/** 
	 * Returns an ArrayList of Card objects with a specific
	 * suit removed.
	 * 
	 * @return Modified ArrayList<Card>
	 */
	public ArrayList<Card> removeSuit(ArrayList<Card> cd, char c)
	{
		for(Card tempCard: cd)
		{
			if(tempCard.getSuit()==c)
			{
				cd.remove(tempCard);
			}
		}
		return cd;
	}
	
	/**
	 * Determines whether or not the hand has the given suit.
	 * 
	 * @return True if the hand has a card with the suit
	 */
	public boolean hasSuit(char c)
	{
		boolean has = false;
		for(Card tempCard: cards)
		{
			if(tempCard.getSuit()==c)
			{
				return true;
			}
		}
		return has;
	}
	
	/**
	 * Returns the index of the best card played during a trick.
	 * This method also resets the declarer (who plays first).
	 * 
	 * @param cards The cards of the trick
	 * @param contract The contract
	 * @return Index of best card
	 */
	public int highestCard(Contract contract)
	{
		char strain = contract.getStrain();
		int tempMaxValue = 0;
		int tempMaxIndex = 0;
		ArrayList<Integer> ranking = new ArrayList<Integer>();
		if(strain=='T')
		{
			for(Card tempCard: cards)
			{
				if(tempCard.getSuit()==cards.get(0).getSuit())
				{
					ranking.add(tempCard.getValue());
				}
				else
				{
					ranking.add(1);
				}
			}
			for(Integer tempRank: ranking)
			{
				if(tempRank>tempMaxValue)
				{
					tempMaxValue = tempRank;
					tempMaxIndex = ranking.indexOf(tempRank);
				}
			}
			return tempMaxIndex;
		}
		else
		{
			for(Card tempCard: cards)
			{
				if(tempCard.getSuit()==cards.get(0).getSuit() && cards.get(0).getSuit()!=strain)
				{
					ranking.add(tempCard.getValue());
				}
				if(tempCard.getSuit()==strain)
				{
					ranking.add(tempCard.getValue()*20);
				}
				if(tempCard.getSuit()!=strain && tempCard.getSuit()!=cards.get(0).getSuit())
				{
					ranking.add(1);
				}
			}
			for(Integer tempRank: ranking)
			{
				if(tempRank>tempMaxValue)
				{
					tempMaxValue = tempRank;
					tempMaxIndex = ranking.indexOf(tempRank);
				}
			}
			return tempMaxIndex;
		}
	}
}