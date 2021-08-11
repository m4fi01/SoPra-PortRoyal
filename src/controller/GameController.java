package controller;

import ai.AIController;
import model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Controls the game "Port Royal" and can start a new game or end a game.
 * Main controller of the game that knows all the other controllers.
 *
 * @author Leon Krick
 * @author Thomas Alexander Hövelmann
 */
public class GameController {
	/**
	 * Specifies the number of players in a game of Port Royal necessary so that the sixth expedition card is needed
	 * according to game rules.
	 */
	public static final int NUMBER_OF_PLAYERS_FOR_FINAL_EXPEDITION = 5;

	/**
	 * Specifies the number of coins each player gets at the start of the game.
	 */
	public static final int NEW_GAME_NUMBER_OF_COINS_FOR_EACH_PLAYER = 3;

	private final ArrayList<String> HIGH_SCORE;

	private GameLog gameLog;

	private final PlayerController PLAYER_CONTROLLER;
	private final DiscoverController DISCOVER_CONTROLLER;
	private final TradeAndHireController TRADE_AND_HIRE_CONTROLLER;
	private final LogController LOG_CONTROLLER;
	private final CardController CARD_CONTROLLER;
	private final PileController PILE_CONTROLLER;
	private final AIController AI_CONTROLLER;
	private final IOController IO_CONTROLLER;

	private boolean logging;
	private Game tempGame;

	/**
	 * Returns the playerController
	 * @return {@code PlayerController} object
	 */
	public PlayerController getPlayerController() {
		return this.PLAYER_CONTROLLER;
	}

	/**
	 * Returns the discoverController
	 * @return {@code DiscoverController} object
	 */
	public DiscoverController getDiscoverController() {
		return this.DISCOVER_CONTROLLER;
	}

	/**
	 * Returns the tradeAndHireController
	 * @return {@code TradeAndHireController} object
	 */
	public TradeAndHireController getTradeAndHireController() {
		return this.TRADE_AND_HIRE_CONTROLLER;
	}

	/**
	 * Returns the logController
	 * @return {@code LogController} object
	 */
	public LogController getLogController() {
		return this.LOG_CONTROLLER;
	}

	/**
	 * Returns the cardController
	 * @return {@code CardController} object
	 */
	public CardController getCardController() {
		return this.CARD_CONTROLLER;
	}

	/**
	 * Returns the pileController
	 * @return {@code PileController} object
	 */
	public PileController getPileController() {
		return this.PILE_CONTROLLER;
	}

	/**
	 * Returns the AIController
	 * @return {@code AIController} object
	 */
	public AIController getAIController() {
		return this.AI_CONTROLLER;
	}

	/**
	 * Returns the IOController
	 * @return {@code IOController} object
	 */
	public IOController getIOController(){
		return this.IO_CONTROLLER;
	}

	/**
	 * Returns the high score list
	 * @return ArrayList of {@code String} objects that contains the high scores
	 */
	public ArrayList<String> getHighScore() {
		return this.HIGH_SCORE;
	}

	/**
	 * Returns the currently active game
	 * @return {@code Game} object that is the currently active game
	 */
	public Game getGame() {
		if(logging) {
			return this.tempGame;
		} else {
			return this.gameLog.get();
		}
	}

	/**
	 * Called at the beginning of a action to log. Sets the game in logging mode, which means that GameController
	 * returns a temporary game state if getGame is called. Later the logging can be canceled (temporary game state gets
	 * deleted) or finished successfully (temporary game state gets saved as the current game state).
	 *
	 * @param game The temporary game state.
	 */
	public void startLogging(Game game) {
		this.logging = true;
		this.tempGame = game;
	}

	/**
	 * Called at the end of a action to log. Exits the logging mode for the game, which means that GameController
	 * returns the current game state if getGame is called.
	 */
	public void stopLogging() {
		this.logging = false;
		this.tempGame = null;
	}

	/**
	 * Returns the game log
	 * @return {@code GameLog} object that represents the game log
	 */
	public GameLog getGameLog() {
		return this.gameLog;
	}

	/**
	 * Sets the current game log. Used to load saved games.
	 *
	 * @param loadedGame The saved game to be loaded.
	 * @throws NullPointerException if the parameter is {@code null}.
	 */
	public void setGameLog(GameLog loadedGame) {
		if(loadedGame == null) {
			throw new NullPointerException();
		}
		this.gameLog = loadedGame;
	}

	/**
	 * Checks whether two or more ships of same colour lie in the harbour display
	 *
	 * @return true for too many ships of same colour in harbour display
	 */
	public boolean checkShipColours(){
		ArrayList<Card> harbourDisplay = getGame().getHarborDisplay();
		int size = harbourDisplay.size();
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				if((i != j) && (harbourDisplay.get(i).getCardType() == CardType.SHIP) && (harbourDisplay.get(j).getCardType() == CardType.SHIP) && (harbourDisplay.get(i).getCardColour() == harbourDisplay.get(j).getCardColour())){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Is called if the winning condition is {@code true}. Updates the high score list if the winning players are high
	 * score worthy.
	 *
	 * @return The list of players who won the game.
	 */
	public ArrayList<Player> endGame() {
		//get all worthy players
		ArrayList<Player> players = new ArrayList<>();
		ArrayList<Integer> unworthyPlayerIDs = gameLog.getUnworthyPlayerIDs();
		for(Player player : gameLog.get().getPlayersList()) {
			if(!unworthyPlayerIDs.contains(player.getPlayerID())) {
				players.add(player);
			}
		}
		ArrayList<Player> winners = new ArrayList<>();
		winners.add(players.get(0));
		for(Player player : players){//fill all players with most victory points and most coins into winners
			if(player.getVictoryPointsCount() > winners.get(0).getVictoryPointsCount()){//player has most victory points up until now
				winners.clear();//he is the only winner up until now
				winners.add(player);
			}
			else if((!player.equals(winners.get(0))) && (player.getVictoryPointsCount() == winners.get(0).getVictoryPointsCount())){//player has as many victory points as the current known maximum
				if(player.getCoinsCount() > winners.get(0).getCoinsCount()){//current player has most coins
					winners.clear();//he is the only winner up until now
					winners.add(player);
				}
				else if(player.getCoinsCount() == winners.get(0).getCoinsCount()){//current player has as many coins as the other winners up until now
					winners.add(player);
				}
			}
		}
		String timeStamp = LocalDateTime.now().toString();
		for(Player winner : winners){//add al worthy players to the highScore list
			HIGH_SCORE.add("" + winner.getVictoryPointsCount() + ";" + timeStamp + ";" + winner.getName());
		}
		Collections.sort(this.HIGH_SCORE);
		this.IO_CONTROLLER.saveHighScore(new File(IOController.HIGH_SCORE_PATH));
		return winners;
	}

	/**
	 * Starts a new game, creates the draw pile, sets the player list and adds the new game to the game log.
	 *
	 * @param players ArrayList of Player-objects that want to participate in the game.
	 * @param initialPileFile File for the initial draw pile, {@code null} if the initial pile should be random.
	 * @param simulationSpeed The simulation speed of players with artificial intelligence.
	 * @throws NullPointerException if the ArrayList of Player-objects is {@code null}.
	 * @throws FileNotFoundException if the file for the initial pile could not be found.
	 */
	public void newGame(ArrayList<Player> players, File initialPileFile, int simulationSpeed) throws FileNotFoundException {
		if(players == null) {
			throw new NullPointerException();
		}
		Player startPlayer = players.get(0);
		DrawPile drawPile;
		if(initialPileFile == null){ // Randomised DrawPile.
			drawPile = PILE_CONTROLLER.generateInitialPile(null);
		}
		else{ // DrawPile with predefined card order.
			ArrayList<String> initialPileAsStrings = IO_CONTROLLER.loadPile(initialPileFile);
			ArrayList<Card> initialPile = CARD_CONTROLLER.generatePile(initialPileAsStrings);
			drawPile = PILE_CONTROLLER.generateInitialPile(initialPile);
		}
		Game newGame;
		if(players.size() == NUMBER_OF_PLAYERS_FOR_FINAL_EXPEDITION) {
			Card finalExpedition = this.CARD_CONTROLLER.generateFinalExpedition();
			newGame = new Game(startPlayer,drawPile,finalExpedition,players);
		} else {
			newGame = new Game(startPlayer,drawPile,null,players);
		}
		this.logging = true;
		this.tempGame = newGame;
		for(Player player : players){
			if(player instanceof AIPlayer){ // Sets simulation speed for all players with artificial intelligence.
				((AIPlayer)player).setSimulationSpeed(simulationSpeed);
			}
			this.PLAYER_CONTROLLER.drawCoins(player,NEW_GAME_NUMBER_OF_COINS_FOR_EACH_PLAYER);
		}
		this.LOG_CONTROLLER.log(Action.START_GAME,null,null);
		this.LOG_CONTROLLER.log(Action.START_DISCOVER_PHASE,null,null);
	}

	/**
	 * Constructor that generates and sets the other controllers
	 */
	public GameController(){
		this.logging = false;
		this.tempGame = null;
		this.AI_CONTROLLER = new AIController(this);
		this.CARD_CONTROLLER = new CardController();
		this.PILE_CONTROLLER = new PileController(this);
		this.DISCOVER_CONTROLLER = new DiscoverController(this);
		this.LOG_CONTROLLER = new LogController(this);
		this.IO_CONTROLLER = new IOController(this);
		ArrayList<String> highScore = IO_CONTROLLER.loadHighScore(new File(IOController.HIGH_SCORE_PATH));
		if(highScore == null) {
			highScore = new ArrayList<>();
		}
		this.HIGH_SCORE = highScore;
		this.PLAYER_CONTROLLER = new PlayerController(this);
		this.TRADE_AND_HIRE_CONTROLLER = new TradeAndHireController(this);
		this.gameLog = new GameLog();
	}
}
