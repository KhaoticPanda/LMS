package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.books;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class BorrowBookController {

    @FXML
    private TableView<books> tableBooks;

    @FXML
    private TableColumn<books, Integer> colId;

    @FXML
    private TableColumn<books, String> colTitle, colAuthor, colCategory;

    @FXML
    private TableColumn<books, Integer> colCopies;

    @FXML
    private Button btnBorrow, btnClose;

    private String studentId;

    public void setStudentId(String id) {
        this.studentId = id;
        System.out.println("Student ID for borrowing: " + id);
    }

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colCopies.setCellValueFactory(new PropertyValueFactory<>("copiesAvailable"));

        loadAvailableBooks();
    }

    // ---------------- LOAD AVAILABLE BOOKS ----------------
    private void loadAvailableBooks() {
        ObservableList<books> booksList = FXCollections.observableArrayList();
        String query = "SELECT * FROM books WHERE status='Available' AND copies_available > 0";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                booksList.add(new books(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getInt("copies_available")
                ));
            }

            tableBooks.setItems(booksList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- HANDLE BORROW ACTION ----------------
    @FXML
    private void handleBorrowBook() {
        books selectedBook = tableBooks.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a book to borrow.");
            return;
        }

        // Dates
        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(14); // 2 weeks later

        // Updated SQL queries based on your DB
        String insertQuery = "INSERT INTO borrow_records (student_id, book_id, borrow_date, due_date, status) VALUES (?, ?, ?, ?, 'Borrowed')";
        String updateQuery = "UPDATE books SET copies_available = copies_available - 1, " +
                             "status = CASE WHEN copies_available - 1 = 0 THEN 'Borrowed' ELSE status END " +
                             "WHERE book_id = ?";

        try (Connection conn = Database.getConnection()) {

            // Insert record into borrow_records
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, studentId);
            insertStmt.setInt(2, selectedBook.getBookId());
            insertStmt.setDate(3, java.sql.Date.valueOf(borrowDate));
            insertStmt.setDate(4, java.sql.Date.valueOf(dueDate));
            insertStmt.executeUpdate();

            // Update book copies and status
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setInt(1, selectedBook.getBookId());
            updateStmt.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Book borrowed successfully!");
            loadAvailableBooks(); // Refresh list

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Something went wrong while borrowing the book.");
        }
    }

    // ---------------- HANDLE CLOSE BUTTON ----------------
    @FXML
    private void handleClose() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    // ---------------- ALERT HELPER ----------------
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
