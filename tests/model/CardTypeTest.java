package model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * test class for the enum CardType
 */
public class CardTypeTest {
    /**
     * testing overridden methode toString()
     */
    @Test
    public void testToString() {
        assertEquals(CardType.PERSON.toString(),"Person");
        assertEquals(CardType.SHIP.toString(),"Schiff");
        assertEquals(CardType.EXPEDITION.toString(),"Expedition");
        assertEquals(CardType.TAX_INCREASE.toString(),"Steuererhöhung");

    }

    /**
     * testing methode toString(boolean asPlural)
     */
    @Test
    public void testToString1() {
        assertEquals(CardType.PERSON.toString(true),"Personen");
        assertEquals(CardType.SHIP.toString(true),"Schiffe");
        assertEquals(CardType.EXPEDITION.toString(true),"Expeditionen");
        assertEquals(CardType.TAX_INCREASE.toString(true),"Steuererhöhungen");

        assertEquals(CardType.SHIP.toString(false),"Schiff");
        assertEquals(CardType.EXPEDITION.toString(false),"Expedition");
    }
}