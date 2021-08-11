package view;

import ai.AIController;
import controller.Action;
import controller.LogController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class MainGameViewController extends ViewController {
    @FXML
    private BorderPane root;
    @FXML
    private VBox drawPile;

    @FXML
    private Label drawPileCardsCount;

    @FXML
    private Label discardPileCardsCount;

    @FXML
    private VBox discardPile;

    @FXML
    private ScrollPane harborPane;

    @FXML
    private ScrollPane expeditionPane;

    @FXML
    private HBox expeditionsDisplay;

    @FXML
    private HBox passivePlayersDisplay;

    @FXML
    private HBox harborDisplay;

    @FXML
    private Button undoButton;

    @FXML
    private Button redoButton;

    @FXML
    private Button endPhase;

    @FXML
    private HBox playersCardsDisplay;

    private ArrayList<Card> loadedCards;
    private ArrayList<CardViewController> loadedCardViewControllers;
    //private HashMap<Player,Node> passivePlayerViewsMap;
    //private HashMap<Player,PassivePlayerViewController> passivePlayerViewControllersMap;
    private Node[] passivePlayerViews;
    private PassivePlayerViewController[] passivePlayerViewControllers;
    @FXML
    private Label victoryPointsLabel;
    @FXML
    private Label swordsLabel;
    @FXML
    private Label anchorsLabel;
    @FXML
    private Label housesLabel;
    @FXML
    private Label crossesLabel;
    @FXML
    private Label coinLabel;
    @FXML
    private Label actingPlayerName;


    @FXML
    void initialize() {
        try {
            harborPane.setFitToHeight(true);
            expeditionPane.setFitToHeight(true);
            expeditionPane.setFitToWidth(true);
            // Loads the passive player views.
            passivePlayerViews = new Parent[5];
            passivePlayerViewControllers = new PassivePlayerViewController[5];
            for(int i = 0; i < 5; i++) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/passivePlayerView.fxml"));
                passivePlayerViews[i] = fxmlLoader.load();
                passivePlayerViewControllers[i] = fxmlLoader.getController();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void mainMenuClicked() {
        MainMenuViewController mainMenuViewController = this.guiController.getMainMenuViewController();
        Parent mainMenu = mainMenuViewController.getRoot();
        mainMenuViewController.update();
        this.guiController.swapScene(mainMenu);
    }

    @FXML
    void hintClicked() {
        Game game = this.guiController.getGameController().getGame();
        // TODO: Credits view
        AIController aiController = guiController.getGameController().getAIController();
        aiController.setGUIController(guiController);
        this.guiController.getReportViewController().showMessage(aiController.generateTip());
    }

    @FXML
    void undoClicked() {
        this.guiController.getGameController().getLogController().undo();
        update();
    }

    @FXML
    void redoClicked() {
        this.guiController.getGameController().getLogController().redo();
        update();
    }

    @FXML
    public void endPhaseClicked() {
        ReportViewController reportViewController = this.guiController.getReportViewController();
        Game game = this.guiController.getGameController().getGame();
        switch(game.getCurrentGamePhase()) {
            case DISCOVER : {
                reportViewController.showMessage("Ahoi!\nZeit zu handeln und zu heuern.");
                this.guiController.getGameController().getTradeAndHireController().startPhase(false);
                game = this.guiController.getGameController().getGame();
                game.setLastCardDrawn(null);
                update();
                break;
            }
            case TRADE_AND_HIRE : {
                Player nextActingPlayer = this.guiController.getGameController().getTradeAndHireController().nextActingPlayer();
                if(nextActingPlayer.equals(game.getActivePlayer())) {
                    if(this.guiController.getGameController().getPlayerController().checkWinCondition()) {
                        ArrayList<Player> winners = this.guiController.getGameController().endGame();
                        String winnersString = "";
                        for(int i = 0; i < winners.size(); i++) {
                            if(i < (winners.size() - 1)) {
                                winnersString += winners.get(i).getName() + " ";
                            } else {
                                winnersString += winners.get(i).getName();
                            }
                        }
                        if(winners.size() == 1) {
                            reportViewController.showMessage("Das Spiel ist vorbei!\nGewonnen hat:\n" + winnersString);
                        } else {
                            reportViewController.showMessage("Das Spiel ist vorbei!\nGewonnen haben:\n" + winnersString);
                        }
                        MainMenuViewController mainMenuViewController = this.guiController.getMainMenuViewController();
                        Parent mainMenu = mainMenuViewController.getRoot();
                        mainMenuViewController.update();
                        this.guiController.swapScene(mainMenu);
                        return;
                    } else {
                        LogController logController = this.guiController.getGameController().getLogController();
                        game = this.guiController.getGameController().getGame();
                        game.setCurrentGamePhase(GamePhase.DISCOVER);
                        logController.log(Action.START_DISCOVER_PHASE,null,null);
                        reportViewController.showMessage("Ahoi!\nZeit zu entdecken.");
                    }
                } else {
                    nextActingPlayer();
                }
                update();
                reportViewController.showMessage(nextActingPlayer.getName() + " was sind eure Befehle?",1500);
                if(nextActingPlayer instanceof AIPlayer){
                    AIController aiController = this.guiController.getGameController().getAIController();
                    aiController.setGUIController(this.guiController);
                    aiController.makeMove((AIPlayer) nextActingPlayer);
                }
                break;
            }
        }
    }

    public void logClicked(ActionEvent actionEvent) {
        LogViewController logViewController = this.guiController.getLogViewController();
        logViewController.showLog();
    }

    @Override
    public void update() {
        Game game = this.guiController.getGameController().getGame();
        switch(game.getCurrentGamePhase()) {
            case DISCOVER : {
                if(game.getLastCardDrawn() == null) {
                    this.endPhase.setDisable(true);
                } else {
                    this.endPhase.setDisable(false);
                }
                break;
            }
            case TRADE_AND_HIRE : {
                this.endPhase.setDisable(false);
                break;
            }
        }
        GameLog gameLog = this.guiController.getGameController().getGameLog();
        if(gameLog.hasPrevious()) {
            this.undoButton.setDisable(false);
        } else {
            this.undoButton.setDisable(true);
        }
        if(gameLog.hasNext()) {
            this.redoButton.setDisable(false);
        } else {
            this.redoButton.setDisable(true);
        }
        updateActingPlayer();
        updatePassivePlayers();
        updateDrawPile();
        updateDiscardPile();
        updateExpeditionDisplay();
        updateHarborDisplay();
    }

    private Parent getCardView(Card card) {
        int index = -1;
        for(int i = 0; i < loadedCards.size(); i++) {
            // Necessary because each Card object needs a separate view. (equals not sufficient)
            if(loadedCards.get(i) == card) {
                index = i;
            }
        }
        if(index >= 0) {
            CardViewController cardViewController = this.loadedCardViewControllers.get(index);
            cardViewController.update();
            return cardViewController.root;
        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/cardView.fxml"));
                Parent cardView = fxmlLoader.load();
                CardViewController cardViewController = fxmlLoader.getController();
                cardViewController.setGUIController(this.guiController);
                cardViewController.setRoot(cardView);
                cardViewController.setCard(card);
                cardViewController.setOwner(this);
                this.loadedCards.add(card);
                this.loadedCardViewControllers.add(cardViewController);
                return cardView;
            } catch (IOException iOEx) {
                iOEx.printStackTrace();
            }
        }
        return null;
    }

    public void initGame() {
        Game game = this.guiController.getGameController().getGame();

        // Clears all loaded cards.
        this.loadedCards = new ArrayList<>();
        this.loadedCardViewControllers = new ArrayList<>();

        // Passive player
        ArrayList<Player> players = game.getPlayersList();
        //this.passivePlayerViewsMap = new HashMap<>();
        //this.passivePlayerViewControllersMap = new HashMap<>();
        for(int i = 0; i < players.size(); i++) {
            this.passivePlayerViewControllers[i].setGUIController(this.guiController);
            this.passivePlayerViewControllers[i].setPlayer(players.get(i));
            //this.passivePlayerViewControllersMap.put(players.get(i),passivePlayerViewControllers[i]);
            //this.passivePlayerViewsMap.put(players.get(i),this.passivePlayerViews[i]);
        }

        // Removes all passive player views from the main game view.
        // Adds all currently passive players to the main game view.
        passivePlayersDisplay.getChildren().clear();
        updatePassivePlayers();
        Player actingPlayer = game.getActingPlayer();
        for(int i = 0; i < game.getPlayersList().size(); i++) {
            if(!game.getPlayersList().get(i).equals(actingPlayer)) {
                this.passivePlayersDisplay.getChildren().add(passivePlayerViews[i]);
            }
        }

        // Updates all displays.
        update();

        this.guiController.swapScene(root);
        this.guiController.getReportViewController().showMessage("Willkommen in Port Royal!\n");
        this.guiController.getReportViewController().showMessage(game.getActingPlayer().getName() + " was sind eure Befehle?", 1500);
        if(game.getActingPlayer() instanceof AIPlayer){
            AIController aiController = this.guiController.getGameController().getAIController();
            aiController.setGUIController(this.guiController);
            aiController.makeMove((AIPlayer) game.getActingPlayer());
        }
    }


    private void nextActingPlayer() {
        Game game = this.guiController.getGameController().getGame();
        passivePlayersDisplay.getChildren().clear();
        updatePassivePlayers();
        Player actingPlayer = game.getActingPlayer();
        for(int i = 0; i < game.getPlayersList().size(); i++) {
            if(!game.getPlayersList().get(i).equals(actingPlayer)) {
                this.passivePlayersDisplay.getChildren().add(passivePlayerViews[i]);
            }
        }
    }

    private void updatePassivePlayers() {
        Game game = this.guiController.getGameController().getGame();
        ArrayList<Player> players = game.getPlayersList();
        for(int i = 0; i < players.size(); i++) {
            passivePlayerViewControllers[i].setPlayer(players.get(i));
            passivePlayerViewControllers[i].update();
        }
    }

    private void updateActingPlayer() {
        this.playersCardsDisplay.getChildren().clear();
        Game game = this.guiController.getGameController().getGame();
        Player player = game.getActingPlayer();
        ArrayList<Card> actingPlayerCards = player.getPersonalDisplay();
        for(Card card : actingPlayerCards) {
            Parent cardView = getCardView(card);
            this.playersCardsDisplay.getChildren().add(cardView);
        }
        this.victoryPointsLabel.setText(String.valueOf(player.getVictoryPointsCount()));
        this.swordsLabel.setText(String.valueOf(player.getSwordsCount()));
        this.anchorsLabel.setText(player.getAnchorsCount() + " (" + player.getJacksCount() + ")");
        this.crossesLabel.setText(player.getCrossesCount() + " (" + player.getJacksCount() + ")");
        this.housesLabel.setText(player.getHousesCount() + " (" + player.getJacksCount() + ")");
        this.coinLabel.setText(String.valueOf(player.getCoinsCount()));
        this.actingPlayerName.setText(player.getName());
    }

    private void updateDrawPile() {
        this.drawPile.getChildren().clear();
        Game game = this.guiController.getGameController().getGame();
        ArrayList<Card> drawPile = game.getDrawPile().getPile();
        if(!drawPile.isEmpty()) {
            Card card = drawPile.get(0);
            Parent cardView = getCardView(card);
            this.drawPile.getChildren().add(cardView);
        }
        this.drawPileCardsCount.setText(Integer.toString(drawPile.size()));
    }

    private void updateDiscardPile() {
        this.discardPile.getChildren().clear();
        Game game = this.guiController.getGameController().getGame();
        ArrayList<Card> discardPile = game.getDiscardPile();
        if(!discardPile.isEmpty()) {
            Card card = discardPile.get(discardPile.size() - 1);
            Parent cardView = getCardView(card);
            this.discardPile.getChildren().add(cardView);
        }
        this.discardPileCardsCount.setText(Integer.toString(discardPile.size()));
    }

    public void updateExpeditionDisplay() {
        this.expeditionsDisplay.getChildren().clear();
        Game game = this.guiController.getGameController().getGame();
        ArrayList<Card> cards = game.getExpeditionDisplay();
        for(Card card : cards) {
            Parent cardView = getCardView(card);
            this.expeditionsDisplay.getChildren().add(cardView);
        }
    }

    public void updateHarborDisplay() {
        this.harborDisplay.getChildren().clear();
        Game game = this.guiController.getGameController().getGame();
        ArrayList<Card> cards = game.getHarborDisplay();
        for(Card card : cards) {
            Parent cardView = getCardView(card);
            this.harborDisplay.getChildren().add(cardView);
        }
    }

    @Override
    public void notify(CardViewController cardViewController, Event event) throws IOException {
        ReportViewController reportViewController = this.guiController.getReportViewController();
        Game game = this.guiController.getGameController().getGame();
        Node display = cardViewController.getRoot().getParent();
        Card card = cardViewController.getCard();
        if(event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
                switch(game.getCurrentGamePhase()) {
                    case DISCOVER : {
                        if (display.equals(this.drawPile)) {
                            this.guiController.getGameController().getDiscoverController().drawCard();
                            updateDrawPile();
                            update();
                            switch(card.getCardType()) {
                                case TAX_INCREASE :
                                    updateDiscardPile();
                                    break;
                                case EXPEDITION :
                                    update();
                                    break;
                                case PERSON :
                                    update();
                                    break;
                                case SHIP :
                                    if(this.guiController.getRepelShipViewController().ask()) {
                                        if(this.guiController.getGameController().getDiscoverController().repelShip()) {
                                            update();
                                        } else {
                                            reportViewController.showMessage("Argh!\nDas war wohl nichts!");
                                            update();
                                        }
                                    } else {
                                        update();
                                    }
                                    if(this.guiController.getGameController().checkShipColours()) {
                                        reportViewController.showMessage("Argh!\nDa waren wir wohl zu gierig.\nZeit, dass jemand anders entdeckt.");
                                        this.guiController.getGameController().getTradeAndHireController().startPhase(true);
                                        nextActingPlayer();
                                        update();
                                        Player nextActingPlayer = this.guiController.getGameController().getGame().getActingPlayer();
                                        if(nextActingPlayer instanceof AIPlayer){
                                            AIController aiController = this.guiController.getGameController().getAIController();
                                            aiController.setGUIController(this.guiController);
                                            aiController.makeMove((AIPlayer) nextActingPlayer);
                                        }
                                    }
                                    break;
                            }
                        }
                        else if(display.equals(this.expeditionsDisplay)){
                            FulfillExpeditionViewController fulfillExpeditionViewController = this.guiController.getFulfillExpeditionView();
                            fulfillExpeditionViewController.showExpeditionWindow(card);
                            update();
                        }
                        else if(display.equals(this.harborDisplay)) {
                            this.guiController.getReportViewController().showMessage("Beim Klabautermann!\nEs ist der falsche\n Zeitpunkt dafür!");
                        }
                        break;
                    }
                    case TRADE_AND_HIRE : {
                        if (display.equals(this.harborDisplay)) {
                            switch(card.getCardType()) {
                                case PERSON : {
                                    if(game.getActionPoints() == 0) {
                                        reportViewController.showMessage("Argh!\nWir haben alles getan\n was wir konnten.");
                                        break;
                                    }
                                    if(this.guiController.getGameController().getTradeAndHireController().hire(card)) {
                                        update();
                                    } else {
                                        reportViewController.showMessage("Argh!\nDafür haben wir nicht genug\n Gold.");
                                    }
                                    break;
                                }
                                case SHIP : {
                                    if(game.getActionPoints() == 0) {
                                        reportViewController.showMessage("Argh!\nWir haben alles getan\n was wir konnten.");
                                        break;
                                    }
                                    if(this.guiController.getGameController().getTradeAndHireController().trade(card)) {
                                        update();
                                    } else {
                                        System.err.println("Trade failed. Impossible state.");
                                        reportViewController.showMessage("Ooops!\nHier sollten wir nicht\n landen.");
                                    }
                                    break;
                                }
                                default : throw new IllegalStateException();
                            }
                        } else if (display.equals(this.expeditionsDisplay)) {
                            if(game.getActingPlayer().equals(game.getActivePlayer())) {
                                FulfillExpeditionViewController fulfillExpeditionViewController = this.guiController.getFulfillExpeditionView();
                                fulfillExpeditionViewController.showExpeditionWindow(card);
                                update();
                            } else {
                                this.guiController.getReportViewController().showMessage("Beim Klabautermann!\nDas ist gerade nicht\n unsere Aufgabe!");
                            }
                        } else if(display.equals(this.drawPile)) {
                            this.guiController.getReportViewController().showMessage("Beim Klabautermann!\nEs ist der falsche\n Zeitpunkt dafür!");
                        }
                        break;
                    }
                }
            }
        }
    }


}