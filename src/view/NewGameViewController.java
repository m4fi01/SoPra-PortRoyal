package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.AIPlayer;
import model.Level;
import model.Player;

public class NewGameViewController extends ViewController {
    private static final int MAXIMUM_NUMBER_OF_PLAYERS = 5;
    private static final int MINIMUM_NUMBER_OF_PLAYERS = 2;
    private static final int MAXIMUM_SIMULATION_SPEED = 500;

    @FXML
    private Pane root;
    @FXML
    private RadioButton randomOrderRadioButton;

    @FXML
    private ToggleGroup initialPileGroup;

    @FXML
    private RadioButton fixedOrderRadioButton;

    @FXML
    private TextField playerNameTextfield;

    @FXML
    private Label aiDifficultyLabel;

    @FXML
    private Slider aiSimulationSpeedSlider;

    @FXML
    private ListView<Integer> playersListView;

    @FXML
    private Button cancelButton;

    private Level selectedAILevel;

    private TreeSet<Integer> availableTempPlayerIDs;

    private HashMap<Integer,String> playerNameMap;

    private HashMap<Integer,Level> aiLevelMap;

    private File initialPile;

    @FXML
    void addAIClicked(ActionEvent event) {
        this.addPlayer(selectedAILevel);
    }

    @FXML
    void addPlayerClicked(ActionEvent event) {
        this.addPlayer(null);
    }

    private void addPlayer(Level aiLevel) {
        String playerName = this.playerNameTextfield.getText();
        if(playerName == null || playerName == "") {
            this.guiController.getReportViewController().showMessage("Ahoi Landratte!\nSag mir deinen Namen.");
        } else {
            Integer tempPlayerID = this.availableTempPlayerIDs.pollFirst();
            if(tempPlayerID != null) {
                this.playerNameMap.put(tempPlayerID,playerName);
                this.aiLevelMap.put(tempPlayerID,aiLevel);
                this.playersListView.getItems().add(tempPlayerID);
                this.playersListView.getSelectionModel().selectLast();
                this.playerNameTextfield.setText(null);
                this.update();
            } else {
                this.guiController.getReportViewController().showMessage("Yo-ho-ho!\nEs sind bereits alle da.");
            }
        }
    }


    @FXML
    void randomOrderClicked(ActionEvent event) {
        this.initialPile = null;
    }

    @FXML
    public void cancelClicked(ActionEvent actionEvent) {
        MainMenuViewController mainMenuViewController = guiController.getMainMenuViewController();
        Parent root = mainMenuViewController.getRoot();
        this.initialize();
        guiController.swapScene(root);
    }

    @FXML
    void fixedOrderClicked(ActionEvent event) {
        this.initialPile = this.guiController.showLoadPileChooser();
        if(this.initialPile == null) {
            this.randomOrderRadioButton.setSelected(true);
            this.update();
        }
    }

    @FXML
    void lowerAILevelClicked(ActionEvent event) {
        switch(this.selectedAILevel) {
            case MEDIUM: this.selectedAILevel = Level.EASY; break;
            case HARD : this.selectedAILevel = Level.MEDIUM; break;
            default : return;
        }
        this.update();
    }

    @FXML
    void movePlayerDownClicked(ActionEvent event) {
        Integer selectedListEntry = this.playersListView.getSelectionModel().getSelectedItem();
        if(selectedListEntry != null) {
            ObservableList<Integer> observableList = this.playersListView.getItems();
            int currentIndex = observableList.indexOf(selectedListEntry);
            if(currentIndex != (observableList.size() - 1)) {
                observableList.remove(currentIndex);
                int newIndex = currentIndex + 1;
                observableList.add(newIndex,selectedListEntry);
                this.playersListView.getSelectionModel().select(newIndex);
            }
        }
        this.update();
    }

    @FXML
    void movePlayerUpClicked(ActionEvent event) {
        Integer selectedListEntry = this.playersListView.getSelectionModel().getSelectedItem();
        if(selectedListEntry != null) {
            ObservableList<Integer> observableList = this.playersListView.getItems();
            int currentIndex = observableList.indexOf(selectedListEntry);
            if(currentIndex != 0) {
                observableList.remove(currentIndex);
                int newIndex = currentIndex - 1;
                observableList.add(newIndex,selectedListEntry);
                this.playersListView.getSelectionModel().select(newIndex);
            }
        }
        this.update();
    }

    @FXML
    void raiseAILevelClicked(ActionEvent event) {
        switch(this.selectedAILevel) {
            case EASY : this.selectedAILevel = Level.MEDIUM; break;
            case MEDIUM : this.selectedAILevel = Level.HARD; break;
            default : return;
        }
        this.update();
    }

    @FXML
    void randomisePlayerOrderClicked(ActionEvent event) {
        Collections.shuffle(this.playersListView.getItems());
        this.update();
    }

    @FXML
    void removePlayerClicked(ActionEvent event) {
        Integer selectedListEntry = this.playersListView.getSelectionModel().getSelectedItem();
        if(selectedListEntry != null) {
            ObservableList<Integer> observableList = this.playersListView.getItems();
            observableList.remove(selectedListEntry);
            this.availableTempPlayerIDs.add(selectedListEntry);
            this.playerNameMap.remove(selectedListEntry);
            this.aiLevelMap.remove(selectedListEntry);
            this.update();
        }
    }

    @FXML
    void startGameClicked(ActionEvent event) {
        if(this.playersListView.getItems().size() < MINIMUM_NUMBER_OF_PLAYERS) {
            this.guiController.getReportViewController().showMessage("Arrgh!\nWir sind noch zu wenige.");
        } else {
            try {
                ArrayList<Player> players = new ArrayList<>();
                for(Integer tempPlayerID : this.playersListView.getItems()) {
                    Level aiLevel = this.aiLevelMap.get(tempPlayerID);
                    String name = this.playerNameMap.get(tempPlayerID);
                    Player player;
                    if(aiLevel == null) {
                        player = new Player(name);
                    } else {
                        player = new AIPlayer(aiLevel,name);
                    }
                    players.add(player);
                }
                int simulationSpeed = MAXIMUM_SIMULATION_SPEED - ((MAXIMUM_SIMULATION_SPEED * (int)this.aiSimulationSpeedSlider.getValue()) / 100);
                this.guiController.getGameController().newGame(players,this.initialPile,simulationSpeed);
                this.guiController.getMainMenuViewController().setGameInProgress(true);
                MainGameViewController mainGameViewController = this.guiController.getMainGameViewController();
                mainGameViewController.initGame();
                this.initialize();
            } catch(FileNotFoundException fNFEx) {
                this.guiController.getReportViewController().showMessage("Arrgh!\nDas Pergament für den Nachziehstapel ist verschollen.");
                this.initialPile = null;
                this.randomOrderRadioButton.setSelected(true);
                this.update();
            }
        }
    }

    @FXML
    void initialize() {
        this.availableTempPlayerIDs = new TreeSet<>();
        for(int i = 0; i < MAXIMUM_NUMBER_OF_PLAYERS; i++) {
            this.availableTempPlayerIDs.add(i);
        }
        this.playerNameMap = new HashMap<>();
        this.aiLevelMap = new HashMap<>();
        this.selectedAILevel = Level.EASY;
        this.playersListView.getItems().clear();
        this.playersListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.playersListView.setCellFactory(param -> new ListCell<Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item == null || !playerNameMap.containsKey(item) || !aiLevelMap.containsKey(item)) {
                    setText(null);
                } else {
                    if(aiLevelMap.get(item) == null) {
                        setText("<Mensch> " + playerNameMap.get(item));
                    } else {
                        setText("<KI " + aiLevelMap.get(item) + "> " + playerNameMap.get(item));
                    }
                }
            }
        });
        this.playerNameTextfield.setText(null);
        this.update();
    }

    @Override
    public void update() {
        // TODO: German terms e.g. via getNoun in Level
        this.aiDifficultyLabel.setText(this.selectedAILevel.name());
        this.playersListView.refresh();
    }


}
