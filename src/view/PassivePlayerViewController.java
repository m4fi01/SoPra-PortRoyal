package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import model.Card;
import model.Game;
import model.Player;

import java.io.IOException;
import java.util.ArrayList;

public class PassivePlayerViewController extends ViewController {
    private boolean showPlayersCards;

    @FXML
    private Label victoryPointsLabel;

    @FXML
    private Label swordsLabel;

    @FXML
    private Label anchorsLabel;

    @FXML
    private Label crossesLabel;

    @FXML
    private Label housesLabel;

    @FXML
    private Label playerName;

    @FXML
    private Label playerNameLabel;

    @FXML
    private Button showPlayersCardsButton;

    @FXML
    private ScrollPane playersCardsScrollPane;

    @FXML
    private HBox playersCardsDisplay;

    @FXML
    private Label coinLabel;

    private Player player;

    @FXML
    void showPlayersCardsClicked(ActionEvent event) {
        this.showPlayersCards = !this.showPlayersCards;
        this.playersCardsScrollPane.setVisible(this.showPlayersCards);
    }

    @FXML
    void initialize() {
        this.showPlayersCards = false;
        this.playersCardsScrollPane.setVisible(false);

    }

    public void setPlayer(Player player) {
        this.player = player;
        this.playerNameLabel.setText(player.getName());
    }

    @Override
    public void update() {
        this.playersCardsDisplay.getChildren().clear();
        ArrayList<Card> cards = player.getPersonalDisplay();
        if(cards.isEmpty()) {
            showPlayersCardsButton.setVisible(false);
        } else {
            for(Card card : cards) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/cardView.fxml"));
                    Parent root = fxmlLoader.load();
                    CardViewController cardViewController = fxmlLoader.getController();
                    cardViewController.setGUIController(this.guiController);
                    cardViewController.setRoot(root);
                    cardViewController.setCard(card);
                    cardViewController.setOwner(this);
                    this.playersCardsDisplay.getChildren().add(root);
                } catch (IOException iOEx) {
                    iOEx.printStackTrace();
                }
            }
            showPlayersCardsButton.setVisible(true);
        }
        this.victoryPointsLabel.setText(String.valueOf(this.player.getVictoryPointsCount()));
        this.swordsLabel.setText(String.valueOf(this.player.getSwordsCount()));
        this.anchorsLabel.setText(this.player.getAnchorsCount() + " (" + this.player.getJacksCount() + ")");
        this.crossesLabel.setText(this.player.getCrossesCount() + " (" + this.player.getJacksCount() + ")");
        this.housesLabel.setText(this.player.getHousesCount() + " (" + this.player.getJacksCount() + ")");
        this.coinLabel.setText(String.valueOf(this.player.getCoinsCount()));
    }
}