package controller;

import model.*;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * @author Hoang Hai Nguyen
 * Tests the DiscoverController
 */

//TODO repair test
public class DiscoverControllerTest {
    private GameController gameController;
    private DiscoverController discoverController;


    /**
     * Setup before testing
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

        gameController= new GameController();
        discoverController=gameController.getDiscoverController();
    }

    /**
     * Tests repelShip by generating ship cards, creating a new game and adding the cards to the harbor display.
     * Then testing if repelShip() returns true or false.
     */
    @Test
    public void repelShip() {
        Card testShipCard1= new Card();
        testShipCard1.setSwords(1);
        testShipCard1.setCardName(CardName.FLUTE);
        Card testShipCard2= new Card();
        testShipCard2.setCardName(CardName.FLUTE);
        testShipCard2.setSwords(99);
        Card testPersonCard = new Card();
        testPersonCard.setCardName(CardName.PIRATE);
        testPersonCard.setSwords(9);
        Player testPlayer = new Player("Hai Nguyen"); // this player has 9 swords
        testPlayer.addCard(testPersonCard,true);
        ArrayList<Player> playerslist= new ArrayList<Player>();
        playerslist.add(testPlayer);
        ArrayList<Card> pile = new ArrayList<Card>(); // drawpile
        pile.add(testShipCard1); // ship
        DrawPile drawPile = new DrawPile(null, pile);
        Game testGame= new Game(testPlayer,drawPile, null,playerslist);
        testGame.addToHarborDisplay(testShipCard2);
        testGame.addToHarborDisplay(testShipCard1);
        testGame.setLastCardDrawn(testShipCard2);// card with 99 swords
        gameController.getGameLog().add(testGame);
        assertEquals(discoverController.repelShip(),false); // should be false because player with 9 swords cannot repel ship with 99 swords
        testGame.setLastCardDrawn(testShipCard1);// card with 1 sword
        gameController.getGameLog().add(testGame);
        assertEquals(discoverController.repelShip(),true); // should be true because player with 9 swords can repel ship with 1 sword
    }

    /**
     * Tests drawCard by creating two players and giving them cards. Then generating a new game and a draw pile.
     * Then testing drawCard with different cards on the pile.
     */
    @Test
    public void drawCard() {
        Player testPlayer1 = new Player("Hai 1.Clone"); // min victory, min swords have 11 coins
        Player testPlayer4 = new Player("Hai 4.Clone"); // min victory, min swords have 11 coins
        Player testPlayer2 = new Player("Hai 2.Clone"); // max victory, max swords have 12 coins
        Player testPlayer3 = new Player("Hai 3.Clone"); // max victory, max swords have 12 coins
        Player testPlayer5 = new Player("Hai 5.Clone"); // nothing special, 1.player in player list has 0 coin
        Card victoryCard1 = new Card();
        Card victoryCard2 = new Card();
        victoryCard1.setVictoryPoints(1);
        victoryCard1.setCardName(CardName.PIRATE);
        victoryCard2.setVictoryPoints(99);
        victoryCard2.setCardName(CardName.PIRATE);
        Card swordCard1 = new Card();
        Card swordCard2= new Card();
        swordCard1.setSwords(1);
        swordCard1.setCardName(CardName.PIRATE);
        swordCard2.setSwords(99);
        swordCard2.setCardName(CardName.PIRATE);
        testPlayer1.addCard(victoryCard1,true);
        testPlayer1.addCard(swordCard1,true);
        testPlayer4.addCard(victoryCard1,true);
        testPlayer4.addCard(swordCard1,true);
        Card victoryCard3 = new Card();
        victoryCard3.setVictoryPoints(2);
        victoryCard3.setCardName(CardName.PIRATE);
        Card swordCard3= new Card();
        swordCard3.setSwords(2);
        swordCard3.setCardName(CardName.PIRATE);
        testPlayer5.addCard(victoryCard3,true);
        testPlayer5.addCard(swordCard3,true);
        for(int i= 0; i< 11; i++){
            Card coin = new Card();
            testPlayer1.addCard(coin,false);
            testPlayer4.addCard(coin,false);
        }
        testPlayer2.addCard(victoryCard2,true);
        testPlayer2.addCard(swordCard2,true);
        testPlayer3.addCard(swordCard2,true);
        testPlayer3.addCard(victoryCard2,true);
        for(int i= 0; i< 12; i++){
            Card coin = new Card();
            testPlayer2.addCard(coin,false);
            testPlayer3.addCard(coin,false);
        }
        Card pileCard1= new Card();
        pileCard1.setCardType(CardType.SHIP);
        Card pileCard2= new Card();
        pileCard2.setCardType(CardType.EXPEDITION);
        Card pileCard3= new Card();
        pileCard3.setCardType(CardType.TAX_INCREASE);
        pileCard3.setCardName(CardName.CHARITY);
        Card pileCard4 = new Card();
        pileCard4.setCardType(CardType.TAX_INCREASE);
        pileCard4.setCardName(CardName.PROTECTION_MONEY);
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(testPlayer5);
        players.add(testPlayer1);
        players.add(testPlayer2);
        players.add(testPlayer3);
        players.add(testPlayer4);
        ArrayList<Card> pile = new ArrayList<Card>(); // drawpile
        pile.add(pileCard1); // ship
        pile.add(pileCard2); // expedition
        pile.add(pileCard3); // tax charity
        pile.add(pileCard3); // player 1 takes this as coin
        pile.add(pileCard3); // player 4 takes this as coin
        pile.add(pileCard4); // tax protection
        DrawPile drawPile = new DrawPile(null, pile);
        Game testGame = new Game(testPlayer2,drawPile,null,players);
        gameController.getGameLog().add(testGame);
        discoverController.drawCard(); // draw ship card
        assertEquals(gameController.getGame().getHarborDisplay().size(),1); // size of habour display should be 1 now
        assertEquals(gameController.getGame().getHarborDisplay().get(0).equals(pileCard1),true); // this ship card should be added to habour display
        assertEquals(gameController.getGame().getDiscardPile().size(),0);// no card should be added in discard pile
        assertEquals(gameController.getGame().getLastCardDrawn().equals(pileCard1),true);// the last card drawn in the game should be this ship card
        testGame = gameController.getGame();
        gameController.getGameLog().add(testGame);
        discoverController.drawCard(); // expedition card
        assertEquals(gameController.getGame().getExpeditionDisplay().size(),1); // size of expedition display should be 1 now
        assertEquals(gameController.getGame().getDiscardPile().size(),0); // no card should be added in discard pile
        assertEquals(gameController.getGame().getExpeditionDisplay().get(0).equals(pileCard2),true); // this ship card should be added to expedition display
        assertEquals(gameController.getGame().getLastCardDrawn().equals(pileCard2),true);// the last card drawn in the game should be this expedition card
        testGame = gameController.getGame();
        gameController.getGameLog().add(testGame);
        discoverController.drawCard(); // tax charity
        assertEquals(gameController.getGame().getPlayersList().get(0).getCoinsCount(),0); // player 5 should have 0 coin
        assertEquals(gameController.getGame().getPlayersList().get(2).getCoinsCount(),6);// player 2 should have only 6 coins because he has before this tax card 12 coins
        assertEquals(gameController.getGame().getPlayersList().get(3).getCoinsCount(),6);// player 3 should have only 6 coins because he has before this tax card 12 coins
        assertEquals(gameController.getGame().getDrawPile().getPile().size(),1); // draw pile size should be 1 because 2 next card were added to player 1 and player 4 as coins
        assertEquals(gameController.getGame().getDiscardPile().size(),13); // discard pile size should be 13 because : 1 tax card , 6 coin cards of player 2, 6 coin cards of player 3
        assertEquals(gameController.getGame().getPlayersList().get(1).getCoinsCount(),12); // player 1 should have 12 coins because he has the least victory points (11 coins + 1 coin added thanks to charity card)
        assertEquals(gameController.getGame().getPlayersList().get(4).getCoinsCount(),12);// player 4 should have 12 coins because he has the least victory points (11 coins + 1 coin added thanks to charity card)
        assertEquals(gameController.getGame().getLastCardDrawn().equals(pileCard3),true);// the last card drawn in the game should be this tax card
        testGame = gameController.getGame();
        gameController.getGameLog().add(testGame);
        discoverController.drawCard(); // tax protection
        assertEquals(gameController.getGame().getPlayersList().get(0).getCoinsCount(),0);// player 5 should have 0 coin
        assertEquals(gameController.getGame().getPlayersList().get(1).getCoinsCount(),6);// player 1 should have only 6 coins because he has before this tax card 12 coins
        assertEquals(gameController.getGame().getPlayersList().get(4).getCoinsCount(),6);// player 4 should have only 6 coins because he has before this tax card 12 coins
        //assertEquals(gameController.getGame().getDrawPile().getPile().size(),23);// draw pile size should be 23 because : new draw pile from discard pile had 13 cards + 12 coin card from players then minus 2 coin card to be added to player 2 and player 3
       // assertEquals(gameController.getGame().getDiscardPile().size(),1);// discard pile should be 1 because 13 cards was used to make new draw pile, then 1 card added is the tax card
        assertEquals(gameController.getGame().getPlayersList().get(2).getCoinsCount(),7);// player 2 should have 7 coins because he has the most swords (6 coins + 1 coin added from tax protection)
        assertEquals(gameController.getGame().getPlayersList().get(3).getCoinsCount(),7);// player 3 should have 7 coins because he has the most swords (6 coins + 1 coin added from tax protection)
        assertEquals(gameController.getGame().getLastCardDrawn().equals(pileCard4),true);// the last card drawn in the game should be this tax card
    }

    /**
     * Tests startExpedition by generating a player, cards, a draw pile and a new game and then starting an expedition
     * with different cards.
     */
    @Test
    public void startExpedition() {
        Player testPlayer = new Player("Hai Nguyen");
        ArrayList<Player> players= new ArrayList<Player>();
        players.add(testPlayer);
        Card expedition = new Card(); // expedition with 2 anchors, 2 houses, 2 crosses, coin value 3
        expedition.setCardName(CardName.EXPEDITION);
        expedition.setAnchors(2);
        expedition.setHouses(2);
        expedition.setCrosses(2);
        expedition.setCoinValue(3);
        Card card1 = new Card();
        card1.setCrosses(1);
        card1.setCardName(CardName.PIRATE);
        Card card2 = new Card();
        card2.setAnchors(1);
        card2.setCardName(CardName.PIRATE);
        Card card3 = new Card();
        card3.setHouses(1);
        card3.setCardName(CardName.PIRATE);
        Card cardJester1 = new Card();
        cardJester1.setCardName(CardName.JACK_OF_ALL_TRADES);
        ArrayList<Card> cardList1= new ArrayList<Card>();
        cardList1.add(card1);
        cardList1.add(card2);
        cardList1.add(card3);
        cardList1.add(cardJester1);
        cardList1.add(cardJester1);
        cardList1.add(cardJester1);
        for(int i= 0; i< cardList1.size(); i++){
            testPlayer.addCard(cardList1.get(i),true);
        }
        Card card4 = new Card();
        card4.setCrosses(2);
        card4.setCardName(CardName.PIRATE);
        Card card5 = new Card();
        card5.setAnchors(2);
        card5.setCardName(CardName.PIRATE);
        Card card6 = new Card();
        card6.setHouses(2);
        card6.setCardName(CardName.PIRATE);
        Card cardJester2 = new Card();
        cardJester2.setCardName(CardName.JACK_OF_ALL_TRADES);
        ArrayList<Card> cardList2= new ArrayList<Card>();
        cardList2.add(card4);
        cardList2.add(card5);
        cardList2.add(card6);
        cardList2.add(cardJester2);
        for(int i= 0; i< cardList2.size(); i++){
            testPlayer.addCard(cardList2.get(i),true);
        }
        Card card7 = new Card();
        card7.setCrosses(1);
        card7.setCardName(CardName.PIRATE);
        Card card8 = new Card();
        card8.setAnchors(2);
        card8.setCardName(CardName.PIRATE);
        Card card9 = new Card();
        card9.setHouses(1);
        card9.setCardName(CardName.PIRATE);
        Card cardJester3 = new Card();
        cardJester3.setCardName(CardName.JACK_OF_ALL_TRADES);
        ArrayList<Card> cardList3= new ArrayList<Card>();
        cardList3.add(card7);
        cardList3.add(card8);
        cardList3.add(card9);
        cardList3.add(cardJester3);
        for(int i= 0; i< cardList3.size(); i++){
            testPlayer.addCard(cardList3.get(i),true);
        }
        Card card10 = new Card();
        card10.setCrosses(22);
        card10.setCardName(CardName.PIRATE);
        Card card11 = new Card();
        card11.setAnchors(1);
        card11.setCardName(CardName.PIRATE);
        Card card12 = new Card();
        card12.setHouses(1);
        card12.setCardName(CardName.PIRATE);
        Card cardJester13 = new Card();
        cardJester13.setCardName(CardName.JACK_OF_ALL_TRADES);
        ArrayList<Card> cardList4= new ArrayList<Card>();
        cardList4.add(card10);
        cardList4.add(card11);
        cardList4.add(card12);
        cardList4.add(cardJester13);
        for(int i= 0; i< cardList4.size(); i++){
            testPlayer.addCard(cardList4.get(i),true);
        }

        Card card14 = new Card();
        card14.setCrosses(2);
        card14.setCardName(CardName.PIRATE);
        Card card15 = new Card();
        card15.setAnchors(2);
        card15.setCardName(CardName.PIRATE);
        Card card16 = new Card();
        card16.setHouses(0);
        card16.setCardName(CardName.PIRATE);
        Card cardJester14 = new Card();
        cardJester14.setCardName(CardName.JACK_OF_ALL_TRADES);
        ArrayList<Card> cardList5= new ArrayList<Card>();
        cardList5.add(card14);
        cardList5.add(card15);
        cardList5.add(card16);
        cardList5.add(cardJester14);
        for(int i= 0; i< cardList5.size(); i++){
            testPlayer.addCard(cardList5.get(i),true);
        }

        Card card17 = new Card();
        card17.setCrosses(0);
        card17.setCardName(CardName.PIRATE);
        Card card18 = new Card();
        card18.setAnchors(2);
        card18.setCardName(CardName.PIRATE);
        Card card19 = new Card();
        card19.setHouses(2);
        card19.setCardName(CardName.PIRATE);
        Card cardJester15 = new Card();
        cardJester15.setCardName(CardName.JACK_OF_ALL_TRADES);
        ArrayList<Card> cardList6= new ArrayList<Card>();
        cardList6.add(card17);
        cardList6.add(card18);
        cardList6.add(card19);
        cardList6.add(cardJester15);
        for(int i= 0; i< cardList6.size(); i++){
            testPlayer.addCard(cardList6.get(i),true);
        }
        ArrayList<Card> cardList7= new ArrayList<Card>();
        cardList7.add(card2);
        // player has 24 cards
        DrawPile drawPile= new DrawPile(null, cardList3);
        Game testGame= new Game(testPlayer,drawPile,null,players);
        gameController.getGameLog().add(testGame);
        int ex1= discoverController.startExpedition(expedition,cardList1);
        assertEquals(ex1, 0);// cardlist 1 has 3 jack , 1 anchors, 1 houses , 1 crosses should fullfill this expedition
        assertEquals(gameController.getGame().getActivePlayer().getCoinsCount(),3);// 3 coins from expedition card should be added to player
        assertEquals(gameController.getGame().getActivePlayer().getPersonalDisplay().size(),21);// 4 cards should be take from player
        int ex2 = discoverController.startExpedition(expedition,cardList2); //cardlist 2 has 1 jack , 2 anchors, 2 houses , 2 crosses should be too much for this expedition
        assertEquals(ex2,2);
        int ex4 = discoverController.startExpedition(expedition,cardList4);//cardlist 4 has 1 jack , 2 anchors, 2 houses , 22 crosses should be too much for this expedition
        assertEquals(ex4, 2);
        int ex3 = discoverController.startExpedition(expedition,cardList3);//cardlist 3 has 1 jack , 2 anchors, 1 houses , 1 crosses should not fullfill this expedition
        assertEquals(ex3, 1);
        int ex5 = discoverController.startExpedition(expedition,cardList5);//cardlist 5 has 1 jack , 2 anchors, 0 houses , 2 crosses should not fullfill this expedition
        assertEquals(ex5,1);
        int ex6 = discoverController.startExpedition(expedition,cardList6);//cardlist 6 has 1 jack , 2 anchors, 2 houses , 0 crosses should not fullfill this expedition
        assertEquals(ex6,1);
        int ex7 = discoverController.startExpedition(expedition,cardList7);
        assertEquals(ex7,1);

    }
}