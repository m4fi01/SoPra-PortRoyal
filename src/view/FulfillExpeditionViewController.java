package view;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import model.Card;
import model.CardName;
import model.CardType;

import java.io.IOException;
import java.util.ArrayList;

public class FulfillExpeditionViewController extends ViewController{


    @FXML
    private HBox chosenCardsDisplay;

    @FXML
    private HBox personalCardsDisplay;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    @FXML
    private VBox expeditionDisplay;

    @FXML
    private Pane root;

    private Stage expeditionStage;

    private Card expedition;
    private ArrayList<Card> personalCards;
    private ArrayList<Card> chosenCards;
    private ReportViewController reportView;

    @Override
    public void update() throws IOException {
        updatePersonalCardsDisplay();
        updateChosenCardsDisplay();
    }

    public void notify(CardViewController cardViewController, Event event) throws IOException {
        Node display = cardViewController.getRoot().getParent();
        Card card = cardViewController.getCard();

        if(event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 1) {
                if(display.equals(personalCardsDisplay)) {
                    personalCards.remove(card);
                    chosenCards.add(card);
                    updatePersonalCardsDisplay();
                    updateChosenCardsDisplay();
                }
                else if(display.equals(chosenCardsDisplay)){
                    chosenCards.remove(card);
                    personalCards.add(card);
                    updatePersonalCardsDisplay();
                    updateChosenCardsDisplay();
                }
            }
        }
    }

    @FXML
    void initialize() {
        expeditionStage = new Stage();
        expeditionStage.initStyle(StageStyle.UNDECORATED);
        expeditionStage.initModality(Modality.APPLICATION_MODAL);
        //to make rounded edges
        Rectangle rect = new Rectangle(950,600);
        rect.setArcHeight(60.0);
        rect.setArcWidth(60.0);
        root.setClip(rect);
        Scene scene = new Scene(root);
        expeditionStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        expeditionStage.setScene(scene);
        personalCards = new ArrayList<>();
        chosenCards = new ArrayList<>();
    }

    public void setReportView(ReportViewController reportView) {
        this.reportView = reportView;
        this.reportView.setOwner(this.expeditionStage);
    }


    public void confirmClicked(ActionEvent actionEvent) {
        int result = this.guiController.getGameController().getDiscoverController().startExpedition(expedition,chosenCards);
        ReportViewController reportViewController = this.guiController.getReportViewController();
        if(result == 0) {
            this.chosenCards.clear();
            this.expeditionStage.hide();
        }
        if(result == 1) {
            this.reportView.showMessage("Argh!\nWir werden mehr Leute\ndafür abstellen müssen.",2000);
        } else if(result == 2) {
            this.reportView.showMessage("Argh!\nWir sind hier nicht\ndie Wohlfahrt.\nWeniger Personal tut es auch.",2000);
        }
    }

    public void cancelClicked(ActionEvent actionEvent) {
        expeditionStage.hide();
    }

    private void updatePersonalCardsDisplay() throws IOException {
        personalCardsDisplay.getChildren().clear();
        for(Card card:personalCards){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/cardView.fxml"));
            Parent root = fxmlLoader.load();
            CardViewController viewController = fxmlLoader.getController();
            viewController.setGUIController(this.guiController); // TODO: Quick load
            viewController.setRoot(root);
            viewController.setOwner(this);
            viewController.setCard(card);
            this.personalCardsDisplay.getChildren().add(root);
        }
    }

    private void updateChosenCardsDisplay() throws IOException {
        chosenCardsDisplay.getChildren().clear();
        for(Card card:chosenCards){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/cardView.fxml"));
            Parent root = fxmlLoader.load();
            CardViewController viewController = fxmlLoader.getController();
            viewController.setGUIController(this.guiController); // TODO: Quick load
            viewController.setRoot(root);
            viewController.setOwner(this);
            viewController.setCard(card);
            this.chosenCardsDisplay.getChildren().add(root);
        }
    }

    public void showExpeditionWindow(Card expedition) throws IOException {
        this.expeditionDisplay.getChildren().clear();
        this.expedition = expedition;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/cardView.fxml"));
        Parent root = loader.load();
        CardViewController viewController = loader.getController();
        viewController.setGUIController(this.guiController); // TODO: Quick load
        viewController.setOwner(this);
        viewController.setRoot(root);
        viewController.setCard(expedition);
        this.expeditionDisplay.getChildren().add(root);

        //adding Person_Type cards to Display to choose from
        ArrayList<Card> playerCards = guiController.getGameController().getGame().getActingPlayer().getPersonalDisplay();
        for(Card card : playerCards){
            switch(card.getCardName()) {
                case PRIEST :
                case CAPTAIN :
                case SETTLER :
                case JACK_OF_ALL_TRADES : {
                    personalCards.add(card);
                }
            }
        }
        update();
        expeditionStage.showAndWait();
    }

    public void setOwner(Window owner) {
        expeditionStage.initOwner(owner);
    }
}

