package view;

import controller.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class GUIController {
    private final GameController GAME_CONTROLLER;
    private final Stage PRIMARY_STAGE;
    private MainMenuViewController mainMenuViewController;
    private MainGameViewController mainGameViewController;
    private ReportViewController reportViewController;
    private NewGameViewController newGameViewController;
    private RepelShipViewController repelShipViewController;
    private FulfillExpeditionViewController fulfillExpeditionViewController;
    private LogViewController logViewController;

    public GUIController(GameController gameController, Stage primaryStage) {
        this.GAME_CONTROLLER = gameController;
        this.PRIMARY_STAGE = primaryStage;
        try{
            this.mainMenuViewController = (MainMenuViewController)this.initViewController("/view/mainMenuView.fxml");
            this.mainGameViewController =  (MainGameViewController)this.initViewController("/view/mainGameView.fxml");
            this.reportViewController = (ReportViewController)this.initViewController("/view/reportView.fxml");
            this.reportViewController.setOwner(this.PRIMARY_STAGE);
            this.newGameViewController = (NewGameViewController)initViewController("/view/newGameView.fxml");
            this.repelShipViewController = (RepelShipViewController)initViewController("/view/repelShipView.fxml");
            this.repelShipViewController.setOwner(this.PRIMARY_STAGE);
            this.fulfillExpeditionViewController = (FulfillExpeditionViewController) this.initViewController("/view/fulfillExpeditionView.fxml");
            this.fulfillExpeditionViewController.setOwner(this.PRIMARY_STAGE);
            ReportViewController fulfillExpeditionReport = (ReportViewController)this.initViewController("/view/reportView.fxml");
            this.fulfillExpeditionViewController.setReportView(fulfillExpeditionReport);
            this.logViewController = (LogViewController) this.initViewController("/view/logView.fxml");
            this.logViewController.setOwner(this.PRIMARY_STAGE);
        } catch(IOException | NullPointerException iONPEx) {
            iONPEx.printStackTrace();
            System.exit(-1);
        }
        primaryStage.setScene(new Scene(this.mainMenuViewController.getRoot()));
        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        this.mainMenuViewController.update();
        primaryStage.show();
    }

    private ViewController initViewController(String fxmlPath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = fxmlLoader.load();
        ViewController viewController = fxmlLoader.getController();
        viewController.setGUIController(this);
        viewController.setRoot(root);
        return viewController;
    }

    public GameController getGameController() {
        return this.GAME_CONTROLLER;
    }

    public MainMenuViewController getMainMenuViewController() {
        return this.mainMenuViewController;
    }

    public MainGameViewController getMainGameViewController() {
        return this.mainGameViewController;
    }

    public ReportViewController getReportViewController() {
        return this.reportViewController;
    }

    public NewGameViewController getNewGameViewController() {
        return this.newGameViewController;
    }

    public RepelShipViewController getRepelShipViewController(){
        return this.repelShipViewController;
    }

    public FulfillExpeditionViewController getFulfillExpeditionView(){ return this.fulfillExpeditionViewController;}

    public LogViewController getLogViewController() { return logViewController; }

    public void swapScene(Parent root) {
        if(root == null) {
            throw new NullPointerException();
        }
        this.PRIMARY_STAGE.getScene().setRoot(root);
    }

    public File showLoadPileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Nachziehstapel (Reihenfolge)");
        fileChooser.setInitialDirectory(new File("./"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Nachziehstapel","*.csv")
        );
        return fileChooser.showOpenDialog(this.PRIMARY_STAGE);
    }

    public File showLoadFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Spiel laden");
        fileChooser.setInitialDirectory(new File("./"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Spielstand","*.ser")
        );
        return fileChooser.showOpenDialog(this.PRIMARY_STAGE);
    }

    public File showSaveFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Spiel speichern");
        fileChooser.setInitialDirectory(new File("./"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Spielstand","*.ser")
        );
        return fileChooser.showSaveDialog(this.PRIMARY_STAGE);
    }
}
