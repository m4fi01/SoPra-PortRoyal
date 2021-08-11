package controller;

import model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Represents the phase discover in the game port royal
 * @author Phuong Huong Nguyen
 */
public class DiscoverController {

	/**
	 *	gameController that knows the data structure and the other controllers
	 */
	private GameController gameController;

	public static final int TAX_ALLOWANCE = 12;

	PileController pileController ;

	/**
	 * Constructor that sets the game controller
	 * @param gameController main controller of the game that knows the data structure and the other controllers
	 */
	public DiscoverController(GameController gameController){
		this.gameController=gameController;
		this.pileController= gameController.getPileController();
	}

	/**
	 * Checks, whether last drawn ship card can be repeled or not.
	 * When the card can be repeled, then it is removed from the harbor display and added to the discard pile
	 * @return {@code true} if the repelsprocess is successful, otherwise {@code false} is returned
	 */
	public boolean repelShip(){
		LogController logController = this.gameController.getLogController();
		logController.startLogging(); // Starts log recording
		Game game = gameController.getGame();
		Player activePlayer = game.getActivePlayer();
		Card lastCardDrawn = game.getLastCardDrawn();      // get the card, which has just drawn by the active player
		int  ownedSwords = activePlayer.getSwordsCount();   // get the sword number of the active player
		int lastCardSwords = lastCardDrawn.getSwords();    // get the sword number of the last drawn card
		if(lastCardSwords > ownedSwords){
			logController.cancelLogging(); // Cancels logging because this action was not executed successfully
			return false;
		}else{
			game.getDiscardPile().add(lastCardDrawn);   // add the last drawn card to the ArrayList of discardPile
			game.getHarborDisplay().remove(lastCardDrawn);  // remove the last drawn card to the ArrayList of harborDisplay
			logController.log(Action.REPEL_SHIP,null,null); // Logs this executed action
			return true;
		}
	}

	/**
	 * The drawCard() method is used to draw a card from the draw pile and put it on the harbor display
	 * Also handles the effects of the tax increase cards and puts expedition cards to the expedition display.
	 */
	public void drawCard() {
		LogController logController = this.gameController.getLogController();
		logController.startLogging(); // Starts log recording
		Game game = gameController.getGame();
		DrawPile drawPile = game.getDrawPile();
		drawPile = createNewDrawPile(drawPile); // create a new drawPile, if the current drawPile is empty
		game.setDrawPile(drawPile);   // create a new drawPile, if the current drawPile is empty
		Card drawCard = drawPile.pop();  // draw the card on the top of drawPile and remove it from the current drawPile
		game.setFaceUp(drawCard,true);
		if(drawCard.getCardType().equals(CardType.EXPEDITION)){
			game.getExpeditionDisplay().add(drawCard);   // add the drawCard to the list of expedition cards
		}
		else if(drawCard.getCardType().equals(CardType.TAX_INCREASE)){   // When the draw card is a TAX_INCREASE card
			ArrayList<Player> playerList = game.getPlayersList();   // This arrayList includes all the players of this game
			ArrayList<Player> maxSwords = maxSwordsPlayers(playerList) ; // this maxSwords contains of all players who have the most sword
			ArrayList<Player> minVictoryPoints = minVictoryPlayers(playerList);  // this minVictoryPoints includes all players who have the least victory points
			for(int i =0; i< playerList.size(); i++){
				Player currentPlayer = playerList.get(i);
				int totalCoins =  currentPlayer.getCoinsCount();
				if( totalCoins >= TAX_ALLOWANCE){
					gameController.getPlayerController().discardCoins(currentPlayer, totalCoins /2); // delete coins from the coinDisplay and add the removed card onto the discardPile
				}
			}
			ArrayList<Player> coinRecipients = null;
			if(drawCard.getCardName().equals(CardName.CHARITY)){ // plus 1 coin for the players who have the least victory points, when the name card is CHARITY
				coinRecipients = minVictoryPoints;
			}
			else if(drawCard.getCardName().equals(CardName.PROTECTION_MONEY)){   // plus 1 coin for the players who have the most number of swords, when the name card is PROTECTION_MONEY
				coinRecipients = maxSwords;
			}
			int actingPlayerIndex = playerList.indexOf(game.getActingPlayer());
			for(int i = actingPlayerIndex; i < playerList.size(); i++) {
				Player player = playerList.get(i);
				if(coinRecipients.contains(player)) {
					game.setDrawPile(createNewDrawPile(game.getDrawPile()));
					Card coin = game.getDrawPile().pop();
					game.setFaceUp(coin,false);
					player.addCard(coin,false);
				}
			}
			for(int i = 0; i < actingPlayerIndex; i++) {
				Player player = playerList.get(i);
				if(coinRecipients.contains(player)) {
					game.setDrawPile(createNewDrawPile(game.getDrawPile()));
					Card coin = game.getDrawPile().pop();
					game.setFaceUp(coin,false);
					player.addCard(coin,false);
				}
			}
			game.getDiscardPile().add(drawCard); // add the drawCard onto the discardPile
		}
		else{       // falls the type of drawn card is PERSON or SHIP
			game.getHarborDisplay().add(drawCard);  // add the drawCard onto the harborDisplay
		}
		game.setLastCardDrawn(drawCard);
		game.setDrawPile(createNewDrawPile(drawPile));   // create a new drawPile, if the current drawPile is empty
		logController.log(Action.DRAW_CARD,drawCard,null); // Logs this executed action
	}

	/**
	 * Returns a list of players with max swords points from the player list in parameter
	 * @param players the player list
	 * @return a list of players with max swords points
	 */
	private ArrayList<Player> maxSwordsPlayers(ArrayList<Player> players){
		ArrayList<Player> maxSwords = new ArrayList<Player>(players);
		Collections.sort(maxSwords, new Comparator<Player>() {
			@Override
			public int compare(Player player1, Player player2) { return -(player1.getSwordsCount()-player2.getSwordsCount()); }});
        maxSwords.removeIf(player -> { return player.getSwordsCount()!=maxSwords.get(0).getSwordsCount(); });
        return maxSwords;
	}

	/**
	 * Returns a list of players with min victory points from the player list in parameter
	 * @param players the player list
	 * @return a list of players with min victory points
	 */
	private ArrayList<Player> minVictoryPlayers(ArrayList<Player> players){
		ArrayList<Player> minVictory = new ArrayList<Player>(players);
		Collections.sort(minVictory, new Comparator<Player>() {
			@Override
			public int compare(Player player1, Player player2) { return player1.getVictoryPointsCount()-player2.getVictoryPointsCount(); }});
		minVictory.removeIf(player -> { return player.getVictoryPointsCount()!=minVictory.get(0).getVictoryPointsCount(); });
		return minVictory;
	}


	/**
	 * the startExpedition(...) method is used to implement a fulfilling of  an expedition card on the expedition display
	 * @param expedition is an expedition card which is on the expedition display
	 * @param selectedCards are cards which the active player choosed to fulfill the expedition card
	 * @return 0 when the requirements of the expedition card is fulfilled,1 if player used too few cards to fullfill, 2 if player used too many cards
	 */
	public int startExpedition(Card expedition, ArrayList<Card> selectedCards) {
		LogController logController = this.gameController.getLogController();
		logController.startLogging(); // Starts log recording
		Game game = gameController.getGame();
		Player activePlayer = game.getActivePlayer();
		int selectedJokers = 0;
		int selectedAnchors = 0;
		int selectedHouses = 0;
		int selectedCrosses = 0;
		// calculate the quantity of anchors, houses, crosses and jockers, which selectedCards own
		for(Card c : selectedCards){
			if(c.getCardName().equals(CardName.JACK_OF_ALL_TRADES)){
				selectedJokers++;
			}else{
				selectedAnchors += c.getAnchors();
				selectedHouses += c.getHouses();
				selectedCrosses += c.getCrosses();
			}
		}
		// calculate the different number of anchors, houses, crosses between Card expedition and selectedCards
		int remainingAnchors = expedition.getAnchors() - selectedAnchors;
		int remainingHouses = expedition.getHouses() - selectedHouses;
		int remainingCrosses = expedition.getCrosses() - selectedCrosses;
		int calculate = 0;
		if(remainingAnchors < 0 || remainingHouses < 0 || remainingCrosses < 0){
			logController.cancelLogging(); // Cancels logging because this action was not executed successfully
			return 2;
		}
		if(remainingAnchors > 0){
            calculate = calculateJokers(remainingAnchors,selectedJokers);
            if(calculate == -1){
				logController.cancelLogging(); // Cancels logging because this action was not executed successfully
            	return 1;
            }
            selectedJokers = calculate;
		}
		if(remainingHouses > 0){
			calculate = calculateJokers(remainingHouses,selectedJokers);
			if(calculate == -1){
				logController.cancelLogging(); // Cancels logging because this action was not executed successfully
				return 1;
			}
			selectedJokers = calculate;
		}
		if(remainingCrosses > 0){
			calculate = calculateJokers(remainingCrosses,selectedJokers);
			if(calculate == -1){
				logController.cancelLogging(); // Cancels logging because this action was not executed successfully
				return 1;
			}
			selectedJokers = calculate;
		}
		if(selectedJokers>0){
			logController.cancelLogging(); // Cancels logging because this action was not executed successfully
			return 2;
		}
		else{
			game.getExpeditionDisplay().remove(expedition);
			activePlayer.addCard(expedition,true);
			for(Card c : selectedCards){     // removes the given card from the acting players personalDisplay and adds it onto the discardPile
				pileController.discard(c);
			}
			game.setDrawPile(createNewDrawPile(game.getDrawPile()));  //  if the draw Pile is empty, then a new draw pile will be recreated from the discard Pile
			gameController.getPlayerController().drawCoins(expedition.getCoinValue());
			logController.log(Action.START_EXPEDITION,expedition,null); // Logs this executed action
			return 0;
		}
	}

	/**
	 * To calculate the number of jokers are left after use for an attribute
	 * @param attribute the attribute (Cross, House, Anchor)
	 * @param selectedJokers number of jokers left
	 * @return -1 if number of jokers is not enough to bring attribute to 0 or number of jokers left
	 */
	private int calculateJokers(int attribute, int selectedJokers){
		while(attribute!=0 && selectedJokers!=0){
			attribute=attribute-1;
			selectedJokers=selectedJokers-1;
		}
		if(attribute>0){return -1;}
		return selectedJokers;
	}

	/**
	 * This method is used to create a new drawPile, when the current drawPile is empty
	 * @param drawPile new {@code DrawPile} object
	 */
	private DrawPile createNewDrawPile(DrawPile drawPile){
		Game game = gameController.getGame();
		if(drawPile.getPile().isEmpty()){
			DrawPile newpile=pileController.reorganisePile(game.getDiscardPile());
			game.setDiscardPile(new ArrayList<Card>());
			return newpile;
		}
		return drawPile;
	}
}
