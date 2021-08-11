package controller;

import model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


/**
 * The controller which can generate an initial pile from a given String representation, reorganize a discard pile into
 * the required order given by the initial pile and return it as a new pile as well as discard a given card from the
 * acting players personal display onto the discard pile.
 *
 * @author Leon Krick
 * @author Thomas Alexander Hövelmann
 */
public class PileController {

	public static final int INITIAL_PILE_SIZE = 119;

	private final GameController GAME_CONTROLLER;
	CardController cardController;

	/**
	 * Takes the ArrayList of Card-objects specified by the GameController and builds the DrawPile from it either in
	 * randomised order if the specified ArrayList is {@code null} or in order of the specified ArrayList.
	 *
	 * @param initialPile The ArrayList of Card-objects given by the GameController
	 * @return A randomised DrawPile from the specified ArrayList of Card-objects with no initial pile if
	 *         the specified ArrayList is {@code null} or a DrawPile in order of the specified ArrayList with it as the
	 *         initial pile otherwise.
	 * @throws IllegalArgumentException if the loaded ArrayList of Card-objects or the specified ArrayList of
	 * 									Card-objects has not the expected {@code INITIAL_PILE_SIZE} size.
	 * @throws FileNotFoundException if no file was found at the {@code STANDARD_PILE_PATH}.
	 * @see GameController#newGame(ArrayList, File, int)
	 * @see #INITIAL_PILE_SIZE
	 * @see IOController#STANDARD_PILE_PATH
	 */
	public DrawPile generateInitialPile(ArrayList<Card> initialPile) throws FileNotFoundException {
		if(initialPile == null) {
			ArrayList<String> allCardsAsStrings = GAME_CONTROLLER.getIOController().loadPile(new File(IOController.STANDARD_PILE_PATH));
			ArrayList<Card> allCards = GAME_CONTROLLER.getCardController().generatePile(allCardsAsStrings);
			Collections.shuffle(allCards); // Randomises card order.
			return new DrawPile(null,allCards);
		} else {
			if(initialPile.size() < INITIAL_PILE_SIZE) {
				throw new IllegalArgumentException();
			}
			return new DrawPile(new ArrayList<>(initialPile),new ArrayList<>(initialPile));
		}
	}

	/**
	 * creates a new draw pile by adding the cards from the discard pile in the order given by the initial pile
	 * @param discardPile the cards to be ordered into a new draw pile
	 * @return a new @code{DrawPile} based on the the reorganized given pile
	 */
	public DrawPile reorganisePile(ArrayList<Card> discardPile) {
		Game game = GAME_CONTROLLER.getGame();
		ArrayList<Card> initialPile = GAME_CONTROLLER.getGame().getDrawPile().getInitialPile();
		ArrayList<Card> reorganized = new ArrayList<Card>();
		if(initialPile == null){
			Collections.shuffle(discardPile);
			reorganized = new ArrayList<Card>(discardPile);
			for(Card card : reorganized) {
				game.setFaceUp(card,false);
			}
			discardPile.clear();
			return new DrawPile(null, reorganized);
		}
		for(Card initial : initialPile){//for every card in initial pile
			int index = discardPile.indexOf(initial);
			if(index > -1){//if card is present in discard pile
				Card discarded = discardPile.get(index);//get card from discard pile
				game.setFaceUp(discarded,false);
				reorganized.add(discarded);//add discarded pile to reorganized pile (is in right order because initial pile is traversed linearly)
				discardPile.remove(initial);//remove discarded pile from discardPile since it was just "shuffled into the deck"
			}
		}
		return new DrawPile(initialPile, reorganized);
	}

	/**
	 * removes the given card from the acting players personalDisplay and adds it onto the discardPile
	 * @param card the card to be removed from the acting players personalDisplay and to be added onto the discardPile
	 */
	public void discard(Card card) {
		Game game = GAME_CONTROLLER.getGame();
		Player actingPlayer = game.getActingPlayer();
		actingPlayer.removeCard(card);
		game.getDiscardPile().add(card);
	}


	/**
	 * constructor for PileController that takes the current gameController as parameter
	 * @param gameController the current GameController
	 */
	public PileController(GameController gameController){
		this.GAME_CONTROLLER = gameController;
		this.cardController = gameController.getCardController();
	}
}
