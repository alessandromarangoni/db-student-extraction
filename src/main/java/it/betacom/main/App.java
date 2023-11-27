package it.betacom.main;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;


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

    private static void createTableStudenti(Statement stm) throws SQLException {
        stm.executeUpdate("CREATE TABLE studenti (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(50) NOT NULL, sede VARCHAR(50) NOT NULL)");
    }

    private static void createTableEstrazioni(Statement stm) throws SQLException {
        stm.executeUpdate("CREATE TABLE estrazioni (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(50) NOT NULL, sede VARCHAR(50) NOT NULL, momento_estrazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
    }
    
    private static void popolatabellaStudenti(Statement stm, String nome, String sede) throws SQLException {
    	stm.executeUpdate("INSERT INTO studenti(nome, sede) VALUES('" + nome + "','" + sede + "')");
    }
    
    private static void CsvReader(Statement stm) throws IOException, SQLException{
    	String path= "./esercizioPartecipanti.CSV";
    	try (Reader reader = Files.newBufferedReader(Paths.get(path));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(';'))) {
               for (CSVRecord csvRecord : csvParser) {
                   // Ad esempio, se il tuo CSV ha due colonne 'Nome' e 'Città'
                   String nome = csvRecord.get(0); // O usa il nome della colonna se definito
                   String sede = csvRecord.get(1);
                   System.out.println("Nome: "  + nome + " || " + "sede: " + sede);
                   popolatabellaStudenti(stm, nome,sede);
               }
           }
    }
    
    private static int getUltimoRecord(Statement stm) throws SQLException {
		String query = "SELECT * FROM studenti ORDER BY id DESC LIMIT 1";
		 ResultSet rs = stm.executeQuery(query);
		 if (rs.next()) {
		        int id = rs.getInt("id");
		        System.out.println("ID: " + id);
		        return id;
		    }
		return 0;
    }

    public static void main(String[] args) {
    	
        try (Connection con = connectToDB()) {
            if (con != null) {
                try (Statement stm = con.createStatement()) {
                    stm.executeUpdate("USE academyExtraction");
                    createTableStudenti(stm);
                    createTableEstrazioni(stm);
                    try {
                    	//prendo dati da csv e popolo db
						CsvReader(stm);
						//prendo l id dell ultimo record 
						int Ultimorecord = getUltimoRecord(stm);
						int randomNum = (int)(Math.random() * Ultimorecord) + 1;
						System.out.println(randomNum);
					} catch (IOException e) {
						e.printStackTrace();
					}
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
}

