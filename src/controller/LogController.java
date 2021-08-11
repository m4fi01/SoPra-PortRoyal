package controller;

import model.Card;
import model.Game;
import model.GameLog;
import model.Player;

/**
 * Class Log Controller for undo and redo functions
 * @author Hoang Hai Nguyen
 * @author Thomas Alexander Hövelmann
 */
public class LogController {
	/**
	 * The game controller
	 */
	private GameController gameController;

	/**
	 * Called to end logging while adding the log entries specific for the specified action. NOTE: Performs no
	 * plausibility checks.
	 *
	 * @param lastAction The action performed.
	 * @param card The card possibly involved in this action.
	 * @param player The player possibly involved in this action.
	 */
	public void log(Action lastAction, Card card, Player player) {
		Game gameToLog = this.gameController.getGame();
		switch(lastAction) {
			case START_GAME : log(gameToLog,"Ein neues Spiel mit " + gameToLog.getPlayersList().size() + " Spielern wurde gestartet."); break;
			case START_DISCOVER_PHASE : log(gameToLog,"Die 'Entdecken'-Phase hat begonnen. " + gameToLog.getActivePlayer().getName() + " ist der aktive und agierende Spieler."); break;
			case START_TRADE_AND_HIRE_PHASE : log(gameToLog,"Die 'Handeln und Heuern'-Phase hat begonnen." + gameToLog.getActivePlayer().getName() + " ist der aktive und agierende Spieler."); break;
			case START_EXPEDITION : log(gameToLog,"Es wurde auf Kosten von " + card.getAnchors() + " Ankern, " + card.getCrosses() + " Kreuzen und " + card.getHouses() + " Häusern eine Expedition mit " + card.getVictoryPoints() + " Siegpunkten erfüllt."); break;
			case HIRE : log(gameToLog,"Es wurde auf Kosten von " + card.getCoinValue() + " Münzen eine Karte '" + card.getCardName() + "' mit "  + card.getVictoryPoints() + " Siegpunkten aufgenommen."); break;
			case TRADE : log(gameToLog,"Es wurde auf ein Schiff '" + card.getCardName() + "' im Wert von " + card.getCoinValue() + " eingetauscht."); break;
			case DRAW_CARD : log(gameToLog,"Es wurde eine Karte '" + card.getCardName() + "' gezogen."); break;
			case REPEL_SHIP : log(gameToLog,"Das Schiff wurde abgewehrt."); break;
		}
		if(player != null) {
			log(gameToLog,"Entsprechend erhielt " + player.getName() + " eine Münze als Kompensation.");
		}
		this.gameController.getGameLog().add(gameToLog);
		this.gameController.stopLogging();
	}

	/**
	 * Appends the specified log entry to the log of the specified game state.
	 *
	 * @param game The game state to add the log entry to.
	 * @param logEntry The log entry to add.
	 */
	public void log(Game game, String logEntry) {
		game.getActionLog().add(logEntry);
	}

	/**
	 * Copies the current game state as base for the now started logging process.
	 */
	public void startLogging() {
		Game currentGameToLog = new Game(this.gameController.getGame());
		this.gameController.startLogging(currentGameToLog);
	}

	/**
	 * Stops logging without actually saving the newly created game state while logging was on.
	 */
	public void cancelLogging() {
		this.gameController.stopLogging();
	}

	/**
	 * Moves back to the latest game state in which the currently acting player was the acting player if available. If
	 * this was successful this player also gets added to the list of high score unworthy players.
	 *
	 * @return {@code true} if, and only if, the game state was changed to the latest game state chronologically before
	 *         the current game state (at call of method) were the acting players are the same.
	 */
	public boolean undo() {
		GameLog gameLog = this.gameController.getGameLog();
		Game game = gameLog.get();
		Player currentActingPlayer = game.getActingPlayer();
		int currentIndex = gameLog.getIndex();
		if(gameLog.hasPrevious()) {
			game = gameLog.getPrevious();
		} else {
			return false;
		}
		while(gameLog.hasPrevious() && !currentActingPlayer.equals(game.getActingPlayer())) {
			game = gameLog.getPrevious();
		}
		if(currentActingPlayer.equals(game.getActingPlayer())) { // No previous move of this player available.
			gameLog.addToUnworthyList(currentActingPlayer.getPlayerID());
			return true;
		} else {
			gameLog.jump(currentIndex);
			return false;
		}
	}

	/**
	 * Only callable after undo was called successfully before and no new game state was added since.
	 *
	 * @throws IllegalStateException if this method is called when there are no game states chronologically after the
	 *                               current game state.
	 */
	public void redo() {
		GameLog gameLog = this.gameController.getGameLog();
		Game game = gameLog.get();
		Player currentActingPlayer = game.getActingPlayer();
		if(gameLog.hasNext()) {
			game = gameLog.getNext();
		} else {
			new IllegalStateException();
		}
		while(gameLog.hasNext() && !currentActingPlayer.equals(game.getActingPlayer())) {
			game = gameLog.getNext();
		}
	}

	/**
	 * Contructor of the class
	 *
	 * @param gameController the game Controller
	 */
	public LogController(GameController gameController){
		this.gameController = gameController;
	}
}
