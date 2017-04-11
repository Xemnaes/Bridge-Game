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
	private Suits suit;
	//Initialize value of the card (Jack = 11, Queen = 12, King = 13, Ace = 14)
	private int value;
	
	/**
	 * Creates the Card object, with it's suit and value.
	 * @param s The card suit
	 * @param v The card value
	 */
	public Card(Suits s, int v) 
	{
		suit = s;
		value = v;
	}
	
	/**
	 * Returns the suit of the card.
	 * @return The card's suit
	 */
	public Suits getSuit()
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
	
	public int getSortVal()
	{
		switch(suit)
		{
		case SPADES:
			return 30;
		case HEARTS:
			return 20;
		case CLUBS:
			return 10;
		case DIAMONDS:
			return 0;
		case NOTRUMP:
			return -1;
		case PASS:
			return -1;
		}
		return 0;
	}
	
	public int getRankVal()
	{
		switch(suit)
		{
		case SPADES:
			return 3;
		case HEARTS:
			return 2;
		case CLUBS:
			return 0;
		case DIAMONDS:
			return 1;
		case NOTRUMP:
			return -1;
		case PASS:
			return -1;
		}
		return 0;
	}
	
	public char getCharVal()
	{
		switch(suit)
		{
		case SPADES:
			return 'S';
		case HEARTS:
			return 'H';
		case CLUBS:
			return 'C';
		case DIAMONDS:
			return 'D';
		case NOTRUMP:
			return 'T';
		case PASS:
			return 'P';
		}
		return 0;
	}
}