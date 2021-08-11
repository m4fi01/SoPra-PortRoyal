package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Player is a data container that contains all the information needed to represent a player during a single game of
 * Port Royal. It supports a unique identification number that allows multiple players with the same name at the same
 * time, and provides methods for managing all the cards a player holds during a match by supporting appropriate add and
 * remove functionality.
 * <p>
 * Player implements the Serializable-interface to allow the current game of Port Royal to be stored by serialising the
 * GameLog-object that represents it.
 *
 * @author Thomas Alexander Hövelmann
 *
 * @see AIPlayer
 * @see Card
 * @see Object
 */
public class Player implements Serializable {
	/**
	 * Specifies the next unique playerID given to the next constructed Player-object.
	 */
	private static int nextPlayerID = 1;

	/**
	 * Unique identification number of this player. Allows the easy comparison of Player-objects and the use of
	 * identical player names for different players.
	 */
	private final int PLAYER_ID;

	/**
	 * Customisable name of this player.
	 */
	private String name;

	/**
	 * Represents the number of victory points this player has.
	 */
	private int victoryPoints;

	/**
	 * Represents the number of spendable anchor symbols this player has, ignoring any jack of all trades cards.
	 */
	private int anchors;

	/**
	 * Represents the number of spendable house symbols this player has, ignoring any jack of all trades cards.
	 */
	private int houses;

	/**
	 * Represents the number of spendable cross symbols this player has, ignoring any jack of all trades cards.
	 */
	private int crosses;

	/**
	 * Represents the number of spendable sword symbols this player has, ignoring any jack of all trades cards.
	 */
	private int swords;

	/**
	 * Represents the number of spendable coin symbols this player has.
	 */
	private int coins;

	/**
	 * Represents the number of jack of all trades cards this player has.
	 */
	private int jacks;

	/**
	 * Represents the number of mademoiselle cards this player has.
	 */
	private int mademoiselles;

	/**
	 * Represents the number of jester cards this player has.
	 */
	private int jesters;

	/**
	 * Represents the number of admiral cards this player has.
	 */
	private int admirals;

	/**
	 * Represents the number of governor cards this player has.
	 */
	private int governors;

	/**
	 * Holds all coin cards this player has. Ensures that always the earliest earned coin is spend.
	 */
	private final Queue<Card> coinDisplay;

	/**
	 * Holds all non coin cards this player has.
	 */
	private final ArrayList<Card> personalDisplay;

	/**
	 * Constructs a new representation of a player who has the specified name and no cards at all.
	 *
	 * @param name Name for this player.
	 */
	public Player(String name) {
		this.PLAYER_ID = Player.nextPlayerID++;
		this.name = name;
		this.anchors = 0;
		this.coins = 0;
		this.crosses = 0;
		this.houses = 0;
		this.jacks = 0;
		this.swords = 0;
		this.mademoiselles = 0;
		this.jesters = 0;
		this.admirals = 0;
		this.governors = 0;
		this.victoryPoints = 0;
		this.coinDisplay = new LinkedList<>();
		this.personalDisplay = new ArrayList<>();
	}

	/**
	 * Copy constructor for logging. Creates a log friendly deep alike copy of the given draw pile.
	 *
	 * @param player The draw pile to copy for logging.
	 */
	public Player(Player player) {
		this.PLAYER_ID = player.PLAYER_ID;
		this.name = player.name;
		this.anchors = player.anchors;
		this.coins = player.coins;
		this.crosses = player.crosses;
		this.houses = player.houses;
		this.jacks = player.jacks;
		this.swords = player.swords;
		this.mademoiselles = player.mademoiselles;
		this.jesters = player.jesters;
		this.admirals = player.admirals;
		this.governors = player.governors;
		this.victoryPoints = player.victoryPoints;
		this.coinDisplay = new LinkedList<>(player.coinDisplay);
		this.personalDisplay = new ArrayList<>(player.personalDisplay);
	}

	/**
	 * Returns the unique player identification number of this Player.
	 *
	 * @return The unique player identification number of this Player.
	 */
	public int getPlayerID() {
		return this.PLAYER_ID;
	}

	/**
	 * Returns the customisable player name of this Player.
	 *
	 * @return The customisable player name of this Player.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the number of victory points this Player currently has.
	 *
	 * @return The number of victory points this Player currently has.
	 */
	public int getVictoryPointsCount() {
		return this.victoryPoints;
	}

	/**
	 * Returns the number of anchor symbols this Player currently has, ignoring anchor symbols contributed by jack of
	 * all trades cards.
	 *
	 * @return The number of anchor symbols this Player currently has, ignoring anchor symbols contributed by jack of
	 *         all trades cards.
	 */
	public int getAnchorsCount() {
		return this.anchors;
	}

	/**
	 * Returns the number of cross symbols this Player currently has, ignoring cross symbols contributed by jack of all
	 * trades cards.
	 *
	 * @return The number of cross symbols this Player currently has, ignoring cross symbols contributed by jack of all
	 * 	 	   trades cards.
	 */
	public int getCrossesCount() {
		return this.crosses;
	}

	/**
	 * Returns the number of coins this Player currently has.
	 *
	 * @return The number of coins this Player currently has.
	 */
	public int getCoinsCount() {
		return this.coins;
	}

	/**
	 * Returns the number of house symbols this Player currently has, ignoring house symbols contributed by jack of all
	 * trades cards.
	 *
	 * @return The number of house symbols this Player currently has, ignoring house symbols contributed by jack of all
	 * 	 	   trades cards.
	 */
	public int getHousesCount() {
		return this.houses;
	}

	/**
	 * Returns the number of usable sword symbols this Player currently has.
	 *
	 * @return The number of usable sword symbols this Player currently has.
	 */
	public int getSwordsCount() {
		return this.swords;
	}

	/**
	 * Returns the number of jack of all trades cards this Player currently has.
	 *
	 * @return The number of jack of all trades cards this Player currently has.
	 */
	public int getJacksCount() {
		return this.jacks;
	}

	/**
	 * Returns the number of mademoiselle cards this Player currently has.
	 *
	 * @return The number of mademoiselle cards this Player currently has.
	 */
	public int getMademoisellesCount() {
		return mademoiselles;
	}

	/**
	 * Returns the number of jester cards this Player currently has.
	 *
	 * @return The number of jester cards this Player currently has.
	 */
	public int getJestersCount() {
		return jesters;
	}

	/**
	 * Returns the number of admiral cards this Player currently has.
	 *
	 * @return The number of admiral cards this Player currently has.
	 */
	public int getAdmiralsCount() {
		return admirals;
	}

	/**
	 * Returns the number of governor cards this Player currently has.
	 *
	 * @return The number of governor cards this Player currently has.
	 */
	public int getGovernorsCount() {
		return governors;
	}

	/**
	 * Returns a shallow copy of the list containing the non coin cards this player has.
	 *
	 * @return A shallow copy of the list containing the non coin cards this player has.
	 */
	public ArrayList<Card> getPersonalDisplay() {
		return new ArrayList<>(this.personalDisplay);
	}

	/**
	 * Returns the number of trader cards of the specified colour this player has.
	 *
	 * @param colour The colour of the trader cards to be counted.
	 * @return The number of trader cards of the specified colour this player has.
	 */
	public int getTradersCount(CardColour colour) {
		int tradersCount = 0;
		for(Card card : this.personalDisplay) {
			if(card.getCardName().equals(CardName.TRADER) & card.getCardColour().equals(colour)) {
				tradersCount++;
			}
		}
		return tradersCount;
	}

	/**
	 * Adds the specified card either to the coin cards or to the non-coin cards that this player owns, depending on
	 * whether the card is face down or face up. Adjusts all stored values depending on the added card.
	 * @param card the card to be added to the acting players coin or personal display
	 * @throws NullPointerException if the specified card is {@code null}.
	 */
	public void addCard(Card card, boolean faceUp) {
		if(card == null) {
			throw new NullPointerException();
		}
		if(!faceUp) {
			this.coinDisplay.add(card);
			this.coins++;
			return;
		}
		this.houses += card.getHouses();
		this.anchors += card.getAnchors();
		this.crosses += card.getCrosses();
		this.swords += card.getSwords();
		this.victoryPoints += card.getVictoryPoints();
		CardName cardName = card.getCardName();
		if(cardName.equals(CardName.JACK_OF_ALL_TRADES)) {
			this.jacks++;
		}
		if(cardName.equals(CardName.MADEMOISELLE)) {
			this.mademoiselles++;
		}
		if(cardName.equals(CardName.JESTER)) {
			this.jesters++;
		}
		if(cardName.equals(CardName.ADMIRAL)) {
			this.admirals++;
		}
		if(cardName.equals(CardName.GOVERNOR)) {
			this.governors++;
		}
		this.personalDisplay.add(card);
	}

	/**
	 * Returns the next earliest earned coin of this player or {@code null} if this player has got no coins.
	 *
	 * @return A reference to the Card-object representing the next earliest earned coin of this player or {@code null}
	 *         if this player has got no coins
	 */
	public Card removeCoin() {
		if(this.coins > 0) {
			Card coin = this.coinDisplay.remove();
			this.coins--;
			return coin;
		}
		return null;
	}

	/**
	 * Removes the specified card from the cards owned by this Player.
	 *
	 * @param card The Card-object to be removed.
	 * @return {@code true} if, and only if, the specified card was owned by this Player and was successfully removed.
	 * @throws NullPointerException if the specified card is {@code null}.
	 */
	public boolean removeCard(Card card) {
		if(card == null) {
			throw new NullPointerException();
		}
		this.houses -= card.getHouses();
		this.anchors -= card.getAnchors();
		this.crosses -= card.getCrosses();
		this.victoryPoints -= card.getVictoryPoints();
		CardName cardName = card.getCardName();
		if(cardName.equals(CardName.JACK_OF_ALL_TRADES)) {
			this.jacks--;
		}
		return this.personalDisplay.remove(card);
	}

	/**
	 * Sets the name of this Player as specified.
	 *
	 * @param name New name for this player.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Compares the specified Object with this Player for equality. Two Player-objects are considered equal if, and only
	 * if, both have the same unique identification number.
	 *
	 * @param obj The Object to be compared for equality with this Player.
	 * @return {@code true} if, and only if, the specified Object is a Player and has the same unique identification
	 *         number as this Player.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Player player = (Player) obj;
		return this.PLAYER_ID == player.PLAYER_ID;
	}

	/**
	 * Returns this players identification number as a unique hash code.
	 *
	 * @return This players identification number as a unique hash code.
	 */
	@Override
	public int hashCode() {
		return this.PLAYER_ID;
	}
}
