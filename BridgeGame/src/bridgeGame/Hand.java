package bridgeGame;

import java.util.ArrayList;

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
		{
	        ArrayList<Card> tempCards = new ArrayList<Card>(cards);
	        Hand tempHand = new Hand(tempCards);
	        cards.clear();
	        for(Suits tempSuit: Suits.values())
	        {
	            while(tempHand.hasSuit(tempSuit))
	            {
	                int nextMaxIndex = tempHand.highestInSuit(tempSuit);
	                cards.add(tempHand.getCards().get(nextMaxIndex));
	                tempHand.getCards().remove(nextMaxIndex);
	            }
	        }
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
		int[] cardAmounts = new int[4];
		for(Suits tempSuit: Suits.values())
		{
			for(Card tempCard: cards)
			{
				if(tempCard.getSuit()==tempSuit)
				{
					cardAmounts[tempCard.getRankVal()]++;
				}
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
	public int highestInSuit(Suits suit)
	{
		int tempIndex = 0;
		int tempValue = 0;
		for(Card tempCard: cards)
		{
			if(tempCard.getValue()>tempValue && tempCard.getSuit()==suit)
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
	public int lowestInSuit(Suits suit)
	{
		int tempIndex = 0;
		int tempValue = 15;
		for(Card tempCard: cards)
		{
			if(tempCard.getValue()<tempValue && tempCard.getSuit()==suit)
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
			int index = lowestInSuit(tempSuits);
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
	public ArrayList<Card> removeSuit(ArrayList<Card> cd, Suits suit)
	{
		for(Card tempCard: cd)
		{
			if(tempCard.getSuit()==suit)
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
	public boolean hasSuit(Suits suit)
	{
		boolean has = false;
		for(Card tempCard: cards)
		{
			if(tempCard.getSuit()==suit)
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
		Suits strain = contract.getStrain();
		int tempMaxValue = 0;
		int tempMaxIndex = 0;
		ArrayList<Integer> ranking = new ArrayList<Integer>();
		if(contract.getCharVal()=='T')
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