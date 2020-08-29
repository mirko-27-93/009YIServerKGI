package programranje.yatospace.server.config.engine.impl;

import java.io.File;

import programranje.yatospace.server.config.engine.GeneralTextConfigurationResourceEngine;

/**
 * Конфигурације које говоре како се треба звати 
 * коријенски администратор базе података. 
 * @author MV
 * @version 1.0
 */
public class DatabaseRootConfigEngine extends GeneralTextConfigurationResourceEngine{
	
	public DatabaseRootConfigEngine(String dir) {
		super(new File(dir, "root.database.admistrator.word.txt"), "/programranje/yatospace/server/config/files/root.database.admistrator.word.txt");
	}
	
}
