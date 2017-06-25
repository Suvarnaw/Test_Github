package com.automation.libs.common;

import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.automation.libs.util.ConfigReader;
import com.automation.libs.util.ObjectRepoReader;
import com.automation.libs.util.Reporter;
import com.automation.libs.util.Util;

public class Common {
	
	WebDriver driver;
	Reporter reporter;
	WebElement element;

	public Common(WebDriver wdriver, Reporter reporterObject) {
		
		this.driver = wdriver;
		this.reporter = reporterObject;
	}
	
	private String getDataFromObjectRepoReader(String key){
		
		ObjectRepoReader repoReader = ObjectRepoReader.getRepoReader();
		String propertyValue = repoReader.getData(key);
		
		return propertyValue;
	}
	
	public WebElement getElement(String objectName){
		
		String objectProperty = getDataFromObjectRepoReader(objectName);
		
		String[] arrObjectProperty = objectProperty.split(";");
		String locatorMechanism = arrObjectProperty[0].toUpperCase();
		String propertyValue = arrObjectProperty[1];
		
		WebElement element = null;
		
		try{
			switch(locatorMechanism){
				case "ID":
					element = Util.wait(driver).until(ExpectedConditions.visibilityOfElementLocated(By.id(propertyValue)));
					break;
				case "NAME":
					element = driver.findElement(By.name(propertyValue));
					break;
				case "CLASS":
				case "CLASSNAME":
					element = Util.wait(driver).until(ExpectedConditions.visibilityOfElementLocated(By.className(propertyValue)));
					break;
				case "LINKTEXT":
					element = driver.findElement(By.linkText(propertyValue));
					break;
				case "PARTIALLINKTEXT":
					element = driver.findElement(By.partialLinkText(propertyValue));
					break;
				case "XPATH":
					element = Util.wait(driver).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(propertyValue)));
					break;
				case "CSS":
				case "CSSSELECTOR":
					element = driver.findElement(By.cssSelector(propertyValue));
					break;				
			}
		}catch(NoSuchElementException nsee){
			reporter.report("fail", "No unique element is found for the locator mechanism '" + locatorMechanism + "' with <br> property as '" + propertyValue + "'", "", nsee);
		}
		catch(TimeoutException toe){
			reporter.report("fail", "Timeout in finding the locator mechanism '" + locatorMechanism + "' with <br> property as '" + propertyValue + "'", "", toe);
		}
		
		return element;
	}
	
	public List<WebElement> getElements(String objectName){
		
		String objectProperty = getDataFromObjectRepoReader(objectName);
		
		String[] arrObjectProperty = objectProperty.split(";");
		String locatorMechanism = arrObjectProperty[0].toUpperCase();
		String propertyValue = arrObjectProperty[1];
		
		List<WebElement> elements = null;
		
		switch(locatorMechanism){
			case "ID":
				elements = driver.findElements(By.id(propertyValue));
				break;
			case "NAME":
				elements = driver.findElements(By.name(propertyValue));
				break;
			case "CLASS":
			case "CLASSNAME":
				elements = Util.wait(driver).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className(propertyValue)));
				break;
			case "LINKTEXT":
				elements = driver.findElements(By.linkText(propertyValue));
				break;
			case "PARTIALLINKTEXT":
				elements = driver.findElements(By.partialLinkText(propertyValue));
				break;
			case "XPATH":
				elements = Util.wait(driver).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(propertyValue)));
				break;
			case "CSS":
			case "CSSSELECTOR":
				elements = driver.findElements(By.cssSelector(propertyValue));
				break;
	    }
		
		return elements;
	}
	
	public void getUrl(){
		ConfigReader configReader = ConfigReader.getConfigReader();
		String appUrl = configReader.getData("app.url");
		
		getUrl(appUrl);
	}
	
	public void getUrl(String url){
		
	   try{
		   driver.get(url);
		   Util.wait(driver).until(ExpectedConditions.urlToBe(url));		
		   
		   reporter.report("pass", "Navigate URL - " + url, "URL is navigated successfully");
	   }catch(WebDriverException wde){
		   reporter.report("fail", "Navigate URL - " + url, "Failed to navigate to URL", wde);
	   }
	}
	
	public void enterText(String elementName, String text, String fieldName){
		
		try{
			element = getElement(elementName);
			element.clear();
			element.sendKeys(text);
			
			reporter.report("pass", "Enter '" + fieldName + "' text as '" + text + "'", "Text is entered");
		}catch(WebDriverException | NoSuchElementException e){
			reporter.report("fail", "Enter '" + fieldName + "' text as '" + text + "'", "Failed to enter text", e);
		}
	}
	
	public void enterText(WebElement element, String text, String fieldName){
		
		try{
			element.clear();
			element.sendKeys(text);			
			reporter.report("pass", "Enter '" + fieldName + "' text as '" + text + "'", "Text is entered");
		}catch(WebDriverException | NoSuchElementException e){
			reporter.report("fail", "Enter '" + fieldName + "' text as '" + text + "'", "Failed to enter text", e);
		}
	}
	
	public void clickElement(String elementName, String fieldName){
		
		try{
			boolean isElementClicked = false;
			long time = System.currentTimeMillis();
			long currentTime = time;
			while(!isElementClicked && (currentTime - time) <= 1000 * Util.MAX_TIMEOUT){
				try{
					element = getElement(elementName);
					Util.wait(driver).until(ExpectedConditions.elementToBeClickable(element));
					element.click();
					isElementClicked = true;
				}catch(StaleElementReferenceException sere){
					
				}
				
				currentTime = System.currentTimeMillis();
			}
			
			if(!isElementClicked){
				reporter.report("fail", "Click '" + fieldName + "' element", "Element click failed");
			}
			
			reporter.report("pass", "Click '" + fieldName + "' element", "Element is clicked");
		}catch(WebDriverException | NoSuchElementException e){
			reporter.report("fail", "Click '" + fieldName + "' element", "Element click failed", e);
		}
	}
	
	public void clickElement(WebElement element, String fieldName){
		
		try{
			boolean isElementClicked = false;
			long time = System.currentTimeMillis();
			long currentTime = time;
			while(!isElementClicked && (currentTime - time) <= 1000 * Util.MAX_TIMEOUT){
				try{
					Util.wait(driver).until(ExpectedConditions.elementToBeClickable(element));
					element.click();
					isElementClicked = true;
				}catch(StaleElementReferenceException sere){
					
				}
				
				currentTime = System.currentTimeMillis();
			}
			
			if(!isElementClicked){
				reporter.report("fail", "Click '" + fieldName + "' element", "Element click failed");
			}
			
			reporter.report("pass", "Click '" + fieldName + "' element", "Element is clicked");
		}catch(WebDriverException | NoSuchElementException e){
			reporter.report("fail", "Click '" + fieldName + "' element", "Element click failed", e);
		}
	}
	
	public String getElementText(String elementName){
		
		element = getElement(elementName);
		String elementText = element.getText().trim();
		
		return elementText;
	}
	
	public String getElementText(WebElement elementName){
		
		String elementText = element.getText().trim();
		
		return elementText;
	}
	
	public void isElementDisplayed(String elementName, String fieldName){
		try{
			element = getElement(elementName);
			boolean result = element.isDisplayed();
			
			if(result){
				reporter.report("pass", "Verify '" + fieldName + "' element is displayed", "Element is displayed");
			}else{
				reporter.report("fail", "Verify '" + fieldName + "' element is displayed", "Element is not displayed");
			}
		}catch(WebDriverException | NoSuchElementException e){
			reporter.report("fail", "Verify '" + fieldName + "' element is displayed", "Element is not displayed", e);
		}
	}
	
	public void isElementDisplayed(WebElement element, String fieldName){
		try{
			boolean result = element.isDisplayed();
			
			if(result){
				reporter.report("pass", "Verify '" + fieldName + "' element is displayed", "Element is displayed");
			}else{
				reporter.report("fail", "Verify '" + fieldName + "' element is displayed", "Element is not displayed");
			}
		}catch(WebDriverException | NoSuchElementException e){
			reporter.report("fail", "Verify '" + fieldName + "' element is displayed", "Element is not displayed", e);
		}
	}
}