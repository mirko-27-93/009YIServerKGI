package programranje.yatospace.server.config.engine;

import java.io.File;
import java.util.Scanner;

/**
 * Општи оквир и класа за очитавање линија тескста из текстуалне датотеке, 
 * ради конфигурација, а са средствима за резерве, ако не постоји, и ауто-
 * матско генерисање датотетке, у овом случају.
 * @author MV
 * @version 1.0
 */
public class GeneralLinesConfigurationResourceEngine extends GeneralLinesConfigurationFileEngine{
	private String resource; 
	
	public  GeneralLinesConfigurationResourceEngine(File file, String resource) {
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
			loadFromFile();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	} 
	
	public void loadFromResource() {
		lines.clear(); 
		try(Scanner scanner = new Scanner(getClass().getResourceAsStream(resource),"UTF-8")){
			while(scanner.hasNextLine()) 
				lines.add(scanner.nextLine());  
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
