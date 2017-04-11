package bridgeGame;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * PlayBridge
 * 
 * This class handles the operation of the java application. It retrieves data from
 * other classes using instances of their objects, and acts accordingly.
 * 
 * @author Alexander Schulz
 * @verion 04.09.2017
 */
public class PlayBridge {

	/**
	 * Main method which allows the user to play games of bridge.
	 * 
	 * @param Empty arguement
	 */
	public static void main(String args[])
	{
		char option = 'Y';
		while(option=='Y')
		{
			Deck deck = new Deck();
			deck.shuffle();
			Game game = new Game(deck);
			ArrayList<Hand> hands = game.deal();
			Contract contract = game.biddingPhase(hands);
			System.out.println("-----------------------------");
			System.out.println("The bidding phase has ended.");
			System.out.println("-----------------------------");
			System.out.println(contractOutput(contract));
			if(contract.getStrain()!=Suits.PASS && contract.getLevel()!=0)
			{
				int score = game.playingPhase(hands, contract);
				System.out.println("-----------------------------");
				score = score-contract.getLevel()-6;
				System.out.println("The score for the attacker was : " + score);
				System.out.println("-----------------------------");
			}
			System.out.println("Would you like to play another game? [Y/N]");
			Scanner sc2 = new Scanner(System.in);
			boolean wrongInput = false;
			option = 'Z';
			while(option!='N' && option!='Y')
			{
				try
				{
					if(wrongInput)
					{
						System.out.println("Please enter a valid selection: ");
					}
					wrongInput = false;
					option = sc2.nextLine().toUpperCase().charAt(0);
				}
				catch (InputMismatchException ime)
				{
					System.out.println("Please enter a valid selection: ");
					sc2.nextLine();
				}
				if(option!='N' && option!='Y')
				{
					wrongInput = true;
				}
			}
			if(option=='Y')
			{
				System.out.println("-----------------------------");
			}
			if(option=='N')
			{
				System.out.println("Thanks for playing!");
			}
		}
	}
	
	/**
	 * Displays the given hand.
	 * 
	 * @param hand Given hand.
	 * @return String equivalent of the hand.
	 */
	public static String[] showHand(Hand hand)
	{
		hand.sortHand();
		StringBuilder sb = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		int previousLocation = 0;
		char tempChar = 0;
		for(Card tempCard: hand.getCards())
		{
			Suits suit = tempCard.getSuit();
			switch(suit)
			{
				case CLUBS:
					sb.append('\u2663');
					tempChar = '\u2663';
					break;
				case DIAMONDS:
					sb.append('\u2662');
					tempChar = '\u2662';
					break;
				case HEARTS:
					sb.append('\u2661');
					tempChar = '\u2661';
					break;
				case SPADES:
					sb.append('\u2660');
					tempChar = '\u2660';
					break;
				case NOTRUMP:
					break;
				case PASS:
					break;
			}
			int HCP = tempCard.getHCP();
			if(HCP>0)
			{
				switch(HCP)
				{
				case 1:
					sb.append("Jack");
					break;
				case 2:
					sb.append("Queen");
					break;
				case 3:
					sb.append("King");
					break;
				case 4:
					sb.append("Ace");
				}
			}
			else
			{
				sb.append(tempCard.getValue());
			}
			sb.append("  ");
			int tempIndex = sb.indexOf(Character.toString(tempChar), previousLocation);
			for(int j = 0; j<tempIndex; j++)
			{
				sb2.append(" ");
			}
			sb2.insert(tempIndex, hand.getCards().indexOf(tempCard));
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
	 * 
	 * @param card The given card
	 * @return System output of the card
	 */
	public static String showCard(Card card)
	{
		StringBuilder sb = new StringBuilder();
		String value = Integer.toString(card.getValue());
		int HCP = card.getHCP();
		Suits suit = card.getSuit();
		switch(suit)
		{
			case CLUBS:
				sb.append('\u2663');
				break;
			case DIAMONDS:
				sb.append('\u2662');
				break;
			case HEARTS:
				sb.append('\u2661');
				break;
			case SPADES:
				sb.append('\u2660');
				break;
			case NOTRUMP:
				break;
			case PASS:
				break;
		}
		if(card.getHCP()>0)
		{
			switch(HCP)
			{
				case 1:
					sb.append("Jack");
					break;
				case 2:
					sb.append("Queen");
					break;
				case 3:
					sb.append("King");
					break;
				case 4:
					sb.append("Ace");
					break;
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
	 * 
	 * @param contract The given contract
	 * @return The system output for the contract
	 */
	public static String contractOutput(Contract contract)
	{
		Suits strain = contract.getStrain();
		int level = contract.getLevel();
		StringBuilder sb = new StringBuilder();
		sb.append("The contract is ");
		sb.append(level+" ");
		switch(strain)
		{
			case CLUBS:
				sb.append('\u2663');
				break;
			case DIAMONDS:
				sb.append('\u2662');
				break;
			case HEARTS:
				sb.append('\u2661');
				break;
			case SPADES:
				sb.append('\u2660');
				break;
			case NOTRUMP:
				sb.append("NT");
				break;
			case PASS:
				sb.append("P");
				break;
		}
		return sb.toString();
	}
}
