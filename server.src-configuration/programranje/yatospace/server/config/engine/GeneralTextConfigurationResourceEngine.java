package programranje.yatospace.server.config.engine;

import java.io.File;
import java.util.Scanner;

/**
 * Опште очитавање текстуалних фајлова (за конфигурације), са резервним средстивима 
 * и аутоматским (ре)генерисањем ако је ово непостојеће.
 * @author MV
 * @version 1.0
 */
public class GeneralTextConfigurationResourceEngine extends GeneralTextConfigurationFileEngine{
	private String resource; 
	
	public  GeneralTextConfigurationResourceEngine(File file, String resource) {
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
		String text = ""; 
		try(Scanner scanner = new Scanner(getClass().getResourceAsStream(resource), "UTF-8")){
			while(scanner.hasNextLine()) 
				text += scanner.nextLine()+"\n";  
			text = ("."+text).trim().substring(1);
			setText(text);
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
