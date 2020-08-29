package programranje.yatospace.server.config.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Општи оквир за очитавање из фајла са резервним средствима, као и генерисање 
 * фајлова ако их нема, кад су у питању конфигурације својстава. 
 * @author MV
 * @version 1.0
 */
public class GeneralPropertyConfigurationResourceEngine extends GeneralPropertyConfigurationFileEngine{
	private String resource; 
	
	public GeneralPropertyConfigurationResourceEngine(File file, String resource) {
		super(file);
		if(resource == null) throw new RuntimeException();
		this.resource = resource; 
	}
	
	public String getResource() {
		return resource;
	}

	@Override
	public void initalize() {
		try {
			if(!getLocation().exists()) {
				getLocation().createNewFile();
				loadFromResource();
				storeToFile();
			}
			getContent().clear();
			getContent().load(new FileInputStream(getLocation()));
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	} 
	
	public void loadFromResource() {
		try {
			InputStream is = getClass().getResourceAsStream(resource);
			getContent().clear();
			getContent().load(is);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
