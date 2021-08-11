package controller;

import model.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Tests the PileController
 * @author Jana Hauck
 */
public class PileControllerTest {

    private GameController gameController;
    private PileController pileController;

    /**
     * Setup before testing
     * @throws Exception exception
     */
    @Before
    public void setUp() throws Exception {
        gameController = new GameController();
        pileController = gameController.getPileController();
    }

    /**
     * Test generateInitialPile by generating a pile with null and with a pile as an ArrayList of cards
     */
    @Test
    public void generateInitialPile() {
        try {
            // Randomized pile
            DrawPile initialPile = pileController.generateInitialPile(null);

            assertEquals(initialPile.getInitialPile(), null);

            //InitialPile given
            CardController cardController = gameController.getCardController();
            ArrayList<String> allCardsAsStrings = gameController.getIOController().loadPile(new File(IOController.STANDARD_PILE_PATH));
            ArrayList<Card> cards = cardController.generatePile(allCardsAsStrings);

            DrawPile pile1 = pileController.generateInitialPile(cards);
            assertEquals(pile1.pop().getCardName(), CardName.EXPEDITION);
            assertEquals(pile1.pop().getCardName(), CardName.EXPEDITION);
            assertEquals(pile1.pop().getCardName(), CardName.EXPEDITION);
            assertEquals(pile1.pop().getCardName(), CardName.EXPEDITION);
            assertEquals(pile1.pop().getCardName(), CardName.EXPEDITION);
            assertNotEquals(pile1.pop().getCardName(), CardName.EXPEDITION); //removes expedition card when there are less than 5 players
            assertEquals(pile1.pop().getCardType(), CardType.TAX_INCREASE);
            assertEquals(pile1.pop().getCardType(), CardType.TAX_INCREASE);
            assertEquals(pile1.pop().getCardType(), CardType.TAX_INCREASE);

            assertNotEquals(pile1.getInitialPile(), null);

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Tests generateInitialPile if the ArrayList has less than 119 Cards
     */
    @Test (expected = IllegalArgumentException.class)
    public void generateInitialPileWithLessThan119Cards() {
        ArrayList<Card> pile = new ArrayList<>();
        try {
            DrawPile initialPile = pileController.generateInitialPile(pile);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Tests reorganisePile if an initial pile is given
     */
    @Test
    public void reorganisePile() {

        try {
            ArrayList<Player> players = new ArrayList<Player>();
            Player player1 = new Player("Player1");
            Player player2 = new Player("Player2");
            players.add(player1);
            players.add(player2);
            IOController ioController = gameController.getIOController();
            gameController.newGame(players, new File(ioController.STANDARD_PILE_PATH), 1);

            CardController cardController = gameController.getCardController();

            Card card1 = gameController.getGame().getDrawPile().pop();
            Card card2 = gameController.getGame().getDrawPile().pop();
            Card card3 = gameController.getGame().getDrawPile().pop();
            ArrayList<Card> discardPile = new ArrayList<>();
            discardPile.add(card2);
            discardPile.add(card1);
            discardPile.add(card3);

            DrawPile nDrawPile = pileController.reorganisePile(discardPile);

            assertEquals(nDrawPile.pop(), card1);
            assertEquals(nDrawPile.pop(), card2);
            assertEquals(nDrawPile.pop(), card3);

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    /**
     * Tests reorganise pile when the initial pile is null
     * shuffles the new pile
     */
    @Test
    public void reorganisePileInitialPileNull() {
        try {
            ArrayList<Player> players = new ArrayList<Player>();
            Player player1 = new Player("Player1");
            Player player2 = new Player("Player2");
            players.add(player1);
            players.add(player2);

            gameController.newGame(players, null, 1);
            Game game = gameController.getGame();
            Card card1 = game.getDrawPile().pop();
            Card card2 = game.getDrawPile().pop();
            Card card3 = game.getDrawPile().pop();
            ArrayList<Card> discardPile = new ArrayList<>();
            discardPile.add(card2);
            discardPile.add(card1);
            discardPile.add(card3);
            DrawPile nDrawPile = pileController.reorganisePile(discardPile);


            assertNotEquals(nDrawPile.pop(), null);
            assertNotEquals(nDrawPile.pop(), null);
            assertNotEquals(nDrawPile.pop(), null);
            assertEquals(nDrawPile.pop(), null);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests discard by removing a card from a player onto the discard pile and then checking the discard pile
     */
    @Test
    public void discard() {

        try {
            ArrayList<Player> players = new ArrayList<Player>();
            Player player1 = new Player("Player1");
            Player player2 = new Player("Player2");
            players.add(player1);
            players.add(player2);
            IOController ioController = gameController.getIOController();
            gameController.newGame(players, new File(ioController.STANDARD_PILE_PATH), 1);

            Card card1 = gameController.getGame().getDrawPile().pop();
            Card card2 = gameController.getGame().getDrawPile().pop();
            player1.addCard(card1,true);
            player2.addCard(card2,true);
            Game game = gameController.getGame();
            game.setActingPlayer(player1);
            pileController.discard(card1);

            assertTrue(game.getDiscardPile().contains(card1));

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}