package listeners;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;

public class CustomListeners extends TestBase implements ITestListener{

	
	
	@Override
	public void onTestFailure(ITestResult result) {
		String path=null;
			try {
				path=screenshot(result.getMethod().getMethodName(),(WebDriver)result.getTestContext().getAttribute("WebDriver"));
			
			} catch (WebDriverException | IOException e) {
				e.printStackTrace();
			}
		
		Reporter.log("<a href="+path+">Screenshot</a>");
		Reporter.log("<a href="+path+"><img src="+path+" height=100 width=100></img></a>");
		extentTest.log(LogStatus.FAIL, result.getTestClass().getRealClass().getName()+" "+result.getMethod().getMethodName()+result.getThrowable().toString());
		extentTest.log(LogStatus.FAIL,extentTest.addScreenCapture(path));
	}
	
	@Override
	public void onTestSuccess(ITestResult result) {
		extentTest.log(LogStatus.PASS, result.getName()+" Passed");
	}
	

	@Override
	public void onTestStart(ITestResult result) {
		extentTest=extentReport.startTest(result.getTestClass().getRealClass().getName());
		result.getTestContext().setAttribute("ExtentTest", extentTest);
		result.getTestContext().setAttribute("Method", result.getMethod().getMethodName());
	}
	
	@Override
	public void onFinish(ITestContext context) {
		extentReport.endTest(extentTest);
		extentReport.flush();
	}
	
}
