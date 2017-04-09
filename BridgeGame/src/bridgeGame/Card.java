/** 
* This class handles the Card object. A card contains a suit and a value.
* The suit can be a diamond, club, heart or spade. The value can range from
* 1 to Ace. Cards can be ordered into either a deck or hand.
*
* @author Alexander Schulz
*/
package bridgeGame;

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
			return 30;
		}
		if(this.getSuit()=='H')
		{
			return 20;
		}
		if(this.getSuit()=='C')
		{
			return 10;
		}
		//This means the suit is a diamond.
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
			return 0;
		}
		if(this.getSuit()=='D')
		{
			return 1;
		}
		if(this.getSuit()=='H')
		{
			return 2;
		}
		//This is a spade
		return 3;
	}

}