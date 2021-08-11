package controller;

import model.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * @author Mohammad Almidani
 *
 * test Class for the PlayerController
 */
public class PlayerControllerTest {

    private GameController gameController;
    private PlayerController playerController;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private Player player5;

    /**
     * setUp methode to initialize required Attributes for the test
     * @throws Exception exception
     */
    @Before
    public void setUp() throws Exception {
        // Creates a new and empty high score file for testing.
        File highScoreFile = new File(IOController.HIGH_SCORE_PATH);
        if(highScoreFile.exists()) {
            Files.delete(highScoreFile.toPath());
        }
        highScoreFile.createNewFile();
        highScoreFile.deleteOnExit(); // Deletes the newly created file after testing.

        gameController = new GameController();
        ArrayList<Player> players = new ArrayList<>();
        player1= new Player("one");
        players.add(player1);
        gameController.newGame(players,null,500);
        playerController = gameController.getPlayerController();

        player2 = new Player("two");
        player3 = new Player("three");
        player4 = new Player("four");
        player5 = new Player("five");
    }

    /**
     * testing methode addPlayer()
     * needs a parameter Player player
     */
    @Test
    public void addPlayer() {
        try {
            playerController.addPlayer(player2);
            playerController.addPlayer(player3);
            playerController.addPlayer(player4);
            playerController.addPlayer(player5);

            assertEquals(gameController.getGame().getPlayersList().get(0),player1);
            assertEquals(gameController.getGame().getPlayersList().get(1),player2);
            assertEquals(gameController.getGame().getPlayersList().get(2),player3);
            assertEquals(gameController.getGame().getPlayersList().get(3),player4);
            assertEquals(gameController.getGame().getPlayersList().get(4),player5);

            GameController gameController1 = new GameController();
            PlayerController playerController1 = new PlayerController(gameController1);
            assertFalse(playerController1.addPlayer(player1));
        } catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    /**
     * testing methode AddAI()
     * takes two parameters, String aiName and Level level
     */
    @Test
    public void addAI() {
        try {
            assertTrue(playerController.addAI("Noobie", Level.EASY));
            assertTrue(playerController.addAI("Normal", Level.MEDIUM));
            assertTrue(playerController.addAI("WhySoEZ", Level.HARD));

            assertEquals(gameController.getGame().getPlayersList().get(1).getName(),"Noobie");
            assertEquals(gameController.getGame().getPlayersList().get(2).getName(),"Normal");
            assertEquals(gameController.getGame().getPlayersList().get(3).getName(),"WhySoEZ");

            GameController gameController1 = new GameController();
            PlayerController playerController1 = new PlayerController(gameController1);
            assertFalse(playerController1.addAI("hi wanna play?", Level.MEDIUM));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * testing methode removePlayer() by adding and then removing players
     */
    @Test
    public void removePlayer() {
        playerController.addPlayer(player1);
        playerController.addPlayer(player2);

        assertTrue(playerController.removePlayer(player1)); //player1 was already added by initializing game
        assertTrue(playerController.removePlayer(player1));
        assertEquals(gameController.getGame().getPlayersList().get(0),player2);
        assertTrue(playerController.removePlayer(player2));
        assertTrue(gameController.getGame().getPlayersList().isEmpty());
        assertFalse(playerController.removePlayer(null));
    }

    /**
     * testing methode checkWinCondition() by giving a player cards to win the game and checking the win condition
     */
    @Test
    public void checkWinCondition() {
        Card card = new Card();
        card.setCardName(CardName.CAPTAIN);
        card.setVictoryPoints(12);
        player2.addCard(card,true);

        playerController.addPlayer(player2);
        assertTrue(playerController.checkWinCondition());

        playerController.removePlayer(player2);
        player2.removeCard(card);
        playerController.addPlayer(player2);
        assertFalse(playerController.checkWinCondition());
    }

    /**
     * testing methode nextActingPlayer() by adding five players and then calling nextActingPlayer
     */
    @Test
    public void nextActingPlayer() {
        playerController.addPlayer(player2);
        playerController.addPlayer(player3);
        playerController.addPlayer(player4);
        playerController.addPlayer(player5);

        //the acting player now is player1
        Player player = playerController.nextActingPlayer();
        assertEquals(player,player2);

        player = playerController.nextActingPlayer();
        assertEquals(player,player3);

        player = playerController.nextActingPlayer();
        assertEquals(player,player4);

        player = playerController.nextActingPlayer();
        assertEquals(player,player5);

        player = playerController.nextActingPlayer();
        assertEquals(player,player1);
    }

    /**
     * testing methode nextActivePlayer() by adding four players and then calling nextActivePlayer
     */
    @Test
    public void nextActivePlayer() {
        playerController.addPlayer(player2);
        playerController.addPlayer(player3);
        playerController.addPlayer(player4);
        playerController.addPlayer(player5);

        assertEquals(playerController.nextActivePlayer(),player2);
        assertEquals(playerController.nextActivePlayer(),player3);
        assertEquals(playerController.nextActivePlayer(),player4);
        assertEquals(playerController.nextActivePlayer(),player5);
        assertEquals(playerController.nextActivePlayer(),player1);
    }

    /**
     * testing methode transferCoinTo() by adding coins to one player and then calling transferCoinTo to give a coin to another player
     */
    @Test
    public void transferCoinTo() {
        playerController.addPlayer(player2);
        Card card = new Card();
        player2.addCard(card,false);
        player2.addCard(card,false);
        playerController.nextActingPlayer();
        playerController.transferCoinTo();
        playerController.transferCoinTo();
        assertEquals(player1.getCoinsCount(),2+3);
        assertEquals(player2.getCoinsCount(),0);
        try{
            playerController.transferCoinTo();
        }
        catch (IllegalArgumentException illegalArgumentException){
            assertEquals(0,0);
        }
    }

    /**
     * testing methode discardCoins(int amount) by adding cards to a player an then calling discardCoins to remove those coins
     */
    @Test
    public void discardCoins() {
        Card card = new Card();
        card.setCardName(CardName.CAPTAIN);
        player1.addCard(card,false);
        Card card1 = new Card();
        card1.setCardName(CardName.ADMIRAL);
        player1.addCard(card1,false);
        int amount = gameController.getGame().getDiscardPile().size();
        playerController.discardCoins(1);
        assertEquals(gameController.getGame().getDiscardPile().size(),amount+1);
        assertEquals(player1.getCoinsCount(),1+3);
        playerController.discardCoins(1);
        assertEquals(player1.getCoinsCount(),3);

        assertEquals(gameController.getGame().getDiscardPile().size(),amount+2);

        player1.addCard(card,false);
        player1.addCard(card1,false);

        playerController.discardCoins(2);
        assertEquals(player1.getCoinsCount(),3);

        playerController.discardCoins(1); // an IllegalArgumentException should be thrown
    }

    /**
     * testing methode discardCoins(Player player, int amount)
     */
    @Test (expected = IllegalArgumentException.class)
    public void testDiscardCoins() {
        playerController.addPlayer(player2);
        playerController.addPlayer(player3);
        Card card = new Card();
        card.setCardName(CardName.CAPTAIN);
        player1.addCard(card,false);
        Card card1 = new Card();
        card1.setCardName(CardName.ADMIRAL);
        player2.addCard(card1,false);
        player2.addCard(card,false);

        player3.addCard(card,false);
        player3.addCard(card1,false);
        player3.addCard(card,false);
        int amount = gameController.getGame().getDiscardPile().size();
        playerController.discardCoins(player3, 2);
        assertEquals(player3.getCoinsCount(), 1);
        assertEquals(gameController.getGame().getDiscardPile().size(), amount+2);

        playerController.discardCoins(player1, 1);
        assertEquals(player1.getCoinsCount(), 3);
        assertEquals(gameController.getGame().getDiscardPile().size(), amount+3);

        playerController.discardCoins(player2, 2);
        assertEquals(player2.getCoinsCount(), 0);
        assertEquals(gameController.getGame().getDiscardPile().size(),amount+5);


        playerController.discardCoins(player2, 1); // an IllegalArgumentException should be thrown
    }

    /**
     * testing methode drawCoins() by drawing coins for the active player and then checking the coin count
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void drawCoins() {
        try {
        playerController.drawCoins(1);
        assertEquals(gameController.getGame().getActivePlayer().getCoinsCount(),1+3);
        playerController.drawCoins(3);
        assertEquals(gameController.getGame().getActivePlayer().getCoinsCount(),4+3);
        playerController.drawCoins(112);
        assertEquals(gameController.getGame().getActivePlayer().getCoinsCount(), 112+4+3);
        Card coin = new Card ();
        coin.setCardName(CardName.PIRATE);
        gameController.getGame().addToDiscardPile(coin);
        gameController.getGame().addToDiscardPile(coin);
        playerController.drawCoins(2);
        assertEquals(gameController.getGame().getActivePlayer().getCoinsCount(), 121);
        playerController.drawCoins(1); //should throw IndexOutOfBoundException
        }catch (IndexOutOfBoundsException e){
            throw new IndexOutOfBoundsException();
        }
    }
}