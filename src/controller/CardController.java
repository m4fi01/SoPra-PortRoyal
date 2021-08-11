package controller;

import model.*;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * CardController provides the functionality to create a pile like ArrayList of all cards needed for a game of Port
 * Royal based on String-representations of this cards previously loaded from a proper CSV file and to create the
 * special expedition card used in games of Port Royal with five players.
 *
 * @author Thomas Alexander Hövelmann
 *
 * @see Card
 * @see CardFactory
 * @see IOController
 * @see GameController
 */
public class CardController {
	/**
	 * The absolute coin value of a trader card with one victory point.
	 */
	public static final int COINS_FOR_1VP_TRADER = 3;

	/**
	 * The absolute coin value of a trader card with two victory point.
	 */
	public static final int COINS_FOR_2VP_TRADER = 5;

	/**
	 * The String-representation of the expedition card only used in games of Port Royal with for five players.
	 */
	public static final String FIVE_PLAYER_EXPEDITION_STRING_REPRESENTATION = "Expedition,3 Different (5P only),3";

	/**
	 * The card factory used by this controller to construct Card-objects based on the information loaded from a CSV
	 * file in the format to define a pile of Port Royal cards.
	 */
	private final CardFactory CARD_FACTORY;

	/**
	 * Constructs a new CardController-object and a new associated CardFactory-object.
	 */
	public CardController(){
		this.CARD_FACTORY = new CardFactory();
	}

	/**
	 * Returns a list of Port Royal cards based on the specified list of String-objects containing the information
	 * loaded from a CSV file in the format to define a pile of Port Royal cards. The order of cards in the returned
	 * list matches the order of the String-representations of this cards in the specified list.
	 *
	 * @param cardsAsStrings A list of String-representations of the Port Royal cards to be created.
	 * @return A list of Port Royal cards based on the specified list of String-objects.
	 * @throws NullPointerException if the specified list is {@code null}.
	 * @throws IllegalArgumentException if the length of the specified list does not match the expected size of a
	 *                                  initial draw pile in a game of Port Royal
	 *                                  ({@code PileController.INITIAL_PILE_SIZE}).
	 * @see PileController#INITIAL_PILE_SIZE
	 */
	public ArrayList<Card> generatePile(ArrayList<String> cardsAsStrings) {
		if(cardsAsStrings == null) {
			throw new NullPointerException();
		}
		// Removes the expedition card only used in games of Port Royal with for five players.
		cardsAsStrings.remove(FIVE_PLAYER_EXPEDITION_STRING_REPRESENTATION);
		if(cardsAsStrings.size() != PileController.INITIAL_PILE_SIZE) {
			throw new IllegalArgumentException();
		}
		ArrayList<Card> cards = new ArrayList<>();
		for(String cardAsString : cardsAsStrings) {
			cards.add(this.generateCard(cardAsString));
		}
		return cards;
	}

	/**
	 * Returns a newly constructed Card-object representing the expedition card only used in games of Port Royal with
	 * five players.
	 *
	 * @return The expedition card only used in games of Port Royal with five players.
	 */
	public Card generateFinalExpedition() {
		return this.CARD_FACTORY.generateExpedition(1,1,1);
	}

	/**
	 * Returns a newly constructed Port Royal card based on the information contained in the specified String. The
	 * format of this String is given by the format of CSV files for defining a pile of Port Royal cards.
	 *
	 * @param argString String-representation of the information needed for a unambiguous creation of a Port Royal card.
	 * @return A newly constructed Port Royal card based on the information contained in the specified String.
	 * @throws NullPointerException if the specified String is {@code null}.
	 * @throws IllegalArgumentException if the specified String has not the expected format.
	 */
	private Card generateCard(String argString) {
		if(argString == null) {
			throw new NullPointerException();
		}
		try {
			String[] args = this.extractCardArguments(argString);
			String cardNameAsString = args[0].toUpperCase().replaceAll(" ","_");
			int coinValue = Integer.parseInt(args[2].substring(0,1));
			if(cardNameAsString.equals("TAX_INCREASE")) { // Necessary because tax increase is split into CHARITY and PROTECTION_MONEY.
				return this.generateTaxIncrease(args[1]);
			}
			CardName cardName = CardName.valueOf(cardNameAsString);
			CardType cardType = cardName.getCardType();
			if(cardName.equals(CardName.TRADER)) {
				return this.generateTrader(args[1],coinValue);
			} else if(cardType.equals(CardType.PERSON)) {
				return this.generateNonTraderPerson(cardName,coinValue,args[1]);
			} else if(cardType.equals(CardType.SHIP)) {
				return this.generateShip(cardName, coinValue, args[1]);
			} else if(cardType.equals(CardType.EXPEDITION)) {
				return this.generateExpedition(args[1]);
			}
		} catch(NumberFormatException nFEx) {
			// Transformed to IllegalArgumentException
		}
		throw new IllegalArgumentException();
	}

	/**
	 * Returns a newly constructed Card-object representing a trader card based on the specified information previously
	 * extracted in {@link #generateCard(String)}. NOTE: In general, parameters are not checked for plausibility in
	 * context of the rules of the game.
	 *
	 * @param argsAsString The other information needed to construct the trader card as String.
	 * @param coinValue The absolute coin value of the trader card to be constructed.
	 * @return A newly constructed Card-object representing a trader card based on the specified information.
	 * @throws IllegalArgumentException if one of the parameters is {@code null} or at least one of the parameters
	 *                                  violates the expected format or is invalid according to the game rules.
	 * @see #generateCard(String)
	 * @see CardFactory#generatePerson(CardName, CardColour, int, int)
	 */
	private Card generateTrader(String argsAsString, int coinValue) {
		if(argsAsString == null) {
			throw new IllegalArgumentException();
		}
		argsAsString = argsAsString.toUpperCase();
		CardColour traderColour;
		if(argsAsString.contains(CardColour.YELLOW.name())) {
			traderColour = CardColour.YELLOW;
		} else if(argsAsString.contains(CardColour.BLUE.name())) {
			traderColour = CardColour.BLUE;
		} else if(argsAsString.contains(CardColour.GREEN.name())) {
			traderColour = CardColour.GREEN;
		} else if(argsAsString.contains(CardColour.RED.name())) {
			traderColour = CardColour.RED;
		} else if(argsAsString.contains(CardColour.BLACK.name())) {
			traderColour = CardColour.BLACK;
		} else {
			throw new IllegalArgumentException();
		}
		if(coinValue == COINS_FOR_1VP_TRADER) {
			return this.CARD_FACTORY.generatePerson(CardName.TRADER,traderColour,1,coinValue);
		} else if(coinValue == COINS_FOR_2VP_TRADER) {
			return this.CARD_FACTORY.generatePerson(CardName.TRADER,traderColour,2,coinValue);
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Returns a newly constructed Card-object representing a ship card based on the specified information previously
	 * extracted in {@link #generateCard(String)}. NOTE: In general, parameters are not checked for plausibility in
	 * context of the rules of the game.
	 *
	 * @param cardName The name of the ship card to be constructed.
	 * @param coinValue The absolute coin value of the ship card to be constructed.
	 * @param swordsAsString The number of sword symbols of the ship card to be constructed.
	 * @return A newly constructed Card-object representing a ship card based on the specified information.
	 * @throws IllegalArgumentException if one of the parameters is {@code null} or at least one of the parameters
	 *                                  violates the expected format or is invalid according to the game rules.
	 * @see #generateCard(String)
	 * @see CardFactory#generateShip(CardColour, int, int) 
	 */
	private Card generateShip(CardName cardName, int coinValue, String swordsAsString) {
		try {
			CardColour shipColour = cardName.getCardColour();
			if(swordsAsString.equals("Pirate")) {
				return this.CARD_FACTORY.generateShip(shipColour,coinValue,CardFactory.PIRATE_SWORDS);
			}
			int swords = Integer.parseInt(swordsAsString.substring(0,1));
			return this.CARD_FACTORY.generateShip(shipColour,coinValue,swords);
		} catch(NullPointerException | NumberFormatException nPNFEx) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Returns a newly constructed Card-object representing a non-trader card based on the specified information 
	 * previously extracted in {@link #generateCard(String)}. NOTE: In general, parameters are not checked for 
	 * plausibility in context of the rules of the game.
	 * 
	 * @param cardName The name of the non-trader card to be constructed.
	 * @param coinValue The absolute coin value of the non-trader card to be constructed.
	 * @param victoryPointsAsString The victory points of the non-trader card to be constructed as String.
	 * @return A newly constructed Card-object representing a non-trader card based on the specified information
	 * @throws IllegalArgumentException if one of the parameters is {@code null} or at least one of the parameters
	 *                                  violates the expected format or is invalid according to the game rules.
	 * @see #generateCard(String)
	 * @see CardFactory#generatePerson(CardName, CardColour, int, int) 
	 */
	private Card generateNonTraderPerson(CardName cardName, int coinValue, String victoryPointsAsString) {
		try {
			int victoryPoints = Integer.parseInt(victoryPointsAsString.substring(0,1));
			return this.CARD_FACTORY.generatePerson(cardName,CardColour.NONE,victoryPoints,coinValue);
		} catch(NullPointerException | NumberFormatException nPNFEx) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Returns a newly constructed Card-object representing a tax increase card based on the specified information 
	 * previously extracted in {@link #generateCard(String)}. NOTE: In general, parameters are not checked for 
	 * plausibility in context of the rules of the game.
	 * 
	 * @param nameAsString The name of the tax increase card to be constructed.
	 * @return A newly constructed Card-object representing a tax increase card based on the specified information.
	 * @throws IllegalArgumentException if the parameters is {@code null} or violates the expected format or is invalid
	 *                                  according to the game rules.
	 * @see #generateCard(String)
	 * @see CardFactory#generateTaxIncrease(CardName) 
	 */
	private Card generateTaxIncrease(String nameAsString) {
		if(nameAsString == null) {
			throw new IllegalArgumentException();
		}
		if(nameAsString.equals("Max Sword")) {
			return this.CARD_FACTORY.generateTaxIncrease(CardName.PROTECTION_MONEY);
		} else if(nameAsString.equals("Min VP")) {
			return this.CARD_FACTORY.generateTaxIncrease(CardName.CHARITY);
		}
		throw new IllegalArgumentException();
	}

	/**
	 * Returns a newly constructed Card-object representing a expedition card based on the specified information
	 * previously extracted in {@link #generateCard(String)}. NOTE: In general, parameters are not checked for
	 * plausibility in context of the rules of the game.
	 *
	 * @param valuesAsDescription The values of the expedition card to be constructed codes as a description.
	 * @throws IllegalArgumentException if the parameters is {@code null} or violates the expected format or is invalid
	 * 	                                according to the game rules.
	 * @return A newly constructed Card-object representing a expedition card based on the specified information.
	 * @see #generateCard(String)
	 * @see CardFactory#generateTaxIncrease(CardName)
	 */
	private Card generateExpedition(String valuesAsDescription) {
		if(valuesAsDescription == null) {
			throw new IllegalArgumentException();
		}
		switch(valuesAsDescription) {
			case "House Pair" : return this.CARD_FACTORY.generateExpedition(0,0,2);
			case "Cross Pair" : return this.CARD_FACTORY.generateExpedition(0,2,0);
			case "Anchor Pair" : return this.CARD_FACTORY.generateExpedition(2,0,0);
			case "Cross Pair + House" : return this.CARD_FACTORY.generateExpedition(0,2,1);
			case "Anchor Pair + House" : return this.CARD_FACTORY.generateExpedition(2,0,1);
			default : throw new IllegalArgumentException();
		}
	}

	/**
	 * Returns a String-Array of length three containing the extracted information from the specified String-parameter
	 * according to the format of CSV file to define a pile of Port Royal cards. The resulting String-Array contains
	 * following information:
	 * Index 0: The card name
	 * Index 1: Necessary additional information depending on the specific card
	 * Index 3: The absolute coin value of the card
	 *
	 * @param argString String-representation of the information needed for a unambiguous creation of a Port Royal card.
	 * @return A String-Array of length three containing the extracted information from the specified String-parameter.
	 * @throws NullPointerException if the specified parameter is {@code null}.
	 * @throws IllegalArgumentException if the specified parameter violates the expected format.
	 * @see #generateCard(String)
	 */
	private String[] extractCardArguments(String argString) {
		if(argString == null) {
			throw new NullPointerException();
		}
		String[] args = new String[3];
		try {
			StringTokenizer argTokens = new StringTokenizer(argString,IOController.CSV_SEPARATOR);
			args[0] = argTokens.nextToken();
			args[1] = argTokens.nextToken();
			args[2] = argTokens.nextToken();
		} catch(NoSuchElementException nSEEx) {
			throw new IllegalArgumentException();
		}
		return args;
	}
}
