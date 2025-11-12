package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class studentDashboardController {

    @FXML
    private Label lblWelcome;

    private String studentId;
    private String studentName;

    // ✅ Called from loginController after successful login
    public void setStudentDetails(String id, String name) {
        this.studentId = id;
        this.studentName = name;
        lblWelcome.setText("Welcome, " + name + "!");
    }

    // ✅ Handle "View Available Books" button
    @FXML
    private void handleViewAvailableBooks(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AvailableBooks.fxml"));
            Parent root = loader.load();

            AvailableBooksController controller = loader.getController();
            controller.setStudentId(studentId); // Pass studentId to next controller

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Available Books");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load Available Books page.");
        }
    }

    // ✅ Handle Borrow Book
    @FXML
    private void handleBorrowBook(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/BorrowBook.fxml"));
            Parent root = loader.load();

            // Pass student ID to BorrowBookController
            BorrowBookController controller = loader.getController();
            controller.setStudentId(String.valueOf(studentId));

            Stage stage = new Stage();
            stage.setTitle("Borrow Book");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ✅ Handle Return Book
    @FXML
    private void handleReturnBook(ActionEvent event) {
        showAlert("Return Book", "Feature under development.");
    }

    // ✅ Handle View Borrowed Books
    @FXML
    private void handleViewBorrowedBooks(ActionEvent event) {
        showAlert("Borrowed Books", "Feature under development.");
    }

    // ✅ Handle Logout
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Library Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not return to login page.");
        }
    }

    // Utility for showing alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
