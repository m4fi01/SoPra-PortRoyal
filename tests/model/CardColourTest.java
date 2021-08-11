package model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * testing class for enum CardColour
 */
public class CardColourTest {

    //TODO add constructor test case noun and adjective == null in constructor

    /**
     * testing methode toString, which uses noun
     */
    @Test
    public void testToString() {
        assertEquals(CardColour.RED.toString(),"Rot");
        assertEquals(CardColour.BLUE.toString(),"Blau");
        assertEquals(CardColour.BLACK.toString(),"Schwarz");
        assertNotEquals(CardColour.NONE.toString(),"Gelb");
    }
    /**
     * testing methode toString, which uses adjectives for colors
     */
    @Test
    public void testToString1() {
        assertEquals(CardColour.RED.toString(true),"rot");
        assertEquals(CardColour.BLUE.toString(true),"blau");
        assertEquals(CardColour.BLACK.toString(true),"schwarz");
        assertNotEquals(CardColour.NONE.toString(true),"gelb");
        assertEquals(CardColour.RED.toString(false), "Rot");
    }

    /**
     * testing getShipName()
     */
    @Test
    public void getShipName() {
        assertEquals(CardColour.RED.getShipName(),CardName.FRIGATE);
        assertEquals(CardColour.BLACK.getShipName(),CardName.GALLEON);
        assertNotEquals(CardColour.YELLOW.getShipName(),CardName.CAPTAIN);
        assertEquals(CardColour.BLUE.getShipName(), CardName.FLUTE);
        assertEquals(CardColour.GREEN.getShipName(), CardName.SKIFF);
    }

    /**
     * Testing getShipName if the colour is NONE
     */
    @Test (expected = UnsupportedOperationException.class)
    public void getShipNameDefault() {
        CardColour.NONE.getShipName();
    }

}