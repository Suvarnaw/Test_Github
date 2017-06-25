package com.automation.libs.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.automation.libs.common.Common;
import com.automation.libs.util.Reporter;
import com.automation.libs.util.Util;

public class ProductDetailsPage {
	
	@FindBy(id = "add-to-cart-button")
	private WebElement addtoCartButton;
		
	WebDriver driver;
	Common common;
	Reporter reporter;
	
	public ProductDetailsPage(WebDriver wd, Common commonObj, Reporter reporterObj){
		driver = wd;
		common = commonObj;
		reporter = reporterObj;
		
		addtoCartButton = Util.wait(driver).until(ExpectedConditions.visibilityOfElementLocated(By.id("add-to-cart-button")));
		
		PageFactory.initElements(driver, this);
	}
	
	public void addItemtoBag(){
		common.clickElement(addtoCartButton, "Add to Cart");

		Util.wait(driver, 30).until(ExpectedConditions.textToBePresentInElementLocated(By.id("nav-cart-count"), "1"));
	}
	
}
