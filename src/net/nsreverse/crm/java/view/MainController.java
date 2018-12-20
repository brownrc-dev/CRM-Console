package net.nsreverse.crm.java.view;

import io.socket.client.IO;
import io.socket.client.Socket;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.nsreverse.crm.java.model.tablecolumnrows.EntitlementRow;
import net.nsreverse.crm.java.model.tablecolumnrows.InfoRow;
import net.nsreverse.crm.java.model.tablecolumnrows.InteractionAlertRow;
import net.nsreverse.crm.java.utils.*;
import net.nsreverse.crm.java.view.interactions_alerts.AddInteractionController;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;

public class MainController implements AddInteractionController.Delegate {
    private static String TAG = MainController.class.getSimpleName();

    @FXML private TextField accountIdTextField;
    @FXML private Label statusLabel;
    @FXML private ProgressIndicator statusIndicator;
    @FXML private TreeView<String> searchResultsTreeView;
    @FXML private Label titleLabel;
    @FXML private TableView<InfoRow> customerInfoTableView;
    @FXML private TableColumn<InfoRow, String> customerInfoKeyColumn;
    @FXML private TableColumn<InfoRow, String> customerInfoValueColumn;
    @FXML private TableView<InteractionAlertRow> customerInteractionsTableView;
    @FXML private TableColumn<InteractionAlertRow, String> customerInteractionsColumn;
    @FXML private TableView<InteractionAlertRow> customerAlertsTableView;
    @FXML private TableColumn<InteractionAlertRow, String> customerAlertsColumn;
    @FXML private TableView<EntitlementRow> entitlementsTableView;
    @FXML private TableColumn<EntitlementRow, SimpleBooleanProperty> customerIsEntitledColumn;
    @FXML private TableColumn<EntitlementRow, SimpleBooleanProperty> customerIsDisabledColumn;
    @FXML private TableColumn<EntitlementRow, String> entitlementNameTableColumn;

    private JSONObject currentAccount;

    @FXML private void initialize() {

    }

    @FXML private void searchButtonPressed() {
        if (ApplicationContext.loggingEnabled) Logger.i(TAG, "Starting search...");

        statusLabel.setText("Starting Search...");
        statusIndicator.setVisible(true);

        TreeItem<String> searchingRoot = new TreeItem<>("Search Results");
        searchingRoot.getChildren().add(new TreeItem<>("(Searching)"));
        searchResultsTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {});

        try {
            Socket socket = IO.socket(ApplicationContext.socketConnectionString);

            socket.on("searchResults", objects -> {
                JSONArray customerArray = (JSONArray)objects[0];

                TreeItem<String> root = new TreeItem<>("Search Results");
                root.setExpanded(true);

                for (Object currentCustomer : customerArray) {
                    JSONObject convertedCustomer = (JSONObject)currentCustomer;

                    TreeItem<String> customerNode = new TreeItem<>(convertedCustomer.getString("_id") + " (" + convertedCustomer.getString("name") + ") ");

                    root.getChildren().add(customerNode);
                }

                Platform.runLater(() -> {
                    searchResultsTreeView.setRoot(root);
                    statusLabel.setText("Search complete.");
                    statusIndicator.setVisible(false);

                    searchResultsTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> loadCustomerInformation(newValue.getValue().trim()));
                });

                socket.disconnect();
            });

            socket.on(Socket.EVENT_CONNECT, listener -> socket.emit("performSearch", accountIdTextField.getText().trim()));

            socket.on(Socket.EVENT_DISCONNECT, listener -> {
                if (ApplicationContext.loggingEnabled) Logger.i(TAG, "Socket has disconnected");
            });

            socket.connect();
        }
        catch (URISyntaxException ex) {
            AlertDialog.withTitle("Unable to Search")
                    .message("Failed to connect to server")
                    .show();
        }
    }

    @FXML private void clearButtonPressed() {
        accountIdTextField.setText("");
    }

    @FXML private void addInteractionButtonPressed() {
        if (currentAccount != null) {
            addNote(AddInteractionController.NoteMode.INTERACTION);
        }
    }

    @FXML private void addAlertButtonPressed() {
        if (currentAccount != null) {
            addNote(AddInteractionController.NoteMode.ALERT);
        }
    }

    private void addNote(AddInteractionController.NoteMode mode) {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(ResourcesManager.getLayoutPath("controller_add_interaction.fxml")));
            Parent root = loader.load();

            AddInteractionController controller = loader.getController();
            controller.setDelegate(this);
            controller.setMode(mode);
            controller.setupDragListener(stage);

            stage.setTitle("Add Note");
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        }
        catch (IOException ex) {
            if (ApplicationContext.loggingEnabled) Logger.e(TAG, "Unable to add note: " + ex);
        }
    }

    private void loadCustomerInformation(String customerString) {
        String customerId = customerString.split(" ")[0];

        if (!customerId.equals("Search")) {
            customerInfoTableView.getItems().clear();

            titleLabel.setText(customerString);
            statusLabel.setText("Loading customer details for " + customerId + ".");
            statusIndicator.setVisible(true);

            try {
                Socket socket = IO.socket(ApplicationContext.socketConnectionString);

                socket.on("searchResults", objects -> {
                    JSONArray customerArray = (JSONArray)objects[0];
                    JSONObject customer = customerArray.getJSONObject(0);

                    currentAccount = customer;

                    ObservableList<InfoRow> infoList = FXCollections.observableArrayList();

                    infoList.add(new InfoRow("ID", customer.getString("_id")));
                    infoList.add(new InfoRow("Name", customer.getString("name")));
                    infoList.add(new InfoRow("Phone", customer.getString("phone")));
                    infoList.add(new InfoRow("Address", customer.getString("address")));
                    infoList.add(new InfoRow("Email", customer.getString("email")));

                    ObservableList<InteractionAlertRow> interactionList = FXCollections.observableArrayList();

                    JSONArray interactionArray = customer.getJSONArray("interactions");

                    for (Object o : interactionArray) {
                        interactionList.add(new InteractionAlertRow((String)o));
                    }

                    ObservableList<InteractionAlertRow> alertList = FXCollections.observableArrayList();

                    JSONArray alertArray = customer.getJSONArray("alerts");

                    for (Object o : alertArray) {
                        alertList.add(new InteractionAlertRow((String)o));
                    }

                    ObservableList<EntitlementRow> entitlementList = FXCollections.observableArrayList();

                    JSONArray entitlementsArray = customer.getJSONArray("entitlements");

                    if (entitlementsArray.length() > 0) {
                        for (Object o : entitlementsArray) {
                            if ((int)o == 1) {
                                entitlementList.add(new EntitlementRow(new SimpleBooleanProperty(true), new SimpleBooleanProperty(false), "Product/Service 1"));
                            }
                            else if ((int)o == 11) {
                                entitlementList.add(new EntitlementRow(new SimpleBooleanProperty(true), new SimpleBooleanProperty(true), "Product/Service 1"));
                            }
                            else if ((int)o == 2) {
                                entitlementList.add(new EntitlementRow(new SimpleBooleanProperty(true), new SimpleBooleanProperty(false), "Product/Service 2"));
                            }
                            else if ((int)o == 12) {
                                entitlementList.add(new EntitlementRow(new SimpleBooleanProperty(true), new SimpleBooleanProperty(true), "Product/Service 2"));
                            }
                            else if ((int)o == 3) {
                                entitlementList.add(new EntitlementRow(new SimpleBooleanProperty(true), new SimpleBooleanProperty(false), "Product/Service 3"));
                            }
                            else if ((int)o == 13) {
                                entitlementList.add(new EntitlementRow(new SimpleBooleanProperty(true), new SimpleBooleanProperty(true), "Product/Service 3"));
                            }
                        }
                    }

                    Platform.runLater(() -> {
                        customerInfoKeyColumn.setCellValueFactory(new PropertyValueFactory<>("mkey"));
                        customerInfoValueColumn.setCellValueFactory(new PropertyValueFactory<>("mvalue"));
                        customerInteractionsColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
                        customerAlertsColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
                        customerIsEntitledColumn.setCellFactory(column -> new CheckBoxTableCell<>());
                        customerIsEntitledColumn.setCellValueFactory(new PropertyValueFactory<>("isEntitled"));
                        customerIsDisabledColumn.setCellFactory(column -> new CheckBoxTableCell<>());
                        customerIsDisabledColumn.setCellValueFactory(new PropertyValueFactory<>("isDisabled"));
                        entitlementNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("entitlementName"));

                        customerInfoTableView.setItems(infoList);
                        customerInteractionsTableView.setItems(interactionList);
                        customerAlertsTableView.setItems(alertList);
                        entitlementsTableView.setItems(entitlementList);

                        statusLabel.setText("Loaded customer details for " + customerId + ".");
                        statusIndicator.setVisible(false);

                        socket.disconnect();
                    });
                });

                socket.on(Socket.EVENT_CONNECT, listener -> socket.emit("performSearch", customerId.trim()));

                socket.connect();
            }
            catch (URISyntaxException ex) {
                AlertDialog.withTitle("Unable to Search")
                        .message("Failed to connect to server")
                        .show();
            }
        }
    }

    @Override
    public void interactionAdded(String interaction) {
        if (currentAccount != null) {
            JSONArray interactions = currentAccount.getJSONArray("interactions");

            int currentSize = interactions.length() + 1;

            String[] tempInteractions = new String[currentSize];
            tempInteractions[0] = "(" + AgentSession.getUsername() + ") - " + interaction;

            for (int i = 0; i < interactions.length(); i++) {
                tempInteractions[i + 1] = (String)interactions.get(i);
            }

            currentAccount.put("interactions", tempInteractions);
            syncAccount();
        }
    }

    @Override
    public void alertAdded(String interaction) {
        if (currentAccount != null) {
            JSONArray alerts = currentAccount.getJSONArray("alerts");

            int currentSize = alerts.length() + 1;

            String[] tempAlerts = new String[currentSize];
            tempAlerts[0] = "(" + AgentSession.getUsername() + ") - " + interaction;

            for (int i = 0; i < alerts.length(); i++) {
                tempAlerts[i + 1] = (String)alerts.get(i);
            }

            currentAccount.put("alerts", tempAlerts);
            syncAccount();
        }
    }

    private void syncAccount() {
        try {
            Socket socket = IO.socket(ApplicationContext.socketConnectionString);

            socket.on(Socket.EVENT_CONNECT, listener -> {
                socket.emit("performUpdateNativeClient", currentAccount.getString("_id"), currentAccount);
                socket.disconnect();
            });

            socket.on(Socket.EVENT_DISCONNECT, listener -> {
                if (ApplicationContext.loggingEnabled) Logger.i(TAG, "Socket has disconnected.");
            });

            socket.connect();
        }
        catch (URISyntaxException ex) {
            if (ApplicationContext.loggingEnabled) Logger.e(TAG, "Unable to sync account: " + ex);
        }
        finally {
            loadCustomerInformation(currentAccount.getString("_id") + " ");
        }
    }
}