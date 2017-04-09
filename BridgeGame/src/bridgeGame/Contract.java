package bridgeGame;

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
