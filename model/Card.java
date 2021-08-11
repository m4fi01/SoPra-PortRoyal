package model;

import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the cards of the game "Port Royal"
 * @author Jana Hauck
 */
public class Card implements Serializable {
	/**
	 * FrontSide image path of the card.
	 */
	private String frontSideImagePath;

	/**
	 * Backside image path of the card. Default: The coin.
	 */
	private String backsideImagePath;

	/**
	 * Victory Points of this card.
	 */
	private int victoryPoints;

	/**
	 * Anchor symbols of this card.
	 */
	private int anchors;

	/**
	 * House symbols of this card.
	 */
	private int houses;

	/**
	 * Cross symbols of this card.
	 */
	private int crosses;

	/**
	 * Sword symbols of this card. For the pirate {@link controller.CardFactory#PIRATE_SWORDS} is used.
	 */
	private int swords;

	/**
	 * Positive Value: The number of coins a player has to pay for this card.
	 * Negative Value: The number of coins a player gets from this card.
	 */
	private int coinValue;

	/**
	 * The name of this card.
	 */
	private CardName cardName;

	/**
	 * The type of this card.
	 */
	private CardType cardType;

	/**
	 * The colour of this card.
	 */
	private CardColour cardColour;

	/**
	 * Constructor that generates an empty card with the coin backside and {@code CardColour.NONE}.
	 *
	 * @see CardColour#NONE
	 */
	public Card() {
		this.cardName = null;
		this.cardType = null;
		this.cardColour = CardColour.NONE;
		this.backsideImagePath = "/view/images/cards/coin.png";
		this.frontSideImagePath = null;
		this.victoryPoints = 0;
		this.anchors = 0;
		this.houses = 0;
		this.crosses = 0;
		this.swords = 0;
		this.coinValue = 0;
	}

	/**
	 * Returns the cardName of the card
	 * @return {@code cardName} of the card
	 */
	public CardName getCardName() {
		return this.cardName;
	}

	/**
	 * Returns victoryPoints of the card
	 * @return {@code victoryPoints} of the card
	 */
	public int getVictoryPoints() {
		return victoryPoints;
	}

	/**
	 * Returns anchors of the card
	 * @return {@code anchors} of the card
	 */
	public int getAnchors() {
		return anchors;
	}

	/**
	 * Returns houses of the card
	 * @return {@code houses} of the card
	 */
	public int getHouses() {
		return houses;
	}

	/**
	 * Returns crosses of the card
	 * @return {@code crosses} of the card
	 */
	public int getCrosses() {
		return crosses;
	}

	/**
	 * Returns swords of the card
	 * @return {@code swords} of the card
	 */
	public int getSwords() {
		return swords;
	}

	/**
	 * Returns coinValue of the card
	 * @return {@code coinValue} of the card
	 */
	public int getCoinValue() {
		return coinValue;
	}

	/**
	 * Returns the path for the front side image of this card.
	 *
	 * @return The path for the front side image of this card.
	 */
	public String getFrontSideImagePath() {
		return this.frontSideImagePath;
	}

	/**
	 * Returns the path for the backside image of this card.
	 *
	 * @return The path for the backside image of this card.
	 */
	public String getBackside() {
		return this.backsideImagePath;
	}

	/**
	 * Returns the card type of the card
	 * @return {@code cardType} of the card
	 */
	public CardType getCardType() {
		return cardType;
	}

	/**
	 * Returns the card colour of the card
	 * @return {@code cardColour} of the card
	 */
	public CardColour getCardColour() {
		return cardColour;
	}

	/**
	 * Sets the path for the front side image of this card.
	 *
	 * @param frontSideImagePath The path of the image which will be shown if this card is face up.
	 */
	public void setFrontSideImagePath(String frontSideImagePath) {
		this.frontSideImagePath = frontSideImagePath;
	}

	/**
	 * Sets the path for the backside image of this card.
	 *
	 * @param backsideImagePath The path of the image which will be shown if this card is face down.
	 */
	public void setBacksideImagePath(String backsideImagePath) {
		this.backsideImagePath = backsideImagePath;
	}

	/**
	 * Sets the {@link #victoryPoints}
	 * @param points value that will be set as the victory points of the card
	 */
	public void setVictoryPoints(int points) {
		victoryPoints = points;
	}

	/**
	 * Sets the {@link #anchors}
	 * @param anchors value that will be set as the anchors of the card
	 */
	public void setAnchors(int anchors) {
		this.anchors = anchors;
	}

	/**
	 * Sets the {@link #houses}
	 * @param houses value that will be set as the houses of the card
	 */
	public void setHouses(int houses) {
		this.houses = houses;
	}

	/**
	 * Sets the {@link #crosses}
	 * @param crosses value that will be set as the crosses of the card
	 */
	public void setCrosses(int crosses) {
		this.crosses = crosses;
	}

	/**
	 * Sets the {@link #swords}
	 * @param swords value that will be set as the swords of the card
	 */
	public void setSwords(int swords) {
		this.swords = swords;
	}

	/**
	 * Sets the {@link #coinValue},
	 * positive value if the player has to pay the value, negative if the player gets the value
	 * @param coinValue value that will be set as the coinValue of the card
	 */
	public void setCoinValue(int coinValue) {
		this.coinValue = coinValue;
	}

	/**
	 * Sets the {@link #cardName}
	 * @param name CardName that will be set as the cardName of the card
	 */
	public void setCardName(CardName name){
		this.cardName= name;
	}

	/**
	 * Sets the {@link #cardType}
	 * @param type CardType that will be set as the cardName of the card
	 */
	public void setCardType(CardType type){
		this.cardType=type;
	}

	/**
	 * Sets the {@link #cardColour}
	 * @param colour CardColour that will be set as the cardName of the card
	 */
	public void setCardColour(CardColour colour){
		this.cardColour=colour;
	}

	/**
	 * Compares an object to a card and returns true if the two cards have the same values, returns false otherwise
	 * @param obj object that should be compared to this card
	 * @return {@code true} if two cards are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Card card = (Card) obj;
		return victoryPoints == card.victoryPoints &&
				anchors == card.anchors &&
				houses == card.houses &&
				crosses == card.crosses &&
				swords == card.swords &&
				coinValue == card.coinValue &&
				Objects.equals(frontSideImagePath, card.frontSideImagePath) &&
				Objects.equals(backsideImagePath, card.backsideImagePath) &&
				cardName == card.cardName &&
				cardType == card.cardType &&
				cardColour == card.cardColour;
	}

	/**
	 * Returns hashcode of the card
	 * @return hashcode of the card
	 */
	@Override
	public int hashCode() {
		return Objects.hash(frontSideImagePath, backsideImagePath, victoryPoints, anchors, houses, crosses, swords, coinValue, cardName, cardType, cardColour);
	}
}
