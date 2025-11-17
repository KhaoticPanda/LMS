package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Optional;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox; // <-- use VBox for centerContent

public class studentDashboardController {

    @FXML
    private Label lblWelcome; // Top header student name

    @FXML
    private VBox centerContent; // Corrected: matches FXML

    private String studentId;
    private String studentName;

    // ---------------- SET STUDENT DETAILS ----------------
    public void setStudentDetails(String id, String name) {
        this.studentId = id;
        this.studentName = name;
        lblWelcome.setText("Welcome, " + name + "!");
    }

    // ---------------- HANDLE VIEW AVAILABLE BOOKS ----------------
    @FXML
    private void handleViewAvailableBooks(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AvailableBooks.fxml"));
            Parent root = loader.load();

            AvailableBooksController controller = loader.getController();
            controller.setStudentId(studentId);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Available Books");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load Available Books page.");
        }
    }

    // ---------------- HANDLE BORROW BOOK ----------------
    @FXML
    private void handleBorrowBook(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BorrowBook.fxml"));
            Parent root = loader.load();

            BorrowBookController controller = loader.getController();
            controller.setStudentId(studentId);

            Stage stage = new Stage();
            stage.setTitle("Borrow Book");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not open Borrow Book window.");
        }
    }

    // ---------------- HANDLE RETURN BOOK ----------------
    @FXML
    private void handleReturnBook(ActionEvent event) {
        try (Connection conn = Database.getConnection()) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Return Book");
            dialog.setHeaderText("Enter the Book ID you want to return:");
            dialog.setContentText("Book ID:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String bookId = result.get().trim();

                String sql = "UPDATE borrow_records " +
                             "SET return_date = CURDATE() " +
                             "WHERE student_id = ? AND book_id = ? AND status = 'Borrowed'";

                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, studentId);
                stmt.setString(2, bookId);

                int updated = stmt.executeUpdate();
                if (updated > 0) {
                    showAlert("Return Book", "Book return requested successfully! Awaiting admin approval.");
                } else {
                    showAlert("Return Book", "No matching borrowed book found or already returned.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to return book: " + e.getMessage());
        }
    }

    // ---------------- HANDLE VIEW BORROWED BOOKS ----------------
    @FXML
    private void handleViewBorrowedBooks(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BorrowedBooks.fxml"));
            Parent root = loader.load();

            BorrowedBooksController controller = loader.getController();
            controller.setStudentId(studentId);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Borrowed Books");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load Borrowed Books page.");
        }
    }

    // ---------------- HANDLE LOGOUT ----------------
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

    // ---------------- ALERT HELPER ----------------
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
