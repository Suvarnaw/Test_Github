package com.automation.libs.base;

import com.automation.libs.common.Common;
import com.automation.libs.util.ConfigReader;
import com.automation.libs.util.DriverFactory;
import com.automation.libs.util.ExcelUtil;
import com.automation.libs.util.Reporter;
import com.relevantcodes.extentreports.ExtentTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public abstract class TestCaseInitializer extends ReportInitializer{
	
	  public String testCaseName;
	  public ExtentTest logger;
	  public WebDriver driver;
	  public Reporter reporter;
	  public Common common;
	  
	  @BeforeMethod
	  @Parameters("browser")
	  public void beforeMethod(@Optional("") String browser) throws MalformedURLException{
		  if(browser.isEmpty()){
			  ConfigReader configReader = ConfigReader.getConfigReader();
			  browser = configReader.getData("default.browser");
		  }
		  
		  logger = report.startTest(testCaseName);
		  DriverFactory driverFactory = new DriverFactory();
		  driver = driverFactory.getDriver(browser);
		  
		  reporter = new Reporter(driver, logger);
		  
		  common = new Common(driver, reporter);
		  reporter.report("pass", "Launch browser - " + browser, "Browser is launched");		  
	  }
	  
	  @DataProvider(name="testdata")
	  public Object[][] testData() throws FileNotFoundException, IOException{
		  String testDataFilePath = System.getProperty("user.dir") + File.separator + "testdata" + File.separator + "TestData.xls";
		  ExcelUtil excelUtil = new ExcelUtil(testDataFilePath, "TestData", testCaseName);
		  
		  int testDataStartRow = excelUtil.getSearchTextFirstRowNum();
		  int testDataEndRow = excelUtil.getSearchTextLastRowNum();
		  
		  Map<String,String> testDataMap;
		  int rows = 0;
		  int columns = 0;
		  for(int i=testDataStartRow;i <=testDataEndRow;i++){
			  testDataMap = excelUtil.getDataFromExcel(i);
			  columns = testDataMap.size() - 2;
			  
			  if(testDataMap.get("Execute?").equals("Y")){
					rows++;  
			  }
		  }
		  
		  Object[][] arrTestdata = new Object[rows][columns];		  
		  for(int i = 0, iterator=testDataStartRow;iterator <= testDataEndRow;iterator++){
			  testDataMap = excelUtil.getDataFromExcel(iterator);
			  
			  if(!testDataMap.get("Execute?").equals("Y")){
				continue;  
			  }
			  
			  Iterator<String> valuesIterator = testDataMap.values().iterator();
			  valuesIterator.next();
			  valuesIterator.next();
			  
			  int j = 0;
			  while(valuesIterator.hasNext()){
				  
				  try{
					  arrTestdata[i][j] = valuesIterator.next().toString();
					  j++;
				  }
				  catch(Exception e){
					  break;
				  }
			  }
			  
			  i++;
		  }
		  
		  return arrTestdata;
	  }
	
	  @AfterMethod
	  public void afterMethod() {
		  if(driver!=null){
			  driver.quit();
		  }
		  report.endTest(logger);
		  report.flush();
	  }
}
