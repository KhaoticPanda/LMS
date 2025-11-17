package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class adminDashBoardController {

    @FXML
    private Label welcomeLabel;

    // ---------------- Load a new Page ----------------
    private void loadPage(String fxmlName) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlName));
            Stage stage = new Stage();
            stage.setTitle(fxmlName);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Load Error", "Could not load " + fxmlName);
        }
    }

    // ---------- Button Handlers ----------

    @FXML
    private void handleAddStudent(ActionEvent event) {
        loadPage("addStudent.fxml");
    }

    @FXML
    private void handleAuthorizeBorrow(ActionEvent event) {
        loadPage("BorrowBook.fxml");
    }

    @FXML
    private void handleAddBookCopies(ActionEvent event) {
        loadPage("addBook.fxml");
    }

    @FXML
    private void handleApproveReturned(ActionEvent event) {
        loadPage("approveReq.fxml");
    }

    @FXML
    private void handleManageLateFees(ActionEvent event) {
        loadPage("manageFee.fxml");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        showAlert("Logout", "You have been logged out successfully.");
    }

    // ---------- Utility ----------
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
