package bridgeGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PlayBridge {

	/**
	 * Main method which allows the user to play games of bridge. 
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
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(file);
			while(sc.hasNext())
			{
				String[] scString = sc.nextLine().split(" ");
				char suit = scString[0].charAt(0);
				int value = Integer.parseInt(scString[1]);
				Card card = new Card(suit,value);
				cards.add(card);
			}
			Deck deck = new Deck(cards);
			deck.shuffle(cards);
			Game game = new Game(deck);
			ArrayList<Hand> hands = game.deal();
			GameOutput output = new GameOutput();
			Contract contract = game.biddingPhase(hands);
			System.out.println("-----------------------------");
			System.out.println("The bidding phase has ended.");
			System.out.println("-----------------------------");
			System.out.println(output.contractOutput(contract));
			int score = game.playingPhase(hands, contract);
			System.out.println("-----------------------------");
			score = score-contract.getLevel()-6;
			System.out.println("The score for the attacker was : " + score);
			System.out.println("-----------------------------");
			System.out.println("Would you like to play another game? [Y/N]");
			@SuppressWarnings("resource")
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
}
