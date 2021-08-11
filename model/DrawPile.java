package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Has functionality to get the top card and remove it in that matter
 * @author Leon Krick
 */
public class DrawPile extends Stack<Card> implements Serializable {

	/**
	 * draw pile of the game
	 */
	private ArrayList<Card> pile;
	/**
	 * initial draw pile of the game
	 */
	private ArrayList<Card> initialPile;
	/**
	 * Points to the sixth expedition in five player games. Used for logging purposes.
	 */
	private Card finalExpedition;

	/**
	 * Constructor that sets the draw pile and the initial pile
	 * @param initialPile ArrayList of {@code Card} objects that is the initial order of the draw pile
	 * @param pile ArrayList of {@code Card} objects that represents the draw pile
	 */
	public DrawPile(ArrayList<Card> initialPile, ArrayList<Card> pile) {
		this.initialPile = initialPile;
		this.pile = pile;
		this.finalExpedition = null;
	}

	/**
	 * Copy constructor for logging. Creates a log friendly deep alike copy of the given draw pile.
	 *
	 * @param drawPile The draw pile to copy for logging.
	 */
	public DrawPile(DrawPile drawPile) {
		if(drawPile.initialPile == null) {
			this.initialPile = null;
		} else {
			this.initialPile = drawPile.initialPile; // A copy is not necessary the initial pile is never changed
		}
		this.pile = new ArrayList<>(drawPile.pile);
		if(drawPile.finalExpedition == null) {
			this.finalExpedition = null;
		} else {
			this.finalExpedition = drawPile.finalExpedition;
		}
	}

	/**
	 * Returns the initial pile
	 * @return ArrayList of {@code Card} objects that were set as the initial Pile
	 */
	public ArrayList<Card> getInitialPile(){
		return initialPile;
	}

	/**
	 * Returns the pile
	 * @return Array List of Card that is the DrawPile
	 */
	public ArrayList<Card> getPile(){
		return pile;
	}

	/**
	 * Sets the final expedition as specified.
	 *
	 * @param finalExpedition The final expedition.
	 */
	public void setFinalExpedition(Card finalExpedition) {
		this.finalExpedition = finalExpedition;
	}

	/**
	 * Returns the final expedition if set or {@code null} otherwise.
	 *
	 * @return The final expedition if set or {@code null} otherwise.
	 */
	public Card getFinalExpedition() {
		return this.finalExpedition;
	}

	/**
	 * Returns the top card of the stack and removes it from there
	 * @return null if stack size is 0, the card on top else
	 */
	@Override
	public Card pop(){
		if(pile.size() == 0){
			return null;
		}
		Card ret = pile.get(0);
		pile.remove(ret);
		return ret;
	}
}
