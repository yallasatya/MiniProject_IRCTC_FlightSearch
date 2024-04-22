package irctc_flight_search;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import irctc_flight_search.DriverSetup;

public class TestAutomation {
	
	public static WebDriver driver;
	public static String origin, selectOrigin;
	public static String destination, selectDestination;
	public static String selectClass;
	public static String date="";
	
	//Method to call Driver and to select between driver
	public static void getDriver() {
		Scanner scanner = new Scanner(System.in);
		String preferredBrowser;
		System.out.println("Enter your preferred Browser : Chrome/Edge");
		preferredBrowser = scanner.next();
		scanner.close();
		
		if(preferredBrowser.equalsIgnoreCase("chrome")) {
			//Calling getChromedriver method
			driver=DriverSetup.getChromeDriver();
			
		}
		else if(preferredBrowser.equalsIgnoreCase("edge")) {
			//Calling getEdgeDriver Method
			driver = DriverSetup.getEdgeDriver();
		}
	}

	//Method to verify that Appropriate Website is Loading or not
	public static void verifyApplication() throws InterruptedException {
		try {
			String title = driver.getTitle();
			Thread.sleep(2000);
			if (!title.contains("Air Ticket Booking")) {
				driver.navigate().refresh();
				Thread.sleep(2000);
			}
			else if(title.contains("Air Ticket Booking")) {
				System.out.println("The Website Launched is IRCTC AIR TICKET BOOKING and it is verified");
			}
			else {
				System.out.println("The Website Launched is inappropriate");
			}
		}catch(InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	//Method to choose option in Origin
	public static void originStation() throws InterruptedException{
		//finding the WebElement for Origin Station
		driver.findElement(By.id("stationFrom")).sendKeys(origin);
		Thread.sleep(5000);
		// Creating List of Origin Cities which are matching with the Origin Station
		List<WebElement> originList = driver.findElements(By.xpath("//*[contains(text(), '"+origin+"')]"));
		//iterating the Origin cities list and selecting the appropriate Origin city
		for (WebElement webElement : originList) {
			if (webElement.getText().contains(selectOrigin)) {
				webElement.click();
				break;
			}
		}
	}
	
	// Method to choose option in Destination 
	public static void destinationStation() throws InterruptedException{
		//finding the WebElement for destination Station
		driver.findElement(By.id("stationTo")).sendKeys(destination);
		Thread.sleep(5000);
		// Creating List of Destination Cities which are matching with the Destination Station
		List<WebElement> destinatoinList = driver.findElements(By.xpath("//*[contains(text(), '"+destination+"')]"));
		//iterating the Destination cities list and selecting the appropriate Destination city
		for (WebElement webElement : destinatoinList ) {
			if (webElement.getText().contains(selectDestination)) {
				webElement.click();
				break;
			}
		}
	}
	
	//Method to select Today's date
	public static void datePicker() throws InterruptedException {
		//Finding WebEklement for date picker 
		driver.findElement(By.id("originDate")).click();
		driver.findElement(By.xpath("//span[@class='act active-red']")).click();
		Thread.sleep(1000);
	}
	
	//Method to choose travel class type as business
	public static void classSelector() throws InterruptedException {
		driver.findElement(By.id("noOfpaxEtc")).click();
		Thread.sleep(1000);
		//Selecting the appropriate class(Business)
		Select select = new Select(driver.findElement(By.id("travelClass")));
		select.selectByValue(selectClass);
		Thread.sleep(1000);
	}
	//Method click Search button
	public static void clickAction() throws InterruptedException {
		driver.findElement(By.xpath("//button[@class='btn btn-md yellow-gradient home-btn']")).click();
		Thread.sleep(20000);
	}
	
	//Method to capture ScreenShot and save it in ScreenShot Folder
	public static void screenShot() {
		TakesScreenshot ts = ((TakesScreenshot) driver);
		File sourceFile = ts.getScreenshotAs(OutputType.FILE);
		File destinationFile = new File("src/ScreenShot/irctc " + selectOrigin + "-" + selectDestination + ".png");
		try {
			FileUtils.copyFile(sourceFile, destinationFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Method to verifying that City name and Date are matching with the previous page City name and Date values or not
	public static void verifyingCityAndDateValues() {
		List<WebElement> Results = driver.findElements(By.className("right-searchbarbtm-in"));
		int numberOfResults = Results.size();
		boolean result = false;
		try {
			//finding WebElement for the today's date 
			try {
				date = driver.findElement(By.cssSelector("a[href*='onewaytrip'] span")).getText();
			}catch(NoSuchElementException n) {
				driver.findElement(By.id("originDate")).click();
				date = driver.findElement(By.xpath("//span[@class='act active-red']")).getText();
				driver.findElement(By.xpath("//span[@class='act active-red']")).click();
				
			}
			Thread.sleep(2000);
			for (int i = 3; i < numberOfResults+3; i++) {
				// finding WebElement for the city names
				List<WebElement> origin= driver.findElements(By.xpath("//*[@class='right-searchbarbtm-in']/div[2]/span"));
				List<WebElement> dest = driver.findElements(By.xpath("//*[@class='right-searchbarbtm-in']/div[3]/span"));
				//checking whether the city and date of results are matching with the previous web page 
				if(origin.get(i-3).getText().contains(selectOrigin) && dest.get(i-3).getText().contains(selectDestination) && (LocalDate.now().toString().split("-")[2].contains(date) || date.contains(LocalDate.now().toString().split("-")[2]))) {
					result = true;
					
				}else {
					result = false;
					break;
				}
			}
			if (result == true)	System.out.println("City name and Date are matching with the previous page City name and Date values");
			else System.out.println("City name and Date are not matching with the previous page City name and Date values");

	
		}catch(Exception e){
			e.printStackTrace();
			
		}
		
	}
	
	//Method to print the resultant Flight Details (Flight Number and Flight name in the console
	public static void printResultOnConsole() {
		// Creating the list of flights for the route
		List<WebElement> Results = driver.findElements(By.className("right-searchbarbtm-in"));
		int numberOfResults = Results.size();
		
		// Checking whether the number of flights more than 0 ,if true printing them on console
		if (numberOfResults > 0) {
			System.out.println("\nFlights available from " + selectOrigin + " to " + selectDestination + ":\n");
			List<WebElement> flightNames = driver.findElements(By.xpath("//*[@class='right-searchbarbtm-in']/div[1]//b"));
			List<WebElement> flightNumbers = driver.findElements(By.xpath("//*[@class='right-searchbarbtm-in']/div[1]//span"));
			int num = flightNames.size();
			for(int i=0; i<num; i++) {
				String flightName = flightNames.get(i).getText();
				String flightNumber = flightNumbers.get(i).getText();
				
				System.out.println("Flight Number : "+flightNumber + " --> " +"Flight Name :" +flightName);	
			}
		//if number flihgts are 0 than printing no flights
		}else if (numberOfResults == 0) {
			String flightText = driver.findElement(By.cssSelector(".right-searchbarbtm > p")).getText();
			System.out.println(flightText + "\nPlease try tomorrow!");
			System.out.println("Thank You");	
		}
	}
	
	
	// Method to write resultant flight number and Flight name into Excelsheet-'Test_results'.
	public static void printResultInExcel() {
		List<WebElement> Results = driver.findElements(By.className("right-searchbarbtm-in"));
		int numberOfResults = Results.size();
		
		ExcelReadWrite excel = new ExcelReadWrite();
		int rownum = 1;
		
		try {
			// calling clearExcel method to clear the previous data in the Test_Result Excel sheet
			excel.clearExcel();
			excel.setExcel("Test_Results",0,0,"FLIGHT NUMBER");
			excel.setExcel("Test_Results",0,1,"FLIGHT_NAME");
			if (numberOfResults > 0) {
				List<WebElement> flightNames = driver.findElements(By.xpath("//*[@class='right-searchbarbtm-in']/div[1]//b"));
				List<WebElement> flightNumbers = driver.findElements(By.xpath("//*[@class='right-searchbarbtm-in']/div[1]//span"));
				int num = flightNames.size();
				for(int i=0; i<num; i++) {
					String flightName = flightNames.get(i).getText();
					String flightNumber = flightNumbers.get(i).getText();

					// for printing flight details(Flight Number, flight Name) into ExcelSheet
					try {
						excel.setExcel("Test_Results",rownum,0,flightNumber);
						excel.setExcel("Test_Results",rownum,1,flightName);
					}catch(IOException e) {
						e.printStackTrace();
					}
					rownum++;
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	// Method to close the Website.
	public static void closeWebsite() throws InterruptedException {
		Thread.sleep(20000);
		driver.quit();
	}
	
	
	

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ExcelReadWrite read = new ExcelReadWrite();
		// Reading Data from the Excelsheet - 'Test_Data'
		origin = read.readExcel("Test_Data", 0, 0);
		destination = read.readExcel( "Test_Data", 0, 1);
		selectOrigin= read.readExcel( "Test_Data", 0, 2);
		selectDestination = read.readExcel( "Test_Data", 0, 3);
		selectClass= read.readExcel("Test_Data", 0, 4);
		
		//Calling getDriver() method to launch the Browser
		getDriver();
		// Calling verifyApplication() method to verify appropriate application is launched or not
		verifyApplication();
		//Calling originStation() method to select the origin station
		originStation();
		//Calling destination Station() method to select the Destination station
		destinationStation();
		//Calling datePicker() method to select today's date 
		datePicker();
		//Calling classSelector() method to select the appropriate class (Business)
		classSelector();
		//Calling clickAction() method to click on search button
		clickAction();
		// Calling screenShot() method to take screenshot of the results
		screenShot();
		//Calling verifyingCityAndDateValues() method to verify city names and date are matching with previous page city name and date values.
		verifyingCityAndDateValues();
		//calling printResultOnConsole() method to print the resultant flight details(Flight Number, Flight Name) into the console 
		printResultOnConsole();
		//calling printResultInExcel() method to write the resultant flight details(Flight Number, Flight Name) into the Test_Results Excel sheet
		printResultInExcel();
		//calling closeWebsite method to close the Website
		closeWebsite();

	}
}
