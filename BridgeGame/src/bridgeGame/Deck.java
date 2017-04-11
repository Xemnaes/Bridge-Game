package bridgeGame;

/** 
* Deck
* 
* This class handles the Deck object. The deck is the collection of 52 playing cards to
* be utilized by the main game object. Card objects are loaded into the deck, and are
* dispensed once the game is initilized. Once the game is completed, the deck is reloaded.
*
* @author Alexander Schulz
* @version 04.09.2017
*/
import java.util.ArrayList;
import java.util.Collections;

	
public class Deck
{
	//Initialize the deck of cards
	private ArrayList<Card> cards = new ArrayList<Card>();
	
	/**
	 * Constructs the Deck object.
	 * @param c The list of 52 cards in the game
	 */
	public Deck() 
	{
		for(Suits tempSuit: Suits.values())
		{
			if(tempSuit!=Suits.NOTRUMP && tempSuit!=Suits.PASS)
			{	
				for(int i = 2; i<15; i++)
				{
					cards.add(new Card(tempSuit,i));
				}
			}
		}
	}
	
	/**
	 * Shuffles the deck so that a new game can be played.
	 */
	public void shuffle()
	{
		Collections.shuffle(cards);
	}
	
	/**
	 * Returns the list of cards in the deck.
	 * @return Cards in the deck
	 */
	public ArrayList<Card> getCards()
	{
		return cards;
	}
}