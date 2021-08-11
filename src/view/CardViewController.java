package view;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.Card;
import model.Game;

import java.io.IOException;

public class CardViewController extends ViewController {
    private ImageView tooltipImageView;

    @FXML
    private Label imageViewLabel;

    @FXML
    private ImageView imageView;

    private ViewController owner;
    private Image frontSide;
    private Image backside;
    private Card card;

    @FXML
    void clicked(MouseEvent event) throws IOException {
        this.owner.notify(this, event);
    }

    @FXML
    void initialize() {
        tooltipImageView = new ImageView();
        Tooltip tooltip = new Tooltip();
        tooltip.setGraphic(tooltipImageView);
        tooltip.setShowDelay(Duration.seconds(0.5));
        imageViewLabel.setTooltip(tooltip);
        imageView.setFitWidth(140);
        imageView.setFitHeight(215);
        Rectangle rect = new Rectangle(140 ,215);
        rect.setArcHeight(30.0);
        rect.setArcWidth(30.0);
        imageView.setClip(rect);
        tooltipImageView.setFitWidth(275);
        tooltipImageView.setFitHeight(426);
        tooltipImageView.setSmooth(true);
    }

    public void setCard(Card card) {
        this.card = card;
        this.frontSide = new Image(getClass().getResource(this.card.getFrontSideImagePath()).toExternalForm());
        this.backside = new Image(getClass().getResource(this.card.getBackside()).toExternalForm());
        update();
    }

    public void setFit(int width, int height) {
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
    }

    @Override
    public void update() {
        Image image;
        Game game = this.guiController.getGameController().getGame();
        if(game.isFaceUp(this.card)) {
            image = this.frontSide;
        } else {
            image = this.backside;
        }
        imageView.setImage(image);
        tooltipImageView.setImage(image);
    }

    public void setOwner(ViewController owner) {
        this.owner = owner;
    }

    public Card getCard() {
        return this.card;
    }
}
