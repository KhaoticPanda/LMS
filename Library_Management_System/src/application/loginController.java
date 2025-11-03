package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class loginController {

    // Link these with fx:id in your FXML
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    // This method is triggered by onAction="#handleStudentLogin"
    @FXML
    private void handleStudentLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.WARNING, "Missing Information", "Please enter both username and password.");
            return;
        }

        // Simple login logic (replace with your own validation)
        if (username.equals("student") && password.equals("1234")) {
            showAlert(AlertType.INFORMATION, "Login Successful", "Welcome, " + username + "!");
            // TODO: load the next scene (e.g. dashboard)
        } else {
            showAlert(AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    // Utility method to show alerts
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleLibrarianLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals("librarian") && password.equals("admin123")) {
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome Librarian!");
            // TODO: load librarian dashboard
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid librarian credentials.");
        }
    }

}
