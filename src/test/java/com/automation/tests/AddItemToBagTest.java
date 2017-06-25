package com.automation.tests;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.automation.libs.base.TestCaseInitializer;
import com.automation.libs.page.HomePage;
import com.automation.libs.page.ProductDetailsPage;
import com.automation.libs.page.SearchResultsPage;

public class AddItemToBagTest extends TestCaseInitializer{
	
	@BeforeTest
	public void setTestName(){
		testCaseName = "Add Item to Bag";
	}

	
	@Test(dataProvider = "testdata")
	public void addItemToBag(String searchText) {
		
		common.getUrl();
		
		//search text 
		HomePage homePageObj = new HomePage(driver, common, reporter);
		homePageObj.enterSearchText(searchText);
		homePageObj.pressSearchButton();
		
		//verify search results are displayed
		SearchResultsPage searchResultsPageObj = new SearchResultsPage(driver, common, reporter);
		searchResultsPageObj.validateSearchResultsCount();
		
		//select first item
		searchResultsPageObj.selectFirstItem();
		
		//Add item to shopping bag
		ProductDetailsPage pdpObj = new ProductDetailsPage(driver, common, reporter);
		pdpObj.addItemtoBag();
		
	}

}
