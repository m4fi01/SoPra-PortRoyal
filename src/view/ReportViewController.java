package view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.BufferedWriter;
import java.util.Timer;
import java.util.TimerTask;

public class ReportViewController extends ViewController {
    @FXML
    private GridPane root;

    @FXML
    private Label labelReport;

    @FXML
    private Button continueButton;

    private Stage reportStage;

    @FXML
    void buttonClicked(ActionEvent event) {
        reportStage.hide();
    }

    @FXML
    void initialize() {
        reportStage = new Stage();
        reportStage.initStyle(StageStyle.UNDECORATED);
        reportStage.initModality(Modality.APPLICATION_MODAL);
        //to make rounded edges
        Rectangle rect = new Rectangle(815,325);
        rect.setArcHeight(60.0);
        rect.setArcWidth(60.0);
        root.setClip(rect);
        Scene scene = new Scene(root);
        reportStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        reportStage.setScene(scene);
    }

    @Override
    public void update() {
    }

    public void setOwner(Window owner) {
        reportStage.initOwner(owner);
    }

    public void showMessage(String message) {
        labelReport.setText(message);
        reportStage.showAndWait();
    }

    public void showMessage(String message, long millis) {
        labelReport.setText(message);
        root.getChildren().remove(continueButton);
        Timeline wait = new Timeline( new KeyFrame(Duration.millis(millis), new EventHandler<ActionEvent>()
        {
            @Override
            public void handle( ActionEvent event )
            {
                root.getChildren().add(continueButton);
                reportStage.hide();
            }
        } ) );
        wait.setCycleCount( 1 );
        wait.play();
        reportStage.showAndWait();
    }
}
