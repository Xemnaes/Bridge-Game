package bridgeGame;

/** 
* Card
* 
* This class handles the Card object. A card contains a suit and a value.
* The suit can be a diamond, club, heart or spade. The value can range from
* 1 to Ace. Cards can be ordered into either a deck or hand.
*
* @author Alexander Schulz
* @version 04.09.2017
*/
public class Card
{
	//Initialize suit of the card
	private char suit;
	//Initialize value of the card (Jack = 11, Queen = 12, King = 13, Ace = 14)
	private int value;
	
	/**
	 * Creates the Card object, with it's suit and value.
	 * @param s The card suit
	 * @param v The card value
	 */
	public Card(char s, int v) 
	{
		suit = s;
		value = v;
	}
	
	/**
	 * Returns the suit of the card.
	 * @return The card's suit
	 */
	public char getSuit()
	{
		return suit;
	}

	/** 
	 * Returns the value of the card.
	 * @return The card's value
	 */
	public int getValue()
	{
		return value;
	}
	
	/**
	 * Returns the High Card Point (HCP) value of the card.
	 * Jack = 1 Point
	 * Queen = 2 Points
	 * King = 3 Points
	 * Ace = 4 Points
	 * @return HCP Value
	 */
	public int getHCP()
	{
		int HCP = this.getValue()-10;
		if(HCP>0)
		{
			return HCP;
		}
		return 0;
	}

	/**
	 * Method to determine how the suits are organized in the hand.
	 * (This is NOT the suit bidding scale rankings).
	 * @return Integer ranking of suit
	 */
	public int sortValue()
	{
		if(this.getSuit()=='S')
		{
			return Suits.SPADES.getSortValue();
		}
		if(this.getSuit()=='H')
		{
			return Suits.HEARTS.getSortValue();
		}
		if(this.getSuit()=='C')
		{
			return Suits.CLUBS.getSortValue();
		}
		if(this.getSuit()=='D')
		{
			return Suits.DIAMONDS.getSortValue();
		}
		return 0;
	}
	
	/**
	 * Retrieves the index this suit is located in
	 * the card composition array.
	 * @param c The given suit
	 * @return The index of the suit in the Hand class method getComp()
	 */
	public int getCompIndex()
	{
		if(this.getSuit()=='C')
		{
			return Suits.CLUBS.getRankValue();
		}
		if(this.getSuit()=='D')
		{
			return Suits.DIAMONDS.getRankValue();
		}
		if(this.getSuit()=='H')
		{
			return Suits.HEARTS.getRankValue();
		}
		if(this.getSuit()=='S')
		{
			return Suits.SPADES.getRankValue();
		}
		return 0;
	}

}