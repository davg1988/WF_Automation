package utilidades;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class UsefulMethodsWF {

	public static void logoutWF(WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, 45);
		WebElement btnSalir = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@title='Salir del Programa']")));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@class='z-modal-mask']")));
		btnSalir.click();
		List<WebElement> btnOK = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-window-highlighted-cnt']//*[@class='z-messagebox-btn z-button-os']"), 0));
		btnOK.get(0).click();
	}
	
	public static void loginWF (WebDriver driver, String user, String pass) {
		
		WebDriverWait wait = new WebDriverWait(driver, 45);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@title='Introduzca el Nombre de Usuario']"))).sendKeys(user);
		driver.findElement(By.xpath("//*[@class='textlogin z-textbox' and @title='Introduzca la Contraseña del Usuario']")).sendKeys(pass);
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();
	}
	
	public static void createWFTestUser(WebDriver driver) throws BiffException, IOException {
		
		// Getting parameters from excel file
		File fl = new File("Parametros\\\\EnvironmentParameters.xls");
		Workbook wb = Workbook.getWorkbook(fl);
		Sheet sh = wb.getSheet("Environment");
		
		WebDriverWait wait = new WebDriverWait(driver,45);
		
		// Getting admin user and password and login
		String adminUser = sh.getCell(1, 7).getContents();
		String adminPass = sh.getCell(1, 8).getContents();
		UsefulMethodsWF.loginWF(driver, adminUser, adminPass);
		
		// Click on Gestion Login
		wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-toolbarbutton-cnt']"),1)).get(1).click();
		
		// Click on Insertar button
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[@class='z-button-cm' and text()=' Insertar']"))).click();

		// Data to create the WebFront user to be used to execute the tests
		String testUser = sh.getCell(1, 10).getContents();
		String testPass = sh.getCell(1, 11).getContents();
		String role = sh.getCell(1, 12).getContents();
		String functionality = sh.getCell(1, 13).getContents();
		String menuBehaviour = sh.getCell(1, 14).getContents();
		
		List<WebElement> txtFields = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@class='z-textbox']"), 2));
		txtFields.get(1).sendKeys(testUser);
		txtFields.get(2).sendKeys(testPass);
		Select roleList = new Select(driver.findElements(By.xpath("//*[@class='z-selectbox']")).get(0));
		roleList.selectByVisibleText(role);
		Select functionalityList = new Select(driver.findElements(By.xpath("//*[@class='z-selectbox']")).get(1));
		functionalityList.selectByVisibleText(functionality);
		Select selectMenuBehaviour = new Select(driver.findElements(By.xpath("//*[@class='z-selectbox']")).get(2));
		selectMenuBehaviour.selectByVisibleText(menuBehaviour);
		driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();
		
		//Logout
		UsefulMethodsWF.logoutWF(driver);
	}

	public static void deleteWFTestUser(WebDriver driver) throws BiffException, IOException {
		
		// Getting parameters from excel file
		File fl = new File("Parametros\\\\EnvironmentParameters.xls");
		Workbook wb = Workbook.getWorkbook(fl);
		Sheet sh = wb.getSheet("Environment");
		
		// Getting admin user and password and login
		String adminUser = sh.getCell(1, 6).getContents();
		String adminPass = sh.getCell(1, 7).getContents();
		
		// WebFront user to be deleted
		String testUser = sh.getCell(1, 9).getContents();
		
		WebDriverWait wait = new WebDriverWait(driver,45);
		
		UsefulMethodsWF.loginWF(driver, adminUser, adminPass);
		
		//Go To Gestion Login
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@class='z-window-embedded']//*[@class='z-toolbarbutton-cnt']"))).get(1).click();
		driver.findElement(By.xpath("//*[@class='titlebar nomargin_left z-hbox']")).click();
		
		//Find test user
		List<WebElement> white_rows = driver.findElements(By.xpath("//*[@class='z-row']//*[@class='z-listcell-cnt z-overflow-hidden']"));
		List<WebElement> gray_rows = driver.findElements(By.xpath("//*[@class='z-row z-grid-odd']//*[@class='z-listcell-cnt z-overflow-hidden']"));
		int located_index = 0;
		int i = 0;
		while(i<white_rows.size()) {
			if(white_rows.get(i).getText().equals(testUser)) {
				located_index = i*2;
				i = white_rows.size();
			}				
		}
		int j = 0;
		while(j<gray_rows.size()) {
			if(gray_rows.get(i).getText().equals(testUser)) {
				located_index = j+j+1;
				j = gray_rows.size();
			}
		}
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@class='z-button-cm' and text()=' Eliminar']"))).get(located_index).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-window-highlighted-cnt']//*[@class='z-messagebox-btn z-button-os' and text()='Yes']"))).click();
		UsefulMethodsWF.logoutWF(driver);
		driver.close();
	}

	public static void loginWFTestUser(WebDriver driver) throws BiffException, IOException {
		
		// Getting parameters from excel file
		File fl = new File("Parametros\\\\EnvironmentParameters.xls");
		Workbook wb = Workbook.getWorkbook(fl);
		Sheet sh = wb.getSheet("Environment");
		
		// Getting admin user and password and login
		String user = sh.getCell(1, 9).getContents();
		String pass = sh.getCell(1, 10).getContents();
		
		loginWF(driver, user, pass);
	}
	
	public static WebDriver setUpWf() throws BiffException, IOException {
		
		//Getting parameters of the test environment
		File fl = new File("Parametros\\EnvironmentParameters.xls");
		Workbook wb = Workbook.getWorkbook(fl);
		Sheet sh = wb.getSheet("Environment");
		
		//Setting launch of the browser
		System.setProperty("webdriver.chrome.driver", "ChromeDriver\\chromedriver.exe");
		ChromeOptions op = new ChromeOptions();
		op.addArguments("--start-maximized");
		WebDriver driver = new ChromeDriver(op);
		String url = sh.getCell(1,3).getContents();
		driver.get(url);
		
		//Setting the IP for the clients of Rest Service
		Environment.setEnv_ip(url);
		
		return driver;
	}

	public static String getLongLogin() throws BiffException, IOException {
		
		//Getting parameters of the test environment
		File fl = new File("Parametros\\EnvironmentParameters.xls");
		Workbook wb = Workbook.getWorkbook(fl);
		Sheet sh = wb.getSheet("Environment");
		
		//Getting the long of the personnel number configured in UserWatcher.properties
		String long_login = sh.getCell(1, 4).getContents();
		
		return long_login;
	}
	
	public static String getVisibleDigits() throws BiffException, IOException {
		
		//Getting parameters of the test environment
		File fl = new File("Parametros\\EnvironmentParameters.xls");
		Workbook wb = Workbook.getWorkbook(fl);
		Sheet sh = wb.getSheet("Environment");
		
		//Getting the long of the personnel number configured in UserWatcher.properties
		String visible_digits = sh.getCell(1, 5).getContents();
		
		return visible_digits;
	}

	public static void loginAdmin(WebDriver driver) throws BiffException, IOException {
		
		// Getting parameters from excel file
		File fl = new File("Parametros\\\\EnvironmentParameters.xls");
		Workbook wb = Workbook.getWorkbook(fl);
		Sheet sh = wb.getSheet("Environment");
	
		String adminUser = sh.getCell(1, 7).getContents();
		String adminPass = sh.getCell(1, 8).getContents();
		UsefulMethodsWF.loginWF(driver, adminUser, adminPass);
	}
}
