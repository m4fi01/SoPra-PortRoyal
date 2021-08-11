package controller;

import model.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Tests the TradeAndHireController
 * @author Phuong Huong Nguyen and another person
 */
//TODO repair test
public class TradeAndHireControllerTest {

    GameController gameController;
    TradeAndHireController tradeAndHireController;
    PlayerController playerController;
    Card personCard = new Card();
    Card shipCard = new Card();

    Game game;
    Player startPlayer;
    Player activePlayer;
    Player actingPlayer;
    ArrayList<Player> playersList = new ArrayList<>();
    // initializing some kind of cards
    Card card1 = new Card();
    Card card2 = new Card();
    Card ship1 = new Card();
    Card ship2 = new Card();
    Card ship3 = new Card();
    Card mademoiselle = new Card();

    /**
     * Setup before testing
     * @throws Exception exception
     */
    @Before
    public void setUp() throws Exception {
        gameController = new GameController();
        tradeAndHireController = gameController.getTradeAndHireController();
        playerController = gameController.getPlayerController();
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("Someone"));
        players.add(new Player("Somehow"));
        players.add(new Player("Something"));
        GameLog gameLog = gameController.getGameLog();
        LogController logController = gameController.getLogController();
        gameController.newGame(players,null, 500);

        // initializing data for cards
        personCard.setCardName(CardName.CAPTAIN);
        personCard.setCardType(CardType.PERSON);
        personCard.setVictoryPoints(1);
        personCard.setAnchors(1);
        personCard.setCoinValue(3);
        shipCard.setCardName(CardName.PINNACE);
        shipCard.setCardType(CardType.SHIP);
        shipCard.setCardColour(CardColour.YELLOW);
        shipCard.setCoinValue(3);

        // Initialize data for cards for testing method hire()
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
        mademoiselle.setCardName(CardName.MADEMOISELLE);
        mademoiselle.setCardType(CardType.PERSON);
        mademoiselle.setCoinValue(7);
        mademoiselle.setVictoryPoints(2);

        // Initialize players for testing method hire()
        startPlayer = new Player("Helen");
        activePlayer = new Player("Phuong");
        actingPlayer = new Player("Ngoc");
        playersList.add(startPlayer);
        playersList.add(activePlayer);
        playersList.add(actingPlayer);

    }

    /**
     * Test the method trade() for the case that the selected card is not a ship card
     */
    @Test
    public void trade_NotAShipCard(){
        assertFalse(tradeAndHireController.trade(personCard));
    }
    /**
     * Tests trade by letting 2 generated players take ships while one is the active player and the other isn't.
     */
    @Test
    public void trade() {
        Game testGame = gameController.getGame();
        ArrayList<Card> cards = new ArrayList<>();
        Card ship1 = new Card();
        ship1.setCoinValue(-3);
        ship1.setCardType(CardType.SHIP);
        Card person1 = new Card();
        person1.setCoinValue(9);
        person1.setCardType(CardType.PERSON);
        Card ship2 = new Card();
        ship2.setCoinValue(-8);
        ship2.setCardType(CardType.SHIP);
        cards.add(ship1);
        cards.add(person1);
        cards.add(ship2);
        testGame.setHarborDisplay(cards);
        ArrayList<Player> players = testGame.getPlayersList();
        testGame.setActingPlayer(players.get(0));
        testGame.setActivePlayer(players.get(0));
        int previousCoins = gameController.getGame().getPlayersList().get(0).getCoinsCount();
        int actionsDone = gameController.getGame().getActionPoints();
        assertTrue(tradeAndHireController.trade(ship1));
        assertEquals(-1, gameController.getGame().getActionPoints());
        assertEquals(previousCoins + 3, gameController.getGame().getPlayersList().get(0).getCoinsCount());
        gameController.getGame().setActingPlayer(players.get(1));
        previousCoins = gameController.getGame().getPlayersList().get(0).getCoinsCount();
        int previousOtherCoins = gameController.getGame().getPlayersList().get(1).getCoinsCount();
        assertTrue(tradeAndHireController.trade(ship2));
        assertEquals(previousCoins + 1, gameController.getGame().getPlayersList().get(0).getCoinsCount());
        assertEquals(previousOtherCoins + 7, gameController.getGame().getPlayersList().get(1).getCoinsCount());
    }

    /**
     * Test the method hire() for the case that the selected card is not a person card
     */
    @Test
    public void hire_NotAPersonCard(){
        assertFalse(tradeAndHireController.hire(shipCard));
    }

    /**
     * Test the method hire() for the situation that the selected card is a person card, the acting player is not the active player
     * and the acting player hasn't enough money to buy the selected card
     */
    @Test
    public void hire_NotActivePlayerAndNotEnoughMoney(){
        try{
            gameController.newGame(playersList, null, 500); // Each player gets three coins.
            game = gameController.getGame();
            // setting active and acting players to the game
            Player active = game.getPlayersList().get(1);
            Player acting = game.getPlayersList().get(2);
            game.setActivePlayer(active);
            game.setActingPlayer(acting);
            Card captain = null;
            for(Card card : game.getDrawPile().getPile()) {
                if(card.getCardName().equals(CardName.CAPTAIN)) {
                    captain = card;
                    break;
                }
            }
            assertFalse(gameController.getTradeAndHireController().hire(captain));
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Test the method hire() for the situation that the selected card is a person card, the acting player is not the active player
     * and the acting player has enough money to buy the selected card
     */
    @Test
    public void hire_NotActivePlayerAndGetEnoughMoney() {
        try {
            gameController.newGame(playersList, null, 500);
            game = gameController.getGame();
            // setting active and acting players to the game
            Player active = game.getPlayersList().get(1);
            Player acting = game.getPlayersList().get(2);
            game.setActivePlayer(active);
            game.setActingPlayer(acting);
            // initializing coins for active and acting players. At the beginning the active has 2 coins and the acting owns 3 coins and one mademoiselle card
            game.getActivePlayer().addCard(card1, false);
            game.getActivePlayer().addCard(card2, false);
            game.getActingPlayer().addCard(ship1, false);
            game.getActingPlayer().addCard(ship2, false);
            game.getActingPlayer().addCard(ship3, false);
            game.getActingPlayer().addCard(mademoiselle, true);
            Card captain = null;
            for(Card card : game.getDrawPile().getPile()) {
                if(card.getCardName().equals(CardName.CAPTAIN)) {
                    captain = card;
                    break;
                }
            }
            assertTrue(gameController.getTradeAndHireController().hire(captain));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests hire by letting 3 generated players take persons while one is the active player and the others aren't.
     * Also tests cases where they have not enough coins and/or have some amount of mademoiselles.
     */
    @Test
    public void hire() {
        Game testGame = gameController.getGame();
        ArrayList<Card> cards = new ArrayList<>();

        Card ship1 = new Card();
        ship1.setCoinValue(-3);
        ship1.setCardType(CardType.SHIP);

        Card person1 = new Card();
        person1.setCoinValue(4);
        person1.setCardType(CardType.PERSON);
        person1.setCardName(CardName.MADEMOISELLE);

        Card person2 = new Card();
        person2.setCoinValue(17);
        person2.setCardType(CardType.PERSON);
        person2.setCardName(CardName.PROTECTION_MONEY);

        Card person3 = new Card();
        person3.setCoinValue(7);
        person3.setCardType(CardType.PERSON);
        person3.setCardName(CardName.PROTECTION_MONEY);

        Card person4 = new Card();
        person4.setCoinValue(7);
        person4.setCardType(CardType.PERSON);
        person4.setCardName(CardName.PROTECTION_MONEY);

        Card person5 = new Card();
        person5.setCoinValue(7);
        person5.setCardType(CardType.PERSON);
        person5.setCardName(CardName.PROTECTION_MONEY);

        cards.add(ship1);
        cards.add(person1);
        cards.add(person2);
        cards.add(person3);
        cards.add(person4);
        cards.add(person5);

        testGame.setHarborDisplay(cards);
        ArrayList<Player> players = testGame.getPlayersList();
        Player player0 = players.get(0);
        testGame.setActivePlayer(player0);
        testGame.setActingPlayer(player0);
        playerController.drawCoins(20);
        assertFalse(tradeAndHireController.hire(ship1));
        int mademoiselles0 = player0.getMademoisellesCount();
        int previousCoins = player0.getCoinsCount();
        int previousPersonalDisplaySize = player0.getPersonalDisplay().size();
        assertTrue(tradeAndHireController.hire(person1));
        player0 = gameController.getGame().getActingPlayer();
        assertEquals(mademoiselles0 + 1,player0.getMademoisellesCount());
        assertEquals(previousCoins - 4, player0.getCoinsCount());
        assertEquals(previousPersonalDisplaySize + 1, player0.getPersonalDisplay().size());
        assertTrue(tradeAndHireController.hire(person2));
        player0 = gameController.getGame().getActingPlayer();
        assertEquals(3, player0.getCoinsCount());
        assertFalse(tradeAndHireController.hire(person3));
        Player player1 = players.get(1);
        tradeAndHireController.nextActingPlayer();
        playerController.drawCoins(20);
        previousCoins = gameController.getGame().getActingPlayer().getCoinsCount();
        previousPersonalDisplaySize = player1.getPersonalDisplay().size();
        assertTrue(tradeAndHireController.hire(person4));
        player1 = gameController.getGame().getActingPlayer();
        assertEquals(previousCoins - 8, player1.getCoinsCount());
        assertEquals(previousPersonalDisplaySize + 1, player1.getPersonalDisplay().size());
        tradeAndHireController.nextActingPlayer();
        assertFalse(tradeAndHireController.hire(person5));
    }

    /**
     * Tests nextActingPlayer by having a player that isn't the active player with a jester in the appropriate situation
     * and a player that isn't the active player with an admiral in the appropriate situation.
     * Also tests whether the active player is rotated after he is reached again.
     */
    @Test
    public void nextActingPlayer() {
        Game testGame = gameController.getGame();
        ArrayList<Card> cards = new ArrayList<>();

        testGame.setHarborDisplay(cards);
        ArrayList<Player> players = testGame.getPlayersList();

        assertEquals(players.get(0), testGame.getActivePlayer());
        assertEquals(players.get(0), testGame.getActingPlayer());
        Card jester = new Card();
        jester.setCardName(CardName.JESTER);
        jester.setCardType(CardType.PERSON);
        jester.setCoinValue(22);
        players.get(1).addCard(jester,true);
        int previousCoins = players.get(1).getCoinsCount();
        assertEquals(players.get(1), tradeAndHireController.nextActingPlayer());
        assertEquals(1, testGame.getActionPoints());
        assertEquals(previousCoins + 1, players.get(1).getCoinsCount());

        for (int i = 0; i < 5; i++) {
            Card ship1 = new Card();
            ship1.setCoinValue(-3);
            ship1.setCardType(CardType.SHIP);
            cards.add(ship1);
        }

        Card admiral = new Card();
        admiral.setCardName(CardName.ADMIRAL);
        admiral.setCardType(CardType.PERSON);
        admiral.setCoinValue(22);
        players.get(2).addCard(admiral,true);
        previousCoins = players.get(2).getCoinsCount();
        assertEquals(players.get(2), tradeAndHireController.nextActingPlayer());
        assertEquals(previousCoins + 2, players.get(2).getCoinsCount());
        assertEquals(players.get(1), tradeAndHireController.nextActingPlayer());
        assertEquals(0, testGame.getHarborDisplay().size());
    }

    /**
     * Tests startPhase by having a player have a jester when the phase should be skipped as well as by testing whether
     * the number of actions the player gets conforms the amount he should get based on how many different ship colours
     * lay out
     */
    @Test
    public void startPhase() {
        Game testGame = gameController.getGame();
        ArrayList<Card> cards = new ArrayList<>();

        testGame.setHarborDisplay(cards);
        ArrayList<Player> players = testGame.getPlayersList();
        Card ship1 = new Card();
        ship1.setCoinValue(-3);
        ship1.setCardType(CardType.SHIP);
        ship1.setCardColour(CardColour.BLACK);

        Card ship2 = new Card();
        ship2.setCoinValue(-3);
        ship2.setCardType(CardType.SHIP);
        ship2.setCardColour(CardColour.BLUE);

        Card ship3 = new Card();
        ship3.setCoinValue(-3);
        ship3.setCardType(CardType.SHIP);
        ship3.setCardColour(CardColour.RED);

        cards.add(ship1);
        cards.add(ship2);
        cards.add(ship3);

        tradeAndHireController.startPhase(false);
        assertEquals(1, gameController.getGame().getActionPoints());

        Card ship4 = new Card();
        ship4.setCoinValue(-3);
        ship4.setCardType(CardType.SHIP);
        ship4.setCardColour(CardColour.GREEN);
        cards.add(ship4);

        gameController.getGame().setHarborDisplay(cards);
        tradeAndHireController.startPhase(false);
        assertEquals(2, gameController.getGame().getActionPoints());

        Card ship5 = new Card();
        ship5.setCoinValue(-3);
        ship5.setCardType(CardType.SHIP);
        ship5.setCardColour(CardColour.YELLOW);
        cards.add(ship5);

        gameController.getGame().setHarborDisplay(cards);
        tradeAndHireController.startPhase(false);
        assertEquals(3, gameController.getGame().getActionPoints());

        cards.clear();
        Card jester = new Card();
        jester.setCardName(CardName.JESTER);
        jester.setCardType(CardType.PERSON);
        jester.setCoinValue(22);
        cards.add(jester);
        gameController.getGame().getPlayersList().get(1).addCard(jester,true);
        int coinsBefore = gameController.getGame().getPlayersList().get(1).getCoinsCount();
        tradeAndHireController.startPhase(true);
        assertEquals(coinsBefore + 1, gameController.getGame().getPlayersList().get(1).getCoinsCount());
    }
}