package view;

import javafx.event.Event;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class ViewController {
    protected Parent root;
    protected GUIController guiController;

    public void setGUIController(GUIController guiController) {
        this.guiController = guiController;
    }

    public void setRoot(Parent root) {
        this.root = root;
    }

    public Parent getRoot() {
        if(this.root == null) {
            throw new IllegalStateException();
        }
        return this.root;
    }

    public void notify(CardViewController cardViewController, Event event) throws IOException {
        return;
    }

    public abstract void update() throws IOException;
}
