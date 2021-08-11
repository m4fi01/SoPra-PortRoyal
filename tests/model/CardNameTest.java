package model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * test class for the enum CardName
 */
public class CardNameTest {


    /**
     * testing overridden methode toString()
     */
    @Test
    public void testToString() {
        assertEquals(CardName.CAPTAIN.toString(),"Kapitän");
        assertNotEquals(CardName.CAPTAIN.toString(),"Kapitäne");
        assertEquals(CardName.MADEMOISELLE.toString(),"Fräulein");
        assertEquals(CardName.FRIGATE.toString(),"Fregatte");
    }

    /**
     * testing methode toString(boolean asPlural)
     */
    @Test
    public void testToString1() {
        assertEquals(CardName.CAPTAIN.toString(true),"Kapitäne");
        assertEquals(CardName.MADEMOISELLE.toString(true),"Fräuleins");
        assertEquals(CardName.FRIGATE.toString(true),"Fregatten");
        assertEquals(CardName.CAPTAIN.toString(false),"Kapitän");
    }

    /**
     * testing methode getCardColour()
     */
    @Test
    public void getCardColour() {
        assertEquals(CardName.CAPTAIN.getCardColour(),CardColour.NONE);
        assertEquals(CardName.SETTLER.getCardColour(),CardColour.NONE);
        assertEquals(CardName.FLUTE.getCardColour(),CardColour.BLUE);
        assertEquals(CardName.GALLEON.getCardColour(),CardColour.BLACK);
    }

    /**
     * testing getCardColour when the CardColour is null
     */
    @Test (expected = UnsupportedOperationException.class)
    public void getCardColourNull() {
        CardName.TRADER.getCardColour();
    }

    /**
     * testing getCardType
     */
    @Test
    public void getCardType() {
        assertEquals(CardName.ADMIRAL.getCardType(), CardType.PERSON);
    }
}