package tasaDeCambio;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import utilidades.UsefulMethodsWF;

public class casosNegativos {

	WebDriver driver;
	WebDriverWait wait;
	File fl;
	Workbook wb;
	Sheet sh;
	
	@Test (priority = 1)
	public void enBlanco() throws BiffException, IOException {
		
		//Setting Chrome browser
		System.setProperty("webdriver.chrome.driver", "ChromeDriver\\chromedriver.exe");
		ChromeOptions op = new ChromeOptions();
		op.addArguments("--start-maximized");
		driver = new ChromeDriver(op);
		wait = new WebDriverWait(driver, 15);
		
		//Getting parameters
		fl = new File("Parametros\\TasaDeCambio\\Parametros.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("NegativeCases");
		String url = sh.getCell(1, 2).getContents();
		
		driver.get(url);
		
		String adminUser = sh.getCell(1, 5).getContents();
		String adminPass = sh.getCell(2, 5).getContents();
		String testUser = sh.getCell(1, 6).getContents();
		String testPass = sh.getCell(2, 6).getContents();
		String role = sh.getCell(3, 6).getContents();
		String functionality = sh.getCell(4, 6).getContents();
		String menuBehaviour = sh.getCell(5, 6).getContents();
		
		//UsefulMethodsWF.createWFTestUser(adminUser, adminPass, testUser, testPass, role, functionality, menuBehaviour, driver);
		UsefulMethodsWF.loginWF(driver, testUser, testPass);
		
		//Navigates to Tasa de Cambio 
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='verticalmenu z-div']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElements(By.xpath("//*[@class='z-toolbarbutton-cnt']")).get(4))).click();
		
		//Clic on Modificar of Dollar exchange rate
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("z-button-cm"))).get(0).click();
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("z-textbox"))).get(0).clear();
		
		//Get negative input
		wait.until(ExpectedConditions.presenceOfElementLocated(By.className("z-textbox z-textbox-focus"))).sendKeys();
	}
}
