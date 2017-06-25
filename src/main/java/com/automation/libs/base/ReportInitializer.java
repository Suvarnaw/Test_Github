package com.automation.libs.base;

import org.testng.annotations.BeforeSuite;

import com.automation.libs.util.Util;
import com.relevantcodes.extentreports.ExtentReports;

import java.io.File;

import org.testng.annotations.AfterSuite;

public abstract class ReportInitializer {
	
	  public static ExtentReports report;
 
	  @BeforeSuite
	  public void beforeSuite() {
		  Util.deleteDirectory(new File(System.getProperty("user.dir") + File.separator + "reports"));
		  report=new ExtentReports("reports" + File.separator + "ExecutionReport.html");
		  report.loadConfig(new File("extent-config.xml"));
	  }
	
	  @AfterSuite
	  public void afterSuite() {
		  report.close();
	  }
}
