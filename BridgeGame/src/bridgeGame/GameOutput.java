package bridgeGame;

public class GameOutput {

	/** 
	 * Empty constructor for the GameOutput object.
	 */
	public GameOutput()
	{
		
	}
	
	/**
	 * Constructs a string output for the user to see their current hand.
	 * @param hand User's hand
	 * @return String representation of the hand
	 */
	public String[] showHand(Hand hand)
	{
		hand.sortHand();
		StringBuilder sb = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		int cardAmount = hand.getCards().size();
		int previousLocation = 0;
		char tempChar = 0;
		for(int i = 0; i<cardAmount; i++)
		{
			char suit = hand.getCards().get(i).getSuit();
			if(suit=='C')
			{
				sb.append('\u2663');
				tempChar = '\u2663';
			}
			if(suit=='D')
			{
				sb.append('\u2662');
				tempChar = '\u2662';
			}
			if(suit=='H')
			{
				sb.append('\u2661');
				tempChar = '\u2661';
			}
			if(suit=='S')
			{
				sb.append('\u2660');
				tempChar = '\u2660';
			}
			int HCP = hand.getCards().get(i).getHCP();
			if(HCP>0)
			{
				if(HCP==1)
				{
					sb.append("Jack");
				}
				if(HCP==2)
				{
					sb.append("Queen");
				}
				if(HCP==3)
				{
					sb.append("King");
				}
				if(HCP==4)
				{
					sb.append("Ace");
				}
			}
			else
			{
				sb.append(hand.getCards().get(i).getValue());
			}
			sb.append("  ");
			int tempIndex = sb.indexOf(Character.toString(tempChar), previousLocation);
			for(int j = 0; j<tempIndex; j++)
			{
				sb2.append(" ");
			}
			sb2.insert(tempIndex, i);
			previousLocation = tempIndex+1;
		}
		String handOut = sb.toString();
	    String options = sb2.toString();
		String[] cardOutput = new String[2];
		cardOutput[0] = handOut;
		cardOutput[1] = options;
		return cardOutput;
	}
	
	/**
	 * Displays the users card as output.
	 * @param card The given card
	 * @return System output of the card
	 */
	public String showCard(Card card)
	{
		StringBuilder sb = new StringBuilder();
		String value = Integer.toString(card.getValue());
		int HCP = card.getHCP();
		char suit = card.getSuit();
		if(suit=='C')
		{
			sb.append('\u2663');
		}
		if(suit=='D')
		{
			sb.append('\u2662');
		}
		if(suit=='H')
		{
			sb.append('\u2661');
		}
		if(suit=='S')
		{
			sb.append('\u2660');
		}
		if(card.getHCP()>0)
		{
			if(HCP==1)
			{
				 sb.append("Jack");
			}
			if(HCP==2)
			{
				sb.append("Queen");
			}
			if(HCP==3)
			{
				sb.append("King");
			}
			if(HCP==4)
			{
				sb.append("Ace");
			}
		}
		if(card.getHCP()==0)
		{
			sb.append(value);
		}
		return sb.toString();
	}
	
	/**
	 * Shows the contract as a string.
	 * @param contract The given contract
	 * @return The system output for the contract
	 */
	public String contractOutput(Contract contract)
	{
		char strain = contract.getStrain();
		int level = contract.getLevel();
		StringBuilder sb = new StringBuilder();
		sb.append("The contract is ");
		sb.append(level+" ");
		if(strain=='C')
		{
			sb.append('\u2663');
		}
		if(strain=='D')
		{
			sb.append('\u2662');
		}
		if(strain=='H')
		{
			sb.append('\u2661');
		}
		if(strain=='S')
		{
			sb.append('\u2660');
		}
		if(strain=='T')
		{
			sb.append("NT");
		}
		return sb.toString();
	}
	
}
