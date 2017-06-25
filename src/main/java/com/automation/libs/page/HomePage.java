package com.automation.libs.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.automation.libs.common.Common;
import com.automation.libs.util.Reporter;

public class HomePage {
	
	WebDriver driver;
	Common common;
	Reporter reporter;
	
	@FindBy(id = "twotabsearchtextbox")
	private WebElement searchTextBox;
	
	@FindBy(xpath = "//span[@id='nav-search-submit-text']")
	private WebElement searchButton;
	
	public HomePage(WebDriver wd, Common commonObj, Reporter reporterObj){
		driver = wd;		
		common = commonObj;
		reporter = reporterObj;
		PageFactory.initElements(driver, this);
	}
	
	public void enterSearchText(String searchText){
		common.enterText(searchTextBox, searchText, "Search Text Box");
	}
	
	public void pressSearchButton(){
		common.clickElement(searchButton, "Search Button");
	}
}
