package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.event.ActionEvent;

public class loginController {

    @FXML
    private TextField studentIdField;

    @FXML
    private PasswordField passwordField;

    private Connection conn;

    public loginController() {
        // Connect to your MySQL database (ensure DBConnection class is set up)
        conn = Database.getConnection();
    }

    // Called when "Student Login" button is clicked
    @FXML
    private void handleStudentLogin(ActionEvent event) {
        loginStudent(event);
    }

    // Called when "Librarian Login" button is clicked
    @FXML
    private void handleLibrarianLogin(ActionEvent event) {
        loginLibrarian(event);
    }

    // ---------------------- STUDENT LOGIN ----------------------
    private void loginStudent(ActionEvent event) {
        String id = studentIdField.getText();
        String password = passwordField.getText();

        String query = "SELECT first_name FROM students WHERE student_id = ? AND password = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome Student " + id + "!");
                loadScene("StudentDashboard.fxml", event);
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Student ID or Password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Something went wrong while checking student login.");
        }
    }

    // ---------------------- LIBRARIAN LOGIN ----------------------
    private void loginLibrarian(ActionEvent event) {
        String id = studentIdField.getText();
        String password = passwordField.getText();

        String query = "SELECT name FROM librarians WHERE librarian_id = ? AND password = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome Librarian " + id + "!");
                loadScene("AdminDashboard.fxml", event);
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Librarian ID or Password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Something went wrong while checking librarian login.");
        }
    }

    // ---------------------- LOAD NEW SCENE ----------------------
    private void loadScene(String fxmlFile, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Load Error", "Could not load " + fxmlFile);
        }
    }

    // ---------------------- SHOW ALERT ----------------------
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
