package controller;

import model.*;

import java.util.ArrayList;

/**
 * Controls the Players
 * @author Hoang Hai Nguyen
 */
public class PlayerController {
	/**
	 * The number of victory points necessary to fulfil the victory condition.
	 */
	private static final int WIN_CONDITION_VICTORY_POINTS = 12;

	/**
	 *	gameController that knows the data structure and the other controllers
	 */
	private GameController gameController;


	/**
	 * Constructor that sets the gameController and the first circleEnd value to  true
	 * @param gameController main controller of the game that knows the data structure and the other controllers
	 */
	public PlayerController(GameController gameController){
		this.gameController=gameController;
	}

	/**
	 * Adds player with player name to list
	 * @param player the player
	 * @return true if no exception, false if there is exception
	 */
	public boolean addPlayer(Player player) {
    	try {
			gameController.getGame().addToPlayersList(player);
			return true;
		}
    	catch(Exception exception){
    		return false;
    	}
	}

	/**
	 * Adds new ai player with name and level to list
	 * @param aiName AI name
	 * @param level AI level
	 * @return true if no exception, false if there is exception
	 */
	public boolean addAI(String aiName, Level level) {
		try {
			AIPlayer aiPlayer=new AIPlayer(level,aiName);
			gameController.getGame().addToPlayersList(aiPlayer);
			return true;
		}
		catch(Exception exception){return false;}
	}

	/**
	 * Removes the player from game's player list
	 * @param player the player to remove
	 * @return true if remove succeed, false if failed
	 */
	public boolean removePlayer(Player player) {
		try{
			gameController.getGame().removeFromPlayersList(player.getPlayerID());
			return true;
		}catch (Exception exception){return false;}
	}

	/**
	 * Checks if there is a  player in game reached win condition ( has more than 12 victory points)
	 * @return true if there is a victory player, false if there is no
	 */
	public boolean checkWinCondition() {
		ArrayList<Player>playersList = gameController.getGame().getPlayersList();
		for(int i = 0; i < playersList.size(); i++){
			if(playersList.get(i).getVictoryPointsCount() >= WIN_CONDITION_VICTORY_POINTS){return true;}
		}
		return false;
	}

	/**
	 * Sets the next acting player and output that player
	 * @return next acting player
	 */
	public Player nextActingPlayer() {
		int index = gameController.getGame().getPlayersList().indexOf(gameController.getGame().getActingPlayer());// get this acting player index
		if(index==gameController.getGame().getPlayersList().size()-1){ // list end ?
			index=-1;
		}
		gameController.getGame().setActingPlayer(gameController.getGame().getPlayersList().get(index+1));// set next acting player in game
		return gameController.getGame().getActingPlayer(); // return next active player
    }

	/**
	 * Sets the next active player and acting player then output that player
	 * @return active player
	 */
	public Player nextActivePlayer() {
		int index = gameController.getGame().getPlayersList().indexOf(gameController.getGame().getActivePlayer());// get this active player index
		if(index==gameController.getGame().getPlayersList().size()-1){ // list end ?
			index=-1;
		}
		gameController.getGame().setActivePlayer(gameController.getGame().getPlayersList().get(index+1)); // set next active player in game
		gameController.getGame().setActingPlayer(gameController.getGame().getPlayersList().get(index+1));// set next acting player in game to active player
		return gameController.getGame().getActingPlayer(); // return next active player
	}

	/**
	 * Transfers 1 coin from acting player to active player
	 * @throws IllegalArgumentException if acting player has no coin
	 */
	public void transferCoinTo() {
		Player activePlayer= gameController.getGame().getActivePlayer();
		Player actingPlayer= gameController.getGame().getActingPlayer();
		Card coin = actingPlayer.removeCoin();
		if(coin == null){ throw new IllegalArgumentException(); }
		activePlayer.addCard(coin,false);
	 }
	/**
	 * Discards an amount of coins from acting player and add to discard pile
	 * @param amount tha amount of coins to be removed
	 * @throws IllegalArgumentException if acting player has not enough coin
	 */
	public void discardCoins(int amount) {
    	Player actingPlayer = gameController.getGame().getActingPlayer();
        discardCoins(actingPlayer, amount);
	}
	/**
	 * Discards an amount of coins from a player in parameter and add to discard pile
	 * @param player player whom coins to be removed
	 * @param amount amount of coins to be removed
	 * @throws IllegalArgumentException if player has not enough coin
	 */
	public void discardCoins(Player player, int amount) {
		Game game = gameController.getGame();
		for(int c = 0; c < amount; c++){
			Card coin = player.removeCoin();
			if(coin==null){throw new IllegalArgumentException();}
			game.setFaceUp(coin,true);
			game.addToDiscardPile(coin);
		}
	}
	/**
	 * Acting Player draws an amount of coins from draw pile
	 * @param amount the amount of coins to be drawn
	 */
	public void drawCoins(int amount) {
		Player actingPlayer = gameController.getGame().getActingPlayer();
		drawCoins(actingPlayer,amount);
	}
	/**
	 * The specified player draws the specified amount of coins.
	 * @param player The player who draws the coins.
	 * @param amount The amount of coins to be drawn.
	 */
	public void drawCoins(Player player, int amount) {
		Game game = gameController.getGame();
		DrawPile drawPile = gameController.getGame().getDrawPile();
		for(int i = 0; i < amount; i++ ){
			Card card = drawPile.pop();
			if(card == null){
				drawPile=gameController.getPileController().reorganisePile(gameController.getGame().getDiscardPile());
				if(drawPile.getPile().size()==0){
					System.out.println("EX");
					throw new IndexOutOfBoundsException();
				}
				card=drawPile.pop();
			}
			game.setFaceUp(card,false);
			player.addCard(card,false);
		}
		drawPile = createNewDrawPile(drawPile); // create a new drawPile, if the current drawPile is empty
		game.setDrawPile(drawPile);   // create a new drawPile, if the current drawPile is empty
	}

	/**
	 * This method is used to create a new drawPile, when the current drawPile is empty
	 * @param drawPile new {@code DrawPile} object
	 */
	private DrawPile createNewDrawPile(DrawPile drawPile){
		Game game = gameController.getGame();
		if(drawPile.getPile().isEmpty()){
			DrawPile newpile=this.gameController.getPileController().reorganisePile(game.getDiscardPile());
			game.setDiscardPile(new ArrayList<Card>());
			return newpile;
		}
		return drawPile;
	}
 }
