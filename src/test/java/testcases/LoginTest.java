package testcases;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;

public class LoginTest extends TestBase {

	public Logger loggerApplication = Logger.getLogger(TestBase.class.getName());

	@Test
	public void managerLogin() throws IOException, DocumentException {
		logger.info("managerLogin test started");
		String runmode=runmodeTest("managerLogin");
		if(runmode.equals("no")) {
			((ExtentTest) context.getAttribute("ExtentTest")).log(LogStatus.SKIP, "Test skipped");
			logger.info("test skipped");
			throw new SkipException("Method skipped");
		}
		click("Bank_Manager_Login_Button");
		Assert.assertFalse(driver.findElements(By.cssSelector(objectRepositoryProperties.getProperty("bankManagerPageButtons"))).isEmpty(),"login failed");
		logger.info("managerLogin test completed");
		Reporter.log("test passed");
		//Assert.fail("failed to login");
	}

}
