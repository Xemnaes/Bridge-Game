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
	private char strain;
	//Initializes level of the contract
	private int level;
	
	/**
	 * Constructor for the contract object. This object has a strain
	 * and level parameter.
	 * @param s Contract's strain
	 * @param l Contract's level
	 */
	public Contract(char s, int l)
	{
		strain = s;
		level = l;
	}

	/**
	 * Obtains the contract's strain.
	 * @return Contract's strain
	 */
	public char getStrain()
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
	
}
