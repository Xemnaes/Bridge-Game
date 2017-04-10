package bridgeGame;

import java.io.File;
import java.io.FileNotFoundException;
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
	 * @param args Empty argument.
	 * @throws FileNotFoundException When the deck is not found.
	 */
	public static void main(String args[]) throws FileNotFoundException
	{
		char option = 'Y';
		while(option=='Y')
		{
			ArrayList<Card> cards = new ArrayList<Card>();
			File file = new File("Deck.txt");
			Scanner sc = new Scanner(file);
			while(sc.hasNext())
			{
				String[] scString = sc.nextLine().split(" ");
				char suit = scString[0].charAt(0);
				int value = Integer.parseInt(scString[1]);
				Card card = new Card(suit,value);
				cards.add(card);
			}
			sc.close();
			Deck deck = new Deck(cards);
			deck.shuffle(cards);
			Game game = new Game(deck);
			ArrayList<Hand> hands = game.deal();
			Contract contract = game.biddingPhase(hands);
			System.out.println("-----------------------------");
			System.out.println("The bidding phase has ended.");
			System.out.println("-----------------------------");
			System.out.println(contractOutput(contract));
			int score = game.playingPhase(hands, contract);
			System.out.println("-----------------------------");
			score = score-contract.getLevel()-6;
			System.out.println("The score for the attacker was : " + score);
			System.out.println("-----------------------------");
			System.out.println("Would you like to play another game? [Y/N]");
			Scanner sc2 = new Scanner(System.in);
			boolean wrongInput = false;
			option = 'Z';
			while(option!='N' && option!='Y')
			{
				try
				{
					if(wrongInput==true)
					{
						System.out.println("Please enter a valid selection: ");
					}
					wrongInput = false;
					option = sc2.nextLine().toUpperCase().charAt(0);
				}
				catch (InputMismatchException ime)
				{
					System.out.println("Please enter a valid selection: ");
					sc.nextLine();
				}
				if(option!='N' && option!='Y')
				{
					wrongInput = true;
				}
			}
			sc2.close();
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
	 * 
	 * @param card The given card
	 * @return System output of the card
	 */
	public static String showCard(Card card)
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
	 * 
	 * @param contract The given contract
	 * @return The system output for the contract
	 */
	public static String contractOutput(Contract contract)
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
