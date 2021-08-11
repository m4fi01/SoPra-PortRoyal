package controller;

import com.sun.javafx.binding.StringFormatter;
import model.Card;
import model.CardColour;
import model.CardName;
import model.CardType;

import java.util.ArrayList;

/**
 * CardFactory provides all necessary methods to construct Card-objects representing the cards in Port Royal based on
 * the information loaded from a CSV file in the format to define a pile of Port Royal cards.
 *
 * @author Hoang Hai Nguyen
 * @author Thomas Alexander Hövelmann
 *
 * @see Card
 * @see CardController
 * @see CardName
 * @see CardType
 * @see CardColour
 */
public class CardFactory {
    /**
     * Represents the virtual number of sword symbols for pirate ships which are unbeatable according to the game rules.
     */
    public static final int PIRATE_SWORDS = 1000;

    /**
     * Specifies the number of victory points of the special expedition card only used in games with five players.
     */
    public static final int FIVE_PLAYER_EXPEDITION_VICTORY_POINTS = 5;

    /**
     * Specifies the number of anchor symbols of the special expedition card only used in games with five players.
     */
    public static final int FIVE_PLAYER_EXPEDITION_ANCHORS = 1;

    /**
     * Specifies the number of cross symbols of the special expedition card only used in games with five players.
     */
    public static final int FIVE_PLAYER_EXPEDITION_CROSSES = 1;

    /**
     * Specifies the number of house symbols of the special expedition card only used in games with five players.
     */
    public static final int FIVE_PLAYER_EXPEDITION_HOUSES = 1;

    /**
     * Creates and returns a new person card with the specified parameter. If the name is not equal to
     * {@code CardName.TRADER} the specified colour is ignored. NOTE: In general, parameters are not checked for
     * plausibility in context of the rules of the game.
     *
     * @param cardName The name of the person card.
     * @param cardColour The colour of the person card. Ignored if the name is not equal to {@code CardName.TRADER}.
     * @param victoryPoints The victory points of the person card.
     * @return The newly created person card.
     * @throws NullPointerException if the specified name or colour is {@code null}.
     * @throws IllegalArgumentException if the specified name is not associated with {@code CardType.PERSON} or if the
     *                                  specified colour is {@code CardColour.NONE} in case of a trader
     *                                  ({@code CardName.Trader}).
     */
    public Card generatePerson(CardName cardName, CardColour cardColour, int victoryPoints, int coinValue) {
        if(cardName == null || cardColour == null) {
            throw new NullPointerException();
        }
        if(!cardName.getCardType().equals(CardType.PERSON)) {
            throw new IllegalArgumentException();
        }
        // Illegal name-colour combination according to the game rules.
        if(cardName.equals(CardName.TRADER) && cardColour.equals(CardColour.NONE)) {
            throw new IllegalArgumentException();
        }
        Card personCard = new Card();
        personCard.setCardName(cardName);
        personCard.setCardType(CardType.PERSON);
        personCard.setVictoryPoints(victoryPoints);
        String frontSideImagePath;
        if(cardName.equals(CardName.TRADER)) {
            personCard.setCardColour(cardColour);
            frontSideImagePath = "/view/images/cards/trader_" + cardColour.name().toLowerCase() + "_" + victoryPoints + "VP.png";
        } else {
            frontSideImagePath = "/view/images/cards/" + cardName.name().toLowerCase() + "_" + victoryPoints + "VP.png";
        }
        personCard.setCoinValue(coinValue);
        this.specifyPerson(personCard);
        personCard.setFrontSideImagePath(frontSideImagePath);
        return personCard;
    }

    /**
     * Sets additional attributes for all person cards which are not fully specified by the format of the CSV file 
     * defining a pile. 
     * 
     * @param card The person card which needs to be further specified.
     * @see CardController#generatePile(ArrayList)
     */
    private void specifyPerson(Card card) {
        if(card.getCardName().equals(CardName.CAPTAIN)) {
            card.setAnchors(1);
        } else if(card.getCardName().equals(CardName.PRIEST)) {
            card.setCrosses(1);
        } else if (card.getCardName().equals(CardName.SETTLER)) {
            card.setHouses(1);
        } else if(card.getCardName().equals(CardName.JACK_OF_ALL_TRADES)) {
            card.setAnchors(1);
            card.setCrosses(1);
            card.setHouses(1);
        } else if(card.getCardName().equals(CardName.SAILOR)) {
            card.setSwords(1);
        } else if(card.getCardName().equals(CardName.PIRATE)) {
            card.setSwords(2);
        }
    }

    /**
     * Creates and returns a new ship card with the specified parameters. NOTE: In general, parameters are not checked
     * for plausibility in context of the rules of the game.
     *
     * @param cardColour The colour of the ship card.
     * @param coinValue The absolute coin value of ship card.
     * @param swords The number of sword symbols of this ship card. A number of {@code PIRATE_SWORDS} swords
     *               represents pirates.
     * @return The newly created ship card.
     * @throws NullPointerException if the specified colour is {@code null}.
     * @throws UnsupportedOperationException if the specified colour is a colour not used for ships.
     * @see #PIRATE_SWORDS
     */
    public Card generateShip(CardColour cardColour, int coinValue, int swords) {
        if(cardColour == null) {
            throw new NullPointerException();
        }
        CardName cardName = cardColour.getShipName();
        Card shipCard = new Card();
        shipCard.setCardName(cardName);
        shipCard.setCardType(CardType.SHIP);
        shipCard.setCardColour(cardColour);
        shipCard.setCoinValue(-coinValue);
        shipCard.setSwords(swords);
        String frontSideImagePath = "/view/images/cards/ship_" + cardColour.name().toLowerCase() + "_" + coinValue + "G-" + swords + "S.png";
        shipCard.setFrontSideImagePath(frontSideImagePath);
        return shipCard;
    }

    /**
     * Creates and returns a new expedition card with the specified parameters. NOTE: In general, parameters are not
     * checked for plausibility in context of the rules of the game.
     *
     * @param anchors The number of anchor symbols of this expedition card.
     * @param crosses the number of cross symbols of this expedition card.
     * @param houses the number of house symbols of this expedition card.
     * @return The newly created expedition card.
     * @see #FIVE_PLAYER_EXPEDITION_ANCHORS
     * @see #FIVE_PLAYER_EXPEDITION_CROSSES
     * @see #FIVE_PLAYER_EXPEDITION_HOUSES
     * @see #FIVE_PLAYER_EXPEDITION_VICTORY_POINTS
     */
    public Card generateExpedition(int anchors, int crosses, int houses) {
        Card expeditionCard = new Card();
        expeditionCard.setCardName(CardName.EXPEDITION);
        expeditionCard.setCardType(CardType.EXPEDITION);
        if(anchors == FIVE_PLAYER_EXPEDITION_ANCHORS
           && crosses == FIVE_PLAYER_EXPEDITION_CROSSES
           && houses == FIVE_PLAYER_EXPEDITION_HOUSES) { // Special expedition card for games with five players.
            expeditionCard.setVictoryPoints(FIVE_PLAYER_EXPEDITION_VICTORY_POINTS);
            expeditionCard.setBacksideImagePath("/view/images/cards/expedition_1A-1C-1H_backside.png");
        } else {
            expeditionCard.setVictoryPoints(2 * (anchors + crosses + houses));
        }
        expeditionCard.setAnchors(anchors);
        expeditionCard.setCrosses(crosses);
        expeditionCard.setHouses(houses);
        expeditionCard.setCoinValue(-(anchors + crosses + houses));
        String frontSideImagePath = "/view/images/cards/expedition_" + anchors + "A-" + crosses + "C-" + houses + "H.png";
        expeditionCard.setFrontSideImagePath(frontSideImagePath);
        return expeditionCard;
    }

    /**
     * Creates and returns a new tax increase card with the specified parameter.
     *
     * @param cardName The name of the tax increase card.
     * @return The newly created tax increase card.
     * @throws NullPointerException if the specified name is {@code null}.
     * @throws IllegalArgumentException if the specified name is not associated with {@code CardType.TAX_INCREASE}.
     * @see CardType#TAX_INCREASE
     */
    public Card generateTaxIncrease(CardName cardName) {
        if(cardName == null) {
            throw new NullPointerException();
        }
        if(!cardName.getCardType().equals(CardType.TAX_INCREASE)) {
            throw new IllegalArgumentException();
        }
        Card taxIncreaseCard = new Card();
        taxIncreaseCard.setCardName(cardName);
        taxIncreaseCard.setCardType(CardType.TAX_INCREASE);
        if(cardName.equals(CardName.CHARITY)) {
            taxIncreaseCard.setFrontSideImagePath("/view/images/cards/tax_increase_min.png");
        } else if(cardName.equals(CardName.PROTECTION_MONEY)) {
            taxIncreaseCard.setFrontSideImagePath("/view/images/cards/tax_increase_max.png");
        }
        return taxIncreaseCard;
    }
}
