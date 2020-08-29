package programranje.yatospace.server.config.engine.impl;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;
import programranje.yatospace.server.config.engine.GeneralLinesConfigurationResourceEngine;

/**
 * Контрола и конфигурације у вези са регистром 
 * адреса коријенских корисника и база података 
 * које корисник види и оних у серверу које носе
 * и шифру, а које су регистроване на серверу, у 
 * конфигурацијама, а ради кориштења у апликацији. 
 * @author MV
 * @version 1.0
 */
public class DatabaseAddressRegisterMapConfigEngine  extends GeneralLinesConfigurationResourceEngine{

	public DatabaseAddressRegisterMapConfigEngine(String dir) {
		super(new File(dir, "database.register.map.list.txt"), "/programranje/yatospace/server/config/files/database.register.map.list.txt");
	}
	
	@Override
	public void initalize() {
		super.initalize();
	}
	
	public HashMap<String, String> nonPasswordDatabaseAddressesMap(){
		try {
			HashMap<String, String> map = new HashMap<>(); 
			for(String dbAddress: getLines()) {
				URI uri = new URI(dbAddress);
				String  user = uri.getUserInfo().split(":")[0];
				String userDBAddress = UriBuilder.fromUri(uri).userInfo(user).toString();
				map.put(dbAddress, userDBAddress);
			}
			return map;
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public HashMap<String, String> shellDatabaseAddressMap(){
		try {
			HashMap<String, String> result = new HashMap<>();
			for(Map.Entry<String, String> me: nonPasswordDatabaseAddressesMap().entrySet()) {
				URI uri = new URI(me.getValue());
				String shellDBAddress = UriBuilder.fromUri(uri).replacePath("").toString();
				result.put(me.getKey(), shellDBAddress);
			}
			return result; 
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public String realYIDatabaseAddress(String userYIDatabaseAddress) {
		HashMap<String, String> map = nonPasswordDatabaseAddressesMap();
		for(Map.Entry<String, String> me: map.entrySet()) {
			if(me.getValue().contentEquals(userYIDatabaseAddress)) 
				return me.getKey(); 
		}
		return null;
	}
	
	public String shellYIDatabaseAddress(String userYIDatabaseAddress) {
		HashMap<String, String> map = shellDatabaseAddressMap();
		for(Map.Entry<String, String> me: map.entrySet()) {
			if(me.getKey().contentEquals(realYIDatabaseAddress(userYIDatabaseAddress))) 
				return me.getValue(); 
		}
		return null;
	}
	
	public HashMap<String, String> realYIDatabaseAddressPartMap(String userYIDatabaseAddress){
		try {
			String map = realYIDatabaseAddress(userYIDatabaseAddress);
			if(map==null) return null;
			HashMap<String, String> partMap = new HashMap<>();
			URI uri = new URI(map);
			partMap.put("protocol", uri.getScheme());
			partMap.put("username", uri.getUserInfo().split(":")[0]);
			partMap.put("password", uri.getUserInfo().split(":")[1]);
			partMap.put("hostname", uri.getHost());
			partMap.put("portnumb", Integer.toString(uri.getPort()));
			partMap.put("pathline", uri.getPath());
			partMap.put("database", (uri.getPath()==null||uri.getPath().length()==0)?"":uri.getPath().substring(1));
			return partMap; 
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
