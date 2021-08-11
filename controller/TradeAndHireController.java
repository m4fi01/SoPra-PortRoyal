package controller;

import model.*;

import java.util.ArrayList;

/**
 *	Represents the phase trade and hire in the game port royal
 *  @author Jana Hauck
 */
public class TradeAndHireController {

	/**
	 *	gameController that knows the data structure and the other controllers
	 */
	public GameController gameController;

	/**
	 * Threshold ship colours for one action
	 */
	private final int SHIP_COLOURS_THRESHOLD_FOR_ONE_ACTION = 3;

	/**
	 * Threshold ship colours for two actions
	 */
	private final int SHIP_COLOURS_THRESHOLD_FOR_TWO_ACTIONS = 4;

	/**
	 * Threshold ship colours for three actions
	 */
	private final int SHIP_COLOURS_THRESHOLD_FOR_THREE_ACTIONS = 5;

	/**
	 * Needed harbor display size to activate admiral
	 */
	private final int HARBOR_DISPLAY_SIZE_TO_ACTIVATE_ADMIRAL = 5;


	/**
	 *  Trades a ship card for coins for the acting player.
	 *  Only works if the selected card is a ship card.
	 *  Adds the coin value of the card to the player and checks for traders of the same colour.
	 *  Also transfers a coin to the active player when the acting player is different to the active player.
	 *  Increases the actionCounter if traded successfully.
	 * @param selectedCard card that should be traded for coins
	 * @return {@code true} if the selected card could be traded successfully, {@code false} otherwise
	 */
	public boolean trade(Card selectedCard) {
		LogController logController = this.gameController.getLogController();
		logController.startLogging(); // Starts log recording
		if (!(selectedCard.getCardType().equals(CardType.SHIP))) {
			logController.cancelLogging(); // Cancels logging because this action was not executed successfully
			return false;
		}
		else {
			Game game = gameController.getGame();
			PlayerController playerController = gameController.getPlayerController();
			playerController.drawCoins(game.getActingPlayer().getTradersCount(selectedCard.getCardColour()));
			int coinValue = selectedCard.getCoinValue() * (-1);
			playerController.drawCoins(coinValue);
			if(!(game.getActingPlayer().equals(game.getActivePlayer()))) {
				playerController.transferCoinTo();
			}
			game.getDiscardPile().add(selectedCard);
			game.getHarborDisplay().remove(selectedCard);
			game.setActionPoints(game.getActionPoints() - 1);
			//actionCounter++;
			if(!(game.getActingPlayer().equals(game.getActivePlayer()))) {
				logController.log(Action.TRADE,selectedCard,game.getActivePlayer()); // Logs this executed action
			} else {
				logController.log(Action.TRADE,selectedCard,null); // Logs this executed action
			}
			return true;
		}
	}

	/**
	 * Hires a person card for the acting player.
	 * Only works if the selected card is a person card.
	 * Also fails if the acting player does not have enough coins.
	 * Calculates the card price by checking for mademoiselles and removes those coins from the acting player.
	 * Adds the bought card to the personal display of the acting player and transfers a coin to the active player if necessary.
	 * @param selectedCard card that should be bought for coins
	 * @return {@code true} if the selected card could be hired successfully, {@code false} otherwise
	 */
	public boolean hire(Card selectedCard) {
		LogController logController = this.gameController.getLogController();
		logController.startLogging(); // Starts log recording
		if (!(selectedCard.getCardType().equals(CardType.PERSON))) {
			logController.cancelLogging(); // Cancels logging because this action was not executed successfully
			return false;
		}
		Game game = gameController.getGame();
		int counterMademoiselle = game.getActingPlayer().getMademoisellesCount();
		int cardPrice = selectedCard.getCoinValue() - counterMademoiselle;
		if (!(game.getActivePlayer().equals(game.getActingPlayer()))) {
			if ((game.getActingPlayer().getCoinsCount() - cardPrice -1) < 0) {
				logController.cancelLogging(); // Cancels logging because this action was not executed successfully
				return false;
			}
			gameController.getPlayerController().discardCoins(cardPrice);
			gameController.getPlayerController().transferCoinTo();
		}
		else {
			if ((game.getActingPlayer().getCoinsCount() - cardPrice ) < 0) {
				logController.cancelLogging(); // Cancels logging because this action was not executed successfully
				return false;
			}
			gameController.getPlayerController().discardCoins(cardPrice);
		}
		//boolean faceUp = game.isFaceUp(selectedCard);
		game.getActingPlayer().addCard(selectedCard,true);
		game.getHarborDisplay().remove(selectedCard);
		game.setActionPoints(game.getActionPoints() - 1);
		//actionCounter++;
		if(!(game.getActingPlayer().equals(game.getActivePlayer()))) {
			logController.log(Action.HIRE,selectedCard,game.getActivePlayer()); // Logs this executed action
		} else {
			logController.log(Action.HIRE,selectedCard,null); // Logs this executed action
		}
		return true;
	}

	/**
	 *	Changes the acting player in the phase trade and hire.
	 * 	Checks if every player completed the phase trade and hire and then empties the harbor display and changes the active player.
	 * 	Sets the actionCounter and numberOfActions for every acting player and handles the effects of their cards.
	 * @return next acting {@code Player} object
	 */
	public Player nextActingPlayer() {
		Game game = gameController.getGame();
		Player nextActingPlayer = gameController.getPlayerController().nextActingPlayer();
		ArrayList<Card> harborDisplay = game.getHarborDisplay();
		if (nextActingPlayer.equals(game.getActivePlayer())) {
			for(int i = harborDisplay.size() - 1; i >= 0; i-- ) {
				Card card = harborDisplay.get(i);
				game.addToDiscardPile(card);
				harborDisplay.remove(i);
			}
			return gameController.getPlayerController().nextActivePlayer();
		}
		else {
			//actionCounter = 0;
			//numberOfActions = 1;
			game.setActionPoints(1 + game.getActingPlayer().getGovernorsCount());
			if(harborDisplay.size() >= HARBOR_DISPLAY_SIZE_TO_ACTIVATE_ADMIRAL) {
				gameController.getPlayerController().drawCoins(2 * (game.getActingPlayer().getAdmiralsCount()));
			} else if(harborDisplay.size() == 0) {
				gameController.getPlayerController().drawCoins(game.getActingPlayer().getJestersCount());
			}
			//numberOfActions = numberOfActions + game.getActingPlayer().getGovernorsCount();
		}

		return nextActingPlayer;
	}

	/**
	 * Starts the phase trade and hire for the active player. Sets the numberOfActions the player has according to the harbor display.
	 * Also handles the effects of card for the active player and sets the actionCounter to 0.
	 * If this phase should be skipped the active player and the other players get coins for their jesters
	 * @param skipPhase {@code true} if the phase trade and hire should be skipped and {@code false} if this phase should be played normally
	 */
	public void startPhase(boolean skipPhase) {
		LogController logController = this.gameController.getLogController();
		logController.startLogging(); // Starts log recording
		Game game = gameController.getGame();
		//actionCounter = 0;
		//numberOfActions = 0;
		if (!skipPhase) {
			ArrayList<Card> harborDisplay = game.getHarborDisplay();
			int shipColours = countShipColours();
			if (shipColours <= SHIP_COLOURS_THRESHOLD_FOR_ONE_ACTION) {
				//numberOfActions = 1;
				game.setActionPoints(1 + game.getActingPlayer().getGovernorsCount());
			}
			else if (shipColours == SHIP_COLOURS_THRESHOLD_FOR_TWO_ACTIONS) {
				//numberOfActions = 2;
				game.setActionPoints(2 + game.getActingPlayer().getGovernorsCount());
			}
			else if (shipColours == SHIP_COLOURS_THRESHOLD_FOR_THREE_ACTIONS) {
				//numberOfActions = 3;
				game.setActionPoints(3 + game.getActingPlayer().getGovernorsCount());
			}
			if (harborDisplay.size() >= HARBOR_DISPLAY_SIZE_TO_ACTIVATE_ADMIRAL) {
				gameController.getPlayerController().drawCoins(2*(game.getActingPlayer().getAdmiralsCount()));
			}
			//numberOfActions = numberOfActions + game.getActingPlayer().getGovernorsCount();
			game.setCurrentGamePhase(GamePhase.TRADE_AND_HIRE);
			logController.log(Action.START_TRADE_AND_HIRE_PHASE,null,null); // Logs this executed action
		}
		else {
			ArrayList<Card> currentHarborDisplay = this.gameController.getGame().getHarborDisplay();
			for(int i = 0; i < currentHarborDisplay.size(); i++) {
				this.gameController.getGame().getDiscardPile().add(currentHarborDisplay.remove(0));
			}
			gameController.getPlayerController().drawCoins(game.getActivePlayer().getJestersCount());
			Player actingPlayer = nextActingPlayer();
			while (!(game.getActivePlayer().equals(actingPlayer))) {
				gameController.getPlayerController().drawCoins(actingPlayer,actingPlayer.getJestersCount());
				actingPlayer = nextActingPlayer();
			}
			game.setCurrentGamePhase(GamePhase.DISCOVER);
			logController.log(Action.START_DISCOVER_PHASE,null,null); // Logs this executed action
		}
	}

	/**
	 * Counts the different ship colours in the harbor display.
	 * Is used to determine the numberOfActions the active player has.
	 * @return value of different coloured ships in the harbor display
	 */
	public int countShipColours() {
		Game game = gameController.getGame();
		ArrayList<Card> harborDisplay = game.getHarborDisplay();
		int[] colours = {0,0,0,0,0};
		for (Card card : harborDisplay) {
			if (card.getCardColour().equals(CardColour.YELLOW) && card.getCardType().equals(CardType.SHIP)) {
				colours[0]++;
			} else if (card.getCardColour().equals(CardColour.BLUE) && card.getCardType().equals(CardType.SHIP)) {
				colours[1]++;
			} else if (card.getCardColour().equals(CardColour.GREEN) && card.getCardType().equals(CardType.SHIP)) {
				colours[2]++;
			} else if (card.getCardColour().equals(CardColour.RED) && card.getCardType().equals(CardType.SHIP)) {
				colours[3]++;
			} else if (card.getCardColour().equals(CardColour.BLACK) && card.getCardType().equals(CardType.SHIP)) {
				colours[4]++;
			}
		}
		int shipColours = 0;
		for (int i = 0; i < colours.length; i++) {
			if (colours[i] > 0) {
				shipColours++;
			}
		}
		return shipColours;
	}

	/**
	 * Constructor to create the TradeAndHireController, sets the gameController
	 * @param gameController main controller of the game that knows the data structure and the other controllers
	 */
	public TradeAndHireController(GameController gameController){
		this.gameController = gameController;
	}
}
