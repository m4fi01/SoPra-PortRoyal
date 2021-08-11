package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import model.Game;

@SuppressWarnings("unchecked")
public class LogViewController extends ViewController {
    @FXML
    private GridPane pane;

    @FXML
    private HBox root;

    @FXML
    private ListView logList;

    @FXML
    private Button continueButton;

    @FXML
    private Label logLeer;

    private Stage logStage;

    @FXML
    void buttonClicked(ActionEvent event) {
        logStage.hide();
    }

    @FXML
    void initialize() {
        logStage = new Stage();
        logStage.setResizable(false);
        logStage.initModality(Modality.WINDOW_MODAL);
        logStage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root);
        logStage.setScene(scene);
    }

    @Override
    public void update() {
        logList.getItems().clear();
        Game game = this.guiController.getGameController().getGame();
        for(String logEntry : game.getActionLog()) {
            logList.getItems().add(logEntry);
        }
    }

    public void showLog(){
        update();
        logLeer.setVisible(logList.getItems().isEmpty());
        this.logStage.showAndWait();
    }

    public void setOwner(Window owner) {
        logStage.initOwner(owner);
    }
}
