package model;

import org.junit.Test;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * @author Mohammad Almidani
 *
 * Test Class for the model class
 */
public class PlayerTest {

    /**
     * testing methode getName()
     */
    @Test
    public void getName() {
        Player player = new Player("Ehrenmann");
        assertEquals(player.getName(),"Ehrenmann");
    }

    /**
     * testing Methode getVictoryPointsCount()
     */
    @Test
    public void getVictoryPointsCount() {
        Player player = new Player("Noob");
        assertEquals(player.getVictoryPointsCount(),0);

        Card card = new Card();
        card.setVictoryPoints(5);
        card.setCardName(CardName.CAPTAIN);
        player.addCard(card,true);
        assertEquals(player.getVictoryPointsCount(),5);

        Card card1 = new Card();
        card1.setVictoryPoints(3);
        card1.setCardName(CardName.CAPTAIN);
        player.addCard(card1,true);
        assertEquals(player.getVictoryPointsCount(),8);
    }

    /**
     * testing Methode getAnchorsCount()
     */
    @Test
    public void getAnchorsCount() {
        Player player = new Player("Noob");
        assertEquals(player.getAnchorsCount(),0);

        Card card = new Card();
        card.setAnchors(5);
        card.setCardName(CardName.GALLEON);
        player.addCard(card,true);
        assertEquals(player.getAnchorsCount(),5);

        Card card1 = new Card();
        card1.setAnchors(3);
        card1.setCardName(CardName.GALLEON);
        player.addCard(card1,true);
        assertEquals(player.getAnchorsCount(),8);
    }

    /**
     * testing methode getCrossesCount()
     */
    @Test
    public void getCrossesCount() {
        Player player = new Player("Noob");
        assertEquals(player.getCrossesCount(),0);

        Card card = new Card();
        card.setCrosses(5);
        card.setCardName(CardName.CAPTAIN);
        player.addCard(card,true);

        assertEquals(player.getCrossesCount(),5);

        Card card1 = new Card();
        card1.setCrosses(3);
        card1.setCardName(CardName.CAPTAIN);
        player.addCard(card1,true);
        assertEquals(player.getCrossesCount(),8);
    }

    /**
     * testing methode getCoinsCount()
     */
    @Test
    public void getCoinsCount() {
        Player player = new Player("Noob");
        assertEquals(player.getCoinsCount(),0);

        Card card = new Card();
        card.setCardName(CardName.CAPTAIN);
        player.addCard(card,false);
        assertEquals(player.getCoinsCount(),1);

        Card card1 = new Card();
        card1.setCardName(CardName.CAPTAIN);
        player.addCard(card1,false);
        assertEquals(player.getCoinsCount(),2);
    }

    /**
     * testing methode getHousesCount()
     */
    @Test
    public void getHousesCount() {
        Player player = new Player("Noob");
        assertEquals(player.getHousesCount(),0);

        Card card = new Card();
        card.setHouses(5);
        card.setCardName(CardName.CAPTAIN);
        player.addCard(card,true);
        assertEquals(player.getHousesCount(),5);

        Card card1 = new Card();
        card1.setHouses(3);
        card1.setCardName(CardName.CAPTAIN);
        player.addCard(card1,true);
        assertEquals(player.getHousesCount(),8);
    }

    /**
     * testing methode getSwordsCount()
     */
    @Test
    public void getSwordsCount() {
        Player player = new Player("Noob");
        assertEquals(player.getSwordsCount(),0);

        Card card = new Card();
        card.setSwords(5);
        card.setCardName(CardName.CAPTAIN);
        player.addCard(card,true);
        assertEquals(player.getSwordsCount(),5);

        Card card1 = new Card();
        card1.setSwords(3);
        card1.setCardName(CardName.CAPTAIN);
        player.addCard(card1,true);
        assertEquals(player.getSwordsCount(),8);
    }

    /**
     * testing methode getJacksCount()
     */
    @Test
    public void getJacksCount() {
        Player player = new Player("Noob");
        assertEquals(player.getJacksCount(),0);

        Card card = new Card();
        card.setCardName(CardName.JACK_OF_ALL_TRADES);
        player.addCard(card,true);
        assertEquals(player.getJacksCount(),1);

        player.addCard(card,true);
        assertEquals(player.getJacksCount(),2);

        player.addCard(card,true);
        assertEquals(player.getJacksCount(),3);
    }

    /**
     * testing methode getMademoisellesCount()
     */
    @Test
    public void getMademoisellesCount() {
        Player player = new Player("Noob");
        assertEquals(player.getMademoisellesCount(),0);

        Card card = new Card();
        card.setCardName(CardName.MADEMOISELLE);
        player.addCard(card,true);
        assertEquals(player.getMademoisellesCount(),1);

        player.addCard(card,true);
        assertEquals(player.getMademoisellesCount(),2);

        player.addCard(card,true);
        assertEquals(player.getMademoisellesCount(),3);
    }

    /**
     * testing methode getJestersCount()
     */
    @Test
    public void getJestersCount() {
        Player player = new Player("Noob");
        assertEquals(player.getJestersCount(),0);

        Card card = new Card();
        card.setCardName(CardName.JESTER);
        player.addCard(card,true);
        assertEquals(player.getJestersCount(),1);

        player.addCard(card,true);
        assertEquals(player.getJestersCount(),2);

        player.addCard(card,true);
        assertEquals(player.getJestersCount(),3);
    }

    /**
     * testing methode getAdmiralsCount()
     */
    @Test
    public void getAdmiralsCount() {
        Player player = new Player("Noob");
        assertEquals(player.getAdmiralsCount(),0);

        Card card = new Card();
        card.setCardName(CardName.ADMIRAL);
        player.addCard(card,true);
        assertEquals(player.getAdmiralsCount(),1);

        player.addCard(card,true);
        assertEquals(player.getAdmiralsCount(),2);

        player.addCard(card,true);
        assertEquals(player.getAdmiralsCount(),3);
    }

    /**
     * testing methode getGovernorsCount()
     */
    @Test
    public void getGovernorsCount() {
        Player player = new Player("Noob");
        assertEquals(player.getGovernorsCount(),0);

        Card card = new Card();
        card.setCardName(CardName.GOVERNOR);
        player.addCard(card,true);
        assertEquals(player.getGovernorsCount(),1);

        player.addCard(card,true);
        assertEquals(player.getGovernorsCount(),2);

        player.addCard(card,true);
        assertEquals(player.getGovernorsCount(),3);
    }

    /**
     * testing methode getPersonalDisplay()
     */
    @Test
    public void getPersonalDisplay() {
        Player player = new Player("Noob");
        ArrayList<Card> testList = new ArrayList<>();

        Card card = new Card();
        card.setCardName(CardName.GOVERNOR);
        player.addCard(card,true);

        Card card1 = new Card();
        card1.setCardName(CardName.CAPTAIN);
        player.addCard(card1,true);

        Card card2 = new Card();
        card2.setCardName(CardName.CAPTAIN);
        player.addCard(card2,true);

        testList.add(card);
        testList.add(card1);
        testList.add(card2);

        assertEquals(player.getPersonalDisplay(),testList);
    }

    /**
     * testing methode getTradersCount()
     */
    @Test
    public void getTradersCount() {
        Player player = new Player("Noob");

        Card card = new Card();
        card.setCardColour(CardColour.RED);
        card.setCardName(CardName.TRADER);

        Card card1 = new Card();
        card1.setCardColour(CardColour.BLUE);
        card1.setCardName(CardName.TRADER);

        player.addCard(card,true);
        player.addCard(card1,true);

        assertEquals(player.getTradersCount(CardColour.RED),1);
        assertEquals(player.getTradersCount(CardColour.BLUE),1);
    }

    /**
     * testing methode addCard()
     */
    @Test
    public void addCard() {
        Player player = new Player("Noob");

        Card card = new Card();
        card.setCardName(CardName.CAPTAIN);
        card.setCardType(CardType.PERSON);
        card.setCoinValue(3);
        card.setSwords(5);

        Card card1 = new Card();
        card1.setCardName(CardName.CAPTAIN);
        card1.setCardType(CardType.PERSON);
        card1.setCoinValue(3);
        card1.setSwords(5);

        Card card2 = new Card();
        card2.setCardName(CardName.GALLEON);
        card2.setCardType(CardType.SHIP);
        card2.setCoinValue(3);
        card2.setSwords(5);

        player.addCard(card,true);
        player.addCard(card1,true);
        player.addCard(card2,true);

        ArrayList<Card> list = player.getPersonalDisplay();

        assertEquals(card,list.get(0));
        assertEquals(card1,list.get(1));
        assertEquals(card2, list.get(2));
    }

    /**
     * testing addCard when the card is null
     */
    @Test (expected = NullPointerException.class)
    public void addCardNull() {
        Player player = new Player("Noob");
        player.addCard(null,true);
    }

    /**
     * testing methode removeCoin()
     */
    @Test
    public void removeCoin() {
        Player player = new Player("Noob");

        Card card = new Card();
        player.addCard(card,false);

        assertEquals(player.getCoinsCount(),1);

        Card card1= player.removeCoin();

        assertEquals(player.getCoinsCount(),0);

        Player player2 = new Player("Test");
        assertEquals(player.removeCoin(), null);
    }

    /**
     * testing methode removeCard(Card card)
     */
    @Test
    public void removeCard() {
        Player player = new Player("Noob");

        Card card = new Card();
        card.setCardName(CardName.PRIEST);
        card.setCrosses(3);
        card.setCoinValue(3);
        card.setVictoryPoints(5);
        player.addCard(card,true);



        assertEquals(player.getVictoryPointsCount(),5);
        assertTrue(player.removeCard(card));
        assertEquals(player.getVictoryPointsCount(),0);

        Card card2 = new Card();
        card2.setCardName(CardName.JACK_OF_ALL_TRADES);
        player.addCard(card2,true);
        assertEquals(player.getJacksCount(), 1);
        assertTrue(player.removeCard(card2));
        assertEquals(player.getJacksCount(), 0);
    }

    /**
     * testing removeCard when the card is null
     */
    @Test (expected = NullPointerException.class)
    public void removeCardNull() {
        Player player = new Player("Noob");
        player.removeCard(null);
    }

    /**
     * testing methode setName()
     */
    @Test
    public void setName() {
        Player player = new Player("Noob");

        assertEquals(player.getName(),"Noob");

        player.setName("Noobie");

        assertEquals(player.getName(),"Noobie");
    }

    /**
     * testing overridden methode equals() with another player, the same player, null and a card object
     */
    @Test
    public void testEquals() {
        Player player = new Player("Noob");
        Player player1 = new Player("Noobie");
        Player player2 = null;
        Card card = new Card();

        assertEquals(player,player);
        assertNotEquals(player,player1);
        assertNotEquals(player,player2);
        assertNotEquals(card,player);
    }

    /**
     * testing hashcode by creating two players and checking if the hashcode() is different
     */
    @Test
    public void hashcode() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        assertEquals(player1.hashCode(), player1.getPlayerID());
        assertNotEquals(player1.hashCode(), player2.hashCode());
    }

}