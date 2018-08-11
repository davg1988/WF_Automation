package claveDia;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import utilidades.UsefulMethodsWF;

public class ClaveInvalida {

	WebDriver driver;
	WebDriverWait wait;
	Workbook wb;
	Sheet sh;
	File fl;

	@Test (priority= 1, groups= {"FullAutomated"})
	public void launchWF() throws BiffException, IOException {

		//Setting browser configurations
		System.setProperty("webdriver.chrome.driver", "ChromeDriver\\chromedriver.exe");
		ChromeOptions op = new ChromeOptions();
		op.addArguments("--start-maximized");
		driver = new ChromeDriver(op);
		wait = new WebDriverWait(driver, 40);
		
		//Setting data provider
		fl = new File("Parametros\\ClaveDia\\Parametros.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("ClaveInvalida");
		
		//Getting url from Excel file
		String url = sh.getCell(1, 2).getContents();
		driver.get(url);

		//Getting parameters from Excel file
		String adminUser = sh.getCell(1,5).getContents();
		String adminPass = sh.getCell(2,5).getContents();
		String user = sh.getCell(1,6).getContents();
		String pass = sh.getCell(2,6).getContents();
		String role = sh.getCell(3,6).getContents();
		String functionality = sh.getCell(4,6).getContents();
		String menuBehaviour = sh.getCell(5,6).getContents();

		UsefulMethodsWF.createWFTestUser(adminUser, adminPass, user, pass, role, functionality, menuBehaviour, driver);
	}

	@Test (priority= 2, groups= {"FullAutomated"})
	public void insertInvalidKey() {

		//Getting parameters to login
		String user = sh.getCell(1,6).getContents();
		String pass = sh.getCell(2,6).getContents();

		// Login as a user
		UsefulMethodsWF.loginWF(driver, user, pass);

		// Go to Clave Día
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='verticalmenu z-div']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='z-window-embedded-cnt-noborder']"
				+ "//*[@class='z-groupbox-3d-header']//*[@class='z-caption-l' and text()='Fin de Día']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//*[@class='z-toolbarbutton-cnt'])[12]"))).click();
		
		//Insert invalid input
		for (int j = 9; j < sh.getRows(); j++) {
			System.out.println("Loop "+j);
			String invalid_input = sh.getCell(1, j).getContents();
			if (j==9) {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-textbox']"))).clear();
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-textbox z-textbox-focus']"))).sendKeys(invalid_input);
			} else {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-textbox']"))).clear();
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-textbox']"))).sendKeys(invalid_input);
			}
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@class='z-button-cm']"))).get(0).click();
			Assert.assertEquals(wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-messagebox-window z-window-highlighted z-window-highlighted-shadow']//*[@class='z-label']"))).getText(), "El valor ingresado no cumple la regla de validación regex: [0-9]{14}");
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-messagebox-window z-window-highlighted z-window-highlighted-shadow']//*[@class='z-messagebox-btn z-button-os']"))).click();
		}
		
		//Logout
		UsefulMethodsWF.logoutWF(driver);
	}
	
	@Test (priority= 3, groups= {"FullAutomated"})
	public void closeWF() {
		
		//Getting parameters from Excel file
		String adminUser = sh.getCell(1,5).getContents();
		String adminPass = sh.getCell(2,5).getContents();
		String user = sh.getCell(1,6).getContents();
		
		UsefulMethodsWF.deleteWFTestUser(adminUser, adminPass, user, driver);
	}
}
