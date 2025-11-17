package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class adminDashBoardController {

    @FXML
    private Label welcomeLabel;

    // ---------- Table for approved returns ----------
    @FXML
    private TableView<ApprovedReturn> tblApprovedReturns;
    @FXML
    private TableColumn<ApprovedReturn, String> colBookTitle;
    @FXML
    private TableColumn<ApprovedReturn, String> colStudentName;
    @FXML
    private TableColumn<ApprovedReturn, String> colReturnDate;
    @FXML
    private TableColumn<ApprovedReturn, String> colStatus;
    @FXML
    private TableColumn<ApprovedReturn, String> colFine;  // new column for fines

    // ---------- Button Handlers ----------

    @FXML
    private void handleAddStudent(ActionEvent event) {
        showAlert("Add Student", "Add Student button clicked.");
        loadPage("addStudent.fxml");
    }

    @FXML
    private void handleAuthorizeBorrow(ActionEvent event) {
        showAlert("Authorize Borrow Requests", "Authorize Borrow Request button clicked.");
        loadPage("approveBorrowReq.fxml");
    }

    @FXML
    private void handleAddBookCopies(ActionEvent event) {
        showAlert("Add New Book Copies", "Add New Book Copy button clicked.");
        loadPage("addBook.fxml");
    }

    @FXML
    private void handleApproveReturned(ActionEvent event) {
        // Approve returned books in the database
        try (Connection conn = Database.getConnection()) {
            String sql = "UPDATE borrow_records " +
                         "SET status = 'Returned' " +
                         "WHERE return_date IS NOT NULL AND status = 'Borrowed'";
            
            int updated = conn.createStatement().executeUpdate(sql);
            
            showAlert("Approve Returned Books", updated + " record(s) approved successfully.");
            
            // Reload the table to reflect newly approved books
            loadApprovedReturns();
        } catch (Exception e) {
            showAlert("Error", "Failed to approve returned books: " + e.getMessage());
            e.printStackTrace();
        }
        loadPage("approveReq.fxml");
    }

    @FXML
    private void handleManageLateFees(ActionEvent event) {
        // Calculate late fees in the database
        try (Connection conn = Database.getConnection()) {

            // 1️⃣ Update fines for approved returned books
            String sqlReturned = "UPDATE borrow_records " +
                                 "SET fine = DATEDIFF(return_date, due_date) " +
                                 "WHERE status = 'Returned' AND return_date > due_date";
            int returnedUpdated = conn.createStatement().executeUpdate(sqlReturned);

            // 2️⃣ Optional: temporary fines for overdue but not returned books
            String sqlBorrowed = "UPDATE borrow_records " +
                                 "SET fine = DATEDIFF(CURDATE(), due_date) " +
                                 "WHERE status = 'Borrowed' AND CURDATE() > due_date";
            int borrowedUpdated = conn.createStatement().executeUpdate(sqlBorrowed);

            showAlert("Manage Late Fees", 
                returnedUpdated + " returned record(s) updated with late fees.\n" +
                borrowedUpdated + " borrowed record(s) updated with temporary overdue fines.");

            // Refresh the table to show updated fines
            loadApprovedReturns();

        } catch (Exception e) {
            showAlert("Error", "Failed to manage late fees: " + e.getMessage());
            e.printStackTrace();
        }
        loadPage("manageFee.fxml");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        showAlert("Logout", "You have been logged out successfully.");
        // You can add scene switching logic here later.
    }

    // ---------- TableView Logic ----------
    @FXML
    private void initialize() {
        // Setup table columns
        colBookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colFine.setCellValueFactory(new PropertyValueFactory<>("fine")); // bind fine column

        // Load approved returned books
        loadApprovedReturns();
    }

    private void loadApprovedReturns() {
        ObservableList<ApprovedReturn> list = FXCollections.observableArrayList();
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT b.title, s.first_name, s.last_name, br.return_date, br.status, br.fine " +
                         "FROM borrow_records br " +
                         "JOIN books b ON br.book_id = b.book_id " +
                         "JOIN student s ON br.student_id = s.student_id " +
                         "WHERE br.status = 'Returned'";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String bookTitle = rs.getString("title");
                String studentName = rs.getString("first_name") + " " + rs.getString("last_name");
                String returnDate = rs.getString("return_date");
                String status = rs.getString("status");
                String fine = rs.getString("fine");

                list.add(new ApprovedReturn(bookTitle, studentName, returnDate, status, fine));
            }

            tblApprovedReturns.setItems(list);

        } catch (Exception e) {
            showAlert("Error", "Failed to load approved returns: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ---------- Utility ----------
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ---------- Model Class ----------
    public static class ApprovedReturn {
        private final String bookTitle;
        private final String studentName;
        private final String returnDate;
        private final String status;
        private final String fine; // added fine

        public ApprovedReturn(String bookTitle, String studentName, String returnDate, String status, String fine) {
            this.bookTitle = bookTitle;
            this.studentName = studentName;
            this.returnDate = returnDate;
            this.status = status;
            this.fine = fine;
        }

        public String getBookTitle() { return bookTitle; }
        public String getStudentName() { return studentName; }
        public String getReturnDate() { return returnDate; }
        public String getStatus() { return status; }
        public String getFine() { return fine; }
    }
}
