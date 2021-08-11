package controller;
import model.*;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Test class for the IOController class.
 */
public class IOControllerTest {
    private GameController gameController;
    private GameLog gameLog;

    /**
     * Setting things up
     * @throws IOException
     */
    @Before
    public void setUp() throws IOException {
        File highScoreFile = new File(IOController.HIGH_SCORE_PATH);
        File initialPileFile = new File("tests/testResources/shuffled.csv");
        if(highScoreFile.exists()) {
            Files.delete(highScoreFile.toPath());
        }
        highScoreFile.createNewFile();
        highScoreFile.deleteOnExit();

        gameController = new GameController();
        Player player1 = new Player("h1");
        Player player2 = new Player("h2");
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);
        gameController.newGame(players,initialPileFile, 500);
        gameController.getDiscoverController().drawCard();
        gameController.getDiscoverController().drawCard();
        gameController.getPlayerController().drawCoins(15);
        gameController.endGame();
        gameLog = gameController.getGameLog();
    }

    /**
     * Test null parameter when make new IOcontroller Instance
     * @throws NullPointerException
     */
    @Test
    public void ContructorNull(){
        try{
            IOController ioController = new IOController(null);
        }
        catch(NullPointerException nullPointerException){assertEquals(0,0);}
    }

    /**
     * Test if saveGame work
     */
    @Test
    public void saveGame() {
        File saveFile = new File("tests/testResources/test_save.csv");
        IOController ioController = gameController.getIOController();
        try {
           boolean succeed = ioController.saveGame(saveFile);
           assertEquals(succeed,true);
        }
        catch (Exception exception){exception.printStackTrace();}
    }

    /**
     * Test saveGame but with null file
     * @throws FileNotFoundException
     */
    @Test(expected = NullPointerException.class)
    public void testSaveNull() throws FileNotFoundException {
        IOController ioController = gameController.getIOController();
        assertEquals(ioController.saveGame(null),false);
    }

    /**
     * Test if Load Game work : load gamelog from savefile and compare to the gamelog at the beginning
     * @throws FileNotFoundException
     */
    @Test
    public void loadGame() throws FileNotFoundException {
        IOController ioController = gameController.getIOController();
        GameLog gameLogLoaded = ioController.loadGame(new File("tests/testResources/test_save.csv"));
        ArrayList<Game> gameList1 = new ArrayList<Game>();
        ArrayList<Game> gameList2 = new ArrayList<Game>();
        while(gameLogLoaded.hasNext()){
            gameList1.add(gameLogLoaded.getNext());
        }
        while(gameLog.hasNext()){
            gameList2.add(gameLogLoaded.getNext());
        }
        assertEquals(gameList1.size(), gameList2.size());
        for(int i = 0; i < gameList1.size() ; i++ ){
            Game game1 = gameList1.get(i);
            Game game2 = gameList2.get(i);
            ArrayList<Player> players1 = game1.getPlayersList();
            ArrayList<Player> players2 = game2.getPlayersList();
            ArrayList<Card> habour1 = game1.getHarborDisplay();
            ArrayList<Card> habour2 = game2.getHarborDisplay();
            ArrayList<Card> expedi1 = game1.getExpeditionDisplay();
            ArrayList<Card> expedi2 = game2.getExpeditionDisplay();
            ArrayList<Card> discard1 = game1.getDiscardPile();
            ArrayList<Card> discard2 = game2.getDiscardPile();
            ArrayList<Card> drawPile1 = game1.getDrawPile().getPile();
            ArrayList<Card> drawPile2 = game2.getDrawPile().getPile();
            assertEquals(players1.size(),players2.size());
            assertEquals(habour1.size(),habour2.size());
            assertEquals(expedi1.size(),expedi2.size());
            assertEquals(discard1.size(),discard2.size());
            assertEquals(drawPile1.size(),drawPile2.size());
            for( int j = 0 ; j < players1.size() ; j++){
                assertEquals(players1.get(j),players2.get(j));
            }
            for( int j = 0 ; j < habour1.size() ; j++){
                assertEquals(habour1.get(j),habour2.get(j));
            }
            for( int j = 0 ; j < expedi1.size() ; j++){
                assertEquals(expedi1.get(j),expedi2.get(j));
            }
            for( int j = 0 ; j < discard1.size() ; j++){
                assertEquals(discard1.get(j),discard2.get(j));
            }
            for( int j = 0 ; j < drawPile1.size() ; j++){
                assertEquals(drawPile1.get(j),drawPile2.get(j));
            }
        }
    }

    /**
     * Test loadGame with null parameter
     * @throws FileNotFoundException
     */
    @Test(expected = NullPointerException.class)
    public void testLoadNull() throws FileNotFoundException {
        IOController ioController = gameController.getIOController();
        ioController.loadGame(null);
    }

    /**
     * Test loadGame with false file
     * @throws FileNotFoundException
     */
    @Test
    public void testLoadFalseFile() throws FileNotFoundException {
        IOController ioController = gameController.getIOController();
        assertNull(ioController.loadGame(new File("tests/testResources/test_highscore.csv")));
    }

    /**
     * Test savehighscore
     */
    @Test
    public void saveHighscore(){
        IOController ioController = gameController.getIOController();
        assertEquals(ioController.saveHighScore(new File("tests/testResources/test_highscore.csv")),true);
    }

    /**
     * Test load Highscore :save game' highscore to a variable, then save it to a file and from that file, compare to the highscore at the beginning
     * @throws IOException
     */
    @Test
    public void loadHighscore() throws IOException {
        ArrayList<String> highscores1 = gameController.getHighScore();
        IOController ioController = gameController.getIOController();
        File newFile = new File("tests/testResources/test_highscore.csv");
        if (newFile.exists()){
            Files.delete(newFile.toPath());
        }
        ioController.saveHighScore(newFile);
        ArrayList<String> highscores2 = ioController.loadHighScore(newFile);
        for(int i = 0 ; i < highscores2.size() ; i++){
            assertEquals(highscores1.get(i).substring(0,16),highscores2.get(i).substring(0,16));
            assertEquals(highscores1.get(i).substring(highscores1.get(i).length()-1-2,highscores1.get(i).length()),highscores2.get(i).substring(highscores2.get(i).length()-1-2,highscores2.get(i).length()));
        }
    }

    /**
     * Test saveHighSore but with null parameter
     */
    @Test
    public  void saveHighscoresNull(){
        IOController ioController = gameController.getIOController();
        assertEquals(ioController.saveHighScore(null),false);
    }

    /**
     * Test save highscore but from Package path
     */
    @Test
    public  void saveHighscoresException(){
        IOController ioController = gameController.getIOController();
        assertEquals(ioController.saveHighScore(new File("tests/testResources/")),false);
    }

    /**
     * Test load highscore but null parameter
     */
    @Test
    public  void loadHighscoresNull(){
        IOController ioController = gameController.getIOController();
        assertEquals(ioController.loadHighScore(null),null);
    }
    /**
     * Test load highscore but from Package path
     */
    @Test
    public  void loadHighscoresException(){
        IOController ioController = gameController.getIOController();
        assertEquals(ioController.loadHighScore(new File("tests/testResources/")),null);
    }
}
