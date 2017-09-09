package com.automation.tests;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.automation.libs.base.TestCaseInitializer;
import com.automation.libs.page.HomePage;
import com.automation.libs.page.SearchResultsPage;

public class AmazonSearchFunctionalityTest extends TestCaseInitializer{
	
	@BeforeTest
	public void setTestName(){
		testCaseName = "Search Functionality";
	}

	@Test(dataProvider = "testdata")
	public void searchTest(String searchText) {
		
		common.getUrl();
				
		HomePage homePageObj = new HomePage(driver, common, reporter);
		homePageObj.enterSearchText(searchText);
		homePageObj.pressSearchButton();
		
		SearchResultsPage searchResultsPageObj = new SearchResultsPage(driver,  common, reporter);
		searchResultsPageObj.validateSearchResultsCount();
		
	}

}
