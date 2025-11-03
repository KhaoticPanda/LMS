package application;

import java.sql.Connection;

public class Test_connection {
    public static void main(String[] args) {
        Connection conn = Database.getConnection();
        if (conn != null) {
            System.out.println("Connection test successful!");
        } else {
            System.out.println("Connection failed!");
        }
    }
}
