package application;
	
import controller.GameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import model.Player;
import view.CardViewController;
import view.GUIController;
import view.MainGameViewController;
import view.MainMenuViewController;
import org.tensorflow.TensorFlow;

import java.io.IOException;
import java.util.ArrayList;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {

		System.out.println("TensorFlow " + TensorFlow.version());//temporary to remind people that we may use neural networks
		GameController gameController = new GameController();
		GUIController guiController = new GUIController(gameController,primaryStage);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
