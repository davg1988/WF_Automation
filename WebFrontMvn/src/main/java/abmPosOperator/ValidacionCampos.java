package abmPosOperator;

import java.io.File;
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import junit.framework.Assert;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import utilidades.Environment;
import utilidades.ScreenShot;
import utilidades.UsefulMethodsWF;
import webFrontCommonUtils.RServiceClientFactory;

public class ValidacionCampos {

	public WebDriver driver;
	public WebDriverWait wait;
	public Sheet sh;
	public Workbook wb;
	public File fl;
	public RServiceClientFactory factory;
	
	@Test (priority=1, groups = {"FullAutomated"})
	public void launchWF() throws BiffException, IOException {
		

		//Setting driver
		driver = UsefulMethodsWF.setUpWf();
		wait = new WebDriverWait(driver,20);
		factory = new RServiceClientFactory();
		
		//Extracting parameters from Excel file
		fl = new File("Parametros\\AbmOperadoresPos\\Parametros.xls");
		wb = Workbook.getWorkbook(fl);
		sh = wb.getSheet("ValidarCampos");
		
		//Getting parameters from excel file
		String adminUser = sh.getCell(1,5).getContents();
		String adminPass = sh.getCell(2,5).getContents();
		String user = sh.getCell(1,6).getContents();
		String pass = sh.getCell(2,6).getContents();
		String role = sh.getCell(3,6).getContents();
		String functionality = sh.getCell(4,6).getContents();
		String menuBehaviour = sh.getCell(5,6).getContents();
			
		UsefulMethodsWF.createWFTestUser(adminUser, adminPass, user, pass, role, functionality, menuBehaviour, driver);
		
		// Login with the test user
		UsefulMethodsWF.loginWF(driver, user, pass);
		
		//Navigate to mantenimiento de usuarios
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='verticalmenu z-div']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElements(By.xpath("//*[@class='z-toolbarbutton-cnt']")).get(0))).click();
	}
	
	@Test (priority=4, groups = {"FullAutomated"})
	public void validateFields() throws Exception {

		int count = 1;
		for (int i = 10; i < sh.getRows(); i++) {
		
			// Obtain Parameters From Excel File
			String role, username, login, tc;
			role = sh.getCell(1,i).getContents();
			username = sh.getCell(2,i).getContents();
			login = sh.getCell(3,i).getContents();
			tc = sh.getCell(0,i).getContents();
			
			//Insertion of new user
			//Click on Insertar button
			wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.className("z-button-cm"), 2));
			driver.findElements(By.className("z-button-cm")).get(1).click();
			
			//Selecting the role of the operator
			wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//select[@class='z-selectbox']"), 2));
			Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@class='z-selectbox' and not (@disabled='disabled')]"))));
			select.selectByVisibleText(role);
			
			//Filling the name field
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-textbox' and @maxlength='20']"))).sendKeys(username);
			
			//Filling the login field
			driver.findElement(By.xpath("//*[@class='z-longbox']")).clear();
			driver.findElement(By.xpath("//*[@class='z-longbox z-longbox-focus']")).sendKeys(login);
			
			if(role.equals("Supervisor")) {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-intbox' and @maxlength='3']"))).clear();
				String level = sh.getCell(4, i).getContents();
				driver.findElement(By.xpath("//*[@class='z-intbox z-intbox-focus']")).sendKeys(level);
			}
			
			//Click on Confirmar button
			driver.findElement(By.xpath("//*[@class='z-button-cm' and text()=' Confirmar']")).click();
			String window_message = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath
					("//*[@class='z-messagebox-window z-window-highlighted z-window-highlighted-shadow']"
							+ "//*[@class='z-window-highlighted-header z-window-highlighted-header-move']"))).getText();
			Assert.assertEquals("Error", window_message);
			
			//Click on OK
			ScreenShot.takeSnapShot(driver, "Evidencia\\AbmPosOperator\\ValidacionCampos\\"+ tc + count +".png");
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-messagebox-window z-window-highlighted z-window-highlighted-shadow']//*[@class='z-messagebox-btn z-button-os']"))).click();
			
			//Click on Cancelar
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("z-modal-mask")));
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='z-button-cm' and text()=' Cancelar']"))).click();
			count++;
			
			//Check that the fields for filling the information of the pos operator disappears
			//before inserting a new one
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@class='z-textbox' and @maxlength='20']")));
		}	
		UsefulMethodsWF.logoutWF(driver);
	}
	
	@Test (priority=9, groups = {"FullAutomated"})
	public void deleteTestUser() {
		
		//Login as an admin
		String user, pass;
		user = sh.getCell(1,5).getContents();
		pass = sh.getCell(2,5).getContents();
		String name = sh.getCell(1,6).getContents();
		UsefulMethodsWF.deleteWFTestUser(user, pass, name, driver);
	}

}
