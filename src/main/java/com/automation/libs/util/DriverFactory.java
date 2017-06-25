package com.automation.libs.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverFactory {
	
	public WebDriver driver;
	ConfigReader configReader = ConfigReader.getConfigReader();
	
	public WebDriver getDriver(String browser) throws MalformedURLException{
		
		//capture the value passed from jenkins
		boolean isRemoteExecution = "true".equalsIgnoreCase(System.getProperty("remote.execution", "false")); 
		
		//capture the value specified in the config.properties file when jenkins is not used
		if(!isRemoteExecution){
		   isRemoteExecution = Boolean.parseBoolean(configReader.getData("remote.execution"));
		}
		
		//creating the local/remote driver
		if(!isRemoteExecution){
			driver = localDriver(browser);
		}else{
			driver = remoteDriver(browser);
		}
		
		driver.manage().window().maximize();
		
		return driver;
	}
	
	private WebDriver localDriver(String browser){
		
		switch(browser.toUpperCase()){
			case "CHROME":
				 System.setProperty("webdriver.chrome.driver", configReader.getData("chrome.driver.path"));
				 driver = new ChromeDriver();
				 break;
			case "FIREFOX":
			case "FF":
				 System.setProperty("webdriver.gecko.driver", configReader.getData("ff.driver.path"));
				 driver = new FirefoxDriver();
				 break;
			default:
				throw new WebDriverException("Unrecognized browser: "+ browser);
		}
		
		return driver;
	}
	
	private WebDriver remoteDriver(String browser) throws MalformedURLException{
		
		DesiredCapabilities dc = new DesiredCapabilities();
		
		switch(browser.toUpperCase()){
			case "CHROME":
				 dc.setBrowserName("chrome");
				 break;
			case "FIREFOX":
			case "FF":
				 dc.setBrowserName("firefox");
				 break;
			default:
				throw new WebDriverException("Unrecognized browser: "+ browser);
		}
		
		driver = new RemoteWebDriver(new URL(configReader.getData("selenium.hub.url")), dc);
		
		return driver;
	}
}
