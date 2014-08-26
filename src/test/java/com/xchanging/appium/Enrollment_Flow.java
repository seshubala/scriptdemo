package com.xchanging.appium;

import static org.junit.Assert.assertEquals;
import io.appium.java_client.AndroidKeyCode;
import io.appium.java_client.AppiumDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.AfterStories;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.xchanging.util.ConfigurationManager;
import com.xchanging.util.ImportfromExcel;
import com.xchanging.util.JDBC;
import com.xchanging.util.Logging;



public class Enrollment_Flow {

	private AppiumDriver driver = null;
	public WebElement element = null;

	ImportfromExcel excel = new ImportfromExcel();

	Logger log = Logging.getLogger(getClass());
	
	private WebDriverWait wait = null; 
	
	
	
	@Given("I have launched the application")
	public void launch() {
		
		String fileName = System.getProperty("user.dir");
		
		File app = new File(fileName + "//APK_Files//"
				+ ConfigurationManager.getString("APK_FILE"));
		
		final DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "android");
		capabilities.setCapability(CapabilityType.VERSION,
				ConfigurationManager.getString("ANDROID_VER"));
		capabilities.setCapability(CapabilityType.PLATFORM,
				ConfigurationManager.getString("PLATFORM"));
		capabilities.setCapability("Device",
				ConfigurationManager.getString("DEVICE_NAME"));
		capabilities.setCapability("newCommandTimeout", "180");
		capabilities.setCapability("appPackage",
				ConfigurationManager.getString("APK_PACKAGE"));
		
		capabilities.setCapability("appActivity",
				ConfigurationManager.getString("APK_ACTIVITY"));
		
		capabilities.setCapability("App", app.getAbsolutePath());

		try {
			driver = new AppiumDriver(new URL(
					"http://172.27.34.175:4723/wd/hub"), capabilities);
			log.debug("Driver invoked successfully");
		} catch (final MalformedURLException e) {
			e.printStackTrace();
		}
		driver.manage().timeouts().implicitlyWait(80, TimeUnit.SECONDS);
		log.debug("Maximized and implicit wait is set to 80 seconds");
		
		wait = new WebDriverWait(driver, 80); 
		
	}

	
	@Given("I have accepted the terms and conditions and sms agreement")
	public void acceptTerms() throws Exception {

		
		
		Thread.sleep(10000);
		// Find and click on "Enroll in Mobile Service"
		
		WebElement Enroll = driver
				.findElementByClassName("android.widget.Button");
		assertEquals("Enroll in Mobile Service", Enroll.getText());
		Enroll = driver.findElement(By.name("Enroll in Mobile Service"));
		if (Enroll.isEnabled()) {
			log.info(Enroll.getText() + " is enabled");
			Enroll.click();
		} else {
			log.error(Enroll.getText() + " is disabled");
		}

		// Select "Accept Terms and Conditions"
		final List<WebElement> chk_accept = driver
				.findElementsByClassName("android.widget.CheckBox");
		for (final WebElement chk : chk_accept) {
			Thread.sleep(4000);
			chk.click();
		}
		log.info("Accepted Terms and Conditions");

		// Click on NEXT
		final WebElement NEXT = driver
				.findElementByClassName("android.widget.ImageButton");
		NEXT.click();
		log.info("Clicked on Next");

		Thread.sleep(4000);
	}
	
	@Given("I have entered <customer name>, <mobile number>, <email>, <passcode>, <confirm passcode>")
	public void enterPersonalInformation() throws Exception {

		final String DATA_NAME = excel.readFromExcel("TestData", 2, 1);
		final String DATA_MSISDN = excel.readFromExcel("TestAccounts", 2, 1);
		final String DATA_EMAIL = excel.readFromExcel("TestAccounts", 1, 1);
		final String DATA_PASSCODE = excel.readFromExcel("TestData", 1, 1);
		

		// ENTER PERSONAL DETAILS

		WebElement NAME = driver
				.findElementByClassName("android.widget.EditText");
		NAME = driver.findElement(By.name("John Smith"));
		NAME.click();
		NAME.sendKeys(DATA_NAME);
		log.info(DATA_NAME + "is entered for Name");

		Thread.sleep(1000);

		// MSISDN
		WebElement MSISDN = driver
				.findElementByClassName("android.widget.EditText");
		MSISDN = driver.findElement(By.name("(415) 123–4567"));
		MSISDN.click();
		MSISDN.sendKeys(DATA_MSISDN);
		log.info(DATA_MSISDN + "is entered for MSISDN");
		Thread.sleep(1000);

		final WebElement EMAIL = driver.findElement(By
				.name("john.smith@mail.com"));
		EMAIL.click();
		EMAIL.sendKeys(DATA_EMAIL);
		log.info(DATA_EMAIL + "is entered for Email");
		Thread.sleep(1000);
		driver.sendKeyEvent(AndroidKeyCode.ENTER);

		Thread.sleep(3000);
		
		
		final List<WebElement> PASSCODE = driver
				.findElementsByClassName("android.widget.EditText");
		PASSCODE.get(PASSCODE.size() - 1).sendKeys(DATA_PASSCODE);
		log.info(DATA_PASSCODE + "is entered for Passcode");
		Thread.sleep(3000);

		driver.sendKeyEvent(AndroidKeyCode.ENTER);

		Thread.sleep(3000);

		final List<WebElement> CONF_PASSCODE = driver
				.findElementsByClassName("android.widget.EditText");
		CONF_PASSCODE.get(CONF_PASSCODE.size() - 1).sendKeys(DATA_PASSCODE);
		log.info(DATA_PASSCODE + "is entered for Confirm Passcode");

		// Click on NEXT
				final WebElement NEXT = driver
						.findElementByClassName("android.widget.ImageButton");

		NEXT.click();
		log.info("Clicked on NEXT");

		Thread.sleep(3000);
		
		//Click Ok on send verification code confirmation message

		WebElement OK = driver.findElementByClassName("android.widget.Button");
		OK = driver.findElement(By.name("OK"));
		OK.click();
		log.info("Click on OK");

		Thread.sleep(5000);
	}
	
	@Given("I have entered my HVC code")
	public void enterHvc() throws Exception {
		
		final String DATA_MSISDN = excel.readFromExcel("TestAccounts", 2, 1);
	

		final JDBC jdbcObject = new JDBC();
		jdbcObject.jdbcConnection();
		log.info("Connected to DB");
		Thread.sleep(2000);

		final String number = "1" + DATA_MSISDN;
		final long l = Long.parseLong(number);

		final String hvc = jdbcObject.getHVC(l);
		System.out.println(hvc);
		log.info("Got the hvc value from DB :" + hvc);

		final WebElement HVC = driver
				.findElementByClassName("android.widget.EditText");
		HVC.click();
		HVC.sendKeys(hvc);
		Thread.sleep(1000);

		// Click on NEXT
				final WebElement NEXT = driver
						.findElementByClassName("android.widget.ImageButton");

		NEXT.click();
		Thread.sleep(6000);
	}
	
	@Given("I have entered the security questions")
	public void selectSecurityQuestions() throws Exception {
		
		final String DATA_SANSWER1 = excel.readFromExcel("TestData", 3, 1);
		final String DATA_SANSWER2 = excel.readFromExcel("TestData", 4, 1);
		final String DATA_SANSWER3 = excel.readFromExcel("TestData", 5, 1);

		driver.findElementsByClassName("android.widget.TextView");
		
		// Select security questions

		final WebElement SEC_QUES1 = driver.findElement(By
				.name("What is your mother's maiden name?"));
		SEC_QUES1.click();

		Thread.sleep(2000);

		final WebElement SEC_QUES2 = driver.findElement(By
				.name("What is your place of birth?"));
		SEC_QUES2.click();

		Thread.sleep(2000);

		final WebElement SEC_QUES3 = driver.findElement(By
				.name("What was your first pet's name?"));
		SEC_QUES3.click();

		Thread.sleep(2000);

		// Click on NEXT
				final WebElement NEXT = driver
						.findElementByClassName("android.widget.ImageButton");

		NEXT.click();

		Thread.sleep(4000);
		
	
		
		//Enter Security answers

		final List<WebElement> answers = driver
				.findElementsByClassName("android.widget.EditText");
		answers.get(0).sendKeys(DATA_SANSWER1);
		Thread.sleep(2000);
		driver.sendKeyEvent(AndroidKeyCode.ENTER);
		answers.get(1).sendKeys(DATA_SANSWER2);
		Thread.sleep(2000);
		driver.sendKeyEvent(AndroidKeyCode.ENTER);
		answers.get(2).sendKeys(DATA_SANSWER3);
		Thread.sleep(4000);

		// Click Next

		NEXT.click();
		
		log.info("Entered security answers");
		
	}
	
	@Given("I skip entering card details")
	public void skipAddCard() throws Exception {
		
		//Skip Add card

		 Thread.sleep(2000);
		 driver.findElementByClassName("android.widget.Button");
		 element = driver.findElement(By.name("Skip This Step"));
		 element.click();
	}
	
	
		 
	@When("I verify email and login")	
	public void verifyEmail() throws Exception {
		
		final String DATA_EMAIL = excel.readFromExcel("TestAccounts", 1, 1);
		
		
		//Email verification

		final JDBC jdbcObjectemail = new JDBC();
		jdbcObjectemail.jdbcConnection();
		Thread.sleep(2000);

		final String verfc = jdbcObjectemail.getverificationcode(DATA_EMAIL);
		System.out.println(verfc);

		final String NEW_DATA_EMAIL = DATA_EMAIL.replace("@", "%40");
		final String url = ConfigurationManager.getString("VERIFYEMAIL_URL")
				+ "?code=" + verfc + "&email=" + NEW_DATA_EMAIL
				+ "&appId=1000001" + "&USER_LOCALE=en_US";

		System.out.println(url);

		WebDriver firefox = new FirefoxDriver();
		firefox.get(url);
		Thread.sleep(3000);
		firefox.close();

		// Click on NEXT
				final WebElement NEXT = driver
						.findElementByClassName("android.widget.ImageButton");
		NEXT.click();

		Thread.sleep(10000);
	}
	
	@Then("I successfully finish enrolment process")
	public void enrolled() throws Exception {
	
		 
		log.info("Email verified and logged in");
	}
	
	@AfterScenario
	public void signOff() throws Exception{
		//Signing out
				wait.until(ExpectedConditions.elementToBeClickable(By.name("XYZ Financial")));
				driver.sendKeyEvent(AndroidKeyCode.BACK);
				
				//Click yes on sign out confirmation
				Thread.sleep(2000);

				WebElement Yes = driver.findElementByClassName("android.widget.Button");
				Yes = driver.findElement(By.name("Yes"));
				Yes.click();
				Thread.sleep(5000);
				
	}
	
	@AfterStories
	public void cleanup() throws Exception{
		
	 // Clean up
		
		Thread.sleep(5000);
		driver.quit();
		System.out.println("Over");

	}
	
	
	
	@Given("I have already enroled")
	public void alreadyenrolled() throws Exception {
		
		log.info("I am in already enrolled");
		
		
	}
	
	@Given("I have relaunched the application")
	public void relaunch() throws Exception {
		
			//Following will work after Appium 1.3 launch
//		driver.launchApp();
		
		log.info("App launched");
		
		WebElement launch = driver.findElementByClassName("android.widget.Button");
		if (launch.getAttribute("text") == "Enroll in Mobile Service") {
			
			final String DATA_EMAIL = excel.readFromExcel("TestAccounts", 1, 1);
			final String DATA_PASSCODE = excel.readFromExcel("TestData", 1, 1);
			
			System.out.println("Its a new launch");
			List<WebElement> element = driver
					.findElementsByClassName("android.widget.Button");
			for (final WebElement ele : element) {
				if (ele.getText().equals("Already Enrolled?")) {
					ele.click();
				}
			}

			WebElement EMAIL = driver.findElement(By.name("john.smith@mail.com"));
			EMAIL.click();
			EMAIL.sendKeys(DATA_EMAIL);
			log.info(DATA_EMAIL + "is entered for " + EMAIL);
			Thread.sleep(1000);
			driver.sendKeyEvent(AndroidKeyCode.ENTER);

			Thread.sleep(3000);
			final List<WebElement> ele = driver
					.findElementsByClassName("android.widget.EditText");
			ele.get(ele.size() - 1).sendKeys(DATA_PASSCODE);
			log.info(DATA_PASSCODE + "is entered for " + ele.get(ele.size() - 1));
			Thread.sleep(3000);

			Thread.sleep(3000);

			final WebElement NEXT1 = driver
					.findElementByClassName("android.widget.ImageButton");

			NEXT1.click();

			Thread.sleep(6000);

			final JDBC jdbcObjectemail = new JDBC();
			jdbcObjectemail.jdbcConnection();
			Thread.sleep(2000);

			final String verfc = jdbcObjectemail.getverificationcode(DATA_EMAIL);
			System.out.println(verfc);

			WebElement element1 = driver
					.findElementByClassName("android.widget.EditText");

			element1.click();
			element1.sendKeys(verfc);

			final WebElement NEXT2 = driver
					.findElementByClassName("android.widget.ImageButton");

			NEXT2.click();

		} else if(launch.getAttribute("text") == "Sign In") { 
			System.out.println("Enrollment done on this device");
		}
				
	}

	
	@When("I enter the passcode")
	
	public void login() throws Exception{
		
		//Enter Passcode
		
		final String DATA_PASSCODE = excel.readFromExcel("TestData", 1, 1);
		
		final WebElement passcode = driver
				.findElementByClassName("android.widget.EditText");
		passcode.click();
		passcode.sendKeys(DATA_PASSCODE);
		
	}
	
	@Then("I login to the app successfully")
	public void loggedIn() throws Exception{
		
		//Login
		
		WebElement SignIn = driver.findElement(By.name("Sign In"));
		SignIn.click();
		
		Thread.sleep(10000);
				
	}
	
	@Given("I have launched and login to the application")
	public void launchlogin() throws Exception {
		
		//Launch application and login
		
		relaunch();
		login();
		loggedIn();
			
	}
	
	@When("I enter the card details")
	public void enterCardDetails() throws Exception {
		
		//Get Card data from Excel
		
		final String CARD_NUMBER = excel.readFromExcel("CardData", 2, 1);
		final String CARD_NAME = excel.readFromExcel("CardData", 3, 1);
		final String EXP_MONTH = excel.readFromExcel("CardData", 4, 1);
		final String EXP_YEAR = excel.readFromExcel("CardData", 5, 1);
		final String SEC_CODE = excel.readFromExcel("CardData", 6, 1);
		final String ZIP_CODE = excel.readFromExcel("CardData", 7, 1);
		final String ADDRESS = excel.readFromExcel("CardData", 8, 1);
		final String STATE = excel.readFromExcel("CardData", 9, 1);
		final String CITY = excel.readFromExcel("CardData", 10, 1);
		
		//Click Addcard
		
		WebElement ADDCARD = driver.findElement(By.name("Add Card"));
		ADDCARD.click();
		
		//Enter Card details
		
		WebElement CARD_NICKNAME = driver
				.findElementByClassName("android.widget.EditText");
		CARD_NICKNAME = driver.findElement(By.name("Card nickname"));
		CARD_NICKNAME.click();
		CARD_NICKNAME.sendKeys(CARD_NAME);

		WebElement CARD_NUM = driver
				.findElementByClassName("android.widget.EditText");
		CARD_NUM = driver.findElement(By.name("Card number"));
		CARD_NUM.click();
		CARD_NUM.sendKeys(CARD_NUMBER);

		WebElement CARD_DATE = driver
				.findElementByClassName("android.widget.EditText");
		CARD_DATE = driver.findElement(By.name("MM/YY"));
		CARD_DATE.click();

		WebElement eles = driver.findElement(By
				.className("android.widget.LinearLayout"));
		WebElement CANCEL = driver.findElement(By.name("Cancel"));
		CANCEL.click();
		eles.sendKeys(EXP_MONTH + "/" + EXP_YEAR);

		Thread.sleep(1000);
	

		final List<WebElement> cvv = driver
				.findElementsByClassName("android.widget.EditText");
		cvv.get(cvv.size() - 5).sendKeys(SEC_CODE);
		

		driver.sendKeyEvent(AndroidKeyCode.ENTER);

		WebElement ADDR = driver.findElement(By.name("Billing address"));
		ADDR.sendKeys(ADDRESS);

		driver.sendKeyEvent(AndroidKeyCode.ENTER);

		WebElement CITY1 = driver.findElement(By.name("City"));
		CITY1.sendKeys(CITY);

		driver.sendKeyEvent(AndroidKeyCode.BACK);

		WebElement state = driver.findElement(By.name("State"));
		state.click();

		WebElement state1 = driver.findElement(By
				.className("android.widget.LinearLayout"));
		CANCEL.click();
		state1.sendKeys(STATE);

		Thread.sleep(3000);

		WebElement zip = driver.findElement(By.name("5 digit code"));
		zip.sendKeys(ZIP_CODE);
		
		//Click Add Card

		final WebElement ADD_CARD = driver
				.findElementByClassName("android.widget.Button");

		ADD_CARD.click();

		Thread.sleep(3000);
		
	}
	
	 @Then("I should be able to add card successfully")
	 
	 public void addCardConfirm() throws Exception {

		// Click OK on Card confirmation dialog
		WebElement OK = driver.findElementByClassName("android.widget.Button");
		OK = driver.findElement(By.name("OK"));
		OK.click();
		
		log.info("Card has been added");
		
		wait.until(ExpectedConditions.elementToBeClickable(By.name("XYZ Financial")));
	}
	
	
}
