package bridgeGame;

/** 
* Contract
* 
* This class handles the Contract object. A contract contains
* both a strain and a level. This objects is used during both the
* bidding and playing phases.
*
* @author Alexander Schulz
* @version 04.09.2017
*/
public class Contract {
	
	//Initializes strain of the contract
	private Suits strain;
	//Initializes level of the contract
	private int level;
	
	/**
	 * Constructor for the contract object. This object has a strain
	 * and level parameter.
	 * @param s Contract's strain
	 * @param l Contract's level
	 */
	public Contract(Suits s, int l)
	{
		strain = s;
		level = l;
	}

	/**
	 * Obtains the contract's strain.
	 * @return Contract's strain
	 */
	public Suits getStrain()
	{
		return strain;
	}
	
	/**
	 * Obtains the contract's level.
	 * @return Contract's level
	 */
	public int getLevel()
	{
		return level;
	}
	
	/**
	 * Obtains the suit character value.
	 * @return Suit character
	 */
	public char getCharVal()
	{
		switch(strain)
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
