package com.automation.libs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
	
	public static final ConfigReader configReader = new ConfigReader();
	private Properties configProperties = new Properties();
	
	public ConfigReader(){		
		FileInputStream configFile = null;
		try {
			configFile = new FileInputStream("properties" + File.separator + "config.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			configProperties.load(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public static ConfigReader getConfigReader(){
		return configReader;
	}
	
	public String getData(String key){
		String data = null;
		
		if(configProperties.containsKey(key)){
			data = configProperties.getProperty(key);
		}else{
			try {
				throw new Exception("'" + key + "' data is not found in the config properties file");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return data;
	}
}
