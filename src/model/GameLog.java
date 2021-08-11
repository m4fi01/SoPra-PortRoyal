package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * GameLog is a data container that implements a history of game state represented by Game-objects. Since an instance of
 * GameLog contains all the necessary information about the game states, it can serve as a suitable representation of a
 * single game of Port Royal. To provide a basis for implementing undo and redo functionality at controller level,
 * get-methods for the previous, current and next game state are provided, as well as the possibility to add new game
 * states to this history while ensuring the consistency of the history.
 * <p>
 * In addition, GameLog and all classes used in it implements the Serializable-interface, which allows the current game
 * of Port Royal to be stored by serialising the GameLog-object that represents it.
 *
 * @author Thomas Alexander Hövelmann
 *
 * @see Game
 */
public class GameLog implements Serializable {
	/**
	 * Index pointer of the current game state.
	 */
	private int logPointer;

	/**
	 * To save ids of player who shouldnt be in highscore list
	 */
    private ArrayList<Integer> unworthyPlayerIDs = new ArrayList<>();

	/**
	 * Chronological list of all game states of the current game of Port Royal. The game state at index 0 marks the
	 * beginning of the current game.
	 */
	private final ArrayList<Game> log;

	/**
	 * Constructs a new and empty game state history.
	 */
	public GameLog() {
		this.logPointer = -1;
		this.log = new ArrayList<>();
	}

	/**
	 * Adds the specified game state to this game state history directly after the current game state. Afterwards, all
	 * game states after the newly added one are removed from the history as they are now obsolete. The newly added game
	 * state is now considered the current one (More formally: The index pointer is set to the index of the newly added
	 * game state in this game state history).
	 *
	 * @param game Object representing the current game state.
	 * @throws NullPointerException if the specified game state is {@code null}.
	 */
	public void add(Game game) {
		if(game == null) {
			throw new NullPointerException();
		}
		this.log.add(++this.logPointer, game);
		if(this.log.size() > this.logPointer + 1) {
			this.log.subList(this.logPointer + 1, this.log.size()).clear();
		}
	}

	/**
	 * Returns the game state chronologically one state before the current game state if it exists. The returned game
	 * state is now considered the current one (More formally: The index pointer is set to the index of the returned
	 * game state in this game state history).
	 *
	 * @return A Game-object that represents the game state chronologically one state before the current game state at
	 *         the time this method is called or {@code null} if no such game state exists.
	 */
	public Game getPrevious() {
		if(this.hasPrevious()) {
			return this.log.get(--this.logPointer);
		}
		return null;
	}

	/**
	 * add an player ID to unworthy list
	 * @param playerID the ID of player who are not suitable for highscore list
	 */
	public void addToUnworthyList(int playerID){
		if(!unworthyPlayerIDs.contains(playerID)){
			unworthyPlayerIDs.add(playerID);
		}
	}

	/**
	 * Returns the current game state and updates highscoreworthy attribute of players if it exists.
	 * More formally: Returns the game state at the index in this game state history that the index pointer points to.
	 * @return A Game-object that represents the current game state or {@code null} if no such game state exists.
	 */
	public Game get() {
		if(!this.isEmpty()) {
			Game game = this.log.get(this.logPointer);
			return game;
		}
		return null;
	}

	/**
	 * Returns a list of the IDs of all players who are high score unworthy due to their actions during the game.
	 *
	 * @return A list of the IDs of all players who are high score unworthy due to their actions during the game.
	 */
	public ArrayList<Integer> getUnworthyPlayerIDs() {
		return unworthyPlayerIDs;
	}

	/**
	 * Returns the game state chronologically one state after the current game state if it exists. The returned game
	 * state is now considered the current one (More formally: The index pointer is set to the index of the returned
	 * game state in this game state history).
	 *
	 * @return A Game-object that represents the game state chronologically one state after the current game state at
	 *         the time this method is called or {@code null} if no such game state exists.
	 */
	public Game getNext() {
		if(this.hasNext()) {
			return this.log.get(++this.logPointer);
		}
		return null;
	}

	/**
	 * Returns whether this game state history contains a game state that is chronologically one state before the
	 * current game state.
	 *
	 * @return {@code true} if, and only if, a call of {@link #getPrevious()} currently would not return {@code null}.
	 */
	public boolean hasPrevious() {
		return (this.logPointer - 1) >= 0;
	}

	/**
	 * Returns whether this game state history contains a game state that is chronologically one state after the
	 * current game state.
	 *
	 * @return {@code true} if, and only if, a call of {@link #getNext()} currently would not return {@code null}.
	 */
	public boolean hasNext() {
		return (this.logPointer + 1) < this.log.size();
	}

	/**
	 * Returns weather this game state history contains no game states.
	 *
	 * @return {@code true} if, and only if, this game state history contains no game states.
	 */
	public boolean isEmpty() {
		return log.isEmpty();
	}

	/**
	 * Returns the current position of the log pointer.
	 * @return The current position of the log pointer.
	 */
	public int getIndex() {
		return this.logPointer;
	}

	/**
	 * Sets the log pointer position and therefore the current game state.
	 *
	 * @param index The new log pointer position.
	 * @throws IndexOutOfBoundsException if the specified index is smaller than 0 or larger than the log size.
	 */
	public void jump(int index) {
		if(index < 0 | index >= this.log.size()) {
			throw new IndexOutOfBoundsException();
		}
		this.logPointer = index;
	}
}
