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

public class AvailableBooksController {

    // ----------------- FXML Fields -----------------
    @FXML private TableView<books> tableBooks;
    @FXML private TableColumn<books, Integer> colId;
    @FXML private TableColumn<books, String> colTitle, colAuthor, colCategory;
    @FXML private TableColumn<books, Integer> colCopies;
    @FXML private Button btnClose;

    // ----------------- Variables -----------------
    private String studentId;

    // ----------------- Setter for Student ID -----------------
    public void setStudentId(String id) {
        this.studentId = id;
        System.out.println("Student ID passed to AvailableBooksController: " + id);
        loadAvailableBooks(); // refresh table whenever student ID is set
    }

    // ----------------- Initialization -----------------
    @FXML
    private void initialize() {
        // Set up TableView columns
        colId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colCopies.setCellValueFactory(new PropertyValueFactory<>("copiesAvailable"));
    }

    // ----------------- Load Available Books -----------------
    private void loadAvailableBooks() {
        ObservableList<books> booksList = FXCollections.observableArrayList();

        // Query to get only available books (copies > 0)
        String query = """
                SELECT * FROM books 
                WHERE copies_available > 0
                """;

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

    // ----------------- Close Button Handler -----------------
    @FXML
    private void handleClose() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}
