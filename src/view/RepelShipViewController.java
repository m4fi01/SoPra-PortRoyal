package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import model.Card;

import java.io.IOException;

public class RepelShipViewController extends ViewController {
    @FXML
    private Pane root;

    @FXML
    private HBox shipCardDisplay;

    private Card ship;

    private Stage repelShipStage;

    private boolean repelShip;

    @FXML
    void ignoreClicked(ActionEvent event) {
        this.repelShip = false;
        repelShipStage.hide();
    }

    @FXML
    void repelClicked(ActionEvent event) {
        this.repelShip = true;
        repelShipStage.hide();
    }

    @FXML
    void initialize() {
        repelShipStage = new Stage();
        repelShipStage.initStyle(StageStyle.UNDECORATED);
        repelShipStage.initModality(Modality.APPLICATION_MODAL);
        //to make rounded edges
        Rectangle rect = new Rectangle(620,466);
        rect.setArcHeight(60.0);
        rect.setArcWidth(60.0);
        root.setClip(rect);
        Scene scene = new Scene(root);
        repelShipStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        repelShipStage.setScene(scene);
    }

    public void setOwner(Window owner) {
        repelShipStage.initOwner(owner);
    }

    @Override
    public void update() {
        this.ship = this.guiController.getGameController().getGame().getLastCardDrawn();
        updateShipCardDisplay();

    }

    private void updateShipCardDisplay() {
        shipCardDisplay.getChildren().clear();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/cardView.fxml"));
            Parent root = fxmlLoader.load();
            CardViewController cardViewController = fxmlLoader.getController();
            cardViewController.setGUIController(this.guiController);
            cardViewController.setRoot(root);
            cardViewController.setCard(ship);
            cardViewController.setOwner(this);
            this.shipCardDisplay.getChildren().add(root);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public boolean ask() {
        update();

        repelShipStage.showAndWait();
        return this.repelShip;
    }
}
