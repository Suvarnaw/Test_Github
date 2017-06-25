package com.automation.libs.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotGenerator {
	
	private static String screenshotPath = System.getProperty("user.dir") + File.separator + "reports" + File.separator + "screenshots" + File.separator;
	private static File screenshotFile;	
	
	private static void takeScreenshot(WebDriver driver){
		File src= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		screenshotPath = screenshotPath + System.currentTimeMillis() + ".png";
		screenshotFile = new File(screenshotPath);
		
		try {
			  FileUtils.copyFile(src, screenshotFile);
        }catch (IOException e){
        	System.out.println(e.getMessage());
	    }
	}
	
	public static String getScreenshot(WebDriver driver){		
		takeScreenshot(driver);
		return screenshotPath;
	}
	
}
