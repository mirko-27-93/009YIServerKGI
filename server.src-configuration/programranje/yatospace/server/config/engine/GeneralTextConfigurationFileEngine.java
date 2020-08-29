package programranje.yatospace.server.config.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

import programranje.yatospace.server.config.controller.GeneralInitializerInterface;

/**
 * Општи оквир за текстуално учитавање (текстуалног) фајла (при конфигурисању). 
 * @author MV
 * @version 1.0
 */
public class GeneralTextConfigurationFileEngine implements GeneralInitializerInterface{
	private File location; 
	private String text = ""; 
	
	public GeneralTextConfigurationFileEngine(File file){
		if(file == null) throw new NullPointerException("CONFIG FILE DECLARING");
		this.location = file; 
	}
	
	@Override
	public void initalize() {
		try {
			if(!location.exists()) location.createNewFile(); 
			loadFromFile();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public File getLocation() {
		return location;
	}

	public void setLocation(File location) {
		this.location = location;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if(text==null) text = ""; 
		this.text = text;
	}
	
	public void loadFromFile() {
		String text = ""; 
		try(Scanner scanner = new Scanner(location, "UTF-8")){
			while(scanner.hasNextLine()) 
				text += scanner.nextLine()+"\n";  
			text = ("."+text).trim().substring(1);
			this.text = text; 
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public void storeToFile() {
		try(PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(location), "UTF-8"))){
			pw.print(text);
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
