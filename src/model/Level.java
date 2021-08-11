package model;

import java.io.Serializable;
import java.util.Random;

/**
 * Level is an enumeration of all difficulty levels available for players controlled by artificial intelligence.
 * <p>
 * Level implements the Serializable-interface to allow the current game of Port Royal to be stored by serialising the
 * GameLog-object that represents it.
 *
 * @author Thomas Alexander Hövelmann
 *
 * @see AIPlayer
 * @see GameLog
 */
public enum Level implements Serializable {
	/**
	 * AIs at this level are not challenging and are good for learning the basic mechanics of the game without having to
	 * pay attention to strategy.
	 */
	EASY,

	/**
	 * AIs at this level are decent players and provide a beatable challenge for anyone.
	 */
	MEDIUM,

	/**
	 * AI's at this level are designed to be the best possible players and therefore the most challenging possible.
	 */
	HARD;

	/**
	 * Returns a randomly chosen name for an AI depending on this Level.
	 *
	 * @return A String-representation of a randomly chosen name for an AI depending on this Level.
	 */
	public String getAIPlayerName() {
		switch(this) {
			case EASY : return "Noobie";
			case MEDIUM : return "Normal";
			//case HARD : return "WhySoEZ";
			default: return "WhySoEZ"; //this was added to get 100% coverage from this class, because we can never reach the default section if all levels are covered
			}
		}
	}
