package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.io.FileInputStream;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            
            // Set up the window (stage)
            primaryStage.setTitle("Library Management System - Login");
            primaryStage.setScene(new Scene(root, 500, 400));
            primaryStage.getIcons().add(new Image(new FileInputStream("resources/icons8-library-24.png")));


            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("⚠️ Failed to load login.fxml. Check file path.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
