package base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.DocumentException;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import listeners.ExtentReportGenerator;


public class TestBase {
	
	public WebDriver driver;
	Properties configProperties=new Properties();
	public Properties objectRepositoryProperties=new Properties();
	FileInputStream fileInputStream;
	File file;
	public XSSFWorkbook workbook;
	public  XSSFSheet sheet;
	public WebDriverWait wait;
	public Date date;
	public ExtentTest extentTest;
	public ExtentReports extentReport=ExtentReportGenerator.reportInitializer();
	public ITestContext context;
	SoftAssert softAssert=new SoftAssert();
	public String dateString;
	public static Hashtable<String,String> tableForEachRow;
	public Logger logger=Logger.getLogger("DataDrivenSuite");
	public XMLReader xmlReader=new XMLReader("src\\test\\resources\\properties\\ObjectRepository.xml");
	public JSONReader jsonReader=new JSONReader("src\\test\\resources\\properties\\ObjectRepository.json");
	
	@BeforeTest
	@Parameters( value = {"browser"})
	public void setUp(@Optional("edge") String browser) throws IOException {
		PropertyConfigurator.configure("src\\test\\resources\\logs\\log4j.properties");
		if(driver==null) {
			file=new File(System.getProperty("user.dir")+"\\src\\test\\resources\\properties\\Config.properties");
			fileInputStream=new FileInputStream(file);
			configProperties.load(fileInputStream);
			
			file=new File(System.getProperty("user.dir")+"\\src\\test\\resources\\properties\\ObjectRepository.properties");
			fileInputStream=new FileInputStream(file);
			objectRepositoryProperties.load(fileInputStream);
					
			if(browser==null) {
				System.setProperty("webdriver.edge.driver", System.getProperty("user.dir")+"\\src\\test\\resources\\executables\\msedgedriver.exe");
				driver=new EdgeDriver();
			}
			
			else if(browser.equals("chrome")) {
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\src\\test\\resources\\executables\\chromedriver.exe");
				driver=new ChromeDriver();
			}
			
			else if(browser.equals("edge")) {
				System.setProperty("webdriver.edge.driver", System.getProperty("user.dir")+"\\src\\test\\resources\\executables\\msedgedriver.exe");
				driver=new EdgeDriver();
				//((RemoteWebDriver) driver).setLogLevel(Level.INFO);
			} 
			logger.info("browser opened");
			
			driver.manage().window().maximize();
			driver.get(configProperties.getProperty("url"));
			logger.info("url opened");
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Long.parseLong((String) configProperties.get("implicitWait"))));
			wait=new WebDriverWait(driver,Duration.ofSeconds(5));
		}
		
	}
	
	@BeforeTest(dependsOnMethods = "setUp")
	public void excelFileSetup() throws IOException {
		file=new File(System.getProperty("user.dir")+"\\src\\test\\resources\\excel\\testdata.xlsx");
		fileInputStream=new FileInputStream(file);
		workbook=new XSSFWorkbook(fileInputStream);
	}
	
	@BeforeTest(dependsOnMethods = "excelFileSetup")
	public void context(ITestContext context) {
		context.setAttribute("WebDriver", driver);
		this.context=context;
	}
	
	@AfterTest
	public void tearDown() {
		LogEntries logEntries= driver.manage().logs().get(LogType.BROWSER);
		List<LogEntry> les=logEntries.getAll();
		for(LogEntry le:les) {
			logger.info(le.toString());
		}
		if(driver!=null) {
			driver.quit();
		}
		logger.info("test completed");
	}
	
	public void readFromXML(String locatorName) throws DocumentException {
		if(locatorName.contains("xpath")) {
			driver.findElement(By.xpath(xmlReader.getLocator(locatorName))).click();
		}
		else if(locatorName.contains("css")) {
			driver.findElement(By.cssSelector(xmlReader.getLocator(locatorName))).click();
		}
		else {
			driver.findElement(By.id(xmlReader.getLocator(locatorName))).click();
		}
		
	}
	
	public void readFromJson(String locatorName) throws IOException {
		if(locatorName.contains("xpath")) {
		driver.findElement(By.xpath(jsonReader.getLocator(locatorName))).click();
		}
	}
	
	public void click(String locatorName) throws DocumentException, IOException {
		
		if(locatorName.contains("xml")) {
			readFromXML(locatorName);
			((ExtentTest) context.getAttribute("ExtentTest")).log(LogStatus.INFO, "Click on "+locatorName.split("xml.locators.")[1].split(".xpath")[0]);
			Reporter.log("Click on "+locatorName);
		}
		else if(locatorName.contains("json")) {
			readFromJson(locatorName);
			((ExtentTest) context.getAttribute("ExtentTest")).log(LogStatus.INFO, "Click on "+locatorName.split("json.locators.")[1].split(".xpath")[0]);
			Reporter.log("Click on "+locatorName);
		}
		else {
			driver.findElement(By.cssSelector(objectRepositoryProperties.getProperty(locatorName))).click();
			((ExtentTest) context.getAttribute("ExtentTest")).log(LogStatus.INFO, "Click on "+locatorName);
			Reporter.log("Click on "+locatorName);
		}
		
		
	}
	
	public void  type(String locatorName,String value) {		
		driver.findElement(By.cssSelector(objectRepositoryProperties.getProperty(locatorName))).sendKeys(value);
		((ExtentTest) context.getAttribute("ExtentTest")).log(LogStatus.INFO, "Enter "+"< "+value+" >"+" in "+locatorName);
		Reporter.log("Enter "+"< "+value+" >"+" in "+locatorName);
	}
	
	public String screenshot(String method,WebDriver driver) throws WebDriverException, IOException {
		date=new Date();
		dateString=date.toString().replace(" ", "_").replace(":", "_");
		String path=System.getProperty("user.dir")+"\\test-output\\screenshots\\"+method+"\\"+dateString+".jpg";
		FileUtils.copyFile(((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE),new File(path));
		return path;
	}
	
	public void verifyEquals(String actual,String expected) throws WebDriverException, IOException{
		
		try {
			softAssert.assertEquals(actual, expected);
			softAssert.assertAll();
		} catch (Throwable t) {
			String path=screenshot((String) context.getAttribute("Method"),driver);
			((ExtentTest) context.getAttribute("ExtentTest")).log(LogStatus.FAIL, ((ExtentTest) context.getAttribute("ExtentTest")).addScreenCapture(path));
		}		
	}
	
	public String runmodeTest(String method) throws IOException {
		excelFileSetup();		
		String value=null;
		int noOfSheets=workbook.getNumberOfSheets();
		for(int i=0;i<noOfSheets;i++) {
			if(workbook.getSheetAt(i).getSheetName().equals("runmode")) {
				sheet=workbook.getSheet("runmode");
			}
		}
		int count =sheet.getLastRowNum()-sheet.getFirstRowNum();
		for(int i=1;i<=count;i++) {
			Hashtable<String,String> tableForEachRow=new Hashtable<>();
			for(int j=0;j<sheet.getRow(i).getLastCellNum();j++) {
				tableForEachRow.put(sheet.getRow(0).getCell(j).getStringCellValue(), sheet.getRow(i).getCell(j).getStringCellValue());			
			}
			if(tableForEachRow.containsValue(method)) {
				value=tableForEachRow.get("runmode");
			}
		}		
		return value;
	}
}
