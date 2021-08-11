package model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Mohammad Almidani
 *
 * Test class for Card
 */
public class CardTest{

    /**
     * Testing both methods getCardName() and setCardName()
     */
    @Test
    public void getCardName() {
        Card card = new Card();
        card.setCardName(CardName.TRADER);
        assertEquals(card.getCardName(),CardName.TRADER);
        Card card1 = new Card();
        card1.setCardName(CardName.CAPTAIN);
        assertEquals(card1.getCardName(),CardName.CAPTAIN);
    }

    /**
     * Testing both methods getVictoryPoints() and setVictoryPoints()
     */
    @Test
    public void getVictoryPoints() {
        Card card = new Card();
        card.setVictoryPoints(10);
        assertEquals(card.getVictoryPoints(),10);
    }

    /**
     * Testing both methods getAnchors() and setAnchors()
     */
    @Test
    public void getAnchors() {
        Card card = new Card();
        card.setAnchors(5);
        assertEquals(card.getAnchors(),5);
    }

    /**
     * Testing both methods getHouses() and setHouses()
     */
    @Test
    public void getHouses() {
        Card card = new Card();
        card.setHouses(5);
        assertEquals(card.getHouses(),5);
    }

    /**
     * Testing both methods getCrosses() and setCrosses()
     */
    @Test
    public void getCrosses() {
        Card card = new Card();
        card.setCrosses(3);
        assertEquals(card.getCrosses(),3);
    }

    /**
     * Testing both methods getSwords() and setSwords()
     */
    @Test
    public void getSwords() {
        Card card = new Card();
        card.setSwords(3);
        assertEquals(card.getSwords(),3);
    }

    /**
     * Testing both methods getCoinValue() and setCoinValue()
     */
    @Test
    public void getCoinValue() {
        Card card = new Card();
        card.setCoinValue(9);
        assertEquals(card.getCoinValue(),9);
    }

    /**
     * Testing both methods getFrontSide() and setFrontSide()
     */
    @Test
    public void getFrontSide() {
        Card card = new Card();
        card.setFrontSideImagePath("/testResources/coin.png");

        assertEquals(card.getFrontSideImagePath(),"/testResources/coin.png");

        card.setFrontSideImagePath("/testResources/jack.png");
        assertEquals(card.getFrontSideImagePath(),"/testResources/jack.png");
    }

    /**
     * Testing both methods getBackSide() and setBackSide()
     */
    @Test
    public void getBackside() {
        Card card = new Card();

        card.setBacksideImagePath("/testResources/coin.png");
        assertEquals(card.getBackside(),"/testResources/coin.png");

        card.setBacksideImagePath("/testResources/jack.png");
        assertEquals(card.getBackside(),"/testResources/jack.png");
    }

    /**
     * Testing both methods getCardType() and setCardType()
     */
    @Test
    public void getCardType() {
        Card card = new Card();
        card.setCardType(CardType.SHIP);

        assertEquals(card.getCardType(),CardType.SHIP);
    }

    /**
     * Testing both methods getCardColour() and setCardColour()
     */
    @Test
    public void getCardColour() {
        Card card = new Card();
        card.setCardColour(CardColour.BLACK);

        assertEquals(card.getCardColour(),CardColour.BLACK);
    }

    /**
     * Testing hashcode with 3 cards
     */
    @Test
    public void hashcode() {
        Card card1 = new Card();
        card1.setCardColour(CardColour.BLACK);

        Card card2 = new Card();
        card2.setCardColour(CardColour.BLACK);

        Card card3 = new Card();
        card3.setCardName(CardName.JACK_OF_ALL_TRADES);

        assertEquals(card1.hashCode(), card2.hashCode());
        assertNotEquals(card1.hashCode(), card3.hashCode());
    }

    /**
     * Testing equals with 3 cards and null
     */
    @Test
    public void equals() {
        Card card1 = new Card();
        card1.setCardColour(CardColour.BLACK);

        Card card2 = new Card();
        card2.setCardColour(CardColour.BLACK);

        Card card3 = new Card();
        card3.setCardColour(CardColour.BLACK);
        card3.setCardName(CardName.JACK_OF_ALL_TRADES);
        Player player = new Player("Player1");

        Card card4 = new Card();
        card4.setCardColour(CardColour.BLACK);
        card4.setFrontSideImagePath("None");

        Card card5 = new Card();
        card5.setCardColour(CardColour.BLACK);
        card5.setBacksideImagePath("null");

        Card card6 = new Card();
        card6.setCardColour(CardColour.BLACK);
        card6.setVictoryPoints(3);

        Card card7 = new Card();
        card7.setCardColour(CardColour.BLUE);

        Card card8 = new Card();
        card8.setCardColour(CardColour.BLACK);
        card8.setCardType(CardType.TAX_INCREASE);

        Card card9 = new Card();
        card9.setCardColour(CardColour.BLACK);
        card9.setHouses(1);

        Card card10 = new Card();
        card10.setCardColour(CardColour.BLACK);
        card10.setCrosses(1);

        Card card11 = new Card();
        card11.setCardColour(CardColour.BLACK);
        card11.setAnchors(1);

        Card card12 = new Card();
        card12.setCardColour(CardColour.BLACK);
        card12.setSwords(1);

        Card card13 = new Card();
        card13.setCardColour(CardColour.BLACK);
        card13.setCoinValue(1);

        assertEquals(card1, card1);
        assertNotNull(card1);
        assertEquals(card1, card2);
        assertNotEquals(card3, card1);
        assertNotEquals(null, card1);
        assertNotEquals(card1, player);
        assertNotEquals(card1, card4);
        assertNotEquals(card1, card5);
        assertNotEquals(card1, card6);
        assertNotEquals(card1, card7);
        assertNotEquals(card1, card8);
        assertNotEquals(card1,card9);
        assertNotEquals(card1,card10);
        assertNotEquals(card1,card11);
        assertNotEquals(card1,card12);
        assertNotEquals(card1,card13);
    }
}