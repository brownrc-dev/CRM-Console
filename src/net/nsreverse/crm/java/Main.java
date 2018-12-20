package net.nsreverse.crm.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.nsreverse.crm.java.utils.ApplicationContext;
import net.nsreverse.crm.java.utils.Logger;
import net.nsreverse.crm.java.utils.ResourcesManager;
import net.nsreverse.crm.java.view.LoginController;

public class Main extends Application {

    private static String TAG = Main.class.getSimpleName();

    @Override
    public void start(Stage primaryStage) throws Exception{
        if (ApplicationContext.loggingEnabled) Logger.i(TAG, "Application will start.");

        FXMLLoader loader = new FXMLLoader(getClass().getResource(ResourcesManager.getLayoutPath("controller_login.fxml")));
        Parent root = loader.load();

        LoginController controller = loader.getController();
        controller.setupDragListener(primaryStage);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 150));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }


    public static void main(String[] args) {
        if (ApplicationContext.loggingEnabled) Logger.i(TAG,"Loading application...");

        launch(args);
    }
}
