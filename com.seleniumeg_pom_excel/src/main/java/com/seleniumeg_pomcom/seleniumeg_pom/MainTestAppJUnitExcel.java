package com.seleniumeg_pomcom.seleniumeg_pom;
import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.util.stream.Stream;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.excel.utils.ExcelReadUtils;
import com.excel.utils.ExcelWriteUtils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class MainTestAppJUnitExcel {
	private  static WebDriver driver;
	private static WebDriverWait wait;
		
	@BeforeAll
	public static void setUp() {
		//set driver property
		//load driver
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\Administrator\\Downloads\\chromedriver-win32\\chromedriver.exe");
		
		//create an instance of chrome driver
		driver = new ChromeDriver();
		
		//wait
		wait =new WebDriverWait(driver,Duration.ofSeconds(10));
		
		ExcelReadUtils.init();
		ExcelWriteUtils.init();
		
	}
//testcases
	//Home page to About page
	@Disabled
	@Test
	public void testNavigateHomeAbout() {
		//Open the HTML page (replace with actual URL)
		driver.get("File:///C:\\Users\\Administrator\\eclipse-workspace\\com.seleniumeg_pom\\src\\main\\resources\\Home.html");
		
		//create HomePage
		HomePage homePage = new HomePage(driver);

		//navigate to About page
		AboutPage aboutPage = homePage.gotoAboutPage();	
		
		//wait until About page is loaded
		wait.until(ExpectedConditions.titleContains("About"));
		
		//assert with page title
		assertTrue(driver.getTitle().contains("About"));

	}
	
	//Home Page to Contact Page
	@Disabled
	@Test
	public void testNavigateHomeContact() {
		//Open the HTML page (replace with actual URL)
		driver.get("File:///C:\\Users\\Administrator\\eclipse-workspace\\com.seleniumeg_pom\\src\\main\\resources\\Home.html");
		
		//create HomePage
		HomePage homePage = new HomePage(driver);

		//navigate to Contact page
		ContactPage contactPage = homePage.gotoContactPage();	
		
		//wait until About page is loaded
		wait.until(ExpectedConditions.titleContains("Contact"));
		
		//assert with page title
		assertTrue(driver.getTitle().contains("Contact"));

	}
	
	//Test home to about, further to Contact page
	@Disabled
	@Test
	public void testNavigateHomeAboutContact() {
		//Open the HTML page (replace with actual URL)
		driver.get("File:///C:\\Users\\Administrator\\eclipse-workspace\\com.seleniumeg_pom\\src\\main\\resources\\Home.html");
		
		//create HomePage
		HomePage homePage = new HomePage(driver);

		//navigate from Home to About page
		AboutPage aboutPage = homePage.gotoAboutPage();	
		
		//wait until About page is loaded
		wait.until(ExpectedConditions.titleContains("About"));
		
		//assert with page title
		assertTrue(driver.getTitle().contains("About"));
		
		//navigate from About to Contact page
		ContactPage contactPage = aboutPage.gotoContactPage();	
				
		//wait until About page is loaded
		wait.until(ExpectedConditions.titleContains("Contact"));
				
		//assert with page title
		assertTrue(driver.getTitle().contains("Contact"));

	}
	
	static Stream<Arguments> getContactFormData() {
		//invoke ExcelReadUtils method
		Stream<Arguments> contactdata = ExcelReadUtils.readContactFormData();
		return contactdata;
	}
	
	@ParameterizedTest
	@MethodSource("getContactFormData")
	public void CheckContactForm(String tcid,String name,String email,String details, String sucessmsg) throws Throwable{
		try {
		//Open the HTML page (replace with actual URL)
		driver.get("File:///C:\\Users\\Administrator\\eclipse-workspace\\com.seleniumeg_pom\\src\\main\\resources\\Home.html");
		
		//create HomePage
		HomePage homePage = new HomePage(driver);

		ContactPage contactPage = homePage.gotoContactPage();
		//wait until About page is loaded
		wait.until(ExpectedConditions.titleContains("Contact"));
				
		//assert with page title
		assertTrue(driver.getTitle().contains("Contact"));

		contactPage.fillContactForm(name,email,details);
		Thread.sleep(2000);
		String message= contactPage.checkSubmission();
		assertTrue(message.contains(sucessmsg));//Mail Sent Successfully
		ExcelWriteUtils.writeTCResult(tcid, "PASSED", "");
		}catch(AssertionError ae) {
			//testcase failed
			ExcelWriteUtils.writeTCResult(tcid, "FAILED", ae.getMessage()+getStackTrace(ae));
			ae.printStackTrace();
			throw ae;
		
		}catch(Exception e) {
			ExcelWriteUtils.writeTCResult(tcid, "ERROR", e.getMessage()+getStackTrace(e));
			//testcase error
			e.printStackTrace();
			throw e;
		}

	}
	
	public static String getStackTrace(AssertionError e) {
	    StringWriter sw = new StringWriter();
	    PrintWriter pw = new PrintWriter(sw);
	    e.printStackTrace(pw);
	    return sw.toString();
	}
	
	public static String getStackTrace(Exception e) {
	    StringWriter sw = new StringWriter();
	    PrintWriter pw = new PrintWriter(sw);
	    e.printStackTrace(pw);
	    return sw.toString();
	}
	
	@AfterAll
	public static void tearDown() {
		ExcelWriteUtils.generateExcel();
		if(driver !=null) {
			driver.quit();
		}
	}

}
