package application;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import model.borrowRecord;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.time.LocalDate;

public class BorrowedBooksController {

    @FXML
    private TableView<borrowRecord> tblBorrowedBooks;
    @FXML
    private TableColumn<borrowRecord, Integer> colBookId;
    @FXML
    private TableColumn<borrowRecord, String> colTitle;
    @FXML
    private TableColumn<borrowRecord, String> colBorrowDate;
    @FXML
    private TableColumn<borrowRecord, String> colDueDate;
    @FXML
    private TableColumn<borrowRecord, String> colReturnDate;
    @FXML
    private TableColumn<borrowRecord, String> colStatus;
    @FXML
    private TableColumn<borrowRecord, Double> colFine;

    private String studentId;

    // ✅ Set student ID from dashboard
    public void setStudentId(String studentId) {
        this.studentId = studentId;
        loadBorrowedBooks();
    }

    @FXML
    private void initialize() {
        // Bind TableColumns to model properties
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colBorrowDate.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getBorrowDate().toString()
            ));
        colDueDate.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getDueDate().toString()
            ));
        colReturnDate.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                (cellData.getValue().getReturnDate() != null) 
                    ? cellData.getValue().getReturnDate().toString() 
                    : ""
            ));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colFine.setCellValueFactory(new PropertyValueFactory<>("fine"));
    }

    // Load borrowed books from DB
    private void loadBorrowedBooks() {
        ObservableList<borrowRecord> list = FXCollections.observableArrayList();
        String sql = "SELECT br.book_id, b.title, br.borrow_date, br.due_date, br.return_date, br.status, br.fine " +
                     "FROM borrow_records br " +
                     "JOIN books b ON br.book_id = b.book_id " +
                     "WHERE br.student_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String title = rs.getString("title");
                Date borrowDateSQL = rs.getDate("borrow_date");
                Date dueDateSQL = rs.getDate("due_date");
                Date returnDateSQL = rs.getDate("return_date");
                String status = rs.getString("status");
                double fine = rs.getDouble("fine");

                LocalDate borrowDate = borrowDateSQL.toLocalDate();
                LocalDate dueDate = dueDateSQL.toLocalDate();
                LocalDate returnDate = (returnDateSQL != null) ? returnDateSQL.toLocalDate() : null;

                list.add(new borrowRecord(bookId, title, borrowDate, dueDate, returnDate, status, fine));
            }

            tblBorrowedBooks.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
