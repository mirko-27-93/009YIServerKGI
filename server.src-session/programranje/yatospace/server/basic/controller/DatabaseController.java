package programranje.yatospace.server.basic.controller;

import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import programiranje.yi.database.MySQLUserDAO;
import programiranje.yi.user.app.services.model.DatabaseUserInformation;
import programranje.yatospace.server.basic.database.DatabaseSession;
import programranje.yatospace.server.basic.model.SessionInfo;
import programranje.yatospace.server.config.controller.GeneralConfigController;
import studiranje.ip.data.BasicConnectionPool;
import studiranje.ip.data.DBRootDAO;
import studiranje.ip.data.DBUserDAO;
import studiranje.ip.data.RootConnectionPool;

/**
 * Контролни објекат када је су у питању контролне операције са базом података. 
 * @author MV
 * @version 1.0
 */
public class DatabaseController {
	private SessionController sessionController;
	private DatabaseSession databaseSession = new DatabaseSession();
	private RootConnectionPool generalDBConnector; 
	private RootConnectionPool generalUserDBConnector; 
	private RootConnectionPool userRootDBConnector;
	private BasicConnectionPool userDBConnector;
	private DBRootDAO dbRootDAO; 
	private DBUserDAO dbUserDAO; 
	private MySQLUserDAO mysqlUserDAO; 
	private DBUserDAO userDAO; 
	
	public SessionController getSessionController() {
		return sessionController;
	}

	public void setSessionController(SessionController sessionController) {
		this.sessionController = sessionController;
	} 
	
	public boolean existsDBUser() {
		return sessionController.getDatabaseConfigurationAdapter().getAllInfo().isDbUserExists();
	}
	
	public boolean registredDBUser() {
		return sessionController.getDatabaseConfigurationAdapter().getAllInfo().isDbInfoExists();
	}
	
	public boolean isDeclaredDBUser() {
		DatabaseUserInformation dui = sessionController.getDatabaseConfigurationAdapter().getAllInfo(); 
		return dui.isDbInfoExists() && dui.isDbUserExists();
	}
	
	public boolean isRootDBUser(String username) {
		if(username==null || username.trim().length()==0) return false; 
		return GeneralConfigController.mainAppConfigEngine.databaseRoot.getText().contentEquals(username);
	}
	
	public boolean isAdminDBUser(String username) {
		if(username==null || username.trim().length()==0) return false; 
		return GeneralConfigController.mainAppConfigEngine.databaseAdmins.getLines().contains(username);
	}
	
	public RootConnectionPool getGeneralDBConnector() {
		return generalDBConnector;
	}
	
	public RootConnectionPool getGeneralUserDBConnector() {
		return generalUserDBConnector;
	}
	
	public BasicConnectionPool getUserDBConnector() {
		return this.userDBConnector; 
	}
	
	public DatabaseSession getDatabaseSession() {
		return databaseSession; 
	}
	
	public void loginAsDBUserOnly(String username, String password, String databaseName, String databaseAddress) {
		String dbRootUser     = GeneralConfigController.mainAppConfigEngine.databaseRParam.getDatabaseUser();
		String dbRootPassword = GeneralConfigController.mainAppConfigEngine.databaseRParam.getDatabasePassword(); 
		
		if(dbRootUser==null && dbRootPassword==null) return;
		
		String declaredDatabase = databaseAddress; 
		String definedDatabase = GeneralConfigController.mainAppConfigEngine.databasesMap.realYIDatabaseAddress(declaredDatabase); 
		
		databaseSession.resetDBAdmin();
		databaseSession.resetDBRoot();
		
		HashMap<String, String> partsDBAddress = GeneralConfigController.mainAppConfigEngine.databasesMap.realYIDatabaseAddressPartMap(declaredDatabase);
		
		if(partsDBAddress==null)
			throw new RuntimeException();
		
		String rootDatabase = partsDBAddress.get("database"); 
		String rootUsername = partsDBAddress.get("username"); 
		String rootPassword = partsDBAddress.get("password");
		
		
		String declaredShellDatabase = GeneralConfigController.mainAppConfigEngine.databasesMap.shellYIDatabaseAddress(declaredDatabase); 
		String definedRootDatabase =  UriBuilder.fromUri(declaredShellDatabase).userInfo(rootUsername+":"+rootPassword).build().toString();
		String definedUserDatabase =  UriBuilder.fromUri(declaredDatabase).userInfo(username+":"+password).build().toString();
		
		List<String> admins = GeneralConfigController.mainAppConfigEngine.databaseAdmins.getLines();
		if(dbRootUser.contentEquals(username)) {
			try {
				if(!admins.contains(dbRootUser)) 
					throw new RuntimeException();
				databaseSession.setDBRoot();
				databaseSession.setDBAdmin();
			}catch(Exception ex) {
				logout();
				databaseSession.resetDBRoot();
				databaseSession.resetDBAdmin();
				throw ex;
			}
		}
		if(admins.contains(username)) databaseSession.setDBAdmin();
		if(!databaseSession.isDBAdmin() && !databaseSession.isDBRoot()) 
			throw new RuntimeException();
		
		try {
			generalDBConnector	   =  RootConnectionPool.isolatedConnectionPool(definedRootDatabase);
			generalUserDBConnector =  RootConnectionPool.isolatedConnectionPool(definedDatabase);
			
			dbRootDAO = new DBRootDAO(generalDBConnector);
			
			if(!dbRootDAO.getDatabases().contains(rootDatabase))
				throw new RuntimeException();
			
			userDBConnector = BasicConnectionPool.isolatedConnectionPool(definedUserDatabase, rootDatabase);
			userDAO = new DBUserDAO(userDBConnector); 
			
			userRootDBConnector = RootConnectionPool.isolatedConnectionPool(definedUserDatabase);
			userRootDBConnector.checkIn(userRootDBConnector.checkOut());
			
			databaseSession.login(username);
		}catch(RuntimeException ex) {
			logout();
			databaseSession.resetDBRoot();
			databaseSession.resetDBAdmin();
			throw ex;
		}catch(Exception ex) {
			logout();
			databaseSession.resetDBRoot();
			databaseSession.resetDBAdmin();
			throw new RuntimeException(ex);
		}
	}
	
	public void login(SessionInfo undpParameters, String password) {
		String dbRootUser     = GeneralConfigController.mainAppConfigEngine.databaseRParam.getDatabaseUser();
		String dbRootPassword = GeneralConfigController.mainAppConfigEngine.databaseRParam.getDatabasePassword(); 
		
		if(dbRootUser==null && dbRootPassword==null) return;
		
		String declaredDatabase = undpParameters.getDataBase(); 
		String userDatabase = sessionController.getDatabaseConfigurationAdapter().getAllInfo().getDatabase(); 	
		String definedDatabase = GeneralConfigController.mainAppConfigEngine.databasesMap.realYIDatabaseAddress(declaredDatabase); 
		
		databaseSession.resetDBAdmin();
		databaseSession.resetDBRoot();
		
		if(!definedDatabase.equals(userDatabase))
			throw new RuntimeException();
		
		HashMap<String, String> partsDBAddress = GeneralConfigController.mainAppConfigEngine.databasesMap.realYIDatabaseAddressPartMap(declaredDatabase);
		
		if(partsDBAddress==null)
			throw new RuntimeException();
		
		String rootDatabase = partsDBAddress.get("database"); 
		String rootUsername = partsDBAddress.get("username"); 
		String rootPassword = partsDBAddress.get("password");
		
		String username =  undpParameters.getUserName();
		
		String declaredShellDatabase = GeneralConfigController.mainAppConfigEngine.databasesMap.shellYIDatabaseAddress(declaredDatabase); 
		String definedRootDatabase =  UriBuilder.fromUri(declaredShellDatabase).userInfo(rootUsername+":"+rootPassword).build().toString();
		String definedUserDatabase =  UriBuilder.fromUri(declaredDatabase).userInfo(username+":"+password).build().toString();
		
		try {
			generalDBConnector	   =  RootConnectionPool.isolatedConnectionPool(definedRootDatabase);
			generalUserDBConnector =  RootConnectionPool.isolatedConnectionPool(definedDatabase);
			
			dbRootDAO = new DBRootDAO(generalDBConnector);
			
			if(!dbRootDAO.getDatabases().contains(rootDatabase))
				throw new RuntimeException();

			if(!dbRootDAO.getTables(rootDatabase).contains("userdatabase")) 
				throw new RuntimeException();
		}catch(RuntimeException ex) {
			if(generalDBConnector!=null) {
				try{generalDBConnector.close();}catch(Exception exc) {exc.printStackTrace();}
				generalDBConnector = null;
			}
			if(generalUserDBConnector!=null) {
				try{generalUserDBConnector.close();}catch(Exception exc) {exc.printStackTrace();}
				generalUserDBConnector = null;
			}
			if(dbRootDAO!=null) 
				dbRootDAO = null;
			throw ex;
		}
		catch(Exception ex) {
			if(generalDBConnector!=null) {
				try{generalDBConnector.close();}catch(Exception exc) {exc.printStackTrace();}
				generalDBConnector = null;
			}
			if(generalUserDBConnector!=null) {
				try{generalUserDBConnector.close();}catch(Exception exc) {exc.printStackTrace();}
				generalUserDBConnector = null;
			}
			if(dbRootDAO!=null) 
				dbRootDAO = null;
			throw new RuntimeException(ex);
		}
		
		if(!isDeclaredDBUser()) throw new RuntimeException();
		
		dbUserDAO = new DBUserDAO(generalUserDBConnector);
		mysqlUserDAO = new MySQLUserDAO(generalUserDBConnector);
		try {
			if(!mysqlUserDAO.databaseManervarable()) throw new RuntimeException(); 
			userDBConnector = BasicConnectionPool.isolatedConnectionPool(definedUserDatabase, rootDatabase);
			userDAO = new DBUserDAO(userDBConnector); 
			userRootDBConnector = RootConnectionPool.isolatedConnectionPool(definedUserDatabase);
			userRootDBConnector.checkIn(userRootDBConnector.checkOut());
		}catch(RuntimeException ex) {
			if(generalDBConnector!=null) {
				try{generalDBConnector.close();}catch(Exception exc) {exc.printStackTrace();}
				generalDBConnector = null;
			}
			if(generalUserDBConnector!=null) {
				try{generalUserDBConnector.close();}catch(Exception exc) {exc.printStackTrace();}
				generalUserDBConnector = null;
			}
			if(userDBConnector!=null) {
				try{userDBConnector.close();}catch(Exception exc) {exc.printStackTrace();}
				generalUserDBConnector = null;
			}
			if(userRootDBConnector!=null) {
				try{userDBConnector.close();}catch(Exception exc) {exc.printStackTrace();}
				userRootDBConnector = null;
			}
			if(dbRootDAO!=null) 
				dbRootDAO = null;
			if(dbUserDAO!=null)
				dbUserDAO = null;
			if(mysqlUserDAO!=null)
				mysqlUserDAO = null;
			if(userDAO!=null)
				userDAO = null;
			throw ex;
		}catch(Exception ex) {
			if(generalDBConnector!=null) {
				try{generalDBConnector.close();}catch(Exception exc) {exc.printStackTrace();}
				generalDBConnector = null;
			}
			if(generalUserDBConnector!=null) {
				try{generalUserDBConnector.close();}catch(Exception exc) {exc.printStackTrace();}
				generalUserDBConnector = null;
			}
			if(userDBConnector!=null) {
				try{userDBConnector.close();}catch(Exception exc) {exc.printStackTrace();}
				userDBConnector = null;
			}
			if(userRootDBConnector!=null) {
				try{userDBConnector.close();}catch(Exception exc) {exc.printStackTrace();}
				userRootDBConnector = null;
			}
			if(dbRootDAO!=null) 
				dbRootDAO = null;
			if(dbUserDAO!=null)
				dbUserDAO = null;
			if(mysqlUserDAO!=null)
				mysqlUserDAO = null;
			if(userDAO!=null)
				userDAO = null;
			throw new RuntimeException(ex);
		}
		
		List<String> admins = GeneralConfigController.mainAppConfigEngine.databaseAdmins.getLines();
		databaseSession.login(undpParameters.getUserName());
		if(dbRootUser.contentEquals(undpParameters.getUserName())) {
			try {
				if(!admins.contains(dbRootUser)) 
					throw new RuntimeException();
				databaseSession.setDBRoot();
				databaseSession.setDBAdmin();
			}catch(Exception ex) {
				logout();
				databaseSession.resetDBRoot();
				databaseSession.resetDBAdmin();
				throw ex;
			}
		}
		if(admins.contains(undpParameters.getUserName())) databaseSession.setDBAdmin();
	}
	
	public void logout() {
		if(databaseSession.isLogged()) databaseSession.logout(); 
		if(generalDBConnector!=null) {
			try{generalDBConnector.close();}
			catch(Exception ex) {ex.printStackTrace();}
			finally{generalDBConnector = null;}
		}
		if(generalUserDBConnector!=null) {
			try{generalUserDBConnector.close();}
			catch(Exception ex) {ex.printStackTrace();}
			finally {generalUserDBConnector = null;}
		}
		if(userDBConnector!=null) {
			try{userDBConnector.close();}
			catch(Exception ex) {ex.printStackTrace();}
			finally {userDBConnector = null;}
		}
		if(userRootDBConnector!=null) {
			try{userRootDBConnector.close();}
			catch(Exception ex) {ex.printStackTrace();}
			finally {userRootDBConnector = null;}
		}
		
		if(dbRootDAO!=null) 
			dbRootDAO = null;
		if(dbUserDAO!=null)
			dbUserDAO = null;
		if(mysqlUserDAO!=null)
			mysqlUserDAO = null;
		if(userDAO!=null)
			userDAO = null;
		
		databaseSession.resetDBAdmin();
		databaseSession.resetDBRoot();
	}

	public RootConnectionPool getUserRootDBConnector() {
		return userRootDBConnector;
	}

	public DBRootDAO getDbRootDAO() {
		return dbRootDAO;
	}

	public DBUserDAO getDbUserDAO() {
		return dbUserDAO;
	}

	public MySQLUserDAO getMysqlUserDAO() {
		return mysqlUserDAO;
	}

	public DBUserDAO getUserDAO() {
		return userDAO;
	}
}
