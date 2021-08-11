package controller;

import model.CardColour;
import model.CardName;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for CardFactory
 */
public class CardFactoryTest {

    /**
     * Tests generatePerson when the card name is null
     */
    @Test (expected = NullPointerException.class)
    public void generatePersonCardNameNull() {
        CardFactory factory = new CardFactory();
        factory.generatePerson(null, null, 0, 0);
    }

    /**
     * Tests generatePerson when the card colour is null
     */
    @Test (expected = NullPointerException.class)
    public void generatePersonCardColourNull() {
        CardFactory factory = new CardFactory();
        factory.generatePerson(CardName.ADMIRAL, null, 0, 0);
    }

    /**
     * Tests generatePerson when the card name does not have the card type person
     */
    @Test (expected = IllegalArgumentException.class)
    public void generatePersonCardNameNotAPerson() {
        CardFactory factory = new CardFactory();
        factory.generatePerson(CardName.CHARITY, CardColour.BLACK, 0, 0);
    }

    /**
     * Tests generatePerson when the trader does not have a colour
     */
    @Test (expected = IllegalArgumentException.class)
    public void generatePersonTraderWithoutColour() {
        CardFactory factory = new CardFactory();
        factory.generatePerson(CardName.TRADER, CardColour.NONE, 0, 0);
    }

    /**
     * Tests generateShip when the card colour is null
     */
    @Test (expected = NullPointerException.class)
    public void generateShipCardColourNull() {
        CardFactory factory = new CardFactory();
        factory.generateShip(null, 0,0);
    }


    /**
     * Tests generateTaxIncrease when the card name is null
     */
    @Test (expected = NullPointerException.class)
    public void generateTaxIncreaseCardNameNull() {
        CardFactory factory = new CardFactory();
        factory.generateTaxIncrease(null);
    }

    /**
     * Tests generateTaxIncrease when the card name does not have the card type tax increase
     */
    @Test (expected = IllegalArgumentException.class)
    public void generateTaxIncreaseCardNameNotATaxIncrease() {
        CardFactory factory = new CardFactory();
        factory.generateTaxIncrease(CardName.ADMIRAL);
    }
}