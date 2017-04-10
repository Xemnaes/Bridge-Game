package bridgeGame;

/**
 * Suits
 * 
 * An enum file type for storing data relevant to each suit.
 * 
 * @author Alexander Schulz
 * @version 04.09.2017
 */
public enum Suits {
	CLUBS(10,0,'C'), 
	DIAMONDS(0,1,'D'), 
	HEARTS(20,2,'H'), 
	SPADES(30,3,'S');

	//Initializes the sorting value for the suit.
	private int sortVal;
	//Initializes the ranking value for the suit.
	private int rankVal;
	//Initializes the character value for the suit.
	private char charVal;
	
	/**
	 * Constructor for the enum types.
	 * 
	 * @param sortVal Sorting value
	 * @param rankVal Ranking value
	 * @param charVal Character value
	 */
	Suits(int sortVal, int rankVal, char charVal)
	{
		this.sortVal = sortVal;
		this.rankVal = rankVal;
		this.charVal = charVal;
	}
	
	/**
	 * Returns the int value of the suit for sorting.
	 * 
	 * @return Int value of suit (sorting)
	 */
	public int getSortValue()
	{
		return sortVal;
	}
	
	/**
	 * Returns the int value of the suit for ranking.
	 * @return Int value of suit (ranking)
	 */
	public int getRankValue()
	{
		return rankVal;
	}
	
	/**
	 * Returns the character value of the suit.
	 * @return Character value of suit
	 */
	public char getCharValue()
	{
		return charVal;
	}
}
