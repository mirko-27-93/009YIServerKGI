package programranje.yatospace.server.config.controller;

import java.io.File;

import programranje.yatospace.server.config.engine.impl.DatabaseAddressRegisterMapConfigEngine;
import programranje.yatospace.server.config.engine.impl.DatabaseAdminsConfigEngine;
import programranje.yatospace.server.config.engine.impl.DatabaseRootConfigEngine;
import programranje.yatospace.server.config.engine.impl.DatabaseRootParameterConfigEngine;
import programranje.yatospace.server.config.engine.impl.ServicesConfigEngine;
import programranje.yatospace.server.config.engine.impl.SessionConfigEngine;
import programranje.yatospace.server.config.engine.impl.WebSocketConfigEngine;

/**
 * Опште мјесто за конфигурације. 
 * @author mirko
 * @version 1.0
 */
public class GeneralConfigController implements GeneralInitializerInterface, GeneralFinalizerInterface{
	public static final GeneralConfigController mainAppConfigEngine = new GeneralConfigController();
	
	public final static  String GENERAL_FILE_FOLDER_DIR = "C:\\Users\\MV\\Documents\\Eclipse\\eclipse-workspace-2\\009YIServerKGI"; 
	public final static  String GENERAL_CONFIGURATION_DIR = "C:\\Users\\MV\\Documents\\Eclipse\\eclipse-workspace-2\\009YIServerKGI\\configuration"; 
	public final static  String APPLICATION_STATE_DIR  = "C:\\Users\\MV\\Documents\\Eclipse\\eclipse-workspace-2\\009YIServerKGI\\application"; 
	
	public void initApplicationStateDir() {
		try {
			File file = new File(APPLICATION_STATE_DIR);
			if(!file.exists()) file.mkdirs();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void initGeneralConfigDir() {
		try {
			File file = new File(GENERAL_CONFIGURATION_DIR);
			if(!file.exists()) file.mkdirs();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private boolean initialized; 
	private boolean finalized; 
	
	@Override
	public void finish() {
		if(finalized) return; 
		finalized = true; 
	}

	@Override
	public void initalize() {
		if(initialized) return; 
		
		initGeneralConfigDir();
		initApplicationStateDir();
		
		session.initalize();
		services.initalize();
		webSockets.initalize();
		
		databaseRoot.initalize();
		databaseAdmins.initalize();
		databaseRParam.initalize();
		
		databasesMap.initalize();
		
		initialized = true; 
	} 
	
	public final SessionConfigEngine session = new SessionConfigEngine(GENERAL_CONFIGURATION_DIR);
	public final ServicesConfigEngine services = new ServicesConfigEngine(GENERAL_CONFIGURATION_DIR);
	public final WebSocketConfigEngine webSockets = new WebSocketConfigEngine(GENERAL_CONFIGURATION_DIR);
	
	public final DatabaseRootConfigEngine databaseRoot = new DatabaseRootConfigEngine(APPLICATION_STATE_DIR);
	public final DatabaseAdminsConfigEngine databaseAdmins = new DatabaseAdminsConfigEngine(APPLICATION_STATE_DIR);
	public final DatabaseRootParameterConfigEngine databaseRParam = new DatabaseRootParameterConfigEngine(GENERAL_CONFIGURATION_DIR); 
	
	public final DatabaseAddressRegisterMapConfigEngine databasesMap = new DatabaseAddressRegisterMapConfigEngine(APPLICATION_STATE_DIR);
	
	public boolean isInitialized() {
		return initialized;
	}

	public boolean isFinalized() {
		return finalized;
	}
}
