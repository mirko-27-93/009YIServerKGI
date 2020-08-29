package programranje.yatospace.server.config.engine.impl;

import java.io.File;

import programranje.yatospace.server.config.engine.GeneralPropertyConfigurationResourceEngine;

/**
 * Коријенски параметри. 
 * @author MV
 * @version 1.0
 */
public class DatabaseRootParameterConfigEngine extends GeneralPropertyConfigurationResourceEngine{

	public DatabaseRootParameterConfigEngine(String dir) {
		super(new File(dir, "database.properties"), "/programranje/yatospace/server/config/resources/database.properties");
	}
	
	@Override
	public void initalize() {
		super.initalize();
	}
	
	public String getDatabaseUser() {
		return getContent().getProperty("database.root.user"); 
	}
	
	public String getDatabasePassword() {
		return getContent().getProperty("database.root.password"); 
	}
}
