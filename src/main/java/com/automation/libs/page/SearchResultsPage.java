package com.automation.libs.page;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.automation.libs.common.Common;
import com.automation.libs.util.Reporter;

public class SearchResultsPage {
	
	WebDriver driver;
	Common common;
	Reporter reporter;
	
	List<WebElement> searchResults;
	
	public SearchResultsPage(WebDriver wd, Common commonObj, Reporter reporterObj){
		driver = wd;
		common = commonObj;
		reporter = reporterObj;
		PageFactory.initElements(driver, this);
	}
	
	public void validateSearchResultsCount(){
		WebDriverWait wait = new WebDriverWait(driver, 20);
		searchResults = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("s-item-container")));
		
		if(searchResults.size() > 0){
			reporter.report("pass", "Verify search results are displayed", "Search results are displayed with count as " + searchResults.size());
		}else{
			reporter.report("fail", "Verify search results are displayed", "Search results are not displayed");
		}
	}
	
	public void selectFirstItem(){
		common.clickElement(searchResults.get(0), "First Item");
	}
}
