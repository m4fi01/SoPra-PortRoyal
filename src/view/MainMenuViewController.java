package view;


import animatefx.animation.SlideInDown;
import controller.GameController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import model.GameLog;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;


public class MainMenuViewController extends ViewController {
    private static final String RULE_BOOK_PATH = "./src/resources/Regeln-Port_Royal_de.pdf";

    @FXML
    private GridPane root;

    @FXML
    private ImageView crownImage;

    @FXML
    private Label topPlayerLabel;

    @FXML
    private ImageView victoryPointImage;

    @FXML
    private Label topScoreLabel;

    @FXML
    private Button saveButton;

    @FXML
    private Button gameButton;

    @FXML
    private Button exitButton;

    @FXML
    private ListView<String> highscoreListView;

    @FXML
    private Label test;

    private boolean gameInProgress;

    @FXML
    void creditsClicked(ActionEvent event) {
        ArrayList<String> developers = new ArrayList<>();
        developers.add("Jana H.");
        developers.add("Phuong H. N.");
        developers.add("H. Hai N.");
        developers.add("Leon K.");
        developers.add("Mohammad A.");
        developers.add("T. Alexander H.");
        Collections.shuffle(developers);
        String credits = developers.get(0) + ", " + developers.get(1) + "\n";
        credits += developers.get(2) + ", " + developers.get(3) + "\n";
        credits += developers.get(4) + ", " + developers.get(5);
        this.guiController.getReportViewController().showMessage(credits);
    }

    @FXML
    void exitClicked(ActionEvent event) {
        if(this.gameInProgress) {
            this.gameInProgress = false;
            update();
        } else {
            System.exit(0);
        }
    }

    @FXML
    void loadGameClicked(ActionEvent event) {
        if(this.gameInProgress) {
            this.guiController.getReportViewController().showMessage("Beim Klabautermann!\nWir sind mitten im Spiel!\nJetzt zu laden kann zum Verlust des akutellen Spielfortschritts führen.");
        }
        GameController gameController = this.guiController.getGameController();
        try {
            File fileToLoad = this.guiController.showLoadFileChooser();
            if(fileToLoad != null) {
                GameLog loadedGame = gameController.getIOController().loadGame(fileToLoad);
                gameController.setGameLog(loadedGame);
                this.gameInProgress = true;
                MainGameViewController mainGameViewController = this.guiController.getMainGameViewController();
                Parent game = mainGameViewController.getRoot();
                mainGameViewController.initGame();
                this.guiController.swapScene(game);
            }
        } catch(FileNotFoundException fNFEX) {
            // TODO: Error message for user "FEHLER: Die Datei konnte nicht gefunden werden."
            fNFEX.printStackTrace();
        } catch(SecurityException sEx) {
            // TODO: Error message for user "FEHLER: Die Datei ist schreibgeschützt."
            sEx.printStackTrace();
        }
    }

    @FXML
    void newGameClicked(ActionEvent event) {
        if(this.gameInProgress) {
            MainGameViewController mainGameViewController = this.guiController.getMainGameViewController();
            Parent game = mainGameViewController.getRoot();
            mainGameViewController.update();
            this.guiController.swapScene(game);
        } else {
            NewGameViewController newGameViewController = this.guiController.getNewGameViewController();
            Parent newGame = newGameViewController.getRoot();
            newGameViewController.update();
            this.guiController.swapScene(newGame);
        }
    }

    @FXML
    void rulesClicked(ActionEvent event) {
        try {
            Desktop desktop = Desktop.getDesktop();
            if (desktop != null && desktop.isSupported(Desktop.Action.OPEN)) {
                desktop.open(new File(RULE_BOOK_PATH));
                return;
            }
        } catch(UnsupportedOperationException | IOException uOIOEx) {
        }
        // TODO: Error messages
    }

    @FXML
    void saveClicked(ActionEvent event) {
        File saveFile = this.guiController.showSaveFileChooser();
        if(saveFile != null) {
            try {
                if(!this.guiController.getGameController().getIOController().saveGame(saveFile)) {
                    // TODO: Error message for user "FEHLER: Speichern fehlgeschlagen.\nUrsache unbekannt."
                }
            } catch(FileNotFoundException fNFEX) {
                // TODO: Error message for user "FEHLER: Speichern fehlgeschlagen.\nDie Datei konnte nicht gefunden werden."
                fNFEX.printStackTrace();
            } catch(SecurityException sEx) {
                // TODO: Error message for user "FEHLER: Speichern fehlgeschlagen.\nDie Datei ist schreibgeschützt."
                sEx.printStackTrace();
            }
        }
    }

    @FXML
    void initialize() {
        this.gameInProgress = false;
    }

    @Override
    public void update() {
        this.saveButton.setDisable(!this.gameInProgress);
        if(this.gameInProgress) {
            this.gameButton.setText("Spiel fortsetzen");
            this.exitButton.setText("Spiel beenden");
        } else {
            this.gameButton.setText("Neues Spiel");
            this.exitButton.setText("Beenden");
        }
        this.highscoreListView.getItems().clear();
        ArrayList<String> highScore = this.guiController.getGameController().getHighScore();
        this.topPlayerLabel.setText("-");
        this.topScoreLabel.setText("-");
        if(!highScore.isEmpty()) {
            StringTokenizer tokens = new StringTokenizer(highScore.get(0),";");
            if(tokens.countTokens() == 3) {
                this.topScoreLabel.setText(tokens.nextToken());
                tokens.nextToken();
                this.topPlayerLabel.setText(tokens.nextToken());
            }
            for(String highScoreEntry : highScore) {
                StringTokenizer entryTokens = new StringTokenizer(highScoreEntry,";");
                if(entryTokens.countTokens() == 3) {
                    String victoryPoints = entryTokens.nextToken();
                    entryTokens.nextToken();
                    String player = entryTokens.nextToken();
                    String entry = player + " - " + victoryPoints;
                    this.highscoreListView.getItems().add(entry);
                }
            }
        }
    }

    public void setGameInProgress(boolean gameInProgress) {
        this.gameInProgress = gameInProgress;
    }
}