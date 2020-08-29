package programranje.yatospace.server.basic.database;

import java.io.Serializable;

/**
 * Сесије које се вежу за базе података. 
 * @author MV
 * @version 1.0
 */
public class DatabaseSession implements Serializable{
	private static final long serialVersionUID = 1L;
	private String dbUsername = ""; 
	private boolean dbAdmin = false; 
	private boolean dbRoot = false; 
	
	public String getUsername() {
		return dbUsername; 
	}
	
	public boolean isLogged() {
		return dbUsername!=null && dbUsername.trim().length()!=0;
	}
	
	public void login(String username) {
		if(username==null) username = ""; 
		dbUsername = username;
	}
	
	public void logout() {
		dbUsername = "";
	}
	
	public boolean isDBAdmin() {
		return dbAdmin; 
	}
	
	public boolean isDBRoot() {
		return dbRoot; 
	}
	
	public void setDBAdmin() {
		dbAdmin = true; 
	}
	
	public void setDBRoot() {
		dbRoot = true;
	}
	
	public void resetDBAdmin() {
		dbAdmin = false; 
	}
	
	public void resetDBRoot() {
		dbRoot = false;
	}
}
