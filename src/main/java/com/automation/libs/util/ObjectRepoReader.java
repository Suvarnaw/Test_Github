package com.automation.libs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ObjectRepoReader {
	
	public static final ObjectRepoReader objectRepoReader = new ObjectRepoReader();
	private Properties orProperties = new Properties();
	
	ObjectRepoReader(){		
		FileInputStream configFile = null;
		try {
			configFile = new FileInputStream("properties" + File.separator + "or.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			orProperties.load(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public static ObjectRepoReader getRepoReader(){
		return objectRepoReader;
	}
	
	public String getData(String key){
		String data = null;
		
		if(orProperties.containsKey(key)){
			data = orProperties.getProperty(key);
		}else{
			try {
				throw new Exception("'" + key + "' data is not found in the or.properties file");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return data;
	}
}
