package it.betacom.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class App {

    private static Connection connectToDB() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "root");
            try (Statement stm = con.createStatement()) {
                createDb(stm);
                System.out.println("Database creato correttamente");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    private static void createDb(Statement stm) throws SQLException {
        try {
            stm.executeUpdate("DROP SCHEMA IF EXISTS academyExtraction");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        stm.executeUpdate("CREATE SCHEMA academyExtraction");
    }

    private static void createTableStudents(Statement stm) throws SQLException {
        stm.executeUpdate("CREATE TABLE studenti (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(50) NOT NULL, sede VARCHAR(50) NOT NULL)");
    }

    private static void createTableExtraction(Statement stm) throws SQLException {
        stm.executeUpdate("CREATE TABLE estrazione (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(50) NOT NULL, sede VARCHAR(50) NOT NULL, momento_estrazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
    }

    public static void main(String[] args) {
        try (Connection con = connectToDB()) {
            if (con != null) {
                try (Statement stm = con.createStatement()) {
                    stm.executeUpdate("USE academyExtraction");
                    createTableStudents(stm);
                    createTableExtraction(stm);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

