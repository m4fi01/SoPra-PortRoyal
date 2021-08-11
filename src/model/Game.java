package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Stores all important information of the game
 * @author Hoang Hai Nguyen
 * class Game
 */
public class Game implements Serializable {
	/**
	 * Action points of the current game
	 */
	private int actionPoints;

	/**
	 * Draw Pile for the current game
	 */
	private DrawPile drawPile;

	/**
	 * ArrayList of {@code Card} objects as the discard Pile of the current game
	 */
	private ArrayList<Card> discardPile ;

	/**
	 * List of players of the current game
	 */
	private ArrayList<Player> players ;

	/**
	 * ArrayList of {@code Card} objects as the harbor display of the current game
	 */
	private ArrayList<Card> harborDisplay ;

	/**
	 * ArrayList of {@code Card} objects as the expedition display of the current game
	 */
	private ArrayList<Card> expeditionDisplay ;

	/**
	 * Textual representation of all actions in the game.
	 */
	private ArrayList<String> actionLog;

	/**
	 * The player that was set as the start player of the current game
	 */
	private Player startPlayer;

	/**
	 * The currently active player (started the discover phase)
	 */
	private Player activePlayer;

	/**
	 * The currently acting player
	 */
	private Player actingPlayer;

	/**
	 * Last card that was drawn from the draw pile
	 */
	private Card lastCardDrawn;

	private GamePhase currentGamePhase;

	private final Card[] CARDS;
	private final boolean[] FACE_UP;


	//
	// Constructor /////////////////////////////////////////////////
	//

	/**
	 * Contructor of Game. Default: discover phase
	 * @param startPlayer the Startplayer of this game
	 * @param drawPile the drawpile of this game
	 * @param playersList the player list of this game
	 */
	public Game (Player startPlayer, DrawPile drawPile, Card finalExpedition, ArrayList<Player> playersList){
		this.actionPoints = 0;
		this.startPlayer = startPlayer;
		this.activePlayer = startPlayer;
		this.actingPlayer = startPlayer;
		this.drawPile = drawPile;
		this.players = playersList;
		this.currentGamePhase = GamePhase.DISCOVER;
		this.actionLog = new ArrayList<>();
		this.discardPile = new ArrayList<>();
		this.harborDisplay = new ArrayList<>();
		this.expeditionDisplay = new ArrayList<>();
		if(finalExpedition == null) {
			this.CARDS = new Card[drawPile.getPile().size()];
			this.FACE_UP = new boolean[drawPile.getPile().size()];
			int index = 0;
			for(Card card : drawPile.getPile()) {
				this.CARDS[index] = card;
				this.FACE_UP[index] = false;
				index++;
			}
		} else {
			this.CARDS = new Card[drawPile.getPile().size() + 1];
			this.FACE_UP = new boolean[drawPile.getPile().size() + 1];
			this.CARDS[0] = finalExpedition;
			this.FACE_UP[0] = true;
			this.expeditionDisplay.add(finalExpedition);
			int index = 1;
			for(Card card : drawPile.getPile()) {
				this.CARDS[index] = card;
				this.FACE_UP[index] = false;
				index++;
			}
		}
	}

	/**
	 * Copy constructor for logging. Creates a log friendly deep alike copy of the given game state.
	 *
	 * @param previousGameState The game state to copy for logging.
	 */
	public Game(Game previousGameState) {
		this.CARDS = previousGameState.CARDS;
		this.FACE_UP = previousGameState.FACE_UP.clone();
		this.drawPile = new DrawPile(previousGameState.drawPile);
		this.players = new ArrayList<>();
		for(Player player : previousGameState.players) {
			if(player instanceof AIPlayer) {
				AIPlayer logPlayer = new AIPlayer((AIPlayer)player);
				this.players.add(logPlayer);
				if(logPlayer.equals(previousGameState.startPlayer)) {
					this.startPlayer = logPlayer;
				}
				if(logPlayer.equals(previousGameState.activePlayer)) {
					this.activePlayer = logPlayer;
				}
				if(logPlayer.equals(previousGameState.actingPlayer)) {
					this.actingPlayer = logPlayer;
				}
			} else {
				Player logPlayer = new Player(player);
				this.players.add(logPlayer);
				if(logPlayer.equals(previousGameState.startPlayer)) {
					this.startPlayer = logPlayer;
				}
				if(logPlayer.equals(previousGameState.activePlayer)) {
					this.activePlayer = logPlayer;
				}
				if(logPlayer.equals(previousGameState.actingPlayer)) {
					this.actingPlayer = logPlayer;
				}
			}
		}
		this.currentGamePhase = previousGameState.currentGamePhase;
		this.actionLog = new ArrayList<>(previousGameState.actionLog);
		this.discardPile = new ArrayList<>(previousGameState.discardPile);
		this.harborDisplay = new ArrayList<>(previousGameState.harborDisplay);
		this.expeditionDisplay = new ArrayList<>(previousGameState.expeditionDisplay);
		this.lastCardDrawn = previousGameState.lastCardDrawn;
	}

	//
	// Setters ///////////////////////////////////////////////////
	//

	/**
	 * Sets the number of action points of the acting player.
	 *
	 * @param actionPoints New number of action points of the acting player.
	 */
	public void setActionPoints(int actionPoints) {
		this.actionPoints = actionPoints;
	}

	/**
	 * Sets active Player (Player that on turn in Discover Phase)
	 * @param activePlayer the Player that on turn in Discover Phase
	 */
	public void setActivePlayer(Player activePlayer){
		this.activePlayer=activePlayer;
	}

	/**
	 * Sets acting Player (Player that on turn in Trade and Hire Phase)
	 * @param actingPlayer  the Player that on turn in Trade and Hire Phase
	 */
	public void setActingPlayer(Player actingPlayer){
		this.actingPlayer=actingPlayer;
	}

	/**
	 * Sets Game's DrawPile
	 * @param drawPile the new draw pile to be set
	 */
	public void setDrawPile(DrawPile drawPile) {
		this.drawPile = drawPile;
	}

	/**
	 * Sets Game's last drawn card
	 * @param lastCardDrawn that last drawn card
	 */
	public void setLastCardDrawn(Card lastCardDrawn){
		this.lastCardDrawn=lastCardDrawn;
	}

	/**
	 * Sets Game's discard pile
	 * @param discardPile the new discard pile to be set
	 */
	public void setDiscardPile(ArrayList<Card> discardPile) {
		this.discardPile = discardPile;
	}

	/**
	 * Sets Game's harbor display
	 * @param harborDisplay the new harbor display  to be set
	 */
	public void setHarborDisplay(ArrayList<Card> harborDisplay){
		this.harborDisplay=harborDisplay;
	}

	/**
	 * Sets Game's expedition display
	 * @param expeditionDisplay the new expedition display  to be set
	 */
	public void setExpeditionDisplay(ArrayList<Card> expeditionDisplay){
		this.expeditionDisplay=expeditionDisplay;
	}

	/**
	 * Sets Game's Player List
	 * @param playersList the new player list  to be set
	 */
	public void setPlayersList(ArrayList<Player> playersList){
		this.players=playersList;
	}

	/**
	 * Sets Game's current game phase
	 * @param currentGamePhase the new game phase to be set
	 */
	public void setCurrentGamePhase(GamePhase currentGamePhase) {
		this.currentGamePhase = currentGamePhase;
	}

	/**
	 * Sets for a given card weather this card is face up or face down.
	 *
	 * @param card The card whose orientation is to be determined.
	 * @param faceUp {@code true} if the card shall be face up, {@code false} otherwise.
	 */
	public void setFaceUp(Card card, boolean faceUp) {
		for(int i = 0; i < this.CARDS.length; i++) {
			if(card == this.CARDS[i]) {
				this.FACE_UP[i] = faceUp;
				return;
			}
		}
	}

	//
	// Adders //////////////////////////////////////////////////
	//

	/**
	 * Adds the the specified textual representation of an action to the log.
	 *
	 * @param action Textual representation of the action to log.
	 */
	public void addActionToLog(String action) {
    	this.actionLog.add(action);
	}

	/**
	 * Adds new Card to discard pile
	 * @param card that card to be added
	 */
	public void addToDiscardPile(Card card){
		this.discardPile.add(card);
	}

	/**
	 * Adds new Card to harbor display
	 * @param card that card to be added
	 */
	public void addToHarborDisplay(Card card){
		this.harborDisplay.add(card);
	}

	/**
	 * Adds new card to expedition display
	 * @param card that card to be added
	 */
	public void addToExpeditionDisplay(Card card){
		this.expeditionDisplay.add(card);
	}

	/**
	 * Adds new Player to player list
	 * @param player that player to be added
	 */
	public void addToPlayersList(Player player){
		this.players.add(player);
	}


	//
	// Getters /////////////////////////////////////////////////
	//

	/**
	 * Returns the game's discard pile as array list of card (or empty array list of card if discard pile is empty)
	 * @return discard pile
	 */
	public ArrayList<Card> getDiscardPile(){
		return discardPile;
	}

	/**
	 * Returns the game's acting player ( that player that on turn in trade and hire phase )
	 * @return acting player
	 */
	public Player getActingPlayer(){
		return actingPlayer;
	}

	/**
	 * Returns the game's draw pile
	 * @return draw pile
	 */
	public DrawPile getDrawPile(){
		return drawPile;
	}

	/**
	 * Returns the game's harbor display as array list of card (or empty array list of card if harbor display is empty)
	 * @return harbor display
	 */
	public ArrayList<Card> getHarborDisplay(){
		return harborDisplay;
	}

	/**
	 * Returns the game's expedition display as array list of card (or empty array list of card if expedition display is empty)
	 * @return expedition display
	 */
	public ArrayList<Card> getExpeditionDisplay(){
		return expeditionDisplay;
	}

	/**
	 * Returns the Game's player list as array list of players
	 * @return player list
	 */
	public ArrayList<Player> getPlayersList(){
		return players;
	}

	/**
	 * Returns the Game's start player
	 * @return start player
	 */
	public Player getStartPlayer(){
		return startPlayer;
	}

	/**
	 * Returns the Game's active player (player that on turn in Discover phase)
	 * @return active player
	 */
	public Player getActivePlayer(){
		return activePlayer;
	}

	/**
	 * Returns the Game's action point, start with 0
	 * @return action point
	 */
	public int getActionPoints(){
		return actionPoints;
	}

	/**
	 * Returns the Game's last drawn card or null if no card was drawn in game
	 * @return last drawn card
	 */
	public Card getLastCardDrawn(){
		return lastCardDrawn;
	}

	/**
	 * Returns the current game phase.
	 * @return The current game phase.
	 */
	public GamePhase getCurrentGamePhase() {
		return this.currentGamePhase;
	}

	/**
	 * Returns the log of all actions taken in this game state.
	 *
	 * @return The log of all actions taken in this game state.
	 */
	public ArrayList<String> getActionLog() {
		return this.actionLog;
	}

	/**
	 * Returns the orientation of the given card. Uses object identity instead of equality as comparator.
	 *
	 * @param card The card which orientation is requested.
	 * @return {@code true} if, and only if, the specified card is facing up.
	 */
	public boolean isFaceUp(Card card) {
		for(int i = 0; i < this.CARDS.length; i++) {
			if(card == this.CARDS[i]) {
				return this.FACE_UP[i];
			}
		}
		throw new IllegalArgumentException();
	}

	//
	// Others functions /////////////////////////////////////////
	//

	/**
	 * Removes player with player id from player list
	 * @param playerID the id of the player ought to remove
	 */
	public void removeFromPlayersList(int playerID){
		players.removeIf(player -> {return player.getPlayerID() == playerID;});
	}

	/**
	 * Removes card from harbourDisplay by index
	 * @param index the index of the card which should be removed from the harbourDisplay
	 */
	public void removeFromHarborDisplay(int index){
		this.harborDisplay.remove(index);
	}

	/**
	 * Adds an amount of points to game's action point
	 * @param points the amount of points that will be added
	 */
	public void addActionPoints(int points){
		this.actionPoints = this.actionPoints+points;
	}

}
