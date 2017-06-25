package com.automation.libs.util;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Util {
	
	public static int MAX_TIMEOUT = 30;
	
	public static WebDriverWait wait(WebDriver driver){
		WebDriverWait standardWait = new WebDriverWait(driver, MAX_TIMEOUT); 
		return standardWait;
	}
	
	public static WebDriverWait wait(WebDriver driver, int timeInSeconds){
		WebDriverWait customWait = new WebDriverWait(driver, timeInSeconds);
		return customWait;
	}
	
	public static void deleteDirectory(File directory){

		  if(directory.isDirectory()) {
			  File[] files=directory.listFiles();	
	          for(int i=0;i<files.length;i++){	
				deleteDirectory(files[i]);	
			  }
			  
	          if(directory.exists()){
					directory.delete();
			  }
		  }
		  else{
			if(directory.exists()){
				directory.delete();
			}
		  }
	}
}
