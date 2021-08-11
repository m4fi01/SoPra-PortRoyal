package controller;

import junit.framework.TestCase;
import model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * @author Mohammad Almidani
 *
 * testing class for the CardController
 */
public class CardControllerTest extends TestCase {

    private GameController gameController;

    /**
     * Setup before testing
     * @throws Exception exception
     */
    public void setUp() throws Exception {
        // Creates a new and empty high score file for testing.
        File highScoreFile = new File(IOController.HIGH_SCORE_PATH);
        if(highScoreFile.exists()) {

            Files.delete(highScoreFile.toPath());
        }
        highScoreFile.createNewFile();
        highScoreFile.deleteOnExit(); // Deletes the newly created file after testing.

        try {
            gameController = new GameController();
            ArrayList<Player> players = new ArrayList<>();
            Player player1 = new Player("one");
            players.add(player1);
            gameController.newGame(players, null, 500);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Test with null array of string
     * @throws NullPointerException
     */
    public void testGeneratePileNullArgument(){
        try{
            ArrayList<Card> cards = gameController.getCardController().generatePile(null);
        }
        catch ( NullPointerException nullPointerException){
            assertEquals(0,0);
        }
    }

    /**
     * Test with array of string but false length
     * @throws IllegalArgumentException
     */
    public void testGeneratePileIllegalArgument(){
        try{
            ArrayList<String> cardString = new ArrayList<String>();
            for(int i = 0 ; i < 150 ; i++){
                cardString.add("coin");
            }
            ArrayList<Card> cards = gameController.getCardController().generatePile(cardString);
        }
        catch ( IllegalArgumentException illegalArgument){
            assertEquals(0,0);
        }
    }

    /**
     * Test with null string in string array at parameter
     * @throws NullPointerException
     */
    public void testGenerateCardNull(){
        try{
            ArrayList<String> cardString = new ArrayList<String>();
            for(int i = 0 ; i < 119 ; i++){
                cardString.add(null);
            }
            ArrayList<Card> cards = gameController.getCardController().generatePile(cardString);

        }catch (NullPointerException nullPointerException){
            assertEquals(0,0);
        }
    }

    /**
     * Test with array of string but false string types
     * @throws IllegalArgumentException
     */
    public void testGenerateCardIllegalArgument(){
        try{
            ArrayList<String> cardString = new ArrayList<String>();
            for(int i = 0 ; i < 119 ; i++){
                cardString.add("aaaaaaaaaaaaaaaaaaaaa");
            }
            ArrayList<Card> cards = gameController.getCardController().generatePile(cardString);

        }catch (IllegalArgumentException illegalArgument){
            assertEquals(0,0);
        }
    }



    /**
     * Testing methode generatePile() by loading an initial pile from a file, generating an initial pile from that and
     * then checking the first few cards.
     */
    public void testGeneratePile() {
        try {
            ArrayList<String> initialPileAsStrings = gameController.getIOController().loadPile(new File(IOController.STANDARD_PILE_PATH));
            ArrayList<Card> cards = gameController.getCardController().generatePile(initialPileAsStrings);

            for (Card card : cards) {
                assertNotNull(card);
            }
            assertEquals(cards.get(0).getCardName(), CardName.EXPEDITION);
            assertEquals(cards.get(1).getCardName(), CardName.EXPEDITION);
            assertEquals(cards.get(2).getCardName(), CardName.EXPEDITION);
            assertEquals(cards.get(3).getCardName(), CardName.EXPEDITION);
            assertEquals(cards.get(4).getCardName(), CardName.EXPEDITION);

            assertNotSame(cards.get(5).getCardName(), CardName.EXPEDITION);

            assertEquals(cards.get(5).getCardType(), CardType.TAX_INCREASE);
            assertEquals(cards.get(6).getCardType(), CardType.TAX_INCREASE);
            assertEquals(cards.get(7).getCardType(), CardType.TAX_INCREASE);
            assertEquals(cards.get(8).getCardType(), CardType.TAX_INCREASE);

            assertEquals(cards.get(9).getCardName(), CardName.ADMIRAL);
            assertEquals(cards.get(9).getCardColour(), CardColour.NONE);
            // and so on...
            //.
            //.
            assertEquals(cards.get(118).getCardName(), CardName.SKIFF);

            //trying out with 5 players in game
            Player player2 = new Player("two");
            Player player3 = new Player("three");
            Player player4 = new Player("four");
            Player player5 = new Player("five");
            gameController.getPlayerController().addPlayer(player2);
            gameController.getPlayerController().addPlayer(player3);
            gameController.getPlayerController().addPlayer(player4);
            gameController.getPlayerController().addPlayer(player5);
            initialPileAsStrings = gameController.getIOController().loadPile(new File(IOController.STANDARD_PILE_PATH));
            cards = gameController.getCardController().generatePile(initialPileAsStrings);

            for (Card card : cards) {
                assertNotNull(card);
            }
            assertEquals(cards.get(0).getCardName(), CardName.EXPEDITION);
            assertEquals(cards.get(1).getCardName(), CardName.EXPEDITION);
            assertEquals(cards.get(2).getCardName(), CardName.EXPEDITION);
            assertEquals(cards.get(3).getCardName(), CardName.EXPEDITION);
            assertEquals(cards.get(4).getCardName(), CardName.EXPEDITION);
            //TODO uncomment test when functionality implemented
           // assertEquals(cards.get(5).getCardName(), CardName.EXPEDITION); //not yet Implemented
        }catch (FileNotFoundException | IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    /**
     * Testing methode generateFinalExpedition() by generating the final expedition and then checking the values of that card.
     */
    public void testGenerateFinalExpedition() {
        Card finalExpedition = gameController.getCardController().generateFinalExpedition();

        assertEquals(finalExpedition.getHouses(),1);
        assertEquals(finalExpedition.getAnchors(),1);
        assertEquals(finalExpedition.getCrosses(),1);
        assertEquals(finalExpedition.getCardName(),CardName.EXPEDITION);
    }
}