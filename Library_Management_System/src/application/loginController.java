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
        // Connect to MySQL
        conn = Database.getConnection();
    }

    // ---------------------- STUDENT LOGIN ----------------------
    @FXML
    private void handleStudentLogin(ActionEvent event) {
        String id = studentIdField.getText();
        String password = passwordField.getText();

        String query = "SELECT * FROM student WHERE student_id = ? AND password = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("first_name"); // assuming your DB column name
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome " + firstName + "!");

                // Load Student Dashboard and pass student data
                FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentDashboard.fxml"));
                Parent root = loader.load();

                // Pass student details to the dashboard controller
                studentDashboardController controller = loader.getController();
                controller.setStudentDetails(id, firstName);

                // Switch scene
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Student ID or Password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Something went wrong while checking student login.");
        }
    }

    // ---------------------- LIBRARIAN LOGIN ----------------------
    @FXML
    private void handleLibrarianLogin(ActionEvent event) {
        String id = studentIdField.getText();
        String password = passwordField.getText();

        String query = "SELECT name FROM librarians WHERE librarian_id = ? AND password = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome Librarian!");
                loadScene("AdminDashboard.fxml", event);
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome Librarian " + id + "!");
                loadScene("admin.fxml", event);
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