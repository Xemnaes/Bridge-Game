/** 
* This class handles the Hand object. A hand contains 13 card objects.
* A game of bridge allows for (4) hands to be used at one time. Hands
* are displayed when the player is playing their hand.
*
* @author Alexander Schulz
*/
package bridgeGame;

import java.util.ArrayList;
import java.util.Collections;

public class Hand
{
	//Initialize the collection of cards in a hand
	private ArrayList<Card> cards;
	//Initialize boolean to tell if hand is empty
	private boolean empty = false;
	
	/**
	 * Creates the hand for a given player/AI. Contains 13 card objects.
	 * @param c Cards in the hand.
	 */
	public Hand(ArrayList<Card> c) 
	{
		cards = c;
	}
	
	/**
	 * Determines if the player/AI has any cards left to play.
	 * @return If the hand is empty.
	 */
	public boolean isEmpty()
	{
		if(this.cards.size() == 0)
		{
			empty = true;
		}
		return empty;
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
		
		for(int i = 0; i < cards.size(); i++)
		{
			int suitRank = cards.get(i).sortValue();
			rankings.add(suitRank*10+cards.get(i).getValue());
		}
		for(int i = 0; i < cards.size(); i++)
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
	 * @return High Card Point value
	 */
	public int getHCP()
	{
		int HCP = 0;
		for(int i = 0; i<13 ; i++)
		{
			HCP = HCP + cards.get(i).getHCP();
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
		for(int i = 0; i<cards.size(); i++)
		{
			tempSuit = cards.get(i).getSuit();
			if(tempSuit=='C')
			{
				cardAmounts[0]++;
			}
			if(tempSuit=='D')
			{
				cardAmounts[1]++;
			}
			if(tempSuit=='H')
			{
				cardAmounts[2]++;
			}
			if(tempSuit=='S')
			{
				cardAmounts[3]++;
			}
		}
		return cardAmounts;
	}
	
	/**
	 * Returns the highest card in a given suit for the hand.
	 * @param c Given suit
	 * @return Index of highest valued card in a given suit
	 */
	public int highestInSuit(char c)
	{
		int tempIndex = 0;
		int tempValue = 0;
		for(int i = 0; i<cards.size(); i++)
		{
			if(cards.get(i).getValue()>tempValue && cards.get(i).getSuit()==c)
			{
				tempIndex = i;
				tempValue = cards.get(i).getValue();
			}
		}
		return tempIndex;
	}
	
	/**
	 * returns the lowest card in a given suit for the hand.
	 * @param c Given suit
	 * @return Index of lowest valued card in a given suit
	 */
	public int lowestInSuit(char c)
	{
		int tempIndex = 0;
		int tempValue = 15;
		for(int i = 0; i<cards.size(); i++)
		{
			if(cards.get(i).getValue()<tempValue && cards.get(i).getSuit()==c)
			{
				tempIndex = i;
				tempValue = cards.get(i).getValue();
			}
		}
		return tempIndex;
	}
	
	/**
	 * Returns an ArrayList of the lowest cards of the hand 
	 * per each suit.
	 * @return ArrayList<Card> of the lowest cards per suit.
	 */
	public ArrayList<Card> lowestCards()
	{
		ArrayList<Card> low = new ArrayList<Card>();
		ArrayList<Character> suits = new ArrayList<Character>();
		suits.add('C');
		suits.add('D');
		suits.add('H');
		suits.add('S');
		for(int i = 0; i<suits.size(); i++)
		{
			char tempSuit = suits.get(i);
			int index = lowestInSuit(tempSuit);
			low.add(cards.get(index));
		}
		return low;
	}
	
	/**
	 * Finds the lowest card by value in an ArrayList of card objects.
	 * @param cds The ArrayList of card objects
	 * @return Index of the lowest card in the ArrayList of cards
	 */
	public int lowestCard(ArrayList<Card> cds)
	{
		int tempIndex = 0;
		int tempValue = 15;
		for(int i = 0; i<cds.size(); i++)
		{
			if(tempValue>cds.get(i).getValue())
			{
				tempValue = cds.get(i).getValue();
				tempIndex = i;
			}
		}
		return tempIndex;
	}
	
	/** 
	 * Returns an ArrayList of Card objects with a specific
	 * suit removed.
	 * @return Modified ArrayList<Card>
	 */
	public ArrayList<Card> removeSuit(ArrayList<Card> cd, char c)
	{
		for(int i = 0; i<cd.size(); i++)
		{
			if(cd.get(i).getSuit()==c)
			{
				cd.remove(i);
			}
		}
		return cd;
	}
	
	/**
	 * Determines whether or not the hand has the given suit
	 * @return True if the hand has a card with the suit
	 */
	public boolean hasSuit(char c)
	{
		boolean has = false;
		for(int i = 0; i<cards.size(); i++)
		{
			if(cards.get(i).getSuit()==c)
			{
				return true;
			}
		}
		return has;
	}
	
	/**
	 * Returns the index of the best card played during a trick.
	 * This method also resets the declarer (who plays first).
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
			for(int i = 0; i<cards.size(); i++)
			{
				if(cards.get(i).getSuit()==cards.get(0).getSuit())
				{
					ranking.add(cards.get(i).getValue());
				}
				else
				{
					ranking.add(1);
				}
			}
			for(int i = 0; i<ranking.size(); i++)
			{
				if(ranking.get(i)>tempMaxValue)
				{
					tempMaxValue = ranking.get(i);
					tempMaxIndex = i;
				}
			}
			return tempMaxIndex;
		}
		else
		{
			for(int i = 0; i<cards.size(); i++)
			{
				if(cards.get(i).getSuit()==cards.get(0).getSuit() && cards.get(0).getSuit()!=strain)
				{
					ranking.add(cards.get(i).getValue());
				}
				if(cards.get(i).getSuit()==strain)
				{
					ranking.add(cards.get(i).getValue()*20);
				}
				if(cards.get(i).getSuit()!=strain && cards.get(i).getSuit()!=cards.get(0).getSuit())
				{
					ranking.add(1);
				}
			}
			for(int i = 0; i<ranking.size(); i++)
			{
				if(ranking.get(i)>tempMaxValue)
				{
					tempMaxValue = ranking.get(i);
					tempMaxIndex = i;
				}
			}
			return tempMaxIndex;
		}
	}
}