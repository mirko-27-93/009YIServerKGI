package programranje.yatospace.server.config.engine.impl;

import java.io.File;

import programranje.yatospace.server.config.engine.GeneralLinesConfigurationResourceEngine;

/**
 * Конфигурације које се односе на очитавање дозвољених подразумијеваних 
 * администратора базе података који се могу на дивље пријавити, односно
 * без посједовања корисничких карактеристика (само су корисници БАП). 
 * @author MV
 * @version 1.0
 */
public class DatabaseAdminsConfigEngine extends GeneralLinesConfigurationResourceEngine{
	public DatabaseAdminsConfigEngine(String dir) {
		super(new File(dir, "database.administrator.users.list.txt"), "/programranje/yatospace/server/config/files/database.administrator.users.list.txt");
	}
}
