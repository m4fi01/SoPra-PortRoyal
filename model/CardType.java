package model;

import java.io.Serializable;

/**
 * CardType is an enumeration of all card types used in Port Royal. In addition, CardType provides toString methods to
 * get string representations of a given CardType in German for some grammatical contexts.
 * <p>
 * CardType implements the Serializable-interface to allow the current game of Port Royal to be stored by serialising
 * the GameLog-object that represents it.
 *
 * @author Thomas Alexander Hövelmann
 *
 * @see Card
 * @see GameLog
 */
public enum CardType implements Serializable {
	/**
	 * Represents the type of all person cards in Port Royal.
	 */
	PERSON("Person","Personen"),

	/**
	 * Represents the type of all ship cards in Port Royal.
	 */
	SHIP("Schiff","Schiffe"),

	/**
	 * Represents the type of all expedition cards in Port Royal.
	 */
	EXPEDITION("Expedition","Expeditionen"),

	/**
	 * Represents the type of all tax increase cards in Port Royal.
	 */
	TAX_INCREASE("Steuererhöhung","Steuererhöhungen");

	/**
	 * String-representation of this type as a noun in singular form in German.
	 */
	private String noun;

	/**
	 * String-representation of this type as a noun in plural form in German.
	 */
	private String plural;

	/**
	 * Constructs a type with its specific String-representations in German. If {@code noun} is {@code null}, the
	 * result of {@link #name()} is used instead. If {@code plural} is {@code null}, the previously set value of
	 * {@code noun} is used instead.
	 *
	 * @param noun String-representation of this type as a noun in singular form in German.
	 * @param plural String-representation of this type as a noun in plural form in German.
	 */
	private CardType(String noun, String plural) {
			this.noun = noun;
			this.plural = plural;
	}

	/**
	 * Returns a String-representation of this CardType as a noun in singular form in German. If this specific CardType
	 * is not supported calling this method is equal to {@link #name()}.
	 *
	 * @return A String-representation of this CardType as a noun in singular form in German.
	 */
	@Override
	public String toString() {
		return this.noun;
	}

	/**
	 * Returns a String-representation of this CardType as a noun in plural form or a noun in singular form in German
	 * depending on the specified parameter. If this specific CardColour is not supported calling this method is equal
	 * to {@link #name()}.
	 *
	 * @param asPlural {@code true} if the requested string representation is to be an noun in plural form.
	 * @return A String-representation of this CardType as a noun in plural form or a noun in singular form in German
	 *         depending on the specified parameter.
	 */
	public String toString(boolean asPlural) {
		if(asPlural) {
			return this.plural;
		} else {
			return this.noun;
		}
	}
}
