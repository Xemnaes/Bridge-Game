/** 
* This class handles the Deck object. The deck is the collection of 52 playing cards to
* be utilized by the main game object. Card objects are loaded into the deck, and are
* dispensed once the game is initilized. Once the game is completed, the deck is reloaded.
*
* @author Alexander Schulz
*/
package bridgeGame;
	
import java.util.ArrayList;
import java.util.Collections;

	
public class Deck
{
	//Initialize the deck of cards
	private ArrayList<Card> cards;
	//Initialize boolean to tell if deck is empty
	private boolean empty;
	
	/**
	 * Constructs the Deck object.
	 * @param c The list of 52 cards in the game
	 */
	public Deck(ArrayList<Card> c) 
	{
		cards = c;
	}
	
	/**
	 * Returns whether or not the deck is empty. If empty, the hands will be full.
	 * @return If the deck is empty
	 */
	public boolean isEmpty()
	{
		return empty;
	}
	
	/**
	 * Shuffles the deck so that a new game can be played.
	 * @param c The shuffled deck
	 */
	public void shuffle(ArrayList<Card> c)
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