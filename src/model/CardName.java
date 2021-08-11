package model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * CardName is an enumeration of all card names used in Port Royal. In addition, CardName provides toString methods to
 * get string representations of a given CardName in German for some grammatical contexts, and a method to get the
 * corresponding CardColour for a given CardName.
 * <p>
 * CardName implements the Serializable-interface to allow the current game of Port Royal to be stored by serialising
 * the GameLog-object that represents it.
 *
 * @author Thomas Alexander Hövelmann
 *
 * @see Card
 * @see CardColour
 * @see GameLog
 */
public enum CardName implements Serializable {
	/**
	 * Represents the name of the trader cards in Port Royal.
	 */
	TRADER("Händler",null,null,CardType.PERSON),

	/**
	 * Represents the name of the settler cards in Port Royal.
	 */
	SETTLER("Siedler",null,CardColour.NONE,CardType.PERSON),

	/**
	 * Represents the name of the captain cards in Port Royal.
	 */
	CAPTAIN("Kapitän","Kapitäne",CardColour.NONE,CardType.PERSON),

	/**
	 * Represents the name of the priest cards in Port Royal.
	 */
	PRIEST("Priester",null,CardColour.NONE,CardType.PERSON),

	/**
	 * Represents the name of the jack of all trades cards in Port Royal.
	 */
	JACK_OF_ALL_TRADES("Tausendsassa","Tausendsassas",CardColour.NONE,CardType.PERSON),

	/**
	 * Represents the name of the sailor cards in Port Royal.
	 */
	SAILOR("Matrose","Matrosen",CardColour.NONE,CardType.PERSON),

	/**
	 * Represents the name of the pirate cards in Port Royal.
	 */
	PIRATE("Pirat","Piraten",CardColour.NONE,CardType.PERSON),

	/**
	 * Represents the name of the mademoiselle cards in Port Royal.
	 */
	MADEMOISELLE("Fräulein","Fräuleins",CardColour.NONE,CardType.PERSON),

	/**
	 * Represents the name of the jester cards in Port Royal.
	 */
	JESTER("Witzbold","Witzbolde",CardColour.NONE,CardType.PERSON),

	/**
	 * Represents the name of the admiral cards in Port Royal.
	 */
	ADMIRAL("Admiral","Admiräle",CardColour.NONE,CardType.PERSON),

	/**
	 * Represents the name of the governor cards in Port Royal.
	 */
	GOVERNOR("Gouverneur","Gouverneure",CardColour.NONE,CardType.PERSON),

	/**
	 * Represents the name of the pinnace cards in Port Royal.
	 */
	PINNACE("Pinasse","Pinassen",CardColour.YELLOW,CardType.SHIP),

	/**
	 * Represents the name of the flute cards in Port Royal.
	 */
	FLUTE("Fleute","Fleuten",CardColour.BLUE,CardType.SHIP),

	/**
	 * Represents the name of the skiff cards in Port Royal.
	 */
	SKIFF("Barke","Barken",CardColour.GREEN,CardType.SHIP),

	/**
	 * Represents the name of the frigate cards in Port Royal.
	 */
	FRIGATE("Fregatte","Fregatten",CardColour.RED,CardType.SHIP),

	/**
	 * Represents the name of the galleon cards in Port Royal.
	 */
	GALLEON("Galeone","Galeonen",CardColour.BLACK,CardType.SHIP),

	/**
	 * Represents the name of the expedition cards in Port Royal.
	 */
	EXPEDITION("Expedition","Expeditionen",CardColour.NONE,CardType.EXPEDITION),

	/**
	 * Represents the name of the tax increase cards in Port Royal, whose evaluation depends on the number of sword
	 * symbols a player has.
	 */
	PROTECTION_MONEY("Schutzgeld","Schutzgelder",CardColour.NONE,CardType.TAX_INCREASE),

	/**
	 * Represents the name of the tax increase cards in Port Royal, whose evaluation depends on the number of victory
	 * points symbols a player has.
	 */
	CHARITY("Almosen",null,CardColour.NONE,CardType.TAX_INCREASE);

	/**
	 * String-representation of this name as a noun in singular form in German.
	 */
	private String noun;

	/**
	 * String-representation of this name as a noun in plural form in German.
	 */
	private String plural;

	/**
	 * The colour corresponding to this name.
	 */
	private CardColour cardColour;

	/**
	 * The type corresponding to this name.
	 */
	private CardType cardType;

	/**
	 * Constructs a name with its specific String-representations in German and the specified colour. If {@code noun} is
	 * {@code null}, the result of {@link #name()} is used instead. If {@code plural} is {@code null}, the previously
	 * set value of {@code noun} is used instead. If {@code cardColour} is {@code null} the colour corresponding to this
	 * name is assumed to be ambiguous.
	 *
	 * @param noun String-representation of this name as a noun in singular form in German.
	 * @param plural String-representation of this name as a noun in plural form in German.
	 * @param cardColour The colour corresponding to this name.
	 */
	private CardName(String noun, String plural, CardColour cardColour, CardType cardType) {
		this.noun = noun;
		if(plural == null) {
			this.plural = this.noun;
		} else {
			this.plural = plural;
		}
		this.cardColour = cardColour;
		this.cardType = cardType;
	}

	/**
	 * Returns a String-representation of this CardName as a noun in singular form in German. If this specific CardName
	 * is not supported calling this method is equal to {@link #name()}.
	 *
	 * @return A String-representation of this CardName as a noun in singular form in German.
	 */
	@Override
	public String toString() {
		return this.noun;
	}

	/**
	 * Returns a String-representation of this CardName as a noun in plural form or a noun in singular form in German
	 * depending on the specified parameter. If this specific CardName is not supported calling this method is equal
	 * to {@link #name()}.
	 *
	 * @param asPlural {@code true} if the requested string representation is to be an noun in plural form.
	 * @return A String-representation of this CardName as a noun in plural form or a noun in singular form in German
	 *         depending on the specified parameter.
	 */
	public String toString(boolean asPlural) {
		if(asPlural) {
			return this.plural;
		} else {
			return this.noun;
		}
	}

	/**
	 * Returns the CardColour corresponding to this specific CardName.
	 *
	 * @return The CardColour corresponding to this specific CardName.
	 * @throws UnsupportedOperationException if this specific CardName is not mapped to a (unambiguous) CardColour.
	 */
	public CardColour getCardColour() throws UnsupportedOperationException {
		if(this.cardColour == null) {
			throw new UnsupportedOperationException();
		}
		return this.cardColour;
	}

	/**
	 * Returns the CardType corresponding to this specific CardName.
	 *
	 * @return The CardType corresponding to this specific CardName.
	 */
	public CardType getCardType() {
		return this.cardType;
	}
}
