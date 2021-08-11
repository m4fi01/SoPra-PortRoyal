package model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Test Class for the model Class DrawPile
 */
public class DrawPileTest {

    /**
     * Creates and copies draw piles to test the copy constructor.
     */
    @Test
    public void copyConstructor() {
        ArrayList<Card> initialPile = new ArrayList<>();
        ArrayList<Card> pile = new ArrayList<>();
        Card card = new Card();
        initialPile.add(card);
        pile.add(card);
        DrawPile randomPile = new DrawPile(null,pile);
        DrawPile orderedPile = new DrawPile(initialPile,pile);
        Card finalExpedition = new Card();
        finalExpedition.setCardType(CardType.EXPEDITION);
        randomPile.setFinalExpedition(finalExpedition);

        DrawPile copiedRandomPile = new DrawPile(randomPile);
        DrawPile copiedOrderedPile = new DrawPile(orderedPile);
        assertEquals(randomPile,copiedRandomPile);
        assertEquals(orderedPile,copiedOrderedPile);
    }

    /**
     * Testing methode getInitialPile()
     */
    @Test
    public void getInitialPile() {
        DrawPile pile = new DrawPile(new ArrayList<>(),new ArrayList<>());
        ArrayList<Card> testPile = pile.getInitialPile();

        assertEquals(testPile,pile.getInitialPile());
    }

    /**
     * Testing overridden methode pop() by generating cards, creating a new pile with those cards and
     * then checking the returned cards when using pop().
     */
    @Test
    public void pop() {
        ArrayList<Card> testPile = new ArrayList<>();
        Card card = new Card();
        card.setCardName(CardName.CAPTAIN);
        testPile.add(card);

        Card card1 = new Card();
        card1.setCoinValue(4);
        testPile.add(card1);

        DrawPile pile = new DrawPile(new ArrayList<>(),testPile);

        assertEquals(pile.pop(),card);
        assertEquals(pile.pop(),card1);
        assertNull(pile.pop());
    }

    /**
     * Sets a card as final expedition for a draw pile and checks if it is returned when getFinalExpedition is called.
     */
    @Test
    public void getFinalExpedition() {
        ArrayList<Card> pile = new ArrayList<>();
        DrawPile drawPile = new DrawPile(null,pile);
        Card finalExpedition = new Card();
        drawPile.setFinalExpedition(finalExpedition);
        finalExpedition.setCardColour(CardColour.RED);
        assertEquals(finalExpedition,drawPile.getFinalExpedition());
    }
}