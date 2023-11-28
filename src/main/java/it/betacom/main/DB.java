package it.betacom.main;

public class DB {
	private String connection;
	private String user;
	private String password;
	private static DB instance;
	
	
	private DB() {
		try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
        }
		this.connection = "jdbc:mysql://localhost:3306/";
		this.user = "root";
		this.password = "root"; 
	}
	
    public static DB getInstance() {
        if (instance == null){
            instance = new DB();
        }
        return instance;
    }

	public String getConnection() {
		return connection;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
