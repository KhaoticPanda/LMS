package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// Import your model class
import model.books;

public class AvailableBooksController {

    @FXML
    private TableView<books> tableBooks;

    @FXML
    private TableColumn<books, Integer> colId;

    @FXML
    private TableColumn<books, String> colTitle, colAuthor, colCategory;

    @FXML
    private TableColumn<books, Integer> colCopies;

    @FXML
    private Button btnClose;

    // ✅ Variable to receive student ID from dashboard
    private String studentId;

    // ✅ Setter to accept student ID
    public void setStudentId(String id) {
        this.studentId = id;
        System.out.println("Student ID passed to AvailableBooksController: " + id);
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

    private void loadAvailableBooks() {
        ObservableList<books> booksList = FXCollections.observableArrayList();

        String query = "SELECT * FROM books WHERE status='Available'";

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

    @FXML
    private void handleClose() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}
