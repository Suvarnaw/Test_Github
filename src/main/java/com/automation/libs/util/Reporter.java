package com.automation.libs.util;

import static org.testng.Assert.fail;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Reporter {
	
	private ExtentTest logger;
	private WebDriver driver;
	private Exception exception = null;
	
	public Reporter(WebDriver driver, ExtentTest logger){
		this.driver = driver;
		this.logger = logger;
	}
	
	public void report(String status, String stepName, String details){
		
		switch(status.toUpperCase()){
			case "PASS":
			   ConfigReader configReader = ConfigReader.getConfigReader();
			   String screenshotForEveryStep = configReader.getData("capture.screenshot.per.step");
			   
			   if(Boolean.parseBoolean(screenshotForEveryStep)){
				   details += logger.addScreenCapture(ScreenshotGenerator.getScreenshot(driver));
			   }
			   
			   logger.log(LogStatus.PASS, stepName, details);
			   break;
			case "FAIL":
				details += logger.addScreenCapture(ScreenshotGenerator.getScreenshot(driver));
				logger.log(LogStatus.FAIL, stepName, details);
				
				if(exception!=null){
					String trace = "";					
					StringWriter errors = new StringWriter();
					exception.printStackTrace(new PrintWriter(errors));
					trace = errors.toString();
					
					logger.log(LogStatus.INFO, "Exception details <br><br>" + trace);
				}
				
				fail();
		}		
	}
	
	public void report(String status, String stepName, String details, Exception e){
		this.exception = e;
		report(status, stepName, details);
	}
}
