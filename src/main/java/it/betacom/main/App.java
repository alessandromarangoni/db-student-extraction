package it.betacom.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Hello world!
 *
 */
public class App 
{
	
	
	private static void connectToDB(Connection con, Statement stm) {
		//inizializzo connessione e statement 
	      
	      //connetto al driver 
	      try {
	    	  Class.forName("com.mysql.cj.jdbc.Driver");
	      }catch (ClassNotFoundException e1) {
	    	  e1.printStackTrace();
	      }
	      
	      try {
	    	  //connessione a localhost 
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "root");
			stm = con.createStatement();
//			ResultSet rs = stm.executeQuery("CREATE SCHEMA academyExtraction");
			System.out.println("database creato correttamente");
			stm.executeUpdate("USE academyExtraction");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	      finally {
	    	  if (con != null) {
	    		  	try {
						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		  }
	    	  }
	      	if(stm!= null) {
	      		try {
					stm.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	      	}
	      }
	private static void createDb(Statement stm) {
		try {
			stm.executeUpdate("DROP SCHEMA academyExtraction");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			stm.executeUpdate("CREATE SCHEMA academyExtraction");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void createTableStudents(Statement stm) {
		try {
			 stm.executeUpdate("CREATE TABLE studenti (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(50) NOT NULL, sede VARCHAR(50) NOT NULL )");
			 } catch (SQLException e) {
			System.out.println("IMPOSSIBILE CREARE TABELLA STUDENTS");
		}
	}
	
	private static void createTableExtraction(Statement stm) {
		try {
			 stm.executeUpdate("CREATE TABLE studenti (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(50) NOT NULL, sede VARCHAR(50) NOT NULL, momento_estrazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP, )");
			 } catch (SQLException e) {
			System.out.println("IMPOSSIBILE CREARE TABELLA STUDENTS");
		}
	}
	
	
	
    public static void main( String[] args )
    {
    	Connection con = null;
	    Statement stm = null;
    	
	    //connetto al db e lo uso ()
    	connectToDB(con , stm);
    	
    	//creo tabella studenti
		createTableStudents(stm);
		//creo tab estrazione
		createTableExtraction(stm);
    }
}
