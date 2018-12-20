package net.nsreverse.crm.java.utils;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertDialog {

    private String title;
    private String message;

    private AlertDialog() {
        title = "";
        message = "";
    }

    public static AlertDialog withTitle(String title) {
        AlertDialog dialog = new AlertDialog();
        dialog.title = title;
        return dialog;
    }

    public AlertDialog message(String message) {
        this.message = message;
        return this;
    }

    public void show() {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setTitle(title);
        modal.setMinWidth(250);

        Label label = new Label(message);

        Button closeButton = new Button("Close");
        closeButton.setDefaultButton(true);
        closeButton.setOnAction(event -> modal.close());

        StackPane labelLayout = new StackPane();
        labelLayout.getChildren().add(label);

        StackPane buttonLayout = new StackPane();
        buttonLayout.getChildren().add(closeButton);

        VBox rootLayout = new VBox(10);
        rootLayout.setPadding(new Insets(10, 20, 10, 20));
        rootLayout.getChildren().addAll(labelLayout, buttonLayout);

        Scene currentScene = new Scene(rootLayout);
        modal.setScene(currentScene);
        modal.show();
    }
}
