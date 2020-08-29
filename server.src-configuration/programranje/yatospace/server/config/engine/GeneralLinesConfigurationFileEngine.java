package programranje.yatospace.server.config.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import programranje.yatospace.server.config.controller.GeneralInitializerInterface;

/**
 * Општи оквир за читање линија из текстуалног (конфигурационог) фајла. 
 * @author MV
 * @version 1.0
 */
public class GeneralLinesConfigurationFileEngine implements GeneralInitializerInterface{
	private File location; 
	protected List<String> lines = new ArrayList<>(); 
	
	public GeneralLinesConfigurationFileEngine(File file){
		if(file == null) throw new NullPointerException("CONFIG FILE DECLARING");
		this.location = file; 
	}
	
	public File getLocation() {
		return location;
	}

	public void setLocation(File location) {
		this.location = location;
	}
	
	public synchronized List<String> getLines() {
		return new ArrayList<>(lines);
	}

	public synchronized void setLines(List<String> lines) {
		this.lines = new ArrayList<>(lines);
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
	
	public void resetLines() {
		lines.clear();
	}
	
	public void loadFromFile() {
		lines.clear(); 
		try(Scanner scanner = new Scanner(location, "UTF-8")){
			while(scanner.hasNextLine()) 
				lines.add(scanner.nextLine());  
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public void storeToFile() {
		try(PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(location), "UTF-8"))){
			for(String line: lines)
				pw.println(line);
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public void loadFromString(String string) {
		if(string == null) string = ""; 
		lines.clear(); 
		try(Scanner scanner = new Scanner(string)){
			while(scanner.hasNextLine()) 
				lines.add(scanner.nextLine());  
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public String storeToString() {
		String result = ""; 
		for(int i=0; i<lines.size(); i++) {
			if(i==lines.size()-1) result+=lines.get(i); 
			else result+=lines.get(i)+"\n"; 
		}
		return result; 
	}
}
