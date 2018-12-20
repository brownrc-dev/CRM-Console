package net.nsreverse.crm.java.view;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.nsreverse.crm.java.utils.*;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;

public class LoginController {

    private static String TAG = LoginController.class.getSimpleName();

    @FXML private TitledPane titledPane;
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordTextField;
    @FXML private Button loginButton;
    @FXML private Button cancelButton;
    @FXML private ProgressIndicator loginIndicator;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML private void initialize() {

    }

    public void setupDragListener(Stage stage) {
        // Quick fix.
        // https://stackoverflow.com/questions/13206193/how-to-make-an-undecorated-window-movable-draggable-in-javafx
        titledPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        titledPane.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    @FXML private void loginButtonPressed() {
        // username, password

        if (usernameTextField.getText().trim().isEmpty() || passwordTextField.getText().trim().isEmpty()) {
            AlertDialog.withTitle("Login Failed")
                    .message("Please make sure both username and password fields are used");
            return;
        }

        loginIndicator.setVisible(true);

        try {
            Socket socket = IO.socket(ApplicationContext.socketConnectionString);

            socket.on("loginResults", objects -> {
                JSONObject rootObject = (JSONObject)objects[0];

                if (!rootObject.getBoolean("authStatus")) {
                    if (rootObject.getBoolean("disabled")) {
                        Platform.runLater(() -> AlertDialog.withTitle("Login Failed")
                                .message("This account has been disabled by an administrator. Please contact them for more information.")
                                .show());
                    }
                    else {
                        Platform.runLater(() -> AlertDialog.withTitle("Login Failed")
                                .message("The supplied username or password is incorrect.")
                                .show());
                    }
                }
                else {
                    JSONObject agentProfile = rootObject.getJSONObject("agentProfile");

                    int agentRole = agentProfile.getInt("role");
                    String agentUsername = agentProfile.getString("username");
                    String agentName = agentProfile.getString("name");

                    Platform.runLater(() -> {
                        AgentSession.with(agentRole, agentUsername, agentName);

                        try {
                            Parent root = FXMLLoader.load(getClass().getResource(ResourcesManager.getLayoutPath("controller_main.fxml")));

                            Stage mainStage = new Stage();
                            mainStage.setTitle(AgentSession.getUsername() + " (" + AgentSession.getName() + ") - CRM");
                            mainStage.setScene(new Scene(root));
                            mainStage.show();

                            cancelButtonPressed();
                        }
                        catch (IOException ex) {
                            if (ApplicationContext.loggingEnabled) Logger.e(TAG, "" + ex);
                        }
                    });
                }

                Platform.runLater(() -> loginIndicator.setVisible(false));

                socket.disconnect();
            });

            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    socket.emit("login", usernameTextField.getText().trim(), passwordTextField.getText().trim());
                }
            });

            socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    if (ApplicationContext.loggingEnabled) Logger.i(TAG, "Socket has disconnected.");
                }
            });

            socket.connect();
        }
        catch (URISyntaxException ex) {
            if (ApplicationContext.loggingEnabled) Logger.e(TAG, "Unable to connect to login server: " + ex);

            AlertDialog.withTitle("Login Failure")
                    .message("Unable to connect to the login server")
                    .show();
        }
    }

    @FXML private void cancelButtonPressed() {
        Stage currentStage = (Stage)cancelButton.getScene().getWindow();
        currentStage.close();
    }
}
