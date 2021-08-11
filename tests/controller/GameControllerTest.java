package controller;

import ai.AIController;
import model.*;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Test GameController.java
 * @author Phuong Huong Nguyen
 */
public class GameControllerTest {
    private GameController gameController;
    private Game game;
    // initializing some kind of cards
    Card card1 = new Card();
    Card card2 = new Card();
    Card ship1 = new Card();
    Card ship2 = new Card();
    Card ship3 = new Card();
    DrawPile drawPile;
    Player startPlayer;
    Player activePlayer;
    Player actingPlayer;
    ArrayList<Player> playersList = new ArrayList<>();
    ArrayList<Card> initialPile = new ArrayList<>();
    ArrayList<Card> pile = new ArrayList<>();
    ArrayList<Card> harborDisplay = new ArrayList<>();
    // create cards for test method endGame()
    Card cardCoin1 = new Card();
    Card cardCoin2 = new Card();
    Card cardCoin3 = new Card();
    Card cardCoin4 = new Card();
    Card cardCoin5 = new Card();
    Card cardCoin6 = new Card();
    Card cardCoin7 = new Card();
    Card cardCoin8 = new Card();
    Card cardCoin9 = new Card();
    Card cardCoin10 = new Card();
    Card cardCoin11 = new Card();
    Card cardCoin12 = new Card();
    Card cardCoin13 = new Card();
    Card cardFace1 = new Card();
    Card cardFace2 = new Card();
    Card cardFace3 = new Card();
    Card cardFace4 = new Card();

    /**
     * Initializing data before testing
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

        // Initialize data for cards, drawPile, playersList and game;
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
        ship1.setCardName(CardName.PINNACE);
        ship1.setCardType(CardType.SHIP);
        ship1.setCardColour(CardColour.YELLOW);
        ship1.setCoinValue(1);
        ship2.setCardName(CardName.PINNACE);
        ship2.setCardType(CardType.SHIP);
        ship2.setCardColour(CardColour.YELLOW);
        ship2.setCoinValue(2);
        ship3.setCardName(CardName.PINNACE);
        ship3.setCardType(CardType.SHIP);
        ship3.setCardColour(CardColour.YELLOW);
        ship3.setCoinValue(3);
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
        // initialize gameController object and
        gameController = new GameController();
        gameController.newGame(playersList, null, 500);
        // initializing data for the harbor display with only one yellow ship card
        harborDisplay.add(card1);
        harborDisplay.add(card2);
        harborDisplay.add(ship1);
        // initialize Cards with the faceUp true
        cardFace1.setCardName(CardName.PIRATE);
        cardFace1.setCardType(CardType.PERSON);
        cardFace1.setVictoryPoints(5);
        cardFace1.setSwords(2);
        cardFace1.setHouses(0);
        cardFace1.setCrosses(0);
        cardFace2.setCardName(CardName.JACK_OF_ALL_TRADES);
        cardFace2.setCardType(CardType.PERSON);
        cardFace2.setVictoryPoints(1);
        cardFace2.setCrosses(1);
        cardFace2.setHouses(1);
        cardFace2.setAnchors(1);
        cardFace3.setCardName(CardName.EXPEDITION);
        cardFace3.setCardType(CardType.EXPEDITION);
        cardFace3.setVictoryPoints(6);
        cardFace3.setHouses(2);
        cardFace3.setCrosses(0);
        cardFace3.setAnchors(0);
        cardFace4.setCardName(CardName.EXPEDITION);
        cardFace4.setCardType(CardType.EXPEDITION);
        cardFace4.setVictoryPoints(6);
        cardFace4.setCrosses(2);
        cardFace4.setHouses(1);
        cardFace4.setAnchors(0);
    }

    /**
     * test method getHighScore() for the case that the high score list is empty
     */
    @Test
    public void getHighScore_HighScoreListNull() {
        assertTrue(gameController.getHighScore().size() == 0);
    }

    /**
     * test method getGame()
     */
    @Test
    public void getGame() {
        assertEquals("Helen", gameController.getGame().getStartPlayer().getName());
    }

    /**
     * test method getGameLog()
     */
    @Test
    public void getGameLog() {
        gameController.getGameLog().add(game);  // add object game = new Game(startPlayer, drawPile, playersList)
        assertEquals("Ngoc", gameController.getGameLog().get().getPlayersList().get(2).getName());
    }

    /**
     * test method checkShipColours() for the case that there is less than 2 ship cards with the same colours in the harbor display
     */
    @Test
    public void checkShipColours_LessThanTwo() {
        gameController.getGame().setHarborDisplay(harborDisplay); // add the harbor display including only one yellow ship card the the game
        assertTrue(gameController.checkShipColours() == false);

    }

    /**
     * test method checkShipColours() for the case that there are two yellow ship cards in the harbor display
     */
    @Test
    public void checkShipColours_TwoYellowShipCard(){
        harborDisplay.add(ship2);        // add the second yellow ship card into the harbor display
        gameController.getGame().setHarborDisplay(harborDisplay);
        assertTrue(gameController.checkShipColours() == true);
    }

    /**
     * test method checkShipColours() for the case that there are three yellow ship cards in the harbor display
     */
    @Test
    public void checkShipColours_ThreeYellowShipCard(){
        harborDisplay.add(ship2);        // add the second yellow ship card into the harbor display
        harborDisplay.add(ship3);        // add the third yellow ship card into the harbor display
        gameController.getGame().setHarborDisplay(harborDisplay);
        assertTrue(gameController.checkShipColours() == true);
    }

    /**
     * test method endGame() for the case that there is only one player gained 12 victory points
     */
    @Test
    public void endGame_OnePersonGets12VictoryPoints() {
        // The first player gained 12 victory points and 3 coins.
        gameController.getGame().getPlayersList().get(0).addCard(cardCoin1,false);
        gameController.getGame().getPlayersList().get(0).addCard(cardCoin2,false);
        gameController.getGame().getPlayersList().get(0).addCard(cardCoin3,false);
        gameController.getGame().getPlayersList().get(0).addCard(cardFace1,false);
        gameController.getGame().getPlayersList().get(0).addCard(cardFace2,false);
        gameController.getGame().getPlayersList().get(0).addCard(cardFace3,false);
        // The second player gained 12 victory points and 2 coins.
        gameController.getGame().getPlayersList().get(1).addCard(cardCoin4,false);
        gameController.getGame().getPlayersList().get(1).addCard(cardCoin5,false);
        gameController.getGame().getPlayersList().get(1).addCard(cardFace4,false);
        // The third player gained 6 victory points and 1 coin.
        gameController.getGame().getPlayersList().get(2).addCard(cardCoin6,false);
        gameController.getGame().getPlayersList().get(2).addCard(cardFace3,false);
        // Testing of method endGame():
        gameController.endGame();
        // Because of setUp() the highScore list was empty and should now contain only Helen as the best player.
        assertTrue(gameController.getHighScore().get(0).contains("Helen"));
    }

    /**
     * test method endGame() for the case that there are two players gained 12 victory points and different coins
     */
    @Test
    public void endGame_TwoPeopleGet12VictoryPointsAndDifferentNumberOfCoins() {
        // The first player gained 12 victory points and 3 coins.
        gameController.getGame().getPlayersList().get(0).addCard(cardCoin1,false);
        gameController.getGame().getPlayersList().get(0).addCard(cardCoin2,false);
        gameController.getGame().getPlayersList().get(0).addCard(cardCoin3,false);
        gameController.getGame().getPlayersList().get(0).addCard(cardFace1,true);
        gameController.getGame().getPlayersList().get(0).addCard(cardFace2,true);
        gameController.getGame().getPlayersList().get(0).addCard(cardFace3,true);
        // The second player gained 12 victory points and 5 coins.
        gameController.getGame().getPlayersList().get(1).addCard(cardCoin4,false);
        gameController.getGame().getPlayersList().get(1).addCard(cardCoin5,false);
        gameController.getGame().getPlayersList().get(1).addCard(cardCoin6,false);
        gameController.getGame().getPlayersList().get(1).addCard(cardCoin7,false);
        gameController.getGame().getPlayersList().get(1).addCard(cardCoin8,false);
        gameController.getGame().getPlayersList().get(1).addCard(cardFace3,true);
        gameController.getGame().getPlayersList().get(1).addCard(cardFace4,true);
        // The third player gained 6 points and 1 coin.
        gameController.getGame().getPlayersList().get(2).addCard(cardCoin6,false);
        gameController.getGame().getPlayersList().get(2).addCard(cardFace3,true);
        // Testing of method endGame():
        gameController.endGame();
        // Because of setUp() the highScore list was empty and should now contain only Phuong as the best player.
        assertEquals(1,gameController.getHighScore().size());
        assertTrue(gameController.getHighScore().get(0).contains("Phuong"));
    }

    /**
     * test method endGame() for the case that there are two players gained 12 victory points and the same coins
     */
    @Test
    public void endGame_TwoPeopleGet12VictoryPointsAndEqualNumberOfCoins() {
        // The first player gained 6 points and 1 coin.
        gameController.getGame().getPlayersList().get(0).addCard(cardCoin6,false);
        gameController.getGame().getPlayersList().get(0).addCard(cardFace3,true);
        // The second player gained 12 victory points and 5 coins.
        gameController.getGame().getPlayersList().get(1).addCard(cardCoin4,false);
        gameController.getGame().getPlayersList().get(1).addCard(cardCoin5,false);
        gameController.getGame().getPlayersList().get(1).addCard(cardCoin6,false);
        gameController.getGame().getPlayersList().get(1).addCard(cardCoin7,false);
        gameController.getGame().getPlayersList().get(1).addCard(cardCoin8,false);
        gameController.getGame().getPlayersList().get(1).addCard(cardFace4,true);
        gameController.getGame().getPlayersList().get(1).addCard(cardFace3,true);
        // The third player gained 12 victory points and 5 coins.
        gameController.getGame().getPlayersList().get(2).addCard(cardCoin1,false);
        gameController.getGame().getPlayersList().get(2).addCard(cardCoin2,false);
        gameController.getGame().getPlayersList().get(2).addCard(cardCoin3,false);
        gameController.getGame().getPlayersList().get(2).addCard(cardCoin9,false);
        gameController.getGame().getPlayersList().get(2).addCard(cardCoin10,false);
        gameController.getGame().getPlayersList().get(2).addCard(cardFace1,true);
        gameController.getGame().getPlayersList().get(2).addCard(cardFace2,true);
        gameController.getGame().getPlayersList().get(2).addCard(cardFace3,true);
        // Testing of method endGame():
        gameController.endGame();
        // Because of setUp() the highScore list was empty and should now contain Ngoc and Phuong as the best players in
        // that order, because of the alphabetical order of this names.
        assertTrue(gameController.getHighScore().get(0).contains("Ngoc"));
        assertTrue(gameController.getHighScore().get(1).contains("Phuong"));
    }

    /**
     * test method newGame() for the case that the player list is null
     */
    @Test (expected = NullPointerException.class)
    public void newGame_NullPlayersList() {
        try{
            File initialPileFile = new File("tests/testResources/shuffled.csv");
            gameController.newGame(null, initialPileFile, 500);
        }
       catch (FileNotFoundException e){
            e.printStackTrace();
       }
    }

    /**
     * test method newGame() for the case that the initialPileFile is null and there isn't AIPlayer in the player list
     */
    @Test
    public void newGame_NullInitialPileFileAndNotAIPlayer() {
        try{
            gameController.newGame(playersList, null, 500);
            assertTrue(gameController.getGame().getDrawPile().getPile().size() == 119-3*(playersList.size()));
            assertTrue(gameController.getGame().getDrawPile().getInitialPile() == null);
            assertTrue(gameController.getGameLog().get().getPlayersList().size() == 3);
            assertEquals("Helen", gameController.getGameLog().get().getStartPlayer().getName());
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * test method newGame() for the case that the initialPileFile is null and there is one APPlayer in the player list
     */
    @Test
    public void newGame_NullInitialPileFileAndOneAIPlayer() {
        try{
            ArrayList<Player> playersAndAI = new ArrayList<>();
            AIPlayer aiPlayer = new AIPlayer(Level.MEDIUM,"Bob");
            playersAndAI.add(startPlayer);
            playersAndAI.add(aiPlayer);
            gameController.newGame(playersAndAI, null, 500);
            assertTrue(gameController.getGame().getDrawPile().getPile().size() == 119-3*(playersAndAI.size()));
            assertTrue(gameController.getGame().getDrawPile().getInitialPile() == null);
            assertTrue(gameController.getGameLog().get().getPlayersList().size() == 2);
            assertEquals("Helen", gameController.getGameLog().get().getStartPlayer().getName());
            AIPlayer aiPlayerTest = (AIPlayer) gameController.getGameLog().get().getPlayersList().get(1);
            assertTrue(aiPlayerTest.getSimulationSpeed() == 500);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * test method newGame() for the case that all parameters aren't null and there isn't AIPlayer in the player list
     */
    @Test
    public void newGame_ParametersNotNull() {
        try{
            File initialPileFile = new File("tests/testResources/shuffled.csv");
            gameController.newGame(playersList, initialPileFile, 500);
            assertTrue(gameController.getGame().getDrawPile().getPile().size() == 119-3*(playersList.size()));
            assertTrue(gameController.getGame().getDrawPile().getInitialPile().size() == 119);
            assertTrue(gameController.getGameLog().get().getPlayersList().size() == 3);
            assertEquals("Helen", gameController.getGameLog().get().getStartPlayer().getName());
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IllegalArgumentException ex){
            ex.printStackTrace();
        }
    }

    /**
     * test method newGame() for the case that all parameters aren't null and there is one AIPlayer in the player list
     */
    @Test
    public void newGame_ParametersNotNullAndOneAIPlayer() {
        try{
            File initialPileFile = new File("tests/testResources/shuffled.csv");
            ArrayList<Player> playersAndAI = new ArrayList<>();
            AIPlayer aiPlayer = new AIPlayer(Level.MEDIUM,"Bob");
            playersAndAI.add(startPlayer);
            playersAndAI.add(aiPlayer);
            gameController.newGame(playersAndAI, initialPileFile, 500);
            assertTrue(gameController.getGame().getDrawPile().getPile().size() == 119-3*( playersAndAI.size()));
            assertTrue(gameController.getGame().getDrawPile().getInitialPile().size() == 119);
            assertTrue(gameController.getGameLog().get().getPlayersList().size() == 2);
            assertEquals("Helen", gameController.getGameLog().get().getStartPlayer().getName());
            AIPlayer aiPlayerTest = (AIPlayer) gameController.getGameLog().get().getPlayersList().get(1);
            assertTrue(aiPlayerTest.getSimulationSpeed() == 500);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IllegalArgumentException ex){
            ex.printStackTrace();
        }
    }

    /**
     * test method newGame() for the case that all parameters aren't null and there are 5 players in the player list
     */
    @Test
    public void newGame_ParametersNotNullAnd5Players() {
        try{
            File initialPileFile = new File("tests/testResources/shuffled.csv");
            Player player4 = new Player("Selena");
            Player player5 = new Player("Gomez");
            playersList.add(player4);
            playersList.add(player5);
            gameController.newGame(playersList, initialPileFile, 500);
            assertTrue(gameController.getGame().getDrawPile().getPile().size() == 119-3*(playersList.size()));
            assertTrue(gameController.getGame().getDrawPile().getInitialPile().size() == 119);
            assertTrue(gameController.getGameLog().get().getPlayersList().size() == 5);
            assertEquals("Helen", gameController.getGameLog().get().getStartPlayer().getName());
            assertTrue(gameController.getGame().getExpeditionDisplay().size() == 1);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IllegalArgumentException ex){
            ex.printStackTrace();
        }
    }

    /**
     * test method setGameLog() for the case that object GameLog is null
     */
    @Test (expected = NullPointerException.class)
    public void setGameLog_loadGameNull(){
        gameController.setGameLog(null);
    }

    /**
     * test method setGameLog() for the case that object GameLog isn't null
     */
    @Test
    public void setGameLog_loadGameNotNull(){
        GameLog loadedGame = new GameLog();
        loadedGame.add(game);
        gameController.setGameLog(loadedGame);
        assertEquals("Helen", gameController.getGameLog().get().getStartPlayer().getName());
    }

    /**
     * Test get TradeAndHireController
     */
    @Test
    public void  getTradeAndHireController(){
        assertNotEquals(gameController.getTradeAndHireController(),null);
    }
    /**
     * Test get AIController
     */
    @Test
    public void  getAIController(){
        assertNotEquals(gameController.getAIController(),null);
    }
    /**
     * Test get LogController
     */
    @Test
    public void  getLogController(){
        assertNotEquals(gameController.getLogController(),null);
    }
    /**
     * Test get PlayerController
     */
    @Test
    public void getPlayerController(){
        assertNotEquals(gameController.getPlayerController(),null);
    }
    /**
     * Test get DiscoverController
     */
    @Test
    public void getDiscoverController(){
        assertNotEquals(gameController.getDiscoverController(),null);
    }
}