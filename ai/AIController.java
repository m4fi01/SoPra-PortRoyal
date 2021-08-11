package ai;

import controller.*;
import model.*;
import view.GUIController;
import view.MainGameViewController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Controls the AI
 */
public class AIController {
	private final GameController gameController;
	private Game game;
	private ArrayList<Card> harborDisplay;
	private ArrayList<Card> expeditionDisplay;
	private Player activePlayer;
	private DiscoverController discoverController;
	private TradeAndHireController tradeAndHireController;
	private MainGameViewController mainGameViewController;
	private int movesToMake = 1;
	private final ArrayList<Card> personsOfInterest = new ArrayList<>();
	private ArrayList<Card> victoryPointsOrder = new ArrayList<>();

	/**
	 * Is used to generate a tip for the player
	 * @return the String representation of the move the ai would make
	 */
	public String generateTip() {
		update();
		GamePhase gamePhase = game.getCurrentGamePhase();
		String msg = "Phase beenden";
		Player aiCopy = game.getActingPlayer();
		switch (gamePhase){
			case DISCOVER:
				int size = harborDisplay.size();
				if(size == 0){
					msg = "Karte ziehen";
					break;
				}
				Card card = harborDisplay.get(size - 1);
				switch(card.getCardType()){//switch last cardType that got into harborDisplay
					case SHIP:
						if (canRepelShip(aiCopy, card)) {
							if(gameController.checkShipColours()) {
								msg = "Schiff abwehren und neue Karte ziehen";
							}
							else {
								if (tradeAndHireController.countShipColours() >= 4){
									msg = "Nicht weiter ziehen";
								}
								else{
									msg = "Schiff abwehren und weiter ziehen";
								}
							}
						}
						else {
							if(gameController.checkShipColours()) {
								msg = "Zug beenden";
							}
							else{
								if (personsOfInterest.size() > 0){
									if(movesToMake == 1){
										if (canBuyDesiredPerson(aiCopy)){
											msg = "Nicht weiter ziehen";
										}
										else{
											discoverController.drawCard();
											msg = "Nicht weiter ziehen";
										}
									}
									else{
										if (canBuyDesiredPerson(aiCopy)) {
											msg = "Nicht weiter ziehen";
										}
										else{
											if (canBuyDesiredPersonByTakingShips(aiCopy, movesToMake)){
												msg = "Nicht weiter ziehen";
											}
											else{
												msg = "Karte ziehen";
											}
										}
									}
								}
								else{
									if(harborDisplay.size() < 5 && aiCopy.getAdmiralsCount() > 0){
										msg = "Karte ziehen";
									}
									else if (needsCoinsHard(aiCopy) && card.getCoinValue() < -2){//-2 because we want ships to only be taken into consideration if it trades for 3+ coins
										if (movesToMake > 1) {
											msg = "Karte ziehen";
										}
										else{
											msg = "Nicht weiter ziehen";
										}
									}
									else if(!needsCoinsHard(aiCopy)){
										msg = "Karte ziehen";
									}
									else{
										msg = "Karte ziehen";
									}
								}
							}
						}
					case PERSON:
						if (wantsPersonHard(aiCopy, card)){
							if (movesToMake == 1){
								if(canBuyPerson(aiCopy, card)){
									msg = "Nicht weiter ziehen";
								}
								else{
									msg = "Karte ziehen";
								}
							}
							else{
								if(canBuyPerson(aiCopy, card)){
									msg = "Nicht weiter ziehen";
								}
								else{
									if (canBuyDesiredPersonByTakingShips(aiCopy, movesToMake)){
										msg = "Nicht weiter ziehen";
									}
									else{
										msg = "Karte ziehen";
									}
								}
							}
						}
						else{
							msg = "Karte ziehen";
						}
					default:
						break;
				}
				break;
			case TRADE_AND_HIRE:
				int movesLeft = game.getActionPoints();
				boolean isActive = aiCopy.equals(activePlayer);
				for(Card hCard : harborDisplay){
					wantsPersonHard(aiCopy, hCard);
				}
				if(movesLeft == 1){
					if(personsOfInterest.size() > 0){
						if(canBuyDesiredPerson(aiCopy)){
							ArrayList<Card> affordableDesiredPersons = getAffordableDesiredPersons(aiCopy);
							ArrayList<Card> affordableAdmirals = (ArrayList<Card>) affordableDesiredPersons.stream().filter(person -> person.getCardName().equals(CardName.ADMIRAL)).collect(Collectors.toList());
							affordableAdmirals.sort(Comparator.comparing(Card::getCoinValue));
							if(affordableAdmirals.size() > 0){
								Card toBuy = affordableAdmirals.get(affordableAdmirals.size()-1);
								msg = "Nimm " + toBuy.getCardName().toString() + " mit " + toBuy.getCoinValue() + " Münzen";
							}
							else{
								ArrayList<Card> affordableSwordsmen = (ArrayList<Card>) affordableDesiredPersons.stream().filter(person -> (person.getCardName().equals(CardName.SAILOR) || person.getCardName().equals(CardName.PIRATE))).collect(Collectors.toList());
								affordableSwordsmen.sort(Comparator.comparing(Card::getCoinValue));
								ArrayList<Card> affordablePirates = (ArrayList<Card>) affordableDesiredPersons.stream().filter(person -> person.getCardName().equals(CardName.PIRATE)).collect(Collectors.toList());
								affordablePirates.sort(Comparator.comparing(Card::getCoinValue));
								if(aiCopy.getSwordsCount() == 0){
									if(!affordablePirates.isEmpty()){
										Card toBuy = affordablePirates.get(affordablePirates.size()-1);
										msg = "Nimm " + toBuy.getCardName().toString() + " mit " + toBuy.getCoinValue() + " Münzen";
									}
									else if(!affordableSwordsmen.isEmpty()){
										Card toBuy = affordableSwordsmen.get(affordableSwordsmen.size()-1);
										msg = "Nimm " + toBuy.getCardName().toString() + " mit " + toBuy.getCoinValue() + " Münzen";
									}
								}
								else{
									if(!affordableSwordsmen.isEmpty()){
										Card toBuy = affordableSwordsmen.get(affordableSwordsmen.size()-1);
										msg = "Nimm " + toBuy.getCardName().toString() + " mit " + toBuy.getCoinValue() + " Münzen";
									}
								}
							}
						}
						else{
							ArrayList<Card> ships = getMostCoinsShips(aiCopy);
							Card mostCoinsShip = null;
							if(ships.size() > 0){
								mostCoinsShip = ships.get(ships.size() - 1);
							}
							if((mostCoinsShip != null) && mostCoinsShip.getCoinValue() - aiCopy.getTradersCount(mostCoinsShip.getCardColour()) < -2){
								msg = "Nimm " + mostCoinsShip.getCardName().toString() + " mit " + mostCoinsShip.getCoinValue() + " Münzen";
							}
							else{
								orderByVictoryPoints(aiCopy);
								int sizeQ = victoryPointsOrder.size();
								for(int i = sizeQ - 1; i >= 0; i--){
									if(canBuyPerson(aiCopy, victoryPointsOrder.get(i))) {
										Card toBuy = victoryPointsOrder.get(i);
										msg = "Nimm " + toBuy.getCardName().toString() + " mit " + toBuy.getCoinValue() + " Münzen";
										break;
									}
								}
							}
						}
					}
					else{
						ArrayList<Card> ships = getMostCoinsShips(aiCopy);
						Card mostCoinsShip = null;
						if(ships.size() > 0){
							mostCoinsShip = ships.get(ships.size() - 1);
						}
						if((mostCoinsShip != null) && mostCoinsShip.getCoinValue() - aiCopy.getTradersCount(mostCoinsShip.getCardColour()) < -2){
							msg = "Nimm " + mostCoinsShip.getCardName().toString() + " mit " + mostCoinsShip.getCoinValue() + " Münzen";
						}
						else{
							orderByVictoryPoints(aiCopy);
							int sizeQ = victoryPointsOrder.size();
							for(int i = sizeQ - 1; i >= 0; i--){
								if(canBuyPerson(aiCopy, victoryPointsOrder.get(i))) {
									Card toBuy = victoryPointsOrder.get(i);
									msg = "Nimm " + toBuy.getCardName().toString() + " mit " + toBuy.getCoinValue() + " Münzen";
									break;
								}
							}
						}
					}
				}
				else{//case more than 1 move
					if(personsOfInterest.size() > 0) {
						if (canBuyDesiredPerson(aiCopy)) {
							ArrayList<Card> affordableDesiredPersons = getAffordableDesiredPersons(aiCopy);
							ArrayList<Card> affordableAdmirals = (ArrayList<Card>) affordableDesiredPersons.stream().filter(person -> person.getCardName().equals(CardName.ADMIRAL)).collect(Collectors.toList());
							affordableAdmirals.sort(Comparator.comparing(Card::getCoinValue));
							if(affordableAdmirals.size() > 0){
								Card toBuy = affordableAdmirals.get(affordableAdmirals.size()-1);
								msg = "Nimm " + toBuy.getCardName().toString() + " mit " + toBuy.getCoinValue() + " Münzen";
							}
							else{
								ArrayList<Card> affordableSwordsmen = (ArrayList<Card>) affordableDesiredPersons.stream().filter(person -> (person.getCardName().equals(CardName.SAILOR) || person.getCardName().equals(CardName.PIRATE))).collect(Collectors.toList());
								affordableSwordsmen.sort(Comparator.comparing(Card::getCoinValue));
								ArrayList<Card> affordablePirates = (ArrayList<Card>) affordableDesiredPersons.stream().filter(person -> person.getCardName().equals(CardName.PIRATE)).collect(Collectors.toList());
								affordablePirates.sort(Comparator.comparing(Card::getCoinValue));
								if(aiCopy.getSwordsCount() == 0){
									if(!affordablePirates.isEmpty()){
										Card toBuy = affordablePirates.get(affordablePirates.size()-1);
										msg = "Nimm " + toBuy.getCardName().toString() + " mit " + toBuy.getCoinValue() + " Münzen";
									}
									else if(!affordableSwordsmen.isEmpty()){
										Card toBuy = affordableSwordsmen.get(affordableSwordsmen.size()-1);
										msg = "Nimm " + toBuy.getCardName().toString() + " mit " + toBuy.getCoinValue() + " Münzen";
									}
								}
								else{
									if(!affordableSwordsmen.isEmpty()){
										Card toBuy = affordableSwordsmen.get(affordableSwordsmen.size()-1);
										msg = "Nimm " + toBuy.getCardName().toString() + " mit " + toBuy.getCoinValue() + " Münzen";
									}
								}
							}
						}
						else{
							if(canBuyDesiredPersonByTakingShips(aiCopy, movesLeft)){
								ArrayList<Card> ships = getMostCoinsShips(aiCopy);
								if(!ships.isEmpty()){
									Card toBuy = ships.get(ships.size()-1);
									msg = "Nimm " + toBuy.getCardName().toString() + " mit " + -(toBuy.getCoinValue()) + " Münzen";
								}
							}
							else{
								ArrayList<Card> mostCoinsShips = getMostCoinsShips(aiCopy);
								if(mostCoinsShips.size() < 1){
									orderByVictoryPoints(aiCopy);
									int sizeQ = victoryPointsOrder.size();
									for(int i = sizeQ - 1; i >= 0; i--){
										if(canBuyPerson(aiCopy, victoryPointsOrder.get(i))) {
											Card toBuy = victoryPointsOrder.get(i);
											msg = "Nimm " + toBuy.getCardName().toString() + " mit " + toBuy.getCoinValue() + " Münzen";
											break;
										}
									}
								}
								else{
									if(isActive){
										Card toBuy = mostCoinsShips.get(mostCoinsShips.size()-1);
										msg = "Nimm " + toBuy.getCardName().toString() + " mit " + toBuy.getCoinValue() + " Münzen";
									}
									else{
										Card ship = mostCoinsShips.get(mostCoinsShips.size() - 1);
										if((aiCopy.getTradersCount(ship.getCardColour()) - ship.getCoinValue()) < -2){
											msg = "Nimm " + ship.getCardName().toString() + " mit " + ship.getCoinValue() + " Münzen";
										}
									}
								}
							}
						}
					}
					else{
						if(needsCoinsHard(aiCopy)){
							ArrayList<Card> mostCoinsShips = getMostCoinsShips(aiCopy);
							if(mostCoinsShips.size() < 1){
								orderByVictoryPoints(aiCopy);
								int sizeQ = victoryPointsOrder.size();
								for(int i = sizeQ - 1; i >= 0; i--){
									if(canBuyPerson(aiCopy, victoryPointsOrder.get(i))) {
										Card toBuy = victoryPointsOrder.get(i);
										msg = "Nimm " + toBuy.getCardName().toString() + " mit " + toBuy.getCoinValue() + " Münzen";
										break;
									}
								}
							}
							else{
								if(isActive){
									Card toBuy = mostCoinsShips.get(mostCoinsShips.size()-1);
									msg = "Nimm " + toBuy.getCardName().toString() + " mit " + toBuy.getCoinValue() + " Münzen";
								}
								else{
									Card ship = mostCoinsShips.get(mostCoinsShips.size() - 1);
									if((aiCopy.getTradersCount(ship.getCardColour()) - ship.getCoinValue()) < -2){
										msg = "Nimm " + ship.getCardName().toString() + " mit " + ship.getCoinValue() + " Münzen";
									}
								}
							}
						}
						else{
							orderByVictoryPoints(aiCopy);
							int sizeQ = victoryPointsOrder.size();
							for(int i = sizeQ - 1; i >= 0; i--){
								if(canBuyPerson(aiCopy, victoryPointsOrder.get(i))) {
									Card toBuy = victoryPointsOrder.get(i);
									msg = "Nimm " + toBuy.getCardName().toString() + " mit " + toBuy.getCoinValue() + " Münzen";
									break;
								}
							}
						}
					}
				}
				break;
		}
		return msg;
	}

	private void update(){
		this.discoverController = gameController.getDiscoverController();
		this.tradeAndHireController = gameController.getTradeAndHireController();
		this.game = gameController.getGame();
		this.harborDisplay = game.getHarborDisplay();
		this.expeditionDisplay = game.getExpeditionDisplay();
		this.activePlayer = game.getActivePlayer();
		this.movesToMake = activePlayer.getGovernorsCount() + (tradeAndHireController.countShipColours() >= 5? 3 : (tradeAndHireController.countShipColours() < 4? 1 : 2));
		mainGameViewController.update();
	}

	/**
	 * decides on a move based on the given game state
	 * executes the decided on move
	 * @param aiPlayer the current aiPlayer
	 */
	public void makeMove(AIPlayer aiPlayer){
		switch (aiPlayer.getLevel()){
			case MEDIUM:
				makeMediumMove(aiPlayer);
				break;
			case HARD:
				makeHardMove(aiPlayer);
				break;
			default:
				makeEasyMove(aiPlayer);
		}
		personsOfInterest.clear();
	}

	private void makeEasyMove(AIPlayer aiPlayer){
		update();
		if(activePlayer.equals(aiPlayer)) {
			int randomDrawNumber = (new Random()).nextInt(10) + 1;
			for (int i = 0; i < randomDrawNumber; i++) {
				discoverController.drawCard();
				update();
				if (gameController.checkShipColours()) break;
			}
			tradeAndHireController.startPhase(gameController.checkShipColours());
		}
		chooseRandomCardAndBuyIt(aiPlayer);
		update();
		mainGameViewController.endPhaseClicked();
	}

	private void chooseRandomCardAndBuyIt(AIPlayer aiPlayer) {
		update();
		int size = harborDisplay.size();
		if (size > 0) {
			int chosenIndex = (new Random()).nextInt(size);
			Card chosen = harborDisplay.get(chosenIndex);
			if (chosen.getCardType().equals(CardType.SHIP)) {
				tradeAndHireController.trade(chosen);
			} else {
				if (canBuyPerson(aiPlayer, chosen)) {
					tradeAndHireController.hire(chosen);
				}
			}
		}
	}

	private void makeMediumMove(AIPlayer aiPlayer){
		update();
		makeHardMove(aiPlayer);
	}

	private void makeHardMove(AIPlayer aiPlayer){//makes move for hardest ai difficulty based on our experience with 2 player games
		update();
		waitForSimulationSpeed(aiPlayer);
		personsOfInterest.clear();
		victoryPointsOrder.clear();
		update();
		if(activePlayer.equals(aiPlayer)){  // case active player
			//initial loop
			while(harborDisplay.size() < 1){//draw cards until one is in harborDisplay
				discoverController.drawCard();
				update();
				waitForSimulationSpeed(aiPlayer);
			}
			//draw loop
			boolean drawAnother;
			do {
				fulfillExpeditionsHard(aiPlayer);
				update();
				//decide whether to draw further or to repel a ship
				drawAnother = decideOnDrawnCardHard(aiPlayer);
				update();
				waitForSimulationSpeed(aiPlayer);
			}
			while(drawAnother);


			boolean skip = gameController.checkShipColours();
			tradeAndHireController.startPhase(skip);
			//trade and hire loop
			update();
			if(skip)return;
			int actionCount = game.getActionPoints();
			for(int i = 0; i < actionCount; i++){
				tradeOrHireHard(aiPlayer);
				update();
				waitForSimulationSpeed(aiPlayer);
				fulfillExpeditionsHard(aiPlayer);
				update();
			}
		}
		else{  // case acting player
			//trade and hire loop
			int actionCount = game.getActionPoints();
			for(int i = 0; i < actionCount; i++){
				tradeOrHireHard(aiPlayer);
				update();
				waitForSimulationSpeed(aiPlayer);
			}
		}
		mainGameViewController.endPhaseClicked();
	}

	//TODO visual flow
	private void waitForSimulationSpeed(AIPlayer aiPlayer){
		int simulationSpeed = aiPlayer.getSimulationSpeed();
		try {
			Thread.sleep(simulationSpeed);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}


	/**
	 *
	 * @param aiPlayer the AIPlayer whose move it is
	 * @return true if another card should be drawn afterwards, false if ai wants to stop
	 */
	private boolean decideOnDrawnCardHard(AIPlayer aiPlayer){
		int size = harborDisplay.size();
		Card card = harborDisplay.get(size - 1);
		switch(card.getCardType()){//switch last cardType that got into harborDisplay
			case SHIP:
				if (canRepelShip(aiPlayer, card)) {
					if(gameController.checkShipColours()) {
						discoverController.repelShip();
						discoverController.drawCard();
						return true;
					}
					else {
						if (tradeAndHireController.countShipColours() >= 4){
							return false;
						}
						else{
							discoverController.repelShip();
							discoverController.drawCard();
							return true;
						}
					}
				}
				else {
					if(gameController.checkShipColours()) {
						return false;
					}
					else{
						if (personsOfInterest.size() > 0){
							if(movesToMake == 1){
								if (canBuyDesiredPerson(aiPlayer)){
									return false;
								}
								else{
									discoverController.drawCard();
									return true;
								}
							}
							else{
								if (canBuyDesiredPerson(aiPlayer)) {
									return false;
								}
								else{
									if (canBuyDesiredPersonByTakingShips(aiPlayer, movesToMake)){
										return false;
									}
									else{
										discoverController.drawCard();
										return true;
									}
								}
							}
						}
						else{
							if(harborDisplay.size() < 5 && aiPlayer.getAdmiralsCount() > 0){
								discoverController.drawCard();
								return true;
							}
							else if (needsCoinsHard(aiPlayer) && card.getCoinValue() < -2){//-2 because we want ships to only be taken into consideration if it trades for 3+ coins
								if (movesToMake > 1) {
									discoverController.drawCard();
									return true;
								}
								else{
									return false;
								}
							}
							else if(!needsCoinsHard(aiPlayer)){
								discoverController.drawCard();
								return true;
							}
							else{
								discoverController.drawCard();
								return true;
							}
						}
					}
				}
			case PERSON:
				if (wantsPersonHard(aiPlayer, card)){
					if (movesToMake == 1){
						if(canBuyPerson(aiPlayer, card)){
							return false;
						}
						else{
							discoverController.drawCard();
							return true;
						}
					}
					else{
						if(canBuyPerson(aiPlayer, card)){
							return false;
						}
						else{
							if (canBuyDesiredPersonByTakingShips(aiPlayer, movesToMake)){
								return false;
							}
							else{
								discoverController.drawCard();
								return true;
							}
						}
					}
				}
				else{
					discoverController.drawCard();
					return true;
				}
			default:
				break;
		}
		return true;//to be removed
	}

	private boolean wantsPersonHard(Player aiPlayer, Card person){
		CardName cardName = person.getCardName();
		if(cardName.equals(CardName.ADMIRAL)){
			personsOfInterest.add(person);
			return true;
		}
		else if(cardName.equals(CardName.PIRATE) || cardName.equals(CardName.SAILOR)){
			if(aiPlayer.getSwordsCount() < 2){
				personsOfInterest.add(person);
				return true;
			}
		}
		return false;
	}

	private boolean canBuyPerson(Player aiPlayer, Card card){
		boolean isActive = aiPlayer.equals(activePlayer);
		int coins = aiPlayer.getCoinsCount();
		if(!isActive) coins--;
		return coins >= (card.getCoinValue() - aiPlayer.getMademoisellesCount());
	}

	private boolean canBuyDesiredPerson(Player aiPlayer){
		boolean isActive = aiPlayer.equals(activePlayer);
		int coins = aiPlayer.getCoinsCount();
		if(!isActive) coins--;
		for(Card person : personsOfInterest){
			if(person.getCoinValue() <= coins) return true;
		}
		return false;
	}

	private boolean canRepelShip(Player aiPlayer, Card ship){
		return aiPlayer.getSwordsCount() >= ship.getSwords();
	}

	private boolean canBuyDesiredPersonByTakingShips(Player aiPlayer, int movesLeftToMake){
		boolean isActive = aiPlayer.equals(activePlayer);
		int maxMovesToGetCoins = movesLeftToMake - 1;
		int availableCoins = 0;
		int[] shipsWithCorrespondingCoins = new int[4];
		for(Card card : harborDisplay){
			int coinValue = card.getCoinValue();
			switch(coinValue){
				case -1:
					shipsWithCorrespondingCoins[0]++;
					break;
				case -2:
					shipsWithCorrespondingCoins[1]++;
					break;
				case -3:
					shipsWithCorrespondingCoins[2]++;
					break;
				case -4:
					shipsWithCorrespondingCoins[3]++;
					break;
			}
		}
		//do some maths
		for(int i = 3; i > -1; i--){
			int shipsWithThatAmount = shipsWithCorrespondingCoins[i];
			int minOfMaxMovesToGetCoinsAndShipsWithThatAmount = Math.min(maxMovesToGetCoins, shipsWithThatAmount);
			availableCoins += (i + 1) * minOfMaxMovesToGetCoinsAndShipsWithThatAmount;
			maxMovesToGetCoins -= minOfMaxMovesToGetCoinsAndShipsWithThatAmount;
			if(maxMovesToGetCoins <= 0){
				break;
			}
		}
		if(!isActive) availableCoins--;
		for(Card person : personsOfInterest){
			if(person.getCoinValue() <= availableCoins) return true;
		}
		return false;
	}

	private boolean needsCoinsHard(Player aiPlayer){
		return aiPlayer.getCoinsCount() < 7;//7
	}

	/*
	* buy swords *in the beginning* while ai still has low sword amount (buy pirates if enough coins) (try get 2 early)
	* try not to have 12 ore more coins (buy victory points if necessary)
	* (take coins if no person of interest in harbor)
	* buy admiral as fast as possible (there are 6 admirals) (over swords)
	* (buy governor after admiral (there are 4 governors) (over swords) (1 max.))
	* victory points
	* 		trader irrelevant
	* 		buy resource people if ai can fulfill expedition with that card and win that way
	* 		buy mademoiselle if no admiral available
	* 		jesters are irrelevant
	*
	* get coins based on how many actions ai has and take enough coins to afford desired card
	* take ship with highest coins (including traders) (except ai is acting player -> only buy ships with 3+ coins)
	* */
	private void tradeOrHireHard(AIPlayer aiPlayer){
		int movesLeft = game.getActionPoints();
		boolean isActive = aiPlayer.equals(activePlayer);
		if(!isActive){
			for(Card card : harborDisplay){
				wantsPersonHard(aiPlayer, card);
			}
		}
		if(movesLeft == 1){
			if(personsOfInterest.size() > 0){
				if(canBuyDesiredPerson(aiPlayer)){
					takeCardsWhenCanBuyDesiredPersonHard(aiPlayer);
				}
				else{
					takeCardsWhenNoRealIntentHard(aiPlayer);
				}
			}
			else{
				takeCardsWhenNoRealIntentHard(aiPlayer);
			}
		}
		else{//case more than 1 move
			if(personsOfInterest.size() > 0) {
				if (canBuyDesiredPerson(aiPlayer)) {
					takeCardsWhenCanBuyDesiredPersonHard(aiPlayer);
				}
				else{
					if(canBuyDesiredPersonByTakingShips(aiPlayer, movesLeft)){
						ArrayList<Card> ships = getMostCoinsShips(aiPlayer);
						if(!ships.isEmpty()){
							tradeAndHireController.trade(ships.get(ships.size()-1));
						}
					}
					else{
						decideOnHarborDisplayWhenCantGetPersonOfInterest(aiPlayer, isActive);
					}
				}
			}
			else{
				if(needsCoinsHard(aiPlayer)){
					decideOnHarborDisplayWhenCantGetPersonOfInterest(aiPlayer, isActive);
				}
				else{
					orderByVictoryPoints(aiPlayer);
					buyPersonForVictoryPoints(aiPlayer);
				}
			}
		}
	}

	private void decideOnHarborDisplayWhenCantGetPersonOfInterest(Player aiPlayer, boolean isActive) {
		ArrayList<Card> mostCoinsShips = getMostCoinsShips(aiPlayer);
		if(mostCoinsShips.size() < 1){
			orderByVictoryPoints(aiPlayer);
			buyPersonForVictoryPoints(aiPlayer);
		}
		else{
			if(isActive){
				tradeAndHireController.trade(mostCoinsShips.get(mostCoinsShips.size()-1));
			}
			else{
				Card ship = mostCoinsShips.get(mostCoinsShips.size() - 1);
				if((aiPlayer.getTradersCount(ship.getCardColour()) - ship.getCoinValue()) < -2){
					tradeAndHireController.trade(ship);
				}
			}
		}
	}

	private void takeCardsWhenCanBuyDesiredPersonHard(Player aiPlayer) {
		ArrayList<Card> affordableDesiredPersons = getAffordableDesiredPersons(aiPlayer);
		ArrayList<Card> affordableAdmirals = (ArrayList<Card>) affordableDesiredPersons.stream().filter(person -> person.getCardName().equals(CardName.ADMIRAL)).collect(Collectors.toList());
		affordableAdmirals.sort(Comparator.comparing(Card::getCoinValue));
		if(affordableAdmirals.size() > 0){
			tradeAndHireController.hire(affordableAdmirals.get(affordableAdmirals.size()-1));
		}
		else{
			ArrayList<Card> affordableSwordsmen = (ArrayList<Card>) affordableDesiredPersons.stream().filter(person -> (person.getCardName().equals(CardName.SAILOR) || person.getCardName().equals(CardName.PIRATE))).collect(Collectors.toList());
			affordableSwordsmen.sort(Comparator.comparing(Card::getCoinValue));
			ArrayList<Card> affordablePirates = (ArrayList<Card>) affordableDesiredPersons.stream().filter(person -> person.getCardName().equals(CardName.PIRATE)).collect(Collectors.toList());
			affordablePirates.sort(Comparator.comparing(Card::getCoinValue));
			if(aiPlayer.getSwordsCount() == 0){
				if(!affordablePirates.isEmpty()){
					tradeAndHireController.hire(affordablePirates.get(affordablePirates.size()-1));
				}
				else if(!affordableSwordsmen.isEmpty()){
					tradeAndHireController.hire(affordableSwordsmen.get(affordableSwordsmen.size()-1));
				}
			}
			else{
				if(!affordableSwordsmen.isEmpty()){
					tradeAndHireController.hire(affordableSwordsmen.get(affordableSwordsmen.size()-1));
				}
			}
		}
	}

	private ArrayList<Card> getMostCoinsShips(Player aiPlayer){
		ArrayList<Card> ships = (ArrayList<Card>) harborDisplay.stream().filter(card -> card.getCardType().equals(CardType.SHIP)).collect(Collectors.toList());
		ships.sort(Comparator.comparingInt(ship -> (aiPlayer.getTradersCount(ship.getCardColour()) - ship.getCoinValue())));
		return ships;
	}

	private void takeCardsWhenNoRealIntentHard(Player aiPlayer) {
		ArrayList<Card> ships = getMostCoinsShips(aiPlayer);
		Card mostCoinsShip = null;
		if(ships.size() > 0){
			mostCoinsShip = ships.get(ships.size() - 1);
		}
		if((mostCoinsShip != null) && mostCoinsShip.getCoinValue() - aiPlayer.getTradersCount(mostCoinsShip.getCardColour()) < -2){
			tradeAndHireController.trade(mostCoinsShip);
		}
		else{
			orderByVictoryPoints(aiPlayer);
			buyPersonForVictoryPoints(aiPlayer);

		}
	}

	private ArrayList<Card> getAffordableDesiredPersons(Player aiPlayer){
		ArrayList<Card> ret = new ArrayList<>();
		for(Card i : personsOfInterest){
			if(canBuyPerson(aiPlayer, i)){
				ret.add(i);
			}
		}
		return ret;
	}

	//[PP4, CC4]
	//[P1, C1, S3, J1]
	//[4,  4,  9, 6]
	//[2/4, 4/4, 1/3, 5/6]
	//[C1]

	private void orderByVictoryPoints(Player aiPlayer){
		victoryPointsOrder = (ArrayList<Card>) harborDisplay.stream().filter(card -> !(card.getCardType().equals(CardType.SHIP))).collect(Collectors.toList());
		victoryPointsOrder.sort(Comparator.comparing(Card::getVictoryPoints));
		if(expeditionDisplay.size() > 0) {
			victoryPointsOrder.sort(Comparator.comparingDouble(person -> (getVictoryPointsCostEffectivity(aiPlayer, person))));
		}
	}

	private double getVictoryPointsCostEffectivity(Player aiPlayer, Card person){
		if((person.getCardName() != CardName.SETTLER) && (person.getCardName() != CardName.PRIEST) && (person.getCardName() != CardName.CAPTAIN) && (person.getCardName() != CardName.JACK_OF_ALL_TRADES)){
			return ((double) person.getVictoryPoints()) / ((double) person.getCoinValue());
		}
		double max = 0;
		ArrayList<Card> resourcePeople = (ArrayList<Card>) aiPlayer.getPersonalDisplay().stream().filter(dude -> ((dude.getCrosses() > 0) || (dude.getHouses() > 0) || (dude.getAnchors() > 0))).collect(Collectors.toList());
		for(Card expedition : expeditionDisplay){
			int anchors = expedition.getAnchors();
			int houses = expedition.getHouses();
			int crosses = expedition.getCrosses();
			int resourcesNeeded = anchors + houses + crosses;
			int ownAnchors = (int) resourcePeople.stream().filter(dude -> dude.getCardName().equals(CardName.CAPTAIN)).count();
			int ownCrosses = (int) resourcePeople.stream().filter(dude -> dude.getCardName().equals(CardName.PRIEST)).count();
			int ownHouses = (int) resourcePeople.stream().filter(dude -> dude.getCardName().equals(CardName.SETTLER)).count();
			int ownJokers = aiPlayer.getJacksCount();
			//do some maths
			anchors -= ownAnchors;
			anchors = Math.max(0, anchors);
			houses -= ownHouses;
			houses = Math.max(0, houses);
			crosses -= ownCrosses;
			crosses = Math.max(0, crosses);
			int remainingResources = anchors + houses + crosses - ownJokers;
			remainingResources = Math.max(0, remainingResources);
			int ownedApplicableResources = resourcesNeeded - remainingResources;
			double effectivityFromHand = ((double) ownedApplicableResources) / ((double) resourcesNeeded);
			if(remainingResources > 0){
				if(person.getCardName().equals(CardName.SETTLER) && houses > 0){
					double effectivity = (((double) 2) / ((double) person.getCoinValue())) + effectivityFromHand;
					max = Math.max(effectivity, max);
				}
				else if(person.getCardName().equals(CardName.CAPTAIN) && anchors > 0){
					double effectivity = (((double) 2) / ((double) person.getCoinValue())) + effectivityFromHand;
					max = Math.max(effectivity, max);
				}
				else if(person.getCardName().equals(CardName.PRIEST) && crosses > 0){
					double effectivity = (((double) 2) / ((double) person.getCoinValue())) + effectivityFromHand;
					max = Math.max(effectivity, max);
				}
				else if(person.getCardName().equals(CardName.JACK_OF_ALL_TRADES)){
					double effectivity = (((double) 2) / ((double) person.getCoinValue())) + effectivityFromHand;
					max = Math.max(effectivity, max);
				}
				else{
					max = Math.max(((double) person.getVictoryPoints()) / ((double) person.getCoinValue()), max);
				}
			}
			else{
				max = Math.max(((double) person.getVictoryPoints()) / ((double) person.getCoinValue()), max);
			}
		}
		return max;
	}

	private void buyPersonForVictoryPoints(Player aiPlayer){
		int size = victoryPointsOrder.size();
		for(int i = size - 1; i >= 0; i--){
			if(canBuyPerson(aiPlayer, victoryPointsOrder.get(i))) {
				tradeAndHireController.hire(victoryPointsOrder.get(i));
				break;
			}
		}
	}

	private void fulfillExpeditionsHard(Player aiPlayer){
		expeditionDisplay.sort(Comparator.comparingInt(Card::getVictoryPoints).reversed());
		for(Card expedition : expeditionDisplay){
			ArrayList<Card> cardsToFulfill = getCardsForExpedition(aiPlayer, expedition);
			if(cardsToFulfill != null){
				discoverController.startExpedition(expedition, cardsToFulfill);
				update();
			}
		}
	}

	private ArrayList<Card> getCardsForExpedition(Player aiPlayer, Card expedition){
		int anchors = expedition.getAnchors();
		int houses = expedition.getHouses();
		int crosses = expedition.getCrosses();
		int jokers = aiPlayer.getJacksCount();
		int ownedAnchors = aiPlayer.getAnchorsCount();
		int ownedHouses = aiPlayer.getHousesCount();
		int ownedCrosses = aiPlayer.getCrossesCount();
		int anchorsToUse = Math.min(ownedAnchors, anchors);
		int housesToUse = Math.min(ownedHouses, houses);
		int crossesToUse = Math.min(ownedCrosses, crosses);
		int remainingJokers = (anchors - anchorsToUse) + (houses - housesToUse) + (crosses - crossesToUse);
		if(remainingJokers <= jokers){
			ArrayList<Card> cardsToUse = new ArrayList<>();
			CardFactory cardFactory = new CardFactory();
			for(int i = 0; i < anchorsToUse; i++){
				cardsToUse.add(cardFactory.generatePerson(CardName.CAPTAIN, CardColour.NONE, 1, 4));
			}
			for(int i = 0; i < housesToUse; i++){
				cardsToUse.add(cardFactory.generatePerson(CardName.SETTLER, CardColour.NONE, 1, 4));
			}
			for(int i = 0; i < crossesToUse; i++){
				cardsToUse.add(cardFactory.generatePerson(CardName.PRIEST, CardColour.NONE, 1, 4));
			}
			for(int i = 0; i < remainingJokers; i++){
				cardsToUse.add(cardFactory.generatePerson(CardName.JACK_OF_ALL_TRADES, CardColour.NONE, 1, 4));
			}
			return cardsToUse;
		}
		else{
			return null;
		}
	}

	/**
	 * Sets the GUIController, needs to be called before use
	 * @param guiController guiController
	 */
	public void setGUIController(GUIController guiController){
		this.mainGameViewController = guiController.getMainGameViewController();
	}

	/**
	 * Constructor that sets the game controller
	 * @param gameController main controller of the game that knows the data structure and the other controllers
	 */
	public AIController(GameController gameController){
		this.gameController = gameController;
	}

}
