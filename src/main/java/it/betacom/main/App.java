package it.betacom.main;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
	
	private static List<Student> listaStudenti = new ArrayList<Student>();
	private static final Logger logger = LogManager.getLogger(App.class);

	private static Connection connectToDB(){
		Connection con = null;
		DB db = DB.getInstance();
		try {
			con = DriverManager.getConnection(db.getConnection(), db.getUser(), db.getPassword());
			logger.info("connesso correttamente");
		} catch (SQLException e1) {
			logger.error("non connesso ricontrollare dati utente e porta ");
		}
		try (Statement stm = con.createStatement()) {
			createDb(stm);
			logger.info("Database creato correttamente");
		} catch (SQLException e) {
			logger.error("Database Non creato");
		}
		return con;
	}

    private static void createDb(Statement stm) throws SQLException {
        try {
            stm.executeUpdate("DROP SCHEMA IF EXISTS academyExtraction");
        } catch (SQLException e) {
        	logger.info("Database droppato");
        }
        stm.executeUpdate("CREATE SCHEMA academyExtraction");
    }

    private static void createTableStudenti(Statement stm) throws SQLException {
        stm.executeUpdate("CREATE TABLE studenti (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(50) NOT NULL, sede VARCHAR(50) NOT NULL)");
        logger.info("Tabella studenti creata correttamente");
    }

    private static void createTableEstrazioni(Statement stm) throws SQLException {
       stm.executeUpdate("CREATE TABLE estrazioni (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(50) NOT NULL, sede VARCHAR(50) NOT NULL, momento_estrazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
       logger.info("Tabella studenti creata correttamente");
    }
    
    private static void popolatabellaStudenti(Statement stm, String nome, String sede) throws SQLException {
    	stm.executeUpdate("INSERT INTO studenti(nome, sede) VALUES('" + nome + "','" + sede + "')");
    	logger.info("insert avvenuta con successo");
    }
    
    private static void CsvReader(Statement stm) throws IOException, SQLException{
    	String path= "./esercizioPartecipanti.CSV";
    	try (Reader reader = Files.newBufferedReader(Paths.get(path));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(';'))) {
               for (CSVRecord csvRecord : csvParser) {
                   // Ad esempio, se il tuo CSV ha due colonne 'Nome' e 'Città'
                   String nome = csvRecord.get(0); // O usa il nome della colonna se definito
                   String sede = csvRecord.get(1);
                   popolatabellaStudenti(stm, nome,sede);
               }
           }
    }
    
    private static int getLastRecord(Statement stm) throws SQLException {
		String query = "SELECT * FROM studenti ORDER BY id DESC LIMIT 1";
		 ResultSet rs = stm.executeQuery(query);
		 if (rs.next()) {
		        int id = rs.getInt("id");
		        return id;
		    }
		return 0;
    }
    
    private static void doExtractionQuery(Statement stm, int randomNum, List<Student> listaStudenti) throws SQLException {
        String queryselect = "SELECT nome, sede FROM studenti WHERE id = " + randomNum;
        ResultSet rs = stm.executeQuery(queryselect);
        if (rs.next()) {
            String nome = rs.getString("nome");
            String sede = rs.getString("sede");
            String queryinsert = "INSERT INTO estrazioni (nome, sede) VALUES('" + nome + "','" + sede + "')";
            boolean found = false;
            for (Student student : listaStudenti) {
                if (student.getNome().equals(nome) && student.getSede().equals(sede)) {
                    student.incrementExtractionCount();
                    found = true;
                    break;
                }
            }
            if (!found) {
                listaStudenti.add(new Student(nome, sede, LocalDateTime.now()));
            }
            stm.executeUpdate(queryinsert);
            System.out.println("----------------");
            System.out.println("nome: " + nome );
            System.out.println( "sede: " + sede);
        }
    }
    
    private static void generaPDF(List<Student> listaStudenti) {
    	
    	listaStudenti.sort((s1, s2) -> Integer.compare(s2.getExtractionCount(), s1.getExtractionCount()));

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("ReportEstrazioni.pdf"));
            document.open();
            Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, BaseColor.BLACK);
            for (Student student : listaStudenti) {
                Paragraph p = new Paragraph("Nome: " + student.getNome() + " Sede: " + student.getSede() + " Data ultima estrazione: " + student.getDataEstrazione() + " Estratto: " + student.getExtractionCount() + " volte", font);
                document.add(p);
            }
            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
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
						int Ultimorecord = getLastRecord(stm);
						Scanner scanner = new Scanner(System.in);
						//faccio estrazione per n volte chiesto
						System.out.println("quante estrazioni facciamo?");
						int numeroExtraction = scanner.nextInt();
						for(int i = 0; i < numeroExtraction; i++) {
							int randomNum = 0;
							randomNum = (int)(Math.random() * Ultimorecord) + 1;
							doExtractionQuery(stm, randomNum,listaStudenti );
							generaPDF(listaStudenti);
						}
						
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

