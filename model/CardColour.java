package model;

import java.io.Serializable;

/**
 * CardColour is an enumeration of all card colours used in Port Royal, including NONE to represent the colour of
 * uncoloured cards. In addition, CardColour provides toString methods to get string representations of a given
 * CardColour in German for some grammatical contexts, and a method to get the corresponding CardName for ships of a
 * given colour.
 * <p>
 * CardColour implements the Serializable-interface to allow the current game of Port Royal to be stored by serialising
 * the GameLog-object that represents it.
 *
 * @author Thomas Alexander Hövelmann
 *
 * @see Card
 * @see CardName
 * @see GameLog
 */
public enum CardColour implements Serializable {
	/**
	 * Represents the colour of all uncoloured cards in Port Royal.
	 */
	NONE("Farblosigkeit","farblos"),

	/**
	 * Represents the colour of all yellow cards in Port Royal.
	 */
	YELLOW("Gelb","gelb"),

	/**
	 * Represents the colour of all blue cards in Port Royal.
	 */
	BLUE("Blau","blau"),

	/**
	 * Represents the colour of all green cards in Port Royal.
	 */
	GREEN("Grün","grün"),

	/**
	 * Represents the colour of all red cards in Port Royal.
	 */
	RED("Rot","rot"),

	/**
	 * Represents the colour of all black cards in Port Royal.
	 */
	BLACK("Schwarz","schwarz");

	/**
	 * String-representation of this colour as a noun in singular form in German.
	 */
	private String noun;

	/**
	 * String-representation of this colour as a adjective in German.
	 */
	private String adjective;

	/**
	 * Constructs a colour with its specific String-representations in German. If {@code noun} is {@code null}, the
	 * result of {@link #name()} is used instead. If {@code adjective} is {@code null}, the previously set value of
	 * {@code noun} is used instead.
	 *
	 * @param noun String-representation of this colour as a noun in singular form in German.
	 * @param adjective String-representation of this colour as a adjective in German.
	 */
	private CardColour(String noun, String adjective) {
			this.noun = noun;
			this.adjective = adjective;
	}

	/**
	 * Returns a String-representation of this CardColour as a noun in singular form in German. If this specific
	 * CardColour is not supported calling this method is equal to {@link #name()}.
	 *
	 * @return A String-representation of this CardColour as a noun in singular form in German.
	 */
	@Override
	public String toString() {
		return this.noun;
	}

	/**
	 * Returns a String-representation of this CardColour as an adjective or a noun in singular form in German depending
	 * on the specified parameter. If this specific CardColour is not supported calling this method is equal to
	 * {@link #name()}.
	 *
	 * @param asAdjective {@code true} if the requested string representation is to be an adjective.
	 * @return A String-representation of this CardColour as an adjective or a noun in singular form in German depending
	 *         on the specified parameter.
	 */
	public String toString(boolean asAdjective) {
		if(asAdjective) {
			return this.adjective;
		} else {
			return this.noun;
		}
	}

	/**
	 * Returns the CardName of a ship corresponding to this specific CardColour.
	 *
	 * @return The CardName of a ship corresponding to this specific CardColour.
	 * @throws UnsupportedOperationException if this specific CardColour is not mapped to the CardName of a ship.
	 */
	public CardName getShipName() throws UnsupportedOperationException {
		switch(this) {
			case YELLOW : return CardName.PINNACE;
			case BLUE : return CardName.FLUTE;
			case GREEN : return CardName.SKIFF;
			case RED : return CardName.FRIGATE;
			case BLACK : return CardName.GALLEON;
			default : throw new UnsupportedOperationException();
		}
	}
}
