package model;

import javafx.embed.swing.JFXPanel;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Tests the class Game
 * @author Phuong Huong Nguyen
 */
public class GameTest {

    //TODO add test for lines 200 and 201

    /**
     * the Attributes needed for the test
     */
    Game game;
    Card card1 = new Card();
    Card card2 = new Card();
    ArrayList<Card> initialPile = new ArrayList<>();
    ArrayList<Card> pile = new ArrayList<>();
    DrawPile drawPile;
    Player startPlayer;
    Player activePlayer;
    Player actingPlayer;
    ArrayList<Player> playersList = new ArrayList<>();
    ArrayList<Card> discardPile = new ArrayList<>();
    Card cardDiscard1 = new Card();
    Card cardDiscard2 = new Card();
    ArrayList<Card> harborDisplay = new ArrayList<>();
    ArrayList<Card> expeditionDisplay = new ArrayList<>();
    Card expedition1 = new Card();
    Card expedition2 = new Card();
    ArrayList<String> actionLog = new ArrayList<>();


    /**
     * SetUp for the Test. Generates two cards, a drawPile, a playersList and the game.
     * Then initializes the initial pile, pile, draw pile, discard pile, players, playersList, game, harbor display,
     *  expedition display and the log.
     * @throws Exception exception
     */
    @Before
    public void setUp() throws Exception {
        // Initialize data for card1, card2, drawPile, playersList and game;
        card1.setCardName(CardName.TRADER);
        card1.setCardType(CardType.PERSON);
        card1.setCardColour(CardColour.YELLOW);
        card1.setCoinValue(1);
        card1.setVictoryPoints(1);
        card2.setCardName(CardName.CAPTAIN);
        card2.setCardType(CardType.PERSON);
        card2.setVictoryPoints(1);
        card2.setAnchors(1);
        card2.setCoinValue(4);
        // initialize data for initalPile, pile and drawPile
        initialPile.add(card1);
        initialPile.add(card2);
        pile.add(card1);
        pile.add(card2);
        drawPile = new DrawPile(initialPile, pile);
        // initialize data for players and playersList
        startPlayer = new Player("Helen");
        activePlayer = new Player("Phuong");
        actingPlayer = new Player("Ngoc");
        playersList.add(startPlayer);
        playersList.add(activePlayer);
        playersList.add(actingPlayer);
        // initialize data for the game object
        game = new Game(startPlayer, drawPile,null, playersList);
        // initialize data for the discardPile
        cardDiscard1.setCardName(CardName.PIRATE);
        cardDiscard1.setCardType(CardType.PERSON);
        cardDiscard1.setVictoryPoints(1);
        cardDiscard1.setCoinValue(1);
        cardDiscard1.setSwords(2);
        cardDiscard2.setCardName(CardName.JACK_OF_ALL_TRADES);
        cardDiscard2.setCardType(CardType.PERSON);
        cardDiscard2.setVictoryPoints(1);
        cardDiscard2.setCoinValue(6);
        discardPile.add(cardDiscard1);
        discardPile.add(cardDiscard2);
        // initialize data for the harborDisplay
        harborDisplay.add(card1);
        harborDisplay.add(card2);
        // initialize data for the expeditionDisplay
        expedition1.setCardName(CardName.EXPEDITION);
        expedition1.setCardType(CardType.EXPEDITION);
        expedition1.setVictoryPoints(1);
        expedition1.setCoinValue(2);
        expedition1.setSwords(2);
        expedition2.setCardName(CardName.EXPEDITION);
        expedition2.setCardType(CardType.EXPEDITION);
        expedition2.setVictoryPoints(1);
        expedition2.setCoinValue(2);
        expedition2.setCrosses(2);
        expeditionDisplay.add(expedition1);
        expeditionDisplay.add(expedition2);
        // initialize data for the actionLog
        actionLog.add("log");
        actionLog.add("log");

    }

    /**
     * Tests method setActivePlayer()
     */
    @Test
    public void setActivePlayer() {
        game.setActivePlayer(activePlayer);
        assertEquals("Phuong" ,game.getActivePlayer().getName());
    }

    /**
     * Tests method setActingPlayer()
     */
    @Test
    public void setActingPlayer() {
        game.setActingPlayer(actingPlayer);
        assertEquals("Ngoc", game.getActingPlayer().getName());
    }

    /**
     * Tests method setDrawPile()
     */
    @Test
    public void setDrawPile() {
        game.setDrawPile(drawPile);
        assertTrue(game.getDrawPile().getInitialPile().size() == 2);
        assertEquals(CardName.CAPTAIN, game.getDrawPile().getInitialPile().get(1).getCardName());
    }

    /**
     * Tests setLastCardDrawn()
     */
    @Test
    public void setLastCardDrawn() {
        game.setLastCardDrawn(card1);
        assertEquals(CardType.PERSON, game.getLastCardDrawn().getCardType());
    }

    /**
     * Tests setDiscardPile()
     */
    @Test
    public void setDiscardPile() {
        game.setDiscardPile(discardPile);
        assertTrue(game.getDiscardPile().size()==2);
        assertEquals(CardName.JACK_OF_ALL_TRADES, game.getDiscardPile().get(1).getCardName());
    }

    /**
     * Tests method setHarborDisplay()
     */
    @Test
    public void setHarborDisplay() {
        game.setHarborDisplay(harborDisplay);
        assertTrue(game.getHarborDisplay().size() == 2);
        assertEquals(CardName.TRADER, game.getHarborDisplay().get(0).getCardName());
    }

    /**
     * Tests method setExpeditionDisplay()
     */
    @Test
    public void setExpeditionDisplay() {
        game.setExpeditionDisplay(expeditionDisplay);
        assertTrue(game.getExpeditionDisplay().size() == 2);
        assertTrue(game.getExpeditionDisplay().get(0).getSwords() == 2);

    }

    /**
     * Tests method setPlayerList()
     */
    @Test
    public void setPlayersList() {
        game.setPlayersList(playersList);
        assertTrue(game.getPlayersList().size() == 3);
        assertEquals("Helen", game.getStartPlayer().getName());
    }

    /**
     * Tests method addToDiscardPile()
     */
    @Test
    public void addToDiscardPile() {
        game.setDiscardPile(discardPile);
        game.addToDiscardPile(card1);
        assertTrue(game.getDiscardPile().size() == 3);
        assertEquals(CardName.TRADER, game.getDiscardPile().get(2).getCardName());
    }

    /**
     * Tests method addToHarborDisplay()
     */
    @Test
    public void addToHarborDisplay() {
        game.setHarborDisplay(harborDisplay);
        game.addToHarborDisplay(card1);
        assertTrue(game.getHarborDisplay().size() == 3);
        assertEquals(CardType.PERSON, game.getHarborDisplay().get(2).getCardType());
    }

    /**
     * Tests method addToExpeditionDisplay()
     */
    @Test
    public void addToExpeditionDisplay() {
        game.setExpeditionDisplay(expeditionDisplay);
        Card expedition3 = new Card();
        expedition3.setCardName(CardName.EXPEDITION);
        expedition3.setCardType(CardType.EXPEDITION);
        expedition3.setVictoryPoints(1);
        expedition3.setCoinValue(2);
        expedition3.setHouses(2);
        game.addToExpeditionDisplay(expedition3);
        assertTrue(game.getExpeditionDisplay().size() == 3);
        assertEquals(2, game.getExpeditionDisplay().get(2).getHouses());
    }

    /**
     * Tests method addToPlayersList()
     */
    @Test
    public void addToPlayersList() {
        game.setPlayersList(playersList);
        Player player4 = new Player("abc");
        game.addToPlayersList(player4);
        assertTrue(game.getPlayersList().size() == 4);
        assertEquals("abc", game.getPlayersList().get(3).getName());
    }

    /**
     * Tests method getDiscardPile by setting the discard pile and then checking the size and the first card on the pile
     */
    @Test
    public void getDiscardPile() {
        game.setDiscardPile(discardPile);
        assertTrue(game.getDiscardPile().size() == 2);
        assertEquals(CardName.JACK_OF_ALL_TRADES, game.getDiscardPile().get(1).getCardName());
    }

    /**
     * Tests getActingPlayer by setting the acting player and then checking the name
     */
    @Test
    public void getActingPlayer() {
        game.setActingPlayer(actingPlayer);
        assertEquals("Ngoc", game.getActingPlayer().getName());
    }

    /**
     * Tests getDrawPile by setting the draw pile and then checking the size and the first card on the pile
     */
    @Test
    public void getDrawPile() {
        game.setDrawPile(drawPile);
        assertTrue(game.getDrawPile().getInitialPile().size() == 2);
        assertEquals(CardName.CAPTAIN, game.getDrawPile().getInitialPile().get(1).getCardName());
    }

    /**
     * Tests getHarborDisplay by setting the harbor display and then checking the size and the first card in the display
     */
    @Test
    public void getHarborDisplay() {
        game.setHarborDisplay(harborDisplay);
        assertTrue(game.getHarborDisplay().size() == 2);
        assertEquals(CardName.TRADER, game.getHarborDisplay().get(0).getCardName());
    }

    /**
     * Tests getExpeditionDisplay by setting the harbor display and then checking the size and the first card in the display
     */
    @Test
    public void getExpeditionDisplay() {
        game.setExpeditionDisplay(expeditionDisplay);
        assertTrue(game.getExpeditionDisplay().size() == 2);
        assertTrue(game.getExpeditionDisplay().get(0).getSwords() == 2);
    }

    /**
     * Tests getPlayersList by setting the players list and then checking the size and the name of the start player
     */
    @Test
    public void getPlayersList() {
        game.setPlayersList(playersList);
        assertTrue(game.getPlayersList().size() == 3);
        assertEquals("Helen", game.getStartPlayer().getName());
    }

    /**
     * Tests getStartPlayer by checking the name of the start player
     */
    @Test
    public void getStartPlayer() {
        assertEquals("Helen", game.getStartPlayer().getName());
    }

    /**
     * Tests getActivePlayer by setting the active player and then checking the name
     */
    @Test
    public void getActivePlayer() {
        game.setActivePlayer(activePlayer);
        assertEquals("Phuong", game.getActivePlayer().getName());
    }

    /**
     * Tests method getActionPoints()
     */
    @Test
    public void getActionPoints() {
        assertTrue(game.getActionPoints() == 0);
        game.addActionPoints(33);
        assertTrue(game.getActionPoints() == 33);
    }

    /**
     * Tests getLastCardDrawn by setting it and then checking the name, swords, victory points and the coin value
     */
    @Test
    public void getLastCardDrawn() {
        game.setLastCardDrawn(expedition1);
        assertEquals(CardName.EXPEDITION, game.getLastCardDrawn().getCardName());
        assertTrue(game.getLastCardDrawn().getSwords() == 2);
        assertTrue(game.getLastCardDrawn().getVictoryPoints() == 1);
        assertTrue(game.getLastCardDrawn().getCoinValue() == 2);
    }

    /**
     * Test method removeFromPlayersList()
     */
    @Test
    public void removeFromPlayersList() {
        game.setPlayersList(playersList);
        game.removeFromPlayersList(game.getPlayersList().get(0).getPlayerID());
        assertTrue(game.getPlayersList().size() == 2);
        assertEquals("Phuong", game.getPlayersList().get(0).getName());
        assertEquals("Ngoc", game.getPlayersList().get(1).getName());
    }

    /**
     * Tests removeFromHarborDisplay by setting the it, then removing the card at index 1
     * and checking the size and the first card in the harbor display
     */
    @Test
    public void removeFromHarborDisplay() {
        game.setHarborDisplay(harborDisplay);
        game.removeFromHarborDisplay(1);
        assertTrue(game.getHarborDisplay().size() == 1);
        assertEquals(CardName.TRADER, game.getHarborDisplay().get(0).getCardName());
        assertTrue(game.getHarborDisplay().get(0).getVictoryPoints() == 1);
    }

    /**
     * Tests method addActionPoints()
     */
    @Test
    public void addActionPoints() {
        game.addActionPoints(44);
        assertTrue(game.getActionPoints() == 44);
    }

    /**
     * Test get currentGamePhase
     */
    @Test
    public void getCurrentGamePhaseTest(){
        assertEquals(game.getCurrentGamePhase(),GamePhase.DISCOVER);
        game.setCurrentGamePhase(GamePhase.TRADE_AND_HIRE);
        assertEquals(game.getCurrentGamePhase(),GamePhase.TRADE_AND_HIRE);
    }
    /**
     * Test method add action to log
     */
    @Test
    public void addActionToLog(){
        int size = game.getActionLog().size();
        game.addActionToLog("Action");
        assertEquals(game.getActionLog().size(),size+1);
        assertEquals(game.getActionLog().get(size),"Action");

    }
    /**
     * Test is face up
     */
    @Test
    public void testIsFaceUp(){
        assertEquals(game.isFaceUp(card1),false);
        game.setFaceUp(card1,true);
        assertEquals(game.isFaceUp(card1), true);
    }

    /**
     * Tests previous game with AI
     */
    @Test
    public void testPreviousGameWithAI(){
        Card expedition = new Card();
        expedition.setCardName(CardName.EXPEDITION);
        AIPlayer aiPlayer1 = new AIPlayer(Level.EASY,"test");
        AIPlayer aiPlayer2 = new AIPlayer(Level.EASY,"test");
        ArrayList<Player> playerAIArrayList = new ArrayList<Player>();
        playerAIArrayList.add(aiPlayer1);
        playerAIArrayList.add(aiPlayer2);
        Game game2 = new Game(aiPlayer1, drawPile, null, playersList);//
        Game game2ex = new Game(aiPlayer1, drawPile, expedition, playerAIArrayList);
        Game game3 = new Game(game2);
        Game game4 = new Game(game2ex);

    }
}