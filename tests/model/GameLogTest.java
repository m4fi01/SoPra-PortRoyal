package model;

import javafx.embed.swing.JFXPanel;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * @author Phuong Huong Nguyen
 */
public class GameLogTest {
    GameLog gameLog = new GameLog();
    Game game, game1, game2;
    Card card1 = new Card();
    Card card2 = new Card();
    ArrayList<Card> initialPile = new ArrayList<>();
    ArrayList<Card> pile = new ArrayList<>();
    DrawPile drawPile;
    Player startPlayer;
    Player activePlayer;
    Player actingPlayer;
    ArrayList<Player> playersList = new ArrayList<>();

    /**
     *  Setup before testing
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
        // initialize data for the game objects
        game = new Game(startPlayer, drawPile,null,playersList);
        game1 = new Game(actingPlayer, drawPile,null, playersList);
        game2 = new Game(activePlayer, drawPile,null, playersList);
    }

    /**
     * test method add() for the case object game is null
     */
    @Test (expected = NullPointerException.class)
    public void add_GameNull() {
        gameLog.add(null);

    }

    /**
     * Adds three different game states and checks if you can jump from one to the others.
     */
    @Test
    public void jump() {
        gameLog.add(game);
        gameLog.add(game1);
        gameLog.add(game2);
        gameLog.jump(1);
        assertEquals(game1, gameLog.get());
        assertEquals(1, gameLog.getIndex());
        gameLog.jump(0);
        assertEquals(game, gameLog.get());
        assertEquals(0, gameLog.getIndex());
        gameLog.jump(2);
        assertEquals(game2, gameLog.get());
        assertEquals(2, gameLog.getIndex());
    }

    /**
     * Adds three different game states and checks if an exception is thrown if a invalid index is used in
     * jump.
     */
    @Test (expected = IndexOutOfBoundsException.class)
    public void jumpInvalidIndexToSmall() {
        gameLog.add(game);
        gameLog.add(game1);
        gameLog.add(game2);
        gameLog.jump(-1);
    }

    /**
     * Adds three different game states and checks if an exception is thrown if a invalid index is used in
     * jump.
     */
    @Test (expected = IndexOutOfBoundsException.class)
    public void jumpInvalidIndexToBig() {
        gameLog.add(game);
        gameLog.add(game1);
        gameLog.add(game2);
        gameLog.jump(3);
    }

    /**
     * Adds two different player ids to the unworthy list to check if all works as expected.
     */
    @Test
    public void addToUnworthyList() {
        assertEquals(0,gameLog.getUnworthyPlayerIDs().size());
        gameLog.addToUnworthyList(5);
        assertEquals(1,gameLog.getUnworthyPlayerIDs().size());
        assertEquals(5,gameLog.getUnworthyPlayerIDs().get(0).intValue());
        gameLog.addToUnworthyList(7);
        assertEquals(2,gameLog.getUnworthyPlayerIDs().size());
        assertEquals(5,gameLog.getUnworthyPlayerIDs().get(0).intValue());
        assertEquals(7,gameLog.getUnworthyPlayerIDs().get(1).intValue());
        gameLog.addToUnworthyList(5);
        assertEquals(2,gameLog.getUnworthyPlayerIDs().size());
        assertEquals(5,gameLog.getUnworthyPlayerIDs().get(0).intValue());
        assertEquals(7,gameLog.getUnworthyPlayerIDs().get(1).intValue());
    }

    /**
     * test method add() for the case object game is not null
     */
    @Test
    public void add_GameNotNull(){
        gameLog.add(game);
        gameLog.add(game1);
        gameLog.getPrevious();
        gameLog.add(game2);
        assertEquals("Phuong", gameLog.get().getStartPlayer().getName());
        assertEquals("Helen", gameLog.getPrevious().getStartPlayer().getName());
    }

    /**
     * test method getPrevious for the case that the previous game doesn't exist
     */
    @Test
    public void getPrevious_PreviousNull() {
        gameLog.add(game);
        assertEquals(null, gameLog.getPrevious());
    }

    /**
     * test method getPrevious for the case that the previous game exists
     */
    @Test
    public void getPrevious_PreviousNotNull(){
        gameLog.add(game1);
        gameLog.add(game);
        assertEquals("Ngoc", gameLog.getPrevious().getStartPlayer().getName());
    }

    /**
     * test method get() for the case that there isn't a current game
     */
    @Test
    public void get_GameNull() {
        assertEquals(null, gameLog.get());
    }

    /**
     * test method get() for the case that there is a game in gameLog
     */
    @Test
    public void get_GameNotNull(){
        gameLog.add(game);
        assertEquals("Helen", gameLog.get().getStartPlayer().getName());
        assertEquals("Phuong", gameLog.get().getPlayersList().get(1).getName());
    }

    /**
     * test method getNext() for the case that hasNext() is null
     */
    @Test
    public void getNext_NextGameNull(){
        gameLog.add(game);
        gameLog.add(game1);
        gameLog.add(game2);
        assertEquals(null, gameLog.getNext());
    }
    /**
     * test method getNext() for the case that hasNext() is not null
     */
    @Test
    public void getNext_NextGameNotNull() {
        gameLog.add(game);
        gameLog.add(game1);
        gameLog.add(game2);
        gameLog.getPrevious();
        assertEquals("Phuong", gameLog.getNext().getStartPlayer().getName());
    }

    /**
     * test method hasPrevious() for the case that result is false
     */
    @Test
    public void hasPrevious_FalseCase() {
        gameLog.add(game);
        assertTrue(gameLog.hasPrevious() == false);
    }

    /**
     * test method hasPrevious for the case that result is true
     */
    @Test
    public void hasPrevious_TrueCase(){
        gameLog.add(game);
        gameLog.add(game1);
        assertTrue(gameLog.hasPrevious() == true);
    }

    /**
     * test method hasNext() for the case that result is false
     */
    @Test
    public void hasNext_FalseCase() {
        gameLog.add(game1);
        assertTrue(gameLog.hasNext() == false);
    }
    /**
     * Test jump methode in gamelog with invalid parameter
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void jumpInvalidTest(){
        gameLog.add(game1);
        gameLog.add(game1);
        gameLog.add(game1);
        gameLog.jump(-1);
        gameLog.jump(3);
    }

    /**
     * Test jump methode in gamelog with valid parameter
     */
    @Test
    public void jumpTest(){
        gameLog.add(game1);
        gameLog.add(game1);
        gameLog.add(game1);
        gameLog.jump(0);
        gameLog.jump(2);
        gameLog.jump(1);
    }

    /**
     * test method hasNext() for the case that result is true
     */
    @Test
    public void hasNext_TrueCase(){
        gameLog.add(game);
        gameLog.add(game1);
        gameLog.add(game2);
        gameLog.getPrevious();
        assertTrue(gameLog.hasNext() == true);
    }

    /**
     * test method isEmpty() for the case that result is false
     */
    @Test
    public void isEmpty_FalseCase() {
        gameLog.add(game2);
        assertTrue(gameLog.isEmpty() == false);
    }

    /**
     * test method isEmpty() for the case that result is true
     */
    @Test
    public void isEmpty_TrueCase(){
        assertTrue(gameLog.isEmpty() == true);
    }
    
}