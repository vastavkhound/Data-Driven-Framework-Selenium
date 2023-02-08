package testcases;

import java.io.IOException;

import org.dom4j.DocumentException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import dataprovider.DataProviderClass;

public class AddCustomerTest extends TestBase {
	
	@Test (dataProviderClass = DataProviderClass.class,dataProvider = "dataprovider")
	public void addCustomer(String firstname, String lastname, Double postcode,String alert, String runmodeData) throws WebDriverException, IOException, DocumentException{
		logger.info("addCustomer test started");
		String runmodeTest=runmodeTest("addCustomer");
		if(runmodeTest.equals("no")) {
			((ExtentTest) context.getAttribute("ExtentTest")).log(LogStatus.SKIP, "Test skipped");
			logger.info("test skipped");
			throw new SkipException("Skipped");
		}
		else if(runmodeData.equals("no")) {
			((ExtentTest) context.getAttribute("ExtentTest")).log(LogStatus.SKIP, "Test skipped for dataset");
			throw new SkipException("Test skipped for dataset");
		}
		
		int noOfSheets=workbook.getNumberOfSheets();
		for(int i=0;i<noOfSheets;i++) {
			if(workbook.getSheetAt(i).getSheetName().equals("addCustomer")) {
				sheet=workbook.getSheet("addCustomer");
			}
		}

		try {
			click("Bank_Manager_Login_Button");
		} catch (Exception e) {
		}
		for (int i = 0; i < driver
				.findElements(By.cssSelector(objectRepositoryProperties.getProperty("bankManagerPageButtons")))
				.size(); i++) {
			if (driver.findElements(By.cssSelector(objectRepositoryProperties.getProperty("bankManagerPageButtons")))
					.get(i).getAttribute("innerHTML").contains("Add Customer")) {
				String info="Add_Customer_Button";
				driver.findElements(By.cssSelector(objectRepositoryProperties.getProperty("bankManagerPageButtons")))
						.get(i).click();
				((ExtentTest) context.getAttribute("ExtentTest")).log(LogStatus.INFO, "Click on "+info);
				Reporter.log("Click on "+info);
				break;
			}
		}
		//verifyEquals("abc", "xyz");
		type("firstname", firstname);
		type("lastname", lastname);
		
		type("postcode", Double.toString(postcode).replaceAll(".0", ""));
		click("Add_Customer_Button");
		String s = driver.switchTo().alert().getText();
		if (s.contains(alert)) {
			Assert.assertTrue(true);
			Reporter.log("test passed");
			logger.info("test passed");
		} else {
			Assert.assertTrue(false);
			Reporter.log("test failed");
			logger.info("test failed");
		}
		driver.switchTo().alert().accept();
		Assert.fail("failed to add customer");
	}

}
