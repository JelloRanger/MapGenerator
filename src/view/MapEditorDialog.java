package view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapEditorDialog extends Application {

    private static final String TAG = MapEditorDialog.class.getSimpleName();

    private final String TITLE = "MapMaker";

    @Override
    public void start(Stage primaryStage) {
        try {
            AnchorPane page = FXMLLoader.load(getClass().getResource("main.fxml"));
            ScrollPane toolPane = (ScrollPane) page.lookup("#mToolPane");
            double width = toolPane.getPrefWidth();
            double height = toolPane.getPrefHeight();
            MenuBar menuBar = (MenuBar) page.lookup("#mMenuBar");
            menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
            double menuWidth = menuBar.getWidth();
            double menuHeight = menuBar.getHeight();
            toolPane.setPrefHeight(1000 > height ? 1000:height);
            Scene scene = new Scene(page, width + 1000, 1000 > height ? 1000 + menuHeight : height + menuHeight);
            primaryStage.setScene(scene);
            primaryStage.setTitle(TITLE);
            primaryStage.show();

            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Platform.exit();
                    System.exit(0);
                }
            });
        } catch (IOException ioe) {
            Logger.getLogger(TAG).log(Level.SEVERE, null, ioe);
        }

    }

    public static void main(String args[]) {
        launch(args);
    }
}
