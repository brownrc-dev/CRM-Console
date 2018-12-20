package net.nsreverse.crm.java.view.interactions_alerts;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;

public class AddInteractionController {
    @FXML private TextArea ticketDetailsTextArea;
    @FXML private TitledPane titlePane;
    @FXML private Button submitButton;
    @FXML private Button cancelButton;

    private Delegate delegate;
    private NoteMode currentMode;

    private double xOffset = 0.0;
    private double yOffset = 0.0;

    public enum NoteMode {
        INTERACTION,
        ALERT
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    public void setMode(NoteMode mode) {
        currentMode = mode;
    }

    public void setupDragListener(Stage stage) {
        // Quick fix.
        // https://stackoverflow.com/questions/13206193/how-to-make-an-undecorated-window-movable-draggable-in-javafx
        titlePane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        titlePane.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    @FXML private void submitButtonPressed() {
        if (delegate != null) {
            if (currentMode == NoteMode.INTERACTION) {
                delegate.interactionAdded(ticketDetailsTextArea.getText().trim());
            }
            else {
                delegate.alertAdded(ticketDetailsTextArea.getText().trim());
            }
        }

        Stage stage = (Stage)submitButton.getScene().getWindow();
        stage.close();
    }

    @FXML private void cancelButtonPressed() {
        Stage stage = (Stage)cancelButton.getScene().getWindow();
        stage.close();
    }

    public interface Delegate {
        void interactionAdded(String interaction);
        void alertAdded(String interaction);
    }
}
