package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    // Update these details to match your MySQL setup
    private static final String URL = "jdbc:mysql://localhost:3306/school_db";
    private static final String USER = "root";        // your MySQL username
    private static final String PASSWORD = "Williamson@26";        // your MySQL password (if any)

    private static Connection connection;

    // Method to establish a connection
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Database connected successfully!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error connecting to database: " + e.getMessage());
        }
        return connection;
    }

    // Optional: Close connection when done
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔒 Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error closing connection: " + e.getMessage());
        }
    }
}
